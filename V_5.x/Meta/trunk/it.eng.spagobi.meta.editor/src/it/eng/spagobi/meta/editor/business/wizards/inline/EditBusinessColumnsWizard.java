/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.TableItem;

public class EditBusinessColumnsWizard extends AbstractSpagoBIModelWizard {

	BusinessColumnSet businessColumnSet;
	EditBusinessColumnsWizardPagePhysicalTableSelection pageZero;
	IWizardPage pageOne;
	
	
	public EditBusinessColumnsWizard(BusinessColumnSet businessColumnSet, EditingDomain editingDomain, ISpagoBIModelCommand command){
		super(editingDomain, command);
		this.setWindowTitle("Edit business columns");
		this.setHelpAvailable(false);	
		this.businessColumnSet = businessColumnSet;
		
	}
	
	@Override
	public void addPages() {
		if (businessColumnSet instanceof BusinessTable){
			BusinessTable businessTable = (BusinessTable)businessColumnSet;
			//check for empty Physical Table reference
			if (businessTable.getPhysicalTable() == null){
				pageZero = new EditBusinessColumnsWizardPagePhysicalTableSelection("Edit Business Column page zero",businessColumnSet.getModel());
			}
		}
		pageOne = new EditBusinessColumnsWizardPage("Edit Business Column page one",businessColumnSet);
		if (pageZero != null){
			pageZero.setColumnSelectionPage((EditBusinessColumnsWizardPage)pageOne);
			addPage(pageZero);
		}	
		addPage( pageOne );
	}
	
	public CommandParameter getCommandInputParameter(){
		EditBusinessColumnsWizardPage wizardPage = (EditBusinessColumnsWizardPage)pageOne;
		
		TableItem[] columnsToImport = wizardPage.getColumnsToImport();
		int numCol = columnsToImport.length;
		List<PhysicalColumn> colList = new ArrayList<PhysicalColumn>();
		PhysicalColumn pc = null;
		for (int i=0; i<numCol; i++){
			if ( columnsToImport[i].getData() instanceof PhysicalColumn ){
				pc = ((PhysicalColumn)columnsToImport[i].getData());
			}
			else if ( columnsToImport[i].getData() instanceof SimpleBusinessColumn ){
				pc = ((SimpleBusinessColumn)columnsToImport[i].getData()).getPhysicalColumn();
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
