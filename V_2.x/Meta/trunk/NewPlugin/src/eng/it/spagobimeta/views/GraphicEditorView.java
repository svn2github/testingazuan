/*
 * This View shows the Business Model 
 */
package eng.it.spagobimeta.views;

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
import eng.it.spagobimeta.dnd.TableDropListener;
import eng.it.spagobimeta.model.BMWrapper;
import eng.it.spagobimeta.model.BusinessClass;
import eng.it.spagobimeta.model.BusinessModel;
import eng.it.spagobimeta.util.BMTreeContentProvider;
import eng.it.spagobimeta.util.BMTreeLabelProvider;


public class GraphicEditorView extends ViewPart {
	
	private ScrolledComposite sc;
	
	
	public GraphicEditorView() {
		
	}

	@Override
	public void createPartControl(Composite parent) {
		sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);				
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
		
		//Create a TreeViewer
		TreeViewer bmTree = new TreeViewer(bmGroup);
	    bmTree.setContentProvider(new BMTreeContentProvider());
	    bmTree.setLabelProvider(new BMTreeLabelProvider());
	    bmTree.setUseHashlookup(true);
	    
	    //TODO: (TO REMOVE) create fake BM holder 
	    BusinessModel bm = new BusinessModel("My Business Model Name");
	    bm.addBc(new BusinessClass("My Business Class",bm));
	    //Get Singleton class
	    BMWrapper.getInstance().init(bm);
	   	    
	    //add drop support
	    int operations = DND.DROP_COPY | DND.DROP_MOVE;
	    Transfer[] transferTypes = new Transfer[]{TextTransfer.getInstance()};
	    TableDropListener dtListener = new TableDropListener(bmTree);
	    bmTree.addDropSupport(operations, transferTypes, dtListener);
	    //Set initial input
	    bmTree.setInput(BMWrapper.getInstance());
	    bmTree.expandAll();
	    
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

}
