package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.editors.model.bo.CrossNavigationBO;
import it.eng.spagobi.studio.geo.editors.model.bo.HierarchyBO;
import it.eng.spagobi.studio.geo.editors.model.bo.LevelBO;
import it.eng.spagobi.studio.geo.editors.model.bo.LinkBO;
import it.eng.spagobi.studio.geo.editors.model.geo.CrossNavigation;
import it.eng.spagobi.studio.geo.editors.model.geo.GEODocument;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchies;
import it.eng.spagobi.studio.geo.editors.model.geo.Hierarchy;
import it.eng.spagobi.studio.geo.editors.model.geo.Level;
import it.eng.spagobi.studio.geo.editors.model.geo.Levels;
import it.eng.spagobi.studio.geo.editors.model.geo.Link;

import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

public class CrossNavigationDesigner {
	private GEOEditor editor=null;
	private Composite mainComposite;
	private GEODocument geoDocument;
	private Vector<TableEditor> tableEditors = new Vector<TableEditor>();
	
	public CrossNavigationDesigner(Composite _composite, GEOEditor _editor, GEODocument _geoDocument) {
		super();
		mainComposite= _composite;
		editor = _editor;
		geoDocument = _geoDocument;
	}
	public void createCrossnavigationTable(final Composite sectionClient, FormToolkit toolkit){
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan =4;
		gd.heightHint=150;
		gd.minimumHeight=100;
		gd.verticalAlignment=SWT.TOP;
				
		final Composite crossNavGroup = new Composite(sectionClient, SWT.FILL);
		
		crossNavGroup.setLayoutData(gd);
		crossNavGroup.setLayout(sectionClient.getLayout());
		
		final Table crossNavTable = toolkit.createTable(crossNavGroup, SWT.MULTI | SWT.BORDER
				| SWT.FULL_SELECTION);
		crossNavTable.setLayoutData(gd);
		crossNavTable.setLinesVisible(true);
		crossNavTable.setHeaderVisible(true);

		String[] titles = { "  Hierarchy   ","     Level     ", "Optional parameters"};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(crossNavTable, SWT.NONE);
			column.setText(titles[i]);

		}
		// look up for crossNavigation stored in geodocument
		final CrossNavigation crossNavigation = CrossNavigationBO.getCrossNavigation(geoDocument);

		if (crossNavigation != null && crossNavigation.getLinks() != null) {
			//selectDataset(sectionClient, metadata);

		} else {
			for (int i = 0; i < 1; i++) {
				TableItem item = new TableItem(crossNavTable, SWT.TRANSPARENT);
				createTableItemRow(item, crossNavTable);
			}
		}
		for (int i = 0; i < titles.length; i++) {
			crossNavTable.getColumn(i).pack();
		}
		// resize the row height using a MeasureItem listener
		crossNavTable.addListener(SWT.MeasureItem, new Listener() {
			public void handleEvent(Event event) {
				// height cannot be per row so simply set
				event.height = 20;
			}
		});

		// listener per measures --> right click
		crossNavTable.addListener(SWT.MouseDown, new Listener() {
			public void handleEvent(Event event) {
				if (event.button == 3) {
					TableItem[] selection = crossNavTable.getSelection();
					// find the column
					Link link = LinkBO.getLinkByHierarchyAndLevel(geoDocument,
							selection[0].getText(0), selection[0].getText(1));

					if (link != null) {
						createOptionalShell(sectionClient, link);
					} else {
						MessageDialog.openWarning(sectionClient.getShell(),
								"Warning", "No link defined");
					}
				}
			}
		});
		crossNavTable.redraw();
	}

	private void createOptionalShell(Composite sectionClient, Link link){
		
	}
	private void createTableItemRow(TableItem item, Table crossNavTable){
		TableEditor editor = new TableEditor(crossNavTable);
		Combo hierarchiesCombo = createHierachiesCombo(crossNavTable);
		editor.setEditor(hierarchiesCombo, item, 0);
		
		editor = new TableEditor(crossNavTable);
		Combo levelCombo = createLevelsCombo(crossNavTable, hierarchiesCombo.getText());
		editor.setEditor(levelCombo, item, 1);
		
		editor = new TableEditor(crossNavTable);
		Button insertParam = new Button(crossNavTable, SWT.PUSH);
		editor.setEditor(insertParam, item, 2);
		
		tableEditors.add(editor);
	}
	private Combo createHierachiesCombo(Table crossNavTable){
		Combo hierCombo = new Combo(crossNavTable, SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		Hierarchies hierarchies=HierarchyBO.getAllHierarchies(geoDocument);
		if(hierarchies != null && hierarchies.getHierarchy() != null){
			for(int i=0; i< hierarchies.getHierarchy().size(); i++){
				Hierarchy hier = hierarchies.getHierarchy().elementAt(i);
				String name = hier.getName();
				hierCombo.add(name);				
			}
		}
		return hierCombo;
	}
	private Combo createLevelsCombo(Table crossNavTable, String hierarchyName){
		Combo levelCombo = new Combo(crossNavTable, SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		if(hierarchyName != null && !hierarchyName.equals("")){
			Levels levels=LevelBO.getLevelsByHierarchyName(geoDocument, hierarchyName);
			if(levels != null && levels.getLevel()!= null){
				for(int i=0; i< levels.getLevel().size(); i++){
					Level level = levels.getLevel().elementAt(i);
					String name = level.getName();
					levelCombo.add(name);				
				}
			}
		}
		return levelCombo;
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
	public Vector<TableEditor> getTableEditors() {
		return tableEditors;
	}
	public void setTableEditors(Vector<TableEditor> tableEditors) {
		this.tableEditors = tableEditors;
	}

}
