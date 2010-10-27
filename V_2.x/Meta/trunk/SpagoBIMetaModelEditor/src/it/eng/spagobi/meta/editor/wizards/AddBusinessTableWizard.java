/*
 * Wizard for creating a new Business Table
 */
package it.eng.spagobi.meta.editor.wizards;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.commons.IModelObjectFilter;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.TableItem;

public class AddBusinessTableWizard extends Wizard {
	
	private AddBusinessTableWizardPageOne pageOne;
	private AddBusinessTableWizardPageTwo pageTwo;
	private TableItem[] columnsToImport;
	private PhysicalTable physicalTable;
	
	public AddBusinessTableWizard(PhysicalTable physicalTable){
		super();
		this.setWindowTitle("Create a new Business Table");
		this.setHelpAvailable(false);
		this.physicalTable = physicalTable;
	}
	
	@Override
	public void addPages() {
		pageOne = new AddBusinessTableWizardPageOne("Create Business Table step one", physicalTable);
		addPage(pageOne);
		pageTwo = new AddBusinessTableWizardPageTwo("Create Business Table step two", pageOne, physicalTable);
		addPage(pageTwo);
		pageOne.setPageTwoRef(pageTwo);
		
	}
	
	@Override
	public boolean performFinish() {				
		if (pageTwo.isPageComplete() && pageTwo.isColumnSelected()){
			//table to import
			String tableName = pageOne.getTableSelected();
			//columns to import
			columnsToImport = pageTwo.getColumnsToImport();
			int numCol = columnsToImport.length;
			List<PhysicalColumn> colList = new ArrayList<PhysicalColumn>();
			for (int i=0; i<numCol; i++){
				PhysicalColumn pc = ((PhysicalColumn)columnsToImport[i].getData());
				colList.add(pc);
			}
			//Create Business Table from a Physical Table with column filter
			BusinessModel bm = CoreSingleton.getInstance().getBusinessModel();
			PhysicalModel pm = CoreSingleton.getInstance().getPhysicalModel();
			PhysicalTable pTable = pm.getTable(tableName);
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			initializer.addTable(pTable, new ColumnFilter(colList), bm, true);			
			return true;
		}
		if (pageOne.isPageComplete()){
			String tableName = pageOne.getTableSelected();
			//Create Business Table from a Physical Table
			BusinessModel bm = CoreSingleton.getInstance().getBusinessModel();
			PhysicalModel pm = CoreSingleton.getInstance().getPhysicalModel();
			PhysicalTable pTable = pm.getTable(tableName);
			BusinessModelInitializer initializer = new BusinessModelInitializer();
			initializer.addTable(pTable, bm, true);
			return true;
		}
			return false;
	}
	
	
	public void dispose(){
		pageOne.dispose();
		pageTwo.dispose();
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
