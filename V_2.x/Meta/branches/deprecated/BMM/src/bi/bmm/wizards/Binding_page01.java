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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;

import bi.bmm.Activator;
import bi.bmm.BMUniverseView;
import bi.bmm.DataPools;
import bi.bmm.util.ComplexClassInfo;
import bi.bmm.util.Value;
import bi.bmm.util.WSConnection;


public class Binding_page01 extends WizardPage {
	
	private Composite container;
	private BMUniverseView bmUniverse;
	private DataPools dataPool;
	private String data;
	private String cbc2Binding;
	private ComplexClassInfo complexBC;
	private String wsConnName;
	private String wsMethName;
	private ArrayList<Value> swtList;
	
	public Binding_page01(String data, String cbc2Binding) {
		super("");
		setTitle("Binding Wizard");
		setDescription("Set binding parameters with WS method.");
		ImageDescriptor image = Activator.getImageDescriptor("icons/wizards/bindingCBC.png");
	    if (image!=null) setImageDescriptor(image);
	    this.data = data;
	    this.cbc2Binding = cbc2Binding;
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
		bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
		dataPool =	(DataPools) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.datapools");
		//recupero la CBC
		Iterator<ComplexClassInfo> cciIt = bmUniverse.cbcList.iterator();
		while(cciIt.hasNext()){
			ComplexClassInfo cci = cciIt.next();
			if (cci.getClassName().equals(cbc2Binding)){
				complexBC = cci;
				break;
			}
		}
		
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
			String key = wsMethName;
			//Aggiungo un gruppo
			Group g = new Group(container, SWT.SHADOW_ETCHED_IN);
			g.setText(key);
			GridLayout layout = new GridLayout();
			layout.numColumns = 2;
			layout.makeColumnsEqualWidth=true;
			g.setLayout(layout);
			g.setLayoutData(new GridData(GridData.FILL_BOTH));
			
			ArrayList<String[]> parList = wsConn.getMethods().get(key);
			swtList = new ArrayList<Value>();
			//aggiungo il campo parametro con una combo cn tutti i campi della CBC
			Iterator<String[]> it = parList.iterator();
			while(it.hasNext()){
				String[] par = it.next();
				Label l = new Label(g, SWT.BORDER);
				l.setText(par[0] +" - "+ par[1]);
				
				Combo c = new Combo(g, SWT.NULL);
				Iterator<String> i = complexBC.getInhBC().keySet().iterator(); 
				while(i.hasNext()){
					c.add("null - null");
					String key2 = i.next();
					ArrayList<String[]> fields = complexBC.getInhBC().get(key2); 
					Iterator<String[]> fieldIt = fields.iterator();
					while(fieldIt.hasNext()){
						String[] field = fieldIt.next();
						c.add(field[0]+ " - " +field[1]);
					}
				}
				swtList.add(new Value(c, l));							
			}
		
		
		
		
		
		// Required to avoid an error in the system
		setControl(container);
		checkPageComplete();

	}

	private void checkPageComplete() {
			setPageComplete(true);
	}
	
	public ArrayList<Value> getSwtList(){
		return this.swtList;
	}
	
	public String getWSConnName(){
		return this.wsConnName;
	}
	public String getWSMethodName(){
		return this.wsMethName;
	}
	

}
