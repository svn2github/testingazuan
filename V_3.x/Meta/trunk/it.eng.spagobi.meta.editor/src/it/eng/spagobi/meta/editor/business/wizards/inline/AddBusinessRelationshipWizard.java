package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;

public class AddBusinessRelationshipWizard extends AbstractSpagoBIModelWizard {

	BusinessModel model;
	BusinessColumnSet sourceTable;
	BusinessColumnSet destinationTable;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	
	public AddBusinessRelationshipWizard(BusinessModel model, BusinessColumnSet sourceTable, BusinessColumnSet destinationTable, EditingDomain editingDomain, AbstractSpagoBIModelCommand command){
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.addbusinessrelationship.title"));
		this.setHelpAvailable(false);	
		this.model = model;
		this.sourceTable = sourceTable;
		this.destinationTable = destinationTable;
		
	}
	
	@Override
	public void addPages() {
		IWizardPage pageOne = new AddBusinessRelationshipWizardPage("Add business relationship", model, sourceTable, destinationTable);
		addPage( pageOne );
	}
	
	public CommandParameter getCommandInputParameter(){
		AddBusinessRelationshipWizardPage wizardPage = (AddBusinessRelationshipWizardPage)this.getStartingPage();

		return new CommandParameter(sourceTable, null, wizardPage.getRelationshipDescriptor(), new ArrayList<Object>());

	}
	
	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
