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
import java.util.Iterator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import bi.bmm.Activator;
import bi.bmm.DataPools;
import bi.bmm.util.WSConnection;


public class Binding_page02 extends WizardPage {
	
	private Composite container;
	private DataPools dataPool;
	private String data;
	private String wsConnName;
	private String wsMethName;
	private Label l;
	private Text t;
	
	public Binding_page02(String data) {
		super("");
		setTitle("Binding Wizard");
		setDescription("Set return parameters.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/bindingCBC.png");
	    if (image!=null) setImageDescriptor(image);
	    this.data = data;
	}

	@Override
	public void createControl(Composite parent) {
		
		container = new Composite(parent, SWT.NULL);
		GridLayout gridLayout = new GridLayout(); 
		gridLayout.numColumns = 1; 
		container.setLayout(gridLayout); 
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//metto da un lato i parametri del metodo e dall'altro una combo con i nomi dei
		//campi della CBC + null nel caso nn si voglia mettere niente
		//---------------------------------------------

				
		//recupero le viste
		dataPool =	(DataPools) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.datapools");
		
		
		//recupero i parametri dal WS
		String[] dataSplit = data.split(" - ");
		if(dataSplit.length!=4){
			if(dataSplit.length!=2){
			System.err.println(data +"BUG: PASSAGE OF DATA FROM DND of WS is FAILED");
			return;
			}
			else{
				wsConnName = dataSplit[1];
				wsMethName = dataSplit[0];
			}
		}
		else{
			wsConnName = dataSplit[3];
			wsMethName = dataSplit[2];
		}
	
		//recupero il WS
		WSConnection wsConn = dataPool.activeWS.getConnection(wsConnName);
		if(wsConn==null){
			System.err.println("BUG: THE CONNECTION "+wsConnName+" ISN'T ACTIVE!");
			return;
		}
		//recupero il metodo
			String key = wsMethName+"Response";
			//Aggiungo un gruppo
			Group g = new Group(container, SWT.SHADOW_ETCHED_IN);
			g.setText(key);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			g.setLayout(layout);
			g.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			ArrayList<String[]> parList = wsConn.getMethods().get(key);
			if(parList == null){
				System.err.println("BUG: method "+key+" doesn't exist in WS");
				return;
			}
			//aggiungo il campo parametro con una combo cn tutti i campi della CBC
			Iterator<String[]> it = parList.iterator();
			while(it.hasNext()){
				String[] par = it.next();
				
				l = new Label(g, SWT.BORDER);
				l.setText(par[0] +" - "+ par[1]);
				
				t = new Text(g, SWT.BORDER);
				t.setText(par[0]);
				t.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			}
			
			t.addKeyListener(new KeyListener() {
				
				@Override
				public void keyReleased(KeyEvent e) {
					checkPageComplete();
				}
				
				@Override
				public void keyPressed(KeyEvent e) {
					checkPageComplete();
				}
			});
		
		
		// Required to avoid an error in the system
		setControl(container);
		checkPageComplete();

	}

	private void checkPageComplete() {
		if(t.getText().equals(""))
			setPageComplete(false);
		else
			setPageComplete(true);
	}
	
	public String[] getReturnName (){
		return new String[]{this.l.getText(),this.t.getText()};
	}

}
