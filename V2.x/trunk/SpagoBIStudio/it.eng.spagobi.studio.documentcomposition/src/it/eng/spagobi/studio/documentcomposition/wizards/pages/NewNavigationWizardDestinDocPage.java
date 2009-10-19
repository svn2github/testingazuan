package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardDestinDocPage extends WizardPage {

	Vector<Combo> destinationDocNameCombo;
	Vector<Text> destinationInputParam ;
	
	String name = "";
	String paramIn = "";
	
	int destinCounter = 0;
	
	
	private HashMap<String, Text> destinationInfo;
	
	
	public HashMap<String, Text> getDestinationInfo() {
		return destinationInfo;
	}
	public void setDestinationInfo(HashMap<String, Text> destinationInfo) {
		this.destinationInfo = destinationInfo;
	}

	public Vector<Text> getDestinationInputParam() {
		return destinationInputParam;
	}
	public Vector<Combo> getDestinationDocNameCombo() {
		return destinationDocNameCombo;
	}
	public NewNavigationWizardDestinDocPage() {		
		super("New Document - Destination document");

		setTitle("Insert Destination Document");		
	}
	public NewNavigationWizardDestinDocPage(String pageName) {		
		super(pageName);
		setTitle("Insert Destination document");


	}

	public void createControl(Composite parent) {

		destinationInfo = new HashMap<String, Text>();
		destinationDocNameCombo = new Vector<Combo>();
		destinationInputParam = new Vector<Text>();

		final Composite composite =  new Composite(parent, SWT.BORDER | SWT.SCROLL_PAGE);

		//composite.setSize(600, 400);
		final GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		new Label(composite, SWT.NONE).setText("Destination document:");				
		destinationDocNameCombo.addElement(new Combo(composite, SWT.BORDER |SWT.READ_ONLY ));

		//riempimento fittizio destinazioni
		for(int i=0; i<4; i++){
			destinationDocNameCombo.elementAt(destinCounter).add("destination "+i);
		}
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		destinationDocNameCombo.elementAt(destinCounter).setLayoutData(gd);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth =200;
		gd.widthHint = 200;
		
		new Label(composite, SWT.NONE).setText("Input parameter:");
		destinationInputParam.addElement(new Text(composite, SWT.BORDER));
		destinationInputParam.elementAt(destinCounter).setLayoutData(gd);
		
		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan =2;

		final Button addButton = new Button(composite, SWT.PUSH) ;
		addButton.setText("Add destination");
		addButton.setVisible(false);
		addButton.setLayoutData(gd);

		
		destinationInputParam.elementAt(destinCounter).addListener( SWT.FocusIn, new Listener() {
			public void handleEvent(Event event) {
				addButton.setVisible(true);				
				composite.redraw();
			
			}
		});	
		
		addButton.addListener( SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				destinCounter++;
				
				GridData gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
				gridData.horizontalSpan = 1;
				gridData.widthHint = 200;
				
				new Label(composite, SWT.NONE).setText("Destination document:");	
				destinationDocNameCombo.addElement(new Combo(composite, SWT.BORDER |SWT.READ_ONLY ));

				//riempimento fittizio destinazioni
				for(int i=0; i<4; i++){
					destinationDocNameCombo.elementAt(destinCounter).add("destination "+i);
				}
				destinationDocNameCombo.elementAt(destinCounter).setLayoutData(gridData);
				destinationDocNameCombo.elementAt(destinCounter).addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						int sel = destinationDocNameCombo.elementAt(destinCounter).getSelectionIndex();
						name = destinationDocNameCombo.elementAt(destinCounter).getItem(sel);
						setPageComplete(name.length() > 0	&& paramIn.length() > 0);
						
					}
				});

				//crea una nuovo output text
				new Label(composite, SWT.NONE).setText("Input parameter:");
				Text newText =new Text(composite, SWT.BORDER );

				destinationInputParam.addElement(newText);
				newText.setLayoutData(gridData);
				
				destinationInputParam.elementAt(destinCounter).addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						//aggiunge pulsante x add delle pagine
						addButton.setVisible(true);
						paramIn = destinationInputParam.elementAt(destinCounter).getText();
						setPageComplete(name.length() > 0	&& paramIn.length() > 0);
						
						int sel = destinationDocNameCombo.elementAt(destinCounter).getSelectionIndex();
						destinationInfo.put(destinationDocNameCombo.elementAt(destinCounter).getItem(sel), destinationInputParam.elementAt(destinCounter));
						
						composite.redraw();
					}
				});
				setPageComplete(true);
				
				composite.pack(false);
				composite.redraw();

			}
		});
		destinationDocNameCombo.elementAt(0).addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {

				int sel = destinationDocNameCombo.elementAt(0).getSelectionIndex();
				name = destinationDocNameCombo.elementAt(0).getItem(sel);
				setPageComplete(name.length() > 0	&& paramIn.length() > 0);
				
			}
		});	

		destinationInputParam.elementAt(0).addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {

				//aggiunge pulsante x add delle pagine
				addButton.setVisible(true);
				paramIn = destinationInputParam.elementAt(0).getText();
				setPageComplete(name.length() > 0	&& paramIn.length() > 0);
				
				int sel = destinationDocNameCombo.elementAt(0).getSelectionIndex();
				destinationInfo.put(destinationDocNameCombo.elementAt(0).getItem(sel), destinationInputParam.elementAt(0));
				
				composite.redraw();
			}
		});		


		composite.pack(false);
		composite.redraw();
		
		setPageComplete(true);
		setControl(composite);
	}

}

