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
package it.eng.spagobi.meta.model.business.actions;

import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.EditBusinessColumnsCommand;
import it.eng.spagobi.meta.model.business.wizards.EditBusinessColumnsWizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.edit.command.CommandActionDelegate;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.StaticSelectionCommandAction;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
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
	
	public void configureAction(ISelection selection) {
		// only handle structured selections
	    if (!(selection instanceof IStructuredSelection))
	    {
	      disable();
	    }
	    else
	    {
	      // convert the selection to a collection of the selected objects
	      IStructuredSelection sselection = (IStructuredSelection) selection;
	      List<?> list = sselection.toList();
	      collection = new ArrayList<Object>(list);
	      if (collection.size() == 1) {
	    	  owner = collection.iterator().next();
	      }
	      
	      // if the editing domain wasn't given by the workbench part, try to get
	      // it from the selection
	      if (editingDomain == null)
	      {
	        for (Object o : collection)
	        {
	          editingDomain = AdapterFactoryEditingDomain.getEditingDomainFor(o);
	          if (editingDomain != null)
	          {
	            break;
	          }
	        }
	      }

	      // if we found an editing domain, create command
	      if (editingDomain != null)
	      {
	        command = createActionCommand(editingDomain, collection);
	        setEnabled(true);
	      }

	      // give up if we couldn't create the command; otherwise, use a
	      // CommandActionDelegate to set the action's text, tool-tip, icon,
	      // etc. or just use the default icon
	      if (command == null || command == UnexecutableCommand.INSTANCE)
	      {
	        disable();
	      }
	      else if (!(command instanceof CommandActionDelegate))
	      {
	        if (getDefaultImageDescriptor() != null)
	        {
	          setImageDescriptor(getDefaultImageDescriptor());
	        }
	      }
	      else
	      {
	        CommandActionDelegate commandActionDelegate =
	          (CommandActionDelegate) command;

	        ImageDescriptor imageDescriptor =
	          objectToImageDescriptor(commandActionDelegate.getImage());
	        if (imageDescriptor != null)
	        {
	          setImageDescriptor(imageDescriptor);
	        }
	        else if (getDefaultImageDescriptor() != null)
	        {
	          setImageDescriptor(getDefaultImageDescriptor());
	        }

	        if (commandActionDelegate.getText() != null)
	        {
	          setText(commandActionDelegate.getText());
	        }
	        
	        if (commandActionDelegate.getDescription() != null)
	        {
	          setDescription(commandActionDelegate.getDescription());
	        }

	        if (commandActionDelegate.getToolTipText() != null)
	        {
	          setToolTipText(commandActionDelegate.getToolTipText());
	        }
	      }
	    }
	  }
	 
	@Override
	protected Command createActionCommand(EditingDomain editingDomain, Collection<?> collection) {
		
		if (collection.size() == 1) {
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
