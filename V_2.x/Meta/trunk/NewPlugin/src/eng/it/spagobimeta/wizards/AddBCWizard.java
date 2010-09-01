/*
 * This class create a wizard used when a table is dragged
 * inside the GraphicEditorView to create a new BusinessClass
 */
package eng.it.spagobimeta.wizards;

import org.eclipse.jface.wizard.Wizard;

public class AddBCWizard extends Wizard {
	private AddBCWizardPage1 pageOne;
	private String originalTableName;

	public AddBCWizard(String originalName){
		super();
		setNeedsProgressMonitor(true);
		this.setWindowTitle("Create a new Business Class");
		this.setHelpAvailable(false);
		originalTableName = originalName;
	}
	
	@Override
	public void addPages() {
		pageOne = new AddBCWizardPage1("Create BC page",originalTableName);
		addPage(pageOne);
	}
	
	@Override
	public boolean performFinish() {
		if (pageOne.isPageComplete())
			return true;
		return false;
	}

	public void dispose(){
		pageOne.dispose();
	}
	
}
