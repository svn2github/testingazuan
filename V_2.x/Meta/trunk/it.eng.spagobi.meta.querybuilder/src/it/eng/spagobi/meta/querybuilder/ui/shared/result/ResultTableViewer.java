/**

 SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.querybuilder.ui.shared.result;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.QbeDatasetFactory;
import it.eng.spagobi.meta.querybuilder.model.QueryProvider;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ResultTableViewer extends TableViewer {
	
	private ViewModelStructure modelStructure;
	private IDataSet dataSet;
	private ResultTableComparator comparator;
	private static final int defaultColumnWidth = 100;
	private int offset=0;
	private int pageSize= 25;
	private int maxResults = 10000;
	private int resultSize= 0;
	private Label pages; //label for pagination

	private static Logger logger = LoggerFactory.getLogger(ResultTableViewer.class);
	
	/**
	 * Constructor
	 * @param groupQueryResult
	 * @param modelStructure
	 */
	public ResultTableViewer(Group groupQueryResult, ViewModelStructure modelStructure){
		super(groupQueryResult, SWT.BORDER | SWT.FULL_SELECTION);	
		this.modelStructure = modelStructure;
		getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setLabelProvider(new ResultTableLabelProvider());
		setContentProvider(new ResultTableContentProvider());
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(true);
		
		//COLUMN SORTING
		comparator = new ResultTableComparator();
		setComparator(comparator);

		//Pagination
		addPagination(groupQueryResult);


	}
	
	/**
	 * Add the pagination grid to the result page
	 * @param groupQueryResult
	 */
	public void addPagination(Group groupQueryResult){
	
		Composite bottonContainer = new Composite(groupQueryResult, SWT.NONE);
		//bottonContainer.setLayout(new GridLayout(3, true));
		bottonContainer.setLayout(new RowLayout());
		
		Button prevoiusButton = new Button(bottonContainer, SWT.PUSH);
		prevoiusButton.setText("<<");
		prevoiusButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				offset = offset-pageSize;
				if(offset<0){
					offset=0;
				}
				setInput(updateTableData());
			}
		});

		pages = new Label(bottonContainer,SWT.CENTER);
		pages.setLayoutData(new RowData(200,20));
		int end = offset+pageSize;
		if(end>resultSize){
			end = resultSize;
		}
		int maxResultSize = (resultSize>maxResults)? maxResults: resultSize;
		pages.setText(offset+" - "+end+" / "+maxResultSize);
		
		Button nextButton = new Button(bottonContainer, SWT.PUSH);
		nextButton.setText(">>");
		nextButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				int maxResultSize = (resultSize>maxResults)? maxResults: resultSize;
				offset = offset+pageSize;
				if(offset>=maxResultSize){
					offset=maxResultSize-pageSize;
					if(offset<0){
						offset=0;
					}
				}
				setInput(updateTableData());
			}
		});
	}
	
	/**
	 * Update the table: delete the columns,  reload the query, execute the query 
	 */
	public void updateTable(){
		String name;
		Class type; 
		//execute the query
		IDataStore dataStore =updateTableData();

		//delete the old columns
		TableColumn[] columns = getTable().getColumns();
		if(columns!=null){
			for(int i=0; i<columns.length; i++){
				columns[i].dispose();
			}
		}	
		
		//build the new columns
		logger.debug("Building the result table");
		List<Map<String,Object>> headers = new ArrayList<Map<String,Object>>();
		headers= DataStoreReader.getColumnMetaData(dataStore);
		for(int i =0; i<headers.size(); i++){
			final TableColumn column = new TableColumn(getTable(), SWT.NONE);
			name = (String) headers.get(i).get("title");
			type = (Class) headers.get(i).get("type");
			column.setWidth(defaultColumnWidth);
			column.setText(name);
			column.setResizable(true);
			column.setMoveable(true);
			column.addSelectionListener(getSelectionAdapter(column, i, type));
		}

		setInput(dataStore);
		logger.debug("Result table built");
	}
	
	/**
	 * Update the table: reload the query, execute the query 
	 */
	public IDataStore updateTableData(){
		IDataStore dataStore = null;
		Query query = QueryProvider.getQuery();
		if(!query.isEmpty()){
			logger.debug("Getting the dataset");
			this.dataSet =  QbeDatasetFactory.createDataSet(modelStructure.getDataSource().createStatement(query));
			logger.debug("Data set loaded.");
			logger.debug("Executing the query");
			dataSet.loadData(offset,pageSize,maxResults);
			dataStore = dataSet.getDataStore();
			logger.debug("Query executed");

			//Update variables for pagination
			resultSize = DataStoreReader.getMaxResult(dataStore);
			int end = offset+pageSize;
			if(end>resultSize){
				end = resultSize;
			}
			pages.setText(offset+" - "+end+" / "+resultSize);
		}else{
			logger.debug("The query is empty");
		}
		return dataStore;
	}

	
	/**
	 * For every column build a selection adapter (now we manage only the order)
	 * @param column TableColumn
	 * @param index index of the column
	 * @param type type of the data in the column
	 * @return
	 */
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index, final Class type) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index,type);
				int dir = getTable().getSortDirection();
				if (getTable().getSortColumn() == column) {
					dir = dir == SWT.UP ? SWT.DOWN : SWT.UP;
				} else {

					dir = SWT.DOWN;
				}
				getTable().setSortDirection(dir);
				getTable().setSortColumn(column);
				refresh();
			}
		};
		return selectionAdapter;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}
	
	
}
