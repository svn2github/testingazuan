/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.provider;


import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.edit.SpagoBIMetaEditPlugin;
import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.business.SimpleBusinessColumn} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class SimpleBusinessColumnItemProvider
	extends BusinessColumnItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	
	 private OlapModelInitializer olapModelInitializer = new OlapModelInitializer();

	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SimpleBusinessColumnItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addPhysicalColumnPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Physical Column feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	protected void addPhysicalColumnPropertyDescriptor(Object object) {
		IResourceLocator RL = SpagoBIMetaEditPlugin.getInstance().getResourceLocator();
		
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 RL.getString("model.business.column.feature.physicalcolumn.name"),
				 RL.getString("model.property.descriptor", new Object[]{"model.business.column.feature.physicalcolumn.longname", "model.business.column.type"}),
				 BusinessModelPackage.Literals.SIMPLE_BUSINESS_COLUMN__PHYSICAL_COLUMN,
				 false,
				 false,
				 true,
				 null,
				 "Physical References",
				 null));
	}

	/**
	 * This returns SimpleBusinessColumn.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public Object getImage(Object object) {
		//return overlayImage(object, getResourceLocator().getImage("full/obj16/SimpleBusinessColumn"));
		BusinessColumn businessColumn = ((BusinessColumn)object);
		//if the column is a identifier display the appropriate icon
		if (businessColumn.isIdentifier() || businessColumn.isPartOfCompositeIdentifier()){
			return overlayImage(object, getResourceLocator().getImage("full/obj16/BusinessIdentifier"));
		}
		if (olapModelInitializer.getMeasure((SimpleBusinessColumn)object) != null){
			return overlayImage(object, getResourceLocator().getImage("full/obj16/Measure"));
		}

		return overlayImage(object, getResourceLocator().getImage("full/obj16/BusinessColumn"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public String getText(Object object) {
		/*
		String label = ((SimpleBusinessColumn)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_SimpleBusinessColumn_type") :
			getString("_UI_SimpleBusinessColumn_type") + " " + label;
		*/
		String label = ((SimpleBusinessColumn)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_SimpleBusinessColumn_type") :
			/*getString("_UI_BusinessColumn_type") + " " + */ label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
	 * that can be created under this object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

}
