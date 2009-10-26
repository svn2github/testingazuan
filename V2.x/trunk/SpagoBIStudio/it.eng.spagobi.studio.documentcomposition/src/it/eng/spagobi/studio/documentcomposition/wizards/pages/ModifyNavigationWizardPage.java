package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Refresh;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.RefreshDocLinked;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataParameter;
import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBIModifyNavigationWizard;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.util.DestinationInfo;

import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.CellEditor.LayoutData;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
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
	Vector<Combo> destinationInputParam ;
	Vector<Text> destinationInputParamDefaultValue ;
	

	String name = "";
	String paramIn = "";
	
	int destinCounter = -1;

	private DestinationInfo destinationInfo;
	private Vector<DestinationInfo> destinationInfos;
	

	Text navigationNameText;

	Text masterDocName;
	Text masterParamName;
	Text masterDefaultValueOutputParam;


	private MetadataDocumentComposition metaDoc = Activator.getDefault().getMetadataDocumentComposition();

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
		if(destinCounter != -1){
			for(int i = 0; i<destinCounter; i++){
				int sel = destinationDocNameCombo.elementAt(destinCounter).getSelectionIndex();
				if(sel != -1){
					String destin = destinationDocNameCombo.elementAt(destinCounter).getItem(sel);
					
					if ((destinationInputParam.elementAt(destinCounter).getText() == null || destinationInputParam.elementAt(destinCounter).getText().length() == 0)
							&&(sel ==-1 || destin == null )) {
						return false;
					}
				}
			}	
		}
		return ret;
	}

	
	
	public void createControl(Composite parent) {

		destinationInfo = new DestinationInfo();
		destinationDocNameCombo = new Vector<Combo>();
		destinationInputParam = new Vector<Combo>();
		destinationInputParamDefaultValue = new Vector<Text>();

		final ScrolledComposite sc =  new ScrolledComposite(parent, SWT.V_SCROLL );
		final Composite composite = new Composite(sc, SWT.NONE);
		sc.setContent(composite);
		
		final GridLayout gl = new GridLayout();
		int ncol = 1;
		gl.numColumns = ncol;
		composite.setLayout(gl);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		gd.minimumWidth =200;
		gd.widthHint = 260;
		
		final GridLayout glBlock = new GridLayout();
		glBlock.numColumns = 3;
		
		
		Composite singleBlock = new Composite(composite, SWT.BORDER | SWT.FILL);
		singleBlock.setLayout(glBlock);
		
		new Label(singleBlock, SWT.NONE).setText("Navigation name:");				
		navigationNameText = new Text(singleBlock, SWT.BORDER);
		navigationNameText.setLayoutData(gd);
		
		
		Composite singleBlockMaster = new Composite(composite, SWT.BORDER | SWT.FILL);
		singleBlockMaster.setLayout(glBlock);
		
		new Label(singleBlockMaster, SWT.NONE).setText("Master document:");				
		masterDocName = new Text(singleBlockMaster, SWT.BORDER);		
		masterDocName.setLayoutData(gd);
		
		new Label(singleBlockMaster, SWT.NONE).setText("Output parameter:");				
		masterParamName = new Text(singleBlockMaster, SWT.BORDER);
		masterParamName.setLayoutData(gd);	
		
		new Label(singleBlockMaster, SWT.NONE).setText("Default value:");
		masterDefaultValueOutputParam = new Text(singleBlockMaster, SWT.BORDER);
		masterDefaultValueOutputParam.setLayoutData(gd);


		getNavigationItem();//riempie campi precedentemente inseriti

		gd = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
		gd.horizontalSpan = 2;
		
		final Button addButton = new Button(composite, SWT.PUSH) ;
		addButton.setText("Add destination");
		addButton.setVisible(true);
		addButton.setLayoutData(gd);

		
		addButton.addListener( SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				
				destinCounter++;
				
				Composite composite = navigationNameText.getParent().getParent();
				
				GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
				gridData.widthHint = 260;
				gridData.minimumWidth =200;
				gridData.grabExcessHorizontalSpace = true;
				gridData.horizontalSpan = 2;
				
				//inserisce blocco x destinazione
				Composite destBlock = new Composite(composite, SWT.BORDER | SWT.FILL);
				GridLayout glBlock = new GridLayout();
				glBlock.numColumns = 3;
				destBlock.setLayout(glBlock);
				
				new Label(destBlock, SWT.NONE).setText("Destination document:");	
				destinationDocNameCombo.addElement(new Combo(destBlock, SWT.BORDER |SWT.READ_ONLY ));

				fillDestinationCombo(null, destinCounter);
				
				destinationDocNameCombo.elementAt(destinCounter).setLayoutData(gridData);
				destinationDocNameCombo.elementAt(destinCounter).setVisible(true);

				//crea una nuovo output text
				new Label(destBlock, SWT.NONE).setText("Input parameter:");
				Combo newText =new Combo(destBlock, SWT.BORDER |SWT.READ_ONLY);

				destinationInputParam.addElement(newText);
				newText.setLayoutData(gridData);
				
				destinationInputParam.elementAt(destinCounter).addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						//aggiunge pulsante x add delle pagine
						addButton.setVisible(true);
					}
				});

				new Label(destBlock, SWT.NONE).setText("Default value:");
				destinationInputParamDefaultValue.addElement(new Text(destBlock, SWT.BORDER));
				destinationInputParamDefaultValue.elementAt(destinCounter).setLayoutData(gridData);
				
				final int element = destinCounter;
				destinationDocNameCombo.elementAt(destinCounter).addModifyListener(new ModifyListener() {
					public void modifyText(ModifyEvent event) {
						Combo selectedCombo = (Combo) event.widget;
						//ricavo dal vettore di combo la sua posizione
						int destinComboToRedraw = destinationDocNameCombo.indexOf(selectedCombo);
						//System.out.println("position of the combo!!!"+destinComboToRedraw);

						int sel = destinationDocNameCombo.elementAt(element).getSelectionIndex();
						name = destinationDocNameCombo.elementAt(element).getItem(sel);
						
						destinationInputParam.elementAt(destinComboToRedraw).removeAll();
						
						fillDestinationParamCombo(name, destinComboToRedraw, null);
						destinationInputParam.elementAt(destinComboToRedraw).redraw();
						
					}
				});	
				int sel = destinationDocNameCombo.elementAt(destinCounter).getSelectionIndex();
				if(sel != -1){
					destinationInfo = new DestinationInfo();
					
					destinationInfo.setDocDestName(destinationDocNameCombo.elementAt(destinCounter).getItem(sel));				
					
					int selIn = destinationInputParam.elementAt(destinCounter).getSelectionIndex();		
					destinationInfo.setParamDestName(destinationInputParam.elementAt(destinCounter).getItem(selIn));
					
					destinationInfo.setParamDefaultValue(destinationInputParamDefaultValue.elementAt(destinCounter));
	
					destinationInfos.add(destinationInfo);	
				}
				composite.pack(false);
				//composite.redraw();
				composite.getParent().redraw();

			}
		});
	
		

		composite.pack(false);
		composite.redraw();
		
		setControl(composite);
	}

	private void fillDestinationCombo(String docDest, int comboToRedraw){
		
		if(destinationDocNameCombo.elementAt(comboToRedraw).getItemCount() == 0){
			if(metaDoc != null){
				Vector docs = metaDoc.getMetadataDocuments();
				if(docs != null){
					for(int i=0; i<docs.size(); i++){
						String destinationName = ((MetadataDocument)docs.elementAt(i)).getName();
						
							if(destinationName != null && !destinationName.equals("")){
								destinationDocNameCombo.elementAt(comboToRedraw).add(destinationName);
								if(docDest != null && docDest.equals(destinationName)){
									int pos = destinationDocNameCombo.elementAt(comboToRedraw).getItemCount();
									destinationDocNameCombo.elementAt(comboToRedraw).select(pos-1);
								}
							}
						
					}
				}
			}
		}
		String master = masterDocName.getText();
		//per ridisegnare combo
		
		if(master != null && !master.equals("")){

			int posMaster =destinationDocNameCombo.elementAt(comboToRedraw).indexOf(master);
			if(posMaster != -1){
				destinationDocNameCombo.elementAt(comboToRedraw).remove(posMaster);
				
			}
		}
		destinationDocNameCombo.elementAt(comboToRedraw).redraw();
		
	}
	private void fillDestinationParamCombo(String destDoc, int destinComboToRedraw, String paramInSel){

		if(destinComboToRedraw == 0){
			destinationInputParam.elementAt(destinComboToRedraw).removeAll();
		}
		if(metaDoc != null){
			Vector docs = metaDoc.getMetadataDocuments();
			if(docs != null){
				for(int i=0; i<docs.size(); i++){
					MetadataDocument doc = (MetadataDocument)docs.elementAt(i);
					String docName = doc.getName();
					if(docName != null && !docName.equals("") &&(docName.equals(destDoc))){
						Vector params = doc.getMetadataParameters();
						for (int j =0; j<params.size(); j++){
							MetadataParameter param = (MetadataParameter)params.elementAt(j);
							String label = param.getLabel();
							destinationInputParam.elementAt(destinComboToRedraw).add(label);
							if(paramInSel != null && paramInSel.equals(label)){
								int pos = destinationInputParam.elementAt(destinComboToRedraw).getItemCount();
								destinationInputParam.elementAt(destinComboToRedraw).select(pos-1);
							}
						}
						
					}
				}
			}
		}
		
	}
	
	private Parameter getNavigationItem(){
		Parameter param = null;
		
		IStructuredSelection selection =((SpagoBIModifyNavigationWizard)getWizard()).getSelection();
		Object objSel = selection.toList().get(0);
		Table listOfNavigations = (Table)objSel;
		TableItem[] itemsSel = listOfNavigations.getSelection();
		Composite composite = navigationNameText.getParent().getParent();
		
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.widthHint = 260;
		gridData.minimumWidth =200;
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
	
		//gridData.horizontalSpan = 2;
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
				    				
				    				String masterDoc = ((Document)docs.elementAt(i)).getSbiObjLabel();
				    				String masterParam = param.getSbiParLabel();
				    				String masterParamDefault = param.getDefaultVal();
				    				
				    				masterDocName.setText(masterDoc);
				    				masterDocName.setEditable(false);
				    				
				    				masterParamName.setText(masterParam);
				    				masterParamName.setEditable(false);
				    				
				    				masterDefaultValueOutputParam.setText(masterParamDefault);
				    				masterDefaultValueOutputParam.setEditable(false);
				    				
				    				//cicla su destinazioni
				    				Refresh refresh = param.getRefresh();
				    				Vector<RefreshDocLinked> destinations = refresh.getRefreshDocLinked();
				    				for(int k =0; k<destinations.size(); k++){
				    					
				    					destinCounter++;
				    					
				    					RefreshDocLinked destin = destinations.get(k);
				    					String docDest = destin.getLabelDoc();
				    					final String docDestParam = destin.getLabelParam();
				    					//inserisce blocco x destinazione
				    					Composite destBlock = new Composite(composite, SWT.BORDER | SWT.FILL);
				    					GridLayout glBlock = new GridLayout();
				    					glBlock.numColumns = 3;
				    					destBlock.setLayout(glBlock);
				    					
				    					new Label(destBlock, SWT.NONE).setText("Destination document:");	
				    					destinationDocNameCombo.addElement(new Combo(destBlock, SWT.BORDER |SWT.READ_ONLY ));

				    					destinationDocNameCombo.elementAt(k).setLayoutData(gridData);
				    					destinationDocNameCombo.elementAt(k).setVisible(true);
				    					
				    					fillDestinationCombo(docDest, k);

				    					//crea una nuovo output text
				    					new Label(destBlock, SWT.NONE).setText("Input parameter:");
				    					Combo newText =new Combo(destBlock, SWT.BORDER |SWT.READ_ONLY);

				    					
				    					destinationInputParam.addElement(newText);
				    					newText.setLayoutData(gridData);
				    					fillDestinationParamCombo(docDest, k, docDestParam);

				    					new Label(destBlock, SWT.NONE).setText("Default value:");
				    					destinationInputParamDefaultValue.addElement(new Text(destBlock, SWT.BORDER));
				    					destinationInputParamDefaultValue.elementAt(k).setLayoutData(gridData);
				    					setDefaultValue(docs,k,docDest,docDestParam);
				    					
				    					final int element = k;
				    					
				    					destinationDocNameCombo.elementAt(k).addModifyListener(new ModifyListener() {
				    						public void modifyText(ModifyEvent event) {
				    							Combo selectedCombo = (Combo) event.widget;
				    							//ricavo dal vettore di combo la sua posizione
				    							int destinComboToRedraw = destinationDocNameCombo.indexOf(selectedCombo);
				    							//System.out.println("position of the combo!!!"+destinComboToRedraw);

				    							int sel = destinationDocNameCombo.elementAt(element).getSelectionIndex();
				    							name = destinationDocNameCombo.elementAt(element).getItem(sel);
				    							
				    							destinationInputParam.elementAt(destinComboToRedraw).removeAll();
				    							
				    							fillDestinationParamCombo(name, destinComboToRedraw, docDestParam);
				    							destinationInputParam.elementAt(destinComboToRedraw).redraw();
				    							
				    						}
				    					});					    					
				    					destinationInfo = new DestinationInfo();
				    					int sel = destinationDocNameCombo.elementAt(destinCounter).getSelectionIndex();
				    					destinationInfo.setDocDestName(destinationDocNameCombo.elementAt(destinCounter).getItem(sel));				
				    					
				    					int selIn = destinationInputParam.elementAt(destinCounter).getSelectionIndex();		
				    					destinationInfo.setParamDestName(destinationInputParam.elementAt(destinCounter).getItem(selIn));
				    					
				    					destinationInfo.setParamDefaultValue(destinationInputParamDefaultValue.elementAt(destinCounter));

				    					destinationInfos.add(destinationInfo);
				    				}				    				
				    				//fine

				    			}	

				    		}
			    			
		    				setPageComplete(true);
		    				composite.redraw();
						}	
					}
				}
			}
		}
		
		return param;
	}
	private void setDefaultValue(Vector<Document> docs, int destinPos, String destDoc, String parLabel){
		if(docs != null){
			for(int i=0; i<docs.size(); i++){
				Document doc = (Document)docs.elementAt(i);
				if(doc.getSbiObjLabel().equals(destDoc)){
					Parameters params = (doc).getParameters();
					if(params != null){
			    		for (int j = 0; j<params.getParameter().size(); j++){
			    			Parameter param = params.getParameter().elementAt(j);
			    			if(param.getSbiParLabel().equals(parLabel )&& param.getType().equals("IN")){
			    				destinationInputParamDefaultValue.elementAt(destinPos).setText(param.getDefaultVal());
			    			}
			    		}
					}
				}
			}
		}	
		
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

	public Vector<Combo> getDestinationInputParam() {
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
	public Vector<Text> getDestinationInputParamDefaultValue() {
		return destinationInputParamDefaultValue;
	}
	public void setDestinationInputParamDefaultValue(
			Vector<Text> destinationInputParamDefaultValue) {
		this.destinationInputParamDefaultValue = destinationInputParamDefaultValue;
	}
	public Text getMasterDefaultValueOutputParam() {
		return masterDefaultValueOutputParam;
	}
	public void setMasterDefaultValueOutputParam(Text masterDefaultValueOutputParam) {
		this.masterDefaultValueOutputParam = masterDefaultValueOutputParam;
	}
}
