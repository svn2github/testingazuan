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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewConn_page02 extends WizardPage {
	private Text server;
	private Text port;
	private Text user;
	private Text password;
	private Text database;
	private Composite container;

	public NewConn_page02() {
		super("Connection details");
		setTitle("Connection details");
		setDescription("Insert your connection details (port, host, username, password, ... )");
	}

	@Override
	public void createControl(Composite parent) {
		container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		//------------------------------------------------------
		Label label1 = new Label(container, SWT.NULL);
		label1.setText("Server");
		server = new Text(container, SWT.BORDER | SWT.SINGLE);
		server.setText("");
		server.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!server.getText().isEmpty()
					&& !port.getText().isEmpty()
					&& !database.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});
		//------------------------------------------------------
		Label label2 = new Label(container, SWT.NULL);
		label2.setText("Port");
		port = new Text(container, SWT.BORDER | SWT.SINGLE);
		port.setText("");
		port.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!server.getText().isEmpty()
					&& !port.getText().isEmpty()
					&& !database.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});
		//------------------------------------------------------
		Label label22 = new Label(container, SWT.NULL);
		label22.setText("Database");
		database = new Text(container, SWT.BORDER | SWT.SINGLE);
		database.setText("");
		database.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!server.getText().isEmpty()
					&& !port.getText().isEmpty()
					&& !database.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});
		//------------------------------------------------------
		Label label3 = new Label(container, SWT.NULL);
		label3.setText("User");
		user = new Text(container, SWT.BORDER | SWT.SINGLE);
		user.setText("");
		user.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!server.getText().isEmpty()
					&& !port.getText().isEmpty()
					&& !database.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});
		//------------------------------------------------------
		Label label4 = new Label(container, SWT.NULL);
		label4.setText("Password");
		password = new Text(container, SWT.BORDER | SWT.SINGLE);
		password.setText("");
		password.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (!server.getText().isEmpty()
					&& !port.getText().isEmpty()
					&& !database.getText().isEmpty()) {
					setPageComplete(true);
				}
			}

		});
		//-------------------------------------------------------
		/*
		 * Setta i parametri di default in base ai Driver
		 * */
		setParam();
		//-------------------------------------------------------
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		server.setLayoutData(gd);
		database.setLayoutData(gd);
		port.setLayoutData(gd);
		user.setLayoutData(gd);
		password.setLayoutData(gd);
		
		// Required to avoid an error in the system
		setControl(container);
		setPageComplete(false);
	}

	private void setParam() {
		server.setText("localhost");
		database.setText("foodmart");
		port.setText("3306");
		user.setText("root");
	}

	/*
	 * Getter dei parametri
	 * 
	 * */
	public String[] getConnessionDetails() {
		return new String[]{server.getText(),port.getText(),database.getText(),user.getText(),password.getText()};
	}
	
	public String getServer(){
		return server.getText();
	}

	public String getPort(){
		return port.getText();
	}
	
	public String getUser(){
		return user.getText();
	}
	
	public String getPassword(){
		return password.getText();
	}
	
	public String getDatabase(){
		return database.getText();
	}
}

