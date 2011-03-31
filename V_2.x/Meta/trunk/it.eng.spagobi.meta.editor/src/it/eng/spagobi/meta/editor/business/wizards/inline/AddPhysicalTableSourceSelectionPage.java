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

import it.eng.spagobi.meta.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;

/**
 * @author cortella
 *
 */
public class AddPhysicalTableSourceSelectionPage extends WizardPage {

	private BusinessColumnSet owner;
	private Combo comboPhysicalTables;
	private List listPhysicalTableColumns;
	private String selectedPhysicalTable;
	private AddBusinessViewInnerJoinPage pageThreeRef;
	/**
	 * @param pageName
	 */
	protected AddPhysicalTableSourceSelectionPage(String pageName, BusinessColumnSet owner) {
		super(pageName);
		setTitle("Select Source Physical Table to Join");
		setDescription("Please select the physical table to use as source for join relationship");
		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("wizards/createBC.png"));
	    if (image!=null) setImageDescriptor(image);	
	    this.owner = owner;
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		//Important: Setting page control
 		setControl(composite);
 		
		//create GUI only for BusinessView
		if (owner instanceof BusinessView){
	 		Group grpSourcePhysicalTable = new Group(composite, SWT.NONE);
	 		grpSourcePhysicalTable.setText("Source Physical Table Selection");
	 		grpSourcePhysicalTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	 		grpSourcePhysicalTable.setLayout(new FillLayout(SWT.HORIZONTAL));
	 		
	 		Composite compSourcePhysicalTableSelection = new Composite(grpSourcePhysicalTable, SWT.NONE);
	 		compSourcePhysicalTableSelection.setLayout(new GridLayout(1, true));
	 		
	 		comboPhysicalTables = new Combo(compSourcePhysicalTableSelection, SWT.READ_ONLY);
	 		comboPhysicalTables.addSelectionListener(new SelectionAdapter() {
	 			@Override
	 			public void widgetSelected(SelectionEvent e) {
	 				int selectionIndex = comboPhysicalTables.getSelectionIndex();
	 				selectedPhysicalTable = comboPhysicalTables.getItem(selectionIndex);
	 				populateColumnList(selectedPhysicalTable);
	 				checkPageComplete();
	 			}
	 		});
	 		comboPhysicalTables.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	 		
	 		Label lblCurrentlyDerivedColumns = new Label(compSourcePhysicalTableSelection, SWT.NONE);
	 		lblCurrentlyDerivedColumns.setText("Columns:");
	 		
	 		listPhysicalTableColumns = new List(compSourcePhysicalTableSelection, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	 		listPhysicalTableColumns.setEnabled(false);
	 		listPhysicalTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	 		
	 		populatePhysicalTablesCombo();
	 		
	 		checkPageComplete();
	}
		

	}
	
	public void populatePhysicalTablesCombo(){
		comboPhysicalTables.removeAll();
		java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTables();
		for (PhysicalTable physicalTable:physicalTables){
			comboPhysicalTables.add(physicalTable.getName());
		}
	}
	
	public void populateColumnList(String physicalTableName){
		listPhysicalTableColumns.removeAll();
		java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTables();
		PhysicalTable selectedPhysicalTable = null;
		for (PhysicalTable physicalTable : physicalTables){
			if (physicalTable.getName().equals(physicalTableName)){
				selectedPhysicalTable = physicalTable;
				break;
			}
		}

		/*
		EList<BusinessColumn> businessColumns = ((BusinessView)owner).getColumns();
		for (BusinessColumn businessColumn : businessColumns){
			if (businessColumn.getPhysicalColumn().getTable() == selectedPhysicalTable ){
				listPhysicalTableColumns.add(businessColumn.getName());
			}
		}
		//warning the user: there are no columns imported from the selected physical table
		if (listPhysicalTableColumns.getItemCount() == 0){
			this.setErrorMessage("Attention, you haven't imported columns from this physical table yet. Please select another Physical Table or add columns to you Business Table");
		}
		else 
			this.setErrorMessage(null);
		*/
		EList<PhysicalColumn> physicalColumns = selectedPhysicalTable.getColumns();
		for (PhysicalColumn physicalColumn : physicalColumns){
			listPhysicalTableColumns.add(physicalColumn.getName());
		}
	}
	
	//check if the right conditions to go forward occurred
	public void checkPageComplete(){
		if(selectedPhysicalTable != null){
			PhysicalModel physicalModel = owner.getModel().getPhysicalModel();
			PhysicalTable physicalTable = physicalModel.getTable(selectedPhysicalTable);
			pageThreeRef.setOriginalPhysicalTable(physicalTable);
			pageThreeRef.populateSourcePhysicalTableGroup(owner, physicalTable);
			setPageComplete(true);
		}
		else{			
			setPageComplete(false);
		}		
	}
	
	public String getSelectedPhysicalTable(){
		return selectedPhysicalTable;
	}

	/**
	 * @param pageThreeRef the pageThreeRef to set
	 */
	public void setPageThreeRef(AddBusinessViewInnerJoinPage pageThreeRef) {
		this.pageThreeRef = pageThreeRef;
	}

	/**
	 * @return the pageThreeRef
	 */
	public AddBusinessViewInnerJoinPage getPageThreeRef() {
		return pageThreeRef;
	}
}
