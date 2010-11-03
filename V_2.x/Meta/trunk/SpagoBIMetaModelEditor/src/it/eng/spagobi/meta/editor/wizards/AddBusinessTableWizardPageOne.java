package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class AddBusinessTableWizardPageOne extends WizardPage {
	
	private List tableList;
	private String tableSelected;
	private boolean columnSelection;
	private AddBusinessTableWizardPageTwo pageTwoRef;
	private PhysicalTable physicalTable;
	
	protected AddBusinessTableWizardPageOne(String pageName, PhysicalTable physicalTable) {
		super(pageName);
		setTitle("Business Table Creation");
		setDescription("Please select the physical table used to create the Business Table.");
		ImageDescriptor image = Activator.getImageDescriptor("wizards/createBC.png");
	    if (image!=null) setImageDescriptor(image);	
	    this.physicalTable = physicalTable;
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		//Physical Table List
		Group tableGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		tableGroup.setText("Physical Table Selection");
		GridLayout glTable = new GridLayout();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		glTable.numColumns = 1;
		glTable.makeColumnsEqualWidth = false;
		tableGroup.setLayout(glTable);
		tableGroup.setLayoutData(gd);

		Composite compList = new Composite(tableGroup, SWT.NONE);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compList.setLayout(glL);
		compList.setLayoutData(gdL);
 		
		tableList = new List(compList, SWT.BORDER|SWT.SINGLE|SWT.V_SCROLL|SWT.H_SCROLL);
 		GridData gdList = new GridData(GridData.FILL_BOTH);
 		gdList.heightHint = 250;
 		tableList.setLayoutData(gdList);
 		
        //Important: Setting page control
 		setControl(composite);
 		
 		populateTableList();
 		
		//adding listener to List
 		tableList.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				String[] sel = tableList.getSelection();
				setTableSelected(sel[0]);
				checkPageComplete();
			}
		});
 		
 		checkPageComplete();
 		
		//select physical table and disable widget
		if (physicalTable != null){
			String[] items = tableList.getItems();
			for (int i=0; i<items.length ; i++){
				if (items[i].equals(physicalTable.getName())){
					tableList.select(i);
					setTableSelected(items[i]);
					tableList.setEnabled(false);					
					break;
				}
			}
		}
 		

 		
	}
	
	
	//populate the list with the Physical Tables' names
	private void populateTableList(){
		PhysicalModel pm = CoreSingleton.getInstance().getPhysicalModel();
		int numTables = pm.getTables().size();
		String tabName;
		for (int i = 0; i < numTables; i++){
			tabName = pm.getTables().get(i).getName();
			tableList.add(tabName);		
		}
	}

	//check if the right conditions to go forward occurred
	public void checkPageComplete(){
		if(tableSelected != null){
			setPageComplete(true);
			if (pageTwoRef != null){
				setColumnSelection(true);
				if (physicalTable == null){
					pageTwoRef.addTableItems(tableSelected);
				}
			}
		}
		else{			
			setPageComplete(false);
		}		
	}	
	
	/**
	 * @param tableSelected the tableSelected to set
	 */
	public void setTableSelected(String tableSelected) {
		this.tableSelected = tableSelected;
	}

	/**
	 * @return the tableSelected
	 */
	public String getTableSelected() {
		return tableSelected;
	}

	/**
	 * @param pageTwoRef the pageThreeRef to set
	 */
	public void setPageTwoRef(AddBusinessTableWizardPageTwo pageTwoRef) {
		this.pageTwoRef = pageTwoRef;
	}

	/**
	 * @return the pageThreeRef
	 */
	public AddBusinessTableWizardPageTwo getPageTwoRef() {
		return pageTwoRef;
	}

	/**
	 * @param columnSelection the columnSelection to set
	 */
	public void setColumnSelection(boolean columnSelection) {
		this.columnSelection = columnSelection;
	}

	/**
	 * @return the columnSelection
	 */
	public boolean isColumnSelection() {
		return columnSelection;
	}

}
