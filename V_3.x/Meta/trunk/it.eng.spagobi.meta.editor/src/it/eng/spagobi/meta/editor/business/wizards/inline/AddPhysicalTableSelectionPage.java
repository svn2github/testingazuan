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

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
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
public class AddPhysicalTableSelectionPage extends WizardPage {
	
	private List tableList;
	private BusinessColumnSet owner;
	private String tableSelected;
	private AddBusinessViewInnerJoinPage pageThreeRef;
	private boolean isBusinessView;
	private String selectedPhysicalTableName;
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	/**
	 * @param pageName
	 */
	protected AddPhysicalTableSelectionPage(String pageName, BusinessColumnSet owner, boolean isBusinessView, String selectedPhysicalTableName) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.addphysicaltable.title"));
		setDescription(RL.getString("business.editor.wizard.physicaltableselection.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
	    if (image!=null) setImageDescriptor(image);	
	    this.owner = owner;
	    this.isBusinessView = isBusinessView;
	    this.selectedPhysicalTableName = selectedPhysicalTableName;
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
 		//check if the physical table to add is predefined
 		if (selectedPhysicalTableName != null){
 			String[] items = tableList.getItems();
 			for (int i=0; i<items.length; i++){
 				if (items[i].equals(selectedPhysicalTableName)){
 					tableList.select(i);
 					break;
 				}
 			}
 			tableSelected = selectedPhysicalTableName;
 		}
 		checkPageComplete();

	}
	
	private void createTableGroup(Composite composite, int style){
		//Physical Table List
		Group tableGroup = new Group(composite, style);
		tableGroup.setText(RL.getString("business.editor.wizard.physicaltableselection.label"));
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
		PhysicalModel physicalModel = owner.getModel().getPhysicalModel();
		
		
		//get already present Physical Table to exclude
		java.util.List<PhysicalTable> excludedPhysicalTables = new ArrayList<PhysicalTable>();
		/*
		if (owner instanceof BusinessTable){
			//get the only Physical Table of Business Table
			excludedPhysicalTables.add(((BusinessTable)owner).getPhysicalTable());
		} else if (owner instanceof BusinessView){
			//get the Physical Tables of the Business View
			excludedPhysicalTables.addAll(((BusinessView)owner).getPhysicalTables());
		}
		*/
		
		int numTables = physicalModel.getTables().size();
		String tabName;
		for (int i = 0; i < numTables; i++){
			if (! excludedPhysicalTables.contains(physicalModel.getTables().get(i)) ){
				tabName = physicalModel.getTables().get(i).getName();
				tableList.add(tabName);		
			}
		}
	}
	//check if the right conditions to go forward occurred
	public void checkPageComplete(){
		if(tableSelected != null){
			setPageComplete(true);
			//disable selection if PhysicalTable Name is predefined
			if (selectedPhysicalTableName != null){
				tableList.setEnabled(false);
			}
			else {
				//do this populate only when table is select from the UI
				PhysicalModel physicalModel = owner.getModel().getPhysicalModel();
				PhysicalTable physicalTable = physicalModel.getTable(tableSelected);
				pageThreeRef.populateDestinationPhysicalTableGroup(physicalTable);
			}
		}
		else{			
			setPageComplete(false);
		}		
	}
	
	public IWizardPage getNextPage() {
		//If is a BusinessView go to the page to select the physical table to join
		if (isBusinessView) { return super.getNextPage(); }
		//If is a BusinessTable go directly to the join relationship selection page
		return getWizard().getPage("Select join relationship");
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
	 * @param pageTwoRef the pageTwoRef to set
	 */
	public void setPageThreeRef(AddBusinessViewInnerJoinPage pageThreeRef) {
		this.pageThreeRef = pageThreeRef;
	}

	/**
	 * @return the pageTwoRef
	 */
	public AddBusinessViewInnerJoinPage getPageThreeRef() {
		return pageThreeRef;
	}	
}
