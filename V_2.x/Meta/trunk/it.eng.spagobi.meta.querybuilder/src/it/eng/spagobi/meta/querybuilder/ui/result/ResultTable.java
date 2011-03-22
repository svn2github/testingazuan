package it.eng.spagobi.meta.querybuilder.ui.result;

import java.util.List;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.structure.IModelNode;
import it.eng.qbe.model.structure.ViewModelEntity;
import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.QbeDatasetFactory;
import it.eng.spagobi.tools.dataset.bo.IDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TableColumn;

public class ResultTable extends TableViewer {
	
	private ViewModelStructure modelStructure;
	private IDataSet dataSet;
	private ResultTableComparator comparator;
	private static final int defaultColumnWidth = 100;

	
	public ResultTable(Group groupQueryResult, ViewModelStructure modelStructure){
		super(groupQueryResult, SWT.BORDER | SWT.FULL_SELECTION);
		this.modelStructure = modelStructure;
		this.dataSet = getDataSet();
		getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		setLabelProvider(new ResultTableLabelProvider());
		setContentProvider(new ResultTableContentProvider());
		getTable().setLinesVisible(true);
		getTable().setHeaderVisible(true);
		comparator = new ResultTableComparator();
		setComparator(comparator);
	}
	

	public void loadFirstResultAndHeaders(int offset, int fetchSize, int maxResults){
		IDataStore dataStore = loadData(dataSet, offset, fetchSize, maxResults);
		List<String> headers = DataStoreReader.getColumnNames(dataStore);
		for(int i =0; i<headers.size(); i++){
			final TableColumn column = new TableColumn(getTable(), SWT.NONE);
			column.setWidth(defaultColumnWidth);
			column.setText(headers.get(i));
			column.setResizable(true);
			column.setMoveable(true);
			column.addSelectionListener(getSelectionAdapter(column, i));
		}
		setInput(dataStore);
	}
	
	public IDataStore loadData(IDataSet dataSet, int offset, int fetchSize, int maxResults){
		try {
			dataSet.loadData(offset,fetchSize,maxResults);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		IDataStore dataStore = dataSet.getDataStore();
		
		return dataStore;
	}
	
	private SelectionAdapter getSelectionAdapter(final TableColumn column, final int index) {
		SelectionAdapter selectionAdapter = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				comparator.setColumn(index);
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
	
	private IDataSet getDataSet(){
		IDataSource datasource = modelStructure.getDataSource();
		Query query = new Query();
		List entities = modelStructure.getRootEntities("foodmart");
		if(entities.size() > 0) {
			ViewModelEntity entity = (ViewModelEntity)entities.get(0);
			List fields = entity.getAllFields();
			for(int i = 0; i < fields.size(); i++) {
					IModelNode field = (IModelNode)fields.get(i);
					query.addSelectFiled(field.getUniqueName(), null, field.getName(), true, true, false, null, null);
			}
		}	
		return QbeDatasetFactory.createDataSet(modelStructure.getDataSource().createStatement(query));
	}
}
