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


import java.util.Vector;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import bi.bmm.Activator;
import bi.bmm.util.DBConnection;
public class NewBC_page01 extends WizardPage {
	
	private Composite container;
	private Button[] radios;
	private DBConnection conn;
	private boolean tableSelect;
	private Table tb;
	
	public NewBC_page01(boolean tableSelect,DBConnection conn) {
		super("Create a new BC");
		setTitle("BC Creation Mode");
		setDescription("This wizard drives you to crate a new Business Class in your BM project," +
				" please choose a mode to create your BC.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/createBC.png");
	    if (image!=null) setImageDescriptor(image);
	   
	    this.conn=conn;
	    this.tableSelect=tableSelect;
	    this.tb = null;
	}

	@Override
	public void createControl(Composite parent) {
		
		container = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 2; 
		
		container.setLayout(gridLayout); 
		
		
	
		//Table chooser
		if(!tableSelect && conn.Start()){
			Label lbl = new Label(container, SWT.NULL);
			lbl.setText("Select a Table from schema: ");	
			tb = new Table(container, SWT.BORDER);
			//MC: Aggiunto LayoutData
			tb.setLayoutData(new GridData( GridData.GRAB_VERTICAL |
					GridData.GRAB_HORIZONTAL | GridData.FILL_BOTH ));
			
			Vector<String[]> v =conn.getTables();
			for (int i = 0; i < v.size(); i++){
				TableItem ti = new TableItem(tb, SWT.NONE);
				ti.setText(v.get(i)[0]);
			}
			conn.Stop();
		}
		
		
		//--------------------------------------------
		
		radios = new Button[2];

		Label lb0img = new Label(container, SWT.NULL);
		lb0img.setText("");
		Image imageFast = Activator.getImageDescriptor("icons/wizards/fastBC.png").createImage();
		if (imageFast!=null) lb0img.setImage(imageFast);
		
	    Composite c1 = new Composite(container, SWT.NULL);
		GridLayout gl1 = new GridLayout(); 
		gl1.numColumns = 1; 
		c1.setLayout(gl1); 
		
		Label lb1img = new Label(container, SWT.NULL);
		lb1img.setText("");
		Image imageNorm = Activator.getImageDescriptor("icons/wizards/normBC.png").createImage();
		if (imageNorm!=null) lb1img.setImage(imageNorm);
		
		Composite c2 = new Composite(container, SWT.NULL);
		GridLayout gl2 = new GridLayout(); 
		gl2.numColumns = 1; 
		c2.setLayout(gl2); 
		
		 final Composite [] composites = new Composite [] {c1, c2};
		  Listener radioGroup = new Listener () {
		    public void handleEvent (Event event) {
		      for (int i=0; i<composites.length; i++) {
		        Composite composite = composites [i];
		        Control [] children = composite.getChildren ();
		        for (int j=0; j<children.length; j++) {
		          Control child = children [j];
		          if (child instanceof Button) {
		            Button button = (Button) child;
		            if ((button.getStyle () & SWT.RADIO) != 0) {
		            	button.setSelection (false);
		            }
		          }
		        }
		      }
		      Button button = (Button) event.widget;
		      button.setSelection (true);
		    }
		  };
		  
		//--------------------------------------------
		
			
	    radios[0] = new Button(c1, SWT.RADIO);
	    radios[0].setSelection(true);
	    radios[0].setText("Fast Business Class Creation:");
	    radios[0].setFont(new Font (container.getDisplay(), "Arial", 14, SWT.BOLD & SWT.ITALIC));
	    radios[0].setBounds(10, 5, 75, 30);
	    radios[0].addListener (SWT.Selection, radioGroup);
	    
	    Label lb0 = new Label(c1, SWT.NULL);
	    lb0.setText("provide you a BC from the selected table quickly.\nBusiness Element will be" +
	    		" created automatically.\nYou shall only choose keys or mapping.\n");
	   
	    //---------------------------------------------
	    
		
	    radios[1] = new Button(c2, SWT.RADIO);
	    radios[1].setText("Expert Business Class Creation:");
	    radios[1].setFont(new Font (container.getDisplay(), "Arial", 14, SWT.BOLD & SWT.ITALIC));
		radios[1].setBounds(10, 30, 75, 30);
		radios[1].addListener (SWT.Selection, radioGroup);
			 
	    Label lb1 = new Label(c2, SWT.NULL);
	    lb1.setText("Create your BC from the selected table choosing\nall mapping parameters as well as Business Elements.\n");
		
	    
	    //table listener
	    if(!tableSelect){
		    tb.addListener(SWT.MouseDown, new Listener () {
				public void handleEvent (Event event) {
					Point point = new Point (event.x, event.y);
					TableItem item = tb.getItem(point);
					if (item != null) {
						checkPageComplete();
					}
				}
		    });
	    }
	    /*
		.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (!text1.getText().equals("") && !text2.getText().equals("")){
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		*/
	    
	    
		
		// Required to avoid an error in the system
		setControl(container);
		checkPageComplete();

	}

	private void checkPageComplete() {
		if(!tableSelect){
			if(tb.getSelectionCount()!=0 && radios[0].getSelection()){
				setPageComplete(true);
			}
			else {
				if(radios[1].getSelection()){
					setPageComplete(true);
				}
				else{
					setPageComplete(false);
				}
			}
		}
		else{
			setPageComplete(true);
		}
	}

	public boolean getFastMode() {
		return radios[0].getSelection();
	}
	
	public String getTableName(){
		return tb.getItem(tb.getSelectionIndex()).getText();
	}
	
	public boolean getTableSelect(){
		return tableSelect;
	}
	
}
