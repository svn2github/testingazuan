package it.eng.spagobi.studio.geo.editors;


import it.eng.spagobi.sdk.exceptions.MissingParameterValue;
import it.eng.spagobi.studio.core.bo.DataStoreMetadata;
import it.eng.spagobi.studio.core.bo.DataStoreMetadataField;
import it.eng.spagobi.studio.core.bo.Dataset;
import it.eng.spagobi.studio.core.bo.GeoFeature;
import it.eng.spagobi.studio.core.bo.GeoMap;
import it.eng.spagobi.studio.core.bo.SpagoBIServerObjects;
import it.eng.spagobi.studio.core.exceptions.NoServerException;
import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.geo.editors.model.bo.HierarchyBO;
import it.eng.spagobi.studio.geo.editors.model.bo.LevelBO;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Level;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class Designer {


	private GEOEditor editor=null;
	private Composite mainComposite;
	private GEODocument geoDocument;

	public Designer(Composite _composite, GEOEditor _editor) {
		super();
		mainComposite= _composite;
		editor = _editor;
	}

	public Designer(Composite _composite, GEOEditor _editor, GEODocument _geoDocument) {
		super();
		mainComposite= _composite;
		editor = _editor;
		geoDocument = _geoDocument;
	}
	
	private void createNewHierarchy(Tree hierarchiesTree, String name, String type){		
        TreeItem iItem = new TreeItem(hierarchiesTree, SWT.NONE);
        iItem.setText(name);
        hierarchiesTree.redraw();
        //crea oggetto java con name+type
        HierarchyBO.setNewHierarchy(geoDocument, name, type);
		
	}
	private void createNewLevel(Tree hierarchiesTree, Level newLevel, TreeItem parent){		
        TreeItem iItem = new TreeItem(parent, SWT.NONE);
        iItem.setText(newLevel.getName());
        hierarchiesTree.redraw();
        //crea oggetto java con name+type
        LevelBO.setNewLevel(geoDocument, parent.getText(), newLevel);
		
	}
	
	private void deleteItem(Tree hierarchiesTree, TreeItem item){
        //elimina oggetto java
		if(item.getParentItem() == null){
			//hierarchy--> delete hierarchy
			HierarchyBO.deleteHierarchy(geoDocument, item.getText());
		}else{
			//level--> deleteLevel
			LevelBO.deleteLevel(geoDocument, item.getParentItem().getText(), item.getText());
		}
		item.dispose();
		hierarchiesTree.redraw();
	}
	private void createMenu(final Composite sectionClient, final Tree hierarchiesTree){
		
    	Menu menu = new Menu (sectionClient.getShell(), SWT.POP_UP);
    	MenuItem menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText ("New Hierarchy");
		menuItem.addListener(SWT.Selection, new Listener () {
            public void handleEvent (Event event) { 
            	TreeItem[] sel = hierarchiesTree.getSelection();
            	System.out.println("crea hierarchy");
            	createNewHierarchyShell(hierarchiesTree);
            }
        });
		menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText ("New Level");
		menuItem.addListener(SWT.Selection, new Listener () {
            public void handleEvent (Event event) { 
            	TreeItem[] sel = hierarchiesTree.getSelection();
            	if(sel[0] != null){
                	System.out.println("crea nuovo level...per "+sel[0].getText());
                	createNewLevelShell(hierarchiesTree, sel[0]);
            	}else{
            		MessageDialog.openWarning(sectionClient.getShell(), "Warning", "Please select a hierarchy");
            	}

            }
        });	 
		menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText ("Delete");
		menuItem.addListener(SWT.Selection, new Listener () {
            public void handleEvent (Event event) { 
            	TreeItem[] sel = hierarchiesTree.getSelection();
            	if(sel[0] != null){
                	deleteItem(hierarchiesTree, sel[0]);
            	}else{
            		MessageDialog.openWarning(sectionClient.getShell(), "Warning", "Please select an item to delete");
            	}

            }
        });	
		hierarchiesTree.setMenu(menu);
	}
	protected void createHierarchiesTree(final Composite sectionClient){
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =4;
				
		final Group hierarchiesGroup = new Group(sectionClient, SWT.FILL | SWT.RESIZE);
		hierarchiesGroup.setLayout(sectionClient.getLayout());
		hierarchiesGroup.setLayoutData(gd);
		hierarchiesGroup.setText("HIERARCHIES");
		
		final Tree hierarchiesTree = new Tree(hierarchiesGroup, SWT.SINGLE);
		hierarchiesTree.setLayoutData(gd);
	    for (int i = 0; i < 4; i++) {
	        TreeItem iItem = new TreeItem(hierarchiesTree, 0);
	        iItem.setText("TreeItem (0) -" + i);
	        
	        for (int j = 0; j < 4; j++) {
	          TreeItem jItem = new TreeItem(iItem, 0);
	          jItem.setText("TreeItem (1) -" + j);
	        }
	    }
	    //mouseDoubleClick --> new hierarchy
	    hierarchiesTree.addListener(SWT.MouseDoubleClick, new Listener () {
            public void handleEvent (Event event) {
            	createNewHierarchyShell(hierarchiesTree);
            }
        });
	    //rightClick --> menu
	    hierarchiesTree.addListener(SWT.MouseDown, new Listener () {
            public void handleEvent (Event event) {            	
            	if (event.button==3){	
            		createMenu(sectionClient, hierarchiesTree);	            	            	
            	}
            }
        });	    

	    hierarchiesGroup.redraw();
        sectionClient.redraw();
	}
	private void createNewHierarchyShell(final Tree hierarchiesTree){
		final Shell dialog = new Shell (mainComposite.getDisplay(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("New Hierarchy");
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label label = new Label (dialog, SWT.RIGHT);
		label.setText ("Hierarchy name:");
		FormData data = new FormData ();
		data.width = 100;
		label.setLayoutData (data);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setText ("Cancel");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (100, 0);
		data.bottom = new FormAttachment (100, 0);
		cancel.setLayoutData (data);
		cancel.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("User cancelled dialog");
				dialog.close ();
			}
		});

		final Text text = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (label, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (label, 0, SWT.CENTER);
		//data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		text.setLayoutData (data);
		
		
		//type
		data = new FormData ();
		data.width = 100;
		data.top = new FormAttachment(text, 5);

		Label labelType = new Label (dialog, SWT.RIGHT);
		labelType.setText ("Type:");		
		labelType.setLayoutData (data);
		
		final Text textType = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelType, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelType, 0, SWT.CENTER);
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		textType.setLayoutData (data);
		

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				//create tree item
				String type = textType.getText();
				String name = text.getText();
				createNewHierarchy(hierarchiesTree, name, type);
				dialog.close ();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack ();
		dialog.open ();

	}
	private void createNewLevelShell(final Tree hierarchiesTree, final TreeItem selectedItem){
		final Shell dialog = new Shell (mainComposite.getDisplay(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("New Level for "+selectedItem);
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label label = new Label (dialog, SWT.RIGHT);
		label.setText ("Level name:");
		FormData data = new FormData ();
		data.width = 100;
		label.setLayoutData (data);

		Button cancel = new Button (dialog, SWT.PUSH);
		cancel.setText ("Cancel");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (100, 0);
		data.bottom = new FormAttachment (100, 0);
		cancel.setLayoutData (data);
		cancel.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				System.out.println("User cancelled dialog");
				dialog.close ();
			}
		});

		final Text text = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (label, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (label, 0, SWT.CENTER);
		text.setLayoutData (data);
		
		//dataset column 
		data = new FormData ();
		data.width = 100;
		data.top = new FormAttachment(text, 5);
		Label labelColumn = new Label (dialog, SWT.RIGHT);
		labelColumn.setText ("Dataset column:");
		labelColumn.setLayoutData (data);		
		
	
		final Combo textColumn = drawColumnIdCombo(dialog);

		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelColumn, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelColumn, 0, SWT.CENTER);
		textColumn.setLayoutData (data);
		
		//description
		data = new FormData ();
		data.width = 100;
		data.top = new FormAttachment(textColumn, 5);
		Label labelDescr = new Label (dialog, SWT.RIGHT);
		labelDescr.setText ("Description:");
		labelDescr.setLayoutData (data);	
		
		final Text textDescription = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelDescr, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelDescr, 0, SWT.CENTER);
		textDescription.setLayoutData (data);
		
		//feature
		data = new FormData ();
		data.width = 100;
		data.top = new FormAttachment(textDescription, 5);
		Label labelFeature = new Label (dialog, SWT.RIGHT);
		labelFeature.setText ("Feature:");
		labelFeature.setLayoutData (data);	
		
		final Combo textFeature = drawFeaturesNameCombo(dialog);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelFeature, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelFeature, 0, SWT.CENTER);
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		textFeature.setLayoutData (data);

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("OK");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (cancel, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				//create tree item
				String columnId = textColumn.getText();
				String columnDesc = textDescription.getText();
				String feature = textFeature.getText();
				String name = text.getText();
				
		        Level newLevel = new Level();
		        newLevel.setName(name);
		        newLevel.setColumnId(columnId);
		        newLevel.setColumnDesc(columnDesc);
		        newLevel.setFeatureName(feature);
				createNewLevel(hierarchiesTree, newLevel,  selectedItem);
				dialog.close ();
			}
		});

		dialog.setDefaultButton (ok);
		dialog.pack ();
		dialog.open ();

	}
	
	private Combo drawColumnIdCombo(final Shell dialog){
		final Combo textColumn = new Combo(dialog, SWT.SINGLE);

		String datasetLabel=editor.getSelectedDataset();
		DataStoreMetadata dataStoreMetadata=null;
		// get the metadata
		if(editor.getTempDsMetadataInfos().get(datasetLabel)!=null){
			dataStoreMetadata=editor.getTempDsMetadataInfos().get(datasetLabel);
		}
		else{
			Dataset dataset = editor.getDatasetInfos().get(datasetLabel);
			try{
				if(dataset.getId() != null){
					dataStoreMetadata=new SpagoBIServerObjects().getDataStoreMetadata(dataset.getId());
				}
				
				if(dataStoreMetadata!=null){
					editor.getTempDsMetadataInfos().put(datasetLabel, dataStoreMetadata);
				}
				else{
					SpagoBILogger.warningLog("Dataset returned no metadata");
					MessageDialog.openWarning(mainComposite.getShell(), "Warning", "Dataset with label = "+datasetLabel+" returned no metadata");			
				}
			}
			catch (MissingParameterValue e2) {
				SpagoBILogger.errorLog("Could not execute dataset with label = "+datasetLabel+" metadata: probably missing parameter", e2);
				MessageDialog.openError(mainComposite.getShell(), "Error", "Could not execute dataset with label = "+datasetLabel+" metadata: probably missing parameter");
			}
			catch (NoServerException e1) {
				SpagoBILogger.errorLog("Error No comunciation with server retrieving dataset with label = "+datasetLabel+" metadata", e1);
				MessageDialog.openError(mainComposite.getShell(), "Error", "No comunciation with server retrieving dataset with label = "+datasetLabel+" metadata");
			}
		}
		if(dataStoreMetadata!=null){
			
			for (int i = 0; i < dataStoreMetadata.getFieldsMetadata().length; i++) {
				DataStoreMetadataField dsmf=dataStoreMetadata.getFieldsMetadata()[i];
				String column = dsmf.getName();
				textColumn.add(column);				
			}
			//dialog.redraw();
		}
		return textColumn;
	}
	private Combo drawFeaturesNameCombo(final Shell dialog){
		final Combo textFeature = new Combo(dialog, SWT.SINGLE);

		String mapLabel=editor.getSelectedMap();
		GeoFeature[] geoFeatures=null;
		// get the metadata
		if(editor.getTempMapMetadataInfos().get(mapLabel)!=null){
			geoFeatures=editor.getTempMapMetadataInfos().get(mapLabel);
		}
		else{
			GeoMap geoMap = editor.getMapInfos().get(mapLabel);
			try{
				if(geoMap.getMapId() != -1){
					geoFeatures=new SpagoBIServerObjects().getFeaturesByMapId(geoMap.getMapId());
				}
				if(geoFeatures!=null){
					editor.getTempMapMetadataInfos().put(mapLabel, geoFeatures);
				}
				else{
					SpagoBILogger.warningLog("No features returned from map with label "+mapLabel);
					MessageDialog.openWarning(mainComposite.getShell(), "Warning", "No features returned from map with label "+mapLabel);			
				}
			}
			catch (NoServerException e1) {
				SpagoBILogger.errorLog("Could not get features associated to map with label = "+mapLabel, e1);
				MessageDialog.openError(mainComposite.getShell(), "Error", "Could not get features associated to map with label = "+mapLabel);
			}
		}
		if(geoFeatures!=null){
			for (int i = 0; i < geoFeatures.length; i++) {
				GeoFeature geoFeature=geoFeatures[i];
				geoFeature.getName();

				
			}
		}
		return textFeature;
	}
	public GEOEditor getEditor() {
		return editor;
	}

	public void setEditor(GEOEditor editor) {
		this.editor = editor;
	}

	public Composite getMainComposite() {
		return mainComposite;
	}

	public void setMainComposite(Composite mainComposite) {
		this.mainComposite = mainComposite;
	}
	public GEODocument getGeoDocument() {
		return geoDocument;
	}


	public void setGeoDocument(GEODocument geoDocument) {
		this.geoDocument = geoDocument;
	}
}
