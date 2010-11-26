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

import java.util.ArrayList;
import java.util.Collection;

import it.eng.spagobi.meta.initializer.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.test.TestEditorPlugin;

import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.List;

/**
 * @author cortella
 *
 */
public class AddBusinessViewInnerJoinPage extends WizardPage {
	private BusinessColumnSet owner;
	private Composite container;
	private List joinRelationshipList, physicalColumnsList, businessColumnsList ;
	private PhysicalTable originalPhysicalTable,physicalTable;
	private BusinessViewInnerJoinRelationshipDescriptor relationshipDescriptor;
	private java.util.List<PhysicalColumn> sourceColumns, destinationColumns;
	
	/**
	 * @param pageName
	 */
	protected AddBusinessViewInnerJoinPage(String pageName, BusinessColumnSet owner) {
		super(pageName);
		setTitle("Select join relationship");
		setDescription("Please select the columns to use in the join relationship.");
		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("wizards/createBC.png"));
	    if (image!=null) setImageDescriptor(image);	
	    this.owner = owner;
	    sourceColumns = new ArrayList<PhysicalColumn>();
	    destinationColumns = new ArrayList<PhysicalColumn>();
	    if (owner instanceof BusinessTable){
		    originalPhysicalTable = ((BusinessTable)owner).getPhysicalTable();
	    } else if (owner instanceof BusinessView){
	    	//TODO: user must select what physical table use to join, in this will be used the first found
	    	originalPhysicalTable = ((BusinessView)owner).getPhysicalTables().get(0);
	    }

	}


	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, true));
		
		Composite compositeColumns = new Composite(container, SWT.NONE);
		compositeColumns.setLayout(new GridLayout(2, true));
		compositeColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createBusinessTableGroup(compositeColumns, SWT.NONE);
		createPhysicalTableGroup(compositeColumns, SWT.NONE);
		createButtons(container, SWT.NONE);
		createRelationshipGroup(container, SWT.NONE);
		
		populateBusinessTableGroup(owner);
		checkPageComplete();
	}
	
	public void createBusinessTableGroup(Composite composite, int style){
		Group grpBusinessTableColumns = new Group(composite, style);
		grpBusinessTableColumns.setText("Business Table Columns");
		grpBusinessTableColumns.setLayout(new GridLayout(1, false));
		grpBusinessTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compBusinessTableColumns = new Composite(grpBusinessTableColumns, SWT.NONE);
		compBusinessTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compBusinessTableColumns.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		businessColumnsList = new List(compBusinessTableColumns, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	}
	
	public void createPhysicalTableGroup(Composite composite, int style){
		Group grpPhysicalTableColumns = new Group(composite, style);
		grpPhysicalTableColumns.setText("Physical Table Columns");
		grpPhysicalTableColumns.setLayout(new GridLayout(1, false));
		grpPhysicalTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compPhysicalTableColumns = new Composite(grpPhysicalTableColumns, SWT.NONE);
		compPhysicalTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compPhysicalTableColumns.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		physicalColumnsList = new List(compPhysicalTableColumns, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	}
	public void createButtons(Composite composite, int style){
		Composite compButton = new Composite(container, style);
		compButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		compButton.setLayout(new GridLayout(1, false));
		
		Button btnAddRelationship = new Button(compButton, SWT.NONE);
		btnAddRelationship.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnAddRelationship.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//add relationship
				if ( (physicalColumnsList.getSelection().length != 0) && (businessColumnsList.getSelection().length != 0)  ){
					setErrorMessage(null);
					addJoinRelationship(businessColumnsList.getSelection()[0],physicalColumnsList.getSelection()[0]);
					checkPageComplete();
				} else {
					setErrorMessage("You must select a source column and a destination column");
				}
			}
		});
		btnAddRelationship.setText("Add Relationship");
	}
	
	public void createRelationshipGroup(Composite composite, int style){	
		Composite compositeRelationship = new Composite(composite, style);
		compositeRelationship.setLayout(new GridLayout(1, false));
		compositeRelationship.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Group grpJoinRelationship = new Group(compositeRelationship, SWT.NONE);
		grpJoinRelationship.setText("Join Relationship");
		grpJoinRelationship.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpJoinRelationship.setLayout(new GridLayout(1, false));
		
		joinRelationshipList = new List(grpJoinRelationship, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		joinRelationshipList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	
	public void populateBusinessTableGroup(BusinessColumnSet businessColumnSet){
		Collection<BusinessColumn> businessColumns =  businessColumnSet.getColumns();
		for(BusinessColumn businessColumn:businessColumns){
			businessColumnsList.add(businessColumn.getName());
		}
	}
	
	public void populatePhysicalTableGroup(PhysicalTable physicalTable){
		physicalColumnsList.removeAll();
		this.physicalTable = physicalTable;
		Collection<PhysicalColumn> physicalColumns =  physicalTable.getColumns();
		for(PhysicalColumn physicalColumn:physicalColumns){
			physicalColumnsList.add(physicalColumn.getName());
		}		
	}
	
	public void addJoinRelationship(String sourceColumn, String destinationColumn){
		//Get BusinessColumn first, than corresponding PhysicalColumn
		BusinessColumn businessColumn = owner.getColumn(sourceColumn);
		PhysicalColumn sourcePhysicalColumn = businessColumn.getPhysicalColumn();
		
		PhysicalColumn destinationPhysicalColumn = physicalTable.getColumn(destinationColumn);
		sourceColumns.add(sourcePhysicalColumn);
		destinationColumns.add(destinationPhysicalColumn);
		
		if (relationshipDescriptor == null){
			//create descriptor
			relationshipDescriptor = new BusinessViewInnerJoinRelationshipDescriptor(originalPhysicalTable, physicalTable, sourceColumns, destinationColumns,1,owner.getName());
		} else {
			//update descriptor
			relationshipDescriptor.setSourceColumns(sourceColumns);
			relationshipDescriptor.setDestinationColumns(destinationColumns);
		}
		
		joinRelationshipList.add(sourceColumn+" -> "+destinationColumn );
		
	}
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if(joinRelationshipList.getItemCount() > 0){	
			setPageComplete(true);
		}
		else{			
			setPageComplete(false);
		}		
	}


	/**
	 * @param relationshipDescriptor the relationshipDescriptor to set
	 */
	public void setRelationshipDescriptor(BusinessViewInnerJoinRelationshipDescriptor relationshipDescriptor) {
		this.relationshipDescriptor = relationshipDescriptor;
	}


	/**
	 * @return the relationshipDescriptor
	 */
	public BusinessViewInnerJoinRelationshipDescriptor getRelationshipDescriptor() {
		return relationshipDescriptor;
	}
}
