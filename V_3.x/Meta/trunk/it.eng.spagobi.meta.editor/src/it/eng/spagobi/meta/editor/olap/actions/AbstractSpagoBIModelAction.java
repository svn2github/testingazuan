/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.olap.actions;



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
