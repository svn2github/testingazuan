package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

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

public class AddBusinessTableWizardPageTwo extends WizardPage {
	
	private List tableList;
	private String tableSelected;
	private boolean columnSelection;
	private AddBusinessTableWizardPageOne pageOneRef;
	private AddBusinessTableWizardPageThree pageThreeRef;
	
	protected AddBusinessTableWizardPageTwo(String pageName, AddBusinessTableWizardPageOne pageOne) {
		super(pageName);
		setTitle("Business Table Creation");
		setDescription("Please select the physical table used to create the Business Table.");
		ImageDescriptor image = Activator.getImageDescriptor("wizards/createBC.png");
	    if (image!=null) setImageDescriptor(image);	
	    pageOneRef = pageOne;
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
				
        //Important: Setting page control
 		setControl(composite);

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
	private void checkPageComplete(){
		if(tableSelected != null){
			setPageComplete(true);
			if (pageThreeRef != null)
				pageThreeRef.addTableItems(tableSelected);
		}
		else{			
			setPageComplete(false);
		}		
	}	
	
	public IWizardPage getNextPage() {
		//check if column selection is needed
		setColumnSelection(pageOneRef.isColumnSelection());
		//go to finish page
		if (!isColumnSelection()) {
			//return getWizard().getPage("Create Business Table step four");
			return null;
		}
		//go to the column selection page
		return getWizard().getPage("Create Business Table step three");
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
	 * @param pageThreeRef the pageThreeRef to set
	 */
	public void setPageThreeRef(AddBusinessTableWizardPageThree pageThreeRef) {
		this.pageThreeRef = pageThreeRef;
	}

	/**
	 * @return the pageThreeRef
	 */
	public AddBusinessTableWizardPageThree getPageThreeRef() {
		return pageThreeRef;
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
