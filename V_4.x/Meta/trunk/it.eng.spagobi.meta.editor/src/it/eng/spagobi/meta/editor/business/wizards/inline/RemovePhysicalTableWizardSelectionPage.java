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
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.net.URL;
import java.util.ArrayList;

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
public class RemovePhysicalTableWizardSelectionPage extends WizardPage {

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	private List tableList;
	private BusinessColumnSet owner;
	private String tableSelected;
	/**
	 * @param pageName
	 */
	protected RemovePhysicalTableWizardSelectionPage(String pageName, BusinessColumnSet businessColumnSet) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.removephysicaltable.title"));
		setDescription(RL.getString("business.editor.wizard.removephysicaltable.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
	    if (image!=null) setImageDescriptor(image);	
	    owner = businessColumnSet;
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		createTableGroup(composite, SWT.SHADOW_ETCHED_IN);
		 		
        //Important: Setting page control
 		setControl(composite);
 		
 		populateTableList();
 		addListener();
 		checkPageComplete();
	}
	private void createTableGroup(Composite composite, int style){
		//Physical Table List
		Group tableGroup = new Group(composite, style);
		tableGroup.setText(RL.getString("business.editor.wizard.removephysicaltable.label"));
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
	}
	
	public void addListener(){
		//adding listener to List
 		tableList.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				String[] sel = tableList.getSelection();
				setTableSelected(sel[0]);
				checkPageComplete();
			}
		});
	}
	
	//populate the list with the Physical Tables' names
	private void populateTableList(){
		if (owner instanceof BusinessView){
			//java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTables();
			java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTablesOccurrences();
			java.util.List<String> duplicatePhysicalTables = new ArrayList<String>();
			for (PhysicalTable physicalTable : physicalTables){
				if (duplicatePhysicalTables.contains(physicalTable.getName())){
					int counter=1;
					for (String tableName:duplicatePhysicalTables){
						if (tableName.equals(physicalTable.getName())){
							counter++;
						}
					}
					duplicatePhysicalTables.add(physicalTable.getName());
					tableList.add(physicalTable.getName()+"#"+counter);
				}
				else {
					duplicatePhysicalTables.add(physicalTable.getName());
					tableList.add(physicalTable.getName());
				}
			}
		}
	}
	
	//check if the right conditions to go forward occurred
	public void checkPageComplete(){
		if(tableSelected != null){
			setPageComplete(true);
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
}
