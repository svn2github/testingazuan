package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardDestinDocPage extends WizardPage {

	Combo destinationDocNameCombo;
	Text destinationInputText;
	
	
	public NewNavigationWizardDestinDocPage() {
		super("New Navigation - Destination document");
		setTitle("Insert Destination document");
	}
	public NewNavigationWizardDestinDocPage(String pageName) {
		super(pageName);
		setTitle("Insert Destination document");
	}

	public void createControl(Composite parent) {
		
		Shell shell = parent.getShell();
		
		Composite composite =  new Composite(parent, SWT.BORDER);
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		new Label(composite, SWT.NONE).setText("Destination document:");				
		destinationDocNameCombo = new Combo(composite, SWT.BORDER);
		
		for(int i=0; i<4; i++){
			destinationDocNameCombo.add("destination "+i);
		}

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = ncol - 1;
		destinationDocNameCombo.setLayoutData(gd);
		
		new Label(composite, SWT.NONE).setText("Input parameter:");
		destinationInputText = new Text(composite, SWT.BORDER);
		destinationInputText.setLayoutData(gd);
		
		setControl(composite);
	}
	
	public Combo getDestinationDocNameCombo() {
		return destinationDocNameCombo;
	}
	public Text getDestinationInputText() {
		return destinationInputText;
	}

}

