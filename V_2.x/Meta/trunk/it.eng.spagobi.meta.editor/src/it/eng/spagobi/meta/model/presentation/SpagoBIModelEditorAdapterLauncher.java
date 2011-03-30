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
package it.eng.spagobi.meta.model.presentation;

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.editor.SpagoBIMetaModelEditorPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorLauncher;
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
			
			Assert.assertNotNull("Inut parameter [path] cannot be null", path);
			modelFile = new File(path.toOSString());
			resource = loadResourceFromFile(modelFile);
			Assert.assertNotNull("Method [loadResourceFromFile] cannot return  null", path);
	
			TreeIterator<EObject> it = resource.getAllContents();
			if(it.hasNext()) {
				spagobiModel = (Model)it.next();
				SpagoBIModelInput editorInput = new SpagoBIModelInput(modelFile, spagobiModel);
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
	
	protected void openSpagoBIModelEditor(SpagoBIModelInput editorInput) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			logger.debug("Open editor on medel [{}]", editorInput.getName());
			page.openEditor( editorInput , SpagoBIModelEditor.PLUGIN_ID );
		} catch (PartInitException exception) {
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SpagoBIMetaModelEditorPlugin.INSTANCE.getString("_UI_OpenEditorError_label"), exception.getMessage());
		}
		
	}

}
