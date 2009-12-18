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

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;

import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;

public class NewRelWizard extends Wizard {
	
	private NewRel_page01 one;
	
	private ArrayList<ClassInfo>ciList;
	private BMScheme bms;

	public NewRelWizard(ArrayList<ClassInfo>ciList,BMScheme bms) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Relationship");
		
		this.ciList=ciList;
		this.bms=bms;
	}

	@Override
	public void addPages() {
		one = new NewRel_page01(ciList);
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		
		if (one.isPageComplete()){
			NewRelWizard2 wizard = new NewRelWizard2(ciList, bms, one.getClassIn(),one.getClassOut());
	    	WizardDialog dialog = new WizardDialog(new Shell(), wizard);
	    	dialog.open();
    		if(dialog.getReturnCode() == WizardDialog.OK){
    			
    			return true;
    		}
	    	
		}
		
	return false;
	}
	
	public ClassInfo getOne(){
		return one.getClassIn();
	}
}