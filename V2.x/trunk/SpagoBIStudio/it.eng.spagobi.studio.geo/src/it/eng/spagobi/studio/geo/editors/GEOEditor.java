package it.eng.spagobi.studio.geo.editors;


import it.eng.spagobi.sdk.datasets.bo.SDKDataSet;
import it.eng.spagobi.sdk.proxy.DataSetsSDKServiceProxy;
import it.eng.spagobi.studio.core.bo.Dataset;
import it.eng.spagobi.studio.core.bo.GeoMap;
import it.eng.spagobi.studio.core.bo.SpagoBIServerObjects;
import it.eng.spagobi.studio.core.exceptions.NoServerException;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.core.properties.PropertyPage;
import it.eng.spagobi.studio.core.sdk.SDKProxyFactory;
import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.bo.ModelBO;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
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
	
	private String selectedDataset;
	private Table datasetTable;
	
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
		Composite sectionClient = toolkit.createComposite(section, SWT.NO_REDRAW_RESIZE);
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
		final Combo datasetCombo = new Combo(datasetGroup,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		for (Iterator<String> iterator = datasetInfos.keySet().iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			datasetCombo.add(name);
		}		
		datasetCombo.setLayoutData(gd);
	}
	
	private void createDatasetTable(Composite sectionClient, Group datasetGroup){
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =2;

		datasetTable = new Table(datasetGroup, SWT.SINGLE  | SWT.BORDER );
		datasetTable.setLayoutData(gd);
		datasetTable.setLinesVisible (true);
		datasetTable.setHeaderVisible (true);
		
	    String[] titles = { "  Column name   " , "   Type   ", "     Select       ", "   Aggregation mode   "};
	    for (int i = 0; i < titles.length; i++) {
		      TableColumn column = new TableColumn(datasetTable, SWT.RESIZE);
		      column.setText(titles[i]);
		      column.setResizable(true);

		} 
	    
	    TableEditor editor = new TableEditor (datasetTable);
	    
	    //valori reperiti da dataset selezionato
	    if(selectedDataset != null && !selectedDataset.equals("") && datasetInfos.get(selectedDataset) != null){
	    	
	    	Dataset datasetItem = datasetInfos.get(selectedDataset);
	    	TableItem item = new TableItem(datasetTable, SWT.NONE);
	    	
	
		    item.setText(0, datasetItem.getName());
		    item.setText(1, datasetItem.getType());
		    
		    //combo per geoid, measures, geocd
		    Combo comboSel = new Combo(datasetTable,SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
			comboSel.add("geoid");
			comboSel.add("measures");
			comboSel.add("geocd");
			for(int i=0; i< comboSel.getItemCount(); i++){
				boolean val = datasetItem.getNumberingRows().booleanValue();
				if(String.valueOf(val).equalsIgnoreCase(comboSel.getItem(i))){
					comboSel.select(i);
				}		
			}
	
			comboSel.pack();
			
		    editor.minimumWidth = comboSel.getSize ().x;
		    editor.horizontalAlignment = SWT.CENTER;
		    editor.grabHorizontal=true;
		    editor.setEditor(comboSel, item, 2);
	
		    
		    //combo per geoid, measures, geocd
		    Combo comboAgg = new Combo(datasetTable,SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		    comboAgg.add("sum");
		    comboAgg.add("media");
		    comboAgg.pack();
		    
		    editor = new TableEditor (datasetTable);
		    editor.minimumWidth = comboAgg.getSize ().x;
		    editor.horizontalAlignment = SWT.CENTER;
		    editor.grabHorizontal=true;
		    editor.setEditor(comboAgg, item, 3);

	    }
	    for (int i=0; i<titles.length; i++) {
	    	datasetTable.getColumn (i).pack();
	    }  
	    datasetTable.redraw();
    
	}
	
	private void createMapCombo(Composite sectionClient,Group mapGroup){
		
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
	}

	private void createMapTable(Composite sectionClient, Group mapGroup){

		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =2;
		
		Table mapTable = new Table(mapGroup, SWT.SINGLE  | SWT.BORDER );
		mapTable.setLayoutData(gd);
		mapTable.setLinesVisible (true);
		mapTable.setHeaderVisible (true);


	    String[] titles = { "  Column name  " , "  Type  ", "  Select  ", "  Aggregation mode "};
	    for (int i = 0; i < titles.length; i++) {
		      TableColumn column = new TableColumn(mapTable, SWT.RESIZE);
		      column.setText(titles[i]);
		} 
	    
	    TableEditor editor = new TableEditor (mapTable);
	    
	    //valori reperiti da dataset selezionato
/*    	TableItem item = new TableItem(mapTable, SWT.NONE);
	    item.setText(0, mapInfos.get("name"));
	    item.setText(1, mapInfos.get("description"));
	    
	    
	    //combo per geoid, measures, geocd
	    Button radio = new Button(mapTable, SWT.RADIO);
	    if(mapInfos.get("selected").equalsIgnoreCase("true")){
	    	radio.setSelection(true);
	    }

	    radio.setEnabled(true);
	    radio.pack();
		
	    editor.minimumWidth = radio.getSize ().x;
	    editor.horizontalAlignment = SWT.CENTER;
	    editor.grabHorizontal= true;
	    editor.verticalAlignment = SWT.BOTTOM;
	    editor.setEditor(radio, item, 2);
	    
	    item.setText(3, mapInfos.get("color"));*/

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
	
}
