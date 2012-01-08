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
package it.eng.spagobi.meta.editor.multi;

import java.net.URL;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;

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
public class BusinessTableSelectionPage extends WizardPage {

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	private Table physicalTables;
	private Table businessTables;
	private Image physicalTableImage = null;
	private Image businessTableImage = null;
	private TableItem[] tablesToImport;

	
	/**
	 * @param pageName
	 */
	public BusinessTableSelectionPage(String pageName) {
		super(pageName);
		setTitle(RL.getString("multi.editor.wizard.businessclassselection.title"));
		setMessage(RL.getString("multi.editor.wizard.businessclassselection.message"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.selectTables") );
	    if (image!=null) {
	    	setImageDescriptor(image);
	    }
		ImageDescriptor physicalTableImageDescriptor = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.PhysicalTable") );
		physicalTableImage = null;
		if (physicalTableImageDescriptor!=null) {
			physicalTableImage = physicalTableImageDescriptor.createImage();
	    }
		
		ImageDescriptor dbTableImageDescriptor = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.BusinessTable") );
		if (dbTableImageDescriptor!=null) {
			businessTableImage = dbTableImageDescriptor.createImage();
	    }
		
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
        //Important: Setting page control
 		setControl(composite);
 		
		Group mainGroup = initMainGroup(composite,SWT.SHADOW_ETCHED_IN);
		initPhysicalTableGroup(mainGroup,SWT.NONE);
		initButtonsGroup(mainGroup,SWT.NONE);
		initBusinessTableGroup(mainGroup,SWT.NONE);

		//first check
 		checkPageComplete();
	}
	
	public Group initMainGroup(Composite parent, int style){
		Group mainGroup = new Group(parent, style);
		mainGroup.setText(RL.getString("multi.editor.wizard.businessclassselection.label"));
		GridLayout glColumns = new GridLayout();
		glColumns.numColumns = 3;
		glColumns.makeColumnsEqualWidth = false;
		mainGroup.setLayout(glColumns);
		mainGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		return mainGroup;
	}
	
	public void initPhysicalTableGroup(Composite parent, int style){
		Composite compPhysicalTable = new Composite(parent, style);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compPhysicalTable.setLayout(glL);
		compPhysicalTable.setLayoutData(gdL);
		Label lblLeftTab = new Label(compPhysicalTable,SWT.NONE);
		lblLeftTab.setText(RL.getString("multi.editor.wizard.businessclassselection.physicaltables.label"));
 		physicalTables = new Table(compPhysicalTable, SWT.BORDER | SWT.MULTI);
 		physicalTables.setLayoutData(gdL);		
	}	
	
	public void initBusinessTableGroup(Composite parent, int style){
		Composite compBusinessTable = new Composite(parent, style);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compBusinessTable.setLayout(glR);
		compBusinessTable.setLayoutData(gdR);
		Label lblRightTab = new Label(compBusinessTable,SWT.NONE);
		lblRightTab.setText(RL.getString("multi.editor.wizard.businessclassselection.businessclasses.label"));
 		businessTables = new Table(compBusinessTable, SWT.BORDER | SWT.MULTI);
 		businessTables.setLayoutData(gdR);		
	}
	
	public void initButtonsGroup(Composite parent, int style){
		Composite compButtons = new Composite(parent, style);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compButtons.setLayout(glC);
	    Button bAddAllField = new Button(compButtons,SWT.FLAT);
		bAddAllField.setToolTipText(RL.getString("multi.editor.wizard.businessclassselection.addallbutton.tooltip"));
		Image imageAddAll = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_right") ).createImage();
	    if (imageAddAll!=null) bAddAllField.setImage(imageAddAll);
		
		Button bAddBusinessTable = new Button(compButtons,SWT.FLAT);
		bAddBusinessTable.setToolTipText(RL.getString("multi.editor.wizard.businessclassselection.addbutton.tooltip"));
		Image imageAdd =ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_right") ).createImage();
	    if (imageAdd!=null) bAddBusinessTable.setImage(imageAdd);
		Button bRemoveBusinessTable = new Button(compButtons,SWT.FLAT);
		bRemoveBusinessTable.setToolTipText(RL.getString("multi.editor.wizard.businessclassselection.removebutton.tooltip"));
		Image imageRem = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_left") ).createImage();
	    if (imageRem!=null) bRemoveBusinessTable.setImage(imageRem);
	    
	    Button bRemoveAllField = new Button(compButtons,SWT.FLAT);
		bRemoveAllField.setToolTipText(RL.getString("multi.editor.wizard.businessclassselection.removeallbutton.tooltip"));
		Image imageRemAll = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_left") ).createImage();
	    if (imageRemAll!=null) bRemoveAllField.setImage(imageRemAll);
	    
	    addListenerButtons(bAddBusinessTable,bRemoveBusinessTable,bAddAllField,bRemoveAllField);
	}
	
	public void addListenerButtons(Button bAddBusinessTable, Button bRemoveBusinessTable, Button bAddAllField, Button bRemoveAllField ){
		//adding listener to Add button		
		bAddBusinessTable.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				TableItem[] tiMultiSel = null;
				//single selection
				if (physicalTables.getSelectionCount() == 1)
					tiSel = physicalTables.getSelection()[0];
				//multi selection
				else if (physicalTables.getSelectionCount() > 1){
					int selectionCount = physicalTables.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i=0; i<selectionCount; i++){
						tiMultiSel[i] = physicalTables.getSelection()[i];
					}
				}
				if (tiSel!= null){
					TableItem ti = new TableItem(businessTables, 0);
					ti.setText(tiSel.getText());
					ti.setImage(businessTableImage);
					physicalTables.remove(physicalTables.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(businessTables, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setImage(businessTableImage);				
					}
					physicalTables.remove(physicalTables.getSelectionIndices());
				}
				checkPageComplete();
			}
		});
		
		//adding listener to Remove button		
		bRemoveBusinessTable.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				TableItem[] tiMultiSel = null;
				//single selection
				if (businessTables.getSelectionCount() == 1)
					tiSel = businessTables.getSelection()[0];
				//multi selection
				else if (businessTables.getSelectionCount() > 1){
					int selectionCount = businessTables.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i=0; i<selectionCount; i++){
						tiMultiSel[i] = businessTables.getSelection()[i];
					}
				}
				if (tiSel!= null){
					TableItem ti = new TableItem(physicalTables, 0);
					ti.setText(tiSel.getText());
					ti.setImage(physicalTableImage);					
					businessTables.remove(businessTables.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(physicalTables, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setImage(physicalTableImage);												
					}
					businessTables.remove(businessTables.getSelectionIndices());
				}
				checkPageComplete();
	
			}
		}); 	
		
		//adding listener to Add All button		
 		bAddAllField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem[] tableToAdd = null;
				tableToAdd = physicalTables.getItems();
				
				//add Table to Business Model panel
				for (int i=0; i< tableToAdd.length; i++){
					TableItem ti = new TableItem(businessTables, 0);
					ti.setText(tableToAdd[i].getText());
					ti.setImage(businessTableImage);											
				}
				//Remove table from Physical Model panel
				physicalTables.removeAll();
				
				checkPageComplete();
			}
		});
 		
		//adding listener to Remove All button		
 		bRemoveAllField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem[] tableToRemove = null;
				tableToRemove = businessTables.getItems();
				
				//add table to Physical Model panel
				for (int i=0; i< tableToRemove.length; i++){
					TableItem ti = new TableItem(physicalTables, 0);
					ti.setText(tableToRemove[i].getText());
					ti.setImage(physicalTableImage);												
				}
				//Remove table from Business Model panel 
				businessTables.removeAll();
				
				checkPageComplete();
			}
		});		
		
	}
	//add the original physical columns as TableItem (in the left Table Widget)
	public void addTableItems(TableItem[] physicalTableImported){		
		businessTables.removeAll();
		physicalTables.removeAll();
		
		for (TableItem tableItem : physicalTableImported){
			TableItem ti = new TableItem(physicalTables, 0);
			ti.setText(tableItem.getText());
			ti.setImage(physicalTableImage);	
		}

	}	
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if(businessTables.getItemCount() > 0){				
			//store the Physical Columns selected
			//setErrorMessage(null);
			setTablesToImport(businessTables.getItems());
			setPageComplete(true);
		}
		/*
		else{			
			setErrorMessage("The Business Model hasn't any tables, please select at least one to continue");
			setPageComplete(false);
		}
		*/
	}
	
	/**
	 * @param tablesToImport the tablesToImport to set
	 */
	public void setTablesToImport(TableItem[] tablesToImport) {
		this.tablesToImport = tablesToImport;
	}

	/**
	 * @return the tablesToImport
	 */
	public TableItem[] getTablesToImport() {
		return tablesToImport;
	}	

}
