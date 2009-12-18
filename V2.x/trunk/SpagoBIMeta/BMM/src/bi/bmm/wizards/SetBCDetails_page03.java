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
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;

import bi.bmm.Activator;
import bi.bmm.BMUniverseView;
import bi.bmm.ELUniverseView;
import bi.bmm.util.ClassInfo;

public class SetBCDetails_page03 extends WizardPage {
	
	private Composite container;

	private String table;
	private ArrayList<ClassInfo> ciOut;
	private Composite butt;
	private ArrayList<Button> manyToMany;
	private ArrayList<Button>  manyToOne;
	private ArrayList<Button>  none;

	private ArrayList<String[]> tab_arg_out;
	private ArrayList<String[]> tab_arg_in;

	
	public SetBCDetails_page03(String table) {
		super("Set BC details Fast Mode");
		setTitle("BC Details Fast mode");
		setDescription("Automated Indirect Relationships descover...");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createBC.png");
	    if (image!=null) setImageDescriptor(image);
	    
	    this.table = table;
	    this.manyToMany = new ArrayList<Button>();
	    this.manyToOne = new ArrayList<Button>();
	    this.none = new ArrayList<Button>();
	    this.ciOut = new ArrayList<ClassInfo>();
	    this.tab_arg_in = new ArrayList<String[]>();
	    this.tab_arg_out = new ArrayList<String[]>();
	   
	}

	@Override
	public void createControl(Composite parent) {
		
		 container = new Composite(parent, SWT.NULL);
		 GridLayout gl = new GridLayout();
		 gl.numColumns = 1;
		 gl.makeColumnsEqualWidth = true;
		 container.setLayout(gl);
		 
		 //------------------------------------------------------------
		 Group detailGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		 detailGroup.setText("Indirect Relationships Descovered");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 3;
		 glDet.makeColumnsEqualWidth = true;
		 detailGroup.setLayout(glDet);
		 detailGroup.setLayoutData(gd);
		 		//------------------------------------------------------
		 		descoverRelationships(detailGroup);
		 		
		 		//------------------------------------------------------

		
		// Required to avoid an error in the system
		setControl(container);
		checkPageComplete();
	}


	private void checkPageComplete() {
		
		if (ciOut == null){
			setPageComplete(true);
			return;
		}
		for(int i =0; i<none.size(); i++)
		{
		if (none.get(i).getSelection()
				|| manyToMany.get(i).getSelection()
				|| manyToOne.get(i).getSelection())
		setPageComplete(true);
		else setPageComplete(false);
		}
		
		setPageComplete(true);
	}

	private void descoverRelationships(Group detailGroup) {
		//recupero la view
		ELUniverseView elUniverse = (ELUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.eluniverse");
		//
		HashMap<String, String[]> zip = elUniverse.getFK();
		
		//cerco quali hanno relazioni con la tabella
		Iterator<String> zipIterator = zip.keySet().iterator();
		while(zipIterator.hasNext()){
			String key = zipIterator.next();
			String[] res = zip.get(key);
			if (res[1].toString().equals(table.toString())){
				System.out.println("Related whit "+key+" -> "+res[0]+" "+res[2]);
				//vedo se esiste una BC che contiene mapping a cities.city_id se c'è
				//la collego a questa e faccio scegliere la relazione
				BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
				//per ogni elemento della bc
				for(int i = 0; i< bmUniverse.bcList.size(); i++){
					//ottengo la classInfo e vedo se è entità con 
					//table = a tab e con arg = a arg
					ClassInfo ci = bmUniverse.bcList.get(i);
					if (ci.isTarget(key,res[0])){
						ciOut.add(ci);
						//segnalo che ho trovato un fk
						
						//UPPERLINE
						Label l0 = new Label(detailGroup,SWT.NULL);
						
						Image imageRel = Activator.getImageDescriptor("icons/wizards/createREL.png").createImage();
					    if (imageRel!=null) l0.setImage(imageRel);
				 		
				 		
				 		Label l1 = new Label(detailGroup, SWT.NULL);
				 		l1.setText("Find indirect relationships: ");
				 		
				 		Label l2 = new Label(detailGroup, SWT.NULL);
				 		l2.setText("");
				 		
					 		//BOTTOMLINE
				 		Label l3 = new Label(detailGroup, SWT.NULL);
				 		l3.setText("");
				 		
				 		Group fkGroup = new Group(detailGroup, SWT.SHADOW_ETCHED_IN);
						fkGroup.setText("Relationship proposed: ");
						fkGroup.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
						GridLayout glDet = new GridLayout();
						GridData gd = new GridData(GridData.FILL_BOTH);
						glDet.numColumns = 3;
						glDet.makeColumnsEqualWidth = true;
						fkGroup.setLayout(glDet);
						fkGroup.setLayoutData(gd);
				 		
				 			Label lIn = new Label(fkGroup, SWT.NULL);
					 		lIn.setText(res[1] +"."+ res[2]);
					 		tab_arg_in.add(new String[]{res[1],res[2]});
					 		lIn.setAlignment(SWT.CENTER);
					 		
					 		butt = new Composite(fkGroup, SWT.NULL);
					 		GridLayout gl1 = new GridLayout();
					 		GridData gd1 = new GridData(GridData.FILL_BOTH);
				 			gl1.numColumns = 1;
				 			butt.setLayout(gl1);
				 			butt.setLayoutData(gd1);
				 			butt.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
						 			//------------------------------------------------------
				 					Button nn = new Button(butt, SWT.RADIO);
				 					Image image = Activator.getImageDescriptor("icons/BMUniverse/ManyToMany16x16.png").createImage();
				 					if (image!=null)nn.setImage(image);
				 					manyToMany.add(nn);
						 			
								    Button n1 = new Button(butt, SWT.RADIO);
						 			image = Activator.getImageDescriptor("icons/BMUniverse/ManyToOne16x16.png").createImage();
								    if (image!=null)n1.setImage(image);
								    manyToOne.add(n1);
								    
								    Button x = new Button(butt, SWT.RADIO);
						 			image = Activator.getImageDescriptor("icons/BMUniverse/None16x16.png").createImage();
								    if (image!=null)x.setImage(image);
								    none.add(x);
					 		
					 		Label lOut = new Label(fkGroup, SWT.NULL);
					 		lOut.setText(key +"."+ res[0]);
					 		tab_arg_out.add(new String[]{key,res[0]});
					 		lOut.setAlignment(SWT.CENTER);
					 		
					 		//Listener ai bottoni
					 		nn.addListener(SWT.Selection, new Listener() {
					 			
					 			@Override
					 			public void handleEvent(Event event) {
					 				checkPageComplete();	
					 			}
					 		});
					 		n1.addListener(SWT.Selection, new Listener() {
					 			
					 			@Override
					 			public void handleEvent(Event event) {
					 				checkPageComplete();	
					 			}
					 		});
					 		x.addListener(SWT.Selection, new Listener() {
					 			
					 			@Override
					 			public void handleEvent(Event event) {
					 				checkPageComplete();	
					 			}
					 		});
						}
				}
			}
		}
		
		/*
		if (res!=null){
		    
			argIn=zip.get(table)[0];
			tab=zip.get(table)[1];
			arg=zip.get(table)[2];
		
		
			//recupero l'altra view
			BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
			//per ogni elemento della bc
			ciOut=null;
			
			for(int i = 0; i< bmUniverse.bcList.size(); i++){
				//ottengo la classInfo e vedo se è entità con 
				//table = a tab e con arg = a arg
				ClassInfo ci = bmUniverse.bcList.get(i);
				if (ci.isTarget(tab,arg)){
					ciOut = ci;
					break;
				}
			}
			if (ciOut != null){ //segnalo che ho trovato un fk
				
				//UPPERLINE
				Label l0 = new Label(detailGroup,SWT.NULL);
				
				Image imageRel = Activator.getImageDescriptor("icons/wizards/createREL.png").createImage();
			    if (imageRel!=null) l0.setImage(imageRel);
		 		
		 		
		 		Label l1 = new Label(detailGroup, SWT.NULL);
		 		l1.setText("Trovata una relazione: ");
		 		
		 		Label l2 = new Label(detailGroup, SWT.NULL);
		 		l2.setText("");
		 		
			 		//BOTTOMLINE
		 		Label l3 = new Label(detailGroup, SWT.NULL);
		 		l3.setText("");
		 		
		 		Group fkGroup = new Group(detailGroup, SWT.SHADOW_ETCHED_IN);
				fkGroup.setText("Relationship proposed: ");
				fkGroup.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				GridLayout glDet = new GridLayout();
				GridData gd = new GridData(GridData.FILL_BOTH);
				glDet.numColumns = 3;
				glDet.makeColumnsEqualWidth = true;
				fkGroup.setLayout(glDet);
				fkGroup.setLayoutData(gd);
		 		
		 			Label lIn = new Label(fkGroup, SWT.NULL);
			 		lIn.setText(table +"."+ argIn);
			 		lIn.setAlignment(SWT.CENTER);
			 		
			 		butt = new Composite(fkGroup, SWT.NULL);
			 		GridLayout gl1 = new GridLayout();
			 		GridData gd1 = new GridData(GridData.FILL_BOTH);
		 			gl1.numColumns = 1;
		 			butt.setLayout(gl1);
		 			butt.setLayoutData(gd1);
		 			butt.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WHITE));
				 			//------------------------------------------------------
		 					oneToOne = new Button(butt, SWT.RADIO);
		 					Image image = Activator.getImageDescriptor("icons/BMUniverse/OneToOne16x16.png").createImage();
		 					if (image!=null)oneToOne.setImage(image);
				 			
						    oneToMany = new Button(butt, SWT.RADIO);
				 			image = Activator.getImageDescriptor("icons/BMUniverse/OneToMany16x16.png").createImage();
						    if (image!=null)oneToMany.setImage(image);
						    
						    none = new Button(butt, SWT.RADIO);
				 			image = Activator.getImageDescriptor("icons/BMUniverse/None16x16.png").createImage();
						    if (image!=null)none.setImage(image);
			 		
			 		Label lOut = new Label(fkGroup, SWT.NULL);
			 		lOut.setText(tab +"."+ arg);
			 		lOut.setAlignment(SWT.CENTER);
			 		
			 		//Listener ai bottoni
			 		oneToOne.addListener(SWT.Selection, new Listener() {
			 			
			 			@Override
			 			public void handleEvent(Event event) {
			 				checkPageComplete();	
			 			}
			 		});
			 		oneToMany.addListener(SWT.Selection, new Listener() {
			 			
			 			@Override
			 			public void handleEvent(Event event) {
			 				checkPageComplete();	
			 			}
			 		});
			 		none.addListener(SWT.Selection, new Listener() {
			 			
			 			@Override
			 			public void handleEvent(Event event) {
			 				checkPageComplete();	
			 			}
			 		});
				}
			
			
		}
		*/
		
	}

	public ArrayList<String[]> getTabArgOut(){
		return tab_arg_out;
	}
	
	public ArrayList<String[]> getTabArgIn(){
		return tab_arg_in;
	}
	
	public ArrayList<ClassInfo> getCiOut(){
		return ciOut;
	}
	
	public ArrayList<String> getRelType(){
		ArrayList<String> res =new ArrayList<String>(); 
		for (int i = 0; i< ciOut.size(); i++){
			if(none.get(i).getSelection())			res.add("NONE");
			if(manyToMany.get(i).getSelection())	res.add("MANY_TO_MANY");
			if(manyToOne.get(i).getSelection())		res.add("MANY_TO_ONE");
		}
		return res;	
	}

}
