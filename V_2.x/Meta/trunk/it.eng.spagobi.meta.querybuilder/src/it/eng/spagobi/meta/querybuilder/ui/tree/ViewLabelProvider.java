package it.eng.spagobi.meta.querybuilder.ui.tree;

import it.eng.qbe.model.structure.AbstractModelNode;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelNode;
import it.eng.qbe.model.structure.IModelObject;
import it.eng.qbe.model.structure.ModelField;

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
		Bundle generatorBundle = Platform.getBundle("it.eng.spagobi.meta.querybuilder");
		String path = null; 
		String type = null;
		Image img = null;
		try {
			IPath ipath = new Path(Platform.asLocalURL(generatorBundle.getEntry(imgPath)).getPath());
			path = ipath.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		type = (String)((IModelNode)obj).getProperties().get("type");
		path=path+type+".gif";
		
		try {
			img = new Image(viewer.getControl().getDisplay(), path); 
		} catch (Exception e) {
			String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
			img =  PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
		}

		return img;
	}
	

	
}