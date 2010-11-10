/*
 * LabelProvider for IConnectionProfile from the DSE View
 */
package it.eng.spagobi.meta.model.presentation;


import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.jface.viewers.LabelProvider;



public class CPLabelProvider extends LabelProvider {

	public CPLabelProvider() {
		
	}

	public String getText(Object element){
		return ((IConnectionProfile)element).getName();
	}
	


}
