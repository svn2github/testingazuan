package it.eng.spagobi.meta.model.business.wizards;

import java.util.Collection;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.test.TestEditorPlugin;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
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

public class EditBusinessColumnsWizardPage extends WizardPage {

	private Table columns,fields;
	private TableItem[] columnsToImport;
	private BusinessTable businessTable;
	private BusinessView businessView;
	
	protected EditBusinessColumnsWizardPage(String pageName, BusinessColumnSet businessColumnSet) {
		super(pageName);
		setTitle("Edit Business Table Columns");
		setDescription("Please select the columns to add in your Business Table");
		ImageDescriptor image =	ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("wizards/createBC.png"));	
	    if (image!=null) setImageDescriptor(image);	
	    
	    if (businessColumnSet instanceof BusinessTable)
	    	this.businessTable = (BusinessTable)businessColumnSet;
	    else if (businessColumnSet instanceof BusinessView)
	    	this.businessView = (BusinessView)businessColumnSet;
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
		lblLeftTab.setText("Physical Table Columns: ");
 		columns = new Table(compLeft, SWT.BORDER | SWT.MULTI);
 		columns.setLayoutData(gdL);
 		

 		//Center buttons -------------------------------
		Composite compCenter = new Composite(columnsGroup, SWT.NONE);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compCenter.setLayout(glC);
		Button bAddField = new Button(compCenter,SWT.FLAT);
		bAddField.setToolTipText("Add column as a Business Table Column");
		Image imageAdd = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("arrow_right.png")).createImage();
	    if (imageAdd!=null) bAddField.setImage(imageAdd);
		Button bRemoveField = new Button(compCenter,SWT.FLAT);
		bRemoveField.setToolTipText("Remove column from Business Table");
		Image imageRem = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("arrow_left.png")).createImage();
	    if (imageRem!=null) bRemoveField.setImage(imageRem);
		
		//Right table -------------------------------
		Composite compRight = new Composite(columnsGroup, SWT.NONE);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compRight.setLayout(glR);
		compRight.setLayoutData(gdR);
		Label lblRightTab = new Label(compRight,SWT.NONE);
		lblRightTab.setText("Business Table Columns: ");
 		fields = new Table(compRight, SWT.BORDER | SWT.MULTI);
 		fields.setLayoutData(gdR);
 	
	
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
					TableItem ti = new TableItem(fields, 0);
					ti.setText(tiSel.getText());
					ti.setData(tiSel.getData());
					columns.remove(columns.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(fields, 0);
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
				if (fields.getSelectionCount() == 1)
					tiSel = fields.getSelection()[0];
				//multi selection
				else if (fields.getSelectionCount() > 1){
					int selectionCount = fields.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i=0; i<selectionCount; i++){
						tiMultiSel[i] = fields.getSelection()[i];
					}
				}
				if (tiSel!= null){
					TableItem ti = new TableItem(columns, 0);
					ti.setText(tiSel.getText());
					ti.setData(tiSel.getData());					
					fields.remove(fields.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(columns, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setData(tiMultiSel[i].getData());											
					}
					fields.remove(fields.getSelectionIndices());
				}
				checkPageComplete();

			}
		}); 	
 		
 		//populate tables
 		addTableItems();
 		
 		//first check
 		checkPageComplete(); 		
		
        //Important: Setting page control
 		setControl(composite);
	}

	//add the original physical columns as TableItem (in the left Table Widget)
	public void addTableItems(){		
		columns.removeAll();
		fields.removeAll();
		
		//Get Columns for a simple Business Tables
		if (businessTable != null) {
			//retrieve the Physical Table Columns
			PhysicalTable pTable = businessTable.getPhysicalTable();
			int numCols = pTable.getColumns().size();
			for (int i=0; i<numCols; i++){
				PhysicalColumn pColumn = pTable.getColumns().get(i);
				//check if a corresponding Business Column already exist in the Business Table
				if ( businessTable.getColumn(pColumn) == null ){
					TableItem ti = new TableItem(columns, 0);
					//associate table item with the object It represents
					ti.setData(pColumn);
					ti.setText(pColumn.getName());
				}
			}
			
			//retrieve Business Table Columns
			numCols = businessTable.getColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(fields, 0);
				BusinessColumn bColumn = businessTable.getColumns().get(i);
				//associate table item with the object It represents
				ti.setData(bColumn);
				ti.setText(bColumn.getName());
			}
			
		}
		//--------------
		//Get Columns for a Business View
		else if (businessView != null){
			//retrieve the Physical Table Columns
			Collection<PhysicalTable> physicalTables = businessView.getPhysicalTables();
			int numCols;
			for (PhysicalTable physicalTable:physicalTables){
				numCols = physicalTable.getColumns().size();
				for (int i=0; i<numCols; i++){
					PhysicalColumn pColumn = physicalTable.getColumns().get(i);
					//check if a corresponding Business Column already exist in the Business View
					if ( businessView.getColumn(pColumn) == null ){
						TableItem ti = new TableItem(columns, 0);
						//associate table item with the object It represents
						ti.setData(pColumn);
						ti.setText(pColumn.getName());
					}
				}
			}
			
			//retrieve Business View Columns
			numCols = businessView.getColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(fields, 0);
				BusinessColumn bColumn = businessView.getColumns().get(i);
				//associate table item with the object It represents
				ti.setData(bColumn);
				ti.setText(bColumn.getName());
			}
		}
		//------
	}
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if(fields.getItemCount() > 0){				
			//store the Physical Columns selected
			setErrorMessage(null);
			setColumnsToImport(fields.getItems());
			setPageComplete(true);
		}
		else{			
			setErrorMessage("This Business Table hasn't columns, please select at least one to continue");
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
