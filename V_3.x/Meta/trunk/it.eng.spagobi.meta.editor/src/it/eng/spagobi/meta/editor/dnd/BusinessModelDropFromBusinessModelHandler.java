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
