package it.eng.spagobi.studio.geo.editors;

import it.eng.spagobi.studio.geo.Activator;
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
import it.eng.spagobi.studio.geo.editors.model.geo.LinkParam;

import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;

public class CrossNavigationDesigner {
	private GEOEditor editor=null;
	private Composite mainComposite;
	private GEODocument geoDocument;
	private Vector<TableEditor> tableEditors = new Vector<TableEditor>();
	
	final ImageDescriptor addIcon = AbstractUIPlugin
	.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/add.gif");
	
	final ImageDescriptor paramsIcon = AbstractUIPlugin
	.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "icons/linkParams.gif");
	
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

		String[] titles = { "  Hierarchy   ","     Level     ", "    ", "    "};
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
				event.height = 23;
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
		sectionClient.redraw();
	}

	private void createOptionalShell(Composite sectionClient, final Link link){
		final Shell dialog = new Shell (mainComposite.getDisplay(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText("Optional Parameter");

		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth = 10;
		formLayout.marginHeight = 10;
		formLayout.spacing = 10;
		dialog.setLayout (formLayout);

		Label label = new Label (dialog, SWT.RIGHT);
		label.setText ("Parameter Name:");
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
		labelType.setText ("Parameter Type:");		
		labelType.setLayoutData (data);

		final Combo textType = new Combo (dialog, SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
		textType.add("absolute");
		textType.add("relative");
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelType, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelType, 0, SWT.CENTER);

		textType.setLayoutData (data);
		
		//value
		data = new FormData ();
		data.width = 100;
		data.top = new FormAttachment(textType, 5);

		Label labelValue = new Label (dialog, SWT.RIGHT);
		labelValue.setText ("Parameter Value:");		
		labelValue.setLayoutData (data);
		
		final Text textValue = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelValue, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelValue, 0, SWT.CENTER);
		textValue.setLayoutData (data);
		
		//value
		data = new FormData ();
		data.width = 100;
		data.top = new FormAttachment(textValue, 5);

		Label labelScope = new Label (dialog, SWT.RIGHT);
		labelScope.setText ("Parameter Scope:");		
		labelScope.setLayoutData (data);
		
		final Text textScope = new Text (dialog, SWT.BORDER);
		data = new FormData ();
		data.width = 200;
		data.left = new FormAttachment (labelScope, 0, SWT.DEFAULT);
		data.right = new FormAttachment (100, 0);
		data.top = new FormAttachment (labelScope, 0, SWT.CENTER);
		data.bottom = new FormAttachment (cancel, 0, SWT.DEFAULT);
		textScope.setLayoutData (data);
		
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText ("Finish");
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
				String scope =textScope.getText();
				String value = textValue.getText();

				LinkParam param = new LinkParam();
				param.setName(name);
				param.setScope(scope);
				param.setType(type);
				param.setValue(value);
				
				LinkBO.addParamToLink(geoDocument, link, param);
				editor.setIsDirty(true);
				dialog.close ();
			}
		});
		
		Button more = new Button (dialog, SWT.PUSH);
		more.setText ("Add more");
		data = new FormData ();
		data.width = 60;
		data.right = new FormAttachment (ok, 0, SWT.DEFAULT);
		data.bottom = new FormAttachment (100, 0);
		more.setLayoutData (data);
		more.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				//create tree item
				String type = textType.getText();
				String name = text.getText();
				String scope =textScope.getText();
				String value = textValue.getText();

				LinkParam param = new LinkParam();
				param.setName(name);
				param.setScope(scope);
				param.setType(type);
				param.setValue(value);
				
				LinkBO.addParamToLink(geoDocument, link, param);
				editor.setIsDirty(true);
				createOptionalShell(dialog.getParent(), link);
				
				dialog.close ();
			}
		});
		dialog.setDefaultButton (ok);
		dialog.pack ();
		dialog.open ();

	}
	private void createTableItemRow(TableItem item, final Table crossNavTable){
		TableEditor editor = new TableEditor(crossNavTable);

		Combo hierarchiesCombo = createHierachiesCombo(crossNavTable);
		
		editor.minimumWidth = hierarchiesCombo.getBounds().x;
		editor.horizontalAlignment = SWT.CENTER;
		editor.grabHorizontal = true;
		editor.minimumHeight = hierarchiesCombo.getBounds().y;
		editor.verticalAlignment = SWT.CENTER;
		editor.grabVertical = true;
		editor.setEditor(hierarchiesCombo, item, 0);
		editor.layout();
		
		editor = new TableEditor(crossNavTable);

		
		final Combo levelCombo = createLevelsCombo(crossNavTable, hierarchiesCombo.getText());
		editor.minimumWidth = levelCombo.getBounds().x;
		editor.horizontalAlignment = SWT.CENTER;
		editor.grabHorizontal = true;
		editor.minimumHeight = levelCombo.getBounds().y;
		editor.verticalAlignment = SWT.CENTER;
		editor.grabVertical = true;
		editor.setEditor(levelCombo, item, 1);
		
		hierarchiesCombo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				String hierarchySelected = ((Combo)e.widget).getText();
				recreateLevelsCombo(crossNavTable,levelCombo, hierarchySelected);
				getEditor().setIsDirty(true);
				crossNavTable.redraw();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		editor = new TableEditor(crossNavTable);
		Button addNew = new Button(crossNavTable, SWT.PUSH);
		//addNew.setText("Add");
		Image addImage = addIcon.createImage();

		addNew.setSize(20, 20);
		
		addNew.setImage(addImage);
		editor.minimumWidth = addNew.getBounds().x;
		editor.horizontalAlignment = SWT.CENTER;
		editor.grabHorizontal = true;
		editor.minimumHeight = addNew.getBounds().y;
		editor.verticalAlignment = SWT.TOP;
		editor.grabVertical = true;
		editor.setEditor(addNew, item, 2);
		
		addNew.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				TableItem item = new TableItem(crossNavTable, SWT.NONE);
				createTableItemRow(item, crossNavTable);
				getEditor().setIsDirty(true);
				crossNavTable.redraw();
			}
		});
		addNew.pack();
		addNew.setToolTipText("Add new link");
		
		
		editor = new TableEditor(crossNavTable);
		Button addParameter = new Button(crossNavTable, SWT.PUSH);
		addParameter.setSize(paramsIcon.createImage().getBounds().width, paramsIcon.createImage().getBounds().height);
		addParameter.setImage(paramsIcon.createImage());
		
		addParameter.setToolTipText("Add parameters");
		editor.minimumWidth = addParameter.getBounds().x;
		editor.horizontalAlignment = SWT.CENTER;
		editor.grabHorizontal = true;
		editor.minimumHeight = addParameter.getBounds().y;
		editor.verticalAlignment = SWT.TOP;
		editor.grabVertical = true;
		editor.setEditor(addParameter, item, 3);
		
		
		addParameter.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				TableItem item =crossNavTable.getItem(new Point(((Button)e.widget).getBounds().x, ((Button)e.widget).getBounds().y));
				// find the link
				if(item != null){
					Link link = LinkBO.getLinkByHierarchyAndLevel(geoDocument,
							item.getText(0), item.getText(1));	
					if (link == null) {
						link = LinkBO.setNewLink(geoDocument, item.getText(0), item.getText(1));
					} 
					createOptionalShell(crossNavTable.getParent(), link);
				}else {
					MessageDialog.openWarning(crossNavTable.getParent().getShell(),
							"Warning", "No link selected");
				}
			}
		});
		addParameter.pack();
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
	private void recreateLevelsCombo(Table crossNavTable,Combo levelCombo, String hierarchyName){
		levelCombo.removeAll();
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
