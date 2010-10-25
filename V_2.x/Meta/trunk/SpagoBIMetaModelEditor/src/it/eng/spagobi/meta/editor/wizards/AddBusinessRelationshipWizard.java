package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
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
			List<BusinessRelationshipContainer> listBr = pageOne.getRelationshipsContainer();
			for (BusinessRelationshipContainer br : listBr){
				BusinessTable sourceTable = br.getSourceTable();
				BusinessTable destinationTable = br.getDestinationTable();
				List<BusinessColumn> sourceColumns = br.getSourceColumns();
				List<BusinessColumn> destinationColumns = br.getDestinationColumns();
				String relationshipName =  br.getRelationshipName();
				initializer.addRelationship(sourceTable, destinationTable, sourceColumns, destinationColumns, relationshipName);
			}
			return true;
		}
		return false;
	}

}
