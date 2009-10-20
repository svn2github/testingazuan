package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBINavigationWizard;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardPage extends WizardPage {


	@Override
	public boolean canFlipToNextPage() {
		if (navigationNameText.getText() == null || navigationNameText.getText().length() == 0) {
			return false;
		}else
			return super.canFlipToNextPage();
	}

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
		
		
		navigationNameText.addListener(SWT.Modify, new Listener() {
	        public void handleEvent(Event event) {
	          String text = navigationNameText.getText();
	          System.out.println("text::"+text);
	          if(text != null && text.length()!=0)
	            setPageComplete(true);
	          else
	            setPageComplete(false);
	        }
	      });
		navigationNameText.setFocus();
		

		
		setControl(composite);
	}
	
	public Text getNavigationNameText() {
		return navigationNameText;
	}


}

