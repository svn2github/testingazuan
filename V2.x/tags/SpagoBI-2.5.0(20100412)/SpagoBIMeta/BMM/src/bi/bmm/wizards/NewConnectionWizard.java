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
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.IViewDescriptor;

import bi.bmm.util.ConnectionsPool;
import bi.bmm.util.DBConnection;

import java.sql.*;

public class NewConnectionWizard extends Wizard {

	private ConnectionsPool activeConn;
	public Connection db;
	
	private NewConn_page01 one;
	private NewConn_page02 two;

	public NewConnectionWizard(ConnectionsPool activeConn) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new DB connection");
		this.activeConn = activeConn;
	}

	@Override
	public void addPages() {
		one = new NewConn_page01();
		two = new NewConn_page02();
		addPage(one);
		addPage(two);
	}

	@Override
	public boolean performFinish() {
		
		DBConnection conn = new DBConnection(one.getName(),one.getDriver(), two.getServer(),
												two.getDatabase(), two.getUser(), 
												two.getPassword(), two.getPort());		
		if (!conn.Start()){
			String errorText = "Unable to connect to the database:Could not connect to "+two.getServer();
			MessageDialog.openError(new Shell(), "Database Error", errorText);
			return false;
		}
		else
		{
			if (conn.getTables()!= null){
				
				IViewDescriptor desc = PlatformUI.getWorkbench().getViewRegistry().find("bi.bmm.views.bme.datapools");
				
				 if(desc != null) {
					 activeConn.addConnection(conn);
					 return true;
				    }
				 else{
					 String errorText = "Unable to open the perspective";
						MessageDialog.openError(new Shell(), "Perspective Error", errorText);
						return false;
					}

				
		        
				
			}
			else{
				String errorText = "Unable to execute the query";
				MessageDialog.openError(new Shell(), "SQL Error", errorText);
				return false;
			}
		}
	}

	
}



