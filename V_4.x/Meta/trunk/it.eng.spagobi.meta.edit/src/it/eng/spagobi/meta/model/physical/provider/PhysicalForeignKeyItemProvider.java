/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.physical.provider;


import it.eng.spagobi.meta.model.phantom.provider.FolderItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModelPackage;
import it.eng.spagobi.meta.model.provider.ModelObjectItemProvider;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
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
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class PhysicalForeignKeyItemProvider
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
	public PhysicalForeignKeyItemProvider(AdapterFactory adapterFactory) {
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

			addSourceTablePropertyDescriptor(object);
			addSourceColumnsPropertyDescriptor(object);
			addSourceNamePropertyDescriptor(object);
			addDestinationNamePropertyDescriptor(object);
			addDestinationTablePropertyDescriptor(object);
			addDestinationColumnsPropertyDescriptor(object);
			addModelPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Source Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourceNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalForeignKey_sourceName_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalForeignKey_sourceName_feature", "_UI_PhysicalForeignKey_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__SOURCE_NAME,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Source Table feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourceTablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalForeignKey_sourceTable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalForeignKey_sourceTable_feature", "_UI_PhysicalForeignKey_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__SOURCE_TABLE,
				 false,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Source Columns feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addSourceColumnsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalForeignKey_sourceColumns_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalForeignKey_sourceColumns_feature", "_UI_PhysicalForeignKey_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__SOURCE_COLUMNS,
				 false,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Destination Name feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDestinationNamePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalForeignKey_destinationName_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalForeignKey_destinationName_feature", "_UI_PhysicalForeignKey_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__DESTINATION_NAME,
				 false,
				 false,
				 false,
				 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Destination Table feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDestinationTablePropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalForeignKey_destinationTable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalForeignKey_destinationTable_feature", "_UI_PhysicalForeignKey_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__DESTINATION_TABLE,
				 false,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Destination Columns feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addDestinationColumnsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalForeignKey_destinationColumns_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalForeignKey_destinationColumns_feature", "_UI_PhysicalForeignKey_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__DESTINATION_COLUMNS,
				 false,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_PhysicalForeignKey_model_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_PhysicalForeignKey_model_feature", "_UI_PhysicalForeignKey_type"),
				 PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__MODEL,
				 false,
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
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__SOURCE_COLUMNS);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__DESTINATION_NAME);
			childrenFeatures.add(PhysicalModelPackage.Literals.PHYSICAL_FOREIGN_KEY__DESTINATION_COLUMNS);
		}
		return childrenFeatures;
	}
	
	@Override
	public Collection<?> getChildren(Object object) {
		PhysicalForeignKey foreignKey;
		FolderItemProvider foreignKeyItemProvider;
		EList<PhysicalColumn> sourceColumns, destinationColumns;
		String sourceColumnsNames = "";
		String destinationColumnsNames = "";
		Collection children;
		
		foreignKey = (PhysicalForeignKey)object;
		//getting fk's sourceColumns
		sourceColumns = foreignKey.getSourceColumns();
		for (PhysicalColumn column : sourceColumns){
			sourceColumnsNames = sourceColumnsNames+" "+column.getName();
		}
		//getting fk's destinationColumns
		destinationColumns = foreignKey.getDestinationColumns();
		for (PhysicalColumn column : destinationColumns){
			destinationColumnsNames = destinationColumnsNames+" "+column.getName();
		}
		
		//group fk
		foreignKeyItemProvider = new FolderItemProvider(adapterFactory, foreignKey, null);
		foreignKeyItemProvider.setImage("full/obj16/Arrow");
		foreignKeyItemProvider.setText("("+foreignKey.getSourceTable().getName()+") "+sourceColumnsNames+" -> ("+foreignKey.getDestinationTable().getName()+") "+destinationColumnsNames);
		
		children = new LinkedHashSet();
		//children.addAll(  getChildrenFeatures(object) );
		children.add( foreignKeyItemProvider );
		
		return children;
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
	 * This returns PhysicalForeignKey.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/PhysicalForeignKey"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public String getText(Object object) {
		/*
		String label = ((PhysicalForeignKey)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_PhysicalForeignKey_type") :
			getString("_UI_PhysicalForeignKey_type") + " " + label;
		*/
		String label = ((PhysicalForeignKey)object).getSourceName();
		return label == null || label.length() == 0 ?
			getString("_UI_PhysicalForeignKey_type") :
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

		switch (notification.getFeatureID(PhysicalForeignKey.class)) {
			case PhysicalModelPackage.PHYSICAL_FOREIGN_KEY__SOURCE_COLUMNS:
			case PhysicalModelPackage.PHYSICAL_FOREIGN_KEY__DESTINATION_NAME:
			case PhysicalModelPackage.PHYSICAL_FOREIGN_KEY__DESTINATION_COLUMNS:
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

}
