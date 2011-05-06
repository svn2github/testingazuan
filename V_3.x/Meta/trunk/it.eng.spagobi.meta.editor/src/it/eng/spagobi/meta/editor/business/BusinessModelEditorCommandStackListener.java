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
package it.eng.spagobi.meta.editor.business;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCompoundCommand;
import it.eng.spagobi.meta.model.validator.ModelExtractor;
import it.eng.spagobi.meta.model.validator.ModelValidator;

import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.ui.viewer.IViewerProvider;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelEditorCommandStackListener implements CommandStackListener {
	
	BusinessModelEditor editor;
	
	public BusinessModelEditorCommandStackListener(BusinessModelEditor editor) {
		this.editor = editor;
	}
	
	public void commandStackChanged(final EventObject event) {
		Display.getDefault().asyncExec
			(new Runnable() {
				public void run() {
					Command mostRecentCommand = ((CommandStack)event.getSource()).getMostRecentCommand();
					if(mostRecentCommand != null){
						if(mostRecentCommand instanceof AbstractSpagoBIModelEditCommand 
						|| mostRecentCommand instanceof AbstractSpagoBIModelEditCompoundCommand
						|| mostRecentCommand instanceof DeleteCommand) {
							editor.firePropertyChange(IEditorPart.PROP_DIRTY);
							validateCommandResults(mostRecentCommand, false);  
							editor.refreshViewer();
							editor.setSelectionToViewer(mostRecentCommand.getAffectedObjects());
						}
					} else {
						// TODO: this is a strange behaviour. try to fix it!
						editor.firePropertyChange(IEditorPart.PROP_DIRTY);
						editor.refreshViewer();
					}
					
					if (editor.getPropertySheetPage() != null && !editor.getPropertySheetPage().getControl().isDisposed()) {
					  editor.getPropertySheetPage().refresh();
					}
				}
				
				public void validateCommandResults(Command mostRecentCommand, boolean undoOnError) {
					Collection<?> affectedObjects = mostRecentCommand.getAffectedObjects();
					Iterator it = affectedObjects.iterator();
					if(it.hasNext()) {
						ModelObject modelObject = (ModelObject)it.next();
						Model model = ModelExtractor.getModel(modelObject);
						if(model != null) {
							ModelValidator validator = new ModelValidator();
							if(validator.validate(model) == false) {
								showError("Impossible perform command " + mostRecentCommand.getLabel(), validator.getDiagnosticMessage());
								if(undoOnError) {
									mostRecentCommand.undo();
								}
							}
						} else {
							showError("Strange behaviour from command " + mostRecentCommand.getLabel(), "Impossible to resolve root model from edit command affected object [" + modelObject + "]");
						}
					} else {
						showError("Starnge behaviour from command " + mostRecentCommand.getLabel(), "Edit command does not specify any affected object");
					}
				}
			});
	}
	
	
	
	public void showError(final String title, final String message) {
		  Display.getDefault().asyncExec(new Runnable() {
		    @Override
		    public void run() {
		      MessageDialog.openError(null, title, message);
		    }
		  });
		}	

}
