/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

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

/**
 * @author cortella
 *
 */
public class AddBusinessTableWizardPageColumnSelection extends WizardPage {

	private AddBusinessTableWizardPagePhysicalTableSelection pageOneRef;
	private BusinessModel owner;
	private Table columns,fields;
	private TableItem[] columnsToImport;
	private boolean columnSelected = false;
	private PhysicalTable physicalTable;
	private Button bAddField, bRemoveField;
	
	/**
	 * @param pageName
	 */
	protected AddBusinessTableWizardPageColumnSelection(String pageName,
			BusinessModel owner,
			AddBusinessTableWizardPagePhysicalTableSelection columnSelectionPage,
			PhysicalTable physicalTable) {
		super(pageName);
		setTitle("Business Table Creation");
		setDescription("Please select the columns to use in your Business Table");
		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("wizards/createBC.png"));
		columnsToImport = null;
	    if (image!=null) setImageDescriptor(image);	
	    pageOneRef = columnSelectionPage;
	    this.physicalTable = physicalTable;
		this.owner = owner;
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		Group columnsGroup = createColumnsGroup(composite, SWT.SHADOW_ETCHED_IN);

		createPhysicalColumnsGroup(columnsGroup, SWT.NONE);
		
		createButtonsGroup(columnsGroup, SWT.NONE);

		createBusinessColumnsGroup(columnsGroup, SWT.NONE);
		
		addListeners();
        
 		//Important: Setting page control
 		setControl(composite);
 		
 		//first check
 		checkPageComplete();
 		
 		//workaround: if a PhysicalTable is passed, use It to populate widgets
 		if (physicalTable != null){
 			addTableItems(physicalTable);
 			pageOneRef.checkPageComplete();
 		}
	}

	public Group createColumnsGroup(Composite composite, int style){
		//Columns Group
		Group columnsGroup = new Group(composite, style);
		columnsGroup.setText("Columns selection");
		GridLayout glColumns = new GridLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		glColumns.numColumns = 3;
		glColumns.makeColumnsEqualWidth = false;
		columnsGroup.setLayout(glColumns);
		columnsGroup.setLayoutData(gd2);
		
		return columnsGroup;
	}
	
	public void createPhysicalColumnsGroup(Composite composite, int style){
		//Physical Columns Group -------------------------------
		Composite compPhysicalColumns = new Composite(composite, SWT.NONE);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compPhysicalColumns.setLayout(glL);
		compPhysicalColumns.setLayoutData(gdL);
		Label lblLeftTab = new Label(compPhysicalColumns,SWT.NONE);
		lblLeftTab.setText("Physical Table Columns: ");
 		columns = new Table(compPhysicalColumns, SWT.BORDER | SWT.MULTI);
 		columns.setLayoutData(gdL);
	}
	
	public void createButtonsGroup(Composite composite, int style){
 		//Buttons Group -------------------------------
		Composite compButtons = new Composite(composite, SWT.NONE);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compButtons.setLayout(glC);
		bAddField = new Button(compButtons,SWT.FLAT);
		bAddField.setToolTipText("Add column as a Business Table Column");
		Image imageAdd = ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("arrow_right.png")).createImage();
	    if (imageAdd!=null) bAddField.setImage(imageAdd);
		bRemoveField = new Button(compButtons,SWT.FLAT);
		bRemoveField.setToolTipText("Remove column from Business Table");
		Image imageRem = ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("arrow_left.png")).createImage();
	    if (imageRem!=null) bRemoveField.setImage(imageRem);		
	}
	
	public void createBusinessColumnsGroup(Composite composite, int style){
		//Business Columns Group -------------------------------
		Composite compBusinessColumns = new Composite(composite, SWT.NONE);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compBusinessColumns.setLayout(glR);
		compBusinessColumns.setLayoutData(gdR);
		Label lblRightTab = new Label(compBusinessColumns,SWT.NONE);
		lblRightTab.setText("Business Table Columns: ");
 		fields = new Table(compBusinessColumns, SWT.BORDER | SWT.MULTI);
 		fields.setLayoutData(gdR);		
	}
	
	public void addListeners(){
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
	}
	
	//add the original physical columns as TableItem (in the left Table Widget)
	public void addTableItems(String tableName){		
		columns.removeAll();
		fields.removeAll();

		if (tableName != null) {
			//retrieve the Physical Table Columns
			PhysicalTable pTable = owner.getPhysicalModel().getTable(tableName);
			int numCols = pTable.getColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(columns, 0);
				PhysicalColumn pColumn = pTable.getColumns().get(i);
				//associate table item with the object It represents
				ti.setData(pColumn);
				ti.setText(pColumn.getName());
			}
		}
	}

	//add the original physical columns as TableItem (in the left Table Widget), whit PhysicalTable specified
	public void addTableItems(PhysicalTable physicalTable){		
		columns.removeAll();
		fields.removeAll();
		//retrieve the Physical Table Columns
		PhysicalTable pTable = physicalTable;
		int numCols = pTable.getColumns().size();
		for (int i=0; i<numCols; i++){
			TableItem ti = new TableItem(columns, 0);
			PhysicalColumn pColumn = pTable.getColumns().get(i);
			//associate table item with the object It represents
			ti.setData(pColumn);
			ti.setText(pColumn.getName());
		}

	}	
	
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if (pageOneRef.isColumnSelection()){
			if(fields.getItemCount() > 0){
				setColumnSelected(true);				
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
		else {
			setPageComplete(true);
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

	
	
	/**
	 * @param columnSelected the columnSelected to set
	 */
	public void setColumnSelected(boolean columnSelected) {
		this.columnSelected = columnSelected;
	}

	/**
	 * @return the columnSelected
	 */
	public boolean isColumnSelected() {
		return columnSelected;
	}	
}
