/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.ArrayList;
import java.util.Collection;

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
	private BusinessColumnSet businessColumnSet;
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 

	/**
	 * @param editingDomain
	 * @param command
	 */
	public AddBusinessIdentifierWizard(EditingDomain editingDomain,
			ISpagoBIModelCommand command, String defaultTable, BusinessColumnSet businessColumnSet) {
		super(editingDomain, command);
		this.setWindowTitle(RL.getString("business.editor.wizard.addbusinessidentifier.title"));
		this.setHelpAvailable(false);		
		this.defaultTable = defaultTable;
		this.businessColumnSet = businessColumnSet;
	}
	
	public void addPages() {
		if (defaultTable == null){
			pageOne = new AddBusinessIdentifierWizardPageBusinessTableSelection("Add Business Identifier page one", defaultTable, businessColumnSet);
			addPage(pageOne);
		}
		pageTwo = new AddBusinessIdentifierWizardPageColumnSelection("Add Business Identifier page two", defaultTable, businessColumnSet);
		addPage(pageTwo);	
		if (pageOne != null){
			pageOne.setPageTwoRef(pageTwo);
		}
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
		return new CommandParameter(businessColumnSet, null, colList, new ArrayList<Object>());

	}

	@Override
	public boolean isWizardComplete() {
		if (pageTwo.isPageComplete()){
			return true;
		}
		return false;
	}

}
