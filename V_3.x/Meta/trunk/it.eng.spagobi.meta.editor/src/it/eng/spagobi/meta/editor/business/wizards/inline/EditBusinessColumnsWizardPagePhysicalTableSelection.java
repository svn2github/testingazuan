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
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

/**
 * @author cortella
 *
 */
public class EditBusinessColumnsWizardPagePhysicalTableSelection extends
		WizardPage {

	private PhysicalTable physicalTable;
	private List tableList;
	private String tableSelected;
	private boolean columnSelection;
	private BusinessModel owner;
	private EditBusinessColumnsWizardPage columnSelectionPage;
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	/**
	 * @param pageName
	 */
	protected EditBusinessColumnsWizardPagePhysicalTableSelection(String pageName, BusinessModel owner) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.editbusinessattributes.title"));
		setDescription(RL.getString("business.editor.wizard.editbusinessattributes.physicaltable.selection.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
	    if (image!=null) setImageDescriptor(image);	
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
		
		//Physical Table List
		Group tableGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		tableGroup.setText(RL.getString("business.editor.wizard.addbusinessclass.physicaltableselection"));
		GridLayout glTable = new GridLayout();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		glTable.numColumns = 1;
		glTable.makeColumnsEqualWidth = false;
		tableGroup.setLayout(glTable);
		tableGroup.setLayoutData(gd);

		Composite compList = new Composite(tableGroup, SWT.NONE);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compList.setLayout(glL);
		compList.setLayoutData(gdL);
 		
		tableList = new List(compList, SWT.BORDER|SWT.SINGLE|SWT.V_SCROLL|SWT.H_SCROLL);
 		GridData gdList = new GridData(GridData.FILL_BOTH);
 		gdList.heightHint = 250;
 		tableList.setLayoutData(gdList);
 		
        //Important: Setting page control
 		setControl(composite);
 		
 		populateTableList();
 		
		//adding listener to List
 		tableList.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				String[] sel = tableList.getSelection();
				setTableSelected(sel[0]);
				checkPageComplete();
			}
		});
 		
 		checkPageComplete();


	}
	//populate the list with the Physical Tables' names
	private void populateTableList(){
		PhysicalModel pm = owner.getPhysicalModel();
		int numTables = pm.getTables().size();
		String tabName;
		for (int i = 0; i < numTables; i++){
			tabName = pm.getTables().get(i).getName();
			tableList.add(tabName);		
		}
	}

	//check if the right conditions to go forward occurred
	public void checkPageComplete(){
		if(tableSelected != null){
			setPageComplete(true);
			if (columnSelectionPage != null){
				setColumnSelection(true);
				if (physicalTable == null){
					columnSelectionPage.addTableItems(tableSelected);
				}
			}
		}
		else{			
			setPageComplete(false);
		}		
	}	
	
	/**
	 * @param tableSelected the tableSelected to set
	 */
	public void setTableSelected(String tableSelected) {
		this.tableSelected = tableSelected;
	}

	/**
	 * @return the tableSelected
	 */
	public String getTableSelected() {
		return tableSelected;
	}
	/**
	 * @param columnSelectionPage the columnSelectionPage to set
	 */
	public void setColumnSelectionPage(EditBusinessColumnsWizardPage columnSelectionPage) {
		this.columnSelectionPage = columnSelectionPage;
	}

	/**
	 * @return the columnSelectionPage
	 */
	public EditBusinessColumnsWizardPage getColumnSelectionPage() {
		return columnSelectionPage;
	}

	/**
	 * @param columnSelection the columnSelection to set
	 */
	public void setColumnSelection(boolean columnSelection) {
		this.columnSelection = columnSelection;
	}

	/**
	 * @return the columnSelection
	 */
	public boolean isColumnSelection() {
		return columnSelection;
	}

}
