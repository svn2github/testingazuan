package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.AddBusinessRelationshipCommand;
import it.eng.spagobi.meta.model.business.commands.AddBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.AddIdentifierCommand;
import it.eng.spagobi.meta.model.business.commands.EditBusinessColumnsCommand;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

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
		 }
		 
		 return result;
	}
}
