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
