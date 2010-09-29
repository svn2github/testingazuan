package it.eng.spagobi.meta.editor.wizards;

import it.eng.spagobi.meta.editor.Activator;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class AddBusinessTableWizardPageOne extends WizardPage {
	private Button[] radios;
	private boolean columnSelection = false;
	
	protected AddBusinessTableWizardPageOne(String pageName) {
		super(pageName);
		setTitle("Business Table Creation");
		setDescription("This wizard drives you to create a new Business Table in your Business Model.\n"+
				"Plese select how to create the Business Table.");
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
		
		//Radio buttons
		radios = new Button[2];
		radios[0] = new Button(composite, SWT.RADIO);
	    radios[0].setSelection(true);
	    radios[0].setText("Using physical table directly");
	    radios[1] = new Button(composite, SWT.RADIO);
	    radios[1].setText("Selecting columns from physical table");    	
	    
	    //Radio button listener
	    radios[0].addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (radios[0].getSelection()){
	            	setColumnSelection(false);
	            }
	        }
	    });
	    radios[1].addListener(SWT.Selection, new Listener() {
	        public void handleEvent(Event event) {
	            if (radios[1].getSelection()){
	            	setColumnSelection(true);
	            }
	        }
	    });

	    
        //Important: Setting page control
 		setControl(composite);
	}
	
	/**
	 * @param columnSelection the columnSelection to set
	 */
	public void setColumnSelection(boolean columnSelection) {
		this.columnSelection = columnSelection;
	}

	/**
	 * @return the columnSelection
	 */
	public boolean isColumnSelection() {
		return columnSelection;
	}

}
