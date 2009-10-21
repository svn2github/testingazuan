package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;
import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBIModifyNavigationWizard;
import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBINavigationWizard;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.util.DestinationInfo;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class ModifyNavigationWizardPage  extends WizardPage{


	Vector<Combo> destinationDocNameCombo;
	Vector<Text> destinationInputParam ;
	
	String name = "";
	String paramIn = "";
	
	int destinCounter = 0;

	private DestinationInfo destinationInfo;
	private Vector<DestinationInfo> destinationInfos;
	

	Text navigationNameText;

	Text masterDocName;
	Text masterParamName;
	
	

	public ModifyNavigationWizardPage() {		
		super("Modify navigation");
		setTitle("Modify Destination Document");		
		destinationInfos = new Vector<DestinationInfo>();
	}
	public ModifyNavigationWizardPage(String pageName) {		
		super(pageName);
		setTitle("Modify Destination document");
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

		final ScrolledComposite sc =  new ScrolledComposite(parent, SWT.V_SCROLL );
		final Composite composite = new Composite(sc, SWT.BORDER);
		sc.setContent(composite);

		final GridLayout gl = new GridLayout();
		int ncol = 2;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth =200;
		gd.widthHint = 250;
		
		new Label(composite, SWT.NONE).setText("Navigation name:");				
		navigationNameText = new Text(composite, SWT.BORDER);
		navigationNameText.setLayoutData(gd);
		
		new Label(composite, SWT.NONE).setText("Master document:");				
		masterDocName = new Text(composite, SWT.BORDER);		
		masterDocName.setLayoutData(gd);
		
		new Label(composite, SWT.NONE).setText("Output parameter:");				
		masterParamName = new Text(composite, SWT.BORDER);
		masterParamName.setLayoutData(gd);

		getNavigationItem();
		
		new Label(composite, SWT.NONE).setText("Destination document:");				
		destinationDocNameCombo.addElement(new Combo(composite, SWT.BORDER |SWT.READ_ONLY ));

		fillDestinationCombo();
		
		
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

				
				composite.pack(false);
				//composite.redraw();
				composite.getParent().redraw();

			}
		});
		destinationDocNameCombo.elementAt(0).addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {

				int sel = destinationDocNameCombo.elementAt(0).getSelectionIndex();
				name = destinationDocNameCombo.elementAt(0).getItem(sel);
				//setPageComplete(name.length() > 0	&& paramIn.length() > 0);
				
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
		String master = masterDocName.getText();
		//per ridisegnare combo
		
		if(master != null && !master.equals("")){
			int posMaster =destinationDocNameCombo.elementAt(destinCounter).indexOf(master);
			destinationDocNameCombo.elementAt(destinCounter).remove(posMaster);
			destinationDocNameCombo.elementAt(destinCounter).redraw();
		}
		
	}
	
	private Parameter getNavigationItem(){
		Parameter param = null;
		
		IStructuredSelection selection =((SpagoBIModifyNavigationWizard)getWizard()).getSelection();
		Object objSel = selection.toList().get(0);
		Table listOfNavigations = (Table)objSel;
		TableItem[] itemsSel = listOfNavigations.getSelection();
		if(itemsSel != null && itemsSel.length != 0){

			DocumentComposition docComp = Activator.getDefault().getDocumentComposition();
			if(docComp != null){
				Vector docs = docComp.getDocumentsConfiguration().getDocuments();
				if(docs != null){
					for(int i=0; i<docs.size(); i++){
						Parameters params = ((Document)docs.elementAt(i)).getParameters();
						if(params != null){
				    		for (int j = 0; j<params.getParameter().size(); j++){
				    			param = params.getParameter().elementAt(j);
				    			String navigName = itemsSel[0].getText();
				    			if(param.getType() != null &&param.getType().equalsIgnoreCase("OUT") 
				    					&& param.getNavigationName()!= null 
				    					&& param.getNavigationName().equals(navigName)){
				    				navigationNameText.setText(navigName);
				    				navigationNameText.setEditable(false);
				    				
				    				String masterDoc = ((Document)docs.elementAt(i)).getLabel();
				    				String masterParam = param.getLabel();
				    				
				    				masterDocName.setText(masterDoc);
				    				masterDocName.setEditable(false);
				    				
				    				masterParamName.setText(masterParam);
				    				masterParamName.setEditable(false);
				    				
				    				setPageComplete(true);
				    				navigationNameText.redraw();
				    			}
				    		}
						}
						
					}
				}
			}
		}
		return param;
	}

	
	public Text getNavigationNameText() {
		return navigationNameText;
	}
	public void setNavigationNameText(Text navigationNameText) {
		this.navigationNameText = navigationNameText;
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
	public Text getMasterDocName() {
		return masterDocName;
	}
	public void setMasterDocName(Text masterDocName) {
		this.masterDocName = masterDocName;
	}
	public Text getMasterParamName() {
		return masterParamName;
	}
	public void setMasterParamName(Text masterParamName) {
		this.masterParamName = masterParamName;
	}
	public Vector<DestinationInfo> getDestinationInfos() {
		return destinationInfos;
	}
	public void setDestinationInfos(Vector<DestinationInfo> destinationInfos) {
		this.destinationInfos = destinationInfos;
	}
	public int getDestinCounter() {
		return destinCounter;
	}
	public void setDestinCounter(int destinCounter) {
		this.destinCounter = destinCounter;
	}
}
