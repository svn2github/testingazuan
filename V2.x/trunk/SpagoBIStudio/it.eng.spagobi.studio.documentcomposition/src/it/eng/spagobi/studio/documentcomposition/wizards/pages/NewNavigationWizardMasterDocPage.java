package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardMasterDocPage extends WizardPage {


	String name = "";
	String paramOut ="";
	
	Text masterDocNameText;
	Text masterDocOutputParam;

	public String getName() {
		return name;
	}

	public String getParamOut() {
		return paramOut;
	}
	public NewNavigationWizardMasterDocPage() {
		super("New Document - Master document");
		setTitle("Insert Master document");
	}

	public NewNavigationWizardMasterDocPage(String pageName) {
		super(pageName);
		setTitle("Insert Master document");
	}
	@Override
	public boolean canFlipToNextPage() {
		if ((masterDocNameText.getText() == null || masterDocNameText.getText().length() == 0)
				&&(masterDocOutputParam.getText() == null || masterDocOutputParam.getText().length() == 0)) {
			return false;
		}else
			return super.canFlipToNextPage();
	}
	
	public void createControl(Composite parent) {

		final Composite composite = new Composite(parent, SWT.BORDER
				| SWT.NO_REDRAW_RESIZE);
		composite.setSize(600, 400);
		final GridLayout gl = new GridLayout();

		int ncol = 3;
		gl.numColumns = ncol;

		composite.setLayout(gl);
		new Label(composite, SWT.NONE).setText("Master document:");
		masterDocNameText = new Text(composite, SWT.BORDER);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		masterDocNameText.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.widthHint = 200;

		// fielset per parametri output
		new Label(composite, SWT.NONE).setText("Ouput parameter:");
		masterDocOutputParam = new Text(composite, SWT.BORDER);
		masterDocOutputParam.setLayoutData(gd);

		masterDocNameText.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				name = masterDocNameText.getText();
				setPageComplete(name.length() > 0	&& paramOut.length() > 0);
			}
		});
		
		masterDocOutputParam.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				paramOut = masterDocOutputParam.getText();
				setPageComplete(name.length() > 0	&& paramOut.length() > 0);
			}
		});
		composite.pack();
		composite.redraw();
		setControl(composite);
	}

	public Text getMasterDocNameText() {
		return masterDocNameText;
	}

	public Text getMasterDocOutputParam() {
		return masterDocOutputParam;
	}

}
