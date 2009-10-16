package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardDestinDocPage extends WizardPage {

	Combo destinationDocNameCombo;
	Vector<Text> destinationOutputParams = new Vector<Text>();
	
	public Vector<Text> getDestinationOutputParams() {
		return destinationOutputParams;
	}
	public NewNavigationWizardDestinDocPage() {
		super("New Document - Destination document");
		setTitle("Insert Destination document");
	}
	public NewNavigationWizardDestinDocPage(String pageName) {
		super(pageName);
		setTitle("Insert Destination document");
	}

	public void createControl(Composite parent) {
		
		final Composite composite =  new Composite(parent, SWT.BORDER | SWT.NO_REDRAW_RESIZE);
		composite.setSize(600, 400);
		final GridLayout gl = new GridLayout();
		int ncol = 3;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		new Label(composite, SWT.NONE).setText("Destination document:");				
		destinationDocNameCombo = new Combo(composite, SWT.BORDER |SWT.READ_ONLY );

		for(int i=0; i<4; i++){
			destinationDocNameCombo.add("destination "+i);
		}
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		destinationDocNameCombo.setLayoutData(gd);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth =200;
		gd.widthHint = 200;
		
		new Label(composite, SWT.NONE).setText("Input parameter:");
		destinationOutputParams.addElement(new Text(composite, SWT.BORDER));
		destinationOutputParams.elementAt(0).setLayoutData(gd);
		
		//aggiungo bottone add al focusin di input parameter
		final Button addButton = new Button(composite, SWT.PUSH) ;
		addButton.setText("Add");
		addButton.setVisible(false);
		addButton.setLayoutData(gd);
		
		destinationOutputParams.elementAt(0).addListener( SWT.FocusIn, new Listener() {
			public void handleEvent(Event event) {
				GridData gridData = new GridData();
				gridData.minimumWidth=60;
				//gridData.widthHint = 200;
				gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
				gridData.horizontalSpan = 1;
				gridData.grabExcessHorizontalSpace = true;
				
				addButton.setVisible(true);
				addButton.setLayoutData(gridData);
				composite.layout(false);
				composite.pack(false);
				composite.redraw();
			
			}
		});		

		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan =2;

		addButton.addListener( SWT.Selection, new Listener() {
			public void handleEvent(Event event) {

				GridData gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
				gridData.horizontalSpan = 2;
				//gridData.grabExcessHorizontalSpace = true;
				gridData.widthHint = 200;
				//gridData.minimumWidth=160;

				//crea una nuovo output text
				new Label(composite, SWT.NONE).setText("Input parameter:");
				Text newText =new Text(composite, SWT.BORDER );
				destinationOutputParams.addElement(newText);
				newText.setLayoutData(gridData);
				composite.pack(false);
				composite.redraw();
				
			}
		});
		composite.pack(false);
		composite.redraw();
		
		setControl(composite);
	}
	
	public Combo getDestinationDocNameCombo() {
		return destinationDocNameCombo;
	}

}

