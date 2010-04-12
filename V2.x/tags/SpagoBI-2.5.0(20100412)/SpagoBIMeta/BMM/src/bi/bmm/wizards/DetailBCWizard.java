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

import java.io.File;
import java.io.IOException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import bi.bmm.BMUniverseView;
import bi.bmm.elements.BusinessModelUtil;
import bi.bmm.figures.ActivityFigure;
import bi.bmm.util.ClassInfo;
import bi.bmm.util.HunkIO;

public class DetailBCWizard extends Wizard {

	
	private DetailBC_page01 one;
	private ClassInfo ci;
	private ActivityFigure af;
	private BusinessModelUtil bmu;

	public DetailBCWizard(ClassInfo ci,ActivityFigure af) {
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Business Class Details");
		this.ci = ci;
		this.af = af;
	}

	@Override
	public void addPages() {
		one = new DetailBC_page01(ci);
		//two = new NewConn_page02();
		addPage(one);
		//addPage(two);
	}

	@Override
	public boolean performFinish() {
		
		if (one.isPageComplete()){
			
			//elimino la vecchia figura e il vecchio file
			af.removeAll();
			af.setVisible(false);
			File f = new File(ci.getClassPathInfo());
			if (f.exists()) f.delete();
			//compongo il file con l'entità
			createEntity(one.getKeys(),one.getAttributes(),one.getClassName());
			//disegno nella view la tabella
			createTableInBMU();
			
			bmu.configuringPersistenceProvider(ci.getConnection());
			
			return true;
		}
	return false;
	}

	
	private void createEntity(Table keys, Table attributes, String name) {
		
		try {
			//Setto il file di persistenza
			String result = HunkIO.readEntireFile( HunkIO.DEFAULT_INFO_FILE, "UTF-8" );
			String[] sResult =result.split("#");
			
			
			bmu = new BusinessModelUtil(sResult[0], sResult[1]);
			bmu.creaDirPersistence(ci.getConnection().getName());
			bmu.configuringPersistenceProvider(ci.getConnection());
			bmu.createClassInfo(ci.getConnection(),keys,attributes,name,ci.getMappings(),ci.getRelationships(),ci.getClassTable());
		
		} catch (IOException e) {
			MessageDialog.openError(new Shell(), "Error reading info file.","An error encourring when trying to read the BM info file");
		}
		
		
	}

	private void createTableInBMU() {
		if(bmu == null){
			return;
		}
		//recupero la view
		BMUniverseView bmUniverse = (BMUniverseView) PlatformUI.getWorkbench().getActiveWorkbenchWindow()
		.getActivePage().findView("bi.bmm.views.bme.bmuniverse");
		//recupero il gruppo principale
		//recupero la classe e la compilo
		ClassInfo ci2 = new ClassInfo(bmu.getClassPathInfo(),ci.getConnection());
		System.out.println(bmu.getClassPathInfo());
		ci2.buildClassInfo();
		bmUniverse.bcList.remove(ci);
		bmUniverse.bcList.add(ci2);
		ci2.printClassInfo();
		bmUniverse.drawBC(ci2);
		
	}


	

	
}