package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.commands.AddBusinessRelationshipCommand;
import it.eng.spagobi.meta.model.business.commands.AddBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.AddIdentifierCommand;
import it.eng.spagobi.meta.model.business.commands.EditBusinessColumnsCommand;
import it.eng.spagobi.meta.model.business.commands.GenerateJPAMappingCommand;
import it.eng.spagobi.meta.model.provider.CustomItemPropertyDescriptor;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ViewerNotification;

import org.eclipse.emf.edit.provider.ItemProviderAdapter;

public class BusinessRootItemProvider extends FolderItemProvider {

	public BusinessRootItemProvider(AdapterFactory adapterFactory,
			Object parent, Collection children) {
		super(adapterFactory, parent, children);
	}
	
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) { 
		BusinessModel busienssModel = (BusinessModel)parentObject;

	    // Build the collection of new child descriptors.
	    //
	    Collection<Object> newChildDescriptors = new ArrayList<Object>();
	    collectNewChildDescriptors(newChildDescriptors, object);

	   
	    return super.getNewChildDescriptors(busienssModel, editingDomain, sibling);
	}
	
	public Command createCustomCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter) {
		 Command result;
		 
		 result = null;
		 
		 if(commandClass == AddBusinessTableCommand.class) {
		   	System.err.println(">>> " + commandClass.getName() + " <<<");
		   	result = new AddBusinessTableCommand(domain, commandParameter);
		 } else if(commandClass == AddBusinessRelationshipCommand.class) {
			System.err.println(">>> " + commandClass.getName() + " <<<");
			result = new AddBusinessRelationshipCommand(domain, commandParameter);
		 } else if(commandClass == GenerateJPAMappingCommand.class) {
		    System.err.println(">>> " + commandClass.getName() + " <<<");
			result = new GenerateJPAMappingCommand(domain, commandParameter);
		 }
		 
		 return result;
	}
	
	//---------
	
	@Override
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(BusinessModel.class)) {
			case BusinessModelPackage.BUSINESS_MODEL__TABLES:
			case BusinessModelPackage.BUSINESS_MODEL__DOMAINS:
			case BusinessModelPackage.BUSINESS_MODEL__JOIN_RELATIONSHIPS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		super.notifyChanged(notification);
	}
	
	
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(BusinessModelPackage.Literals.BUSINESS_MODEL__TABLES);
		}
		return childrenFeatures;
	}
	
	/**
	 * This returns the property descriptors for the adapted class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
		
		
		BusinessRootItemProvider businessRootItemProvider = (BusinessRootItemProvider)object;
		Object o = businessRootItemProvider.getParentObject();
		System.err.println("----- PPP: " + o.getClass().getName());
		
		if (itemPropertyDescriptors == null) {
			super.getPropertyDescriptors(o);

			//addParentModelPropertyDescriptor(o);
			//addPhysicalModelPropertyDescriptor(o);
			//addTablesPropertyDescriptor(o);
			//addRelationshipsPropertyDescriptor(o);
			
			addCustomColumnPropertyDescriptors(o);
		}
		return itemPropertyDescriptors;
	}
	
	protected void addCustomColumnPropertyDescriptors(Object object) {
		System.err.println("debug1");
		ModelObject modelObject = (ModelObject) object;
		Iterator<String> it = modelObject.getProperties().keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.err.println("property: " + key);
			System.err.println("adapterFactory: " + adapterFactory.getClass().getName());
			
			ModelProperty property = modelObject.getProperties().get(key);
			itemPropertyDescriptors.add(new CustomItemPropertyDescriptor(
					property, ((ComposeableAdapterFactory) adapterFactory)
							.getRootAdapterFactory(), getResourceLocator()));
		}
		System.err.println("debug2");
	}

	/**
	 * This adds a property descriptor for the Parent Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addParentModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessModel_parentModel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessModel_parentModel_feature", "_UI_BusinessModel_type"),
				 BusinessModelPackage.Literals.BUSINESS_MODEL__PARENT_MODEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Physical Model feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addPhysicalModelPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessModel_physicalModel_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessModel_physicalModel_feature", "_UI_BusinessModel_type"),
				 BusinessModelPackage.Literals.BUSINESS_MODEL__PHYSICAL_MODEL,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Tables feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addTablesPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessModel_tables_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessModel_tables_feature", "_UI_BusinessModel_type"),
				 BusinessModelPackage.Literals.BUSINESS_MODEL__TABLES,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}

	/**
	 * This adds a property descriptor for the Relationships feature.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void addRelationshipsPropertyDescriptor(Object object) {
		itemPropertyDescriptors.add
			(createItemPropertyDescriptor
				(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
				 getResourceLocator(),
				 getString("_UI_BusinessModel_relationships_feature"),
				 getString("_UI_PropertyDescriptor_description", "_UI_BusinessModel_relationships_feature", "_UI_BusinessModel_type"),
				 BusinessModelPackage.Literals.BUSINESS_MODEL__RELATIONSHIPS,
				 true,
				 false,
				 true,
				 null,
				 null,
				 null));
	}
	
}
