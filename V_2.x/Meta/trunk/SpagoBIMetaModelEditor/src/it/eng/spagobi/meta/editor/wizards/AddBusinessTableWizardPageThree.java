package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class AddBusinessTableWizardPageThree extends WizardPage {

	protected AddBusinessTableWizardPageThree(String pageName) {
		super(pageName);
		setTitle("Business Table Creation");
		setDescription("Please select the columns to use in your Business Table");
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
		
        //Important: Setting page control
 		setControl(composite);

	}

}
