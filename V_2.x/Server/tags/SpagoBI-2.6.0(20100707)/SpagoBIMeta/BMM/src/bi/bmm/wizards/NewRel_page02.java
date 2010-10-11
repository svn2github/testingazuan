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

import bi.bmm.Activator;
import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;

public class NewRel_page02 extends WizardPage {
	
	private Composite container;
	
	private ArrayList<ClassInfo>ciList;

	private Table table1;

	private Composite err;

	private Label lErr;

	private Label lErr2;

	private Table table2;

	private ClassInfo cIn;

	private ClassInfo cOut;

	private Composite butt;

	private Button oneToOne;

	private Button manyToOne;

	private Button oneToMany;

	private Button manyToMany;

	
	public NewRel_page02(ArrayList<ClassInfo>ciList, BMScheme bms, ClassInfo cIn, ClassInfo cOut) {
		super("Create a Relationship");
		setTitle("Relation settings");
		setDescription("Choose which field is the relational link between your BCs and select the type of the Relationship" +
				" (1:1, 1:n , n:1, n:m)");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createRel.png");
	    if (image!=null) setImageDescriptor(image);
	    this.ciList = ciList;
	    this.cIn = cIn;
	    this.cOut = cOut;
	}

	@Override
	public void createControl(Composite parent) {
		
		 container = new Composite(parent, SWT.NULL);
		 GridLayout gl = new GridLayout();
		 gl.numColumns = 1;
		 container.setLayout(gl);
		 //------------------------------------------------------------
		 Group detailGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		 detailGroup.setText("Select the join attribute and type");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 3;
		 glDet.makeColumnsEqualWidth = true;
		 detailGroup.setLayout(glDet);
		 detailGroup.setLayoutData(gd);
		 
		 if (cIn!=null && cOut!=null)
	 	 {	
		 		//------------------------------------------------------
		 		Group g1 = new Group(detailGroup, SWT.SHADOW_ETCHED_IN);
		 		GridLayout gl1 = new GridLayout();
		 		GridData gd1 = new GridData(GridData.FILL_BOTH);
		 		g1.setText(cIn.getClassName());
		 		gl1.numColumns = 1;
		 		g1.setLayout(gl1);
		 		g1.setLayoutData(gd1);
				 		//------------------------------------------------------
				 		table1 = new Table(g1, SWT.BORDER);
				 		table1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				 		
				 		for (int i = 0; i < cIn.getKeys().size(); i++ ){
					 			TableItem ti = new TableItem(table1,0);
					 			ti.setText(cIn.getKeys().get(i)[0]+" - "+ cIn.getKeys().get(i)[1]);
					 		}
					 	for (int i = 0; i < cIn.getAttributes().size(); i++ ){
					 			TableItem ti = new TableItem(table1,0);
					 			ti.setText(cIn.getAttributes().get(i)[0]+" - "+ cIn.getAttributes().get(i)[1]);
					 		}
				 		
		 		//------------------------------------------------------
	 			
				butt = new Composite(detailGroup, SWT.NULL);
	 			gl1.numColumns = 1;
	 			butt.setLayout(gl1);
	 			butt.setLayoutData(gd1);
			 			//------------------------------------------------------
	 					oneToOne = new Button(butt, SWT.RADIO);
	 					Image image = Activator.getImageDescriptor("icons/BMUniverse/OneToOne.png").createImage();
	 					if (image!=null)oneToOne.setImage(image);
			 			manyToOne = new Button(butt, SWT.RADIO);
			 			image = Activator.getImageDescriptor("icons/BMUniverse/ManyToOne.png").createImage();
					    if (image!=null)manyToOne.setImage(image);
					    oneToMany = new Button(butt, SWT.RADIO);
			 			image = Activator.getImageDescriptor("icons/BMUniverse/OneToMany.png").createImage();
					    if (image!=null)oneToMany.setImage(image);
					    manyToMany = new Button(butt, SWT.RADIO);
			 			image = Activator.getImageDescriptor("icons/BMUniverse/ManyToMany.png").createImage();
					    if (image!=null)manyToMany.setImage(image);
						
		 		 //------------------------------------------------------
				 Group g2 = new Group(detailGroup, SWT.SHADOW_ETCHED_IN);
				 GridLayout gl2 = new GridLayout();
				 GridData gd2 = new GridData(GridData.FILL_BOTH);
				 g2.setText(cOut.getClassName());
				 gl2.numColumns = 1;
				 g2.setLayout(gl2);
				 g2.setLayoutData(gd2);
						 //------------------------------------------------------
						 table2 = new Table(g2, SWT.BORDER);
						 table2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
						 		
						 ClassInfo ci2 = cOut;
						 for (int i = 0; i < ci2.getKeys().size(); i++ ){
							 	TableItem ti = new TableItem(table2,0);
						 		ti.setText(ci2.getKeys().get(i)[0] +" - "+ ci2.getKeys().get(i)[1]);
						 }
						 for (int i = 0; i < ci2.getAttributes().size(); i++ ){
						 		TableItem ti = new TableItem(table2,0);
						 		ti.setText(ci2.getAttributes().get(i)[0]+" - "+ ci2.getAttributes().get(i)[1]);
						 }
				 		//------------------------------------------------------
							
					
		
	 table1.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (table1.getSelectionIndex()!=-1
						&& table2.getSelectionIndex()!=-1 
						&& isTypeSelected()){
					String arg1 = table1.getItem(table1.getSelectionIndex()).getText();
					String arg2 = table2.getItem(table2.getSelectionIndex()).getText();
					
					if(arg1.split(" - ")[1].equals(arg2.split(" - ")[1])){
						lErr2.setText("");
						Image imageE = Activator.getImageDescriptor("icons/validate_32.png").createImage();
					    if (imageE!=null)lErr.setImage(imageE);
						setPageComplete(true);
					}
					else{
						lErr2.setText("The two fields have not the same Data Type, please choose another one to continue.");
						Image imageE = Activator.getImageDescriptor("icons/warning_32.png").createImage();
					    if (imageE!=null)lErr.setImage(imageE);
						
					    setPageComplete(false);
					}
					
				}
				else{
					lErr2.setText("Select a Join field from each BC to continue.");
					Image imageE = Activator.getImageDescriptor("icons/warning_32.png").createImage();
				    if (imageE!=null)lErr.setImage(imageE);
					
				    setPageComplete(false);
				}
			}
		}); 
	 
	 table2.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (table1.getSelectionIndex()!=-1
						&& table2.getSelectionIndex()!=-1 
						&& isTypeSelected()){
					String arg1 = table1.getItem(table1.getSelectionIndex()).getText();
					String arg2 = table2.getItem(table2.getSelectionIndex()).getText();
					
					if(arg1.split(" - ")[1].equals(arg2.split(" - ")[1])){
						lErr2.setText("");
						Image imageE = Activator.getImageDescriptor("icons/validate_32.png").createImage();
					    if (imageE!=null)lErr.setImage(imageE);
						setPageComplete(true);
					}
					else{
						lErr2.setText("The two fields have not the same Data Type, please choose another one to continue.");
						Image imageE = Activator.getImageDescriptor("icons/warning_32.png").createImage();
					    if (imageE!=null)lErr.setImage(imageE);
						
					    setPageComplete(false);
					}
					
				}
				else{
					lErr2.setText("Select a Join field from each BC to continue.");
					Image imageE = Activator.getImageDescriptor("icons/warning_32.png").createImage();
				    if (imageE!=null)lErr.setImage(imageE);
					
				    setPageComplete(false);
				}
			}		
		}); 
	 	
	 	}
	//------------------------------------------------------------
		err = new Composite(detailGroup, SWT.NULL);
		GridLayout glerr = new GridLayout();
		glerr.numColumns = 2;
		err.setLayout(glerr);
		
		lErr = new Label(err, SWT.NULL);
		lErr2 = new Label(err, SWT.NULL);
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}
	 


	private boolean isTypeSelected() {
		if (manyToMany.getSelection()
				|| manyToOne.getSelection()
				|| oneToMany.getSelection()
				|| oneToOne.getSelection())
		return true;
		return false;
	}
	
	public int getJoinType(){
		if(manyToMany.getSelection())	return BMScheme.MANY_TO_MANY;
		if(manyToOne.getSelection())	return BMScheme.MANY_TO_ONE;
		if(oneToMany.getSelection())	return BMScheme.ONE_TO_MANY;
		if(oneToOne.getSelection())		return BMScheme.ONE_TO_ONE;
		return -1;
	}
 
	
	public ArrayList<ClassInfo> getCiList(){
		return this.ciList;
	}
	
	public String[] getJoinIn(){
		return table1.getItem(table1.getSelectionIndex()).getText().split(" - ");
	}
	
	public String[] getJoinOut(){
		return table2.getItem(table2.getSelectionIndex()).getText().split(" - ");
	}
	
	
}
