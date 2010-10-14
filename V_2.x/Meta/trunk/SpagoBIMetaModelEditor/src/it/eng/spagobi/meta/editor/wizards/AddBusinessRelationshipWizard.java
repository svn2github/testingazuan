package it.eng.spagobi.meta.editor.wizards;

import org.eclipse.jface.wizard.Wizard;

public class AddBusinessRelationshipWizard extends Wizard {

	private AddBusinessRelationshipWizardPageOne pageOne;
	
	public AddBusinessRelationshipWizard(){
		super();
		this.setWindowTitle("Create a new Business Relationship");
		this.setHelpAvailable(false);
	}	

	@Override
	public void addPages() {
		pageOne = new AddBusinessRelationshipWizardPageOne("Create Business Relationship step one");
		addPage(pageOne);
	}	
	
	@Override
	public boolean performFinish() {

		return false;
	}

}
