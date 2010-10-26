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

public class BusinessRootItemProvider extends FolderItemProvider{

	public BusinessRootItemProvider(AdapterFactory adapterFactory,
			Object parent, Collection children) {
		super(adapterFactory, parent, children);
	}
}
