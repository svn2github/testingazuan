/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class BusinessViewPhysicalTableItemProvider extends FolderItemProvider {

	/**
	 * @param adapterFactory
	 * @param parent
	 * @param children
	 */
	PhysicalTable physicalTable;
	
	public BusinessViewPhysicalTableItemProvider(AdapterFactory adapterFactory,
			Object parent, Collection children, PhysicalTable physicalTable) {
		super(adapterFactory, parent, children);
		this.physicalTable = physicalTable;

	}
	
	/**
	 * @return the physicalTable
	 */
	public PhysicalTable getPhysicalTable() {
		return physicalTable;
	}

	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) { 
		BusinessView businessView = (BusinessView)parentObject;

	    // Build the collection of new child descriptors.
	    //
	    Collection<Object> newChildDescriptors = new ArrayList<Object>();
	    collectNewChildDescriptors(newChildDescriptors, object);

	   
	    return super.getNewChildDescriptors(businessView, editingDomain, sibling);
	}

}
