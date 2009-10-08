package it.eng.spagobi.studio.jasper.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewJasperReportWizardPage extends WizardPage {

	Text jasperNameText;
	
	public NewJasperReportWizardPage(String pageName) {
		super(pageName);
		setTitle("New Jasper Report Template ...");
	}

	public void createControl(Composite parent) {
		
		Shell shell = parent.getShell();
		
		Composite composite =  new Composite(parent, SWT.BORDER);
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		new Label(composite, SWT.NONE).setText("Name:");				
		jasperNameText = new Text(composite, SWT.BORDER);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = ncol - 1;
		jasperNameText.setLayoutData(gd);
		
		setControl(composite);

	}
	
	public Text getJasperNameText() {
		return jasperNameText;
	}


}

