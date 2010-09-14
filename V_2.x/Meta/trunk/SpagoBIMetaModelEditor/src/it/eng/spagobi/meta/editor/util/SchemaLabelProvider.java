/*
 * LabelProvider for Schema selection
 */
package it.eng.spagobi.meta.editor.util;

import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;


public class SchemaLabelProvider extends LabelProvider {

	public SchemaLabelProvider() {
		
	}

	public String getText(Object element){
		return ((String)element);
	}
	
	public Image getImage(Object element){
		return Activator.getImageDescriptor("database.png").createImage();
	}

}
