package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.Activator;
import it.eng.spagobi.studio.geo.editors.model.bo.GuiSettingsBO;
import it.eng.spagobi.studio.geo.editors.model.bo.Label;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiSettings;
import it.eng.spagobi.studio.geo.editors.model.geo.Labels;

import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class GuiSettingsLabelDesigner {

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
	
	public GuiSettingsLabelDesigner(Composite _composite, GEOEditor _editor, GEODocument _geoDocument) {
		super();
		mainComposite= _composite;
		editor = _editor;
		geoDocument = _geoDocument;
	}
	
	
	public void createGuiSettingsLabels(final Composite sectionClient, FormToolkit toolkit){
		
		GridLayout rl = new GridLayout();
		rl.numColumns=4;
		rl.makeColumnsEqualWidth=true;
		rl.marginRight=10;
		
		// look up for guisettings stored in geodocument
		guiSettings = GuiSettingsBO.getGuiSettings(geoDocument);

		createLabelsGroup(toolkit,  rl);
	
		sectionClient.redraw();
	}
	private void createLabelsGroup(FormToolkit toolkit, Layout rl){
		
		final Group guiGroup = new Group(mainComposite, SWT.FILL);
		
		guiGroup.setLayout(rl);
		guiGroup.setLayout(mainComposite.getLayout());
		
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =4;
		gd.heightHint=75;
		gd.minimumHeight=60;
		gd.minimumWidth=700;
		gd.verticalAlignment=SWT.TOP;
		gd.grabExcessHorizontalSpace=true;
		

		final Table guiWindowsTable = toolkit.createTable(guiGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		guiWindowsTable.setLayoutData(gd);
		guiWindowsTable.setLinesVisible(true);
		guiWindowsTable.setHeaderVisible(true);

		String[] titles = { "     Position     ", "      Class name      "};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(guiWindowsTable, SWT.NONE);
			column.setText(titles[i]);

		}
		if (guiSettings != null && guiSettings.getLabels() != null ) {
		
			Labels labels = guiSettings.getLabels();
			if(labels != null){
				Vector<Label> labelVect = labels.getLabel();
				for(int j=0; j< labelVect.size(); j++){
					TableItem item = new TableItem(guiWindowsTable, SWT.TRANSPARENT);
					createGUIRow(item, guiWindowsTable, labelVect.elementAt(j));
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
            		createMenu(guiWindowsTable, null);	            	            	
            	}
            }
        });
		guiWindowsTable.redraw();
		//form to add parameters
		//createInsertForm(null, toolkit, guiGroup, guiWindowsTable);

	}	
	private void createGUIRow(TableItem item, final Table guiTable, Label label){
		if(label.getPosition() != null)
			item.setText(0, label.getPosition());
		if(label.getClassName() != null )
			item.setText(1, label.getClassName());
		guiTable.redraw();
	}
	private void createMenu(final Table table, final Label label){		
    	Menu menu = new Menu (mainComposite.getShell(), SWT.POP_UP);    	
    	MenuItem menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText ("New label");
		menuItem.addListener(SWT.Selection, new Listener () {
            public void handleEvent (Event event) { 
            	///insert new label

            }
        });	
		menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText ("New parameter");
		menuItem.addListener(SWT.Selection, new Listener () {
            public void handleEvent (Event event) { 
/*            	TreeItem[] sel = hierarchiesTree.getSelection();
            	if(sel[0] != null && sel[0].getParentItem() == null){
                	createNewLevelShell(hierarchiesTree, sel[0], null);
                	
            	}else{
            		MessageDialog.openError(sectionClient.getShell(), "Error", "Wrong position. Please select a hierarchy");
            	}*/

            }
        });	 
		menuItem = new MenuItem (menu, SWT.PUSH);
		menuItem.setText ("Delete");
		menuItem.addListener(SWT.Selection, new Listener () {
            public void handleEvent (Event event) { 
/*            	TreeItem[] sel = hierarchiesTree.getSelection();
            	if(sel[0] != null){
                	deleteItem(hierarchiesTree, sel[0]);
            	}else{
            		MessageDialog.openWarning(sectionClient.getShell(), "Warning", "Please select an item to delete");
            	}*/

            }
        });	
		table.setMenu(menu);
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
