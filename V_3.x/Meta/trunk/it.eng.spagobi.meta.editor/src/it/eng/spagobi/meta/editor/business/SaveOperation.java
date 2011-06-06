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

import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.commons.utils.SpagoBIMetaConstants;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.validator.ModelValidator;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SaveOperation extends WorkspaceModifyOperation {
	
	Resource resource;
	URI resourceURI;
	
	private static Logger logger = LoggerFactory.getLogger(SaveOperation.class);
	
	
	public SaveOperation(Resource resource, URI resourceURI) {
		this.resource = resource;
		this.resourceURI = resourceURI;
	}
	
	@Override
	public void execute(IProgressMonitor monitor) {
		
		TreeIterator<EObject> eObjects;
		List<Model> models = new ArrayList<Model>();
		
		logger.trace("IN");
		
		try {
			eObjects = resource.getAllContents();
			if(eObjects.hasNext() == false) {
				throw new SpagoBIPluginException("The resource to save does not contain any model");
			}
			
			while(eObjects.hasNext()) {
				Object eObject = eObjects.next();
				if(eObject != null) {
					if(eObject instanceof Model) {
						Model model = (Model)eObject;
						models.add(model);
					} else {
						logger.debug(eObject.getClass().getName());
					}
				}
			}
			
			if(models.size() < 1) {
				throw new SpagoBIPluginException("The resource to save doesnt contain any model");
			}
			if(models.size() > 1) {
				throw new SpagoBIPluginException("The resource to save contains more than one model");
			}
			
			ModelValidator validator = new ModelValidator();
			if(validator.validate(models.get(0)) == false) {
				throw new SpagoBIPluginException(validator.getDiagnosticMessage());
			}
			resource.save(Collections.EMPTY_MAP);
			
//			File resourceFile = new File(resourceURI.toFileString());
//			if(resourceFile.delete() == false) {
//				throw new SpagoBIPluginException("Destination file [" + resourceFile + "] alredy exists and cannot be deleted");
//			}
//			refreshWorkspace();
			
//			EmfXmiSerializer serializer = new EmfXmiSerializer();
//			serializer.serialize(models.get(0), resourceFile);
		    refreshWorkspace();
		    logger.debug("Model [" + models.get(0).getName() + "] succesfully saved into file [" + resourceURI.toFileString() + "]");
		    

		   
		} catch(Throwable t) {
			throw new SpagoBIPluginException("Impossible to save model", t);
		} finally {
			logger.trace("OUT");
		}
	}
	
	private void refreshWorkspace() {
		IWorkspace workspace= ResourcesPlugin.getWorkspace();    
		IPath location= Path.fromOSString(resourceURI.toFileString()); 
		IFile ifile= workspace.getRoot().getFileForLocation(location);
        try {
        	// set the dirty property to model file cause it has just been modified
    		logger.debug("set the model as dirty");
    		ifile.setPersistentProperty(SpagoBIMetaConstants.DIRTY_MODEL, "true");
        	ifile.refreshLocal(IResource.DEPTH_ZERO, null);
			logger.debug("Refresh Local workspace on [{}]", ifile.getRawLocation().toOSString());
		} catch (CoreException e) {
			logger.error("Refresh Local workspace error [{}]",e);
			e.printStackTrace();
		}
	}
}
