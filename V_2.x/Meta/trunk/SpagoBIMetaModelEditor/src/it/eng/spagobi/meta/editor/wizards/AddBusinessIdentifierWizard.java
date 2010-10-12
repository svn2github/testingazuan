/*
 * Wizard for creating a new Business Identifier
 */
package it.eng.spagobi.meta.editor.wizards;

import org.eclipse.jface.wizard.Wizard;

public class AddBusinessIdentifierWizard extends Wizard {

	AddBusinessIdentifierWizardPageOne pageOne;
	AddBusinessIdentifierWizardPageTwo pageTwo;
	
	public AddBusinessIdentifierWizard(){
		super();
		this.setWindowTitle("Create a new Business Identifier");
		this.setHelpAvailable(false);		
	}

	@Override
	public void addPages() {
		pageOne = new AddBusinessIdentifierWizardPageOne("Add Business Identifier page one");
		addPage(pageOne);
		pageTwo = new AddBusinessIdentifierWizardPageTwo("Add Business Identifier page two");
		addPage(pageTwo);	
		pageOne.setPageTwoRef(pageTwo);
	}
	@Override
	public boolean performFinish() {
		if (pageOne.isPageComplete())
			return true;
		else
			return false;
	}

}
