/*
 * This class provide the view that show the structure of a Database
 * previously connected via the DSE view
 */

package eng.it.spagobimeta.views;

import java.net.MalformedURLException;
import java.net.URL;


import eng.it.spagobimeta.Activator;
import eng.it.spagobimeta.util.DSE_Bridge;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.modelbase.sql.schema.Database;
import org.eclipse.datatools.modelbase.sql.schema.Schema;
import org.eclipse.datatools.modelbase.sql.tables.Column;
import org.eclipse.datatools.modelbase.sql.tables.Table;
import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.internal.views.ViewsPlugin;
import org.eclipse.ui.part.ViewPart;

public class DBStructureView extends ViewPart {
	
	public Composite container;
	public ScrolledComposite sc;
	public boolean ender = false;
	DSE_Bridge dse;
	Schema sch; 
	@Override
	public void createPartControl(Composite parent) {
		sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);
		container = new Composite(sc, SWT.NONE);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		gridLayout.makeColumnsEqualWidth = true;
		/*
		container.setLayout(gridLayout); 
		sc.setContent(container);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(200, 300));
		*/
		//createTree();
	
	}
	
	public void createTree(IConnectionProfile cp){
		//temporary code
		//IConnectionProfile[] profiles = dse.get_CP();
		DSE_Bridge dse = new DSE_Bridge();
		cp.connect();
		Database db = dse.get_dbModel(cp);
		EList<Schema> schemas = dse.get_schemas(db);
		Schema sch = schemas.get(0);
		//end temp code
		
		//create a new container to host the tree structure
		container = new Composite(sc, SWT.NONE);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		gridLayout.makeColumnsEqualWidth = true;
		container.setLayout(gridLayout); 
		sc.setContent(container);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(200, 300));
		
		
		//Setting up tree for tables
		Group connGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		connGroup.setText(cp.getName());
		connGroup.setLayout(new GridLayout());
		
		final Tree connTree = new Tree(connGroup, SWT.BORDER);
		connTree.setSize(new Point(200, 400));
		
		TreeItem tiDB = new TreeItem(connTree, 0);
		tiDB.setText(sch.getName()); 
		
		Image image = Activator.getImageDescriptor("icons/database.png").createImage();
	    if (image!=null) 
	    {
	    	tiDB.setImage(image);		
	    }
	    //Retrieve table list
	    EList<Table> tables = dse.get_tables(sch);
		//Retrieve table inside schema
		for(Table tab:tables)
		{
			//insert tree for tables
			TreeItem ti = new TreeItem(tiDB, 0);
			ti.setText(tab.getName());
			Image image2 = Activator.getImageDescriptor("icons/table.gif").createImage();
			if (image2!=null) ti.setImage(image2);
			
			//retrieve columns of tables
			EList<Column> columns = dse.get_columns(tab);
			for(Column col:columns)
			{
				//insert tree for columns
				TreeItem tti = new TreeItem(ti, 0);
				tti.setText(col.getName());
				if (col.isPartOfPrimaryKey()){
					image = Activator.getImageDescriptor("icons/key.png").createImage();
				    if (image!=null) tti.setImage(image);
				}
				else {
					image = Activator.getImageDescriptor("icons/column.png").createImage();
				    if (image!=null) tti.setImage(image);
				}

			    //insert tree columns type
				TreeItem ttti1 = new TreeItem(tti, 0);
				ttti1.setText("Type: "+col.getContainedType().getName());
				image = Activator.getImageDescriptor("icons/arrow.png").createImage();
			    if (image!=null) ttti1.setImage(image);
			    
			    //check if the column is part of FK
			    if (col.isPartOfForeignKey()){
					ttti1 = new TreeItem(tti, 0);
					ttti1.setText("Is part of FK");
					image = Activator.getImageDescriptor("icons/arrow.png").createImage();
				    if (image!=null) ttti1.setImage(image);
			    }
			    //check if the column is part of Unique Constraint
			    if (col.isPartOfUniqueConstraint()){
					ttti1 = new TreeItem(tti, 0);
					ttti1.setText("Is part of Unique Constraint");
					image = Activator.getImageDescriptor("icons/arrow.png").createImage();
				    if (image!=null) ttti1.setImage(image);			    	
			    }
			}
		}

		//DRAG & DROP
		//Create the drag source on the tree
	    DragSource ds = new DragSource(connTree, DND.DROP_MOVE);
	    ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
	    ds.addDragListener(new DragSourceAdapter() {
	      public void dragSetData(DragSourceEvent event) {
	         //Set the data to be the first selected item's text
	    	 String sData = connTree.getSelection()[0].getText();
	         event.data = sData;
	      }
	    });
		
		//setto le datalayout
	    GridData gd = new GridData(GridData.FILL_BOTH);
		
	    connGroup.setLayoutData(gd);
		connTree.setLayoutData(gd);
		
		Point p = container.getSize();
		if (ender){
			p.y = 2*p.y;
			ender = false;
		}
		else{ 
			ender=true;
		}
		container.pack();
		container.setSize(p);
		sc.update();
		sc.redraw();
		
		
	}
	  
	/**
     * Returns the image descriptor with the given relative path.
     */
    @SuppressWarnings("deprecation")
	private ImageDescriptor getImageDescriptor(String relativePath) {
            String iconPath = "icons/";
            try {
                    ViewsPlugin plugin = ViewsPlugin.getDefault();
                    URL installURL = plugin.getDescriptor().getInstallURL();
                    URL url = new URL(installURL, iconPath + relativePath);
                    return ImageDescriptor.createFromURL(url);
            }
            catch (MalformedURLException e) {
                    // should not happen
                    return ImageDescriptor.getMissingImageDescriptor();
            }
    }	
	
	@Override
	public void setFocus() {

	}

}
