/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.properties;

import it.eng.spagobi.meta.edit.properties.CustomItemPropertyDescriptor;

import java.util.ArrayList;

import org.eclipse.emf.common.ui.celleditor.ExtendedComboBoxCellEditor;
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
