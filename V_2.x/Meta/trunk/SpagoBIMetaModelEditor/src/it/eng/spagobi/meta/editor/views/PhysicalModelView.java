/*
 * This class provide the view that show the structure of a Database
 * previously connected via the DSE view
 */

package it.eng.spagobi.meta.editor.views;


import it.eng.spagobi.meta.editor.dnd.TableDragListener;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.editor.util.DSEBridge;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import it.eng.spagobi.meta.model.physical.provider.PhysicalModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.physical.util.PhysicalModelAdapterFactory;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryContentProvider;
import org.eclipse.emf.edit.ui.provider.AdapterFactoryLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceListener;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.PropertySheetPage;

public class PhysicalModelView extends ViewPart implements IAdaptable {
	
	private ScrolledComposite sc;
	protected PropertySheetPage propertySheetPage;
	public TreeViewer connTree;

	
	@Override
	public void createPartControl(Composite parent) {
		sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);
	}
	
	public void createTree(IConnectionProfile cp){	
		//get db information from connection profile
		DSEBridge dse = new DSEBridge();
		
		//extract information from the connection
		PhysicalModel model = dse.connectionExtraction(cp);
		//check if the connection extraction is successful
		if (model != null){
			//create a new Composite to host the tree structure
			Composite container = new Composite(sc, SWT.NONE);
			GridLayout gridLayout = new GridLayout(); 
			gridLayout.numColumns = 1; 
			gridLayout.makeColumnsEqualWidth = true;
			container.setLayout(gridLayout); 
		
			//Insert composite inside ScrolledComposite
			sc.setContent(container);
			sc.setExpandHorizontal(true);
			sc.setExpandVertical(true);
			sc.setMinSize(container.computeSize(200, 300));
			
			//Setting up tree for tables
			Group connGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
			connGroup.setText(cp.getName());
			connGroup.setLayout(new GridLayout());
			connTree = new TreeViewer(connGroup, SWT.VIRTUAL | SWT.BORDER);
	
			//Setting TreeViewer for EMF Model instances
			List<PhysicalModelAdapterFactory> factories = new ArrayList<PhysicalModelAdapterFactory>();
			factories.add(new PhysicalModelItemProviderAdapterFactory());
			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);			
			connTree.setContentProvider(new AdapterFactoryContentProvider(adapterFactory));
			connTree.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
			//connTree.setLabelProvider(new DBTreeAdapterFactoryLabelProvider(adapterFactory));
			connTree.setUseHashlookup(true);
			model.eAdapters().add(new PhysicalModelItemProviderAdapterFactory().createPhysicalModelAdapter());
			connTree.setInput(model);
			getSite().setSelectionProvider(connTree);
			
			
			//set drag source
			int operations = DND.DROP_COPY | DND.DROP_MOVE;
			Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
			DragSourceListener dsListener = new TableDragListener(connTree);
			connTree.addDragSupport(operations, transferTypes, dsListener);
			
			//setting datalayout
		    GridData gd = new GridData(GridData.FILL_BOTH);
			connGroup.setLayoutData(gd);
			connTree.getTree().setLayoutData(gd);
			
			Point p = container.getSize();
			container.pack();
			container.setSize(p);
			
			//Create blank sheet in BusinessModelView
			IViewPart businessModelView = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView("it.eng.spagobi.meta.editor.BusinessModel");
			
			//invoke tree creation on BusinessModelView
			//((BusinessModelView)businessModelView).initComposite();
			BusinessModel businessModel = BusinessModelFactory.eINSTANCE.createBusinessModel();
			//Getting CoreSingleton instance
			CoreSingleton cs = CoreSingleton.getInstance();
			String modelName = cs.getBmName();
			PhysicalModel physicalModel = cs.getPhysicalModel();
			businessModel.setName(modelName);
			
			if(physicalModel.getParentModel() != null) {
				businessModel.setParentModel(physicalModel.getParentModel());
			}
			
			businessModel.setPhysicalModel(physicalModel);
			((BusinessModelView)businessModelView).setModel(businessModel);
		}
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
		List<PhysicalModelAdapterFactory> factories = new ArrayList<PhysicalModelAdapterFactory>();
		factories.add(new PhysicalModelItemProviderAdapterFactory());
		ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);			
		if (propertySheetPage == null) {
			propertySheetPage = new PropertySheetPage();
			propertySheetPage.setPropertySourceProvider(new AdapterFactoryContentProvider(adapterFactory));
		}

		return propertySheetPage;
	}	
	
}
