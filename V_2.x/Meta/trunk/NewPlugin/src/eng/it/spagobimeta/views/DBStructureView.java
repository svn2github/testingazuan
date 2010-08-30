/*
 * This class provide the view that show the structure of a Database
 * previously connected via the DSE view
 */

package eng.it.spagobimeta.views;


import eng.it.spagobimeta.dnd.TableDragListener;
import eng.it.spagobimeta.util.DSEContentProvider;
import eng.it.spagobimeta.util.DSELabelProvider;
import eng.it.spagobimeta.util.DSEBridge;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.modelbase.sql.schema.Database;
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

public class DBStructureView extends ViewPart {
	
	private ScrolledComposite sc;
	
	@Override
	public void createPartControl(Composite parent) {
		sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);
	}
	
	public void createTree(IConnectionProfile cp){	
		//get db information from connection profile
		DSEBridge dse = new DSEBridge();
		
		//check if the connection is successful
		if (dse.connect_CP(cp) != null){
			//get the db model object
			Database db = dse.get_dbModel(cp);
			
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
			TreeViewer connTree = new TreeViewer(connGroup);
			connTree.setContentProvider(new DSEContentProvider());
			connTree.setLabelProvider(new DSELabelProvider());
			connTree.setUseHashlookup(true);
			connTree.setInput(db);
			
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
