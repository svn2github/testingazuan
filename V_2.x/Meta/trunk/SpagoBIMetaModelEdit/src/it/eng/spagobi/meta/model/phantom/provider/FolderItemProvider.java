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

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.eclipse.emf.edit.provider.ItemProviderAdapter;

import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * 
 */
public class FolderItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider,
		IStructuredItemContentProvider, ITreeItemContentProvider,
		IItemLabelProvider, IItemPropertySource  {

	
	private Object parent;
	private Collection children;
	private String text;
	private String image; 
	
	private static final String DEFAULT_TEXT = "Grouping Folder";
	private static final String DEFAULT_IMAGE = "full/obj16/BusinessTable";
	

	public FolderItemProvider(AdapterFactory adapterFactory, Object parent, Collection children) {
		super(adapterFactory);
		this.parent = parent;
		this.children = children;
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
		return parent;
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
