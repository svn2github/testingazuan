package it.eng.spagobi.studio.documentcomposition.views;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;

public class DocumentPropertiesView extends ViewPart {

	private Label label;
	private DocumentComposition documentComp;
	Label idLabelName;
	Label idLabelValue;
	Label labelLabelName;
	Label labelLabelValue;
	Label nameLabelName;
	Label nameLabelValue;
	Label descriptionLabelName;
	Label descriptionLabelValue;
	Label typeLabelName;
	Label typeLabelValue;
	Label engineLabelName;
	Label engineLabelValue;
	Label dataSetLabelName;
	Label dataSetLabelValue;
	Label dataSourceLabelName;
	Label dataSourceLabelValue;

	Composite client;
	Table table;
	public static final int ID=0;
	public static final int LABEL=1;
	public static final int NAME=2;
	public static final int DESCRIPTION=3;
	public static final int TYPE=4;
	public static final int ENGINE=5;
	public static final int DATA_SET=6;
	public static final int DATA_SOURCE=7;
	public static final int STATE=8;

	public void setFocus() {
		label.setFocus();
	}

	public void init(IViewSite site) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site);
		//documentComp= (new ModelBO()).getModel();
	}

	public void viewSelectedProperties() {


	}



	public void createPartControl(Composite parent) {




		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		// Lets make a layout for the first section of the screen
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		// Creating the Screen
		Section section = toolkit.createSection(parent, Section.DESCRIPTION
				| Section.TITLE_BAR);
		section.setText("Properties of selected document"); //$NON-NLS-1$
		client = toolkit.createComposite(section, SWT.WRAP);
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);


		table = new Table (client, SWT.MULTI | SWT.BORDER | SWT.FULL_SELECTION);
		table.setLinesVisible (true);
		table.setHeaderVisible (true);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.heightHint = 200;
		table.setLayoutData(data);
		String[] titles = {"Property", "Value"};
		for (int i=0; i<titles.length; i++) {
			TableColumn column = new TableColumn (table, SWT.NONE);
			column.setText (titles [i]);
		}	

		TableItem item = new TableItem (table, SWT.NONE,ID);
		item.setText (0, "Id: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,LABEL);
		item.setText (0, "Label: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,NAME);
		item.setText (0, "Name: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,DESCRIPTION);
		item.setText (0, "Description: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,TYPE);
		item.setText (0, "Type: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,ENGINE);
		item.setText (0, "Engine: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,DATA_SET);
		item.setText (0, "Data Set: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,DATA_SOURCE);
		item.setText (0, "Data Source: ");
		item.setText (1, "");
		item = new TableItem (table, SWT.NONE,STATE);
		item.setText (0, "State: ");
		item.setText (1, "");
		for (int i=0; i<titles.length; i++) {
			table.getColumn (i).pack ();
		}	
		client.pack();
		
		//		
		//		
		//		idLabelName=new Label(client,SWT.NULL);
		//		idLabelName.setText("Id: ");
		//		//idLabelName.setVisible(false);
		//		idLabelValue=new Label(client,SWT.NULL);
		//		idLabelValue.setText("");
		//		idLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		//idLabelName.setVisible(false);
		//		
		//		labelLabelName=new Label(client,SWT.NULL);
		//		labelLabelName.setText("Label: ");
		//		labelLabelName.setVisible(false);
		//		labelLabelValue=new Label(client,SWT.NULL);
		//		labelLabelValue.setText("");
		//		labelLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		labelLabelName.setVisible(false);
		//		
		//		nameLabelName=new Label(client,SWT.NULL);
		//		nameLabelName.setText("Name: ");
		//		nameLabelName.setVisible(false);
		//		nameLabelValue=new Label(client,SWT.NULL);
		//		nameLabelValue.setText("");
		//		nameLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		nameLabelName.setVisible(false);
		//		
		//		descriptionLabelName=new Label(client,SWT.NULL);
		//		descriptionLabelName.setText("Description: ");
		//		descriptionLabelName.setVisible(false);
		//		descriptionLabelValue=new Label(client,SWT.NULL);
		//		descriptionLabelValue.setText("");
		//		descriptionLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		descriptionLabelName.setVisible(false);
		//
		//		typeLabelName=new Label(client,SWT.NULL);
		//		typeLabelName.setText("Type: ");
		//		typeLabelName.setVisible(false);
		//		typeLabelValue=new Label(client,SWT.NULL);
		//		typeLabelValue.setText("");
		//		typeLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		typeLabelName.setVisible(false);
		//		
		//		engineLabelName=new Label(client,SWT.NULL);
		//		engineLabelName.setText("Engine: ");
		//		engineLabelName.setVisible(false);
		//		engineLabelValue=new Label(client,SWT.NULL);
		//		engineLabelValue.setText("");
		//		engineLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		engineLabelName.setVisible(false);
		//
		//		dataSetLabelName=new Label(client,SWT.NULL);
		//		dataSetLabelName.setText("Data Set: ");
		//		dataSetLabelName.setVisible(false);
		//		dataSetLabelValue=new Label(client,SWT.NULL);
		//		dataSetLabelValue.setText("");
		//		dataSetLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		dataSetLabelName.setVisible(false);
		//
		//		dataSourceLabelName=new Label(client,SWT.NULL);
		//		dataSourceLabelName.setText("Data Source: ");
		//		dataSourceLabelName.setVisible(false);
		//		dataSourceLabelValue=new Label(client,SWT.NULL);
		//		dataSourceLabelValue.setText("");
		//		dataSourceLabelValue.setForeground(new Color(client.getDisplay(), new RGB(0,0,255)));
		//		dataSourceLabelName.setVisible(false);

		toolkit.paintBordersFor(client);
		section.setClient(client);
		viewSelectedProperties();

	}

	public void reloadProperties(MetadataDocument document){
			table.getItem(ID).setText(1, document.getId()!=null ? document.getId().toString() : "");
			table.getItem(LABEL).setText(1, document.getLabel()!=null ? document.getLabel() : "");
			table.getItem(NAME).setText(1, document.getName()!=null ? document.getName() : "");
			table.getItem(DESCRIPTION).setText(1, document.getDescription()!=null ? document.getDescription() : "");
			table.getItem(TYPE).setText(1, document.getType()!=null ? document.getType() : "");
			table.getItem(ENGINE).setText(1, document.getEngine()!=null ? document.getEngine() : "");
			table.getItem(DATA_SET).setText(1, document.getDataSet()!=null ? document.getDataSet() : "");
			table.getItem(DATA_SOURCE).setText(1, document.getDataSource()!=null ? document.getDataSource() : "");
			table.getItem(STATE).setText(1, document.getState()!=null ? document.getState() : "");

		client.layout();
		client.redraw();
	}


	@Override
	public IViewSite getViewSite() {
		// TODO Auto-generated method stub
		return super.getViewSite();
	}

	@Override
	public void init(IViewSite site, IMemento memento) throws PartInitException {
		// TODO Auto-generated method stub
		super.init(site, memento);
	}

	@Override
	public void saveState(IMemento memento) {
		// TODO Auto-generated method stub
		super.saveState(memento);
	}

	@Override
	protected void setContentDescription(String description) {
		// TODO Auto-generated method stub
		super.setContentDescription(description);
	}

	@Override
	public void setInitializationData(IConfigurationElement cfig,
			String propertyName, Object data) {
		// TODO Auto-generated method stub
		super.setInitializationData(cfig, propertyName, data);
	}

	@Override
	protected void setPartName(String partName) {
		// TODO Auto-generated method stub
		super.setPartName(partName);
	}

	public Label getLabel() {
		return label;
	}

	public void setLabel(Label label) {
		this.label = label;
	}

	public DocumentComposition getDocumentComp() {
		return documentComp;
	}

	public void setDocumentComp(DocumentComposition documentComp) {
		this.documentComp = documentComp;
	}

	public Label getIdLabelName() {
		return idLabelName;
	}

	public void setIdLabelName(Label idLabelName) {
		this.idLabelName = idLabelName;
	}

	public Label getIdLabelValue() {
		return idLabelValue;
	}

	public void setIdLabelValue(Label idLabelValue) {
		this.idLabelValue = idLabelValue;
	}

	public Label getLabelLabelName() {
		return labelLabelName;
	}

	public void setLabelLabelName(Label labelLabelName) {
		this.labelLabelName = labelLabelName;
	}

	public Label getLabelLabelValue() {
		return labelLabelValue;
	}

	public void setLabelLabelValue(Label labelLabelValue) {
		this.labelLabelValue = labelLabelValue;
	}

	public Label getNameLabelName() {
		return nameLabelName;
	}

	public void setNameLabelName(Label nameLabelName) {
		this.nameLabelName = nameLabelName;
	}

	public Label getNameLabelValue() {
		return nameLabelValue;
	}

	public void setNameLabelValue(Label nameLabelValue) {
		this.nameLabelValue = nameLabelValue;
	}

	public Label getDescriptionLabelName() {
		return descriptionLabelName;
	}

	public void setDescriptionLabelName(Label descriptionLabelName) {
		this.descriptionLabelName = descriptionLabelName;
	}

	public Label getDescriptionLabelValue() {
		return descriptionLabelValue;
	}

	public void setDescriptionLabelValue(Label descriptionLabelValue) {
		this.descriptionLabelValue = descriptionLabelValue;
	}

	public Label getTypeLabelName() {
		return typeLabelName;
	}

	public void setTypeLabelName(Label typeLabelName) {
		this.typeLabelName = typeLabelName;
	}

	public Label getTypeLabelValue() {
		return typeLabelValue;
	}

	public void setTypeLabelValue(Label typeLabelValue) {
		this.typeLabelValue = typeLabelValue;
	}

	public Label getEngineLabelName() {
		return engineLabelName;
	}

	public void setEngineLabelName(Label engineLabelName) {
		this.engineLabelName = engineLabelName;
	}

	public Label getEngineLabelValue() {
		return engineLabelValue;
	}

	public void setEngineLabelValue(Label engineLabelValue) {
		this.engineLabelValue = engineLabelValue;
	}

	public Label getDataSetLabelName() {
		return dataSetLabelName;
	}

	public void setDataSetLabelName(Label dataSetLabelName) {
		this.dataSetLabelName = dataSetLabelName;
	}

	public Label getDataSetLabelValue() {
		return dataSetLabelValue;
	}

	public void setDataSetLabelValue(Label dataSetLabelValue) {
		this.dataSetLabelValue = dataSetLabelValue;
	}

	public Label getDataSourceLabelName() {
		return dataSourceLabelName;
	}

	public void setDataSourceLabelName(Label dataSourceLabelName) {
		this.dataSourceLabelName = dataSourceLabelName;
	}

	public Label getDataSourceLabelValue() {
		return dataSourceLabelValue;
	}

	public void setDataSourceLabelValue(Label dataSourceLabelValue) {
		this.dataSourceLabelValue = dataSourceLabelValue;
	}

	public Composite getClient() {
		return client;
	}

	public void setClient(Composite client) {
		this.client = client;
	}







}
