package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardPage extends WizardPage {

	Text navigationNameText;
	
	public NewNavigationWizardPage() {
		super("New Navigation");
		setTitle("New Navigation");
	}
	public NewNavigationWizardPage(String pageName) {
		super(pageName);
		setTitle("New Navigation");
	}

	public void createControl(Composite parent) {
		
		Shell shell = parent.getShell();
		
		Composite composite =  new Composite(parent, SWT.BORDER);
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		new Label(composite, SWT.NONE).setText("Navigation name:");				
		navigationNameText = new Text(composite, SWT.BORDER);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = ncol - 1;
		navigationNameText.setLayoutData(gd);
		
		setControl(composite);
	}
	
	public Text getNavigationNameText() {
		return navigationNameText;
	}


}

