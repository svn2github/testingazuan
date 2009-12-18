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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;

import bi.bmm.Activator;
import bi.bmm.util.ClassInfo;

public class NewRel_page01 extends WizardPage {
	
	private Composite container;
	
	private ArrayList<ClassInfo>ciList;
	private Combo combo1,combo2;
	
	public NewRel_page01(ArrayList<ClassInfo>ciList) {
		super("Create a Relationship");
		setTitle("Create a new Relationship between two BC");
		setDescription("This wizard drives you to create a new relationship between different Business" +
				"class.\n\nPlease select the BCs you want to be related.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createRel.png");
	    if (image!=null) setImageDescriptor(image);
	    
	    this.ciList = ciList;
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
		 detailGroup.setText("Business Classes to be related");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 2;
		 glDet.makeColumnsEqualWidth = true;
		 detailGroup.setLayout(glDet);
		 detailGroup.setLayoutData(gd);
		 		//------------------------------------------------------
		 		combo1 = new Combo(detailGroup, SWT.BORDER);
		 		combo1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		 		for (int i = 0; i < ciList.size(); i++ ){
		 			combo1.add(ciList.get(i).getClassName());
		 		}
		 		//------------------------------------------------------
		 		//------------------------------------------------------
		 		combo2 = new Combo(detailGroup, SWT.BORDER);
		 		combo2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		 		for (int i = 0; i < ciList.size(); i++ ){
		 			combo2.add(ciList.get(i).getClassName());
		 		}
		 		//------------------------------------------------------
										
					
		
	 combo1.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				
				combo2.removeAll();
				
				for (int i = 0; i < ciList.size(); i++ ){
		 			combo2.add(ciList.get(i).getClassName());
		 		}
				
				combo2.remove(combo1.getSelectionIndex());
				
				if (combo2.getSelectionIndex() != -1
						&& !combo2.getText().equals(combo1.getText())){
					setPageComplete(true);
					}
				else setPageComplete(false);
				
			}
		}); 
	 
	 combo2.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				
				if (combo1.getSelectionIndex() != -1
						&& !combo2.getText().equals(combo1.getText())){
					setPageComplete(true);
				}
				else setPageComplete(false);
			}
		}); 
		
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	public ArrayList<ClassInfo> getCiList(){
		return this.ciList;
	}
	
	public ClassInfo getClassIn(){
		for (int i = 0; i < ciList.size(); i++){
			if (ciList.get(i).getClassName().equals(this.combo1.getText()))
				{
				 return ciList.get(i);
				}
				
		}
		
		return null;
	}
	
	public ClassInfo getClassOut(){
		for (int i = 0; i < ciList.size(); i++){
			if (ciList.get(i).getClassName().equals(this.combo2.getText()))
				return ciList.get(i);
		}
		
		return null;
	}
	
}
