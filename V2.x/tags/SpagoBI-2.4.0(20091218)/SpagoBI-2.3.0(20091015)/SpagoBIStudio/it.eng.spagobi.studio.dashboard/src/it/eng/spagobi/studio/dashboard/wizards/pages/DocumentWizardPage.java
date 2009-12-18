package it.eng.spagobi.studio.dashboard.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class DocumentWizardPage extends WizardPage {

	Text nameFolderText;
	
	public DocumentWizardPage(String pageName) {
		super(pageName);
		setTitle("New Document Wiza ...");
	}

	public void createControl(Composite parent) {
		Composite composite =  new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		
		new Label(composite, SWT.NONE).setText("Name:");				
		nameFolderText = new Text(composite, SWT.BORDER);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = ncol - 1;
		nameFolderText.setLayoutData(gd);
		
		setControl(composite);
		
		
	}

	public Text getNameFolderText() {
		return nameFolderText;
	}

	public void setNameFolderText(Text nameFolderText) {
		this.nameFolderText = nameFolderText;
	}

}
