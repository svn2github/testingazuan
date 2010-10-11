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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMPoolsView;
import bi.bmm.util.ComplexClassInfo;

public class WhereWizard extends Wizard {

	
	private Where_page01 one;
	private ArrayList<String> whereList;
	private Table tb;
	private String arg;
	

	public WhereWizard(ArrayList<String>whereList, Table tb, String arg) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a Where Clause");
		
		this.whereList = whereList;
		this.tb = tb;
		this.arg = arg;
	}

	@Override
	public void addPages() {
		//apro la cbcList per vedere se la prima parte di arg e una cbc
		 BMPoolsView bmPools = (BMPoolsView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		  .getActivePage().findView("bi.bmm.views.bmq.bmpools");
		 
		 Boolean isCbc = false;
		 
		 if(arg.split("\\.").length>0){
			 Iterator<ComplexClassInfo> it = bmPools.cbcList.iterator();
			 while(it.hasNext()){
				 ComplexClassInfo cci = it.next();
				 if(cci.getClassName().equals( arg.split("\\.")[0])){
					 isCbc = true;
					 break;
				 }
			 }
		}
		//se è una cbc la segnalo alla pagina, questo permette di cambiare gli elementi della 
		//combo
		one = new Where_page01(arg,isCbc);
		//two = new NewConn_page02();
		addPage(one);
		//addPage(two);
	}

	@Override
	public boolean performFinish() {
		
		if (one.isPageComplete()){
			
			String whereClause = "";
			if(one.getType().equals("equals")){
				whereClause = arg +".equals(\""+one.getClause()+"\")";
				whereList.add(whereClause);
				TableItem ti = new TableItem(tb, 0);
				ti.setText(whereClause);
				return true;
			}
			if(one.getType().equals("not equals")){
				whereClause ="!"+ arg +".equals(\""+one.getClause()+"\")";
				whereList.add(whereClause);
				TableItem ti = new TableItem(tb, 0);
				ti.setText(whereClause);
				return true;
			}
			if(one.getType().equals("contains")){
				whereClause = arg +".contains(\""+one.getClause()+"\")";
				whereList.add(whereClause);
				TableItem ti = new TableItem(tb, 0);
				ti.setText(whereClause);
				return true;
			}
			if(one.getType().equals("not contains")){
				whereClause ="!"+ arg +".contains(\""+one.getClause()+"\")";
				whereList.add(whereClause);
				TableItem ti = new TableItem(tb, 0);
				ti.setText(whereClause);
				return true;
			}
			whereClause = arg + one.getType() +one.getClause();
			if (!whereList.contains(whereClause)){
				whereList.add(whereClause);
				TableItem ti = new TableItem(tb, 0);
				ti.setText(whereClause);
				return true;
			}
			else{
				MessageDialog.openWarning(new Shell(), "Where Clause Exist", "The where clause already exist, please check it and retry!");
				return true;
			}
			
			
		}
	return false;
	}
}