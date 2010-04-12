/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

@Author Marco Cortella

**/
package bi.bmm;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.part.ViewPart;

import bi.bmm.util.ColumnInfo;
import bi.bmm.util.DBConnection;
import bi.bmm.util.WSConnection;

public class ELUniverseView extends ViewPart {

	public static final String ID = "bi.bmm.views.bme.eluniverse";
	public Composite container;
	public boolean ender=false;
	private HashMap<String, String[]> fkeys;
	
	public ELUniverseView() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createPartControl(Composite parent) {
		ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL |   
				  SWT.V_SCROLL | SWT.BORDER);
		container = new Composite(sc, SWT.NONE);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 2; 
		gridLayout.makeColumnsEqualWidth = true;
		container.setLayout(gridLayout); 
		//container.setSize(parent.getSize());
		sc.setContent(container);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.setMinSize(container.computeSize(1000, 1000));
		
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}
	
	public void addTables(final DBConnection conn){
		//se riesco ad accedere alla connessione
		if (conn.Start()){
			//imposto l'albero con le tabelle
			Group connGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
			connGroup.setText(conn.getName());
			connGroup.setLayout(new GridLayout());
			
			
			//------------------------------------------------------------------
			
			
			final Tree connTree = new Tree(connGroup, SWT.BORDER);
			connTree.setSize(new Point(200, 400));
			
			TreeItem tiDB = new TreeItem(connTree, 0);
			tiDB.setText(conn.getDBName()); 
			
			Image image = Activator.getImageDescriptor("icons/ELUniverse/database.png").createImage();
		    if (image!=null) tiDB.setImage(image);
			
	
		    
		    //ottengo la lista delle tabelle
			Vector<String[]> v =conn.getTables();
			int max = v.size();
			
			for (int i=0; i < max; i++){
				String[] vL = v.remove(0);
				//inserisco le tree delle tabelle
				TreeItem ti = new TreeItem(tiDB, 0);
				ti.setText(vL[0]); 
				Image image2 = Activator.getImageDescriptor("icons/ELUniverse/properties.png").createImage();
				if (image2!=null) ti.setImage(image2);
				
	
			    //recupero i campi delle tabelle
			   
			    List<ColumnInfo> columns = conn.getColumns(vL[0]);
			    fkeys = conn.getFK();
			   
			    Iterator<ColumnInfo> itr = columns.iterator();
			    
			    while(itr.hasNext()){
			    	TreeItem tti = new TreeItem(ti, 0);
			    	ColumnInfo ci = itr.next();
					tti.setText(ci.getColumnName());
					if (ci.isKey()){
						image = Activator.getImageDescriptor("icons/ELUniverse/key.png").createImage();
					    if (image!=null) tti.setImage(image);
					}
					else{
						image = Activator.getImageDescriptor("icons/ELUniverse/R.png").createImage();
					    if (image!=null) tti.setImage(image);
					}
					//recupero i dettagli delle colonne
					TreeItem ttti1 = new TreeItem(tti, 0);
					ttti1.setText(ci.getColumnType() +" ("+ ci.getSize()+") - [pos: "+ci.getPosition()+"]");
					
			    }

			   
			}
			
			//associo la possibilità di D'N'D
			 // Create the drag source on the tree
		    DragSource ds = new DragSource(connTree, DND.DROP_MOVE);
		    ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		    ds.addDragListener(new DragSourceAdapter() {
		      public void dragSetData(DragSourceEvent event) {
		        // Set the data to be the first selected item's text
		    	 String sData = connTree.getSelection()[0].getText()+"#"+
		    	 conn.getDriver()+"#"+
		    	 conn.getServer()+"#"+
		    	 conn.getDBName()+"#"+
		    	 conn.getUser()+"#"+
		    	 conn.getPassword()+"#"+
		    	 conn.getPort()+"#"+
		    	 conn.getName();
		         event.data = sData;
		      }
		    });
			
			
			
			/*
			 * test zone
			 * */
			/*
			 * 
			 * 
			Composite innerDown = new Composite(testGroup,SWT.NULL);
			GridLayout glInnerDown = new GridLayout(); 
			glInnerDown.numColumns = 1; 
			innerDown.setLayout(glInnerDown);
			 // Create the button
		    final Button button = new Button(innerDown, SWT.FLAT);
		    button.setText("Button");
		    button.setAlignment(SWT.CENTER);

		    // Create the drop target on the button
		    DropTarget dt = new DropTarget(button, DND.DROP_MOVE);
		    dt.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		    dt.addDropListener(new DropTargetAdapter() {
		      public void drop(DropTargetEvent event) {
		        // Set the buttons text to be the text being dropped
		        button.setText((String) event.data);
		      }
		    });
			*/
			
		    MessageDialog.openInformation(new Shell(), "Extraction Complete","Extraction from source was completed.");
		    
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
			
		  }
		
		
		//chiudo la connessione
			conn.Stop();
		}
	
	public HashMap<String, String[]> getFK(){
		return fkeys;
	}

	public void addWSTables(WSConnection conn) {
		
			//imposto l'albero con le tabelle
			Group connGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
			connGroup.setText(conn.getName());
			connGroup.setLayout(new GridLayout());
			
			
			//------------------------------------------------------------------
			
			
			final Tree connTree = new Tree(connGroup, SWT.BORDER);
			connTree.setSize(new Point(200, 400));
			
			TreeItem tiWS = new TreeItem(connTree, 0);
			tiWS.setText(conn.getName()); 
			
			Image image = Activator.getImageDescriptor("icons/ELUniverse/webservice.png").createImage();
		    if (image!=null) tiWS.setImage(image);
			
	
		    
		    //ottengo la lista delle tabelle
			HashMap<String, ArrayList<String[]>> methods = conn.getMethods();
			SortedSet<String> sortedKeys = new TreeSet<String>();
			Iterator<String> iter = methods.keySet().iterator();
			while(iter.hasNext()){
				sortedKeys.add(iter.next());
			}
			System.out.println(sortedKeys);
			
			iter = sortedKeys.iterator();
			while(iter.hasNext()){
				String key = iter.next();
				if(!key.contains("Exception")){
					if(!key.contains("Response")){
						ArrayList<String[]> parameters =methods.get(key);
						
						TreeItem method = new TreeItem(tiWS, 0);
						method.setText(key);
						Image image2 = Activator.getImageDescriptor("icons/ELUniverse/dot_green.png").createImage();
						if (image2!=null) method.setImage(image2);
						
						Iterator<String[]> it = parameters.iterator();
						while(it.hasNext()){
							String[] param = it.next();
							TreeItem ti = new TreeItem(method, 0);
							ti.setText(param[0] + " - "+param[1]+" - "+key);
							Image image3 = Activator.getImageDescriptor("icons/ELUniverse/dot_yellow_16x16.png").createImage();
							if (image3!=null) ti.setImage(image3);
						}
					}
					else{
						//aggiungo in rosso un return
						String newKey = key.replaceAll("Response","");
						for (int i = 0; i<tiWS.getItems().length;i++){
							if (tiWS.getItems()[i].getText().contains(newKey)){
								ArrayList<String[]> parameters =methods.get(key);
								Iterator<String[]> it = parameters.iterator();
								while(it.hasNext()){
									String[] param = it.next();
									TreeItem ti = new TreeItem(tiWS.getItems()[i], 0);
									ti.setText(param[0] + " - "+param[1]+" - "+key);
									Image image3 = Activator.getImageDescriptor("icons/ELUniverse/dot_red_16x16.png").createImage();
									if (image3!=null) ti.setImage(image3);
								}
								break;
							}
						}
						
					}
				}
			}
			
			//associo la possibilità di D'N'D
			
			DragSource dsWS = new DragSource(connTree, DND.DROP_MOVE);
		    dsWS.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		    dsWS.addDragListener(new DragSourceAdapter() {
		      public void dragSetData(DragSourceEvent event) {
		        // Set the data to be the first selected item's text
		    	 String sData = connTree.getSelection()[0].getText();
		         event.data = sData+" - "+connTree.getItem(0).getText();
		      }
		    });
			
			
		    MessageDialog.openInformation(new Shell(), "Extraction Complete","Extraction from source was completed.");
		    
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
		
	}
	
	
		
}


