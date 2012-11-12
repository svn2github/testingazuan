/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCompoundCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.RemoveColumnsFromBusinessTable;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.validator.ModelExtractor;
import it.eng.spagobi.meta.model.validator.ModelValidator;

import java.util.Collection;
import java.util.EventObject;
import java.util.Iterator;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CommandStack;
import org.eclipse.emf.common.command.CommandStackListener;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.DeleteCommand;
import org.eclipse.jface.dialogs.MessageDialog;
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
						|| mostRecentCommand instanceof DeleteCommand 
						|| mostRecentCommand instanceof CompoundCommand) {
							editor.firePropertyChange(IEditorPart.PROP_DIRTY);
							validateCommandResults(mostRecentCommand, false);  
							editor.refreshViewer();
							//editor.setSelectionToViewer(mostRecentCommand.getAffectedObjects());
						} 
					} else {
						// TODO: this is a strange behaviour. try to fix it!
						editor.firePropertyChange(IEditorPart.PROP_DIRTY);
						editor.refreshViewer();
					}
					
					if (editor.getPropertySheetPage() != null && editor.getPropertySheetPage().getControl()!=null && !editor.getPropertySheetPage().getControl().isDisposed()) {
					  editor.getPropertySheetPage().refresh();
					}
				}
				
				public void validateCommandResults(Command mostRecentCommand, boolean undoOnError) {
					Collection<?> affectedObjects = mostRecentCommand.getAffectedObjects();
					Iterator it = affectedObjects.iterator();
					if(it.hasNext()) {
						ModelObject modelObject = (ModelObject)it.next();
						Model model = ModelExtractor.getModel(modelObject);
						//Ignore OlapModelCommands
						if((model != null) && (!(model instanceof OlapModel))) {
							ModelValidator validator = new ModelValidator();
							if(validator.validate(model) == false) {
								showError("Impossible perform command " + mostRecentCommand.getLabel(), validator.getDiagnosticMessage());
								if(undoOnError) {
									mostRecentCommand.undo();
								}
							}
						} else {
							if ((!(mostRecentCommand instanceof RemoveColumnsFromBusinessTable) ) && (!(modelObject instanceof BusinessView))) {
								showError("Strange behaviour from command " + mostRecentCommand.getLabel(), "Impossible to resolve root model from edit command affected object [" + modelObject + "]");
							}
						}
					} else {
						showError("Strange behaviour from command " + mostRecentCommand.getLabel(), "Edit command does not specify any affected object");
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
