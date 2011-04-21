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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import bi.bmm.util.DBConnection;


public class NewBCWizard extends Wizard {

	
	private NewBC_page01 one;
	private DBConnection conn;
	private String table;

	public NewBCWizard(DBConnection conn, String table) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Business Class");
		this.conn = conn;
		this.table = table;
	}

	@Override
	public void addPages() {
		if (table == null){
			one = new NewBC_page01(false,conn);
		}
		else{
			one = new NewBC_page01(true,conn);
		}
		//two = new NewConn_page02();
		addPage(one);
		//addPage(two);
	}

	@Override
	public boolean performFinish() {
		
		if (one.isPageComplete()){
			if (!one.getTableSelect()){
				this.table = one.getTableName();
			}
			
			if(one.getFastMode()){
				//fast Mode Procedure
				SetBCDetailFast wizard = new SetBCDetailFast(conn,table);
	        	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
	    		dialog.open();
	    		if(dialog.getReturnCode() == WizardDialog.OK){
	    			return true;
	    		}
			}
			else{
				//TODO: implementare la normal mode procedure
				//normal Mode Procedure
				/**
				 * SIMILE ALLA FAST MODE
				 * qui si possono settare i nomi dei campi, 
				 * creare campi multipli e cambiare il tipo
				 * dei campi
				 * 
				 * */
				return true;
			}
			
		}
	return false;
	}
	

	

	
}