/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessView;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * @author cortella
 *
 */
public class BusinessViewPhysicalTableFolderItemProvider extends
		FolderItemProvider {

	/**
	 * @param adapterFactory
	 * @param parent
	 * @param children
	 */
	public BusinessViewPhysicalTableFolderItemProvider(
			AdapterFactory adapterFactory, Object parent, Collection children) {
		super(adapterFactory, parent, children);
		//adding this custom Item Provider to the adapters of BusinessModel to forward the notify
		//((BusinessView)parent).getModel().eAdapters().add(this);
		
		//adding this custom Item Provider to the adapters of BusinessView to forward the notify
		((BusinessView)parent).eAdapters().add(this);
	}

	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) { 
		BusinessView businessView = (BusinessView)parentObject;

	    // Build the collection of new child descriptors.
	    //
	    Collection<Object> newChildDescriptors = new ArrayList<Object>();
	    collectNewChildDescriptors(newChildDescriptors, object);

	   
	    return super.getNewChildDescriptors(businessView, editingDomain, sibling);
	}
	
	@Override
	public Collection<? extends EStructuralFeature> getChildrenFeatures(Object object) {
		if (childrenFeatures == null) {
			super.getChildrenFeatures(object);
			childrenFeatures.add(BusinessModelPackage.Literals.BUSINESS_MODEL__JOIN_RELATIONSHIPS);
		}
		return childrenFeatures;
	}
	
	
	public void notifyChanged(Notification notification) {
		updateChildren(notification);
		
		switch (notification.getFeatureID(BusinessView.class)) {
		case BusinessModelPackage.BUSINESS_VIEW__JOIN_RELATIONSHIPS:
			fireNotifyChanged(new ViewerNotification(notification, parentObject, true, false));
			return;
		}
		super.notifyChanged(notification);
	}
}
