/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.provider;


import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessRelationship;

import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.phantom.provider.FolderItemProvider;
import it.eng.spagobi.meta.model.provider.ModelObjectItemProvider;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

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
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.business.BusinessRelationship} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BusinessRelationshipItemProvider
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
	public BusinessRelationshipItemProvider(AdapterFactory adapterFactory) {
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

			addModelPropertyDescriptor(object);
			addSourceTablePropertyDescriptor(object);
			addDestinationTablePropertyDescriptor(object);
			addSourceColumnsPropertyDescriptor(object);
			addDestinationColumnsPropertyDescriptor(object);
			addPhysicalForeignKeyPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
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
				 getString("_UI_BusinessRelationship_model_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessRelationship_model_feature", "_UI_BusinessRelationship_type"),
				 BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__MODEL,
				 true,
				 false,
				 true,
				 null,
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
				 getString("_UI_BusinessRelationship_sourceTable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessRelationship_sourceTable_feature", "_UI_BusinessRelationship_type"),
				 BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__SOURCE_TABLE,
				 true,
				 false,
				 true,
				 null,
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
				 getString("_UI_BusinessRelationship_destinationTable_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessRelationship_destinationTable_feature", "_UI_BusinessRelationship_type"),
				 BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__DESTINATION_TABLE,
				 true,
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
				 getString("_UI_BusinessRelationship_sourceColumns_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessRelationship_sourceColumns_feature", "_UI_BusinessRelationship_type"),
				 BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS,
				 true,
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
				 getString("_UI_BusinessRelationship_destinationColumns_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessRelationship_destinationColumns_feature", "_UI_BusinessRelationship_type"),
				 BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Physical Foreign Key feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPhysicalForeignKeyPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessRelationship_physicalForeignKey_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessRelationship_physicalForeignKey_feature", "_UI_BusinessRelationship_type"),
				 BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__PHYSICAL_FOREIGN_KEY,
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
			childrenFeatures.add(BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS);
			childrenFeatures.add(BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS);
		}
		return childrenFeatures;
	}
	
	@Override
	public Collection<?> getChildren(Object object) {
		BusinessRelationship businessRelationship;
		FolderItemProvider relationshipItemProvider;
		EList<BusinessColumn> sourceColumns, destinationColumns;
		String sourceColumnsNames = "";
		String destinationColumnsNames = "";
		Collection children;
		
		businessRelationship = (BusinessRelationship)object;
		//getting relationship's sourceColumns
		sourceColumns = businessRelationship.getSourceColumns();
		destinationColumns = businessRelationship.getDestinationColumns();
		children = new LinkedHashSet();
		
		for(int i=0; i<sourceColumns.size(); i++){
			String sourceColumnName = sourceColumns.get(i).getName();
			String destinationColumn = destinationColumns.get(i).getName();
			
			relationshipItemProvider = new FolderItemProvider(adapterFactory, businessRelationship,null);
			relationshipItemProvider.setImage("full/obj16/Arrow");
			relationshipItemProvider.setText("("+businessRelationship.getSourceTable().getName()+") "+sourceColumnName+" -> ("+businessRelationship.getDestinationTable().getName()+") "+destinationColumn);
			children.add( relationshipItemProvider );
		}
		
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
	 * This returns BusinessRelationship.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/BusinessRelationship"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 */
	@Override
	public String getText(Object object) {
		/*
		String label = ((BusinessRelationship)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_BusinessRelationship_type") :
			getString("_UI_BusinessRelationship_type") + " " + label;
		*/	
		String label = ((BusinessRelationship)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_BusinessRelationship_type") :
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
		
		//*************
		if (notification.getNotifier() instanceof BusinessRelationship){
			BusinessRelationship businessRelationship = ((BusinessRelationship)notification.getNotifier());

			//check if the business relationship has null source or destination table
			if ( ( ((BusinessRelationship)notification.getNotifier()).getDestinationTable() == null ) || ( ((BusinessRelationship)notification.getNotifier()).getSourceTable() == null ) )
				System.out.println("RIFERIMENTO PENDENTE in "+notification.getNotifier());
				BusinessModel businessModel = ((BusinessRelationship)notification.getNotifier()).getModel();
				if ( (businessModel != null) && (businessModel.getRelationships().contains(businessRelationship)) ){
					businessModel.getRelationships().remove(businessRelationship);
					System.out.println("REMOVED RELATIONSHIP "+businessRelationship);
				}
		}
		//*************
		switch (notification.getFeatureID(BusinessRelationship.class)) {
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS:
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS:
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
		return SpagoBIMetalModelEditPlugin.INSTANCE;
	}

}
