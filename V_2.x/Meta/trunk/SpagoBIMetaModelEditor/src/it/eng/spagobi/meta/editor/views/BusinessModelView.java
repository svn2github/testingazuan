/*
 * This View shows the Business Model 
 */
package it.eng.spagobi.meta.editor.views;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.editor.dnd.TableDropListener;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.provider.BusinessModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.business.util.BusinessModelAdapterFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.provider.PhysicalModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.physical.util.PhysicalModelAdapterFactory;

import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
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
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;


public class BusinessModelView extends ViewPart {
	
	private ScrolledComposite sc;
	private TreeViewer bmTree;
	protected PropertySheetPage propertySheetPage;
	
	
	public BusinessModelView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);						
	}
	
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
        CoreSingleton cs = CoreSingleton.getInstance();
        Model rootModel = cs.getRootModel();
        String bmName = cs.getBmName();
        PhysicalModel pm = cs.getPhysicalModel();
	    
	    //initialize the EMF Physical Model
        BusinessModelInitializer modelInitializer = new BusinessModelInitializer();
        BusinessModel model = modelInitializer.initialize( bmName, pm);
				
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
	      
	    /*
	    //TODO: (TO REMOVE) create fake BM holder 
	    BusinessModel bm = new BusinessModel("My Business Model Name");
	    bm.addBc(new BusinessClass("My Business Class",bm));
	    //Get Singleton class
	    BMWrapper.getInstance().init(bm);
	    */
	   	    
	    //add drop support
	    int operations = DND.DROP_COPY | DND.DROP_MOVE;
	    Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
	    TableDropListener dtListener = new TableDropListener(bmTree);
	    bmTree.addDropSupport(operations, transferTypes, dtListener);
	    
	    //Set initial input
	    model.eAdapters().add(new BusinessModelItemProviderAdapterFactory().createBusinessModelAdapter());
	    bmTree.setInput(model);
	    //bmTree.expandAll();
	    
	    //register the tree as a selection provider
	    getSite().setSelectionProvider(bmTree);

	    //setting datalayout
	    GridData gd = new GridData(GridData.FILL_BOTH);
		bmGroup.setLayoutData(gd);
		bmTree.getTree().setLayoutData(gd);
		
		Point p = container.getSize();
		container.pack();
		container.setSize(p);		
	}

	@Override
	public void setFocus() {
		sc.setFocus();
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
	
}
