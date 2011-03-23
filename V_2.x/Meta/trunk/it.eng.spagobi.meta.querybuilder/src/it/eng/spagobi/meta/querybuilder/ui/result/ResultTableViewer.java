package it.eng.spagobi.meta.querybuilder.ui.result;

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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResultTableViewer extends TableViewer {
	
	private ViewModelStructure modelStructure;
	private IDataSet dataSet;
	private ResultTableComparator comparator;
	private static final int defaultColumnWidth = 100;

	private static Logger logger = LoggerFactory.getLogger(ResultTableViewer.class);
	
	
	public ResultTableViewer(Group groupQueryResult, ViewModelStructure modelStructure){
		super(groupQueryResult, SWT.BORDER | SWT.FULL_SELECTION);
		this.modelStructure = modelStructure;
		getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setLabelProvider(new ResultTableLabelProvider());
		setContentProvider(new ResultTableContentProvider());
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(true);
		comparator = new ResultTableComparator();
		setComparator(comparator);
	}
	
	public void updateTable(int offset, int fetchSize, int maxResults){
		TableColumn[] columns = getTable().getColumns();
		if(columns!=null){
			for(int i=0; i<columns.length; i++){
				columns[i].dispose();
			}
		}
		Query query = QueryProvider.getQuery();
		this.dataSet =  QbeDatasetFactory.createDataSet(modelStructure.getDataSource().createStatement(query));
		loadFirstResultAndHeaders(offset, fetchSize, maxResults);
	}
	
	
	private void loadFirstResultAndHeaders(int offset, int fetchSize, int maxResults){
		logger.debug("IN");
		String name;
		Class type; 
		IDataStore dataStore = null;
		List<Map<String,Object>> headers = new ArrayList<Map<String,Object>>();
		try {
			dataSet.loadData(offset,fetchSize,maxResults);
			dataStore = dataSet.getDataStore();
			headers= DataStoreReader.getColumnMetaData(dataStore);
		} catch (Throwable e) {
			e.printStackTrace();
		}
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
		refresh();
		logger.debug("OUT");
	}
	
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
}
