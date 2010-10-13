package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class AddBusinessIdentifierWizardPageTwo extends WizardPage {
	private Table columns,columnsIdentifier;
	private Label lErr;
	private TableItem[] columnsToImport;
		
	protected AddBusinessIdentifierWizardPageTwo(String pageName) {
		super(pageName);
		setTitle("Business Identifier Creation");
		setDescription("Please select the columns to use in your Business Identifier");
		ImageDescriptor image = Activator.getImageDescriptor("wizards/createBI.png");
	    if (image!=null) setImageDescriptor(image);	
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		//Columns Group
		Group columnsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		columnsGroup.setText("Columns selection");
		GridLayout glColumns = new GridLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		glColumns.numColumns = 3;
		glColumns.makeColumnsEqualWidth = false;
		columnsGroup.setLayout(glColumns);
		columnsGroup.setLayoutData(gd2);
        
		//Left table -------------------------------
		Composite compLeft = new Composite(columnsGroup, SWT.NONE);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compLeft.setLayout(glL);
		compLeft.setLayoutData(gdL);
		Label lblLeftTab = new Label(compLeft,SWT.NONE);
		lblLeftTab.setText("Business Table Columns: ");
 		columns = new Table(compLeft, SWT.BORDER | SWT.MULTI);
 		columns.setLayoutData(gdL);
 		

 		//Center buttons -------------------------------
		Composite compCenter = new Composite(columnsGroup, SWT.NONE);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compCenter.setLayout(glC);
		Button bAddField = new Button(compCenter,SWT.FLAT);
		bAddField.setToolTipText("Add column as a Business Identifier Column");
		Image imageAdd = Activator.getImageDescriptor("arrow_right.png").createImage();
	    if (imageAdd!=null) bAddField.setImage(imageAdd);
		Button bRemoveField = new Button(compCenter,SWT.FLAT);
		bRemoveField.setToolTipText("Remove column from Business Identifier");
		Image imageRem = Activator.getImageDescriptor("arrow_left.png").createImage();
	    if (imageRem!=null) bRemoveField.setImage(imageRem);
		
		//Right table -------------------------------
		Composite compRight = new Composite(columnsGroup, SWT.NONE);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compRight.setLayout(glR);
		compRight.setLayoutData(gdR);
		Label lblRightTab = new Label(compRight,SWT.NONE);
		lblRightTab.setText("Business Identifier Columns: ");
 		columnsIdentifier = new Table(compRight, SWT.BORDER | SWT.MULTI);
 		columnsIdentifier.setLayoutData(gdR);
 	
 		//Bottom error label
		Composite err = new Composite(composite, SWT.NULL);
		GridLayout glerr = new GridLayout();
		glerr.numColumns = 2;
		err.setLayout(glerr);	
		lErr = new Label(err, SWT.NULL);
		
		//adding listener to Add button		
 		bAddField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				TableItem[] tiMultiSel = null;
				//single selection
				if (columns.getSelectionCount() == 1)
					tiSel = columns.getSelection()[0];
				//multi selection
				else if (columns.getSelectionCount() > 1){
					int selectionCount = columns.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i=0; i<selectionCount; i++){
						tiMultiSel[i] = columns.getSelection()[i];
					}
				}
				if (tiSel!= null){
					TableItem ti = new TableItem(columnsIdentifier, 0);
					ti.setText(tiSel.getText());
					ti.setData(tiSel.getData());
					columns.remove(columns.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(columnsIdentifier, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setData(tiMultiSel[i].getData());											
					}
					columns.remove(columns.getSelectionIndices());
				}
				checkPageComplete();
			}
		});
 		
		//adding listener to Remove button		
 		bRemoveField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				TableItem[] tiMultiSel = null;
				//single selection
				if (columnsIdentifier.getSelectionCount() == 1)
					tiSel = columnsIdentifier.getSelection()[0];
				//multi selection
				else if (columnsIdentifier.getSelectionCount() > 1){
					int selectionCount = columnsIdentifier.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i=0; i<selectionCount; i++){
						tiMultiSel[i] = columnsIdentifier.getSelection()[i];
					}
				}
				if (tiSel!= null){
					TableItem ti = new TableItem(columns, 0);
					ti.setText(tiSel.getText());
					ti.setData(tiSel.getData());					
					columnsIdentifier.remove(columnsIdentifier.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(columns, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setData(tiMultiSel[i].getData());											
					}
					columnsIdentifier.remove(columnsIdentifier.getSelectionIndices());
				}
				checkPageComplete();

			}
		}); 	
 		
 		//first check
 		checkPageComplete(); 		
		
        //Important: Setting page control
 		setControl(composite);

	}
	
	//add Business Columns as widget's TableItem
	public void addTableItems(String tableName){
		columns.removeAll();
		columnsIdentifier.removeAll();
		if (tableName != null) {
			//retrieve the Business Table Columns
			BusinessTable bTable = CoreSingleton.getInstance().getBusinessModel().getTable(tableName);
			int numCols = bTable.getColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(columns, 0);
				BusinessColumn bColumn = bTable.getColumns().get(i);
				//associate table item with the object It represents
				ti.setData(bColumn);
				ti.setText(bColumn.getName());
			}
		}
		
		//Checking if a Business Identifier for this Business Table already exists
		CoreSingleton cs = CoreSingleton.getInstance();
		BusinessTable bizTable = cs.getBusinessModel().getTable(tableName);
		BusinessIdentifier bizIdentifier = cs.getBusinessModel().getIdentifier(bizTable);
		if (bizIdentifier != null){
			EList<BusinessColumn> bizColumns = bizIdentifier.getColumns();
			for (BusinessColumn col : bizColumns){
				TableItem[] items = columns.getItems();
				if (items != null){
					for (int i=0; i<items.length; i++){
						if (items[i].getText().equals(col.getName())){
							TableItem ti = new TableItem(columnsIdentifier, 0);
							ti.setText(items[i].getText());
							ti.setData(items[i].getData());
							columns.remove(i);
							break;
						}
				    }
				}
			}
		}
		checkPageComplete(); 
	}
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
			if(columnsIdentifier.getItemCount()!=0){
				setErrorMessage(null);
				setColumnsToImport(columnsIdentifier.getItems());
				setPageComplete(true);
			}
			else{			
				setErrorMessage("This Business Identifier hasn't columns, please select at least one to continue");
				setPageComplete(false);
			}

	}
	
	/**
	 * @param columnsToImport the columnsToImport to set
	 */
	public void setColumnsToImport(TableItem[] columnsToImport) {
		this.columnsToImport = columnsToImport;
	}

	/**
	 * @return the columnsToImport
	 */
	public TableItem[] getColumnsToImport() {
		return columnsToImport;
	}	
}
