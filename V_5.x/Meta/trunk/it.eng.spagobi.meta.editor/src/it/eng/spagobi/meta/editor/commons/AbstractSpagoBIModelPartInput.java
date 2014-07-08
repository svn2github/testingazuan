/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.commons;

import it.eng.spagobi.meta.model.ModelObject;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractSpagoBIModelPartInput implements IEditorInput{

	// the file used to load/store the model
	File resourceFile;	
	URI rootObjectURI;
	String rootObjectName;
	String rootObjectDescription;
	
	
	public AbstractSpagoBIModelPartInput(File resourceFile, EObject rootObject) {
		setResourceFile(resourceFile);
		setRootObject(rootObject);
	}
	
	
	public URI getResourceFileURI() {
		return resourceFile!=null? URI.createFileURI(resourceFile.getAbsolutePath().toString()): null;
	}
	
	public File getResourceFile() {
		return resourceFile;
	}

	public void setResourceFile(File resourceFile) {
		this.resourceFile = resourceFile;
	}

	public URI getRootObjectURI() {
		return rootObjectURI;
	}


	public void setRootObject(EObject rootObject) {
		this.rootObjectURI = EcoreUtil.getURI(rootObject);
		if(rootObject instanceof ModelObject) {
			ModelObject r = (ModelObject)rootObject;
			this.rootObjectName = r.getName();
			this.rootObjectDescription = r.getDescription();
		}
	}
	
	public void setRootObject(URI rootObjectURI) {
		this.rootObjectURI = rootObjectURI;
	}


	
	@Override
	public Object getAdapter(Class clazz) {
		return null;
	}

	@Override
	public boolean exists() {
		return false;
	}

	@Override
	public ImageDescriptor getImageDescriptor() {
		return null;
	}
	
	@Override
	public IPersistableElement getPersistable() {
		return null;
	}


	@Override
	public String getName() {
		String name;
		
		name = this.rootObjectName;
		if(this.rootObjectName == null || this.rootObjectName.trim().length() == 0) {
			name = "Input Model";
		}
		
		return name;
	}
	
	@Override
	public String getToolTipText() {
		String description;
		
		description = this.rootObjectDescription;
		if(this.rootObjectDescription == null || this.rootObjectDescription.trim().length() == 0) {
			description = resourceFile.getAbsolutePath();
		}
		
		return description;
	}

}
