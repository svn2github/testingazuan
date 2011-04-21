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
package it.eng.spagobi.meta.model.provider;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.phantom.provider.FolderItemProvider;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.AdapterFactoryItemDelegator;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class CustomItemPropertyDescriptor implements IItemPropertyDescriptor {
	
	ModelProperty property;
	protected AdapterFactoryItemDelegator itemDelegator;
	protected Object staticImage;
	
		
	public CustomItemPropertyDescriptor(ModelProperty property, AdapterFactory adapterFactory, ResourceLocator resourceLocator) {
		this.property = property;
		this.itemDelegator = new ItemDelegator(adapterFactory, resourceLocator);
	}

	@Override
	public boolean canSetProperty(Object o) {
		return true;
	}

	@Override
	public String getCategory(Object o) {
		return property.getPropertyType().getCategory().getName();
	}

	@Override
	public Collection<?> getChoiceOfValues(Object arg0) {
		/*
		List<Enumerator> enumerators = new ArrayList<Enumerator>();
		Collection<String> admissibleValues = property.getPropertyType().getAdmissibleValues();
		int index = 0;
		for(String admissibleValue : admissibleValues) {
			enumerators.add(new AbstractEnumerator(index++, admissibleValue){});
		}
		return enumerators;
		*/
		//return property.getPropertyType().getAdmissibleValues();
		return null;
	}

	@Override
	public String getDescription(Object object) {
		return property.getPropertyType().getDescription();
	}

	@Override
	public String getDisplayName(Object object) {
		return property.getPropertyType().getName();
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
		return property.getPropertyType().getId();
	}

	@Override
	public IItemLabelProvider getLabelProvider(Object object) {
		return itemDelegator;
	}

	@Override
	public Object getPropertyValue(Object object) {
		ModelProperty p = null;
		if(object instanceof ModelObject) {
			ModelObject modelObject = (ModelObject)object;
			p = modelObject.getProperties().get(property.getPropertyType().getId());
			
		} else if(object instanceof FolderItemProvider) {
			FolderItemProvider folderItemProvider = (FolderItemProvider)object;
			ModelObject modelObject = (ModelObject)folderItemProvider.getParentObject();
			p = modelObject.getProperties().get(property.getPropertyType().getId());
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
		property.setValue("NULL");
		
	}

	@Override
	public void setPropertyValue(Object object, Object value) {
		property.setValue("" + value);		
	}
	
	 /**
	   * This class uses a static image
	   */
	  protected class ItemDelegator extends AdapterFactoryItemDelegator {
	    protected ResourceLocator resourceLocator;

	    public ItemDelegator(AdapterFactory adapterFactory) {
	      super(adapterFactory);
	    }

	    public ItemDelegator(AdapterFactory adapterFactory, ResourceLocator resourceLocator) {
	      super(adapterFactory);
	      this.resourceLocator = resourceLocator;
	    }

	    @Override
	    public String getText(Object object) {
	    	
	    	return (String)object;
	    }

	    @Override
	    public Object getImage(Object object) {
	      return staticImage == null ? super.getImage(object) : staticImage;
	    }
	  }

}
