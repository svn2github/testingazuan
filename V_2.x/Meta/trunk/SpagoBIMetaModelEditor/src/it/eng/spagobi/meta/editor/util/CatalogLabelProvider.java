/*
 * LabelProvider for Catalog selection
 */
package it.eng.spagobi.meta.editor.util;

import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class CatalogLabelProvider extends LabelProvider {

	public CatalogLabelProvider() {
		
	}

	public String getText(Object element){
		return ((String)element);
	}
	
	public Image getImage(Object element){
		return Activator.getImageDescriptor("catalog.png").createImage();
	}

}
