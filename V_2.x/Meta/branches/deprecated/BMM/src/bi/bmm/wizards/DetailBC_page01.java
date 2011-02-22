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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bi.bmm.Activator;
import bi.bmm.util.ClassInfo;

public class DetailBC_page01 extends WizardPage{
	
	private ClassInfo ci;
	
	private Composite container;
	private Table keys;
	private Table attributes;
	private Text name;

	private Composite err;
	private Label lErr2;
	private Label lErr;
	
	public DetailBC_page01(ClassInfo ci) {
		super("BC Details");
		setTitle("Modify BC Details");
		setDescription("Here you can modify BC details.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/modifyBC.png");
	    if (image!=null) setImageDescriptor(image);
	    this.ci = ci;
	   
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
		 detailGroup.setText("Name");
		 GridLayout glDet = new GridLayout();
		 GridData gd = new GridData(GridData.FILL_BOTH);
		 glDet.numColumns = 2;
		 glDet.makeColumnsEqualWidth = true;
		 detailGroup.setLayout(glDet);
		 detailGroup.setLayoutData(gd);
		 		//------------------------------------------------------
		 		Label l1 = new Label(detailGroup,SWT.NULL);
		 		l1.setText("BC Name: ");
		 		
		 		name = new Text(detailGroup, SWT.NULL);
		 		name.setTextLimit(25);
		 		name.setText(ci.getClassName());
		 		//------------------------------------------------------
		 //------------------------------------------------------------
		 Group keysGroup = new Group(container, SWT.SHADOW_ETCHED_IN);
		 keysGroup.setText("Keys - Attributes");
		 GridLayout glKeys = new GridLayout();
		 GridData gdK = new GridData(GridData.FILL_BOTH);
		 glKeys.numColumns = 3;
		 glKeys.makeColumnsEqualWidth = true;
		 keysGroup.setLayout(glKeys);
		 keysGroup.setLayoutData(gdK);
				 		//------------------------------------------------------
						Composite c1 = new Composite(keysGroup, SWT.NULL);
						GridLayout gl1 = new GridLayout();
						GridData gd1 = new GridData(GridData.FILL_BOTH);
						gl1.numColumns = 1;
						c1.setLayout(gl1);
						c1.setLayoutData(gd1);
								//------------------------------------------------------
								Label l2 = new Label(c1,SWT.NULL);
						 		l2.setText("BC Keys: ");
						 		
						 		keys = new Table(c1, SWT.BORDER);
						 		keys.setLayoutData(gd1);
						 		
					    //------------------------------------------------------
						Composite c3 = new Composite(keysGroup, SWT.NULL);
						GridLayout gl3 = new GridLayout();
						gl3.numColumns = 4;
						c3.setLayout(gl3);
										//------------------------------------------------------
										Button bAddKey = new Button(c3,SWT.FLAT);
										bAddKey.setToolTipText("Add a key to BC");
										Image imageAdd = Activator.getImageDescriptor("icons/arrow_left.png").createImage();
									    if (imageAdd!=null) bAddKey.setImage(imageAdd);
									    
										Button bRemoveKey = new Button(c3,SWT.FLAT);
										bRemoveKey.setToolTipText("Remove a key from BC");
										Image imageRem = Activator.getImageDescriptor("icons/arrow_right.png").createImage();
									    if (imageRem!=null) bRemoveKey.setImage(imageRem);
									    
									    Button bCancelKey = new Button(c3,SWT.FLAT);
									    bCancelKey.setToolTipText("Cancel an attribute from BC");
										Image imageCanc = Activator.getImageDescriptor("icons/cancel.png").createImage();
									    if (imageCanc!=null) bCancelKey.setImage(imageCanc);
									    
									    Button bReloadKey = new Button(c3,SWT.FLAT);
									    bReloadKey.setToolTipText("Reload all attributes into BC");
									    Image imageReload = Activator.getImageDescriptor("icons/reload.png").createImage();
									    if (imageReload!=null) bReloadKey.setImage(imageReload);
									    
										
						//------------------------------------------------------
						Composite c2 = new Composite(keysGroup, SWT.NULL);
						GridLayout gl2 = new GridLayout();
						GridData gd2 = new GridData(GridData.FILL_BOTH);
							
						gl2.numColumns = 1;
						c2.setLayout(gl2);
						c2.setLayoutData(gd2);
								//------------------------------------------------------
								Label l3 = new Label(c2,SWT.NULL);
								l3.setText("BC Attributes: ");
								
								attributes = new Table(c2, SWT.BORDER);
						 		attributes.setLayoutData(gd2);
						//inserisco le keys plausibili
						addItem();
				 		
		//aggiungo i Listener al button	 		
		bAddKey.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = attributes.getSelection()[0];
				if (!tiSel.equals(null)){
					TableItem ti = new TableItem(keys, 0);
					ti.setText(tiSel.getText());
					attributes.remove(attributes.getSelectionIndex());
				}
				//finally
				checkPageComplete();
			}
		}); 	
		
		bRemoveKey.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = keys.getSelection()[0];
				if (!tiSel.equals(null)){
					TableItem ti = new TableItem(attributes, 0);
					ti.setText(tiSel.getText());
					keys.remove(keys.getSelectionIndex());
				}
				//finally
				checkPageComplete();
			}
		}); 
		
		bCancelKey.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = attributes.getSelection()[0];
				if (!tiSel.equals(null)){
					attributes.remove(attributes.getSelectionIndex());
				}
				//finally
				checkPageComplete();
			}
		}); 
		
		bReloadKey.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if (MessageDialog.openConfirm(new Shell(), "Reload Attributes","This command will reload the initial configuration, "+
						"are you sure?")){
				addItem();	
				
				//finally
				checkPageComplete();
				}
			}
		}); 
		
		
		//------------------------------------------------------------
		err = new Composite(container, SWT.NULL);
		GridLayout glerr = new GridLayout();
		glerr.numColumns = 2;
		err.setLayout(glerr);
		
		lErr = new Label(err, SWT.NULL);
		lErr2 = new Label(err, SWT.NULL);
		
		// Required to avoid an error in the system
		setControl(container);
		checkPageComplete();
	}

	private void checkPageComplete() {
		
				
		if(keys.getItemCount()!=0){
			setPageComplete(true);
			lErr2.setText("");
			Image imageE = Activator.getImageDescriptor("icons/validate_32.png").createImage();
		    if (imageE!=null)lErr.setImage(imageE);
		}
		else{			
			lErr2.setText("This BC has not a Key, please set one to continue.");
			Image imageE = Activator.getImageDescriptor("icons/warning_32.png").createImage();
		    if (imageE!=null)lErr.setImage(imageE);
			
		    setPageComplete(false);
		}
	}

	private void addItem() {
			
				keys.removeAll();
				attributes.removeAll();
			
			    
				for (int i =0; i<ci.getKeys().size(); i++){
						TableItem ti = new TableItem(keys, 0);
						ti.setText(ci.getKeys().get(i)[0]+" - "+ci.getKeys().get(i)[1]);
				}
				for (int i =0; i<ci.getAttributes().size(); i++){
					TableItem ti = new TableItem(attributes, 0);
					ti.setText(ci.getAttributes().get(i)[0]+" - "+ci.getAttributes().get(i)[1]);
			    }
	}
	
	public Table getKeys(){
		return this.keys;
	}
	
	public Table getAttributes(){
		return this.attributes;
	}

	public String getClassName(){
		return this.name.getText();
	}
}