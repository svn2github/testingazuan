/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.properties;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;
import it.eng.spagobi.meta.editor.olap.OlapModelManager;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

/**
 * @author Marco Cortella(macortel@eng.it)
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
	    
		//Save previous value 
		String previousValue = null;
	    if (object instanceof BusinessColumnSet){
		    previousValue = ((BusinessColumnSet)object).getProperties().get("structural.tabletype").getValue();
	    }
	    else if (object instanceof BusinessColumn){
		    previousValue = ((BusinessColumn)object).getProperties().get("structural.columntype").getValue();
	    }
	    //****************************

		super.setPropertyValue(propertyId, value);
	    if(editor != null) {
	    	editor.setDirty(true);
	    	editor.firePropertyChange(IEditorPart.PROP_DIRTY);
	    }
	    
	    //****************************
	    //	  OlapModel management
	    //****************************
	    EditingDomain editingDomain	 = editor.getEditingDomain();
	    //access to the singleton
	    OlapModelManager olapModelManager =  OlapModelManager.getInstance();
	    if ((propertyId.equals("name")) && (value instanceof String)){
	    	if (object instanceof BusinessColumnSet){
	    		olapModelManager.changeOlapObjectName((BusinessColumnSet)object,(String)value);
	    	}
	    }
	    olapModelManager.checkForOlapModel(editingDomain,object, propertyId, value, previousValue );
	    //****************************
	}

	public void setEditor(BusinessModelEditor editor) {
		this.editor = editor;
	}

}
