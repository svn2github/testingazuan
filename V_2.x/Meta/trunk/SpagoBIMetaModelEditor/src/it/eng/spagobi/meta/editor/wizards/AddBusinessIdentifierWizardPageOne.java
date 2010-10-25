package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

public class AddBusinessIdentifierWizardPageOne extends WizardPage {

	private List tableList;
	private String tableSelected;
	private AddBusinessIdentifierWizardPageTwo pageTwoRef;
	private String defaultTable;
	
	protected AddBusinessIdentifierWizardPageOne(String pageName, String defaultTable) {
		super(pageName);
		setTitle("Business Identifier Creation");
		setDescription("This wizard drives you to create a new Business Identifier in your Business Model.\n"+
				"Plese select a Business Table.");
		ImageDescriptor image = Activator.getImageDescriptor("wizards/createBI.png");
	    if (image!=null) setImageDescriptor(image);	
	    this.defaultTable = defaultTable;
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
        //Important: Setting page control
 		setControl(composite);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		//Business Table List
		Group tableGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		tableGroup.setText("Business Table Selection");
		GridLayout glTable = new GridLayout();
		GridData gd = new GridData(GridData.FILL_BOTH);
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
	}
	
	//populate the list with the Business Tables' names
	private void populateTableList(){
		BusinessModel bm = CoreSingleton.getInstance().getBusinessModel();
		int numTables = bm.getTables().size();
		String tabName;
		for (int i = 0; i < numTables; i++){
			tabName = bm.getTables().get(i).getName();
			tableList.add(tabName);		
		}
	}	

	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
 		if (defaultTable != null){
 			tableList.setSelection(new String[]{defaultTable});
 			tableList.setEnabled(false);
 			setPageComplete(true);
 		}
 		else if(tableSelected != null){
			setPageComplete(true);
			if (pageTwoRef != null)
				pageTwoRef.addTableItems(tableSelected);
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
	 * @param pageTwoRef the pageTwoRef to set
	 */
	public void setPageTwoRef(AddBusinessIdentifierWizardPageTwo pageTwoRef) {
		this.pageTwoRef = pageTwoRef;
	}

	/**
	 * @return the pageTwoRef
	 */
	public AddBusinessIdentifierWizardPageTwo getPageTwoRef() {
		return pageTwoRef;
	}
}
