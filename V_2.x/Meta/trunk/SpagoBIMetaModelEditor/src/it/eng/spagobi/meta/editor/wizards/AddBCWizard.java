/*
 * This class create a wizard used when a table is dragged
 * inside the GraphicEditorView to create a new BusinessClass
 */
package it.eng.spagobi.meta.editor.wizards;

import org.eclipse.jface.wizard.Wizard;

public class AddBCWizard extends Wizard {
	private AddBCWizardPage1 pageOne;
	private AddBCWizardPage2 pageTwo;
	private String originalTableName;

	public AddBCWizard(String originalName){
		super();
		this.setWindowTitle("Create a new Business Class");
		this.setHelpAvailable(false);
		originalTableName = originalName;
	}
	
	@Override
	public void addPages() {
		pageOne = new AddBCWizardPage1("Create BC page",originalTableName);
		pageTwo = new AddBCWizardPage2("Set relationship");
		addPage(pageOne);
		addPage(pageTwo);
	}
	
	@Override
	public boolean performFinish() {
		if (pageTwo.isPageComplete())
			return true;
		return false;
	}

	public void dispose(){
		pageOne.dispose();
	}
	
}
