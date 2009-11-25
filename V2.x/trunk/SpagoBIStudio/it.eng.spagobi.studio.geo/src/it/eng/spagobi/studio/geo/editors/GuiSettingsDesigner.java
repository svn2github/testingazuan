package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.bo.GuiSettingsBO;
import it.eng.spagobi.studio.geo.editors.model.bo.MetadataBO;
import it.eng.spagobi.studio.geo.editors.model.bo.WindowBO;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiParam;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiSettings;
import it.eng.spagobi.studio.geo.editors.model.geo.Param;
import it.eng.spagobi.studio.geo.editors.model.geo.Window;

import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class GuiSettingsDesigner {
	
	private GEOEditor editor=null;
	private Composite mainComposite;
	private GEODocument geoDocument;
	
	private GuiSettings guiSettings;
	private final int TYPE_WINDOWS=1;
	private final int TYPE_PARAMS=2;
	
	public GuiSettings getGuiSettings() {
		return guiSettings;
	}

	public void setGuiSettings(GuiSettings guiSettings) {
		this.guiSettings = guiSettings;
	}

	final ImageDescriptor addIcon = AbstractUIPlugin
	.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/add.gif");
	
	final ImageDescriptor paramsIcon = AbstractUIPlugin
	.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/linkParams.gif");
	
	final ImageDescriptor eraseIcon = AbstractUIPlugin
	.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/erase.gif");
	
	final ImageDescriptor detailIcon = AbstractUIPlugin
	.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/detail.gif");
	
	public GuiSettingsDesigner(Composite _composite, GEOEditor _editor, GEODocument _geoDocument) {
		super();
		mainComposite= _composite;
		editor = _editor;
		geoDocument = _geoDocument;
	}
	
	public void createGuiSettingsWindows(final Composite sectionClient, FormToolkit toolkit){
		
		RowLayout rl = new RowLayout();
		rl.fill=true;
		rl.wrap=true;
		
		// look up for guisettings stored in geodocument
		guiSettings = GuiSettingsBO.getGuiSettings(geoDocument);
		//windows section - navigation
		createWindowGroup("Navigation", toolkit,  rl);
		createWindowGroup("Measures", toolkit,  rl);
		createWindowGroup("Layers", toolkit,  rl);
		createWindowGroup("Detail", toolkit,  rl);
		createWindowGroup("Legend", toolkit,  rl);
		createWindowGroup("Colourpicker", toolkit,  rl);
	
		sectionClient.redraw();
	}
	public void createGuiSettingsParams(final Composite sectionClient, FormToolkit toolkit){
		
		RowLayout rl = new RowLayout();
		rl.fill=true;
		rl.wrap=true;
		
		// look up for guisettings stored in geodocument
		guiSettings = GuiSettingsBO.getGuiSettings(geoDocument);

		createParamsGroup(toolkit,  rl);
	
		sectionClient.redraw();
	}

	private void createParamsGroup(FormToolkit toolkit, RowLayout rl){
		
		final Group guiGroup = new Group(mainComposite, SWT.FILL);
		
		guiGroup.setLayout(rl);
		guiGroup.setLayout(mainComposite.getLayout());
		
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =4;
		gd.heightHint=75;
		gd.minimumHeight=60;
		gd.verticalAlignment=SWT.TOP;	

		final Table guiWindowsTable = toolkit.createTable(guiGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		guiWindowsTable.setLayoutData(gd);
		guiWindowsTable.setLinesVisible(true);
		guiWindowsTable.setHeaderVisible(true);

		String[] titles = { "Parameter Name", "Value"};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(guiWindowsTable, SWT.NONE);
			column.setText(titles[i]);

		}

		if (guiSettings != null && guiSettings.getParams() != null ) {
		
			Vector<GuiParam> params = guiSettings.getParams();
			for(int j=0; j< params.size(); j++){
				TableItem item = new TableItem(guiWindowsTable, SWT.TRANSPARENT);
				createGUIRow(item, guiWindowsTable, params.elementAt(j));
			}
		}
		for (int i = 0; i < titles.length; i++) {
			guiWindowsTable.getColumn(i).pack();
		}
		 //rightClick --> menu
		guiWindowsTable.addListener(SWT.MouseDown, new Listener () {
            public void handleEvent (Event event) {            	
            	if (event.button==3){	
            		createMenu(guiWindowsTable, null);	            	            	
            	}
            }
        });
		guiWindowsTable.redraw();
		//form to add parameters
		createInsertParamForm(null, toolkit, guiGroup, guiWindowsTable);

		
	}
	private void createWindowGroup(final String forWindow, FormToolkit toolkit, RowLayout rl){
		
		final Group guiGroup = new Group(mainComposite, SWT.FILL);
		guiGroup.setText(forWindow);
		
		guiGroup.setLayout(rl);
		guiGroup.setLayout(mainComposite.getLayout());
		
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =1;
		gd.heightHint=75;
		gd.minimumHeight=60;
		gd.verticalAlignment=SWT.TOP;	

		final Table guiWindowsTable = toolkit.createTable(guiGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		guiWindowsTable.setLayoutData(gd);
		guiWindowsTable.setLinesVisible(true);
		guiWindowsTable.setHeaderVisible(true);

		String[] titles = { "Parameter Name", "Value"};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(guiWindowsTable, SWT.NONE);
			column.setText(titles[i]);

		}

		if (guiSettings != null && guiSettings.getWindows() != null && guiSettings.getWindows().getWindow() != null) {
			Window window = WindowBO.getWindowByName(geoDocument, forWindow);
			if(window != null){				
				Vector<GuiParam> params = window.getParams();
				for(int j=0; j< params.size(); j++){
					TableItem item = new TableItem(guiWindowsTable, SWT.TRANSPARENT);
					createGUIRow(item, guiWindowsTable, params.elementAt(j));
				}
			}
		} 
		for (int i = 0; i < titles.length; i++) {
			guiWindowsTable.getColumn(i).pack();
		}
		 //rightClick --> menu
		guiWindowsTable.addListener(SWT.MouseDown, new Listener () {
            public void handleEvent (Event event) {            	
            	if (event.button==3){	
            		Window window = WindowBO.getWindowByName(geoDocument, forWindow);
            		createMenu(guiWindowsTable, window);	            	            	
            	}
            }
        });
		guiWindowsTable.redraw();
		//form to add parameters
		createInsertParamForm(forWindow, toolkit, guiGroup, guiWindowsTable);

		
	}
	private void createMenu(final Table table, final Window window){		
    	Menu menu = new Menu (mainComposite.getShell(), SWT.POP_UP);    	
    	MenuItem menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText ("Delete");
		menuItem.addListener(SWT.Selection, new Listener () {
            public void handleEvent (Event event) { 
            	TableItem[] sel = table.getSelection();
            	if(sel[0] != null){
            		if(window != null){
            			deleteItemWindow(table, sel[0], window);
            		}else{
            			deleteItemParams(table, sel[0]);
            		}
                	
            	}else{
            		MessageDialog.openWarning(mainComposite.getShell(), "Warning", "Please select an item to delete");
            	}

            }
        });	
		table.setMenu(menu);
	}
	private void deleteItemWindow(Table table, TableItem item, Window window){
		WindowBO.deleteParamByName(window, item.getText(0));
		item.dispose();
        //table.pack();
        table.redraw();
        editor.setIsDirty(true);
	}
	private void deleteItemParams(Table table, TableItem item){
		GuiSettingsBO.deleteParamByName(geoDocument, item.getText(0));
		item.dispose();
        //table.pack();
        table.redraw();
        editor.setIsDirty(true);
	}
	private void createInsertParamForm(final String windowName, FormToolkit toolkit, Group group, final Table table){
		
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		formLayout.spacing = 5;		
				
		Composite formComp = toolkit.createComposite(group, SWT.NONE);
		formComp.setLayout (formLayout);
		
		Label label = new Label (formComp, SWT.RIGHT);
		label.setText ("Name:");
		FormData data = new FormData ();
		data.width = 40;
		label.setLayoutData (data);
		int type =TYPE_WINDOWS;
		if(windowName == null){
			type =TYPE_PARAMS;
		}
		final Combo text = createParamCombo(formComp, type);
		
		text.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {				
				if(windowName == null){
					GuiParam param = GuiSettingsBO.getParamByName(geoDocument, text.getText());
					if(param != null){
						MessageDialog.openWarning(mainComposite.getShell(), "Warning", "Another parameter with the same name is already defined.");		
						text.deselectAll();
					}
				}else{
					Window window = WindowBO.getWindowByName(geoDocument, windowName);
					if(window != null){
						GuiParam param = WindowBO.getParamByName(window, text.getText());
						if(param != null){
							MessageDialog.openWarning(mainComposite.getShell(), "Warning", "Another parameter with the same name is already defined.");		
							text.deselectAll();
						}
					}
					
				}
			}
		});
		data = new FormData ();
		data.width = 80;
		data.left = new FormAttachment (label, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		text.setLayoutData (data);
		formComp.setData(text.getText());
		
		Label labelVal = new Label (formComp, SWT.RIGHT);
		labelVal.setText ("Value:");
		data = new FormData ();
		data.width = 40;
		data.top = new FormAttachment(text, 5);
		labelVal.setLayoutData (data);

		final Text textVal = toolkit.createText(formComp, "", SWT.BORDER);
		data = new FormData ();
		data.width = 80;
		data.left = new FormAttachment (labelVal, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelVal, 0, SWT.CENTER);
		textVal.setLayoutData (data);
		formComp.setData(textVal.getText());
		
		Button ok = new Button (formComp, SWT.PUSH);
		ok.setText ("Add");
		data = new FormData ();
		data.width = 40;
		data.top = new FormAttachment(textVal, 5);
		data.right = new FormAttachment (100, 0);
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				GuiParam param = new GuiParam();
				param.setName(text.getText());	
				param.setValue(textVal.getText());
				//add parameter to windows bean
				if(windowName != null){
					Window window = WindowBO.getWindowByName(geoDocument, windowName);					
					//insert in geodocument
					if(window != null){
						Vector params = window.getParams();
						if(params != null){
							params.add(param);
						}else{
							params = new Vector<Param>();
							window.setParams(params);						
						}					
					}else{
						//crea window
						Window newWindow = WindowBO.setNewWindow(geoDocument);
						newWindow.setName(windowName.toLowerCase());
						Vector params = newWindow.getParams();
						if(params != null){
							params.add(param);
						}else{
							params = new Vector<Param>();
							params.add(param);
							newWindow.setParams(params);						
						}	
					}

				}else{
					//add parameter to guisettings
					Vector<GuiParam> params = guiSettings.getParams();
					if(params == null){
						params = new Vector<GuiParam>();
						guiSettings.setParams(params);
					}
					params.add(param);
				}
				TableItem item = new TableItem(table, SWT.NONE);
				createGUIRow(item, table, param);
				//clean combo and text
				text.deselectAll();
				textVal.setText("");
				text.redraw();
				textVal.redraw();
				
				editor.setIsDirty(true);
			}
		});
	}

	private void createGUIRow(TableItem item, final Table guiTable, GuiParam param){
		if(param.getName() != null)
			item.setText(0, param.getName());
		if(param.getValue() != null )
			item.setText(1, param.getValue());
		guiTable.redraw();
	}
	private Combo createParamCombo(Composite composite, int type){
		Combo combo= new Combo(composite, SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		if(type == TYPE_PARAMS){
			combo.add("defaultDrillNav");
			combo.add("highlightOnMouseOver");
			combo.add("normalizeChartValues");
			combo.add("chartScale");
			combo.add("chartWidth");
			combo.add("chartHeight");
			combo.add("valueFont");
			combo.add("valueScale");
		}else if(type == TYPE_WINDOWS){
			combo.add("visible");
			combo.add("width");
			combo.add("height");
			combo.add("x");
			combo.add("y");
			combo.add("moovable");
			combo.add("xMin");
			combo.add("yMin");
			combo.add("xMax");
			combo.add("yMax");
			combo.add("showContent");
			combo.add("margin");
			combo.add("titleBarVisible");
			combo.add("statusBarVisible");
			combo.add("title");
			combo.add("statusBarContent");
			combo.add("closeButtonVisible");
			combo.add("minimizeButtonVisible");
			combo.add("maximizeButtonVisible");
			combo.add("minimized");
			combo.add("transform");
			combo.add("styles");
		}
		return combo;
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
