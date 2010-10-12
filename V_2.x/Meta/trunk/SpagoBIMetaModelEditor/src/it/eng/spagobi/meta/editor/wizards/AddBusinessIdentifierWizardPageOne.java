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
	
	protected AddBusinessIdentifierWizardPageOne(String pageName) {
		super(pageName);
		setTitle("Business Identifier Creation");
		setDescription("This wizard drives you to create a new Business Identifier in your Business Model.\n"+
				"Plese select a Business Table.");
		ImageDescriptor image = Activator.getImageDescriptor("wizards/createBC.png");
	    if (image!=null) setImageDescriptor(image);	
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		//Business Table List
		Group tableGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		tableGroup.setText("Business Table Selection");
		GridLayout glTable = new GridLayout();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		glTable.numColumns = 1;
		glTable.makeColumnsEqualWidth = false;
		tableGroup.setLayout(glTable);
		tableGroup.setLayoutData(gd);

	    ScrolledComposite sc = new ScrolledComposite(tableGroup, SWT.H_SCROLL
	            | SWT.V_SCROLL);
		Composite compList = new Composite(sc, SWT.NONE);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compList.setLayout(glL);
		compList.setLayoutData(gdL);
 		tableList = new List(compList, SWT.BORDER);
 		tableList.setLayoutData(gdL);
 		
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

 	    // Set the child as the scrolled content of the ScrolledComposite
 	    sc.setContent(compList);
		GridLayout glS = new GridLayout();
		GridData gdS = new GridData(GridData.FILL_BOTH);
 	    sc.setLayout(glS);
 	    sc.setLayoutData(gdS);

 	    // Expand both horizontally and vertically
 	    sc.setExpandHorizontal(true);
 	    sc.setExpandVertical(true);
 	    
 	    checkPageComplete();
				
        //Important: Setting page control
 		setControl(composite);

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
		if(tableSelected != null){
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
