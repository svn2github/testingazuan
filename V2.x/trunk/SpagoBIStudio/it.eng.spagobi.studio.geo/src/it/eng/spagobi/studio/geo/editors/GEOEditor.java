package it.eng.spagobi.studio.geo.editors;


import it.eng.spagobi.sdk.datasets.bo.SDKDataSet;
import it.eng.spagobi.sdk.exceptions.MissingParameterValue;
import it.eng.spagobi.sdk.proxy.DataSetsSDKServiceProxy;
import it.eng.spagobi.studio.core.bo.DataStoreMetadata;
import it.eng.spagobi.studio.core.bo.DataStoreMetadataField;
import it.eng.spagobi.studio.core.bo.Dataset;
import it.eng.spagobi.studio.core.bo.GeoFeature;
import it.eng.spagobi.studio.core.bo.GeoMap;
import it.eng.spagobi.studio.core.bo.SpagoBIServerObjects;
import it.eng.spagobi.studio.core.exceptions.NoServerException;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.core.sdk.SDKProxyFactory;
import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.bo.ModelBO;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.util.DesignerUtils;

import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class GEOEditor extends EditorPart{

	protected boolean isDirty = false;

	private Vector<String> dataSets;
	private Vector<String> maps;
	private HashMap<String, Dataset> datasetInfos;
	private HashMap<String, GeoMap> mapInfos;

	private HashMap<String, DataStoreMetadata> tempDsMetadataInfos;
	private HashMap<String, GeoFeature[]> tempMapMetadataInfos;


	private String selectedDataset;
	private String selectedMap;
	private Table datasetTable;
	private Combo datasetCombo;	

	private Table mapTable;

	private static final int DATASET_NAME=0;
	private static final int DATASET_CLASS=1;
	private static final int DATASET_SELECT=2;
	private static final int DATASET_AGGREGATION=3;

	private static final int FEATURE_NAME=0;
	private static final int FEATURE_DESCR=1;
	private static final int FEATURE_DEFAULT_LEVEL=2;
	private static final int FEATURE_DEFAULT_COLORS=3;


	public void init(IEditorSite site, IEditorInput input) {
		try{
			this.setPartName(input.getName());

			QualifiedName ciao=PropertyPage.MADE_WITH_STUDIO;
			FileEditorInput fei = (FileEditorInput) input;
			IFile file = fei.getFile();
			ModelBO bo=new ModelBO();
			try {
				GEODocument geoDocument = bo.createModel(file);
				bo.saveModel(geoDocument);

			} catch (CoreException e) {
				e.printStackTrace();
				SpagoBILogger.errorLog(GEOEditor.class.toString()+": Error in reading template", e);
				throw(new PartInitException("Error in reading template"));
			}
			setInput(input);
			setSite(site);

			mapInfos=new HashMap<String, GeoMap>();
			datasetInfos=new HashMap<String, Dataset>();
			tempDsMetadataInfos=new HashMap<String, DataStoreMetadata>();
			tempMapMetadataInfos=new HashMap<String, GeoFeature[]>();
		}catch(Exception e){
			SpagoBILogger.warningLog("Error occurred:"+e.getMessage());
		}
	}
	public void initializeEditor(GEODocument geoDocument){
		SpagoBILogger.infoLog("START: "+GEOEditor.class.toString()+" initialize Editor");	
		// clean the properties View
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		// Document properties
		IWorkbenchPage aa=a.getActivePage();


		SDKDataSet[] sdkDataSets=null;
		try{
			SDKProxyFactory proxyFactory=new SDKProxyFactory();
			DataSetsSDKServiceProxy dataSetsServiceProxy=proxyFactory.getDataSetsSDKServiceProxy();
			sdkDataSets=dataSetsServiceProxy.getDataSets();
			int i=0;
		}
		catch (Exception e) {
			SpagoBILogger.errorLog("No comunication with SpagoBI server, could not retrieve dataset informations", e);
		}	


		SpagoBIServerObjects sbso=new SpagoBIServerObjects();
		Vector<Dataset> datasetVector;
		try {
			datasetVector = sbso.getAllDatasets();

			for (Iterator iterator = datasetVector.iterator(); iterator.hasNext();) {
				Dataset dataset = (Dataset) iterator.next();
				datasetInfos.put(dataset.getLabel(), dataset);
			}
			Vector<GeoMap> mapVector= sbso.getAllGeoMaps();
			for (Iterator iterator = mapVector.iterator(); iterator.hasNext();) {
				GeoMap geoMap = (GeoMap) iterator.next();
				mapInfos.put(geoMap.getName(), geoMap);
			}
		} catch (NoServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//mapInfos.put(sdkMap.getName(), sdkMap);


		// Initialize Designer
		//		dataSets = new Vector<String>();
		//		for (int i=0; i< 4 ; i++){
		//			dataSets.add("dataset"+i);
		//		}
		//		
		//		maps = new Vector<String>();
		//		for (int i=0; i< 5 ; i++){
		//			maps.add("map"+i);
		//		}

		//riferita a metadata
		//		datasetInfos = new HashMap<String, String>();		
		//		datasetInfos.put("column", "region_id");
		//		datasetInfos.put("type", "measure");
		//		datasetInfos.put("select", "geoid");
		//		datasetInfos.put("aggregation", "sum");
		//		
		//		//riferita a layers
		//		mapInfos = new HashMap<String, String>();
		//		mapInfos.put("name", "States");
		//		mapInfos.put("description", "States");
		//		mapInfos.put("selected", "true");
		//		mapInfos.put("color", "#4682B4");

		SpagoBILogger.infoLog("END: "+GEOEditor.class.toString()+" initialize Editor");	
	}


	@Override
	public void createPartControl(Composite parent) {



		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 20;
		layout.verticalSpacing = 10;
		layout.topMargin = 20;
		layout.leftMargin = 20;
		//		FillLayout layout = new FillLayout();

		form.getBody().setLayout(layout);

		final Section section = toolkit.createSection(form.getBody(), 
				Section.TITLE_BAR | SWT.NO_REDRAW_RESIZE);

		section.setSize(1000, 1000);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				//parent.setSize(width, height);
				form.reflow(true);
			}
		});
		section.setText("GEO designer");

		Composite sectionClient = toolkit.createComposite(section, SWT.RESIZE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.makeColumnsEqualWidth = true;
		gl.marginHeight=5;
		gl.marginRight=5;
		gl.marginLeft=5;

		sectionClient.setLayout(gl);
		section.setClient(sectionClient);

		Designer designer = new Designer(sectionClient, this);
		GEODocument geoDocument = Activator.getDefault().getGeoDocument();
		designer.setGeoDocument(geoDocument);

		initializeEditor(geoDocument);
		//creazione delle combo e tabelle
		Group datasetGroup = new Group(sectionClient, SWT.FILL);
		datasetGroup.setLayout(sectionClient.getLayout());
		Group mapGroup = new Group(sectionClient, SWT.FILL);
		mapGroup.setLayout(sectionClient.getLayout());

		createDatasetCombo(sectionClient, datasetGroup);
		createMapCombo(sectionClient,mapGroup);

		createDatasetTable(sectionClient, datasetGroup);
		createMapTable(sectionClient,mapGroup);

		designer.createHierarchiesTree(sectionClient);

		SpagoBILogger.infoLog("END "+GEOEditor.class.toString()+": create Part Control function");

	}


	private void createDatasetCombo(final Composite sectionClient,final Group datasetGroup){

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		gd.horizontalAlignment= SWT.END;
		gd.grabExcessHorizontalSpace= true;
		gd.minimumWidth= 120;

		Label datasetLabel = new Label(datasetGroup,  SWT.SIMPLE);
		datasetLabel.setText("Data Set");
		datasetLabel.setAlignment(SWT.RIGHT);
		datasetCombo = new Combo(datasetGroup,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		for (Iterator<String> iterator = datasetInfos.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			datasetCombo.add(name);
		}		
		datasetCombo.setLayoutData(gd);

		datasetCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				// Once selected the dataset fill the table with its metadata, check first if they have been already recovered!
				datasetTable.removeAll();
				int indexSelection=datasetCombo.getSelectionIndex();
				String datasetLabel=datasetCombo.getItem(indexSelection);
				selectedDataset = datasetLabel;
				DataStoreMetadata dataStoreMetadata=null;
				// get the metadata
				if(tempDsMetadataInfos.get(datasetLabel)!=null){
					dataStoreMetadata=tempDsMetadataInfos.get(datasetLabel);
				}
				else{
					Dataset dataset = datasetInfos.get(datasetLabel);
					try{
						dataStoreMetadata=new SpagoBIServerObjects().getDataStoreMetadata(dataset.getId());
						if(dataStoreMetadata!=null){
							tempDsMetadataInfos.put(datasetLabel, dataStoreMetadata);
						}
						else{
							SpagoBILogger.warningLog("Dataset returned no metadata");
							MessageDialog.openWarning(sectionClient.getShell(), "Warning", "Dataset with label = "+datasetLabel+" returned no metadata");			
						}
					}
					catch (MissingParameterValue e2) {
						SpagoBILogger.errorLog("Could not execute dataset with label = "+datasetLabel+" metadata: probably missing parameter", e2);
						MessageDialog.openError(sectionClient.getShell(), "Error", "Could not execute dataset with label = "+datasetLabel+" metadata: probably missing parameter");
					}
					catch (NoServerException e1) {
						SpagoBILogger.errorLog("Error No comunciation with server retrieving dataset with label = "+datasetLabel+" metadata", e1);
						MessageDialog.openError(sectionClient.getShell(), "Error", "No comunciation with server retrieving dataset with label = "+datasetLabel+" metadata");
					}
				}
				if(dataStoreMetadata!=null){
					for (int i = 0; i < dataStoreMetadata.getFieldsMetadata().length; i++) {
						DataStoreMetadataField dsmf=dataStoreMetadata.getFieldsMetadata()[i];
						TableItem item = new TableItem(datasetTable, SWT.NONE);
						item.setText(DATASET_NAME, dsmf.getName());
						item.setText(DATASET_CLASS, dsmf.getClassName());
						//combo per geoid, measures, geocd
						Combo comboSel = new Combo(datasetTable,SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
						comboSel.add("geoid");
						comboSel.add("measures");
						comboSel.add("geocd");
						comboSel.pack();
						TableEditor editor = new TableEditor (datasetTable);
						editor.minimumWidth = comboSel.getSize ().x;
						editor.horizontalAlignment = SWT.CENTER;
						editor.grabHorizontal=true;
						editor.minimumHeight=comboSel.getSize().y;
						editor.verticalAlignment=SWT.CENTER;
						editor.grabVertical=true;

						editor.setEditor(comboSel, item, DATASET_SELECT);

						//combo per geoid, measures, geocd
						Combo comboAgg = new Combo(datasetTable,SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
						comboAgg.add("sum");
						comboAgg.add("media");
						comboAgg.pack();

						editor = new TableEditor (datasetTable);
						editor.minimumWidth = comboAgg.getSize ().x;
						editor.horizontalAlignment = SWT.CENTER;
						editor.grabHorizontal=true;
						editor.minimumHeight=comboAgg.getSize().y;
						editor.verticalAlignment=SWT.CENTER;
						editor.grabVertical=true;
						editor.setEditor(comboAgg, item, DATASET_AGGREGATION);
					}
				}
				sectionClient.getParent().pack();
				sectionClient.getParent().redraw();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}


	private void createDatasetTable(Composite sectionClient, Group datasetGroup){

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =2;

		datasetTable = new Table(datasetGroup, SWT.SINGLE  | SWT.BORDER );
		datasetTable.setLayoutData(gd);
		datasetTable.setLinesVisible (true);
		datasetTable.setHeaderVisible (true);

		String[] titles = { "  Column name   " , "               Type               ", "     Select       ", "   Aggregation mode   "};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(datasetTable, SWT.RESIZE);
			column.setText(titles[i]);
			column.setResizable(true);

		} 

		for (int i=0; i<titles.length; i++) {
			datasetTable.getColumn (i).pack();
		}  
		datasetTable.redraw();

	}

	private void createMapCombo(final Composite sectionClient,Group mapGroup){

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 1;
		gd.horizontalAlignment= SWT.END;
		gd.grabExcessHorizontalSpace= true;
		gd.minimumWidth= 120;

		Label mapLabel = new Label(mapGroup,  SWT.SIMPLE);
		mapLabel.setText("Map");
		mapLabel.setAlignment(SWT.RIGHT);

		final Combo mapCombo = new Combo(mapGroup,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		for (Iterator<String> iterator = mapInfos.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			mapCombo.add(name);
		}		
		mapCombo.setLayoutData(gd);


		mapCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				// Once selected the dataset fill the table with its metadata, check first if they have been already recovered!
				mapTable.removeAll();
				int indexSelection=mapCombo.getSelectionIndex();
				String mapLabel=mapCombo.getItem(indexSelection);
				selectedMap = mapLabel;
				GeoFeature[] geoFeatures=null;
				// get the metadata
				if(tempMapMetadataInfos.get(mapLabel)!=null){
					geoFeatures=tempMapMetadataInfos.get(mapLabel);
				}
				else{
					GeoMap geoMap = mapInfos.get(mapLabel);
					try{
						geoFeatures=new SpagoBIServerObjects().getFeaturesByMapId(geoMap.getMapId());
						if(geoFeatures!=null){
							tempMapMetadataInfos.put(mapLabel, geoFeatures);
						}
						else{
							SpagoBILogger.warningLog("No features returned from map with label "+mapLabel);
							MessageDialog.openWarning(sectionClient.getShell(), "Warning", "No features returned from map with label "+mapLabel);			
						}
					}
					catch (NoServerException e1) {
						SpagoBILogger.errorLog("Could not get features associated to map with label = "+mapLabel, e1);
						MessageDialog.openError(sectionClient.getShell(), "Error", "Could not get features associated to map with label = "+mapLabel);
					}
				}
				if(geoFeatures!=null){
					for (int i = 0; i < geoFeatures.length; i++) {
						GeoFeature geoFeature=geoFeatures[i];
						TableItem item = new TableItem(mapTable, SWT.NONE);
						item.setText(FEATURE_NAME, geoFeature.getName());

						TableEditor editor = new TableEditor (mapTable);
						Text newDescr = new Text(mapTable, SWT.BORDER);
						newDescr.setBackground(new Color(sectionClient.getDisplay(), new RGB(245,245,245)));
						newDescr.setText(geoFeature.getDescr()!=null ? geoFeature.getDescr() : "");
						newDescr.addModifyListener(new ModifyListener() {
							public void modifyText(ModifyEvent me) {
								System.out.println("Changed");
							}
						});
						editor.minimumWidth = newDescr.getSize ().x;
						editor.horizontalAlignment = SWT.CENTER;
						editor.grabHorizontal=true;						
						editor.minimumHeight=newDescr.getSize().y;
						editor.verticalAlignment=SWT.CENTER;
						editor.grabVertical=true;
						newDescr.selectAll();
						newDescr.setFocus();						
						editor.setEditor(newDescr, item, FEATURE_DESCR);

						Button selButton = new Button(mapTable, SWT.RADIO);
						selButton.setText("");	
						editor = new TableEditor (mapTable);
						editor.minimumWidth = selButton.getSize ().x;
						editor.horizontalAlignment = SWT.CENTER;
						editor.grabHorizontal=true;
						editor.minimumHeight=selButton.getSize().y;
						editor.verticalAlignment=SWT.CENTER;
						editor.grabVertical=true;
						editor.setEditor(selButton, item, FEATURE_DEFAULT_LEVEL);
						Composite colorSection=DesignerUtils.createColorPicker(mapTable, "#FF0000");

						editor = new TableEditor (mapTable);
						editor.minimumWidth = colorSection.getSize ().x;
						editor.horizontalAlignment = SWT.CENTER;
						editor.grabHorizontal=true;
						editor.minimumHeight=colorSection.getSize().y;
						editor.verticalAlignment=SWT.CENTER;
						editor.grabVertical=true;						
						editor.setEditor(colorSection, item, FEATURE_DEFAULT_COLORS);
					}
				}
				sectionClient.getParent().pack();
				sectionClient.getParent().redraw();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});





	}

	private void createMapTable(Composite sectionClient, Group mapGroup){


		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =2;

		mapTable = new Table(mapGroup, SWT.SINGLE  | SWT.BORDER );
		mapTable.setLayoutData(gd);
		mapTable.setLinesVisible (true);
		mapTable.setHeaderVisible (true);


		String[] titles = { "      Feature name      " , "           Description        ", "  Select Default ", "  Default Color "};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(mapTable, SWT.RESIZE);
			column.setText(titles[i]);
		} 


		for (int i=0; i<titles.length; i++) {
			mapTable.getColumn (i).pack ();
		}  
		mapTable.layout();

	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}
	public String getSelectedDataset() {
		return selectedDataset;
	}
	public void setSelectedDataset(String selectedDataset) {
		this.selectedDataset = selectedDataset;
	}
	public HashMap<String, DataStoreMetadata> getTempDsMetadataInfos() {
		return tempDsMetadataInfos;
	}
	public void setTempDsMetadataInfos(
			HashMap<String, DataStoreMetadata> tempDsMetadataInfos) {
		this.tempDsMetadataInfos = tempDsMetadataInfos;
	}
	public HashMap<String, GeoFeature[]> getTempMapMetadataInfos() {
		return tempMapMetadataInfos;
	}
	public void setTempMapMetadataInfos(
			HashMap<String, GeoFeature[]> tempMapMetadataInfos) {
		this.tempMapMetadataInfos = tempMapMetadataInfos;
	}
	public HashMap<String, Dataset> getDatasetInfos() {
		return datasetInfos;
	}
	public void setDatasetInfos(HashMap<String, Dataset> datasetInfos) {
		this.datasetInfos = datasetInfos;
	}
	public HashMap<String, GeoMap> getMapInfos() {
		return mapInfos;
	}
	public void setMapInfos(HashMap<String, GeoMap> mapInfos) {
		this.mapInfos = mapInfos;
	}
	public String getSelectedMap() {
		return selectedMap;
	}
	public void setSelectedMap(String selectedMap) {
		this.selectedMap = selectedMap;
	}

}
