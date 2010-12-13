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
package it.eng.spagobi.meta.model.business.wizards;

import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.test.TestEditorPlugin;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
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

	private List tableList;
	private BusinessColumnSet owner;
	private String tableSelected;
	/**
	 * @param pageName
	 */
	protected RemovePhysicalTableWizardSelectionPage(String pageName, BusinessColumnSet businessColumnSet) {
		super(pageName);
		setTitle("Remove Physical Table");
		setDescription("Please select the physical table to remove from your Business Table.");
		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("wizards/createBC.png"));
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
		tableGroup.setText("Physical Table Selection");
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
			java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTables();
			for (PhysicalTable physicalTable : physicalTables){
				tableList.add(physicalTable.getName());
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
