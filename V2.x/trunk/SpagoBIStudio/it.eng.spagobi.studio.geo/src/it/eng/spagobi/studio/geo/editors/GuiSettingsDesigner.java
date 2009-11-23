package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.editors.model.bo.GuiSettingsBO;
import it.eng.spagobi.studio.geo.editors.model.bo.LinkBO;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.GuiSettings;
import it.eng.spagobi.studio.geo.editors.model.geo.Link;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class GuiSettingsDesigner {
	
	private GEOEditor editor=null;
	private Composite mainComposite;
	private GEODocument geoDocument;
	
	public GuiSettingsDesigner(Composite _composite, GEOEditor _editor, GEODocument _geoDocument) {
		super();
		mainComposite= _composite;
		editor = _editor;
		geoDocument = _geoDocument;
	}
	
	public void createGuiSettingsTable(final Composite sectionClient, FormToolkit toolkit){
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =4;
		gd.heightHint=150;
		gd.minimumHeight=100;
		gd.verticalAlignment=SWT.TOP;
				
		final Group guiGroup = new Group(sectionClient, SWT.FILL);
		guiGroup.setText("GUI Settings");
		
		guiGroup.setLayoutData(gd);
		guiGroup.setLayout(sectionClient.getLayout());
		
		//windows section
		createWindowsTable(toolkit, guiGroup, gd);

		///labels section
		
		//params section
		
		sectionClient.redraw();
	}
	
	private void createWindowsTable(FormToolkit toolkit, Group guiGroup, GridData gd){

		
		final Table guiWindowsTable = toolkit.createTable(guiGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		guiWindowsTable.setLayoutData(gd);
		guiWindowsTable.setLinesVisible(true);
		guiWindowsTable.setHeaderVisible(true);

		String[] titles = { "Navigation","Measures", "Layers", "Detail", "Legend", "ColourPicker"};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(guiWindowsTable, SWT.NONE);
			column.setText(titles[i]);

		}
		// look up for guisettings stored in geodocument
		final GuiSettings guiSettings = GuiSettingsBO.getGuiSettings(geoDocument);

		if (guiSettings != null) {
/*			for(int i=0; i<crossNavigation.getLinks().size(); i++){
				TableItem item = new TableItem(crossNavTable, SWT.TRANSPARENT);
				createTableItemRow(item, crossNavTable, crossNavigation.getLinks().elementAt(i));
			}*/
		} else {
			for (int i = 0; i < 1; i++) {
				TableItem item = new TableItem(guiWindowsTable, SWT.TRANSPARENT);
				createWindowsRow(item, guiWindowsTable, guiSettings);
			}
		}
		for (int i = 0; i < titles.length; i++) {
			guiWindowsTable.getColumn(i).pack();
		}
		// resize the row height using a MeasureItem listener
		guiWindowsTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// height cannot be per row so simply set
				event.height = 23;
			}
		});
		
		guiWindowsTable.redraw();
	}
	private void createWindowsRow(TableItem item, final Table guiTable, GuiSettings guiSettings){
		
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
