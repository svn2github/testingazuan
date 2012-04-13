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
package it.eng.spagobi.meta.editor.business.actions;



import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractSpagoBIModelAction extends StaticSelectionCommandAction {
	
	Class commandClass;
	Object owner;
	Object feature;
	Object value;
	Collection<Object> collection;
	
	public AbstractSpagoBIModelAction(Class commandClass, IWorkbenchPart workbenchPart, ISelection selection) {
		super(workbenchPart);
		this.commandClass = commandClass;
		configureAction(selection);
	}
	
	
	@Override
	protected Command createActionCommand(EditingDomain editingDomain, Collection<?> collection) {
		
		if (collection.size() == 1) {
			owner = collection.iterator().next();
		      return editingDomain.createCommand
		        (commandClass, 
		        		new CommandParameter(owner, null, null, new ArrayList<Object>(collection)));
		}
		return UnexecutableCommand.INSTANCE;
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		// override this please
	}
	
}
