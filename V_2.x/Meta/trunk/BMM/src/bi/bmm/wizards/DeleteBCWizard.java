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

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.jface.wizard.Wizard;

import bi.bmm.elements.BusinessModelUtil;
import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;
import bi.bmm.util.HunkIO;

public class DeleteBCWizard extends Wizard {

	
	private DeleteBC_page01 one;
	
	private ArrayList<ClassInfo>ciList;
	private BMScheme bms;

	public DeleteBCWizard(ArrayList<ClassInfo>ciList,BMScheme bms) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Delete a BC");
		
		this.ciList=ciList;
		this.bms=bms;
	}

	@Override
	public void addPages() {
		one = new DeleteBC_page01(ciList,bms);
		//two = new NewConn_page02();
		addPage(one);
		//addPage(two);
	}

	@Override
	public boolean performFinish() {
		
		if (one.isPageComplete()){
			//risetta il persistence File
			String result;
			try {
				result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
				String[] sResult =result.split("#");
				
				BusinessModelUtil bmu = new BusinessModelUtil(sResult[0], sResult[1]);
				if (!ciList.isEmpty()){
					bmu.configuringPersistenceProvider(ciList.get(0).getConnection());
				}
				else{
					bmu.configuringPersistenceProvider(null);
				}
				return true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			
			
		}
	return false;
	}
}