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

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import bi.bmm.Activator;
import bi.bmm.BMUniverseView;
import bi.bmm.util.ComplexClassInfo;


public class BindingCBC_page01 extends WizardPage {
	
	private Composite container;
	private Table tb;
	private BMUniverseView bmUniverse;
	
	public BindingCBC_page01() {
		super("");
		setTitle("Binding Wizard");
		setDescription("Select the Complex BC you want binding method with.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/bindingCBC.png");
	    if (image!=null) setImageDescriptor(image);
	   
	}

	@Override
	public void createControl(Composite parent) {
		
		container = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		container.setLayout(gridLayout); 
		
		
		//---------------------------------------------

		tb = new Table(container, SWT.BORDER);
		tb.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//recupero la vista
		bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
		
		Iterator<ComplexClassInfo> iter = bmUniverse.cbcList.iterator();
		while(iter.hasNext()){
			ComplexClassInfo cci = iter.next();
			TableItem ti = new TableItem(tb, 0);
			ti.setText(cci.getClassName());
		}
		
		tb.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				checkPageComplete();
			}
		});
		
		
		// Required to avoid an error in the system
		setControl(container);
		checkPageComplete();

	}

	private void checkPageComplete() {
		if(tb.getSelectionIndex()!=-1)
			setPageComplete(true);
		else
			setPageComplete(false);
		
	}
	
	public String getBindingCBC(){
		return tb.getSelection()[0].getText();
	}

}
