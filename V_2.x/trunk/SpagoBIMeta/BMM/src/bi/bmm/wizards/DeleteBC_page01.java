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


import java.io.File;
import java.util.ArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
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
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import bi.bmm.Activator;
import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;

public class DeleteBC_page01 extends WizardPage {
	
	private Composite container;
	
	private ArrayList<ClassInfo>ciList;
	private Table tb;

	private ClassInfo removed;

	private BMScheme bms;
	
	public DeleteBC_page01(ArrayList<ClassInfo>ciList, BMScheme bms) {
		super("Delete a BC");
		setTitle("Delete a BC from BM");
		setDescription("This wizard drives you to quickly remove a Business Class " +
				"from your Business Model");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/removeBC.png");
	    if (image!=null) setImageDescriptor(image);
	    
	    this.ciList = ciList;
	    this.removed = null;
	    this.bms = bms;
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
		 detailGroup.setText("Business Classes in your BM");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 1;
		 glDet.makeColumnsEqualWidth = true;
		 detailGroup.setLayout(glDet);
		 detailGroup.setLayoutData(gd);
		 		//------------------------------------------------------
		 		tb = new Table(detailGroup, SWT.BORDER);
		 		tb.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		 		for (int i = 0; i < ciList.size(); i++ ){
		 			TableItem ti = new TableItem(tb, 0);
		 			ti.setText(ciList.get(i).getClassName());
		 		}
		 		//------------------------------------------------------
		 //------------------------------------------------------------
		 Group buttGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		 buttGroup.setText("Remove");
		 GridLayout glKeys = new GridLayout();
		 GridData gdK = new GridData(GridData.FILL_BOTH);
		 glKeys.numColumns = 5;
		 glKeys.makeColumnsEqualWidth = true;
		 buttGroup.setLayout(glKeys);
		 buttGroup.setLayoutData(gdK);
				 		//------------------------------------------------------
						Composite c1 = new Composite(buttGroup, SWT.NULL);
						GridLayout gl1 = new GridLayout();
						GridData gd1 = new GridData(GridData.FILL_BOTH);
						gl1.numColumns = 1;
						c1.setLayout(gl1);
						c1.setLayoutData(gd1);
								//------------------------------------------------------
								Button bCancelKey = new Button(c1,SWT.FLAT);
								bCancelKey.setToolTipText("Cancel an attribute from BC");
								Image imageCanc = Activator.getImageDescriptor("icons/cancel.png").createImage();
								if (imageCanc!=null) bCancelKey.setImage(imageCanc);
										
					
		
		bCancelKey.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = tb.getSelection()[0];
				if (!tiSel.equals(null)){
					//chiedo conferma 
					if (MessageDialog.openConfirm(new Shell(), "Delete BC","Would you delete the BC "+
							ciList.get(tb.getSelectionIndex()).getClassName()+" ?")){
					
						removed = ciList.remove(tb.getSelectionIndex());
						tb.remove(tb.getSelectionIndex());

						//lo rimuovo dallo schema
						bms.removeClassFigure(removed.getFigure());
						//lo rimuovo dalla lista
						//ciList.remove(one.getCiRemoved());
						//rimuovo i suoi file
						File f = new File(removed.getClassPathInfo());
						if (f.exists()){
							f.delete();
						}
					}
					setPageComplete(true);
				}
			}
		}); 
		
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	public ArrayList<ClassInfo> getCiList(){
		return this.ciList;
	}
	
	public ClassInfo getCiRemoved(){
		return this.removed;
	}
}
