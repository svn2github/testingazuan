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

import it.eng.spagobi.meta.editor.business.BusinessModelEditorInput;
import it.eng.spagobi.meta.editor.business.wizards.inline.GenerateJPAMappingWizard;
import it.eng.spagobi.meta.editor.multi.SpagoBIModelEditorAdapterLauncher;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.generate.GenerateJPAMappingCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class GenerateJPAMappingAction extends AbstractSpagoBIModelAction {
	private static Logger logger = LoggerFactory.getLogger(GenerateJPAMappingAction.class);

	/**
	 * @param commandClass
	 * @param workbenchPart
	 * @param selection
	 */
	public GenerateJPAMappingAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(GenerateJPAMappingCommand.class, workbenchPart, selection);
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			/*
			
			PlatformUI.getWorkbench().getDisplay().syncExec(
					new Runnable() {
						public void run() {
							IWorkbenchWindow window = PlatformUI
							.getWorkbench()
							.getActiveWorkbenchWindow();
							logger.debug("WorkbenchWindow is [{}]",window);
							if ( window != null ) {
								IWorkbenchPage page = window.getActivePage();
								logger.debug("WorkbenchPage is [{}]",page);
								if ( page != null ) {
									IEditorPart editor = page.getActiveEditor();
									if ( editor != null ) {
										IEditorInput input = editor.getEditorInput();
										logger.debug("EditorInput is [{}]",input);
										if ( input instanceof BusinessModelEditorInput ) {
											BusinessModelEditorInput fileInput = (BusinessModelEditorInput) input;
											logger.debug("Path is [{}]",fileInput.getResourceFileURI().toFileString());
											IPath path = new Path(fileInput.getResourceFile().getAbsolutePath());
											IFile file = ResourcesPlugin.getWorkspace().getRoot().getFileForLocation(path);
											logger.debug("File is [{}]",file);
											IProject activeProject = file.getProject();
											logger.debug("Project is [{}]",activeProject);
											logger.debug("Project name is [{}]",activeProject.getName());
											logger.debug("Project path is [{}]",activeProject.getLocation().toOSString());

										}
									}
								}					
							}
						}
					} );
			*/
			
			BusinessModel businessModel = (BusinessModel)((BusinessRootItemProvider)owner).getParentObject();
			GenerateJPAMappingWizard wizard = new GenerateJPAMappingWizard(businessModel, editingDomain, (AbstractSpagoBIModelCommand)command );
	    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
			dialog.create();
	    	dialog.open();
	    	
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
