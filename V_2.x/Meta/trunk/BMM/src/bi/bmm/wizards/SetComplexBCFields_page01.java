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
package bi.bmm.wizards;


import java.util.ArrayList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;

import bi.bmm.Activator;
import bi.bmm.BMUniverseView;
import bi.bmm.util.ClassInfo;

public class SetComplexBCFields_page01 extends WizardPage {
		
	private String cBCName;
	private ArrayList<String> inhBCList;
	private Composite container;
	private Tree methodTree;
	private Table fieldTable;
	private ArrayList<String> fieldList;
	
	public SetComplexBCFields_page01(String cBCName, ArrayList<String> inhBCList) {
		super("Set Complex BC details");
		setTitle("Complex BC Details");
		setDescription("This wizard drives you to add fields to your new Complex Business Class.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createComplexBC.png");
	    if (image!=null) setImageDescriptor(image);
	    
	    this.cBCName = cBCName;
	    this.inhBCList = inhBCList;
	    this.fieldList = new ArrayList<String>();
	   
	}

	@Override
	public void createControl(Composite parent) {
		
		//recupero la vista
		BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
		//setto i container
		 container = new Composite(parent, SWT.NULL);
		 GridLayout gl = new GridLayout();
		 gl.numColumns = 3;
		 gl.makeColumnsEqualWidth = false;
		 container.setLayout(gl);
		 //------------------------------------------------------------
		 Group methodGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		 methodGroup.setText("BCs Attributes: ");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 1;
		 glDet.makeColumnsEqualWidth = true;
		 methodGroup.setLayout(glDet);
		 methodGroup.setLayoutData(gd);
		 
		 		//------------------------------------------------------
		 		Label l1 = new Label(methodGroup,SWT.NULL);
		 		l1.setText("BC Name: ");
		 		
		 		methodTree  =new Tree(methodGroup, SWT.NULL);
		 		for (int i = 0 ; i < inhBCList.size(); i++ ){
		 			TreeItem root = new TreeItem(methodTree, 0);
		 			root.setText(inhBCList.get(i));
		 			Image image2 = Activator.getImageDescriptor("icons/ELUniverse/BC_16x16.png").createImage();
					if (image2!=null) root.setImage(image2);
		 			//cerco nella bcList la classInfo con il nome del inhBC
		 			ClassInfo ci = null;
		 			for(int k = 0 ; k < bmUniverse.bcList.size(); k++){
		 				if ( bmUniverse.bcList.get(k).getClassName().equals(inhBCList.get(i))){
		 					ci = bmUniverse.bcList.get(k);
		 				}
		 			}
		 			if (ci != null){
		 				//inserisco tutti i campi
		 				for(int j = 0 ; j < ci.getMappings().size() ;  j++){
		 					TreeItem ti = new TreeItem(root, 0);
		 					ti.setText(ci.getMappings().get(j)[0]+" - "+ci.getMappings().get(j)[2]);
		 					Image image = Activator.getImageDescriptor("icons/ELUniverse/dot.png").createImage();
							if (image!=null) ti.setImage(image);
		 				}
		 				//inserisco le relazioni
		 				for(int j = 0 ; j < ci.getRelationships().size() ;  j++){
		 					TreeItem ti = new TreeItem(root, 0);
		 					String name4Collection = ci.getRelationships().get(j)[1].substring(0,1).toLowerCase()+
		 							ci.getRelationships().get(j)[1].substring(1)+"Collection";
		 					String nameEl;
		 					if(ci.getRelationships().get(j)[3].equals("ONE_TO_MANY")){
		 						nameEl =name4Collection;
		 						ti.setText(nameEl+" - Collection<"+ci.getRelationships().get(j)[1]+">");
		 						nameEl +=".get(i)";
		 					}
		 					else {
		 						nameEl = ci.getRelationships().get(j)[0];
		 						ti.setText(nameEl+" - "+ci.getRelationships().get(j)[1]+"");
		 					}
		 					
		 					Image image = Activator.getImageDescriptor("icons/ELUniverse/insert_link.png").createImage();
							if (image!=null) ti.setImage(image);
							
							//ricerco la classInfo che la descrive
							ClassInfo ciI = null;
				 			for(int k = 0 ; k < bmUniverse.bcList.size(); k++){
				 				if ( bmUniverse.bcList.get(k).getClassName().equals(ci.getRelationships().get(j)[1])){
				 					ciI = bmUniverse.bcList.get(k);
				 				}
				 			}
				 			if (ciI != null){
				 				
				 				//TODO: rendere l'operazione recursiva in modo da navigare le entità oltre al primo livello
				 				//se la trovo ne descrivo i parametri(al primo livello) !!!! NON LE RELAZIONI
				 				//inserisco tutti i campi
				 				for(int w = 0 ; w < ciI.getMappings().size() ;  w++){
				 					TreeItem tiI = new TreeItem(ti, 0);
				 					tiI.setText(nameEl+"."+ciI.getMappings().get(w)[0]+" - "+ciI.getMappings().get(w)[2]);
				 					Image image3 = Activator.getImageDescriptor("icons/ELUniverse/dot_yellow.png").createImage();
									if (image3!=null) tiI.setImage(image3);
				 				}
				 			}
		 				}
		 			}
		 		}
		 		methodTree.setLayoutData(new GridData(GridData.FILL_BOTH));
//------------------------------------------------------
	Group buttonGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
	buttonGroup.setLayout(glDet);
	buttonGroup.setLayoutData(gd);
	//bottoni per inserire e togliere
				Composite c3 = new Composite(buttonGroup, SWT.NULL);
				GridLayout gl3 = new GridLayout();
				gl3.numColumns = 1;
				c3.setLayout(gl3);
				c3.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
								//------------------------------------------------------
								Button bAddKey = new Button(c3,SWT.FLAT);
								bAddKey.setToolTipText("Add");
								Image imageAdd = Activator.getImageDescriptor("icons/arrow_right.png").createImage();
							    if (imageAdd!=null) bAddKey.setImage(imageAdd);
							    
								Button bRemoveKey = new Button(c3,SWT.FLAT);
								bRemoveKey.setToolTipText("Remove");
								Image imageRem = Activator.getImageDescriptor("icons/cancel.png").createImage();
							    if (imageRem!=null) bRemoveKey.setImage(imageRem);
							  
//------------------------------------------------------------
		Group fieldGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		fieldGroup.setText("Fields: ");
		fieldGroup.setLayout(glDet);
		fieldGroup.setLayoutData(gd);
		//------------------------------------------------------------
				fieldTable  = new Table(fieldGroup, SWT.NULL);
				fieldTable.setLayoutData(new GridData(GridData.FILL_BOTH));
				
	
		//Listener dei Bottoni
		bAddKey.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TreeItem tiSel = methodTree.getSelection()[0];
				if (!tiSel.equals(null)){
					TableItem ti = new TableItem(fieldTable, 0);
					ti.setText(tiSel.getText());
					TreeItem root = tiSel;
					TreeItem pre_root = tiSel.getParentItem();
					
					do {
						root = pre_root;
						pre_root = pre_root.getParentItem();
					} while (pre_root != null);
					
					fieldList.add(tiSel.getText().toString()+" - "+root.getText());
										
				}
				//finally
				checkPageComplete();
			}
		}); 	
					
		bRemoveKey.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = fieldTable.getSelection()[0];
				if (!tiSel.equals(null)){
					for (int i = 0; i < fieldList.size(); i++)
					{
						if (fieldList.get(i).contains(tiSel.getText().toString())){
							fieldList.remove(i);
						}
					}
					fieldTable.remove(fieldTable.getSelectionIndex());
				}
			//finally
			checkPageComplete();
			}
		}); 
		
		// Required to avoid an error in the system
		
		setControl(container);
		checkPageComplete();
	}

	private void checkPageComplete() {
		
		if (fieldList.isEmpty())
		    setPageComplete(false);
		else
			setPageComplete(true);
		
	}

	public String getComplexClassName() {
		return cBCName;
	}

	public  ArrayList<String> getFieldList() {
		return fieldList;
	}

	
}
