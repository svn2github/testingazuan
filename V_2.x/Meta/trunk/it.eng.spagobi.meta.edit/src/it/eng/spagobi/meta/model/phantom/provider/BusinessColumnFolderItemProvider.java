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
package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.ViewerNotification;

/**
 * @author cortella
 *
 */
public class BusinessColumnFolderItemProvider extends FolderItemProvider {

	/**
	 * @param adapterFactory
	 * @param parent
	 * @param children
	 */
	public BusinessColumnFolderItemProvider(AdapterFactory adapterFactory,
			Object parent, Collection children) {
		super(adapterFactory, parent, children);
		//adding this custom Item Provider to the adapters of BusinessColumnSet to forward the notify
		((BusinessColumnSet)parent).eAdapters().add(this);
		//adding this custom Item Provider to the adapters of BusinessModel to forward the notify
		((BusinessColumnSet)parent).getModel().eAdapters().add(this);

		
	}
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) { 
		BusinessColumnSet businessTable = (BusinessColumnSet)parentObject;

	    // Build the collection of new child descriptors.
	    //
	    Collection<Object> newChildDescriptors = new ArrayList<Object>();
	    collectNewChildDescriptors(newChildDescriptors, object);

	   
	    return super.getNewChildDescriptors(businessTable, editingDomain, sibling);
	}
	
	public void notifyChanged(Notification notification) {
		updateChildren(notification);

		switch (notification.getFeatureID(BusinessColumnSet.class)) {
		case BusinessModelPackage.BUSINESS_COLUMN_SET__COLUMNS:
				fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
				return;
		}
		switch (notification.getFeatureID(BusinessModel.class)) {
		case BusinessModelPackage.BUSINESS_MODEL__IDENTIFIERS:
			BusinessColumnFolderItemProvider columnFolder = this;
			BusinessTable parentTable = (BusinessTable) columnFolder.getParentObject();	
			fireNotifyChanged(new ViewerNotification(notification, parentTable, true, false));
			return;
		}
		super.notifyChanged(notification);
	}
}
