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
package it.eng.spagobi.meta.editor.dnd;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import it.eng.spagobi.meta.editor.business.actions.AddBusinessTableAction;
import it.eng.spagobi.meta.editor.business.wizards.inline.AddBusinessTableWizard;
import it.eng.spagobi.meta.editor.business.wizards.inline.AddPhysicalTableWizard;
import it.eng.spagobi.meta.editor.business.wizards.inline.EditBusinessViewInnerJoinWizard;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.model.SortBusinessModelTablesCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.AddColumnsToBusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.table.CreateBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.edit.view.AddPhysicalTableToBusinessViewCommand;
import it.eng.spagobi.meta.model.business.commands.edit.view.EditBusinessViewInnerJoinRelationshipsCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelDropFromPhysicalModelHandler {
	
	private EObject model;
	private Object nextTo;
	private AdapterFactoryEditingDomain editingDomain;
	//BusinessModelInitializer initializer;
	
	public BusinessModelDropFromPhysicalModelHandler(EObject model, AdapterFactoryEditingDomain editingDomain) {
		this.model = model;
		this.nextTo = null;
		this.editingDomain = editingDomain;
		//initializer = new BusinessModelInitializer();
	}
	
	public boolean performDrop(Object data, Object target, boolean isDropLocationBeforeOrAfterTarget) {
		if ( dataDroppedOnModel(target, isDropLocationBeforeOrAfterTarget) ) {
			return performDropOnModel(data, target);
		} else if ( dataDroppedOnTable(target, isDropLocationBeforeOrAfterTarget) ){
			return performDropOnBusinessColumnSet(data, target);
		} else {
			//this.getCurrentEvent().detail = DND.DROP_NONE;
			return false;
		}
	}
	
	private boolean dataDroppedOnModel(Object target, boolean isDropLocationBeforeOrAfterTarget) {
		boolean dataDroppedOnModelRootNode = target instanceof BusinessRootItemProvider;
		boolean dataDroppedBetweenTables = (target instanceof BusinessColumnSet) && isDropLocationBeforeOrAfterTarget;
		return dataDroppedOnModelRootNode || dataDroppedBetweenTables;	
	}
	
	private boolean dataDroppedOnTable(Object target, boolean isDropLocationBeforeOrAfterTarget) {
		boolean dataDroppedOnTable = target instanceof BusinessColumnSet;
		boolean dataDroppedOnColumn = (target instanceof BusinessColumn) && isDropLocationBeforeOrAfterTarget;
		return dataDroppedOnTable || dataDroppedOnColumn;
	}
	
	private List<EObject> getDroppedObjects(Object data) {
		List<EObject> droppedObjects = new ArrayList<EObject>();
		StringTokenizer stringTokenizer = new StringTokenizer(data.toString(), "$$");
		while (stringTokenizer.hasMoreTokens()){
			String tableName = stringTokenizer.nextToken();

			//getting physical table
			URI uri = URI.createURI(tableName);
			EObject eObject = model.eResource().getResourceSet().getEObject(uri, false);
			droppedObjects.add(eObject);
		}
		return droppedObjects;
	}
	
	private boolean performDropOnModel(Object data, Object target) {
		List<EObject> droppedObjects = getDroppedObjects(data);
		
		for (EObject eObject : droppedObjects){
			if (eObject instanceof PhysicalTable){
				executeCreateBusinessTableAction( (PhysicalTable)eObject );
			} else {
				return false;
			}	
		}
		return true;		
	}
	
	private boolean performDropOnBusinessColumnSet(Object data, Object target) {
		BusinessColumnSet businessColumnSet = null;
		
		if (target instanceof BusinessColumn){
			businessColumnSet = ((BusinessColumn)target).getTable();
		} else {
			businessColumnSet = (BusinessColumnSet)target;
		}


		List<EObject> droppedObjects = getDroppedObjects(data);

		for (EObject eObject : droppedObjects) 
		{
			if (eObject instanceof PhysicalColumn){
				executeAddPhysicalColumnToBusinessColumnSetAction( businessColumnSet, (PhysicalColumn)eObject );	
			}
			else if (eObject instanceof PhysicalTable){
				executeAddPhysicalTableToBusinessColumnSetAction( businessColumnSet, (PhysicalTable)eObject );
			}
		}
		return true;
	}

	// =======================================================================
	// ACTIONS
	// =======================================================================

	public boolean executeCreateBusinessTableAction(PhysicalTable physicalTable) {
		
		if(physicalTable == null) return false;
		
		//Get Active Window
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();

		//Create addBusinessTable command
		Command addBusinessTableCommand = editingDomain.createCommand
		(CreateBusinessTableCommand.class, 
				new CommandParameter(model, null, null, new ArrayList<Object>()));
		
		//Launch AddBusinessTableWizard
		AddBusinessTableWizard wizard = new AddBusinessTableWizard((BusinessModel)model, physicalTable, editingDomain,(ISpagoBIModelCommand)addBusinessTableCommand);
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		dialog.create();
		dialog.open();
		
		return true;
	}
	
	public boolean executeAddPhysicalTableToBusinessColumnSetAction(BusinessColumnSet businessColumnSet, PhysicalTable sourcePhysicalTable) {
		boolean isBusinessView = false;
		BusinessView businessView = null;
		if (businessColumnSet instanceof BusinessView){
			isBusinessView = true;
			businessView = (BusinessView)businessColumnSet;
		}
		else
		
		/*
		//OLD IMPLEMENTATION - DO NOT REMOVE
		//add PhysicalTable to BusinessTable or BusinessView
		Command addPhysicalTableCommand = editingDomain.createCommand
		(AddPhysicalTableToBusinessViewCommand.class, 
				new CommandParameter(businessColumnSet, null, null, new ArrayList<Object>()));
		AddPhysicalTableWizard wizard = new AddPhysicalTableWizard(businessColumnSet,editingDomain, (ISpagoBIModelCommand)addPhysicalTableCommand, isBusinessView, sourcePhysicalTable.getName());
		WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.create();
		dialog.open();
		*/
		
		if (!isBusinessView){
			//Upgrade BusinessTable to BusinessView
			BusinessTable businessTable = (BusinessTable)businessColumnSet;
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			businessView = initializer.upgradeBusinessTableToBusinessView(businessTable);
		}
		
		//check if the physicalTable is already in the BusinessView
		if ( businessView.getPhysicalTables().contains(sourcePhysicalTable) ){
			//Do nothing
			//TODO: check what to do in this case

		} else {
			//Add All Physical Columns to the BusinessView
			List<PhysicalColumn> physicalColumns = sourcePhysicalTable.getColumns();
			for(PhysicalColumn physicalColumn : physicalColumns){
				executeAddColumnsToBusinessTableAction(businessView, physicalColumn);
			}
		}
		
		Command editBusinessViewInnerJoinWizard =  editingDomain.createCommand
		(EditBusinessViewInnerJoinRelationshipsCommand.class,
				new CommandParameter(businessView, null, null, new ArrayList<Object>()));

		//Open the Edit Join Paths Wizard
		EditBusinessViewInnerJoinWizard wizard = new EditBusinessViewInnerJoinWizard(businessView, editingDomain, (ISpagoBIModelCommand)editBusinessViewInnerJoinWizard);
		WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.create();
		dialog.open();
		
		//this will "undo" the operation of upgrade to Business View if the user didn't add any join path in the previuos wizard
		if ( (!isBusinessView) && ((businessView.getJoinRelationships().size()) < 1) ){
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			initializer.downgradeBusinessViewToBusinessTable(businessView);
		}
		
		
		//update businessColumnSet reference in case that BusinessTable upgraded to BusinessView
		businessColumnSet = ((BusinessModel)model).getTable(businessColumnSet.getName());
		
		return true;
	}
	
	
	public boolean executeAddPhysicalColumnToBusinessColumnSetAction(BusinessColumnSet businessColumnSet, PhysicalColumn physicalColumn) {
		if (physicalColumn == null) return false;
		
		//check if target is a BusinessView or BusinessTable
		if ( businessColumnSet instanceof BusinessView ){
			executeAddPhysicalColumnToBusinessViewAction((BusinessView)businessColumnSet, physicalColumn);
		}
		else if ( businessColumnSet instanceof BusinessTable ){
			executeAddPhysicalColumnToBusinessTableAction((BusinessTable)businessColumnSet, physicalColumn);
		}
		
		return true;
	}
	
	public boolean executeAddPhysicalColumnToBusinessViewAction(BusinessView businessView, PhysicalColumn physicalColumn) {
		
		// check if column's source PhysicalTable is inside target Physical Tables
		List<PhysicalTable> targetPhysicalTables = businessView.getPhysicalTables();
		PhysicalTable sourcePhysicalTable = physicalColumn.getTable();

		if (targetPhysicalTables.contains(sourcePhysicalTable)){
			//initializer.addColumn(physicalColumn, businessView);
			executeAddColumnsToBusinessTableAction(businessView, physicalColumn);
		} else {
			executeAddPhysicalTableToBusinessColumnSetAction(businessView, sourcePhysicalTable);
			//initializer.addColumn(physicalColumn, businessView);
			executeAddColumnsToBusinessTableAction(businessView, physicalColumn);
		}
		
		return true;
	} 
	
	public void executeAddPhysicalColumnToBusinessTableAction(BusinessTable businessTable, PhysicalColumn physicalColumn) {
		
		PhysicalTable sourcePhysicalTable = physicalColumn.getTable();
		PhysicalTable targetPhysicalTable = businessTable.getPhysicalTable();
		if (targetPhysicalTable != null){
			//if target table has the same PhysicalTable of the added columns, then perform the addColumn
			if (sourcePhysicalTable.equals(targetPhysicalTable)){
				executeAddColumnsToBusinessTableAction(businessTable, physicalColumn);
				//initializer.addColumn(physicalColumn, businessTable);
			}
			//if target table has a different PhysicalTable, then upgrade to BusinessView is necessary
			else {
				//upgrade BusinessTable to BusinessView
				
				/*
				//Old Implementation DO NOT REMOVE
				Command addPhysicalTableCommand = editingDomain.createCommand
				(AddPhysicalTableToBusinessViewCommand.class, 
						new CommandParameter(businessTable, null, null, new ArrayList<Object>()));
				AddPhysicalTableWizard wizard = new AddPhysicalTableWizard(businessTable,editingDomain, (ISpagoBIModelCommand)addPhysicalTableCommand, false, sourcePhysicalTable.getName());
				*/
				
				BusinessModelInitializer initializer = new BusinessModelInitializer();
				BusinessView businessView = initializer.upgradeBusinessTableToBusinessView(businessTable);
				Command editBusinessViewInnerJoinWizard =  editingDomain.createCommand
					(EditBusinessViewInnerJoinRelationshipsCommand.class,
							new CommandParameter(businessView, null, null, new ArrayList<Object>()));

				//add the column
				executeAddColumnsToBusinessTableAction(businessView, physicalColumn);
				
				EditBusinessViewInnerJoinWizard wizard = new EditBusinessViewInnerJoinWizard(businessView, editingDomain, (ISpagoBIModelCommand)editBusinessViewInnerJoinWizard);
				WizardDialog dialog = new WizardDialog(new Shell(), wizard);
				dialog.create();
				dialog.open();

				//re-set businessColumnSet reference to point to BusinessView
				//BusinessView businessView = (BusinessView)((BusinessModel)model).getTable(businessTable.getName());


				//initializer.addColumn(physicalColumn, businessView);
			}
		}
		else {
			//target is an empty Business Table
			executeAddColumnsToBusinessTableAction(businessTable, physicalColumn);
		}
			

	}
	
	public boolean executeAddColumnsToBusinessTableAction(BusinessColumnSet businessColumnSet, PhysicalColumn physicalColumn) {
		List<PhysicalColumn> columnsToAdd = new ArrayList();
		columnsToAdd.add(physicalColumn);
//		Command addColumnsToBusinessTableCommand = editingDomain.createCommand(
//				AddColumnsToBusinessTable.class, 
//				new CommandParameter(businessColumnSet, null, columnsToAdd, null)
//		);
		
		Command addColumnsToBusinessTableCommand = new AddColumnsToBusinessTable(editingDomain, new CommandParameter(businessColumnSet, null, columnsToAdd, null));
		
		editingDomain.getCommandStack().execute( addColumnsToBusinessTableCommand );
		return true;
	}

	public boolean validateDrop(Class dataType, Object target, boolean isLocationBeoforeOrAfterTarget) {
		return true;
	}
}
