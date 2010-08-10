/*
 * LabelProvider for IConnectionProfile from the DSE View
 */
package eng.it.spagobimeta.util;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import eng.it.spagobimeta.Activator;

public class DSE_LabelProvider extends LabelProvider {

	public DSE_LabelProvider() {
		
	}
	//override
	public String getText(Object element){
		return ((IConnectionProfile)element).getName();
	}
	public Image getImage(Object element){
		return Activator.getImageDescriptor("icons/database.png").createImage();
	}

}
