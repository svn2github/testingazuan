package it.eng.spagobi.studio.chart.editors;


import it.eng.spagobi.studio.chart.editors.model.chart.ChartModel;
import it.eng.spagobi.studio.core.bo.DataStoreMetadata;
import it.eng.spagobi.studio.core.bo.DataStoreMetadataField;
import it.eng.spagobi.studio.core.bo.SpagoBIServerObjects;
import it.eng.spagobi.studio.core.log.SpagoBILogger;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

public class DataSetInformationEditor {

	Section sectionDatasetInformation=null;
	Composite sectionClientDatasetInformation=null;
	private Table datasetTable;
	Label noDataSet;
	public DataSetInformationEditor(final ChartModel model, FormToolkit toolkit, final ScrolledForm form) {

		sectionDatasetInformation= toolkit.createSection(form.getBody(), 
				Section.DESCRIPTION|Section.TITLE_BAR|Section.TWISTIE);
		sectionClientDatasetInformation=toolkit.createComposite(sectionDatasetInformation);

		TableWrapData td = new TableWrapData(TableWrapData.FILL);
		sectionClientDatasetInformation.setLayoutData(td);
		sectionDatasetInformation.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		sectionDatasetInformation.setText("Dataset Metadata");
		//sectionDatasetInformation.setDescription("All the selected Dataset Metadata");


		GridLayout gridLayout=new GridLayout();
		gridLayout.numColumns=1;
		sectionClientDatasetInformation.setLayout(gridLayout);
		SpagoBIServerObjects sbso = new SpagoBIServerObjects();
		noDataSet=new org.eclipse.swt.widgets.Label(sectionClientDatasetInformation, SWT.NULL);

		if(model.getSdkDataSetId()!=null){
			DataStoreMetadata dataStoreMetadata= null;
			try {
				dataStoreMetadata=sbso.getDataStoreMetadata(model.getSdkDataSetId());
				//				SDKProxyFactory proxyFactory = new SDKProxyFactory();
				//				DataSetsSDKServiceProxy datasetSDKServiceProxy = proxyFactory.getDataSetsSDKServiceProxy();
				//				SDKDataSet sdkDataSet = datasetSDKServiceProxy.getDataSet(model.getSdkDataSetId());
				//				if(sdkDataSet!=null){
				//					dataSetName=sdkDataSet.getName();
				//					sdkDataStoreMetadata=datasetSDKServiceProxy.getDataStoreMetadata(sdkDataSet);
				//					createDatasetTable(sectionClientDatasetInformation);
				//					fillDatasetTable(sdkDataStoreMetadata);
				//				}

				if(dataStoreMetadata!=null){
					createDatasetTable(sectionClientDatasetInformation);
					fillDatasetTable(dataStoreMetadata);
				}
				else{
					SpagoBILogger
					.errorLog("No comunication with SpagoBI server, could not retrieve dataset informations",null);
					//MessageDialog.openError(sectionDatasetInformation.getShell(), "Error", "Could not retrieve metadata for dataset with ID "+model.getSdkDataSetId());
					noDataSet.setText("No comunication with SpagoBI server, could not retrieve dataset informations");
				}
			} catch (Exception e) {
				SpagoBILogger
				.errorLog("No comunication with SpagoBI server, could not retrieve dataset informations",e);
				//MessageDialog.openError(sectionDatasetInformation.getShell(), "Error", "Could not retrieve metadata for dataset with ID "+model.getSdkDataSetId());
				noDataSet.setText("No comunication with SpagoBI server, could not retrieve dataset informations");
			}

		}
		else{
			noDataSet.setText("No Dataset Associated to the opened document");
		}

		sectionDatasetInformation.setClient(sectionClientDatasetInformation);


	}



	private void createDatasetTable(final Composite sectionClient) {

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;

		datasetTable = new Table(sectionClient, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION | SWT.CHECK);
		datasetTable.setLayoutData(gd);
		datasetTable.setLinesVisible(true);
		datasetTable.setHeaderVisible(true);

		String[] titles = { "            Column name             ",
		"               Type               "};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(datasetTable, SWT.NONE);
			column.setText(titles[i]);
			// column.setResizable(true);
		}
		for (int i = 0; i < titles.length; i++) {
			datasetTable.getColumn(i).pack();
		}
		datasetTable.redraw();

	}


	private void fillDatasetTable(DataStoreMetadata dataStoreMetadata) {
		// if dataset changed than new Metadata

		for (int i = 0; i < dataStoreMetadata.getFieldsMetadata().length; i++) {
			TableItem item = new TableItem(datasetTable, SWT.TRANSPARENT);
			DataStoreMetadataField dsmf = dataStoreMetadata.getFieldsMetadata()[i];
			// find out the current column
			item.setText(0, dsmf.getName());
			item.setText(1, dsmf.getClassName());
		}

		datasetTable.pack();
		datasetTable.redraw();

	}
}
