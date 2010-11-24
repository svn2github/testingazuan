/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.provider;


import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessView;

import it.eng.spagobi.meta.model.phantom.provider.FolderItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.provider.ModelObjectItemProvider;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.ResourceLocator;

import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link it.eng.spagobi.meta.model.business.BusinessView} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class BusinessViewItemProvider
	extends BusinessColumnSetItemProvider
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
	public BusinessViewItemProvider(AdapterFactory adapterFactory) {
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

			addJoinRelationshipsPropertyDescriptor(object);
		}
		return itemPropertyDescriptors;
	}

	/**
	 * This adds a property descriptor for the Join Relationships feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addJoinRelationshipsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessView_joinRelationships_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessView_joinRelationships_feature", "_UI_BusinessView_type"),
				 BusinessModelPackage.Literals.BUSINESS_VIEW__JOIN_RELATIONSHIPS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

@Override
	public Collection<?> getChildren(Object object) {
		BusinessView businessView;
		FolderItemProvider folderItemProvider;
		FolderItemProvider folderItemProviderInRel = null;
		FolderItemProvider folderItemProviderOutRel = null;
		FolderItemProvider folderItemProviderPhysicalTable;
		List<BusinessRelationship> businessRelationships;
		List<BusinessRelationship> inboundBusinessRelationships = new ArrayList<BusinessRelationship>();
		List<BusinessRelationship> outboundBusinessRelationships = new ArrayList<BusinessRelationship>();
		List<PhysicalTable> physicalTables; 
		Collection children;
		
		businessView = (BusinessView)object;
		//group columns
		folderItemProvider = new FolderItemProvider(adapterFactory, businessView, businessView.getColumns());
		folderItemProvider.setText("Columns ("+folderItemProvider.getChildrenNumber()+")");
		if (folderItemProvider.getChildrenNumber() == 0)
			folderItemProvider.setImage("full/obj16/EmptyFolder");
		//getting inbound and outbound relationships
		businessRelationships = businessView.getRelationships();
		
		for( BusinessRelationship relationship : businessRelationships){
			if (relationship.getDestinationTable() == businessView){
				inboundBusinessRelationships.add(relationship);
			}
			else if (relationship.getSourceTable() == businessView){
				outboundBusinessRelationships.add(relationship);
			}
		}
		//group inbound relationship	
		folderItemProviderInRel = new FolderItemProvider(adapterFactory, businessView,inboundBusinessRelationships);
		folderItemProviderInRel.setText("Inbound Relationships ("+folderItemProviderInRel.getChildrenNumber()+")");
		if (folderItemProviderInRel.getChildrenNumber() == 0)
			folderItemProviderInRel.setImage("full/obj16/EmptyFolder");
		
		//group outbound relationship	
		folderItemProviderOutRel = new FolderItemProvider(adapterFactory, businessView,outboundBusinessRelationships);
		folderItemProviderOutRel.setText("Outbound Relationships ("+folderItemProviderOutRel.getChildrenNumber()+")");
		if (folderItemProviderOutRel.getChildrenNumber() == 0)
			folderItemProviderOutRel.setImage("full/obj16/EmptyFolder");	
		
		//group Physical Table
		physicalTables = businessView.getPhysicalTables();
		folderItemProviderPhysicalTable = new FolderItemProvider(adapterFactory, businessView, physicalTables);
		folderItemProviderPhysicalTable.setText("Physical Tables ("+folderItemProviderPhysicalTable.getChildrenNumber()+")");
		if (folderItemProviderPhysicalTable.getChildrenNumber() == 0)
			folderItemProviderPhysicalTable.setImage("full/obj16/EmptyFolder");	
		
		children = new LinkedHashSet();
		children.add( folderItemProvider );
		children.add( folderItemProviderInRel );
		children.add( folderItemProviderOutRel );
		children.add( folderItemProviderPhysicalTable );
		return children;
	}
	/**
	 * This returns BusinessView.gif.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage("full/obj16/BusinessView"));
	}

	/**
	 * This returns the label text for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getText(Object object) {
		String label = ((BusinessView)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_BusinessView_type") :
			getString("_UI_BusinessView_type") + " " + label;
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
