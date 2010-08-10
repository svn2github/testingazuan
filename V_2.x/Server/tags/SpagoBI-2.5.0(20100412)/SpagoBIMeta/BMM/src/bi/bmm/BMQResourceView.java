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
import java.util.Iterator;

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

import bi.bmm.util.ClassInfo;
import bi.bmm.util.ComplexClassInfo;




public class BMQResourceView extends ViewPart {

	public static final String ID = "bi.bmm.views.bmq.bmqresource";
	public Composite container;
	public boolean ender=false;
	private ArrayList<ClassInfo> bcList;
	private Tree connTree;
	private String bmName;

	
	public BMQResourceView() {
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

	}
	
	
	
	public void addResource(final ArrayList<ClassInfo> bcList,final ArrayList<ComplexClassInfo> cbcList, final String bmName){
		//se la lista ha elementi
		if (!bcList.isEmpty()){
			this.bcList = bcList;
			this.bmName = bmName;
			//imposto l'albero con le tabelle
			Group connGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
			connGroup.setText(bmName);
			connGroup.setLayout(new GridLayout());
			
			
			//------------------------------------------------------------------
			
			
			connTree = new Tree(connGroup, SWT.BORDER);
			connTree.setSize(new Point(200, 400));
			
			TreeItem tiBM = new TreeItem(connTree, 0);
			tiBM.setText(bmName); 
			
			Image image = Activator.getImageDescriptor("icons/bm_16.png").createImage();
		    if (image!=null) tiBM.setImage(image);
			
		    //Per ogni BC la AGGIUNGO
		    
		    for (int i=0; i < bcList.size(); i++){
				
		    	ClassInfo ci = bcList.get(i);
		    	TreeItem tiBC = new TreeItem(tiBM, 0);
				tiBC.setText(ci.getClassName()); 
				Image image2 = Activator.getImageDescriptor("icons/ELUniverse/BC_16x16.png").createImage();
				if (image2!=null) tiBC.setImage(image2);
				
	
			    //recupero i campi delle BC
			    //Chiavi
				for (int z = 0; z < ci.getKeys().size() ; z++){
			    	TreeItem ti = new TreeItem(tiBC, 0);
			    	ti.setText(ci.getKeys().get(z)[0]);
			    	image = Activator.getImageDescriptor("icons/ELUniverse/key.png").createImage();
				    if (image!=null) ti.setImage(image);
					
			    }
			    //Attributi
				for (int z = 0; z < ci.getAttributes().size() ; z++){
			    	TreeItem ti = new TreeItem(tiBC, 0);
			    	ti.setText(ci.getAttributes().get(z)[0]);
			    	image = Activator.getImageDescriptor("icons/ELUniverse/Expert.gif").createImage();
				    if (image!=null) ti.setImage(image);
					
			    }
				//Relazioni
				for (int z = 0; z < ci.getRelationships().size() ; z++){
			    	
			    	if (ci.getRelationships().get(z)[3].contains("MANY_TO_ONE")
			    			|| ci.getRelationships().get(z)[3].contains("ONE_TO_ONE")
			    			|| ci.getRelationships().get(z)[3].contains("MANY_TO_MANY"))
			    	{
			    		TreeItem ti = new TreeItem(tiBC, 0);
			    		ti.setText(ci.getRelationships().get(z)[0]+" { FK: "+ci.getRelationships().get(z)[1]+" }");

				    	image = Activator.getImageDescriptor("icons/ELUniverse/fkey.png").createImage();
					    if (image!=null) ti.setImage(image);
			    	}
			    	
			    	else
			    	{
			    		/* NON NECESSARIO PER LE QUERY
			    		TreeItem ti = new TreeItem(tiBC, 0);
			    		String little = ci.getRelationships().get(z)[1].substring(0,1).toLowerCase() + ci.getRelationships().get(z)[1].substring(1);
				    	little += "Collection";
			    		ti.setText(little + " { Set<"+ci.getRelationships().get(z)[1]+"> }");

				    	image = Activator.getImageDescriptor("icons/ELUniverse/anchor_16x16.png").createImage();
					    if (image!=null) ti.setImage(image);
					    */
			    	}
			    }
		    }
		    
		    //Per ogni CBC la AGGIUNGO
		    
		    for (int i=0; i < cbcList.size(); i++){
				
		    	ComplexClassInfo cci = cbcList.get(i);
		    	TreeItem tiCBC = new TreeItem(tiBM, 0);
				tiCBC.setText(cci.getClassName()); 
				Image image2 = Activator.getImageDescriptor("icons/ELUniverse/CBC_16x16.png").createImage();
				if (image2!=null) tiCBC.setImage(image2);
				
	
			    //recupero i campi della CBC
			    //Campi delle BC
				Iterator<String> bcIter = cci.getInhBC().keySet().iterator();
				while(bcIter.hasNext()){
					String key = bcIter.next();
					
					ArrayList<String[]> fields = cci.getInhBC().get(key);
					Iterator<String[]> fieldIter = fields.iterator();
					while(fieldIter.hasNext()){
						String[] field = fieldIter.next();
						TreeItem ti = new TreeItem(tiCBC, 0);
						String[] k = field[0].split("\\.");
						if(k.length>0){
							ti.setText(k[k.length-1]); 
						}
						else{
							ti.setText(field[0]); 
						}
						Image image11 = Activator.getImageDescriptor("icons/ELUniverse/dot_red_16x16.png").createImage();
						if (image11!=null) ti.setImage(image11);
					}
				}
				//Campi dei WS
				Iterator<String[]> wsIter = cci.getInhWS().keySet().iterator();
				while(wsIter.hasNext()){
					String[] key = wsIter.next();
					TreeItem tiKey = new TreeItem(tiCBC, 0);
					//controllo se si può spilttare ed in quel caso prendo l'ultimo
					if(key[0].toString().contains("\\.")){
						String[] k = key[0].split("\\.");
						tiKey.setText(k[k.length-1]); 
					}
					else{
						tiKey.setText(key[0]); 
					}
					Image image1 = Activator.getImageDescriptor("icons/ELUniverse/dot_yellow_16x16.png").createImage();
					if (image1!=null) tiKey.setImage(image1);
					
				}
				
		    }
			
			//associo la possibilità di D'N'D
			 // Create the drag source on the tree
		    DragSource ds = new DragSource(connTree, DND.DROP_MOVE);
		    ds.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		    ds.addDragListener(new DragSourceAdapter() {
		      public void dragSetData(DragSourceEvent event) {
		    	 TreeItem ti = connTree.getSelection()[0];
		    	 TreeItem tiDad = ti.getParentItem();
		    	 String sData = ti.getText();
		    	 
		    	 if(tiDad.getText().equals(bmName)){
		    		 event.data = null;
		    		 return;
		    	 }
		    	 Iterator<ComplexClassInfo> cbcIterator = cbcList.iterator();
		    	 while(cbcIterator.hasNext()){
		    		 ComplexClassInfo cci = cbcIterator.next();
		    		 if (cci.getClassName().equals(tiDad.getText())){
		    			 event.data = "CBC#"+tiDad.getText()+"#"+ti.getText();
		    			 return;
		    		 }
		    	 }
		    	 if (sData.contains("FK:")){
		    		 String tmp = sData.split(" \\{")[0];
		    		 sData = tmp;
		    	 }
		    	 String sTable = connTree.getSelection()[0].getParentItem().getText();
		    	 //controllo se si tratta di una BC o di un BE
		    	 if (sTable.equals(bmName)) event.data = null;
		    	 if (sTable.equals(""));
		    	 else event.data = sTable+"#"+sData;
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
			
		    MessageDialog.openInformation(new Shell(), "Compilation Complete","Compilation from source was completed.");
		    
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

	public  ArrayList<ClassInfo> getBCList() {
		
		return bcList;
	}

	public String[] hasNavigation(String navChild, ArrayList<String> fromList) {
	for(int j = 0; j < fromList.size(); j++){
		String fromName = fromList.get(j);
		for (int i = 0; i < connTree.getItemCount(); i++){
			if (connTree.getItem(i).getText().equals(bmName)){
				for(int z = 0; z < connTree.getItem(i).getItemCount(); z++){
					if (connTree.getItem(i).getItem(z).getText().equals(fromName)){
						for(int k = 0; k < connTree.getItem(i).getItem(z).getItemCount(); k++){
							if (connTree.getItem(i).getItem(z).getItem(k).getText().contains(navChild.split("#")[0])){
								return new String[] {
									fromName, connTree.getItem(i).getItem(z).getItem(k).getText().split(" \\{")[0]	
								};
							}
						}
					}
				}
			}
		}
	}
		
		return null;
	}
	
	public boolean hasRelation(String tab1, String tab2){
		TreeItem item = connTree.getItem(0);
		TreeItem[] items = item.getItems();
		for (int i = 0; i < items.length; i++){
			TreeItem ti = items[i];
			if (ti.getText().equals(tab1)){
				//controllo se tra i suoi figli qualcuno ha relazioni con tab2
				TreeItem[]  children = ti.getItems();
				for(int j = 0; j < children.length; j++){
					TreeItem child = children[j];
					if(child.getText().contains("FK")){
						if (child.getText().split("\\{")[1].contains(tab2))
							return true;
					}
				}
			}
			if (ti.getText().equals(tab2)){
				//controllo se tra i suoi figli qualcuno ha relazioni con tab2
				TreeItem[]  children = ti.getItems();
				for(int j = 0; j < children.length; j++){
					TreeItem child = children[j];
					if(child.getText().contains("FK")){
						if (child.getText().split(" \\{")[1].contains(tab1))
							return true;
					}
				}
			}
			
		}
		return false;
	}

	public String[] getRelation(String tab1, String tab2) {
		String relation1 = "";
		String relation2 = "";
		TreeItem item =connTree.getItem(0);
		TreeItem[] items = item.getItems() ;
		for (int i = 0; i < items.length; i++){
			TreeItem ti = items[i];
			if (ti.getText().equals(tab1)){
				//controllo se tra i suoi figli qualcuno ha relazioni con tab2
				TreeItem[]  children = ti.getItems();
				for(int j = 0; j < children.length; j++){
					TreeItem child = children[j];
					if(child.getText().split(" \\{ FK: "+tab2).length>1){
						if (child.getText().split("\\{")[1].contains(tab2))
							relation1 = child.getText().split(" \\{")[0];
							//cerco la PK dell'altra tabella
							for(int k = 0; k < items.length; k++){
								TreeItem ti2 = items[k];
								if (ti2.getText().equals(tab2)){
									relation2 = ti2.getItem(0).getText(); 
								}
							}
						}
					}
			}
			if (ti.getText().equals(tab2)){
				//controllo se tra i suoi figli qualcuno ha relazioni con tab1
				TreeItem[]  children = ti.getItems();
				for(int j = 0; j < children.length; j++){
					TreeItem child = children[j];
					if(child.getText().split(" \\{ FK: "+tab1).length>1){
						relation2 = child.getText().split(" \\{")[0];
						//cerco la PK dell'altra tabella
						for(int k = 0; k < items.length; k++){
							TreeItem ti2 = items[k];
							if (ti2.getText().equals(tab1)){
								relation1 = ti2.getItem(0).getText(); 
							}
						}
					}
				}
			}
			
		}
		return new String[]{relation1,relation2};
	}
	
	
}


