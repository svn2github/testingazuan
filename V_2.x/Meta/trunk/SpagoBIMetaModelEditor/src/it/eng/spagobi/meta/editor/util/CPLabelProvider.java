/*
 * LabelProvider for IConnectionProfile from the DSE View
 */
package it.eng.spagobi.meta.editor.util;

import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class CPLabelProvider extends LabelProvider {

	public CPLabelProvider() {
		
	}

	public String getText(Object element){
		return ((IConnectionProfile)element).getName();
	}
	
	public Image getImage(Object element){
		return Activator.getImageDescriptor("database.png").createImage();
	}

}
