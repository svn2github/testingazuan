package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.domain.EditingDomain;

public class PhysicalRootItemProvider extends FolderItemProvider{

	public PhysicalRootItemProvider(AdapterFactory adapterFactory,
			Object parent, Collection children) {
		super(adapterFactory, parent, children);
	}
	
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) {  
		PhysicalModel physicalModel = (PhysicalModel)parentObject;

	    // Build the collection of new child descriptors.
	    //
	    Collection<Object> newChildDescriptors = new ArrayList<Object>();
	    collectNewChildDescriptors(newChildDescriptors, object);

	   
	    return super.getNewChildDescriptors(physicalModel, editingDomain, sibling);
	}
}
