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

import bi.bmm.util.WSConnection;
import bi.bmm.util.WSPool;


public class NewWSWizard extends Wizard {

	private WSPool activeWS;
	
	private NewWS_page01 one;

	public NewWSWizard(WSPool activeWS) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new WS connection");
		this.activeWS = activeWS;
	}

	@Override
	public void addPages() {
		one = new NewWS_page01();
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		if (one.isPageComplete()){
			WSConnection conn = new WSConnection(one.getConnectionName(), 
					one.getEndPoint(), one.getWSDLPath());
			if (conn!=null)
			{
				//controllo se la connessione esiste già
				if (activeWS.getConnection(one.getConnectionName())==null){
					//se nn esiste la carico
					activeWS.addWS(conn);
					
					return true;	
				}				
			}
		}
		return false;
	}

	
}



