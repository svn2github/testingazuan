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
package it.eng.spagobi.meta.editor.olap.editor.hierarchies;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.HierarchyDescriptor;
import it.eng.spagobi.meta.initializer.descriptor.HierarchyLevelDescriptor;
import it.eng.spagobi.meta.initializer.properties.OlapModelPropertiesFromFileInitializer;
import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.Hierarchy;
import it.eng.spagobi.meta.model.olap.Level;
import it.eng.spagobi.meta.model.olap.OlapModel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class HierarchiesEditorMainPage extends Dialog {
	private Table tableHierarchies;
	
	OlapModel olapModel;
	BusinessColumnSet businessColumnSet;
	OlapModelInitializer olapModelInitializer;
	List<HierarchyDescriptor> hierarchiesDescriptors;
	
	public static final int COLUMN_NAME = 0;
	public static final int COLUMN_EDIT = 1;
	public static final int COLUMN_REMOVE = 2;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public HierarchiesEditorMainPage(Shell parentShell, OlapModel olapModel, BusinessColumnSet businessColumnSet) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.PRIMARY_MODAL);
		this.olapModel = olapModel;
		this.businessColumnSet = businessColumnSet;
		olapModelInitializer = new OlapModelInitializer();
		hierarchiesDescriptors = new ArrayList<HierarchyDescriptor>();
		
	}
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Hierarchies Editor");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblCreateOrEdit = new Label(composite, SWT.NONE);
		lblCreateOrEdit.setText("Create or Edit Hierarchies for this Dimension");
		
		Group grpHierarchies = new Group(composite, SWT.NONE);
		grpHierarchies.setText("Hierarchies");
		grpHierarchies.setLayout(new GridLayout(1, false));
		grpHierarchies.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeButtons = new Composite(grpHierarchies, SWT.NONE);
		compositeButtons.setLayout(new GridLayout(1, false));
		compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Button btnAddHierarchy = new Button(compositeButtons, SWT.NONE);
		btnAddHierarchy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HierarchyEditor hierarchyEditor = new HierarchyEditor(getShell(), olapModel, businessColumnSet, olapModelInitializer );
				hierarchyEditor.create();
				if (hierarchyEditor.open() ==  Window.OK){
					HierarchyDescriptor hyerarchyDescriptor = hierarchyEditor.getHyerarchyDescriptor();
					//Add Hierarchy Descriptor to internal model
					hierarchiesDescriptors.add(hyerarchyDescriptor);
					//create new item in Hierarchies Table
					createTableItem(hyerarchyDescriptor);
				}
			}
		});
		btnAddHierarchy.setText("Add Hierarchy");
		
		Composite compositeTableHierarchies = new Composite(grpHierarchies, SWT.NONE);
		compositeTableHierarchies.setLayout(new GridLayout(1, false));
		compositeTableHierarchies.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeTableHierarchies.setBounds(0, 0, 64, 64);
		
		Group grpCurrentHierarchies = new Group(compositeTableHierarchies, SWT.NONE);
		grpCurrentHierarchies.setText("Current Hierarchies");
		grpCurrentHierarchies.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpCurrentHierarchies.setLayout(new GridLayout(1, false));
		
		tableHierarchies = new Table(grpCurrentHierarchies, SWT.BORDER | SWT.FULL_SELECTION);
		tableHierarchies.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		tableHierarchies.setHeaderVisible(true);
		tableHierarchies.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(tableHierarchies, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		/*
		TableColumn tblclmnPrimaryKey = new TableColumn(tableHierarchies, SWT.NONE);
		tblclmnPrimaryKey.setWidth(100);
		tblclmnPrimaryKey.setText("Primary Key");
		*/
		
		TableColumn tblclmnEdit = new TableColumn(tableHierarchies, SWT.NONE);
		tblclmnEdit.setWidth(100);
		tblclmnEdit.setText("Edit");
		
		TableColumn tblclmnRemove = new TableColumn(tableHierarchies, SWT.NONE);
		tblclmnRemove.setWidth(100);
		tblclmnRemove.setText("Remove");
		
		//Populate tableHierarchies with existing Hierarchy found on Dimension
		populateTableHierarchies();

		return container;
	}
	
	//Populate tableHierarchies with existing Hierarchy found on Dimension
	private void populateTableHierarchies(){
		//This will create HyerarchyDescriptor objects from Hierarchy objects and
		//HierarchyLevelDescriptor object from Level objects
		Dimension dimension = olapModelInitializer.getDimension(businessColumnSet);
		List<Hierarchy> hierarchies = dimension.getHierarchies();
		if (!hierarchies.isEmpty()){
			for (Hierarchy hierarchy : hierarchies ){
				//Create HyerarchyDescriptor
				HierarchyDescriptor hyerarchyDescriptor = new HierarchyDescriptor();
				
				hyerarchyDescriptor.setName(hierarchy.getName());
	
				hyerarchyDescriptor.setHasAll(Boolean.valueOf(hierarchy.getProperties().get(OlapModelPropertiesFromFileInitializer.HIERARCHY_HAS_ALL).getValue()));
				
				hyerarchyDescriptor.setAllMemberName(hierarchy.getProperties().get(OlapModelPropertiesFromFileInitializer.HIERARCHY_ALL_MEMBER_NAME).getValue());
				List<Level> hierarchyLevels = hierarchy.getLevels();
				if (!hierarchyLevels.isEmpty()){
					for (Level level : hierarchyLevels){
						//Create HierarchyLevelDescriptor
						HierarchyLevelDescriptor hierarchyLevelDescriptor = new HierarchyLevelDescriptor();
						
						hierarchyLevelDescriptor.setBusinessColumn(level.getColumn());
						hierarchyLevelDescriptor.setName(level.getName());
						if (level.getNameColumn() != null){
							hierarchyLevelDescriptor.setNameColumn(level.getNameColumn());
						}
						if (level.getCaptionColumn() != null){
							hierarchyLevelDescriptor.setCaptionColumn(level.getCaptionColumn());
						}
						if (level.getOrdinalColumn() != null){
							hierarchyLevelDescriptor.setOrdinalColumn(level.getOrdinalColumn());
						}
						hierarchyLevelDescriptor.setUniqueMembers(Boolean.valueOf(level.getProperties().get(OlapModelPropertiesFromFileInitializer.LEVEL_UNIQUE_MEMBERS).getValue()));
						//Add HierarchyLevelDescriptor to HyerarchyDescriptor
						hyerarchyDescriptor.getLevels().add(hierarchyLevelDescriptor);
					}

				}
				
				
				//Add Hierarchy Descriptor to internal model
				hierarchiesDescriptors.add(hyerarchyDescriptor);
				//Create corresponding Table Item
				createTableItem(hyerarchyDescriptor);
			}

		}		
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(608, 497);
	}
	
	//create new item in Hierarchies Table
	private void createTableItem(HierarchyDescriptor hyerarchyDescriptor){
		final TableItem item = new TableItem(tableHierarchies, SWT.NONE);
		item.setData(hyerarchyDescriptor);
		item.setText(COLUMN_NAME, hyerarchyDescriptor.getName());
		
		//*******************	
		//create Cell Editor Button Edit
		TableEditor editor_button_edit = new TableEditor(tableHierarchies);
		final Button buttonEdit = new Button(tableHierarchies, SWT.NONE);
		buttonEdit.setText("Edit");
		editor_button_edit.grabHorizontal = true;
		buttonEdit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//find item in summaryPanelPageTableRows
				int index = findItemInHierarchiesDescriptors(hierarchiesDescriptors,item);
				if (index >= 0){
					
					HierarchyDescriptor hierarchyDescriptor = hierarchiesDescriptors.get(index);
					//Open HierarchyEditor to existing Edit Hierarchy
					HierarchyEditor hierarchyEditor = new HierarchyEditor(getShell(), olapModel, businessColumnSet, olapModelInitializer,hierarchyDescriptor );
					hierarchyEditor.create();
					if (hierarchyEditor.open() ==  Window.OK){
						HierarchyDescriptor hyerarchyDescriptor = hierarchyEditor.getHyerarchyDescriptor();
						//ModifyHierarchy Descriptor into internal model
						HierarchyDescriptor removedHierarchyDescriptor = hierarchiesDescriptors.remove(index);
						hyerarchyDescriptor.setUi_buttonEdit(removedHierarchyDescriptor.getUi_buttonEdit());
						hyerarchyDescriptor.setUi_buttonRemove(removedHierarchyDescriptor.getUi_buttonRemove());
						hierarchiesDescriptors.add(index, hyerarchyDescriptor);
						//modify item in Hierarchies Table
						modifyTableItem(index,hyerarchyDescriptor);
					}
					

				}
				tableHierarchies.redraw();

			}
		});
		editor_button_edit.setEditor(buttonEdit,item, COLUMN_EDIT);
		//*******************
		
		//*******************	
		//create Cell Editor Button Remove
		TableEditor editor_button_remove = new TableEditor(tableHierarchies);
		final Button buttonRemove = new Button(tableHierarchies, SWT.NONE);
		buttonRemove.setText("Remove");
		editor_button_remove.grabHorizontal = true;
		buttonRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//find item in summaryPanelPageTableRows
				int index = findItemInHierarchiesDescriptors(hierarchiesDescriptors,item);
				if (index >= 0){
					
					//remove hierarchy item from UI
					hierarchiesDescriptors.get(index).disposeUiElements();
					//remove hierarchy from internal model
					hierarchiesDescriptors.remove(index);

				}
				tableHierarchies.remove(index);

				tableHierarchies.redraw();

			}
		});
		editor_button_remove.setEditor(buttonRemove,item, COLUMN_REMOVE);
		//*******************
		
		//Save referencens to UI element of this Item inside internal object 
		hyerarchyDescriptor.setUi_tableItem(item);
		hyerarchyDescriptor.setUi_buttonEdit(buttonEdit);
		hyerarchyDescriptor.setUi_buttonRemove(buttonRemove);
		
	}
	
	private void modifyTableItem(int index,HierarchyDescriptor hyerarchyDescriptor){
		final TableItem item = tableHierarchies.getItem(index);
		item.setData(hyerarchyDescriptor);
		item.setText(COLUMN_NAME, hyerarchyDescriptor.getName());
		hyerarchyDescriptor.setUi_tableItem(item);
		
	}

	//search a TableItem inside the collection of Hierarchies and if found return the index position of the element
	private int findItemInHierarchiesDescriptors(List<HierarchyDescriptor> hierarchiesDescriptors, TableItem item){
		int index = -1;

		if (hierarchiesDescriptors != null){
			index = 0;
			boolean found = false;
			for(HierarchyDescriptor element:hierarchiesDescriptors){
				if (element.getUi_tableItem().equals(item)){
					return index;
				} else {
					index++;
				}
			}
			if (!found){
				index= -1;
			}
		}
		return index;

	}	
	
	/**
	 * @return the hierarchiesDescriptors
	 */
	public List<HierarchyDescriptor> getHierarchiesDescriptors() {
		return hierarchiesDescriptors;
	}
	/**
	 * @param hierarchiesDescriptors the hierarchiesDescriptors to set
	 */
	public void setHierarchiesDescriptors(
			List<HierarchyDescriptor> hierarchiesDescriptors) {
		this.hierarchiesDescriptors = hierarchiesDescriptors;
	}
	@Override
	protected void okPressed() {
		//first check the inputs
		if (isValidInput()) {
			super.okPressed();		
		} else {
			MessageDialog.openWarning(new Shell(), "Warning", "Please insert all the required values"); 
		}			
	}
	
	//check if all the required input are inserted
	private boolean isValidInput() {
		boolean valid = true;

		
		return valid;
			
	}
	
}
