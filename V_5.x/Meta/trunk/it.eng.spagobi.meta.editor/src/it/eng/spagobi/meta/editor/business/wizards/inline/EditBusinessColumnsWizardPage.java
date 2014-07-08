/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.net.URL;
import java.util.Collection;

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
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	protected EditBusinessColumnsWizardPage(String pageName, BusinessColumnSet businessColumnSet) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.editbusinessattributes.title"));
		setDescription(RL.getString("business.editor.wizard.editbusinessattributes.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
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
		columnsGroup.setText(RL.getString("business.editor.wizard.editbusinessattributes.label"));
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
		lblLeftTab.setText(RL.getString("business.editor.wizard.editbusinessattributes.physicalcolumns.label"));
 		columns = new Table(compLeft, SWT.BORDER | SWT.MULTI);
 		columns.setLayoutData(gdL);
 		

 		//Center buttons -------------------------------
		Composite compCenter = new Composite(columnsGroup, SWT.NONE);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compCenter.setLayout(glC);
	    Button bAddAllField = new Button(compCenter,SWT.FLAT);
		bAddAllField.setToolTipText(RL.getString("business.editor.wizard.editbusinessattributes.addallbutton"));
		Image imageAddAll = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_right") ).createImage();
	    if (imageAddAll!=null) bAddAllField.setImage(imageAddAll);
	    
		Button bAddField = new Button(compCenter,SWT.FLAT);
		bAddField.setToolTipText(RL.getString("business.editor.wizard.editbusinessattributes.addbutton"));
		Image imageAdd = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_right") ).createImage();
	    if (imageAdd!=null) bAddField.setImage(imageAdd);
		Button bRemoveField = new Button(compCenter,SWT.FLAT);
		bRemoveField.setToolTipText(RL.getString("business.editor.wizard.editbusinessattributes.removebutton"));
		Image imageRem = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_left") ).createImage();
	    if (imageRem!=null) bRemoveField.setImage(imageRem);
	    
		Button bRemoveAllField = new Button(compCenter,SWT.FLAT);
		bRemoveAllField.setToolTipText(RL.getString("business.editor.wizard.editbusinessattributes.removeallbutton"));
		Image imageRemAll = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_left") ).createImage();
	    if (imageRemAll!=null) bRemoveAllField.setImage(imageRemAll);
	    
		
		//Right table -------------------------------
		Composite compRight = new Composite(columnsGroup, SWT.NONE);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compRight.setLayout(glR);
		compRight.setLayoutData(gdR);
		Label lblRightTab = new Label(compRight,SWT.NONE);
		lblRightTab.setText(RL.getString("business.editor.wizard.editbusinessattributes.businessattributes.label"));
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
 		
		//adding listener to Add All button		
 		bAddAllField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem[] columnToAdd = null;
				columnToAdd = columns.getItems();
				
				//add Fields to Business Class panel
				for (int i=0; i< columnToAdd.length; i++){
					TableItem ti = new TableItem(fields, 0);
					ti.setText(columnToAdd[i].getText());
					ti.setData(columnToAdd[i].getData());											
				}
				//Remove columns from Physical Table panel
				columns.removeAll();
				
				checkPageComplete();
			}
		});
 		
		//adding listener to Remove All button		
 		bRemoveAllField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem[] fieldsToRemove = null;
				fieldsToRemove = fields.getItems();
				
				//add Fields to Physical Table panel
				for (int i=0; i< fieldsToRemove.length; i++){
					TableItem ti = new TableItem(columns, 0);
					ti.setText(fieldsToRemove[i].getText());
					ti.setData(fieldsToRemove[i].getData());											
				}
				//Remove columns from Business Class panel 
				fields.removeAll();
				
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
			int numCols;
			if (pTable != null){
				numCols = pTable.getColumns().size();
				for (int i=0; i<numCols; i++){
					PhysicalColumn pColumn = pTable.getColumns().get(i);
					//check if a corresponding Business Column already exist in the Business Table
					if ( businessTable.getSimpleBusinessColumn(pColumn) == null ){
						TableItem ti = new TableItem(columns, 0);
						//associate table item with the object It represents
						ti.setData(pColumn);
						ti.setText(pColumn.getName());
					}
				}
			}

						//retrieve Business Table Columns
			numCols = businessTable.getSimpleBusinessColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(fields, 0);
				BusinessColumn bColumn = businessTable.getSimpleBusinessColumns().get(i);
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
					if ( businessView.getSimpleBusinessColumn(pColumn) == null ){
						TableItem ti = new TableItem(columns, 0);
						//associate table item with the object It represents
						ti.setData(pColumn);
						ti.setText(pColumn.getName());
					}
				}
			}
			
			//retrieve Business View Columns
			numCols = businessView.getSimpleBusinessColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(fields, 0);
				BusinessColumn bColumn = businessView.getSimpleBusinessColumns().get(i);
				//associate table item with the object It represents
				ti.setData(bColumn);
				ti.setText(bColumn.getName());
			}
		}
		//------
	}
	
	//add the physical columns of the specified PhyscalTable as TableItem (in the left Table Widget)
	public void addTableItems(String physicalTableName){		
		columns.removeAll();
		fields.removeAll();
		
		//Get Columns for a simple Business Table
		if (businessTable != null) {
			//retrieve the Physical Table Columns
			PhysicalTable pTable = businessTable.getModel().getPhysicalModel().getTable(physicalTableName);
			int numCols;
			if (pTable != null){
				numCols = pTable.getColumns().size();
				for (int i=0; i<numCols; i++){
					PhysicalColumn pColumn = pTable.getColumns().get(i);
					//check if a corresponding Business Column already exist in the Business Table
					if ( businessTable.getSimpleBusinessColumn(pColumn) == null ){
						TableItem ti = new TableItem(columns, 0);
						//associate table item with the object It represents
						ti.setData(pColumn);
						ti.setText(pColumn.getName());
					}
				}
			}

			//retrieve Business Table Columns
			numCols = businessTable.getSimpleBusinessColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(fields, 0);
				BusinessColumn bColumn = businessTable.getSimpleBusinessColumns().get(i);
				//associate table item with the object It represents
				ti.setData(bColumn);
				ti.setText(bColumn.getName());
			}
			
		}
		
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
			setErrorMessage(RL.getString("business.editor.wizard.editbusinessattributes.error"));
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
