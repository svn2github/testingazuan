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



import it.eng.spagobi.meta.editor.multi.wizards.SpagoBIModelEditorWizard;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class NewSpagoBIModelAction  implements IWorkbenchWindowActionDelegate {

	ISelection selection;
	private static Logger logger = LoggerFactory.getLogger(NewSpagoBIModelAction.class);

	public NewSpagoBIModelAction() {
	}


	public void run(IAction action) {
		logger.debug("IN");
		SpagoBIModelEditorWizard sbindw = new SpagoBIModelEditorWizard();
		IStructuredSelection sel=(IStructuredSelection)selection;
		IContainer containerSelected = null;
		Object objSel = sel.getFirstElement();
		// selection is limited to folder
		if(objSel instanceof IFolder){
			containerSelected = (IFolder)objSel;
		}
		else if(objSel instanceof IFile){
			containerSelected = ((IFile)objSel).getParent();
		}
		else if(objSel instanceof IProject){
			containerSelected = (IProject)objSel;
		}

		sbindw.init(PlatformUI.getWorkbench(), sel);

		if(objSel != null && containerSelected!= null){
			IPath pathSelected = containerSelected.getFullPath();
			sbindw.setContainerFullPath(pathSelected);

		}

		// init wizard
		// Create the wizard dialog
		WizardDialog dialog = new WizardDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),sbindw);
		// Open the wizard dialog
		dialog.open();	
		logger.debug("OUT");


	}

	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;		
	}

	public void setActivePart(IAction arg0, IWorkbenchPart arg1) {
		// TODO Auto-generated method stub

	}


	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}


	@Override
	public void init(IWorkbenchWindow window) {


	}

}