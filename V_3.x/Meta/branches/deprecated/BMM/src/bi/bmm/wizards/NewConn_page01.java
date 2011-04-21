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

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class NewConn_page01 extends WizardPage {
	
	private Composite container;
	private Combo combo1;
	private Text text1;

	public NewConn_page01() {
		super("Select a db type");
		setTitle("Select a db type");
		setDescription("This wizard drives you to crate a new database connection, please select your driver from the list");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		
		//--------------------------------------------
		Label label1 = new Label(container, SWT.NULL);
		label1.setText("Connection Name: ");
		
		text1 = new Text(container, SWT.BORDER);
		text1.setText("");
		text1.setSize(100, 20);
		
		text1.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
				if (!combo1.getText().equals("")){
					setPageComplete(true);
				}
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//---------------------------------------------
		Label label2 = new Label(container, SWT.NULL);
		label2.setText("Select your driver: ");

		combo1 = new Combo (container, SWT.READ_ONLY);
		combo1.setItems (new String [] {"MySql 5.0", "MySql 5.1"
				//TODO: Aggiungere più driver
				});
		combo1.setSize (200, 200);

		combo1.addListener(SWT.Selection,new Listener(){
			public void handleEvent(Event e){
				if (!text1.getText().equals("")){
					setPageComplete(true);
				}
			}});
		//---------------------------------------------



		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		combo1.setLayoutData(gd);
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);

	}

	public String[] getConnessionType() {
		String[] string = new String[]{combo1.getText(),text1.getText()}; 
		return string;
	}
	
	public String getDriver(){
		return combo1.getText();
	}
	
	public String getName(){
		return text1.getText();
	}
}
