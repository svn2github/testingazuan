/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.edit.properties;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.phantom.provider.FolderItemProvider;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CustomItemPropertyDescriptor implements IItemPropertyDescriptor, IItemLabelProvider {
	
	ModelPropertyType propertyType;
	Object image;
	ResourceLocator resourceLocator;
		
	public CustomItemPropertyDescriptor(ModelPropertyType propertyType, AdapterFactory adapterFactory, ResourceLocator resourceLocator) {
		this(propertyType, adapterFactory, resourceLocator, null);
	}
	public CustomItemPropertyDescriptor(ModelPropertyType propertyType, AdapterFactory adapterFactory, ResourceLocator resourceLocator, Object image) {
		this.propertyType = propertyType;
		this.image = image;
		this.resourceLocator = resourceLocator;
	}

	@Override
	public boolean canSetProperty(Object o) {
		return true;
	}

	@Override
	public String getCategory(Object o) {
		return propertyType.getCategory().getName();
	}

	@Override
	public Collection<?> getChoiceOfValues(Object arg0) {
		return propertyType.getAdmissibleValues();
	}

	@Override
	public String getDescription(Object object) {
		return propertyType.getDescription();
	}

	@Override
	public String getDisplayName(Object object) {
		return propertyType.getName();
	}

	@Override
	public Object getFeature(Object object) {
		return ModelPackage.Literals.MODEL_PROPERTY;
	}

	@Override
	public String[] getFilterFlags(Object object) {
		return null;
	}

	@Override
	public Object getHelpContextIds(Object object) {
		return null;
	}

	@Override
	public String getId(Object object) {
		return propertyType.getId();
	}

	@Override
	public IItemLabelProvider getLabelProvider(Object object) {
		return this;
	}

	@Override
	public Object getPropertyValue(Object object) {
		ModelProperty p = null;
		if(object instanceof ModelObject) {
			ModelObject modelObject = (ModelObject)object;
			p = modelObject.getProperties().get(propertyType.getId());
			
		} else if(object instanceof FolderItemProvider) {
			FolderItemProvider folderItemProvider = (FolderItemProvider)object;
			ModelObject modelObject = (ModelObject)folderItemProvider.getParentObject();
			p = modelObject.getProperties().get(propertyType.getId());
		}
		
		return p != null? p.getValue(): null; 
	}

	@Override
	public boolean isCompatibleWith(Object arg0, Object arg1, IItemPropertyDescriptor arg2) {
		return true;
	}

	@Override
	public boolean isMany(Object object) {
		return false;
	}

	@Override
	public boolean isMultiLine(Object object) {
		return false;
	}

	@Override
	public boolean isPropertySet(Object object) {
		return getPropertyValue(object) != null;
	}

	@Override
	public boolean isSortChoices(Object object) {
		return false;
	}

	@Override
	public void resetPropertyValue(Object object) {
		setPropertyValue(object, propertyType.getDefaultValue());
		
	}

	@Override
	public void setPropertyValue(Object object, Object value) {
		ModelObject modelObject = (ModelObject)object;
		ModelProperty property = modelObject.getProperties().get(propertyType.getId());
		if(value != null) {
			property.setValue("" + value);	
		}
	}


	@Override
	public String getText(Object object) {
		return (String)object;
	}

	@Override
	public Object getImage(Object object) {
		return image;
	}
	
	public void setImage(Object image) {
		this.image = image;
	}

}
