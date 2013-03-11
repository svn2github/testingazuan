/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.dnd;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.edit.model.SortBusinessModelTablesCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.SortBusinessTableColumnsCommand;

import java.util.ArrayList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelDropFromBusinessModelHandler  {
	
	private EObject model;
	private Object nextTo;
	private AdapterFactoryEditingDomain editingDomain;
	
	public BusinessModelDropFromBusinessModelHandler(EObject model, AdapterFactoryEditingDomain editingDomain) {
		this.model = model;
		this.nextTo = null;
		this.editingDomain = editingDomain;
	}
	
	public boolean performDrop(Object data, Object currentTarget, boolean isLocationBeoforeOrAfterTarget) {
		if (data instanceof TreeSelection){
			return performBusinessModelObjectDrop(data,currentTarget);
		} else {
			return false;
		}
	}
	
	/*
	 * Used for resort of BusinessTable/View and Business Columns inside BusinessModelEditor with drag&drop
	 */
	private boolean performBusinessModelObjectDrop(Object data, Object target){
		//check if current target is a BusinessColumnSet (BusinessTable/BusinessView)
		if (target instanceof BusinessColumnSet){
			if (data != null)
			{
				StructuredSelection selection = (StructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
				BusinessColumnSet businessColumnSet = (BusinessColumnSet)selection.getFirstElement();

				if (model instanceof BusinessModel){
					BusinessModel businessModel = (BusinessModel)model;
					if(nextTo != null)
					{
						for (int i = 0; i < businessModel.getTables().size(); i++)
						{
							Object item = businessModel.getTables().get(i);
							if(item == nextTo)
							{
								//businessModel.getTables().move(i, businessColumnSet);
								try {
									CommandParameter commandParameter =  new CommandParameter(businessModel, i, businessColumnSet, new ArrayList<Object>());
								    if (editingDomain != null) {	    	
								    	editingDomain.getCommandStack().execute(new SortBusinessModelTablesCommand(editingDomain,commandParameter));
								    }
								} catch(Throwable t) {
									t.printStackTrace();
								}
								
								break;
							}
						}
					}
				}
			}
			return true;
		}
		//check if current target is a BusinessColumn 
		else if (target instanceof BusinessColumn){
			if (data != null)
			{
				StructuredSelection selection = (StructuredSelection)LocalSelectionTransfer.getTransfer().getSelection();
				BusinessColumn businessColumn = (BusinessColumn)selection.getFirstElement();
				BusinessColumnSet businessColumnSet = businessColumn.getTable();
				
				if (model instanceof BusinessModel){
					BusinessModel businessModel = (BusinessModel)model;
					if(nextTo != null)
					{
						for (int i = 0; i < businessColumnSet.getColumns().size(); i++)
						{
							Object item = businessColumnSet.getColumns().get(i);
							if(item == nextTo)
							{
								//businessColumnSet.getColumns().move(i, businessColumn);
								try {
									CommandParameter commandParameter =  new CommandParameter(businessColumnSet, i, businessColumn, new ArrayList<Object>());
								    if (editingDomain != null) {	    	
								    	editingDomain.getCommandStack().execute(new SortBusinessTableColumnsCommand(editingDomain,commandParameter));
								    }
								} catch(Throwable t) {
									t.printStackTrace();
								}
								break;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public Object getNextTo() {
		return nextTo;
	}

	public void setNextTo(Object nextTo) {
		this.nextTo = nextTo;
	}

	public boolean validateDrop(Class dataType, Object target, boolean isLocationBeoforeOrAfterTarget) {
		
		return true;
	}
}
