/*
 * This class create the second page of the AddBCWizard
 * to select the relationship between the already created BC 
 * and the new BC
 */
package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;


public class AddBCWizardPage2 extends WizardPage {

	private Label lblBcTo;
	private Combo comboBc;
	
	protected AddBCWizardPage2(String pageName){
		super(pageName);
		setTitle("Business Class Creation");
		setDescription("This wizard drives you to create a new Business Class in your Business Model.\n"+
				"Set relationship of new business class");
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
		
		//BC to relate Group
		Group bcTo = new Group(composite, SWT.SHADOW_ETCHED_IN);
		bcTo.setText("Select other Business Class");
		GridLayout glBcTo = new GridLayout();
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		glBcTo.numColumns = 2;
		glBcTo.makeColumnsEqualWidth = false;
		bcTo.setLayout(glBcTo);
		bcTo.setLayoutData(gd);
		
		//Adding BC to relate Group elements
		lblBcTo = new Label(bcTo,SWT.NONE);
		lblBcTo.setText("Business Class Name:");
		comboBc = new Combo(bcTo,SWT.READ_ONLY);
		comboBc.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
        //Fields Group
		Group fieldGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		fieldGroup.setText("Field to relate");
		GridLayout glField = new GridLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		glField.numColumns = 4;
		glField.makeColumnsEqualWidth = false;
		fieldGroup.setLayout(glField);
		fieldGroup.setLayoutData(gd2);
		
		//Adding elements in Fields Group
		//TODO: This will be generated dynamically with the fields select before,
		//now is a fake UI only for test
		Combo comboFields, comboRelType;
		for (int i=0; i<=3; i++){
			new Label(fieldGroup,SWT.NONE).setText("Field "+i+" relate to: ");
			comboFields = new Combo(fieldGroup,SWT.READ_ONLY);
			comboFields.setItems (new String [] {"Field 1", "Field 2", "Field 3"});
			new Label(fieldGroup,SWT.NONE).setText("Type of relation: ");
			comboRelType = new Combo(fieldGroup,SWT.READ_ONLY);
			comboRelType.setItems (new String [] {"1 to 1", "1 to N", "N to 1", "N to N", "Nothing"});
		}

        //Important: Setting page control
 		setControl(composite);
	}

}
