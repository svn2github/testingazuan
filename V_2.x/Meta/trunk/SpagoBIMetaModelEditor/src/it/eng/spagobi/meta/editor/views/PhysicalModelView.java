/*
 * This class provide the view that show the structure of a Database
 * previously connected via the DSE view
 */

package it.eng.spagobi.meta.editor.views;


import it.eng.spagobi.meta.editor.dnd.TableDragListener;
import it.eng.spagobi.meta.editor.util.DBTreeAdapterFactoryContentProvider;
import it.eng.spagobi.meta.editor.util.DBTreeAdapterFactoryLabelProvider;
import it.eng.spagobi.meta.editor.util.DSEBridge;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.provider.PhysicalModelItemProviderAdapterFactory;
import it.eng.spagobi.meta.model.physical.util.PhysicalModelAdapterFactory;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
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
import org.eclipse.ui.part.ViewPart;

public class PhysicalModelView extends ViewPart {
	
	private ScrolledComposite sc;
	
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
			TreeViewer connTree = new TreeViewer(connGroup, SWT.VIRTUAL | SWT.BORDER);
			/*
			connTree.setContentProvider(new DSEContentProvider());
			connTree.setLabelProvider(new DSELabelProvider());
			connTree.setUseHashlookup(true);
			connTree.setInput(db);
			*/		
	
			//Setting TreeViewer for EMF Model instances
			List<PhysicalModelAdapterFactory> factories = new ArrayList<PhysicalModelAdapterFactory>();
			factories.add(new PhysicalModelItemProviderAdapterFactory());
			ComposedAdapterFactory adapterFactory = new ComposedAdapterFactory(factories);			
			connTree.setContentProvider(new DBTreeAdapterFactoryContentProvider(adapterFactory));
			//connTree.setLabelProvider(new AdapterFactoryLabelProvider(adapterFactory));
			connTree.setLabelProvider(new DBTreeAdapterFactoryLabelProvider(adapterFactory));
			connTree.setUseHashlookup(true);
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
		}

	}

 
	@Override
	public void setFocus() {

	}

	
}
