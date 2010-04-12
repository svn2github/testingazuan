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

import java.util.Iterator;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.util.ComplexClassInfo;


public class BindingWizard extends Wizard {

	
	private Binding_page01 one;
	private String data;
	private String cbc2Binding;
	private Binding_page02 two;

	public BindingWizard(String data,String cbc2Binding) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Binding WS Method with CBC");
		this.data = data;
		this.cbc2Binding=cbc2Binding;
	}

	@Override
	public void addPages() {
			one = new Binding_page01(data,cbc2Binding);
			two = new Binding_page02(data);
			addPage(one);
			addPage(two);
		
	}

	@Override
	public boolean performFinish() {
		
		if (two.isPageComplete()){
			
			//ripesco la cbc info dalla cbclist della BUVIEW
			BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
  			.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
			
			Iterator<ComplexClassInfo> iter = bmUniverse.cbcList.iterator();
			ComplexClassInfo cbcInfo = null;
			while(iter.hasNext()){
				ComplexClassInfo cci = iter.next();
				if (cci.getClassName().equals(cbc2Binding)){
					cbcInfo = cci;
					break;
				}
			}
			//se è ancora null segnalo l'errore e ritorno un false
			if(cbcInfo == null){
				System.err.println("ERROR: impossible find class "+cbc2Binding +" in active list of BMU view.");
				return false;
			}
			//altrimenti ci aggiungo il campo per il WS
			cbcInfo.addInhWS(new String[]{
						/*name*/ two.getReturnName()[1],
						/*type*/ two.getReturnName()[0].split(" - ")[1]},one.getWSMethodName()
						,one.getWSConnName(),one.getSwtList());			
			
					return true;
	    		
			}
	return false;
	}
	

	

	
}