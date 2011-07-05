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



import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.commands.edit.identifier.CreateIdentifierCommand;
import it.eng.spagobi.meta.model.business.commands.edit.relationship.AddBusinessRelationshipCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.ModifyBusinessTableColumnsCommand;
import it.eng.spagobi.meta.model.business.commands.edit.view.AddPhysicalTableToBusinessViewCommand;
import it.eng.spagobi.meta.model.business.commands.edit.view.EditBusinessViewInnerJoinRelationshipsCommand;
import it.eng.spagobi.meta.model.business.commands.edit.view.RemovePhysicalTableFromBusinessViewCommand;
import it.eng.spagobi.meta.model.phantom.provider.BusinessColumnFolderItemProvider;
import it.eng.spagobi.meta.model.phantom.provider.BusinessViewPhysicalTableFolderItemProvider;
import it.eng.spagobi.meta.model.phantom.provider.BusinessViewPhysicalTableItemProvider;
import it.eng.spagobi.meta.model.phantom.provider.FolderItemProvider;
import it.eng.spagobi.meta.model.phantom.provider.InboundRelationshipFolderItemProvider;
import it.eng.spagobi.meta.model.phantom.provider.OutboundRelationshipFolderItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
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

			//addJoinRelationshipsPropertyDescriptor(object);
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
	protected Collection children = null;
	
	@Override
	public Collection<?> getChildren(Object object) {
			BusinessView businessView;
			BusinessColumnFolderItemProvider folderItemProvider;
			InboundRelationshipFolderItemProvider folderItemProviderInRel = null;
			OutboundRelationshipFolderItemProvider folderItemProviderOutRel = null;
			BusinessViewPhysicalTableFolderItemProvider folderItemProviderPhysicalTable;
			List<BusinessRelationship> businessRelationships;
			List<BusinessRelationship> inboundBusinessRelationships = new ArrayList<BusinessRelationship>();
			List<BusinessRelationship> outboundBusinessRelationships = new ArrayList<BusinessRelationship>();
			List<PhysicalTable> physicalTables; 
						
			businessView = (BusinessView)object;
			//group columns
			folderItemProvider = new BusinessColumnFolderItemProvider(adapterFactory, businessView, businessView.getColumns());
			folderItemProvider.setText("Attributes ("+folderItemProvider.getChildrenNumber()+")");
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
			folderItemProviderInRel = new InboundRelationshipFolderItemProvider(adapterFactory, businessView,inboundBusinessRelationships);
			folderItemProviderInRel.setText("Inbound Relationships ("+folderItemProviderInRel.getChildrenNumber()+")");
			if (folderItemProviderInRel.getChildrenNumber() == 0)
				folderItemProviderInRel.setImage("full/obj16/EmptyFolder");
			
			//group outbound relationship	
			folderItemProviderOutRel = new OutboundRelationshipFolderItemProvider(adapterFactory, businessView,outboundBusinessRelationships);
			folderItemProviderOutRel.setText("Outbound Relationships ("+folderItemProviderOutRel.getChildrenNumber()+")");
			if (folderItemProviderOutRel.getChildrenNumber() == 0)
				folderItemProviderOutRel.setImage("full/obj16/EmptyFolder");	
			
			//getting Physical Table
			physicalTables = businessView.getPhysicalTables();
			//create physical table item provider
			Collection<BusinessViewPhysicalTableItemProvider> itemProvidersPhysicalTables = new ArrayList<BusinessViewPhysicalTableItemProvider>();
			for (PhysicalTable physicalTable:physicalTables){
				BusinessViewPhysicalTableItemProvider itemProviderPhysicalTable = new BusinessViewPhysicalTableItemProvider(adapterFactory, businessView,null,physicalTable);
				itemProviderPhysicalTable.setText(physicalTable.getName());
				itemProviderPhysicalTable.setImage("full/obj16/PhysicalTable");
				itemProvidersPhysicalTables.add(itemProviderPhysicalTable);
			}
			//group Physical Tables
			folderItemProviderPhysicalTable = new BusinessViewPhysicalTableFolderItemProvider(adapterFactory, businessView, itemProvidersPhysicalTables);
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
	 */
	@Override
	public String getText(Object object) {
		/*
		String label = ((BusinessView)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_BusinessView_type") :
			getString("_UI_BusinessView_type") + " " + label;
		*/
		String label = ((BusinessView)object).getName();
		return label == null || label.length() == 0 ?
			getString("_UI_BusinessView_type") :
			label;
	}

	/**
	 * This handles model notifications by calling {@link #updateChildren} to update any cached
	 * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	@Override
	public void notifyChanged(Notification notification) {
		
		updateChildren(notification);
		super.notifyChanged(notification);
		/*
		updateChildren(notification);

		switch (notification.getFeatureID(BusinessView.class)) {
			case BusinessModelPackage.BUSINESS_VIEW__JOIN_RELATIONSHIPS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification); 
		*/
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

	public Command createCustomCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter) {
		 Command result;
		 
		 result = null;
		 
		 if(commandClass == ModifyBusinessTableColumnsCommand.class) {
		    	result = new ModifyBusinessTableColumnsCommand(domain, commandParameter);
		    } else if(commandClass == AddBusinessRelationshipCommand.class) {
		    	result = new AddBusinessRelationshipCommand(domain, commandParameter);
		    } else if(commandClass == CreateIdentifierCommand.class) {
		    	result = new CreateIdentifierCommand(domain, commandParameter);
		    } else if(commandClass == AddPhysicalTableToBusinessViewCommand.class) {
		    	result = new AddPhysicalTableToBusinessViewCommand(domain, commandParameter);
		    } else if(commandClass == RemovePhysicalTableFromBusinessViewCommand.class) {
		    	result = new RemovePhysicalTableFromBusinessViewCommand(domain, commandParameter);
		    } else if(commandClass == EditBusinessViewInnerJoinRelationshipsCommand.class) {
		    	result = new EditBusinessViewInnerJoinRelationshipsCommand(domain, commandParameter);
		    }
		 
		 return result;
	}
}
