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


import java.util.HashMap;
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
import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;

public class SetBCDetails_page02 extends WizardPage {
	
	private Composite container;

	private String table;
	private ClassInfo ciOut;
	private String tab;
	private String arg;
	private String argIn;
	private Composite butt;
	private Button oneToOne;
	private Button oneToMany;

	private Button none;
	
	public SetBCDetails_page02(String table) {
		super("Set BC details Fast Mode");
		setTitle("BC Details Fast mode");
		setDescription("Automated Direct Relationships descover...");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createBC.png");
	    if (image!=null) setImageDescriptor(image);
	    
	    this.table = table;
	   
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
		 detailGroup.setText("Relationships Descovered");
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
		
		if (none.getSelection()
				|| oneToMany.getSelection()
				|| oneToOne.getSelection())
		setPageComplete(true);
		else setPageComplete(false);
	}

	private void descoverRelationships(Group detailGroup) {
		//recupero la view
		ELUniverseView elUniverse = (ELUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.eluniverse");
		//
		HashMap<String, String[]> zip = elUniverse.getFK();
		String[] res = zip.get(table);
		
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
		
		
	}

	public String[] getTabArgOut(){
		return new String[]{tab,arg};
	}
	
	public String[] getTabArgIn(){
		return new String[]{table,argIn};
	}
	
	public ClassInfo getCiOut(){
		return ciOut;
	}
	
	public int getRelType(){
		if (ciOut != null){
			
			if(none.getSelection())			return -1;
			if(oneToMany.getSelection())	return BMScheme.ONE_TO_MANY;
			if(oneToOne.getSelection())		return BMScheme.ONE_TO_ONE;
		}
		return -1;
	}

}
