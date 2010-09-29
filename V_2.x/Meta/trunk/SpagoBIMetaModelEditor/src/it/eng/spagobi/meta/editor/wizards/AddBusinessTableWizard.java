/*
 * Wizard for creating a new Business Table
 */
package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.wizard.Wizard;

public class AddBusinessTableWizard extends Wizard {
	private AddBusinessTableWizardPageOne pageOne;
	private AddBusinessTableWizardPageTwo pageTwo;
	private AddBusinessTableWizardPageThree pageThree;
	
	public AddBusinessTableWizard(){
		super();
		this.setWindowTitle("Create a new Business Table");
		this.setHelpAvailable(false);
	}
	
	@Override
	public void addPages() {
		pageOne = new AddBusinessTableWizardPageOne("Create Business Table step one");
		addPage(pageOne);
		pageTwo = new AddBusinessTableWizardPageTwo("Create Business Table step two", pageOne);
		addPage(pageTwo);
		pageThree = new AddBusinessTableWizardPageThree("Create Business Table step three");
		addPage(pageThree);
	}
	
	@Override
	public boolean performFinish() {		
		if (pageTwo.isPageComplete()){
			String tableName = pageTwo.getTableSelected();
			//Create Business Table from a Physical Table
			BusinessModel bm = CoreSingleton.getInstance().getBusinessModel();
			PhysicalModel pm = CoreSingleton.getInstance().getPhysicalModel();
			PhysicalTable pTable = pm.getTable(tableName);
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			initializer.addTable(pTable, bm);
			return true;
		}
		else
			return false;
	}
	
	
	public void dispose(){
		pageOne.dispose();
	}

}
