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

import java.util.ArrayList;

import it.eng.spagobi.meta.edit.properties.CustomItemPropertyDescriptor;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;

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
	public CellEditor createPropertyEditor(Composite composite) {
	
		if (itemPropertyDescriptor instanceof CustomItemPropertyDescriptor) {
			CellEditor result;
			if(itemPropertyDescriptor.getChoiceOfValues(object) != null && itemPropertyDescriptor.getChoiceOfValues(object).size() > 1) {
				result = new ExtendedComboBoxCellEditor(
				        composite,
				        new ArrayList<Object>(itemPropertyDescriptor.getChoiceOfValues(object)),
				        getEditLabelProvider(),
				        itemPropertyDescriptor.isSortChoices(object));
			} else {
				result = createEDataTypeCellEditor(EcorePackage.eINSTANCE.getEString(), composite);
			}
			return result;
		} else {
			// if not a custom property create property editor as usual
			return super.createPropertyEditor(composite);
		}
	}

}
