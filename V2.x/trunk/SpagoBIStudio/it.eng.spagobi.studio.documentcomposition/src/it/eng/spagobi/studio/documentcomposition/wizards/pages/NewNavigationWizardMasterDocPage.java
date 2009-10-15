package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardMasterDocPage extends WizardPage {

	Text masterDocNameText;
	Vector<Text> masterDocOutputParams = new Vector<Text>();
	

	public NewNavigationWizardMasterDocPage() {
		super("New Navigation - Master document");
		setTitle("Insert Master document");
	}
	public NewNavigationWizardMasterDocPage(String pageName) {
		super(pageName);
		setTitle("Insert Master document");
	}

	public void createControl(Composite parent) {
		
		Shell shell = parent.getShell();
		
		final Composite composite =  new Composite(parent, SWT.BORDER | SWT.NO_REDRAW_RESIZE);
		composite.setSize(600, 400);
		GridLayout gl = new GridLayout();
		
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
		

		//fielset per parametri output
		new Label(composite, SWT.NONE).setText("Ouput parameter:");	
		masterDocOutputParams.addElement(new Text(composite, SWT.BORDER));
		masterDocOutputParams.elementAt(0).setLayoutData(gd);
		
		//aggiungo bottone add al focusin di outpu parameter
		final Button addButton = new Button(composite, SWT.PUSH) ;
		addButton.setText("Add");
		addButton.setVisible(false);
		addButton.setLayoutData(gd);
		
		masterDocOutputParams.elementAt(0).addListener( SWT.FocusIn, new Listener() {
			public void handleEvent(Event event) {
				addButton.setVisible(true);
				composite.pack(false);
				composite.redraw();
			
			}
		});		

		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan =2;

		final int currentElement = masterDocOutputParams.size()-1;
		addButton.addListener( SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				GridData gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
				gd.horizontalSpan = 2;
				//crea una nuovo output text
				new Label(composite, SWT.NONE).setText("Ouput parameter:");
				Text newText =new Text(composite, SWT.BORDER );
				masterDocOutputParams.addElement(newText);
				newText.setLayoutData(gd);
				composite.pack(false);
				composite.redraw();
				
			}
		});
		composite.pack(false);
		composite.redraw();
		setControl(composite);
	}
	
	public Text getMasterDocNameText() {
		return masterDocNameText;
	}
	public Vector<Text> getMasterDocOutputParams() {
		return masterDocOutputParams;
	}

}

