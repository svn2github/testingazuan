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
package it.eng.spagobi.meta.editor.multi.wizards;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.editor.multi.BusinessTableSelectionPage;
import it.eng.spagobi.meta.editor.multi.DSEBridge;

import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
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
public class PhysicalTableSelectionPage extends WizardPage {

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	private Table databaseTables;
	private Table physicalTables;
	private DSEBridge dseBridge;
	private TableItem[] tablesToImport;
	private Image physicalTableImage = null;
	private Image dbTableImage = null;
	private BusinessTableSelectionPage businessTableSelectionPageRef;
	
	/**
	 * @param pageName
	 */
	protected PhysicalTableSelectionPage(String pageName) {
		super(pageName);
		setTitle("Select Tables");
		setMessage("Select the tables to import in your Physical Model");
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.selectTables") );
	    if (image!=null) {
	    	setImageDescriptor(image);
	    }
		ImageDescriptor physicalTableImageDescriptor = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.PhysicalTable") );
		physicalTableImage = null;
		if (physicalTableImageDescriptor!=null) {
			physicalTableImage = physicalTableImageDescriptor.createImage();
	    }
		
		ImageDescriptor dbTableImageDescriptor = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.DatabaseTable") );
		if (dbTableImageDescriptor!=null) {
	    	dbTableImage = dbTableImageDescriptor.createImage();
	    }
		
	    dseBridge = new DSEBridge();

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
		initDatabaseTableGroup(mainGroup,SWT.NONE);
		initButtonsGroup(mainGroup,SWT.NONE);
		initPhysicalTableGroup(mainGroup,SWT.NONE);
 	
		//first check
 		checkPageComplete(); 		
	}
	
	public Group initMainGroup(Composite parent, int style){
		Group mainGroup = new Group(parent, style);
		mainGroup.setText("Tables selection");
		GridLayout glColumns = new GridLayout();
		glColumns.numColumns = 3;
		glColumns.makeColumnsEqualWidth = false;
		mainGroup.setLayout(glColumns);
		mainGroup.setLayoutData(new GridData(GridData.FILL_BOTH));
		return mainGroup;
	}
	
	public void initDatabaseTableGroup(Composite parent, int style){
		Composite compDatabaseTable = new Composite(parent, style);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compDatabaseTable.setLayout(glL);
		compDatabaseTable.setLayoutData(gdL);
		Label lblLeftTab = new Label(compDatabaseTable,SWT.NONE);
		lblLeftTab.setText("Original Database Tables: ");
 		databaseTables = new Table(compDatabaseTable, SWT.BORDER | SWT.MULTI);
 		databaseTables.setLayoutData(gdL);		
	}
	
	public void initButtonsGroup(Composite parent, int style){
		Composite compButtons = new Composite(parent, style);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compButtons.setLayout(glC);
		Button bAddPhysicalTable = new Button(compButtons,SWT.FLAT);
		bAddPhysicalTable.setToolTipText("Add table as a Physical Table");
		Image imageAdd = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_right") ).createImage();
	    if (imageAdd!=null) bAddPhysicalTable.setImage(imageAdd);
		Button bRemovePhysicalTable = new Button(compButtons,SWT.FLAT);
		bRemovePhysicalTable.setToolTipText("Remove table from Physical Model");
		Image imageRem = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_left") ).createImage();
	    if (imageRem!=null) bRemovePhysicalTable.setImage(imageRem);
	    addListenerButtons(bAddPhysicalTable,bRemovePhysicalTable);
	}
	
	public void initPhysicalTableGroup(Composite parent, int style){
		Composite compPhysicalTable = new Composite(parent, style);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compPhysicalTable.setLayout(glR);
		compPhysicalTable.setLayoutData(gdR);
		Label lblRightTab = new Label(compPhysicalTable,SWT.NONE);
		lblRightTab.setText("Physical Model Tables: ");
 		physicalTables = new Table(compPhysicalTable, SWT.BORDER | SWT.MULTI);
 		physicalTables.setLayoutData(gdR);		
	}
	
	public void addListenerButtons(Button bAddPhysicalTable, Button bRemovePhysicalTable ){
		//adding listener to Add button		
		bAddPhysicalTable.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				TableItem[] tiMultiSel = null;
				//single selection
				if (databaseTables.getSelectionCount() == 1)
					tiSel = databaseTables.getSelection()[0];
				//multi selection
				else if (databaseTables.getSelectionCount() > 1){
					int selectionCount = databaseTables.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i=0; i<selectionCount; i++){
						tiMultiSel[i] = databaseTables.getSelection()[i];
					}
				}
				if (tiSel!= null){
					TableItem ti = new TableItem(physicalTables, 0);
					ti.setText(tiSel.getText());
					ti.setImage(physicalTableImage);
					databaseTables.remove(databaseTables.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(physicalTables, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setImage(physicalTableImage);				
					}
					databaseTables.remove(databaseTables.getSelectionIndices());
				}
				checkPageComplete();
			}
		});
		
		//adding listener to Remove button		
		bRemovePhysicalTable.addListener(SWT.Selection, new Listener() {		
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
					TableItem ti = new TableItem(databaseTables, 0);
					ti.setText(tiSel.getText());
					ti.setImage(dbTableImage);					
					physicalTables.remove(physicalTables.getSelectionIndex());
				}
				if (tiMultiSel != null){
					for (int i=0; i< tiMultiSel.length; i++){
						TableItem ti = new TableItem(databaseTables, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setImage(dbTableImage);												
					}
					physicalTables.remove(physicalTables.getSelectionIndices());
				}
				checkPageComplete();
	
			}
		}); 	
	}

	
	//add the original physical columns as TableItem (in the left Table Widget)
	public void addTableItems(String ConnectionName, String catalog, String schema){		
		ResultSet tableRs;
		DatabaseMetaData dbMeta;

		databaseTables.removeAll();
		physicalTables.removeAll();
		
		//connect to database and retrieve table list
		try {
			Connection connection = dseBridge.connect(ConnectionName);
			dbMeta = connection.getMetaData();
			tableRs = dbMeta.getTables(catalog, schema, null, new String[]{"TABLE", "VIEW"});
			while (tableRs.next()) {
				String tableName = tableRs.getString("TABLE_NAME");
				TableItem ti = new TableItem(databaseTables, 0);
				ti.setText(tableName);
				ti.setImage(dbTableImage);
			}
			//release result set resources
			tableRs.close();
			//close connection
			//connection.close();
		}
		catch(Throwable t) {
			throw new RuntimeException("Error in database table retrieving", t);
		}		
	}
	
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if(physicalTables.getItemCount() > 0){				
			//store the Physical Columns selected
			setErrorMessage(null);
			setTablesToImport(physicalTables.getItems());
			setPageComplete(true);
		}
		else{			
			setErrorMessage("The Physical Model hasn't any tables, please select at least one to continue");
			setPageComplete(false);
		}
	}
	
	//Set next page data
	public IWizardPage getNextPage() {
    	IWizardPage nextPage = super.getNextPage();
    	if (nextPage instanceof BusinessTableSelectionPage) {
    		getBusinessTableSelectionPageRef().addTableItems(getTablesToImport());
    	}  	
        return nextPage;
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

	/**
	 * @param businessTableSelectionPageRef the businessTableSelectionPageRef to set
	 */
	public void setBusinessTableSelectionPageRef(
			BusinessTableSelectionPage businessTableSelectionPageRef) {
		this.businessTableSelectionPageRef = businessTableSelectionPageRef;
	}

	/**
	 * @return the businessTableSelectionPageRef
	 */
	public BusinessTableSelectionPage getBusinessTableSelectionPageRef() {
		return businessTableSelectionPageRef;
	}

}
