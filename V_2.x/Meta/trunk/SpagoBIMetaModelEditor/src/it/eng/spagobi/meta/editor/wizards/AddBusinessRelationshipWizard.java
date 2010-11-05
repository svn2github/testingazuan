package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.util.List;

import org.eclipse.jface.wizard.Wizard;

public class AddBusinessRelationshipWizard extends Wizard {

	private AddBusinessRelationshipWizardPageOne pageOne;
	private String defaultTable;
	
	public AddBusinessRelationshipWizard(String defaultTable){
		super();
		this.setWindowTitle("Create a new Business Relationship");
		this.setHelpAvailable(false);
		this.defaultTable = defaultTable;
	}	

	@Override
	public void addPages() {
		pageOne = new AddBusinessRelationshipWizardPageOne("Create Business Relationship step one", defaultTable);
		addPage(pageOne);
	}	
	
	@Override
	public boolean performFinish() {
		if(pageOne.isPageComplete()){
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			//create real BusinessRelationship objects
			List<BusinessRelationshipDescriptor> listBr = pageOne.getRelationshipsContainer();
			for (BusinessRelationshipDescriptor br : listBr){
				initializer.addRelationship(br);
			}
			return true;
		}
		return false;
	}

}
