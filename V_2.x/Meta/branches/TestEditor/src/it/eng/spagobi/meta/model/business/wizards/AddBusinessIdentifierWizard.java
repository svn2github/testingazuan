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
package it.eng.spagobi.meta.model.business.wizards;

import java.util.ArrayList;
import java.util.Collection;


import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.AbstractSpagoBIModelCommand;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.swt.widgets.TableItem;

/**
 * @author cortella
 *
 */
public class AddBusinessIdentifierWizard extends AbstractSpagoBIModelWizard {

	private String defaultTable;
	private AddBusinessIdentifierWizardPageBusinessTableSelection pageOne;
	private AddBusinessIdentifierWizardPageColumnSelection pageTwo;
	private BusinessTable businessTable;

	/**
	 * @param editingDomain
	 * @param command
	 */
	public AddBusinessIdentifierWizard(EditingDomain editingDomain,
			AbstractSpagoBIModelCommand command, String defaultTable, BusinessTable businessTable) {
		super(editingDomain, command);
		this.setWindowTitle("Create a new Business Identifier");
		this.setHelpAvailable(false);		
		this.defaultTable = defaultTable;
		this.businessTable = businessTable;
	}
	
	public void addPages() {
		pageOne = new AddBusinessIdentifierWizardPageBusinessTableSelection("Add Business Identifier page one", defaultTable, businessTable);
		addPage(pageOne);
		pageTwo = new AddBusinessIdentifierWizardPageColumnSelection("Add Business Identifier page two", defaultTable, businessTable);
		addPage(pageTwo);	
		pageOne.setPageTwoRef(pageTwo);
	}

	@Override
	public CommandParameter getCommandInputParameter() {
		String tableName;
		if (defaultTable == null){
			tableName = pageOne.getTableSelected();
		}
		else{
			tableName = defaultTable;
		}
			
		//getting columns to import inside Business Identifier
		TableItem[] columnsToImport = pageTwo.getColumnsToImport();
		int numCol = columnsToImport.length;
		Collection<BusinessColumn> colList = new ArrayList<BusinessColumn>();
		for (int i=0; i<numCol; i++){
			BusinessColumn bc = ((BusinessColumn)columnsToImport[i].getData());
			colList.add(bc);
		}
		return new CommandParameter(businessTable, null, colList, new ArrayList<Object>());

	}

	@Override
	public boolean isWizardComplete() {
		if (pageTwo.isPageComplete()){
			return true;
		}
		return false;
	}

}
