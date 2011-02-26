package it.eng.spagobi.meta.model.business.wizards;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
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
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;

public class EditBusinessColumnsWizard extends AbstractSpagoBIModelWizard {

	BusinessColumnSet businessColumnSet;
	
	
	public EditBusinessColumnsWizard(BusinessColumnSet businessColumnSet, EditingDomain editingDomain, AbstractSpagoBIModelCommand command){
		super(editingDomain, command);
		this.setWindowTitle("Edit business columns");
		this.setHelpAvailable(false);	
		this.businessColumnSet = businessColumnSet;
		
	}
	
	@Override
	public void addPages() {
		IWizardPage pageOne = new EditBusinessColumnsWizardPage("Edit Business Column page one",businessColumnSet);
		addPage( pageOne );
	}
	
	public CommandParameter getCommandInputParameter(){
		EditBusinessColumnsWizardPage wizardPage = (EditBusinessColumnsWizardPage)this.getStartingPage();
		
		TableItem[] columnsToImport = wizardPage.getColumnsToImport();
		int numCol = columnsToImport.length;
		List<PhysicalColumn> colList = new ArrayList<PhysicalColumn>();
		PhysicalColumn pc = null;
		for (int i=0; i<numCol; i++){
			if ( columnsToImport[i].getData() instanceof PhysicalColumn ){
				pc = ((PhysicalColumn)columnsToImport[i].getData());
			}
			else if ( columnsToImport[i].getData() instanceof BusinessColumn ){
				pc = ((BusinessColumn)columnsToImport[i].getData()).getPhysicalColumn();
			}
			colList.add(pc);
		}
		
		return new CommandParameter(businessColumnSet, null, colList, new ArrayList<Object>());
	}

	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
