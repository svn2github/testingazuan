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
package bi.bmm.commands;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.elements.BusinessModelUtil;
import bi.bmm.util.ComplexClassInfo;
import bi.bmm.util.ConstantString;
import bi.bmm.util.HunkIO;
public class createProjectHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		if (persistEntity()){
			MessageDialog.openInformation(new Shell(), "Java files was created.","The Business Model Project is ready to be" +
					"compiled and tested.");
		}
		else{
			MessageDialog.openError(new Shell(), "No BM is open.","To generate the Java files for your BM " +
			"you must select a BM or create a new one.");
		}
		
		return null;
	}

private boolean persistEntity() {
		
	//recupero la view
	BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
	.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
	
	if(bmUniverse == null){
		return false;
	}
	
	//recupero la classe e la compilo
		try {
			//
			String result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
			String[] sResult =result.split("#");
			ConstantString.PROJECT_PATH = sResult[1]+"/"+sResult[0]+"/";
			
			BusinessModelUtil bmu = new BusinessModelUtil(sResult[0], sResult[1]);
			//Creo le classi Java
			for(int i=0;i<bmUniverse.bcList.size();i++){
				//passo il nome della classe
				bmu.createClassJava(bmUniverse.bcList.get(i).getClassPathInfo(),bmUniverse.bcList.get(i).getConnection());
				
			}
			
			//creo la classe Complessa JAVA 
			Iterator<ComplexClassInfo> iter = bmUniverse.cbcList.iterator();
			while(iter.hasNext()){
				ComplexClassInfo cci = iter.next();
				cci.doJava();
			}
			return true;
		
		} catch (IOException e) {
			MessageDialog.openError(new Shell(), "Error reading info file.","An error encourring when trying to read the BM info file");
		}
		
		return false;
		
	}
}
