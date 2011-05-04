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
package it.eng.spagobi.meta.model.business.provider;


import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.commands.edit.identifier.AddColumnToIdentifierCommand;
import it.eng.spagobi.meta.model.business.commands.edit.identifier.RemoveColumnFromIdentifierCommand;
import it.eng.spagobi.meta.model.provider.ModelObjectItemProvider;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.IUpdateableItemText;

/**
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.business.BusinessColumn} object.
 */
public class BusinessColumnItemProvider
	extends ModelObjectItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource,
		IUpdateableItemText {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessColumnItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addPhysicalColumnPropertyDescriptor(object);
			addTablePropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Physical Column feature.
	 * @generated
	 */
	protected void addPhysicalColumnPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessColumn_physicalColumn_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessColumn_physicalColumn_feature", "_UI_BusinessColumn_type"),
				 BusinessModelPackage.Literals.BUSINESS_COLUMN__PHYSICAL_COLUMN,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Table feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessColumn_table_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessColumn_table_feature", "_UI_BusinessColumn_type"),
				 BusinessModelPackage.Literals.BUSINESS_COLUMN__TABLE,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This returns BusinessColumn.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public Object getImage(Object object) {
		BusinessColumn businessColumn = ((BusinessColumn)object);
		//if the column is a identifier display the appropriate icon
		if (businessColumn.isIdentifier() || businessColumn.isPartOfCompositeIdentifier()){
			return overlayImage(object, getResourceLocator().getImage("full/obj16/BusinessIdentifier"));
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
		String label = ((BusinessColumn)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_BusinessColumn_type") :
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

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}
	
	public Command createCustomCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter) {
		 Command result;
		 
		 result = null;
		 
		 if(commandClass == AddColumnToIdentifierCommand.class) {
			 result = new AddColumnToIdentifierCommand(domain, commandParameter);
		 }
		 else if(commandClass == RemoveColumnFromIdentifierCommand.class) {
			 result = new RemoveColumnFromIdentifierCommand(domain, commandParameter);
		 }
		 
		 return result;
	 }

	@Override
	public void setText(Object object, String text) {
		ModelObject modelObject = (ModelObject)object;
		modelObject.setName(text);
	}

}
