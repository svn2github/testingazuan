package it.eng.spagobi.studio.documentcomposition.wizards;

import it.eng.spagobi.studio.documentcomposition.editors.model.navigation.Navigation;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardMasterDocPage;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardPage;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class SpagoBINavigationWizard extends Wizard implements INewWizard{


	// dashboard creation page
	private NewNavigationWizardPage newNavigationWizardPage;
	private NewNavigationWizardMasterDocPage newNavigationWizardMasterDocPage;
	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	// the workbench instance
	protected IWorkbench workbench;
	
	
	public SpagoBINavigationWizard() {
		super();
		/*
		addPage(newNavigationWizardPage);
		addPage(newNavigationWizardMasterDocPage);
		*/
		// TODO Auto-generated constructor stub
	}
	@Override
	public boolean performFinish() {
		System.out.println("performFinish del wizard!!!");
	    // Create the entry based on the inputs
	    Navigation navigation = new Navigation();
	    navigation.setNavigationNameText(newNavigationWizardPage.getNavigationNameText().getText());
	    navigation.setMasterDocNameText(newNavigationWizardMasterDocPage.getMasterDocNameText().getText());

	    // Return true to exit wizard
	    return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New Jasper template creation");
/*		this.workbench = workbench;
		this.selection = selection;*/
		
		newNavigationWizardPage = new NewNavigationWizardPage();
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage();
		
	}
	
	public void addPages() {
		super.addPages();
		newNavigationWizardPage = new NewNavigationWizardPage("New Navigation");
		addPage(newNavigationWizardPage);
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage("New Navigation");
		addPage(newNavigationWizardMasterDocPage);
	}


}
