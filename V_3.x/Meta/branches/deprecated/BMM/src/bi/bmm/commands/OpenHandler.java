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
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

import bi.bmm.BMUniverseView;
import bi.bmm.util.ConstantString;
import bi.bmm.util.HunkIO;

public class OpenHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		
		Shell shell = HandlerUtil.getActiveWorkbenchWindow(event).getShell();
		// File standard dialog
		FileDialog fileDialog = new FileDialog(shell);
		// Set the text
		fileDialog.setText("Select a Business Model");
		// Set filter on .txt files
		fileDialog.setFilterExtensions(new String[] { "*.bm" });
		// Put in a readable name for the filter
		fileDialog.setFilterNames(new String[] { "BusinessModel(*.bm)" });
		// Open Dialog and save result of selection
		String selected = fileDialog.open();
		
		try {
			
			String result = HunkIO.readEntireFile(selected,
			         "UTF-8" );
			String defInfoFile = result.split("<DEFAULT_INFO_FILE>")[1].split("</DEFAULT_INFO_FILE>")[0];
			ConstantString.PROJECT_PATH = defInfoFile.split("#")[1]+"/"+defInfoFile.split("#")[0]+"/";
			HunkIO.writeEntireFile(HunkIO.DEFAULT_INFO_FILE, defInfoFile, "UTF-8");
			
			
			//Ottengo la vista
			BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
			
			if (bmUniverse!=null){
				bmUniverse.openFile(result.split("<bmUniverse>\n")[1].split("</bmUniverse>")[0]);
			}
			else {
				//apri una nuova perspective
				ShowBMEHandler sh = new ShowBMEHandler();
				sh.execute(event);
				bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
				.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
				bmUniverse.openFile(result.split("<bmUniverse>\n")[1].split("</bmUniverse>")[0]);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
