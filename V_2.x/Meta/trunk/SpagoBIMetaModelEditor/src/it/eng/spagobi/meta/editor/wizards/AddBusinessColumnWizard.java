package it.eng.spagobi.meta.editor.wizards;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.commons.IModelObjectFilter;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;

public class AddBusinessColumnWizard extends Wizard {

	private AddBusinessColumnWizardPageOne pageOne;
	private BusinessTable businessTable;
	
	public AddBusinessColumnWizard(BusinessTable businessTable){
		super();
		this.setWindowTitle("Edit Business Table Columns");
		this.setHelpAvailable(false);		
		this.businessTable = businessTable;
	}
	
	@Override
	public void addPages() {
		pageOne = new AddBusinessColumnWizardPageOne("Edit Business Column page one",businessTable);
		addPage(pageOne);
	}
	
	@Override
	public boolean performFinish() {
		if (pageOne.isPageComplete()){
			TableItem[] columnsToImport = pageOne.getColumnsToImport();
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
			
			//Create Business Table from a Physical Table with column filter
			BusinessModel bm = CoreSingleton.getInstance().getBusinessModel();
			PhysicalTable pTable = businessTable.getPhysicalTable();
			//remove original table
			bm.getTables().remove(businessTable);
			//create new table
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			initializer.addTable(pTable, new ColumnFilter(colList), bm, true);			
			return true;
		}
		else
			return false;
	}

	
	/*
	 * Inner class that implements IModelObjectFilter
	 */
	private class ColumnFilter implements IModelObjectFilter{

		List<PhysicalColumn> columnsTrue;
		public ColumnFilter(List<PhysicalColumn> columnsToMantain){
			columnsTrue = columnsToMantain;
		}
		@Override
		public boolean filter(ModelObject o) {
			if (columnsTrue.contains((PhysicalColumn)o))
				return false;
			else
				return true;
		}		
	}	
	
}
