package it.eng.spagobi.studio.documentcomposition.wizards.pages;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.wizards.SpagoBINavigationWizard;

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
	String paramOut ="";
	
	Combo masterDocName;
	Text masterDocOutputParam;
	
	public Combo getMasterDocName() {
		return masterDocName;
	}

	public void setMasterDocName(Combo masterDocName) {
		this.masterDocName = masterDocName;
	}
	

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
		int sel = masterDocName.getSelectionIndex();
		String master = masterDocName.getItem(sel);
		
		if ((masterDocOutputParam.getText() == null || masterDocOutputParam.getText().length() == 0)
				&&(sel ==-1 || master == null )) {
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
		new Label(composite, SWT.NONE).setText("Master document:");
		masterDocName = new Combo(composite, SWT.BORDER |SWT.READ_ONLY );
		fillMasterCombo();

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		masterDocName.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 1;
		gd.grabExcessHorizontalSpace = true;
		gd.widthHint = 200;

		// fielset per parametri output
		new Label(composite, SWT.NONE).setText("Ouput parameter:");
		masterDocOutputParam = new Text(composite, SWT.BORDER);
		masterDocOutputParam.setLayoutData(gd);

		masterDocName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				name = masterDocName.getText();
				SpagoBINavigationWizard wizard = (SpagoBINavigationWizard)getWizard();
				wizard.setSelectedMaster(masterDocName.getText());
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


	public Text getMasterDocOutputParam() {
		return masterDocOutputParam;
	}
	private void fillMasterCombo(){
		DocumentComposition docComp = Activator.getDefault().getDocumentComposition();
		if(docComp != null){
			Vector docs = docComp.getDocumentsConfiguration().getDocuments();
			if(docs != null){
				for(int i=0; i<docs.size(); i++){
					String masterName = ((Document)docs.elementAt(i)).getLabel();
					if(masterName != null && !masterName.equals("")){
						masterDocName.add(masterName);
					}
				}
			}
		}
	}
}
