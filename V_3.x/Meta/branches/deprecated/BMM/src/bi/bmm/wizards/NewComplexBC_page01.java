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
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import bi.bmm.Activator;
import bi.bmm.BMUniverseView;
public class NewComplexBC_page01 extends WizardPage {
	
	private Composite container;
	private Text cClassName;
	private Table inheritanceTable;
	private ArrayList<String> inhBcList;
	private Table bcTable;
	
	public NewComplexBC_page01() {
		super("Create a new Complex BC");
		setTitle("BC Creation Mode");
		setDescription("This wizard drives you to crate a Complex Business Class in your BM project.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createComplexBC.png");
	    if (image!=null) setImageDescriptor(image);
	    inhBcList = new ArrayList<String>();
	   
	}

	@Override
	public void createControl(Composite parent) {
		
		
		container = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.makeColumnsEqualWidth = true;
		gridLayout.numColumns = 1; 
		container.setLayout(gridLayout); 
		
		//gruppo per i dettagli sul nome
		Group name = new Group(container, SWT.SHADOW_ETCHED_IN);
		name.setText("Name Detail: ");
		GridLayout gLayout = new GridLayout();
		GridData gd = new GridData(GridData.FILL_BOTH);
		gLayout.numColumns = 2;
		name.setLayout(gLayout);
		name.setLayoutData(gd);
			//label
			Label lClassName =new Label(name, SWT.NULL); 
			lClassName.setText ("Complex Class Name: ");
			//nome
			cClassName = new Text(name, SWT.BORDER);
			cClassName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		//gruppo per scegliere da quali BC deriva
		Group derive = new Group(container, SWT.SHADOW_ETCHED_IN);
		derive.setText("Inheritance Detail: ");
		GridLayout gL = new GridLayout();
		gL.makeColumnsEqualWidth = true;
		gL.numColumns = 3;
		derive.setLayout(gL);
		derive.setLayoutData(gd);
				//label
				Label lClassInh =new Label(derive, SWT.NULL); 
				lClassInh.setText ("Simple Business Classes: ");
				Label lClassInh2 =new Label(derive, SWT.NULL); 
				lClassInh2.setText ("");
				Label lClassInh3 =new Label(derive, SWT.NULL); 
				lClassInh3.setText ("Business Classes Inher");
				//tabella delle BC presenti nel BM
					//recupero la vista
					BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().findView("bi.bmm.views.bme.bmuniverse");

					bcTable = new Table(derive, SWT.BORDER);
					bcTable.setLayoutData(new GridData(GridData.FILL_BOTH));
					
					if (!bmUniverse.bcList.isEmpty()){
						for(int i = 0; i < bmUniverse.bcList.size();i++){
							TableItem ti = new TableItem(bcTable,0);
							ti.setText(bmUniverse.bcList.get(i).getClassName());
						}
					}
				//bottoni per inserire e togliere
					Composite c3 = new Composite(derive, SWT.NULL);
					GridLayout gl3 = new GridLayout();
					gl3.numColumns = 2;
					c3.setLayout(gl3);
					c3.setLayoutData(new GridData(GridData.CENTER));
									//------------------------------------------------------
									Button bAddKey = new Button(c3,SWT.FLAT);
									bAddKey.setToolTipText("Remove");
									Image imageAdd = Activator.getImageDescriptor("icons/arrow_left.png").createImage();
								    if (imageAdd!=null) bAddKey.setImage(imageAdd);
								    
									Button bRemoveKey = new Button(c3,SWT.FLAT);
									bRemoveKey.setToolTipText("Add");
									Image imageRem = Activator.getImageDescriptor("icons/arrow_right.png").createImage();
								    if (imageRem!=null) bRemoveKey.setImage(imageRem);
								    
								    
				//tabella delle BC imparentate con la CBC
					
						inheritanceTable = new Table(derive, SWT.BORDER);
						inheritanceTable.setLayoutData(new GridData(GridData.FILL_BOTH));
						if (!inhBcList.isEmpty()){
							for(int i = 0; i < inhBcList.size();i++){
								TableItem ti = new TableItem(inheritanceTable,0);
								ti.setText(inhBcList.get(i));
							}
						}
					
		//Listener al Text
		cClassName.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				checkPageComplete();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				checkPageComplete();
			}
		});
		//Listener dei Bottoni
		bAddKey.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = inheritanceTable.getSelection()[0];
				if (!tiSel.equals(null)){
					TableItem ti = new TableItem(bcTable, 0);
					ti.setText(tiSel.getText());
					
					inhBcList.remove(tiSel.getText().toString());
										
					inheritanceTable.remove(inheritanceTable.getSelectionIndex());
					
				}
				//finally
				checkPageComplete();
			}
		}); 	
					
		bRemoveKey.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = bcTable.getSelection()[0];
				if (!tiSel.equals(null)){
					TableItem ti = new TableItem(inheritanceTable, 0);
					ti.setText(tiSel.getText());
					
					inhBcList.add(tiSel.getText().toString());

					bcTable.remove(bcTable.getSelectionIndex());
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
			
		if (!inhBcList.isEmpty() && !cClassName.getText().equals("")){
			setPageComplete(true);
		}
		else{
			setPageComplete(false);
		}
		
	}
	
	public ArrayList<String> getInhBcList(){
		return this.inhBcList;
	}
	
	public String getComplexClassName(){
		return this.cClassName.getText().toString();
	}
	
}
