/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.provider;


import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModelPackage;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import it.eng.spagobi.meta.model.provider.ModelObjectItemProvider;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.physical.PhysicalTable} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PhysicalTableItemProvider
	extends ModelObjectItemProvider
	implements
		IEditingDomainItemProvider,
		IStructuredItemContentProvider,
		ITreeItemContentProvider,
		IItemLabelProvider,
		IItemPropertySource {
	/**
	 * This constructs an instance from a factory and a notifier.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTableItemProvider(AdapterFactory adapterFactory) {
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

			addCommentPropertyDescriptor(object);
			addTypePropertyDescriptor(object);
			addColumnsPropertyDescriptor(object);
			addPrimaryKeyPropertyDescriptor(object);
			addForeignKeysPropertyDescriptor(object);
			addReverseForeignKeysPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Comment feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addCommentPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalTable_comment_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalTable_comment_feature", "_UI_PhysicalTable_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_TABLE__COMMENT,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Type feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTypePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalTable_type_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalTable_type_feature", "_UI_PhysicalTable_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_TABLE__TYPE,
				 true,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Columns feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addColumnsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalTable_columns_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalTable_columns_feature", "_UI_PhysicalTable_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_TABLE__COLUMNS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Primary Key feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPrimaryKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalTable_primaryKey_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalTable_primaryKey_feature", "_UI_PhysicalTable_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_TABLE__PRIMARY_KEY,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Foreign Keys feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addForeignKeysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalTable_foreignKeys_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalTable_foreignKeys_feature", "_UI_PhysicalTable_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_TABLE__FOREIGN_KEYS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Reverse Foreign Keys feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addReverseForeignKeysPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalTable_reverseForeignKeys_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalTable_reverseForeignKeys_feature", "_UI_PhysicalTable_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
	 * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
	 * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_TABLE__COLUMNS);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_TABLE__PRIMARY_KEY);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_TABLE__FOREIGN_KEYS);
		}
		return childrenFeatures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EStructuralFeature getChildFeature(Object object, Object child) {
		// Check the type of the specified child object and return the proper feature to use for
		// adding (see {@link AddCommand}) it as a child.

		return super.getChildFeature(object, child);
	}

	/**
	 * This returns PhysicalTable.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/PhysicalTable"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	@Override
	public String getText(Object object) {
		/*
		String label = ((PhysicalTable)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_PhysicalTable_type") :
			getString("_UI_PhysicalTable_type") + " " + label;
		*/
		String label = ((PhysicalTable)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_PhysicalTable_type") :
			label;
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

		switch (notification.getFeatureID(PhysicalTable.class)) {
			case PhysicalModelPackage.PHYSICAL_TABLE__COMMENT:
			case PhysicalModelPackage.PHYSICAL_TABLE__TYPE:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS:
			case PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY:
			case PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
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

		newChildDescriptors.add
			(createChildParameter
				(PhysicalModelPackage.Literals.PHYSICAL_TABLE__COLUMNS,
				 PhysicalModelFactory.eINSTANCE.createPhysicalColumn()));

		newChildDescriptors.add
			(createChildParameter
				(PhysicalModelPackage.Literals.PHYSICAL_TABLE__PRIMARY_KEY,
				 PhysicalModelFactory.eINSTANCE.createPhysicalPrimaryKey()));

		newChildDescriptors.add
			(createChildParameter
				(PhysicalModelPackage.Literals.PHYSICAL_TABLE__FOREIGN_KEYS,
				 PhysicalModelFactory.eINSTANCE.createPhysicalForeignKey()));
	}

	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return SpagoBIMetalModelEditPlugin.INSTANCE;
	}

}
