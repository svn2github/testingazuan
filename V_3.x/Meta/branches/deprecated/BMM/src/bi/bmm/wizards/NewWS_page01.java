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
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;


public class NewWS_page01 extends WizardPage {
	
	private Composite container;
	private Text text1;
	private Text text2;
	private Text text3;

	public NewWS_page01() {
		super("Create a new connection with WS");
		setTitle("New WS Connection");
		setDescription("This wizard drives you to crate a new WS connection.");

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
		text1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text1.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
					checkPageComplete();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
					checkPageComplete();			
			}
		});
		
		//---------------------------------------------
		Label label2 = new Label(container, SWT.NULL);
		label2.setText("Set your End Point: ");

		text2 = new Text(container, SWT.BORDER);
		text2.setText("");
		text2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text2.addKeyListener(new KeyListener() {
			
			@Override
			public void keyReleased(KeyEvent e) {
					checkPageComplete();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
					checkPageComplete();			
			}
		});
		//---------------------------------------------
		Button btn = new Button(container, SWT.FLAT);
		btn.setText("Choose a wsdl file");
		btn.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				//Lancio un nuovo FileDialog
				Shell shell = new Shell();
				// File standard dialog
				FileDialog fileDialog = new FileDialog(shell);
				// Set the text
				fileDialog.setText("Select a .wsdl file");
				// Set filter on .txt files
				fileDialog.setFilterExtensions(new String[] { "*.wsdl" });
				// Put in a readable name for the filter
				fileDialog.setFilterNames(new String[] { "WSDL(*.wsdl)" });
				// Open Dialog and save result of selection
				String selected = fileDialog.open();
				if (selected != null){
					//cambio il text3
					text3.setText(selected);
					checkPageComplete();
				}
				
			}
		});
		
		text3 = new Text(container, SWT.BORDER);
		text3.setText("");
		text3.setEditable(false);
		text3.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				
				

		
		// Required to avoid an error in the system
		setControl(container);
		checkPageComplete();

	}

	protected void checkPageComplete() {
		if (text1.getText().equals("") ||
				text2.getText().equals("")||
				text3.getText().equals("")){
			setPageComplete(false);
			return;
		}
		else{
			//TODO: CONTROLLO CHE IL SERVIZIO SIA ATTIVO
			setPageComplete(true);
			return;
		}
		
	}
	
	public String getConnectionName(){
		return text1.getText();
	}
	
	public String getEndPoint(){
		return text2.getText();
	}
	
	public String getWSDLPath(){
		return text3.getText();
	}

}
