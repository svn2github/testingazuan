/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.wizards.inline.GenerateJPAMappingWizard;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.generate.GenerateJPAMappingCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;
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
			GenerateJPAMappingWizard wizard = new GenerateJPAMappingWizard(businessModel, editingDomain, (ISpagoBIModelCommand)command );
	    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
			dialog.create();
	    	dialog.open();
	    	
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}
}
