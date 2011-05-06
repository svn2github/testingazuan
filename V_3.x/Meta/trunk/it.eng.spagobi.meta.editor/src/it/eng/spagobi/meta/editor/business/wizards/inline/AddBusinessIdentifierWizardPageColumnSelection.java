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

import java.net.URL;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;

import org.eclipse.emf.common.util.EList;
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
public class AddBusinessIdentifierWizardPageColumnSelection extends WizardPage {
	
	private Table columns,columnsIdentifier;
	private Label lErr;
	private TableItem[] columnsToImport;
	private String defaultTable;
	private BusinessColumnSet businessColumnSet;
	private Button bAddField, bRemoveField;
	private Button bAddAllField;
	private Button bRemoveAllField;
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	/**
	 * @param pageName
	 */
	protected AddBusinessIdentifierWizardPageColumnSelection(String pageName, String defaultTable, BusinessColumnSet businessColumnSet) {
		super(pageName);
		setTitle("Business Identifier Creation");
		setDescription("Please select the attributes to use in your Business Identifier");
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBI") );
	    if (image!=null) setImageDescriptor(image);	
		this.defaultTable = defaultTable;
		this.businessColumnSet = businessColumnSet;
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

		createBusinessColumnsGroup(columnsGroup, SWT.NONE); 
		
		createButtonsGroup(columnsGroup, SWT.NONE);

        createBusinessIdentifierColumnsGroup(columnsGroup, SWT.NONE);

        createErrorLabel(composite, SWT.NULL);

		addListeners();
 		
 		if (defaultTable != null)
 			addTableItems(defaultTable);
 		
 		//first check
 		checkPageComplete(); 		
		
        //Important: Setting page control
 		setControl(composite);

	}
	
	public Group createColumnsGroup(Composite composite, int style){
		//Columns Group
		Group columnsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		columnsGroup.setText("Attributes selection");
		GridLayout glColumns = new GridLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		glColumns.numColumns = 3;
		glColumns.makeColumnsEqualWidth = false;
		columnsGroup.setLayout(glColumns);
		columnsGroup.setLayoutData(gd2);
		return columnsGroup;
	}
	
	public void createBusinessColumnsGroup(Composite composite, int style){
		//BusinessColumns group -------------------------------
		Composite compBusinessColumns = new Composite(composite, style);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compBusinessColumns.setLayout(glL);
		compBusinessColumns.setLayoutData(gdL);
		Label lblBusinessColumns = new Label(compBusinessColumns,SWT.NONE);
		lblBusinessColumns.setText(RL.getString("business.editor.wizard.addbusinessclass.columnselection"));
 		columns = new Table(compBusinessColumns, SWT.BORDER | SWT.MULTI);
 		columns.setLayoutData(gdL);		
	}
	
	public void createButtonsGroup(Composite composite, int style){
 		//Buttons Group -------------------------------
		Composite compButtons = new Composite(composite, style);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compButtons.setLayout(glC);
		
		bAddAllField = new Button(compButtons,SWT.FLAT);
		bAddAllField.setToolTipText("Add all attributes as a Business Identifier attributes");
		Image imageAddAll = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_right") ).createImage();
	    if (imageAddAll!=null) bAddAllField.setImage(imageAddAll);
	    
		bAddField = new Button(compButtons,SWT.FLAT);
		bAddField.setToolTipText("Add attribute as a Business Identifier attribute");
		Image imageAdd = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_right") ).createImage();
		if (imageAdd!=null) bAddField.setImage(imageAdd);
		bRemoveField = new Button(compButtons,SWT.FLAT);
		bRemoveField.setToolTipText("Remove attributes from Business Identifier");
		Image imageRem = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_left") ).createImage();
		if (imageRem!=null) bRemoveField.setImage(imageRem);
		
		bRemoveAllField = new Button(compButtons,SWT.FLAT);
		bRemoveAllField.setToolTipText("Remove all attributes from Business Identifier");
		Image imageRemAll = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_left") ).createImage();
	    if (imageRemAll!=null) bRemoveAllField.setImage(imageRemAll);
	}
	
	public void createErrorLabel(Composite composite, int style){
 		//Bottom error label
		Composite err = new Composite(composite,style);
		GridLayout glerr = new GridLayout();
		glerr.numColumns = 2;
		err.setLayout(glerr);	
		lErr = new Label(err, SWT.NULL);
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
 		
		//adding listener to Add All button		
 		bAddAllField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem[] columnToAdd = null;
				columnToAdd = columns.getItems();
				
				//add Fields to Identifier panel
				for (int i=0; i< columnToAdd.length; i++){
					TableItem ti = new TableItem(columnsIdentifier, 0);
					ti.setText(columnToAdd[i].getText());
					ti.setData(columnToAdd[i].getData());											
				}
				//Remove columns from Business Class panel
				columns.removeAll();
				
				checkPageComplete();
			}
		});
 		
		//adding listener to Remove All button		
 		bRemoveAllField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem[] fieldsToRemove = null;
				fieldsToRemove = columnsIdentifier.getItems();
				
				//add Fields to Business Class panel
				for (int i=0; i< fieldsToRemove.length; i++){
					TableItem ti = new TableItem(columns, 0);
					ti.setText(fieldsToRemove[i].getText());
					ti.setData(fieldsToRemove[i].getData());											
				}
				//Remove columns from Identifier panel 
				columnsIdentifier.removeAll();
				
				checkPageComplete();
			}
		});  		 		
	}
	
	public void createBusinessIdentifierColumnsGroup(Composite composite, int style){
		//Business Identifier Columns Group -------------------------------
		Composite compBusinessIdentifier = new Composite(composite, style);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compBusinessIdentifier.setLayout(glR);
		compBusinessIdentifier.setLayoutData(gdR);
		Label lblRightTab = new Label(compBusinessIdentifier,SWT.NONE);
		lblRightTab.setText("Business Identifier Attributes: ");
 		columnsIdentifier = new Table(compBusinessIdentifier, SWT.BORDER | SWT.MULTI);
 		columnsIdentifier.setLayoutData(gdR);
	}
	
	//add Business Columns as widget's TableItem
	public void addTableItems(String tableName){
		columns.removeAll();
		columnsIdentifier.removeAll();
		if (tableName != null) {
			//retrieve the Business Table Columns
			int numCols = businessColumnSet.getColumns().size();
			for (int i=0; i<numCols; i++){
				TableItem ti = new TableItem(columns, 0);
				BusinessColumn bColumn = businessColumnSet.getColumns().get(i);
				//associate table item with the object It represents
				ti.setData(bColumn);
				ti.setText(bColumn.getName());
			}
		}
		
		//Checking if a Business Identifier for this Business Table already exists
		BusinessModel businessModel = businessColumnSet.getModel();	
		BusinessIdentifier bizIdentifier = businessModel.getIdentifier(businessColumnSet);
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
				setErrorMessage("This Business Identifier hasn't attributes, please select at least one to continue");
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
