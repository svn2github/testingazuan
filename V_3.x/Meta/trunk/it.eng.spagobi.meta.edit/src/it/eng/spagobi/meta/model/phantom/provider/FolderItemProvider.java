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

import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.UnexecutableCommand;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.CopyCommand;
import org.eclipse.emf.edit.command.CreateChildCommand;
import org.eclipse.emf.edit.command.CreateCopyCommand;
import org.eclipse.emf.edit.command.DragAndDropCommand;
import org.eclipse.emf.edit.command.InitializeCopyCommand;
import org.eclipse.emf.edit.command.MoveCommand;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.ReplaceCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * 
 */
public class FolderItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider,
		IStructuredItemContentProvider, ITreeItemContentProvider,
		IItemLabelProvider, IItemPropertySource  {

	
	protected Object parentObject;

	protected Collection children;
	private String text;
	private String image; 
	
	private static final String DEFAULT_TEXT = "Grouping Folder";
	private static final String DEFAULT_IMAGE = "full/obj16/Folder";
	

	public FolderItemProvider(AdapterFactory adapterFactory, Object parent, Collection children) {
		super(adapterFactory);
		this.parentObject = parent;
		this.children = children != null ? children: new ArrayList();
		this.text= DEFAULT_TEXT;
		this.image = DEFAULT_IMAGE;
	}
	
	
	@Override
	public Object getImage(Object object) {
		return overlayImage(object, getResourceLocator().getImage(image));
	}
	
	public void setImage(String image) {
		this.image = image;
	}

	@Override
	public String getText(Object object) {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}


	@Override
	public boolean hasChildren(Object object) {
		return children.size() > 0;
	}

	@Override
	public Collection<?> getChildren(Object object) {
		return children;
		
	}

	@Override
	public Object getParent(Object object) {
		return parentObject;
	}
	
	public int getChildrenNumber(){
		return children.size();
	}
	
	/**
	 * Return the resource locator for this item provider's resources.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 */
	@Override
	public ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}
	
	
	
	
	
	/**
	   * This implements {@link IEditingDomainItemProvider#getNewChildDescriptors
	   * IEditingDomainItemProvider.getNewChildDescriptors}, returning descriptors for all the possible children that
	   * can be added to the specified <code>object</code>. Usually, these descriptors will be instances of
	   * {@link org.eclipse.emf.edit.command.CommandParameter}s, containing at least the child object and the feature
	   * under which it should be added.
	   * 
	   * <p>This implementation invokes {@link #collectNewChildDescriptors collectNewChildDescriptors}, which should be
	   * overridden by derived classes, to build this collection.
	   *
	   * <p>If <code>sibling</code> is non-null, an index is added to each <code>CommandParameter</code> with a multi-valued
	   * feature, to ensure that the new child object gets added in the right position.
	   */
	  public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling)
	  {  
		Collection newChildDescriptors;
		if(object instanceof EObject){
			
			newChildDescriptors =  super.getNewChildDescriptors(object, editingDomain, sibling);
		} else {
			newChildDescriptors = new ArrayList<Object>();
			collectNewChildDescriptors(newChildDescriptors, object);
			newChildDescriptors.addAll( super.getNewChildDescriptors(((FolderItemProvider)object).getParentObject(), editingDomain, sibling) );
		}
	   
	    return newChildDescriptors;
	  }
	  
	  /**
	   * This is a temporary way to get the structural features that contribute children. It first calls the new
	   * {link #getChildrenFeatues getChildrenFeatures} method and then, if the result is empty, tries the deprecated
	   * {@link #getChildrenReferences getChildrenReferences} method. It is used, instead of just the new method,
	   * throughout this class.
	   */
	  private Collection<? extends EStructuralFeature> getAnyChildrenFeatures(Object object)
	  {
	    Collection<? extends EStructuralFeature> result = getChildrenFeatures(object);
	    return result.isEmpty() ? getChildrenReferences(object) : result;
	  }
	  
	  public Object getParentObject() {
		return parentObject;
	  }


	  public void setParentObject(Object parentObject) {
		this.parentObject = parentObject;
	  }
	  
		/**
		 * This implements delegated command creation for the given object.
		 */
		@Override
		public Command createCommand(Object object, EditingDomain domain,
				Class<? extends Command> commandClass,
				CommandParameter commandParameter) {
			// Commands should operate on the values, not their wrappers. If the
			// command's values needed to be unwrapped,
			// we'll back get a new CommandParameter.
			//
			CommandParameter oldCommandParameter = commandParameter;
			commandParameter = unwrapCommandValues(commandParameter, commandClass);

			Command result = UnexecutableCommand.INSTANCE;

			if (commandClass == SetCommand.class) {
				result = createSetCommand(
						domain,
						commandParameter.getEOwner(),
						commandParameter.getEStructuralFeature() != null ? commandParameter
								.getEStructuralFeature() : getSetFeature(
								commandParameter.getEOwner(),
								commandParameter.getValue()),
						commandParameter.getValue(), commandParameter.getIndex());
			} else if (commandClass == CopyCommand.class) {
				result = createCopyCommand(domain, commandParameter.getEOwner(),
						(CopyCommand.Helper) commandParameter.getValue());
			} else if (commandClass == CreateCopyCommand.class) {
				result = createCreateCopyCommand(domain,
						commandParameter.getEOwner(),
						(CopyCommand.Helper) commandParameter.getValue());
			} else if (commandClass == InitializeCopyCommand.class) {
				result = createInitializeCopyCommand(domain,
						commandParameter.getEOwner(),
						(CopyCommand.Helper) commandParameter.getValue());
			} else if (commandClass == RemoveCommand.class) {
				if (commandParameter.getEStructuralFeature() != null) {
					result = createRemoveCommand(domain,
							commandParameter.getEOwner(),
							commandParameter.getEStructuralFeature(),
							commandParameter.getCollection());
				} else {
					result = factorRemoveCommand(domain, commandParameter);
				}
			} else if (commandClass == AddCommand.class) {
				if (commandParameter.getEStructuralFeature() != null) {
					result = createAddCommand(domain, commandParameter.getEOwner(),
							commandParameter.getEStructuralFeature(),
							commandParameter.getCollection(),
							commandParameter.getIndex());
				} else {
					result = factorAddCommand(domain, commandParameter);
				}
			} else if (commandClass == MoveCommand.class) {
				if (commandParameter.getEStructuralFeature() != null) {
					result = createMoveCommand(domain,
							commandParameter.getEOwner(),
							commandParameter.getEStructuralFeature(),
							commandParameter.getValue(),
							commandParameter.getIndex());
				} else {
					result = factorMoveCommand(domain, commandParameter);
				}
			} else if (commandClass == ReplaceCommand.class) {
				result = createReplaceCommand(domain, commandParameter.getEOwner(),
						commandParameter.getEStructuralFeature(),
						(EObject) commandParameter.getValue(),
						commandParameter.getCollection());
			} else if (commandClass == DragAndDropCommand.class) {
				DragAndDropCommand.Detail detail = (DragAndDropCommand.Detail) commandParameter
						.getFeature();
				result = createDragAndDropCommand(domain,
						commandParameter.getOwner(), detail.location,
						detail.operations, detail.operation,
						commandParameter.getCollection());
			} else if (commandClass == CreateChildCommand.class) {
				CommandParameter newChildParameter = (CommandParameter) commandParameter
						.getValue();
				result = createCreateChildCommand(domain,
						commandParameter.getEOwner(),
						newChildParameter.getEStructuralFeature(),
						newChildParameter.getValue(), newChildParameter.getIndex(),
						commandParameter.getCollection());
			} else if (ISpagoBIModelCommand.class.isAssignableFrom(commandClass)) {
				result = createCustomCommand(object, domain, commandClass, commandParameter);
			}

			// If necessary, get a command that replaces unwrapped values by their
			// wrappers in the result and affected objects.
			//
			return wrapCommand(result, object, commandClass, commandParameter,oldCommandParameter);
		}
		
		public Command createCustomCommand(Object object, EditingDomain domain,
				Class<? extends Command> commandClass,
				CommandParameter commandParameter) {
			// ignore custom commond at this level
			return null;
		}

}
