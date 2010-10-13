/*
 * This View shows the Business Model 
 */
package it.eng.spagobi.meta.editor.views;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.dnd.TableDropListener;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.wizards.AddBCWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessIdentifierWizard;
import it.eng.spagobi.meta.editor.wizards.AddBusinessTableWizard;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.provider.BusinessModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.business.util.BusinessModelAdapterFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;


public class BusinessModelView extends ViewPart implements IMenuListener, ISelectionChangedListener ,IEditingDomainProvider {
	
	private ScrolledComposite sc;
	private TreeViewer bmTree;
	protected PropertySheetPage propertySheetPage;
	private Action addBTAction,removeBTAction,addBCAction,removeBCAction,addBIAction ;
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
		sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);						
	}
	
	/**
	 * Create a "blank sheet" for the Business Model Editor
	 */
	public void initComposite(){
		Composite container = new Composite(sc, SWT.NONE);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		gridLayout.makeColumnsEqualWidth = true;
		container.setLayout(gridLayout); 
		sc.setContent(container);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(200, 300));

	    Group bmGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		bmGroup.setLayout(new GridLayout());
		
		//Create a TreeViewer
		bmTree = new TreeViewer(bmGroup);
		List<BusinessModelAdapterFactory> factories = new ArrayList<BusinessModelAdapterFactory>();
		factories.add(new BusinessModelItemProviderAdapterFactory());
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);	
		bmTree.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		bmTree.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		bmTree.setUseHashlookup(true);
		
	    //setting initial input
		BusinessModel model = createEmptyModel();
		model.eAdapters().add(new BusinessModelItemProviderAdapterFactory().createBusinessModelAdapter());
	    bmTree.setInput(model);
	    
	    bmGroup.setText("Business Model: "+model.getName());
	    
	    //register the tree as a selection provider
	    getSite().setSelectionProvider(bmTree);
	    
	    //add a SelectionListener to the tree
	    bmTree.addSelectionChangedListener(this);
	    
	    //Create Context Menu and Menu Actions    
	    createBCActions();
	    createBTActions();
	    createBIActions();
	    hookContextMenu();
		
	    //setting datalayout
	    GridData gd = new GridData(GridData.FILL_BOTH);
		bmGroup.setLayoutData(gd);
		bmTree.getTree().setLayoutData(gd);
		
		Point p = container.getSize();
		container.pack();
		container.setSize(p);
	}
	
	/**
	 * Create an empty BusinessModel to populate
	 * @return an empty BusinessModel
	 */
	public BusinessModel createEmptyModel(){
		BusinessModel businessModel = FACTORY.createBusinessModel();
		//Getting CoreSingleton instance
		CoreSingleton cs = CoreSingleton.getInstance();
		String modelName = cs.getBmName();
		PhysicalModel physicalModel = cs.getPhysicalModel();
		businessModel.setName(modelName);
		
		if(physicalModel.getParentModel() != null) {
			businessModel.setParentModel(physicalModel.getParentModel());
		}
		
		businessModel.setPhysicalModel(physicalModel);
		return businessModel;
	}
	
	/**
	 * Create the tree of the business model
	 */
	public void createTree(){
		Composite container = new Composite(sc, SWT.NONE);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		gridLayout.makeColumnsEqualWidth = true;
		container.setLayout(gridLayout); 
		sc.setContent(container);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(200, 300));

	    Group bmGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		bmGroup.setText("Business Model");
		bmGroup.setLayout(new GridLayout());
		
        //retrieve root Model reference and the PhysicalModel
        Model rootModel = cs.getRootModel();
        String bmName = cs.getBmName();
        PhysicalModel pm = cs.getPhysicalModel();
	    
        BusinessModel model;
        //check if BusinessModel is already created
        if (cs.getBusinessModel() != null){
            //erase current Business Model
        	cs.deleteBusinessModel();    	
        	//initialize the EMF Business Model
            BusinessModelInitializer modelInitializer = new BusinessModelInitializer();
            model = modelInitializer.initialize( bmName, pm);
        }
        else {
        	model = cs.getBusinessModel();
        }

				
		//Create a TreeViewer
		bmTree = new TreeViewer(bmGroup);
	    //bmTree.setContentProvider(new BMTreeContentProvider());
	    //bmTree.setLabelProvider(new BMTreeLabelProvider());
		List<BusinessModelAdapterFactory> factories = new ArrayList<BusinessModelAdapterFactory>();
		factories.add(new BusinessModelItemProviderAdapterFactory());
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);	
		bmTree.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
		bmTree.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
		bmTree.setUseHashlookup(true);
	      	    
	    //add drop support
	    int operations = DND.DROP_COPY | DND.DROP_MOVE;
	    Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
	    TableDropListener dtListener = new TableDropListener(bmTree);
	    bmTree.addDropSupport(operations, transferTypes, dtListener);
	    
	    //Set initial input
	    model.eAdapters().add(new BusinessModelItemProviderAdapterFactory().createBusinessModelAdapter());
	    bmTree.setInput(model);
	    
	    //register the tree as a selection provider
	    getSite().setSelectionProvider(bmTree);
	    
	    //add a SelectionListener to the tree
	    bmTree.addSelectionChangedListener(this);
	    
	    //Create Context Menu and Menu Actions    
	    createBCActions();
	    createBTActions();
	    createBIActions();
	    hookContextMenu();

	    //setting datalayout
	    GridData gd = new GridData(GridData.FILL_BOTH);
		bmGroup.setLayoutData(gd);
		bmTree.getTree().setLayoutData(gd);
		
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
		contextMenu.addMenuListener(this);

		Menu menu = contextMenu.createContextMenu(bmTree.getControl());
		bmTree.getControl().setMenu(menu);
		getSite().registerContextMenu(contextMenu, bmTree);

	}	
	
	/**
	 * Create Business Table node Action to show in the context menu
	 */
	private void createBTActions()
	{
		addBTAction = new Action()
		{
			public void run()
			{
				//Start Create Business Table Wizard
				//Get Active Window
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				//Launch AddBusinessTableWizard
				AddBusinessTableWizard wizard = new AddBusinessTableWizard();
		    	WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
				dialog.create();
		    	dialog.open();
			}
		};
		addBTAction.setText("Add a Business Table");
		addBTAction.setToolTipText("Add a Business Table");
		addBTAction.setImageDescriptor(Activator.getImageDescriptor("add.png"));

		removeBTAction = new Action()
		{
			public void run()
			{
				//EditingDomain domain = getEditingDomain();
				//RemoveCommand cmd = new RemoveCommand();
				cs.getBusinessModel().getTables().remove(currentTreeSelection);
				showMessage("Remove BT executed");
			}
		};
		removeBTAction.setText("Remove Business Table");
		removeBTAction.setToolTipText("Remove Business Table");
		removeBTAction.setImageDescriptor(Activator.getImageDescriptor("remove.png"));
		
	}	

	/**
	 * Create Business Column node Action to show in the context menu
	 */
	private void createBCActions()
	{
		addBCAction = new Action()
		{
			public void run()
			{
				showMessage("Add BC executed");
			}
		};
		addBCAction.setText("Add Business Column");
		addBCAction.setToolTipText("Add Business Column");
		addBCAction.setImageDescriptor(Activator.getImageDescriptor("add.png"));

		removeBCAction = new Action()
		{
			public void run()
			{
				((BusinessColumn)currentTreeSelection).getTable().getColumns().remove(currentTreeSelection);
				showMessage("Remove BC executed");
			}
		};
		removeBCAction.setText("Remove Business Column");
		removeBCAction.setToolTipText("Remove Business Column");
		removeBCAction.setImageDescriptor(Activator.getImageDescriptor("remove.png"));
		
	}	
	/**
	 * Create Business Identifier Action to show in the context menu
	 */
	private void createBIActions()
	{
		addBIAction = new Action()
		{
			public void run()
			{
				//Start Create Business Identifier Wizard
				//Get Active Window
				IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				//Launch AddBusinessIdentifierWizard
				AddBusinessIdentifierWizard wizard = new AddBusinessIdentifierWizard();
		    	WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
				dialog.create();
		    	dialog.open();
				showMessage("Add BI executed");
			}
		};
		addBIAction.setText("Add Business Identifier");
		addBIAction.setToolTipText("Add Business Identifier");
		addBIAction.setImageDescriptor(Activator.getImageDescriptor("add.png"));
		
	}	
	
	@Override
	public void menuAboutToShow(IMenuManager manager) {
		//create context menu based on the current tree selection
		if (currentTreeSelection instanceof BusinessTable){
			manager.removeAll();
			manager.add(addBTAction);
			manager.add(removeBTAction);
			manager.add(addBIAction);
		} else if (currentTreeSelection instanceof BusinessColumn){
			manager.removeAll();
			manager.add(addBCAction);
			manager.add(removeBCAction);
		} else {
			manager.removeAll();
			manager.add(addBTAction);
		}
			
	}
	
	// --------------------------------------------------------
	
	//Show a Message in a Dialog
	private void showMessage(String message)
	{
		MessageDialog.openInformation(bmTree.getControl().getShell(), "Business Model Editor", message);
	}

	/*
	 * check what element is selected in the tree
	 * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		ISelection selection;
		if (event.getSelectionProvider() == bmTree){
			selection = event.getSelection();
		    if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1)
		    {
		      currentTreeSelection = ((IStructuredSelection)selection).getFirstElement();
		    }
		}
		
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
		sc.setFocus();
	}	
}
