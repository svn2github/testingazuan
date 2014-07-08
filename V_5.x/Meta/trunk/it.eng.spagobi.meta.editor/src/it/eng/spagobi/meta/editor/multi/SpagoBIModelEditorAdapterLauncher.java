/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.multi;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelPackage;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorLauncher;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIModelEditorAdapterLauncher implements IEditorLauncher {

	private static IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();
	private static Logger logger = LoggerFactory.getLogger(SpagoBIModelEditorAdapterLauncher.class);
		
	@Override
	public void open(IPath path) {
		Resource resource;
		File modelFile;
		Model spagobiModel;
		
		logger.trace("IN");
		
		modelFile = null;
		try {
			
			// Register the package (i.e. load the meta model description). Do not remove
			// this line otherwise resource object cannot successfully load the model from the target file
			ModelPackage libraryPackage = ModelPackage.eINSTANCE;
			
			Assert.assertNotNull("Input parameter [path] cannot be null", path);
			modelFile = new File(path.toOSString());
			resource = loadResourceFromFile(modelFile);
			Assert.assertNotNull("Method [loadResourceFromFile] cannot return  null", path);
	
			TreeIterator<EObject> it = resource.getAllContents();
			if(it.hasNext()) {
				spagobiModel = (Model)it.next();
				SpagoBIModelEditorInput editorInput = new SpagoBIModelEditorInput(modelFile, spagobiModel);
				openSpagoBIModelEditor(editorInput);
			}
		} catch (Throwable t) {
			throw new SpagoBIPluginException("Impossible to load resource from file [" + modelFile + "]", t);
		} finally {
			logger.trace("OUT");
		}
	}
	
	protected Resource loadResourceFromFile(File modelFile) {
		
		URI modelURI;
		Map<Object, Object> options;
		ResourceSet resourceSet;
		Resource resource;
		
		logger.trace("IN");
	
		resource = null;
		try {
			modelURI = URI.createFileURI(modelFile.getAbsolutePath().toString());
			Assert.assertNotNull("Method [getModelURI] cannot return null", modelURI);
			
		    //Force Workspace refresh
			IWorkspace workspace= ResourcesPlugin.getWorkspace();    
			IPath location= Path.fromOSString(modelFile.getAbsolutePath()); 
			IFile ifile= workspace.getRoot().getFileForLocation(location);
	        try {
	        	ifile.refreshLocal(IResource.DEPTH_ZERO, null);
				logger.debug("Refresh Local workspace on [{}]",ifile.getRawLocation().toOSString());

			} catch (CoreException e) {
				logger.error("Refresh Local workspace error [{}]",e);
				e.printStackTrace();
			}
			//*******
			
			resourceSet = new ResourceSetImpl();
			resource = resourceSet.createResource(modelURI);
			options = new HashMap<Object, Object>();
			
			try {
				resource.load(options);
			} catch (Throwable t) {
				throw new SpagoBIPluginException("Impossible to load resource from URI [" + modelURI + "]", t);
			}
			
			logger.debug("Resource loded succesfully from URI [{}]", modelURI);
		} finally {
			logger.trace("OUT");
		}
		
		return resource;
	}
	
	protected void openSpagoBIModelEditor(SpagoBIModelEditorInput editorInput) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			logger.debug("Open editor on model [{}]", editorInput.getName());
			IEditorPart editor = page.openEditor( editorInput , SpagoBIModelEditor.EDITOR_ID);
			
			SpagoBIModelEditor modelEditor = (SpagoBIModelEditor)editor;
			modelEditor.setFocus();
			IEditorPart[] innerEditors = modelEditor.getInnerEditors();
			innerEditors[0].setFocus();
			
		} catch (PartInitException exception) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					RL.getString("business.editor.error.open"), exception.getMessage());
		}
		
	}

}
