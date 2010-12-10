package it.eng.spagobi.meta.model.business.wizards;

import java.util.ArrayList;

import it.eng.spagobi.meta.initializer.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.test.TestEditorPlugin;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.edit.ui.provider.ExtendedImageRegistry;
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
import org.eclipse.swt.widgets.Text;


public class AddBusinessRelationshipWizardPage extends WizardPage {
	
	private Text relationshipNameInputField;
	private Combo sourceTableCombo;
	private Combo destinationTableCombo;
	private List sourceColumnList;
	private List destinationColumnList;
	private List columnCorrelationList;
	private int cardinality;
	
	java.util.List<BusinessColumn> sourceColumns;
	java.util.List<BusinessColumn> destinationColumns;
	
	private static int ONE_TO_ONE = 1;
	private static int ONE_TO_MANY = 2;
	private static int MANY_TO_ONE = 3;
	private static int MANY_TO_MANY = 4;
	
	private BusinessModel model;
	private BusinessColumnSet sourceTable;
	private BusinessColumnSet destinationTable;
	
	protected AddBusinessRelationshipWizardPage(String pageName, BusinessModel model, BusinessColumnSet sourceTable, BusinessColumnSet destinationTable) {
		super(pageName);
		setPageComplete(false);
		setTitle("Business Relationship Creation");
		setDescription("This wizard drives you to create a new Business Relationship in your Business Model.\n");
		ImageDescriptor image = ExtendedImageRegistry.INSTANCE.getImageDescriptor(TestEditorPlugin.INSTANCE.getImage("wizards/createBR.png"));
	    if (image!=null) setImageDescriptor(image);	
	    
	    this.model = model;
	    this.sourceTable = sourceTable;
	    this.destinationTable = destinationTable;
	    sourceColumns = new ArrayList<BusinessColumn>();
	    destinationColumns = new ArrayList<BusinessColumn>();
	}

	@Override
	public void createControl(Composite parent) {

		Composite mainComposite = new Composite(parent, SWT.NULL);
 		setControl(mainComposite);
 		mainComposite.setLayout(new GridLayout(1, false));
 		
 		initRelationshipNameGroup(mainComposite, SWT.NONE);
 		initCorrelationGroup(mainComposite, SWT.NONE); 		
 		initCardinalityGroup(mainComposite, SWT.NONE);
 		initCorrelationSummaryGroup(mainComposite, SWT.NONE);
 		
 		if(selectTable(sourceTableCombo, sourceTable, true)) {
 			populateSourceColumnList(sourceTable);
 		}
 		
 		if(selectTable(destinationTableCombo, destinationTable, true)) {
 			populateDestinationColumnList(destinationTable);
 		}
 		
 		checkPageComplete();
	}
	
	private boolean selectTable(Combo combo, BusinessColumnSet table, boolean disableOnSelectCombo) {
		if (table != null){
 			String[] items = combo.getItems();
 			for (int i=0; i < items.length; i++){
 				if (items[i].equals(table.getName())){
 					combo.select(i);
 					if(disableOnSelectCombo) combo.setEnabled(false);
 					return true;
 				}
 			}
 		}
		return false;
	}
	
	private void initRelationshipNameGroup(Composite parent, int style) {
		Composite composite_name = new Composite(parent,style);
 		composite_name.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		composite_name.setLayout(new GridLayout(2, false)); 		
 		Label lblBusinessRelationshipName = new Label(composite_name, SWT.NONE);
 		lblBusinessRelationshipName.setText("Business Relationship Name");
 		relationshipNameInputField = new Text(composite_name, SWT.BORDER);
 		relationshipNameInputField.setText("br name");
 		relationshipNameInputField.setToolTipText("Name to assign to the Business Relationship");
 		relationshipNameInputField.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
	}
	
	private void initCorrelationGroup(Composite parent, int style) {
		Composite columnCorrelationGroup = new Composite(parent, style);
 		columnCorrelationGroup.setLayout(new FillLayout(SWT.HORIZONTAL));
 		columnCorrelationGroup.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		initSourceGroup(columnCorrelationGroup, SWT.NONE);
 		initDestinationGroup(columnCorrelationGroup, SWT.NONE);
	}
	
	public void initCardinalityGroup(Composite parent, int style){
		Composite cardinalityGroup = new Composite(parent, style);
		cardinalityGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		cardinalityGroup.setLayout(new GridLayout(3, false));
		
		Button btnAddRelationship = new Button(cardinalityGroup, SWT.NONE);
		btnAddRelationship.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if ( (sourceColumnList.getSelection().length != 0) && (destinationColumnList.getSelection().length != 0) && (getBusinessRelationshipName().length() != 0) ){
					setErrorMessage(null);
					columnCorrelationList.add(getBusinessRelationshipName()+": "+ sourceTableCombo.getText() + "." + sourceColumnList.getSelection()[0] + " -> "+destinationTableCombo.getText()+"."+destinationColumnList.getSelection()[0]+" "+cardinalityToString(cardinality));
					sourceColumns.add( getSourceColumn() );
					destinationColumns.add( getDestinationColumn() );
					checkPageComplete();
				} else {
					setErrorMessage("You must select a source column, a target column and specify a name");
				}
					
			}
		});
		btnAddRelationship.setText("Add Relationship"); 		
		Label lblCardinality = new Label(cardinalityGroup, SWT.NONE);
		lblCardinality.setText("Cardinality");
		
		Composite compCardinality = new Composite(cardinalityGroup, SWT.NONE);
		compCardinality.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, true, false, 1, 1));
		compCardinality.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Button btnTo = new Button(compCardinality, SWT.RADIO);
		btnTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					cardinality = ONE_TO_ONE;
			}
		});
		btnTo.setText("1 to 1");
		
		Button btnToN = new Button(compCardinality, SWT.RADIO);
		btnToN.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cardinality = ONE_TO_MANY;
			}
		});
		btnToN.setSelection(true);
		btnToN.setText("1 to N");
		
		Button btnNTo = new Button(compCardinality, SWT.RADIO);
		btnNTo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cardinality = MANY_TO_ONE;
			}
		});
		btnNTo.setText("N to 1");
		
		Button btnNToN = new Button(compCardinality, SWT.RADIO);
		btnNToN.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				cardinality = MANY_TO_MANY;
			}
		});
		btnNToN.setText("N to N");
	}
	
	private void initCorrelationSummaryGroup(Composite parent, int style) {
		Group correlationSummaryGroup = new Group(parent, style);
 		FillLayout fl_grpRelationship = new FillLayout(SWT.HORIZONTAL);
 		fl_grpRelationship.marginWidth = 5;
 		fl_grpRelationship.marginHeight = 5;
 		correlationSummaryGroup.setLayout(fl_grpRelationship);
 		correlationSummaryGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
 		correlationSummaryGroup.setText("Relationship");
 		
 		Composite compCorrelationSummary = new Composite(correlationSummaryGroup, SWT.NONE);
 		compCorrelationSummary.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		columnCorrelationList = new List(compCorrelationSummary, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
	}
	
	private void initSourceGroup(Composite parent, int style) {
		Group sourceGroup = new Group(parent, style);
		sourceGroup.setText("Source Business Table");
		sourceGroup.setLayout(new GridLayout(1, false));
		initSourceTableCombo(sourceGroup, SWT.READ_ONLY);
		Label lblColumns = new Label(sourceGroup, SWT.NONE);
		lblColumns.setText("Source Columns");
		initSourceColumnList(sourceGroup, SWT.NONE);
	}
	
	private void initDestinationGroup(Composite parent, int style) {
		Group destinationGroup = new Group(parent, style);
		destinationGroup.setText("Target Business Table");
		destinationGroup.setLayout(new GridLayout(1, false));
		initDestinationTableCombo(destinationGroup, SWT.READ_ONLY);
		Label label = new Label(destinationGroup, SWT.NONE);
		label.setText("Target Columns");
		initDestinationColumnList(destinationGroup, SWT.NONE);
	}
		
	private void initSourceColumnList(Composite parent, int style) {
		Composite compSourceColumnList = new Composite(parent, style);
 		GridData gdCompSourceColumnList = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
 		gdCompSourceColumnList.heightHint = 100;
 		compSourceColumnList.setLayoutData(gdCompSourceColumnList);
 		compSourceColumnList.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		sourceColumnList = new List(compSourceColumnList, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
 		sourceColumnList.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (destinationColumnList.getSelectionCount() > 0){
 					//set suggested name
 					relationshipNameInputField.setText("BR_"+sourceTableCombo.getText()+"_"+sourceColumnList.getSelection()[0]+"_"+destinationTableCombo.getText()+"_"+destinationColumnList.getSelection()[0]);
 				}
 			}
 		});
	}
	
	private void initDestinationColumnList(Composite parent, int style) {
		Composite compDestinationColumnList = new Composite(parent, style);
 		GridData gdCompDestinationColumnList = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
 		gdCompDestinationColumnList.heightHint = 100;
 		compDestinationColumnList.setLayoutData(gdCompDestinationColumnList);
 		compDestinationColumnList.setLayout(new FillLayout(SWT.HORIZONTAL));
 		
 		destinationColumnList = new List(compDestinationColumnList, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
 		destinationColumnList.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (sourceColumnList.getSelectionCount() > 0){
 					//set suggested name
 					relationshipNameInputField.setText("BR_"+sourceTableCombo.getText()+"_"+sourceColumnList.getSelection()[0]+"_"+destinationTableCombo.getText()+"_"+destinationColumnList.getSelection()[0]);
 				}
 			}
 		});
	}
	
	private void initSourceTableCombo(Composite parent, int style) {
		sourceTableCombo = new Combo(parent, style);
 		sourceTableCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		populateSourceTableCombo();
 		sourceTableCombo.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (sourceTableCombo.getItemCount() > 0) {	
 	 				populateSourceColumnList( getSourceTable() );
 				}
 			}
 		});
	}
	
	private void initDestinationTableCombo(Composite parent, int style) {
		destinationTableCombo = new Combo(parent, style);
 		destinationTableCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
 		populateDestinationTableCombo();
 		destinationTableCombo.addSelectionListener(new SelectionAdapter() {
 			@Override
 			public void widgetSelected(SelectionEvent e) {
 				if (destinationTableCombo.getItemCount() > 0) {
 	 				populateDestinationColumnList( getDestinationTable() );		
 				}
 			}
 		});
	}
	

	private void populateSourceTableCombo(){
		EList<BusinessColumnSet> businessTables = model.getTables();
		for(BusinessColumnSet table : businessTables){
			sourceTableCombo.add(table.getName());
		}
	}
	
	private void populateDestinationTableCombo(){
		destinationTableCombo.removeAll();
		EList<BusinessColumnSet> businessTables = model.getTables();
		for(BusinessColumnSet table : businessTables){
			destinationTableCombo.add(table.getName());
		}
	}
	
	private void populateSourceColumnList(BusinessColumnSet businessTable){
		sourceColumnList.removeAll();
		EList<BusinessColumn> businessColumns = businessTable.getColumns();
		for (BusinessColumn column : businessColumns ){
			sourceColumnList.add(column.getName());
		}
	}
	
	private void populateDestinationColumnList(BusinessColumnSet businessTable){
		destinationColumnList.removeAll();
		EList<BusinessColumn> businessColumns = businessTable.getColumns();
		for (BusinessColumn column : businessColumns ){
			destinationColumnList.add(column.getName());
		}
	}
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if(columnCorrelationList.getItemCount() > 0){	
			setPageComplete(true);
		}
		else{			
			setPageComplete(false);
		}		
	}
	
	private BusinessColumnSet getSourceTable(){
		if (sourceTable != null){
			return sourceTable;
		}
		else{
			return model.getTable(sourceTableCombo.getText());
		}
	}
	
	private BusinessColumnSet getDestinationTable(){
		if (destinationTable != null){
			return destinationTable;
		}
		else{
			return model.getTable(destinationTableCombo.getText());
		}
	}
	
	private BusinessColumn getSourceColumn() {
		return getSourceTable().getColumn(sourceColumnList.getSelection()[0]);
	}
	
	private BusinessColumn getDestinationColumn() {
		return getDestinationTable().getColumn(destinationColumnList.getSelection()[0]);
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
	
	public String getBusinessRelationshipName(){
		return relationshipNameInputField.getText();
	}

	/**
	 * @return the relationshipsContainer
	 */
	public BusinessRelationshipDescriptor getRelationshipDescriptor() {
		return new BusinessRelationshipDescriptor( getSourceTable(), getDestinationTable(), sourceColumns, destinationColumns, cardinality, getBusinessRelationshipName() );
	}

}
