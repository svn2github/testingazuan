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
package it.eng.spagobi.meta.editor.properties;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;

import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * @author cortella
 *
 */
public class CustomizedPropertySource extends PropertySource {

	BusinessModelEditor editor;
	
	/**
	 * @param object
	 * @param itemPropertySource
	 */
	public CustomizedPropertySource(Object object, IItemPropertySource itemPropertySource) {
		super(object, itemPropertySource);
	}
	
	protected IPropertyDescriptor createPropertyDescriptor(IItemPropertyDescriptor itemPropertyDescriptor) {
		return new CustomizedPropertyDescriptor(object, itemPropertyDescriptor);
	}
	
	public void setPropertyValue(Object propertyId, Object value) {
		super.setPropertyValue(propertyId, value);
	    if(editor != null) {
	    	editor.setDirty(true);
	    	editor.firePropertyChange(IEditorPart.PROP_DIRTY);
	    }
	}

	public void setEditor(BusinessModelEditor editor) {
		this.editor = editor;
	}

}
