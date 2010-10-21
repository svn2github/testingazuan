package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.provider.SpagoBIMetalModelEditPlugin;

import java.util.Collection;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

import org.eclipse.emf.edit.provider.ItemProviderAdapter;

public class RootItemProvider extends ItemProviderAdapter implements IEditingDomainItemProvider, IStructuredItemContentProvider,
ITreeItemContentProvider, IItemLabelProvider, IItemPropertySource{

	private Object parent;
	private Collection children;
	private String text;
	private String image; 
	
	private static final String DEFAULT_TEXT = "Business Model Tree Root";
	private static final String DEFAULT_IMAGE = "full/obj16/BusinessModel";
	
	public RootItemProvider(AdapterFactory adapterFactory, Object parent, Collection children) {
		super(adapterFactory);
		this.parent = parent;
		this.children = children;
		this.text = DEFAULT_TEXT;
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
