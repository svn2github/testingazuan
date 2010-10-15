package it.eng.spagobi.meta.editor.wizards;

import java.util.ArrayList;

import it.eng.spagobi.meta.editor.Activator;
import it.eng.spagobi.meta.editor.singleton.CoreSingleton;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessTable;

import org.eclipse.emf.common.util.EList;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.SashForm;


public class AddBusinessRelationshipWizardPageOne extends WizardPage {
	
	private Combo combo, combo_1;
	private List list, list_1, list_2;
	private static int ONE_TO_ONE = 1;
	private static int ONE_TO_MANY = 2;
	private static int MANY_TO_ONE = 3;
	private static int MANY_TO_MANY = 4;
	private int cardinality;
	private java.util.List<BusinessRelationshipContainer> relationshipsContainer;
	
	protected AddBusinessRelationshipWizardPageOne(String pageName) {
		super(pageName);
		setPageComplete(false);
		setTitle("Business Relationship Creation");
		setDescription("This wizard drives you to create a new Business Relationship in your Business Model.\n");
		ImageDescriptor image = Activator.getImageDescriptor("wizards/createBR.png");
	    if (image!=null) setImageDescriptor(image);	
	    relationshipsContainer = new ArrayList<BusinessRelationshipContainer>();
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		
        //Important: Setting page control
 		setControl(composite);
 		composite.setLayout(new GridLayout(1, false));
 		
 		Composite composite_3 = new Composite(composite, SWT.NONE);
 		composite_3.setLayout(new FillLayout(SWT.HORIZONTAL));
 		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		
 		Group grpSourceBusinessTable = new Group(composite_3, SWT.NONE);
 		grpSourceBusinessTable.setText("Source Business Table");
 		grpSourceBusinessTable.setLayout(new GridLayout(1, false));
 		
 		combo = new Combo(grpSourceBusinessTable, SWT.READ_ONLY);
 		combo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		populateLeftCombo();
 		combo.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (combo.getItemCount() > 0) {	
 	 				BusinessTable bc = getBusinessTable(combo.getText());
 	 				//show Table columns
 	 				populateLeftList(bc);

 				}
 			}
 		});
 		
 		Label lblColumns = new Label(grpSourceBusinessTable, SWT.NONE);
 		lblColumns.setText("Source Columns");
 		
 		Composite composite_1 = new Composite(grpSourceBusinessTable, SWT.NONE);
 		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		composite_1.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		list = new List(composite_1, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
 		
 		Group group = new Group(composite_3, SWT.NONE);
 		group.setText("Target Business Table");
 		group.setLayout(new GridLayout(1, false));
 		
 		combo_1 = new Combo(group, SWT.READ_ONLY);
 		combo_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		populateRightCombo();
 		combo_1.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (combo_1.getItemCount() > 0) {
 	 				BusinessTable bc = getBusinessTable(combo_1.getText());
 	 				//show Table columns
 	 				populateRightList(bc);		
 				}
 			}
 		});
 		
 		Label label = new Label(group, SWT.NONE);
 		label.setText("Target Columns");
 		
 		Composite composite_2 = new Composite(group, SWT.NONE);
 		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));
 		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		list_1 = new List(composite_2, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
 		
 		Composite composite_4 = new Composite(composite, SWT.NONE);
 		composite_4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
 		composite_4.setLayout(new GridLayout(3, false));
 		
 		Button btnAddRelationship = new Button(composite_4, SWT.NONE);
 		btnAddRelationship.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if ( (list.getSelection().length != 0) && (list_1.getSelection().length != 0) ){
 					setErrorMessage(null);
 					list_2.add(combo.getText()+"."+list.getSelection()[0]+" -> "+combo_1.getText()+"."+list_1.getSelection()[0]+" "+cardinalityToString(cardinality));
 					
 					//Adding temporary relationship to object container (isn't a real BusinessRelationship yet!)
 					BusinessTable sourceTable = getBusinessTable(combo.getText());
 					BusinessTable targetTable = getBusinessTable(combo_1.getText());
 					BusinessColumn sourceColumn = getBusinessColumn(combo.getText(),list.getSelection()[0]);
 					BusinessColumn targetColumn = getBusinessColumn(combo_1.getText(),list_1.getSelection()[0]);
 					java.util.List<BusinessColumn> sourceColumns = new ArrayList<BusinessColumn>();
 					sourceColumns.add(sourceColumn);
 					java.util.List<BusinessColumn> targetColumns = new ArrayList<BusinessColumn>();
 					targetColumns.add(targetColumn);
 					BusinessRelationshipContainer br = new BusinessRelationshipContainer(sourceTable, targetTable, sourceColumns, targetColumns, cardinality );
 					getRelationshipsContainer().add(br);
 					
 					checkPageComplete();
 				}
 				else {
 					setErrorMessage("You must select a source column and a target column");
 				}
 					
 			}
 		});
 		btnAddRelationship.setText("Add Relationship");
 		
 		Label lblCardinality = new Label(composite_4, SWT.NONE);
 		lblCardinality.setText("Cardinality");
 		
 		Composite composite_6 = new Composite(composite_4, SWT.NONE);
 		composite_6.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
 		composite_6.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		Button btnTo = new Button(composite_6, SWT.RADIO);
 		btnTo.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 					cardinality = ONE_TO_ONE;
 			}
 		});
 		btnTo.setText("1 to 1");
 		
 		Button btnToN = new Button(composite_6, SWT.RADIO);
 		btnToN.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				cardinality = ONE_TO_MANY;
 			}
 		});
 		btnToN.setSelection(true);
 		btnToN.setText("1 to N");
 		
 		Button btnNTo = new Button(composite_6, SWT.RADIO);
 		btnNTo.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				cardinality = MANY_TO_ONE;
 			}
 		});
 		btnNTo.setText("N to 1");
 		
 		Button btnNToN = new Button(composite_6, SWT.RADIO);
 		btnNToN.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				cardinality = MANY_TO_MANY;
 			}
 		});
 		btnNToN.setText("N to N");
 		
 		Group grpRelationship = new Group(composite, SWT.NONE);
 		FillLayout fl_grpRelationship = new FillLayout(SWT.HORIZONTAL);
 		fl_grpRelationship.marginWidth = 5;
 		fl_grpRelationship.marginHeight = 5;
 		grpRelationship.setLayout(fl_grpRelationship);
 		grpRelationship.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
 		grpRelationship.setText("Relationship");
 		
 		Composite composite_5 = new Composite(grpRelationship, SWT.NONE);
 		composite_5.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		list_2 = new List(composite_5, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
 		
 		checkPageComplete();
	}
	
	private void populateLeftCombo(){
		CoreSingleton cs = CoreSingleton.getInstance();
		EList<BusinessTable> businessTables = cs.getBusinessModel().getTables();
		for(BusinessTable table : businessTables){
			combo.add(table.getName());
		}
	}
	
	private void populateRightCombo(){
		combo_1.removeAll();
		CoreSingleton cs = CoreSingleton.getInstance();
		EList<BusinessTable> businessTables = cs.getBusinessModel().getTables();
		for(BusinessTable table : businessTables){
			combo_1.add(table.getName());
		}
	}
	
	private void populateLeftList(BusinessTable businessTable){
		list.removeAll();
		EList<BusinessColumn> businessColumns = businessTable.getColumns();
		for (BusinessColumn column : businessColumns ){
			list.add(column.getName());
		}
	}
	
	private void populateRightList(BusinessTable businessTable){
		list_1.removeAll();
		EList<BusinessColumn> businessColumns = businessTable.getColumns();
		for (BusinessColumn column : businessColumns ){
			list_1.add(column.getName());
		}
	}
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if(list_2.getItemCount() > 0){	
			setPageComplete(true);
		}
		else{			
			setPageComplete(false);
		}		
	}
	
	private BusinessTable getBusinessTable(String tableName){
		CoreSingleton cs = CoreSingleton.getInstance();
		return cs.getBusinessModel().getTable(tableName);
	}
	
	private BusinessColumn getBusinessColumn(String tableName, String columnName){
		CoreSingleton cs = CoreSingleton.getInstance();
		return cs.getBusinessModel().getTable(tableName).getColumn(columnName);
	}
	
	private String cardinalityToString(int cardinality){
		switch(cardinality){
			case 1: return "One to One"; 
			case 2: return "One to Many"; 
			case 3: return "Many to One"; 
			case 4: return "Many to Many"; 
			default: return "One to Many";
		}
	}

	/**
	 * @return the relationshipsContainer
	 */
	public java.util.List<BusinessRelationshipContainer> getRelationshipsContainer() {
		return relationshipsContainer;
	}

}
