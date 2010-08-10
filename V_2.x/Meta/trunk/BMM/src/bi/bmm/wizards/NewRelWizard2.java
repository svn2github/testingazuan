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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.elements.BusinessModelUtil;
import bi.bmm.figures.BMScheme;
import bi.bmm.util.ClassInfo;
import bi.bmm.util.HunkIO;

public class NewRelWizard2 extends Wizard {
	
	private NewRel_page02 one;
	
	private ArrayList<ClassInfo>ciList;
	private BMScheme bms;
	private ClassInfo cIn;
	private ClassInfo cOut;

	public NewRelWizard2(ArrayList<ClassInfo>ciList,BMScheme bms,ClassInfo cIn, ClassInfo cOut) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Relationship");
		
		this.ciList=ciList;
		this.bms=bms;
		this.cIn = cIn;
		this.cOut = cOut;
	}

	@Override
	public void addPages() {
		one = new NewRel_page02(ciList,bms,cIn,cOut);
		addPage(one);
	}

	@Override
	public boolean performFinish() {
		
		if (one.isPageComplete()){
			//recupero la vista
			BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
			.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
			
			switch(one.getJoinType()){
			case BMScheme.MANY_TO_MANY:
				bms.addLinkFigure(cIn.getFigure(), cOut.getFigure(),BMScheme.MANY_TO_MANY);
				bmUniverse.relList.add(new String[] {cIn.getClassPathInfo(), cOut.getClassPathInfo(),"MANY_TO_MANY"});
				//TODO: IMPLEMENT MAPPING TYPE
				break;
			case BMScheme.ONE_TO_MANY:
				bms.addLinkFigure(cIn.getFigure(), cOut.getFigure(),BMScheme.ONE_TO_MANY);
				bmUniverse.relList.add(new String[] {cIn.getClassPathInfo(), cOut.getClassPathInfo(),"ONE_TO_MANY"});
				cIn.addOneToManyRelation(one.getJoinOut()[0], cOut.getClassName());
				cOut.addManyToOneRelation(one.getJoinOut()[0],cIn.getClassName());
				break;
			case BMScheme.MANY_TO_ONE:
				bms.addLinkFigure(cOut.getFigure(), cIn.getFigure(),BMScheme.MANY_TO_ONE);
				bmUniverse.relList.add(new String[] {cOut.getClassPathInfo(), cIn.getClassPathInfo(),"MANY_TO_ONE"});
				cOut.addOneToManyRelation(one.getJoinIn()[0], cIn.getClassName());
				cIn.addManyToOneRelation(one.getJoinIn()[0],cOut.getClassName());
				break;
			case BMScheme.ONE_TO_ONE:
				bms.addLinkFigure(cIn.getFigure(), cOut.getFigure(),BMScheme.ONE_TO_ONE);
				bmUniverse.relList.add(new String[] {cOut.getClassPathInfo(), cIn.getClassPathInfo(),"ONE_TO_ONE"});
				//TODO: IMPLEMENT MAPPING TYPE
				break;
			default:
				MessageDialog.openInformation(new Shell(), "Error!",""+
				" An Error was occurred during the type choosing.");
			;
			}
			
			
			String result;
			try {
				result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
				String[] sResult =result.split("#");
				BusinessModelUtil bmu = new BusinessModelUtil(sResult[0], sResult[1]);
				bmu.createClassInfoByCI(cIn);
				bmu.createClassInfoByCI(cOut);
				
				
		    	return true;		
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				return false;
			}
			
		
			
		}
		
	return false;
	}
	
}