/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
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
