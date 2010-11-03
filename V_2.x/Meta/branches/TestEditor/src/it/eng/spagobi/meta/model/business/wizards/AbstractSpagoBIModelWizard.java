package it.eng.spagobi.meta.model.business.wizards;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.commons.IModelObjectFilter;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.EditBusinessColumnsCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;

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
		if (getStartingPage().isPageComplete()){
			
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

	

	
}
