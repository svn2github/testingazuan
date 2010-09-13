/*
 * This class create the first page of the AddBCWizard
 * to select the columns of the original table to transfer
 * inside the new Business Class created
 */
package it.eng.spagobi.meta.editor.wizards;



import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;



public class AddBCWizardPage1 extends WizardPage {
    private Text bcName;
    private Label lblName,lErr;
    private String defaultName;
    private Table columns,fields;

	protected AddBCWizardPage1(String pageName, String defaultBCName) {
		super(pageName);
		setTitle("Business Class Creation");
		setDescription("This wizard drives you to create a new Business Class in your Business Model.\n"+
				"Insert Business Class Name and select fields from the original table.");
		ImageDescriptor image = Activator.getImageDescriptor("wizards/createBC.png");
	    if (image!=null) setImageDescriptor(image);	
	    defaultName = defaultBCName;
	}

	@Override
	public void createControl(Composite parent) {
		//Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);
		
		//Name Group
		Group nameGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		nameGroup.setText("Name");
		GridLayout glName = new GridLayout();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		glName.numColumns = 2;
		glName.makeColumnsEqualWidth = false;
		nameGroup.setLayout(glName);
		nameGroup.setLayoutData(gd);
       
		//Adding NameGroup elements
        lblName = new Label(nameGroup,SWT.NONE);
        lblName.setText("Business Class Name:");
        bcName = new Text(nameGroup,SWT.BORDER);
        bcName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        bcName.setText(defaultName);
        
        //Fields Group
		Group fieldGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		fieldGroup.setText("Fields");
		GridLayout glField = new GridLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		glField.numColumns = 3;
		glField.makeColumnsEqualWidth = false;
		fieldGroup.setLayout(glField);
		fieldGroup.setLayoutData(gd2);
        
		//Left table -------------------------------
		Composite compLeft = new Composite(fieldGroup, SWT.NONE);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compLeft.setLayout(glL);
		compLeft.setLayoutData(gdL);
		Label lblLeftTab = new Label(compLeft,SWT.NONE);
		lblLeftTab.setText("Table columns: ");
 		columns = new Table(compLeft, SWT.BORDER);
 		columns.setLayoutData(gdL);

 		//Center buttons -------------------------------
		Composite compCenter = new Composite(fieldGroup, SWT.NONE);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compCenter.setLayout(glC);
		Button bAddField = new Button(compCenter,SWT.FLAT);
		bAddField.setToolTipText("Add column as a Business Class field");
		Image imageAdd = Activator.getImageDescriptor("arrow_right.png").createImage();
	    if (imageAdd!=null) bAddField.setImage(imageAdd);
		Button bRemoveField = new Button(compCenter,SWT.FLAT);
		bRemoveField.setToolTipText("Remove field from Business Class");
		Image imageRem = Activator.getImageDescriptor("arrow_left.png").createImage();
	    if (imageRem!=null) bRemoveField.setImage(imageRem);
		
		//Right table -------------------------------
		Composite compRight = new Composite(fieldGroup, SWT.NONE);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compRight.setLayout(glR);
		compRight.setLayoutData(gdR);
		Label lblRightTab = new Label(compRight,SWT.NONE);
		lblRightTab.setText("Business Class fields: ");
 		fields = new Table(compRight, SWT.BORDER);
 		fields.setLayoutData(gdR);
 		
 		//Bottom error label
		Composite err = new Composite(composite, SWT.NULL);
		GridLayout glerr = new GridLayout();
		glerr.numColumns = 2;
		err.setLayout(glerr);	
		lErr = new Label(err, SWT.NULL);
	    
 		//populate left table widget
 		addTableItems();
 		
		//adding listener to Add button		
 		bAddField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				if (columns.getSelectionCount() > 0)
					tiSel = columns.getSelection()[0];
				if (tiSel!= null){
					TableItem ti = new TableItem(fields, 0);
					ti.setText(tiSel.getText());
					columns.remove(columns.getSelectionIndex());
				}
				checkPageComplete();

			}
		});
 		
		//adding listener to Remove button		
 		bRemoveField.addListener(SWT.Selection, new Listener() {		
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				if (fields.getSelectionCount() > 0)
					tiSel = fields.getSelection()[0];
				if (tiSel!= null){
					TableItem ti = new TableItem(columns, 0);
					ti.setText(tiSel.getText());
					fields.remove(fields.getSelectionIndex());
				}
				checkPageComplete();

			}
		}); 	
 		
 		//first check
 		checkPageComplete();
 		
        //Important: Setting page control
 		setControl(composite);
	}
	
	//add the original columns as TableItem (in the left Table Widget)
	private void addTableItems(){
		
		columns.removeAll();
		fields.removeAll();
		//TODO: (TO REMOVE) fake temporary population just to try the widget
		for (int i=0; i<=3;i++){
			TableItem ti = new TableItem(columns, 0);
			ti.setText("Column "+i);
		}
	}
	
	//check if the right conditions to go forward occurred
	private void checkPageComplete(){
		if(fields.getItemCount()!=0){
			setPageComplete(true);
			lErr.setText("");
		}
		else{			
			lErr.setText("This BC has not a field, please select at least one to continue");
			setPageComplete(false);
		}		
	}

}
