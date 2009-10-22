package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBINavigationWizard;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.util.DestinationInfo;

import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
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
	Vector<Text> destinationInputParamDefaultValue ;
	

	String name = "";
	String paramIn = "";
	
	int destinCounter = 0;
	
	

	private DestinationInfo destinationInfo;
	private Vector<DestinationInfo> destinationInfos;
	
	
	public NewNavigationWizardDestinDocPage() {		
		super("New Document - Destination document");
		setTitle("Insert Destination Document");
		destinationInfos = new Vector<DestinationInfo>();
	}
	public NewNavigationWizardDestinDocPage(String pageName) {		
		super(pageName);
		setTitle("Insert Destination document");
		destinationInfos = new Vector<DestinationInfo>();

	}
	@Override
	public boolean isPageComplete() {

				
		boolean ret= super.isPageComplete();
		for(int i = 0; i<destinCounter; i++){
			int sel = destinationDocNameCombo.elementAt(destinCounter).getSelectionIndex();
			String destin = destinationDocNameCombo.elementAt(destinCounter).getItem(sel);
			
			if ((destinationInputParam.elementAt(destinCounter).getText() == null || destinationInputParam.elementAt(destinCounter).getText().length() == 0)
					&&(sel ==-1 || destin == null )) {
				return false;
			}
		}	

		return ret;
	}

	public void createControl(Composite parent) {
		destinationInfo = new DestinationInfo();
		
		destinationDocNameCombo = new Vector<Combo>();
		destinationInputParam = new Vector<Text>();
		destinationInputParamDefaultValue = new Vector<Text>();

		final ScrolledComposite sc =  new ScrolledComposite(parent, SWT.V_SCROLL );
		final Composite composite = new Composite(sc, SWT.BORDER);
		sc.setContent(composite);

		final GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
	
		
		new Label(composite, SWT.NONE).setText("Destination document:");				
		destinationDocNameCombo.addElement(new Combo(composite, SWT.BORDER |SWT.READ_ONLY ));

		fillDestinationCombo();
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		destinationDocNameCombo.elementAt(destinCounter).setLayoutData(gd);

		destinationDocNameCombo.elementAt(0).addListener(SWT.FocusIn, new Listener() {
			public void handleEvent(Event event) {
				fillDestinationCombo();
			}
		});	
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth =200;
		gd.widthHint = 250;
		
		new Label(composite, SWT.NONE).setText("Input parameter:");
		destinationInputParam.addElement(new Text(composite, SWT.BORDER));
		destinationInputParam.elementAt(destinCounter).setLayoutData(gd);
		
		new Label(composite, SWT.NONE).setText("Default value:");
		destinationInputParamDefaultValue.addElement(new Text(composite, SWT.BORDER));
		destinationInputParamDefaultValue.elementAt(destinCounter).setLayoutData(gd);
		
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

				destinationInfo = new DestinationInfo();
				int sel = destinationDocNameCombo.elementAt(destinCounter).getSelectionIndex();
				destinationInfo.setDocDestName(destinationDocNameCombo.elementAt(destinCounter).getItem(sel));
				destinationInfo.setParamDestName(destinationInputParam.elementAt(destinCounter));
				destinationInfo.setParamDefaultValue(destinationInputParamDefaultValue.elementAt(destinCounter));
				destinationInfos.add(destinationInfo);	

				
				destinCounter++;	
				
				
				GridData gridData = new GridData();
				gridData.horizontalAlignment = GridData.FILL_HORIZONTAL;
				gridData.horizontalSpan = 1;
				gridData.widthHint = 250;
				
				new Label(composite, SWT.NONE).setText("Destination document:");	
				destinationDocNameCombo.addElement(new Combo(composite, SWT.BORDER |SWT.READ_ONLY ));

				fillDestinationCombo();
				
				destinationDocNameCombo.elementAt(destinCounter).setLayoutData(gridData);
				destinationDocNameCombo.elementAt(destinCounter).setVisible(true);

				//crea una nuovo output text
				new Label(composite, SWT.NONE).setText("Input parameter:");
				Text newText =new Text(composite, SWT.BORDER );

				destinationInputParam.addElement(newText);
				newText.setLayoutData(gridData);
				
				destinationInputParam.elementAt(destinCounter).addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						//aggiunge pulsante x add delle pagine
						addButton.setVisible(true);
					}
				});
				
				new Label(composite, SWT.NONE).setText("Default value:");
				destinationInputParamDefaultValue.addElement(new Text(composite, SWT.BORDER));
				destinationInputParamDefaultValue.elementAt(destinCounter).setLayoutData(gridData);
				
				composite.pack(false);
				composite.getParent().redraw();

			}
		});
		destinationDocNameCombo.elementAt(0).addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {

				int sel = destinationDocNameCombo.elementAt(0).getSelectionIndex();
				name = destinationDocNameCombo.elementAt(0).getItem(sel);
				
			}
		});	
		
		destinationInputParam.elementAt(0).addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {

				//aggiunge pulsante x add delle pagine
				addButton.setVisible(true);
				paramIn = destinationInputParam.elementAt(0).getText();	
				
				composite.redraw();
			}
		});		

		

		
		composite.pack(false);
		composite.redraw();
		
		setControl(composite);
	}

	private void fillDestinationCombo(){
		DocumentComposition docComp = Activator.getDefault().getDocumentComposition();
		SpagoBINavigationWizard wizard = (SpagoBINavigationWizard)getWizard();
		System.out.println(destinationDocNameCombo.elementAt(destinCounter).getItemCount());
		if(destinationDocNameCombo.elementAt(destinCounter).getItemCount() == 0){
			if(docComp != null){
				Vector docs = docComp.getDocumentsConfiguration().getDocuments();
				if(docs != null){
					for(int i=0; i<docs.size(); i++){
						String destinationName = ((Document)docs.elementAt(i)).getLabel();
						
							if(destinationName != null && !destinationName.equals("")){
								destinationDocNameCombo.elementAt(destinCounter).add(destinationName);
							}
						
					}
				}
			}
		}
		String master = wizard.getSelectedMaster();
		//per ridisegnare combo
		
		if(master != null && !master.equals("")){
			int posMaster =destinationDocNameCombo.elementAt(destinCounter).indexOf(master);
			destinationDocNameCombo.elementAt(destinCounter).remove(posMaster);
			destinationDocNameCombo.elementAt(destinCounter).redraw();
		}
		
	}
	public Vector<DestinationInfo> getDestinationInfos() {
		return destinationInfos;
	}
	public void setDestinationInfos(Vector<DestinationInfo> destinationInfos) {
		this.destinationInfos = destinationInfos;
	}
	public DestinationInfo getDestinationInfo() {
		return destinationInfo;
	}
	public void setDestinationInfo(DestinationInfo destinationInfo) {
		this.destinationInfo = destinationInfo;
	}

	public Vector<Text> getDestinationInputParam() {
		return destinationInputParam;
	}
	public Vector<Combo> getDestinationDocNameCombo() {
		return destinationDocNameCombo;
	}	
	public int getDestinCounter() {
		return destinCounter;
	}
	public void setDestinCounter(int destinCounter) {
		this.destinCounter = destinCounter;
	}
	public Vector<Text> getDestinationInputParamDefaultValue() {
		return destinationInputParamDefaultValue;
	}
	public void setDestinationInputParamDefaultValue(
			Vector<Text> destinationInputParamDefaultValue) {
		this.destinationInputParamDefaultValue = destinationInputParamDefaultValue;
	}
}


