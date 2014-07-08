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
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
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
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	
	/**
	 * @param pageName
	 */
	protected AddPhysicalTableSourceSelectionPage(String pageName, BusinessColumnSet owner) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.addphysicaltable.sourcetableselection.title"));
		setDescription(RL.getString("business.editor.wizard.addphysicaltable.sourcetableselection.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBC") );
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
	 		grpSourcePhysicalTable.setText(RL.getString("business.editor.wizard.addphysicaltable.sourcetableselection.label"));
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
	 				if (selectedPhysicalTable.contains("#")){
	 					selectedPhysicalTable = selectedPhysicalTable.substring(0,selectedPhysicalTable.indexOf("#"));
	 				}
	 				populateColumnList(selectedPhysicalTable);
	 				checkPageComplete();
	 			}
	 		});
	 		comboPhysicalTables.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	 		
	 		Label lblCurrentlyDerivedColumns = new Label(compSourcePhysicalTableSelection, SWT.NONE);
	 		lblCurrentlyDerivedColumns.setText(RL.getString("business.editor.wizard.addphysicaltable.sourcetableselection.columns.label"));
	 		
	 		listPhysicalTableColumns = new List(compSourcePhysicalTableSelection, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	 		listPhysicalTableColumns.setEnabled(false);
	 		listPhysicalTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	 		
	 		populatePhysicalTablesCombo();
	 		
	 		checkPageComplete();
	}
		

	}
	
	public void populatePhysicalTablesCombo(){
		comboPhysicalTables.removeAll();
		//java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTables();
		java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTablesOccurrences();
		java.util.List<String> duplicatePhysicalTables = new ArrayList<String>();
		for (PhysicalTable physicalTable:physicalTables){
			if (duplicatePhysicalTables.contains(physicalTable.getName())){
				int counter=1;
				for (String tableName:duplicatePhysicalTables){
					if (tableName.equals(physicalTable.getName())){
						counter++;
					}
				}
				duplicatePhysicalTables.add(physicalTable.getName());
				comboPhysicalTables.add(physicalTable.getName()+"#"+counter);
			}
			else {
				duplicatePhysicalTables.add(physicalTable.getName());
				comboPhysicalTables.add(physicalTable.getName());
			}
			
		}
	}
	
	public void populateColumnList(String physicalTableName){
		listPhysicalTableColumns.removeAll();
		//java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTables();
		java.util.List<PhysicalTable> physicalTables = ((BusinessView)owner).getPhysicalTablesOccurrences();
		PhysicalTable selectedPhysicalTable = null;
		for (PhysicalTable physicalTable : physicalTables){
			if (physicalTable.getName().equals(physicalTableName)){
				selectedPhysicalTable = physicalTable;
				break;
			}
		}

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
