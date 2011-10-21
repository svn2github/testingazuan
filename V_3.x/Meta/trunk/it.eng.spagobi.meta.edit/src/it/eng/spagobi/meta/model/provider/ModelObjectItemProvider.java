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

import it.eng.spagobi.meta.edit.properties.CustomItemPropertyDescriptor;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.CreateCopyCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.InitializeCopyCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.ModelObject} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ModelObjectItemProvider extends ItemProviderAdapter implements
		IEditingDomainItemProvider, IStructuredItemContentProvider,
		ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource {

	/**
	 * This constructs an instance from a factory and a notifier. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ModelObjectItemProvider(AdapterFactory adapterFactory) {
		super(adapterFactory);
	}

	/**
	 * This returns the property descriptors for the adapted class. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * 
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(object);

			//addIdPropertyDescriptor(object);
			addNamePropertyDescriptor(object);
			addDescriptionPropertyDescriptor(object);
			//Added
			addCustomColumnPropertyDescriptors(object);	
		}
		return itemPropertyDescriptors;
	}

	protected void addCustomColumnPropertyDescriptors(Object object) {
		ModelObject modelObject = (ModelObject) object;
		Set<String> propertyNames = modelObject.getProperties().keySet();
		for (String propertyName : propertyNames) {
			
			ModelProperty property = modelObject.getProperties().get(propertyName);
			ModelPropertyType propertyType = property.getPropertyType();
			
			CustomItemPropertyDescriptor propertyDescriptor = new CustomItemPropertyDescriptor(
					propertyType, 
					((ComposeableAdapterFactory) adapterFactory).getRootAdapterFactory(), 
					getResourceLocator());
			
			String propertyId = propertyType.getId();
			if(propertyId.equals("physical.physicaltable")) {
				propertyDescriptor.setImage( getResourceLocator().getImage("full/obj16/PhysicalTable") );
			} else if (propertyId.equals("structural.visible")){
				propertyDescriptor.setImage( ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE );
			} else {
				propertyDescriptor.setImage( ItemPropertyDescriptor.GENERIC_VALUE_IMAGE );
			}
			
			itemPropertyDescriptors.add(propertyDescriptor);
		}
	}
	

	/**
	 * This adds a property descriptor for the Id feature.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
	protected void addIdPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ModelObject_id_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ModelObject_id_feature", "_UI_ModelObject_type"),
				 ModelPackage.Literals.MODEL_OBJECT__ID,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Name feature.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
	protected void addNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ModelObject_name_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ModelObject_name_feature", "_UI_ModelObject_type"),
				 ModelPackage.Literals.MODEL_OBJECT__NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Unique Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addUniqueNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ModelObject_uniqueName_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ModelObject_uniqueName_feature", "_UI_ModelObject_type"),
				 ModelPackage.Literals.MODEL_OBJECT__UNIQUE_NAME,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Description feature. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected void addDescriptionPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_ModelObject_description_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_ModelObject_description_feature", "_UI_ModelObject_type"),
				 ModelPackage.Literals.MODEL_OBJECT__DESCRIPTION,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean hasChildren(Object object) {
		return hasChildren(object, true);
	}

	/**
	 * This returns ModelObject.gif. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/ModelObject"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((ModelObject)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_ModelObject_type") :
			getString("_UI_ModelObject_type") + " " + label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * @generated
	 */
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(ModelObject.class)) {
			case ModelPackage.MODEL_OBJECT__ID:
			case ModelPackage.MODEL_OBJECT__NAME:
			case ModelPackage.MODEL_OBJECT__UNIQUE_NAME:
			case ModelPackage.MODEL_OBJECT__DESCRIPTION:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
		}
		super.notifyChanged(notification);
	}

	/**
	 * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s
	 * describing the children that can be created under this object. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected void collectNewChildDescriptors(
			Collection<Object> newChildDescriptors, Object object) {
		super.collectNewChildDescriptors(newChildDescriptors, object);
	}

	/**
	 * Return the resource locator for this item provider's resources. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}

	/**
	 * This implements delegated command creation for the given object.
	 */
	@Override
	public Command createCommand(Object object, EditingDomain domain,
			Class<? extends Command> commandClass,
			CommandParameter commandParameter) {
		// Commands should operate on the values, not their wrappers. If the
		// command's values needed to be unwrapped,
		// we'll back get a new CommandParameter.
		//
		CommandParameter oldCommandParameter = commandParameter;
		commandParameter = unwrapCommandValues(commandParameter, commandClass);

		Command result = UnexecutableCommand.INSTANCE;

		if (commandClass == SetCommand.class) {
			result = createSetCommand(
					domain,
					commandParameter.getEOwner(),
					commandParameter.getEStructuralFeature() != null ? commandParameter
							.getEStructuralFeature() : getSetFeature(
							commandParameter.getEOwner(),
							commandParameter.getValue()),
					commandParameter.getValue(), commandParameter.getIndex());
		} else if (commandClass == CopyCommand.class) {
			result = createCopyCommand(domain, commandParameter.getEOwner(),
					(CopyCommand.Helper) commandParameter.getValue());
		} else if (commandClass == CreateCopyCommand.class) {
			result = createCreateCopyCommand(domain,
					commandParameter.getEOwner(),
					(CopyCommand.Helper) commandParameter.getValue());
		} else if (commandClass == InitializeCopyCommand.class) {
			result = createInitializeCopyCommand(domain,
					commandParameter.getEOwner(),
					(CopyCommand.Helper) commandParameter.getValue());
		} else if (commandClass == RemoveCommand.class) {
			if (commandParameter.getEStructuralFeature() != null) {
				result = createRemoveCommand(domain,
						commandParameter.getEOwner(),
						commandParameter.getEStructuralFeature(),
						commandParameter.getCollection());
			} else {
				result = factorRemoveCommand(domain, commandParameter);
			}
		} else if (commandClass == AddCommand.class) {
			if (commandParameter.getEStructuralFeature() != null) {
				result = createAddCommand(domain, commandParameter.getEOwner(),
						commandParameter.getEStructuralFeature(),
						commandParameter.getCollection(),
						commandParameter.getIndex());
			} else {
				result = factorAddCommand(domain, commandParameter);
			}
		} else if (commandClass == MoveCommand.class) {
			if (commandParameter.getEStructuralFeature() != null) {
				result = createMoveCommand(domain,
						commandParameter.getEOwner(),
						commandParameter.getEStructuralFeature(),
						commandParameter.getValue(),
						commandParameter.getIndex());
			} else {
				result = factorMoveCommand(domain, commandParameter);
			}
		} else if (commandClass == ReplaceCommand.class) {
			result = createReplaceCommand(domain, commandParameter.getEOwner(),
					commandParameter.getEStructuralFeature(),
					(EObject) commandParameter.getValue(),
					commandParameter.getCollection());
		} else if (commandClass == DragAndDropCommand.class) {
			DragAndDropCommand.Detail detail = (DragAndDropCommand.Detail) commandParameter
					.getFeature();
			result = createDragAndDropCommand(domain,
					commandParameter.getOwner(), detail.location,
					detail.operations, detail.operation,
					commandParameter.getCollection());
		} else if (commandClass == CreateChildCommand.class) {
			CommandParameter newChildParameter = (CommandParameter) commandParameter
					.getValue();
			result = createCreateChildCommand(domain,
					commandParameter.getEOwner(),
					newChildParameter.getEStructuralFeature(),
					newChildParameter.getValue(), newChildParameter.getIndex(),
					commandParameter.getCollection());
		} else if (ISpagoBIModelCommand.class.isAssignableFrom(commandClass)) {
			result = createCustomCommand(object, domain, commandClass, commandParameter);
		}

		// If necessary, get a command that replaces unwrapped values by their
		// wrappers in the result and affected objects.
		//
		return wrapCommand(result, object, commandClass, commandParameter,oldCommandParameter);
	}

	public Command createCustomCommand(Object object, EditingDomain domain,
			Class<? extends Command> commandClass,
			CommandParameter commandParameter) {
		// ignore custom commond at this level
		return null;
	}

}
