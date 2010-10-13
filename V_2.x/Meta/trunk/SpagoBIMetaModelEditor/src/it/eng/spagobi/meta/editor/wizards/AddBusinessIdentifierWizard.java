/*
 * Wizard for creating a new Business Identifier
 */
package it.eng.spagobi.meta.editor.wizards;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;

public class AddBusinessIdentifierWizard extends Wizard {

	AddBusinessIdentifierWizardPageOne pageOne;
	AddBusinessIdentifierWizardPageTwo pageTwo;
	
	public AddBusinessIdentifierWizard(){
		super();
		this.setWindowTitle("Create a new Business Identifier");
		this.setHelpAvailable(false);		
	}

	@Override
	public void addPages() {
		pageOne = new AddBusinessIdentifierWizardPageOne("Add Business Identifier page one");
		addPage(pageOne);
		pageTwo = new AddBusinessIdentifierWizardPageTwo("Add Business Identifier page two");
		addPage(pageTwo);	
		pageOne.setPageTwoRef(pageTwo);
	}
	@Override
	public boolean performFinish() {
		if (pageTwo.isPageComplete()){
			String tableName = pageOne.getTableSelected();
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			
			//getting columns to import inside Business Identifier
			TableItem[] columnsToImport = pageTwo.getColumnsToImport();
			int numCol = columnsToImport.length;
			List<BusinessColumn> colList = new ArrayList<BusinessColumn>();
			for (int i=0; i<numCol; i++){
				BusinessColumn bc = ((BusinessColumn)columnsToImport[i].getData());
				colList.add(bc);
			}
			//Checking if a Business Identifier for this Business Table already exists
			CoreSingleton cs = CoreSingleton.getInstance();
			BusinessTable bizTable = cs.getBusinessModel().getTable(tableName);
			BusinessIdentifier bizIdentifier = cs.getBusinessModel().getIdentifier(bizTable);
			if (bizIdentifier != null){
				//Business Identifier already exists, substitution
				String identifierName = bizIdentifier.getName();
				cs.getBusinessModel().getIdentifiers().remove(bizIdentifier);
				initializer.addIdentifier(identifierName, bizTable, colList);			
			}
			else {
				//Business Identifier doesn't exists, create
				initializer.addIdentifier(tableName, bizTable, colList);
			}
				
			return true;			
		}
		else
			return false;
	}

}
