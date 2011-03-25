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
package it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree;

import it.eng.qbe.model.structure.IModelNode;
import it.eng.qbe.model.structure.IModelObject;
import it.eng.spagobi.meta.querybuilder.ResourceRegistry;

import java.io.IOException;


import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.osgi.framework.Bundle;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public 	class ViewLabelProvider extends LabelProvider {

	private ModelLabelProvider modelLabelProvider;
	private static final String imgPath = "img";
	private TreeViewer viewer;
	
	public ViewLabelProvider (TreeViewer viewer, ModelLabelProvider modelLabelProvider){
		this.viewer = viewer;
		this.modelLabelProvider = modelLabelProvider;
	}
	
	public String getText(Object o) {
		String text;
		
		if(o instanceof IModelObject){
			IModelObject modelObject = (IModelObject)o;
			text =  modelLabelProvider.getLabel(modelObject);
		} else {
			text = o.getClass().getName();
		}
		
		return text;
	}
	public Image getImage(Object obj) {   
		String type = null;
		Image img = null;
		type = (String)((IModelNode)obj).getProperties().get("type");
		
		try {
			img = ResourceRegistry.getImage("it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree.node"+type);
		} catch (Exception e) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			img =  PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}

		return img;
	}
	

	
}