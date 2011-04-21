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
import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

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
import org.eclipse.swt.widgets.Label;

/**
 * @author cortella
 *
 */
public class AddBusinessViewInnerJoinPage extends WizardPage {
	private BusinessColumnSet owner;
	private Composite container;
	private List joinRelationshipList, destinationPhysicalColumnsList, sourcePhysicalColumnsList ;
	private PhysicalTable originalPhysicalTable,physicalTable;
	private BusinessViewInnerJoinRelationshipDescriptor relationshipDescriptor;
	private java.util.List<PhysicalColumn> sourceColumns, destinationColumns;
	private String selectedPhysicalTableName;
	private Label lblSourcePhysicalTable, lblDestinationPhysicalTable;
	
	/**
	 * @param pageName
	 */
	protected AddBusinessViewInnerJoinPage(String pageName, BusinessColumnSet owner, String selectedPhysicalTableName) {
		super(pageName);
		setTitle("Select join relationship");
		setDescription("Please select the columns to use in the join relationship.");
		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(SpagoBIMetaModelEditorPlugin.INSTANCE.getImage("wizards/createBC.png"));
	    if (image!=null) setImageDescriptor(image);	
	    this.owner = owner;
	    sourceColumns = new ArrayList<PhysicalColumn>();
	    destinationColumns = new ArrayList<PhysicalColumn>();
	    if (owner instanceof BusinessTable){
	    	originalPhysicalTable = ((BusinessTable)owner).getPhysicalTable();
	    } else if (owner instanceof BusinessView){
	    	//this will set later from the AddPhysicalTableSourceSelectionPage
	    	originalPhysicalTable = null;
	    }
	    this.selectedPhysicalTableName = selectedPhysicalTableName;
	}


	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(1, true));
		
		Composite compositeColumns = new Composite(container, SWT.NONE);
		compositeColumns.setLayout(new GridLayout(2, true));
		compositeColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		createSourcePhysicalTableGroup(compositeColumns, SWT.NONE);
		createDestinationPhysicalTableGroup(compositeColumns, SWT.NONE);
		createButtons(container, SWT.NONE);
		createRelationshipGroup(container, SWT.NONE);
		
		if (selectedPhysicalTableName != null){
			PhysicalModel physicalModel = owner.getModel().getPhysicalModel();
			PhysicalTable physicalTable = physicalModel.getTable(selectedPhysicalTableName);
			populateDestinationPhysicalTableGroup(physicalTable);
		}	
		populateSourcePhysicalTableGroup(owner,null);
		checkPageComplete();
	}
	
	public void createSourcePhysicalTableGroup(Composite composite, int style){
		Group grpBusinessTableColumns = new Group(composite, style);
		grpBusinessTableColumns.setText("Source Physical Table Columns");
		grpBusinessTableColumns.setLayout(new GridLayout(1, false));
		grpBusinessTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		lblSourcePhysicalTable = new Label(grpBusinessTableColumns, SWT.NONE);
		
		Composite compBusinessTableColumns = new Composite(grpBusinessTableColumns, SWT.NONE);
		GridData gd_compBusinessTableColumns = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_compBusinessTableColumns.heightHint = 100;
		compBusinessTableColumns.setLayoutData(gd_compBusinessTableColumns);
		compBusinessTableColumns.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		sourcePhysicalColumnsList = new List(compBusinessTableColumns, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	}
	
	public void createDestinationPhysicalTableGroup(Composite composite, int style){
		Group grpPhysicalTableColumns = new Group(composite, style);
		grpPhysicalTableColumns.setText("Destination Physical Table Columns");
		grpPhysicalTableColumns.setLayout(new GridLayout(1, false));
		grpPhysicalTableColumns.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		lblDestinationPhysicalTable = new Label(grpPhysicalTableColumns, SWT.NONE);
		
		Composite compPhysicalTableColumns = new Composite(grpPhysicalTableColumns, SWT.NONE);
		GridData gd_compPhysicalTableColumns = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_compPhysicalTableColumns.heightHint = 100;
		compPhysicalTableColumns.setLayoutData(gd_compPhysicalTableColumns);
		compPhysicalTableColumns.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		destinationPhysicalColumnsList = new List(compPhysicalTableColumns, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	}
	public void createButtons(Composite composite, int style){
		Composite compButton = new Composite(container, style);
		compButton.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		compButton.setLayout(new GridLayout(2, false));
		
		Button btnAddRelationship = new Button(compButton, SWT.NONE);
		btnAddRelationship.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		btnAddRelationship.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//add relationship
				if ( (destinationPhysicalColumnsList.getSelection().length != 0) && (sourcePhysicalColumnsList.getSelection().length != 0)  ){
					setErrorMessage(null);
					addJoinRelationship(sourcePhysicalColumnsList.getSelection()[0],destinationPhysicalColumnsList.getSelection()[0]);
					checkPageComplete();
				} else {
					setErrorMessage("You must select a source column and a destination column");
				}
			}
		});
		btnAddRelationship.setText("Add Relationship");
		
		Button btnRemoveRelationship = new Button(compButton, SWT.NONE);
		btnRemoveRelationship.setText("Remove Relationship");
		btnRemoveRelationship.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = joinRelationshipList.getSelectionIndex();
				
				//remove relationship from List
				joinRelationshipList.remove(selectionIndex);
								
				//remove relationship from descriptor
				sourceColumns.remove(selectionIndex);
				destinationColumns.remove(selectionIndex);
				relationshipDescriptor.setSourceColumns(sourceColumns);
				relationshipDescriptor.setDestinationColumns(destinationColumns);
			}
		});
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
	
	public void populateSourcePhysicalTableGroup(BusinessColumnSet businessColumnSet, PhysicalTable filterPhysicalTable){
		sourcePhysicalColumnsList.removeAll();
		if (filterPhysicalTable != null){
			//get only the columns of the specified PhysicalTable
			Collection<PhysicalColumn> physicalColumns =  filterPhysicalTable.getColumns();
			for (PhysicalColumn physicalColumn : physicalColumns){
				sourcePhysicalColumnsList.add(physicalColumn.getName());
			}
			lblSourcePhysicalTable.setText(filterPhysicalTable.getName());
			lblSourcePhysicalTable.pack();
		}
		else if (businessColumnSet instanceof BusinessTable){
			BusinessTable businessTable = ((BusinessTable)businessColumnSet);
			PhysicalTable physicalTable = businessTable.getPhysicalTable();
			Collection<PhysicalColumn> physicalColumns =  physicalTable.getColumns();
			for (PhysicalColumn physicalColumn : physicalColumns){
				sourcePhysicalColumnsList.add(physicalColumn.getName());
			}
			lblSourcePhysicalTable.setText(physicalTable.getName());
			lblSourcePhysicalTable.pack();
		}

	}
	
	public void populateDestinationPhysicalTableGroup(PhysicalTable physicalTable){
		destinationPhysicalColumnsList.removeAll();
		this.physicalTable = physicalTable;
		Collection<PhysicalColumn> physicalColumns =  physicalTable.getColumns();
		for(PhysicalColumn physicalColumn:physicalColumns){
			destinationPhysicalColumnsList.add(physicalColumn.getName());
		}
		lblDestinationPhysicalTable.setText(physicalTable.getName());
		lblDestinationPhysicalTable.pack();
	}
	
	public void addJoinRelationship(String sourceColumn, String destinationColumn){
		PhysicalColumn sourcePhysicalColumn = originalPhysicalTable.getColumn(sourceColumn);
		PhysicalColumn destinationPhysicalColumn = physicalTable.getColumn(destinationColumn);
		sourceColumns.add(sourcePhysicalColumn);
		destinationColumns.add(destinationPhysicalColumn);
		
		if (relationshipDescriptor == null){
			//create descriptor
			relationshipDescriptor = new BusinessViewInnerJoinRelationshipDescriptor(getOriginalPhysicalTable(), physicalTable, sourceColumns, destinationColumns,1,owner.getName());
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


	/**
	 * @param originalPhysicalTable the originalPhysicalTable to set
	 */
	public void setOriginalPhysicalTable(PhysicalTable originalPhysicalTable) {
		this.originalPhysicalTable = originalPhysicalTable;
	}


	/**
	 * @return the originalPhysicalTable
	 */
	public PhysicalTable getOriginalPhysicalTable() {
		return originalPhysicalTable;
	}
}
