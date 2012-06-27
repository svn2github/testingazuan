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
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.commands.edit.identifier.AddColumnToIdentifierCommand;
import it.eng.spagobi.meta.model.business.commands.edit.identifier.RemoveColumnFromIdentifierCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.AddCalculatedFieldCommand;
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
	 */
	public BusinessColumnItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class.
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			addTablePropertyDescriptor(object);
			//addPhysicalColumnPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Physical Column feature.
	 */
	/*
	protected void addPhysicalColumnPropertyDescriptor(Object object) {
		IResourceLocator RL = SpagoBIMetaEditPlugin.getInstance().getResourceLocator();
		
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 RL.getString("model.business.column.feature.physicalcolumn.name"),
				 RL.getString("model.property.descriptor", new Object[]{"model.business.column.feature.physicalcolumn.longname", "model.business.column.type"}),
				 BusinessModelPackage.Literals.BUSINESS_COLUMN__PHYSICAL_COLUMN,
				 false,
				 false,
				 true,
				 null,
				 "Physical References",
				 null));
	}
	*/
	

	/**
	 * This adds a property descriptor for the Table feature.
	 */
	protected void addTablePropertyDescriptor(Object object) {
		IResourceLocator RL = SpagoBIMetaEditPlugin.getInstance().getResourceLocator();
		
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 RL.getString("model.business.column.feature.businesstable.name"),
				 RL.getString("model.property.descriptor", new Object[]{"model.business.column.feature.businesstable.longname", "model.business.column.type"}),
				 BusinessModelPackage.Literals.BUSINESS_COLUMN__TABLE,
				 false,
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
		 else if(commandClass == AddCalculatedFieldCommand.class) {
			 result = new AddCalculatedFieldCommand(domain, commandParameter);
		 }
		 
		 return result;
	 }

	@Override
	public void setText(Object object, String text) {
		//ModelObject modelObject = (ModelObject)object;
		//modelObject.setName(text);
		setPropertyValue(object, "name", text);
		
		
	}

}
