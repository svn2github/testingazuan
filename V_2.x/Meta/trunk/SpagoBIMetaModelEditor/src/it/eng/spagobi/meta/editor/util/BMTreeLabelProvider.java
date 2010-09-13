/*
 * LabelProvider for TreeViewer inside GraphicEditorView
 */
package it.eng.spagobi.meta.editor.util;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.model.BCField;
import it.eng.spagobi.meta.editor.model.BusinessClass;
import it.eng.spagobi.meta.editor.model.BusinessModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;


public class BMTreeLabelProvider implements ILabelProvider {

	private Map<ImageDescriptor,Image> imageCache;
	
	public BMTreeLabelProvider(){
		imageCache = new HashMap<ImageDescriptor,Image>();
	}
	
	
	@Override
	public Image getImage(Object element) {
		ImageDescriptor descriptor = null;

		if (element instanceof BusinessModel) {
			descriptor = Activator.getImageDescriptor("businessmodel.png");
		} else if (element instanceof BusinessClass) {
			descriptor = Activator.getImageDescriptor("businessclass.png");
		} else if (element instanceof BCField) {
			descriptor = Activator.getImageDescriptor("field.png");
		} else if (element instanceof String) {
			descriptor = Activator.getImageDescriptor("arrow.png");
		} else {
			return null;
		}  
		//obtain the cached image corresponding to the descriptor
		Image image = (Image)imageCache.get(descriptor);
		if (image == null) {
			image = descriptor.createImage();
			imageCache.put(descriptor, image);
		}
		return image;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof BusinessModel) {
			return ((BusinessModel)element).getName();
		} else if (element instanceof BusinessClass) {
			return ((BusinessClass)element).getName();
		} else if (element instanceof BusinessClass) {
			return ((BCField)element).getName();
		} else if (element instanceof String) {
			return ((String)element);
		} else {
			return "N/A";
		}  
	}

	@Override
	public void addListener(ILabelProviderListener arg0) {

	}

	@Override
	public void dispose() {
		for (Iterator<Image> i = imageCache.values().iterator(); i.hasNext();) 
		{
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	@Override
	public boolean isLabelProperty(Object arg0, String arg1) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener arg0) {

	}

}
