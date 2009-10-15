package it.eng.spagobi.studio.documentcomposition.wizards;

import it.eng.spagobi.studio.documentcomposition.editors.model.navigation.Navigation;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardDestinDocPage;
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
	private NewNavigationWizardDestinDocPage newNavigationWizardDestinDocPage;
	

	public SpagoBINavigationWizard() {
		super();

	}
	@Override
	public boolean performFinish() {
	    // Create the entry based on the inputs
	    Navigation navigation = new Navigation();
	    navigation.setNavigationNameText(newNavigationWizardPage.getNavigationNameText().getText());
	    navigation.setMasterDocNameText(newNavigationWizardMasterDocPage.getMasterDocNameText().getText());
	    int selectedDest = newNavigationWizardDestinDocPage.getDestinationDocNameCombo().getSelectionIndex();
	    String sel = newNavigationWizardDestinDocPage.getDestinationDocNameCombo().getItem(selectedDest);
	    navigation.setDestinationDocNameText(sel);
	    navigation.setInputParameterText(newNavigationWizardDestinDocPage.getDestinationInputText().getText());
	    // Return true to exit wizard
	    return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New navigation creation");
		
		newNavigationWizardPage = new NewNavigationWizardPage();
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage();
		newNavigationWizardDestinDocPage = new NewNavigationWizardDestinDocPage();
	}
	
	public void addPages() {
		super.addPages();
		newNavigationWizardPage = new NewNavigationWizardPage("New Navigation");
		addPage(newNavigationWizardPage);
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage("Master document");
		addPage(newNavigationWizardMasterDocPage);
		
		newNavigationWizardDestinDocPage = new NewNavigationWizardDestinDocPage("Destination document");
		addPage(newNavigationWizardDestinDocPage);
	}


}
