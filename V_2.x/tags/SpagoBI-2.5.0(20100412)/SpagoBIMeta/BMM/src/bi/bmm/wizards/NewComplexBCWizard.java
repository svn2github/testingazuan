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



public class NewComplexBCWizard extends Wizard {

	
	private NewComplexBC_page01 one;
	
	public NewComplexBCWizard(String table) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Complex Business Class");
	}

	@Override
	public void addPages() {
		one = new NewComplexBC_page01();
		//two = new NewConn_page02();
		addPage(one);
		//addPage(two);
	}

	@Override
	public boolean performFinish() {
		
		if (one.isPageComplete()){
			SetComplexBCFieldsWizard wizard = new SetComplexBCFieldsWizard(one.getComplexClassName(),one.getInhBcList());
        	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
        	dialog.setPageSize(900,600);
    		dialog.open();
    		if(dialog.getReturnCode() == WizardDialog.OK){
    			return true;
    		}
	    }
		
	return false;
	}
	

	

	
}