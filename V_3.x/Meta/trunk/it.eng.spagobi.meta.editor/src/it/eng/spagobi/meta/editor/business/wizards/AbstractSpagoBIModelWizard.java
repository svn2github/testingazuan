package it.eng.spagobi.meta.editor.business.wizards;

import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.Wizard;

public abstract class AbstractSpagoBIModelWizard extends Wizard {

	AbstractSpagoBIModelCommand performFinishCommand;
	EditingDomain editingDomain;
	
	public AbstractSpagoBIModelWizard(EditingDomain editingDomain, AbstractSpagoBIModelCommand command){
		super();
		this.editingDomain = editingDomain;
		this.performFinishCommand = command;
	}
	
	@Override
	public boolean performFinish() {
		if (isWizardComplete()){
			
			performFinishCommand.setParameter( getCommandInputParameter() );
			
			
			// this guard is for extra security, but should not be necessary
		    if (editingDomain != null && performFinishCommand != null) {
		    	// use up the command
		    	editingDomain.getCommandStack().execute(performFinishCommand);
		    }
	
			return true;
		} else {
			return false;
		}
	}
	
	public abstract CommandParameter getCommandInputParameter();
	public abstract boolean isWizardComplete();

	

	
}
