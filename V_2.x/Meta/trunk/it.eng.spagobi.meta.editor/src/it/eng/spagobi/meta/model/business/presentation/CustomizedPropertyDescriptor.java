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
package it.eng.spagobi.meta.model.business.presentation;

import it.eng.spagobi.meta.model.provider.CustomItemPropertyDescriptor;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

/**
 * @author cortella
 *
 */
public class CustomizedPropertyDescriptor extends PropertyDescriptor {

	/**
	 * @param object
	 * @param itemPropertyDescriptor
	 */
	public CustomizedPropertyDescriptor(Object object,
			IItemPropertyDescriptor itemPropertyDescriptor) {
		super(object, itemPropertyDescriptor);
	}

	@Override
    public CellEditor createPropertyEditor(Composite composite)
    {
      // Test for your case based on the feature or the type of the feature.
      // 
      // Check if the PropertyDescriptor is of type CustomPropertyDescriptor or not
      if (itemPropertyDescriptor instanceof CustomItemPropertyDescriptor){
    	  Object feature = itemPropertyDescriptor.getFeature(this.object);
      	  CellEditor result;
    	  //this force a simple Cell Editor for EString
    	  result = createEDataTypeCellEditor(EcorePackage.eINSTANCE.getEString(), composite);

    	  return result;
      }
      else {
    	  //if not a custom property create property editor as usual
          return super.createPropertyEditor(composite);
      }
	

    }

}
