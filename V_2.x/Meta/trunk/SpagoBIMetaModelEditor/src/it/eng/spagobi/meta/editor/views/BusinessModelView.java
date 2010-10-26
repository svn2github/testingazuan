package it.eng.spagobi.meta.editor.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.dnd.TableDropListener;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.wizards.AddBCWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessColumnWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessIdentifierWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessRelationshipWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.provider.BusinessModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.business.util.BusinessModelAdapterFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.emf.edit.ui.provider.PropertyDescriptor;
import org.eclipse.emf.edit.ui.provider.PropertySource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertySheetPage;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;



public class BusinessModelView extends ViewPart implements ISelectionChangedListener ,IEditingDomainProvider, IAdaptable {
	
	private ScrolledComposite scrolledComposite;
	private TreeViewer businessModelTreeViewer;
	

	protected PropertySheetPage propertySheetPage;
	
	private BasicCommandStack commandStack;
	
	protected AdapterFactoryEditingDomain editingDomain;
	private Object currentTreeSelection;
	private CoreSingleton cs = CoreSingleton.getInstance();
	
	static public BusinessModelFactory FACTORY = BusinessModelFactory.eINSTANCE;
	
	public BusinessModelView() {
	    
		// Create an adapter factory that yields item providers.
		List<BusinessModelAdapterFactory> factories = new ArrayList<BusinessModelAdapterFactory>();
		factories.add(new BusinessModelItemProviderAdapterFactory());
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);
		
		// Create the editing domain with a special command stack.
		commandStack = new BasicCommandStack();
		editingDomain = new AdapterFactoryEditingDomain(adapterFactory, commandStack);
		
	}

	@Override
	public void createPartControl(Composite parent) {
		scrolledComposite = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);						
	}
	
	
	public void setModel(BusinessModel model){
		Composite container = new Composite(scrolledComposite, SWT.NONE);
		GridLayout gridLayout;
		Group businessModelGroup;
		
		container = new Composite(scrolledComposite, SWT.NONE);
		
		gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		gridLayout.makeColumnsEqualWidth = true;
		
		container.setLayout(gridLayout); 
		
		scrolledComposite.setContent(container);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(container.computeSize(200, 300));

	    businessModelGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		businessModelGroup.setLayout(new GridLayout());
		
		//Create a TreeViewer
		businessModelTreeViewer = new TreeViewer(businessModelGroup);
		List<BusinessModelAdapterFactory> factories = new ArrayList<BusinessModelAdapterFactory>();
		factories.add(new BusinessModelItemProviderAdapterFactory());
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);	
		businessModelTreeViewer.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		businessModelTreeViewer.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		businessModelTreeViewer.setUseHashlookup(true);
		
	    //setting initial input
		model.eAdapters().add(new BusinessModelItemProviderAdapterFactory().createBusinessModelAdapter());
	    businessModelTreeViewer.setInput(model);
	    
	    businessModelGroup.setText("Business Model: "+model.getName());
	    
	    //register the tree as a selection provider
	    getSite().setSelectionProvider(businessModelTreeViewer);
	    
	    //add a SelectionListener to the tree
	    businessModelTreeViewer.addSelectionChangedListener(this);
	    
	    //Create Context Menu and Menu Actions    
	    hookContextMenu();
		
	    //setting datalayout
	    GridData gd = new GridData(GridData.FILL_BOTH);
		businessModelGroup.setLayoutData(gd);
		businessModelTreeViewer.getTree().setLayoutData(gd);
		
		Point p = container.getSize();
		container.pack();
		container.setSize(p);
	}
	
	
	
	
	
	
	
	
	
	
	
	

	//  --------------------------------------------------------
	//	Context Menu methods
	//  --------------------------------------------------------
	
	/**
	 * This creates a context menu for the viewer and adds a listener as well registering the menu for
	 * extension.
	 */
	private void hookContextMenu()
	{
		MenuManager contextMenu = new MenuManager("#PopUp");
		contextMenu.add(new Separator("additions"));
		contextMenu.setRemoveAllWhenShown(true);
		contextMenu.addMenuListener( new BusinessModelPopUpMenuListener(this) );

		Menu menu = contextMenu.createContextMenu(businessModelTreeViewer.getControl());
		businessModelTreeViewer.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu, businessModelTreeViewer);

	}	
	
	
	

	/*
	 * check what element is selected in the tree
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection;
		if (event.getSelectionProvider() == businessModelTreeViewer){
			selection = event.getSelection();
		    if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1)
		    {
		      currentTreeSelection = ((IStructuredSelection)selection).getFirstElement();
		    }
		}
		
	}	
	
	public Object getCurrentTreeSelection() {
		return currentTreeSelection;
	}

	/**
	 * This returns the editing domain as required by the {@link IEditingDomainProvider} interface.
	 * This is important for implementing the static methods of {@link AdapterFactoryEditingDomain}	
	 * and for supporting {@link org.eclipse.emf.edit.ui.action.CommandAction}.
	 */
	@Override
	public EditingDomain getEditingDomain() {
		return editingDomain;
	}
	
	/**
	 * This is how the framework determines which interfaces we implement.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(Class key) {	
		if (key.equals(IPropertySheetPage.class)) {
			return getPropertySheetPage();
		}
		else {
			return super.getAdapter(key);
		}
	}	
	
	/**
	 * This accesses a cached version of the property sheet.
	 */
	public IPropertySheetPage getPropertySheetPage() {
		
		List<BusinessModelAdapterFactory> factories = new ArrayList<BusinessModelAdapterFactory>();
		factories.add(new BusinessModelItemProviderAdapterFactory());
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);			
		if (propertySheetPage == null) {
			propertySheetPage = new PropertySheetPage();
			propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
		}

		return propertySheetPage;
	}
	
	@Override
	public void setFocus() {
		scrolledComposite.setFocus();
	}	
	
	public TreeViewer getBusinessModelTreeViewer() {
		return businessModelTreeViewer;
	}

	public void setBusinessModelTreeViewer(TreeViewer businessModelTreeViewer) {
		this.businessModelTreeViewer = businessModelTreeViewer;
	}
}
