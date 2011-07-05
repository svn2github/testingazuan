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

import java.net.URL;
import java.util.ArrayList;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.initializer.descriptor.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class EditBusinessViewInnerJoinPage extends WizardPage {

	BusinessColumnSet businessColumnSet;
	Combo comboSource,comboDestination;
	List sourceColumnsList, destinationColumnsList,joinPathsList;
	java.util.List<PhysicalColumn> sourceColumns,destinationColumns;
	java.util.List<PhysicalTable> sourceTables,destinationTables;
	java.util.List<BusinessViewInnerJoinRelationshipDescriptor> joinDescriptors;
	BusinessView businessView;
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	
	/**
	 * Create the wizard.
	 */
	public EditBusinessViewInnerJoinPage(String pageName, BusinessColumnSet businessColumnSet) {
		super(pageName);
		ImageDescriptor image = ImageDescriptor.createFromURL( (URL)RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createBR") );
	    if (image!=null) setImageDescriptor(image);	
		setTitle(RL.getString("business.editor.wizard.editjoinpaths.title"));
		setDescription(RL.getString("business.editor.wizard.editjoinpaths.description"));
		this.businessColumnSet = businessColumnSet;
		this.sourceColumns = new ArrayList<PhysicalColumn>();
		this.destinationColumns = new ArrayList<PhysicalColumn>();
		this.sourceTables = new ArrayList<PhysicalTable>();
		this.destinationTables = new ArrayList<PhysicalTable>();
		this.joinDescriptors = new ArrayList<BusinessViewInnerJoinRelationshipDescriptor>();
		this.businessView = (BusinessView)businessColumnSet;
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group group = new Group(container, SWT.NONE);
		group.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite mainComposite = new Composite(group, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		
		//create Table Combos
		createTableCombos(mainComposite, SWT.NONE);
		
		//create Table Columns Panel
		createTableColumnsPanel(mainComposite, SWT.NONE);
		
		//create Buttons Panel
		createButtonsPanel(mainComposite, SWT.NONE);
		
		//create Join Paths Panel Group
		createJoinPathsPanel(mainComposite, SWT.NONE);
		
		//UI population
		populateCombos();
		
		//Check if there are previous defined Join Relationships
		java.util.List<BusinessViewInnerJoinRelationship> joinRelationships = businessView.getJoinRelationships();
		//Populate with previous defined join relationships
		if (!joinRelationships.isEmpty()){
			//This will "unpack" Join Relationships to display single pair of join relationships in the UI
			for (BusinessViewInnerJoinRelationship joinRelationship : joinRelationships){
				//create BusinessViewInnerJoinRelationshipDescriptor from BusinessViewInnerJoinRelationship
				java.util.List<PhysicalColumn> newSourceColumns = new ArrayList<PhysicalColumn>();
				newSourceColumns.addAll(joinRelationship.getSourceColumns());
				java.util.List<PhysicalColumn> newDestinationColumns = new ArrayList<PhysicalColumn>();
				newDestinationColumns.addAll(joinRelationship.getDestinationColumns()); 
				
				BusinessViewInnerJoinRelationshipDescriptor relationshipDescriptor = new BusinessViewInnerJoinRelationshipDescriptor(joinRelationship.getSourceTable(), joinRelationship.getDestinationTable(), newSourceColumns,newDestinationColumns,1,businessColumnSet.getName());
				joinDescriptors.add(relationshipDescriptor);
				int columnsNumber = joinRelationship.getSourceColumns().size();
				for (int i=0; i < columnsNumber; i++){
					//add Description on UI List
					joinPathsList.add(joinRelationship.getSourceTable().getName() + "." + joinRelationship.getSourceColumns().get(i).getName() + " -> " + joinRelationship.getDestinationTable().getName() + "." + joinRelationship.getDestinationColumns().get(i).getName());
					//add Tables and Columns to List used by the descriptors
					sourceTables.add(joinRelationship.getSourceTable());
					destinationTables.add(joinRelationship.getDestinationTable());
					sourceColumns.add(joinRelationship.getSourceColumns().get(i));
					destinationColumns.add(joinRelationship.getDestinationColumns().get(i));
				}
			}

		}
		
		setPageComplete(true);

	}
	
	public void createTableCombos(Composite container, int style){
		Composite compositeTables = new Composite(container, style);
		compositeTables.setLayout(new GridLayout(4, false));
		compositeTables.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSourceTable = new Label(compositeTables, SWT.NONE);
		lblSourceTable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSourceTable.setText(RL.getString("business.editor.wizard.editjoinpaths.sourcetable.label"));
		
		comboSource = new Combo(compositeTables, SWT.READ_ONLY);
		comboSource.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		comboSource.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (comboSource.getItemCount() > 0) {	
 					populateSourceColumnsList( getSourceTable() );
 				}
 			}
 		});
		
		Label lblDestinationTable = new Label(compositeTables, SWT.NONE);
		lblDestinationTable.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblDestinationTable.setText(RL.getString("business.editor.wizard.editjoinpaths.destinationtable.label"));
		
		comboDestination = new Combo(compositeTables, SWT.READ_ONLY);
		comboDestination.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));	
		comboDestination.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (comboDestination.getItemCount() > 0) {	
 					populateDestinationColumnsList( getDestinationTable() );
 				}
 			}
 		});
	}
	
	public void createTableColumnsPanel(Composite container, int style){
		Composite compositeColumns = new Composite(container, style);
		compositeColumns.setLayout(new GridLayout(2, false));
		compositeColumns.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblSourceColumns = new Label(compositeColumns, SWT.NONE);
		lblSourceColumns.setText(RL.getString("business.editor.wizard.editjoinpaths.sourcecolumns.label"));
		
		Label lblDestinationColumns = new Label(compositeColumns, SWT.NONE);
		lblDestinationColumns.setText(RL.getString("business.editor.wizard.editjoinpaths.destinationcolumns.label"));
		
		sourceColumnsList = new List(compositeColumns, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_sourceColumnsList = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_sourceColumnsList.heightHint = 100;
		sourceColumnsList.setLayoutData(gd_sourceColumnsList);
		
		destinationColumnsList = new List(compositeColumns, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_destinationColumnsList = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		gd_destinationColumnsList.heightHint = 100;
		destinationColumnsList.setLayoutData(gd_destinationColumnsList);
	}
	
	public void createButtonsPanel(Composite container, int style){
		Composite compositeButtons = new Composite(container, style);
		compositeButtons.setLayout(new GridLayout(2, false));
		compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		Button btnAddJoinRelationship = new Button(compositeButtons, SWT.NONE);
		btnAddJoinRelationship.setText(RL.getString("business.editor.wizard.editjoinpaths.addbutton"));
		btnAddJoinRelationship.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ( (comboSource.getItemCount()>0) && (comboDestination.getItemCount()>0) ){
					if ( (sourceColumnsList.getSelection().length != 0) && (destinationColumnsList.getSelection().length != 0) ){
						joinPathsList.add(comboSource.getText() + "." + sourceColumnsList.getSelection()[0] + " -> " + comboDestination.getText() + "." + destinationColumnsList.getSelection()[0]);
						sourceTables.add(getSourceTable());
						destinationTables.add(getDestinationTable());
						sourceColumns.add(getSourceColumn());
						destinationColumns.add(getDestinationColumn());
							
						//create or update join descriptors 
						if (joinDescriptors.isEmpty()) {	
							//create new descriptor
							java.util.List<PhysicalColumn> newSourceColumns = new ArrayList<PhysicalColumn>();
							newSourceColumns.add(getSourceColumn());
							java.util.List<PhysicalColumn> newDestinationColumns = new ArrayList<PhysicalColumn>();
							newDestinationColumns.add(getDestinationColumn());
							BusinessViewInnerJoinRelationshipDescriptor relationshipDescriptor = new BusinessViewInnerJoinRelationshipDescriptor(getSourceTable(), getDestinationTable(), newSourceColumns, newDestinationColumns, 1 ,businessColumnSet.getName());
							joinDescriptors.add(relationshipDescriptor);
						} else {
							boolean found = false;
							//check if already exist a descriptor with same pair of source and destination table
							for (BusinessViewInnerJoinRelationshipDescriptor joinDescriptor : joinDescriptors){
								if ( (joinDescriptor.getSourceTable() == getSourceTable()) && (joinDescriptor.getDestinationTable() == getDestinationTable()) ){
									//update descriptor
									joinDescriptor.getSourceColumns().add(getSourceColumn());
									joinDescriptor.getDestinationColumns().add(getDestinationColumn());
									
									found = true;
									break;
								}
							}
							if (!found){
								//create descriptor
								java.util.List<PhysicalColumn> newSourceColumns = new ArrayList<PhysicalColumn>();
								newSourceColumns.add(getSourceColumn());
								java.util.List<PhysicalColumn> newDestinationColumns = new ArrayList<PhysicalColumn>();
								newDestinationColumns.add(getDestinationColumn());
								BusinessViewInnerJoinRelationshipDescriptor relationshipDescriptor = new BusinessViewInnerJoinRelationshipDescriptor(getSourceTable(), getDestinationTable(), newSourceColumns, newDestinationColumns, 1 ,businessColumnSet.getName());
								joinDescriptors.add(relationshipDescriptor);
							}
						}
							
					}
				}				
			}
		});
		
		Button btnRemoveJoinRelationship = new Button(compositeButtons, SWT.NONE);
		btnRemoveJoinRelationship.setText(RL.getString("business.editor.wizard.editjoinpaths.removebutton"));
		btnRemoveJoinRelationship.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selectionIndex = joinPathsList.getSelectionIndex();
				if (selectionIndex != -1){
					//remove source and destination columns using same index
					PhysicalColumn removedSourceColumn = sourceColumns.remove(selectionIndex);
					PhysicalColumn removedDestinationColumn = destinationColumns.remove(selectionIndex);
					PhysicalTable removedSourceTable = sourceTables.remove(selectionIndex);
					PhysicalTable removedDestinationTable = destinationTables.remove(selectionIndex);
					joinPathsList.remove(selectionIndex);
					
					boolean foundDescriptor = false;
					BusinessViewInnerJoinRelationshipDescriptor descriptorToCheck = null;
					
					for (BusinessViewInnerJoinRelationshipDescriptor joinDescriptor : joinDescriptors){
						if ( (joinDescriptor.getSourceTable() == removedSourceTable) && (joinDescriptor.getDestinationTable() == removedDestinationTable) ){
							//update descriptor
							joinDescriptor.getSourceColumns().remove(removedSourceColumn);
							joinDescriptor.getDestinationColumns().remove(removedDestinationColumn);
							
							foundDescriptor = true;
							descriptorToCheck = joinDescriptor;
							break;
						}
					}
					if (foundDescriptor){
						foundDescriptor = false;
						//delete descriptor if is empty
						if ( (descriptorToCheck.getSourceColumns().isEmpty()) || (descriptorToCheck.getDestinationColumns().isEmpty())){
							joinDescriptors.remove(descriptorToCheck);							
						}
					}

				}
			}
		});
	}
	
	public void createJoinPathsPanel(Composite container, int style){
		Group grpJoinPaths = new Group(container, style);
		grpJoinPaths.setText(RL.getString("business.editor.wizard.editjoinpaths.joinpaths.label"));
		grpJoinPaths.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_grpJoinPaths = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
		gd_grpJoinPaths.heightHint = 100;
		grpJoinPaths.setLayoutData(gd_grpJoinPaths);
		
		joinPathsList = new List(grpJoinPaths, SWT.BORDER | SWT.V_SCROLL);
	}
	
	public void populateCombos(){
		if (businessColumnSet instanceof BusinessView){
			BusinessView businessView = (BusinessView)businessColumnSet;
			java.util.List<PhysicalTable> physicalTables = businessView.getPhysicalTables();
			comboSource.removeAll();
			comboDestination.removeAll();
			for(PhysicalTable physicalTable : physicalTables){
				comboSource.add(physicalTable.getName());
				comboDestination.add(physicalTable.getName());
			}
		}
	}
	
	public void populateSourceColumnsList(PhysicalTable physicalTable){
		sourceColumnsList.removeAll();
		java.util.List<PhysicalColumn> physicalColumns = physicalTable.getColumns();
		for(PhysicalColumn physicalColumn : physicalColumns){
			sourceColumnsList.add(physicalColumn.getName());
		}
	}
	
	public void populateDestinationColumnsList(PhysicalTable physicalTable){
		destinationColumnsList.removeAll();
		java.util.List<PhysicalColumn> physicalColumns = physicalTable.getColumns();
		for(PhysicalColumn physicalColumn : physicalColumns){
			destinationColumnsList.add(physicalColumn.getName());
		}
	}
	
	public PhysicalTable getSourceTable(){
		PhysicalModel physicalModel = businessColumnSet.getModel().getPhysicalModel();
		String physicalTableName = comboSource.getText();
		PhysicalTable physicalTable = physicalModel.getTable(physicalTableName);
		return physicalTable;
	}
	
	public PhysicalTable getDestinationTable(){
		PhysicalModel physicalModel = businessColumnSet.getModel().getPhysicalModel();
		String physicalTableName = comboDestination.getText();
		PhysicalTable physicalTable = physicalModel.getTable(physicalTableName);
		return physicalTable;
	}
	
	public PhysicalColumn getSourceColumn(){
		return getSourceTable().getColumn(sourceColumnsList.getSelection()[0]);
	}
	
	public PhysicalColumn getDestinationColumn(){
		return getDestinationTable().getColumn(destinationColumnsList.getSelection()[0]);
	}
	
	/**
	 * @return the join relationships Descriptors
	 */
	public java.util.List<BusinessViewInnerJoinRelationshipDescriptor> getJoinRelationshipsDescriptors() {
		return joinDescriptors;
	}
	
}
