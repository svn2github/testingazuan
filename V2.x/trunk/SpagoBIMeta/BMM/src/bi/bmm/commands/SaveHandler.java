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

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.util.HunkIO;

public class SaveHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		//bm PATH
		String bmPath;
		//Ottengo il Path del file da salvare
		try {
			StringBuilder sb = new StringBuilder( 1000 );
			
			String result = HunkIO.readEntireFile(HunkIO.DEFAULT_INFO_FILE,
			         "UTF-8" );
			bmPath = result.split("#")[1]+"/"+result.split("#")[0]+"/bmInfo.bm";
			
			sb.append("<DEFAULT_INFO_FILE>");
			sb.append(result);
			sb.append("</DEFAULT_INFO_FILE>\n");
			
			
			
			//Ottengo la vista
			BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
			
			sb.append("<bmUniverse>\n");
			sb.append(bmUniverse.toFile().toString());
			sb.append("</bmUniverse>\n");
			
			HunkIO.writeEntireFile(bmPath,sb.toString(),
	         "UTF-8" );
			
			MessageDialog.openInformation(new Shell(), "Save BM",""+
			"Your BM was Saved!");
    
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return null;
	}

}
