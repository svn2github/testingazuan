package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocument;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataParameter;
import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBINavigationWizard;

import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class NewNavigationWizardMasterDocPage extends WizardPage {


	String name = "";
	String masterLabel = "";
	String paramOut ="";
	
	
	HashMap <String, String> docInfoUtil;


	Combo masterDocName;
	Combo masterDocOutputParam;
	Text masterDefaultValueOutputParam;
	
	private MetadataDocumentComposition metaDoc;

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
		int sel = masterDocName.getSelectionIndex();
		String master =null;
		if(sel != -1){
			 master = masterDocName.getItem(sel);
		}
		if ((masterDocOutputParam.getText() == null || masterDocOutputParam.getText().length() == 0)
				&&(sel == -1 || master == null )) {
			return false;
		}

		return true;
	}
	
	public void createControl(Composite parent) {
		
		final Composite composite = new Composite(parent, SWT.BORDER
				| SWT.NO_REDRAW_RESIZE);
		composite.setSize(600, 400);
		final GridLayout gl = new GridLayout();

		int ncol = 3;
		gl.numColumns = ncol;

		composite.setLayout(gl);
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		
		new Label(composite, SWT.NONE).setText("Master document:");
		masterDocName = new Combo(composite, SWT.BORDER |SWT.READ_ONLY );
		fillMasterCombo();
		masterDocName.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.grabExcessHorizontalSpace = true;
		gd.widthHint = 200;

		// fielset per parametri output
		new Label(composite, SWT.NONE).setText("Ouput parameter:");
		masterDocOutputParam = new Combo(composite, SWT.BORDER );
		masterDocOutputParam.setLayoutData(gd);
		
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		masterDocName.setLayoutData(gd);
		
		new Label(composite, SWT.NONE).setText("Default value:");
		masterDefaultValueOutputParam = new Text(composite, SWT.BORDER);
		masterDefaultValueOutputParam.setLayoutData(gd);

		masterDocName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				name = masterDocName.getText();
				SpagoBINavigationWizard wizard = (SpagoBINavigationWizard)getWizard();
				wizard.setSelectedMaster(masterDocName.getText());
				fillMasterParamCombo(name);
				masterLabel = docInfoUtil.get(name);
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



	private void fillMasterCombo(){
		docInfoUtil = new HashMap<String, String>();
		metaDoc = Activator.getDefault().getMetadataDocumentComposition();
		if(metaDoc != null){
			Vector docs = metaDoc.getMetadataDocuments();
			if(docs != null){
				for(int i=0; i<docs.size(); i++){

					MetadataDocument doc = (MetadataDocument)docs.elementAt(i);
					String masterName = doc.getName();
					String masterLabel = doc.getLabel();
					if(masterName != null && !masterName.equals("")){
						masterDocName.add(masterName);	
						docInfoUtil.put(masterName, masterLabel);
					}
				}
			}
		}
	}
	
	private void fillMasterParamCombo(String masterDoc){
		masterDocOutputParam.removeAll();
		if(metaDoc != null){
			Vector docs = metaDoc.getMetadataDocuments();
			if(docs != null){
				for(int i=0; i<docs.size(); i++){
					MetadataDocument doc = (MetadataDocument)docs.elementAt(i);
					String masterName = doc.getName();
					if(masterName != null && !masterName.equals("") &&(masterName.equals(masterDoc))){
						Vector params = doc.getMetadataParameters();
						if(params != null){
							for (int j =0; j<params.size(); j++){
								MetadataParameter param = (MetadataParameter)params.elementAt(j);
								String label = param.getLabel();
								masterDocOutputParam.add(label);
							}
						}
					}
				}
			}
		}
		masterDocOutputParam.redraw();
	}
	public Text getMasterDefaultValueOutputParam() {
		return masterDefaultValueOutputParam;
	}

	public void setMasterDefaultValueOutputParam(Text masterDefaultValueOutputParam) {
		this.masterDefaultValueOutputParam = masterDefaultValueOutputParam;
	}

	public Combo getMasterDocName() {
		return masterDocName;
	}

	public void setMasterDocName(Combo masterDocName) {
		this.masterDocName = masterDocName;
	}
	public Combo getMasterDocOutputParam() {
		return masterDocOutputParam;
	}

	public String getName() {
		return name;
	}

	public String getParamOut() {
		return paramOut;
	}

	public HashMap<String, String> getDocInfoUtil() {
		return docInfoUtil;
	}

	public void setDocInfoUtil(HashMap<String, String> docInfoUtil) {
		this.docInfoUtil = docInfoUtil;
	}

	public String getMasterLabel() {
		return masterLabel;
	}

	public void setMasterLabel(String masterLabel) {
		this.masterLabel = masterLabel;
	}


}
