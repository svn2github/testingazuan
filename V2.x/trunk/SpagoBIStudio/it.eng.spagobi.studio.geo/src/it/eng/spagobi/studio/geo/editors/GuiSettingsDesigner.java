package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.bo.GuiSettingsBO;
import it.eng.spagobi.studio.geo.editors.model.bo.WindowBO;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiSettings;
import it.eng.spagobi.studio.geo.editors.model.geo.Param;
import it.eng.spagobi.studio.geo.editors.model.geo.Window;

import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
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
	
	public void createGuiSettingsTable(final Composite sectionClient, FormToolkit toolkit){
		
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

		///labels section
		
		//params section
		
		sectionClient.redraw();
	}
	
	private void createWindowGroup(String forWindow, FormToolkit toolkit, RowLayout rl){
		
		final Group guiGroup = new Group(mainComposite, SWT.FILL);
		guiGroup.setText(forWindow);
		
		guiGroup.setLayout(rl);
		guiGroup.setLayout(mainComposite.getLayout());
		
		
		FillLayout fillLayout = new FillLayout();
		fillLayout.type = SWT.VERTICAL;

		final Table guiWindowsTable = toolkit.createTable(guiGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		guiWindowsTable.setLayout(fillLayout);
		guiWindowsTable.setLinesVisible(true);
		guiWindowsTable.setHeaderVisible(true);

		String[] titles = { "Parameter Name", "Value"};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(guiWindowsTable, SWT.NONE);
			column.setText(titles[i]);

		}
		Window selWindow = null;
		if (guiSettings != null && guiSettings.getWindows() != null && guiSettings.getWindows().getWindow() != null) {
			//look for window name = navigation			
			for(int i=0; i<guiSettings.getWindows().getWindow().size(); i++){
				Window window = guiSettings.getWindows().getWindow().elementAt(i);
				String name = window.getName();
				if(name != null && name.equalsIgnoreCase(forWindow)){
					selWindow = window;
					TableItem item = new TableItem(guiWindowsTable, SWT.TRANSPARENT);
					Vector<Param> params = window.getParams();
					for(int j=0; j< params.size(); j++){
						createWindowsRow(item, guiWindowsTable, params.elementAt(j));
					}
					
				}
			}
		} else {
			for (int i = 0; i < 1; i++) {
				TableItem item = new TableItem(guiWindowsTable, SWT.TRANSPARENT);
			}
		}
		for (int i = 0; i < titles.length; i++) {
			guiWindowsTable.getColumn(i).pack();
		}
		
		guiWindowsTable.redraw();
		//form to add parameters
		createInsertParamForm(toolkit, guiGroup, guiWindowsTable, selWindow);

		
	}
	private void createInsertParamForm(FormToolkit toolkit, Group group, final Table table, final Window window){
		
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 5;
		formLayout.marginHeight = 5;
		formLayout.spacing = 5;		
				
		Composite formComp = toolkit.createComposite(group, SWT.BORDER);
		formComp.setLayout (formLayout);
		
		Label label = new Label (formComp, SWT.RIGHT);
		label.setText ("Name:");
		FormData data = new FormData ();
		data.width = 40;
		label.setLayoutData (data);

		final Combo text = createWindowsParamCombo(formComp);
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
				//add parameter
				Param param = new Param();
				param.setName(text.getText());
				param.setValue(textVal.getText());
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
				}
				
				
				TableItem item = new TableItem(table, SWT.NONE);
				createWindowsRow(item, table, param);
			}
		});
	}

	private void createWindowsRow(TableItem item, final Table guiTable, Param param){
		item.setText(0, param.getName());
		item.setText(1, param.getValue());
		guiTable.redraw();
	}
	private Combo createWindowsParamCombo(Composite composite){
		Combo combo= new Combo(composite, SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
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
