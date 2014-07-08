/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.properties;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.ui.views.properties.IPropertySource;

/**
 * @author cortella
 *
 */
public class CustomizedAdapterFactoryContentProvider extends AdapterFactoryContentProvider {

	BusinessModelEditor editor;

	/**
	 * @param adapterFactory
	 */
	public CustomizedAdapterFactoryContentProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}
	
	protected IPropertySource createPropertySource(Object object, IItemPropertySource itemPropertySource) {
		CustomizedPropertySource propertySource = new CustomizedPropertySource(object, itemPropertySource);
		propertySource.setEditor(editor);
		return propertySource;
	}

	public void setEditor(BusinessModelEditor editor) {
		this.editor = editor;
	}

}
