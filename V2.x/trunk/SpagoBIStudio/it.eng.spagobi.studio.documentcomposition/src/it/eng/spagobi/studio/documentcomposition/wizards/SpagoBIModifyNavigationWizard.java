package it.eng.spagobi.studio.documentcomposition.wizards;

import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentsConfiguration;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Refresh;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.RefreshDocLinked;
import it.eng.spagobi.studio.documentcomposition.util.XmlTemplateGenerator;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.ModifyNavigationWizardPage;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardDestinDocPage;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardMasterDocPage;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardPage;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class SpagoBIModifyNavigationWizard extends Wizard implements INewWizard{

	// workbench selection when the wizard was started
	protected IStructuredSelection selection;


	// the workbench instance
	protected IWorkbench workbench;

	// dashboard creation page
	private ModifyNavigationWizardPage modifyNavigationWizardPage;
	
	private String selectedMaster;

	public String getSelectedMaster() {
		return selectedMaster;
	}


	public void setSelectedMaster(String selectedMaster) {
		this.selectedMaster = selectedMaster;
	}

	public IStructuredSelection getSelection() {
		return selection;
	}


	public void setSelection(IStructuredSelection selection) {
		this.selection = selection;
	}
	public SpagoBIModifyNavigationWizard() {
		super();
	}

	
	@Override
	public void addPage(IWizardPage page) {
		// TODO Auto-generated method stub
		super.addPage(page);
	}
	@Override
	public boolean performFinish() {
	
		//recupera da plugin oggetto DocumentComposition
		
		DocumentComposition docComp = Activator.getDefault().getDocumentComposition();
		
		String masterName= modifyNavigationWizardPage.getMasterDocName().getText();
	    //in realtà prende il doc master corrispondente a quello selezionato dall'utente
	    //all'interno del doc master costrisce Parametro con Refresh (navigazione)	
		//DocumentComposition docComp = new DocumentComposition ();

		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
	    Vector documents = docConf.getDocuments();
	    
	    //elabora documento master
	    for (int i = 0; i< documents.size(); i++){
	    	Document doc = (Document)documents.get(i);
	    	String docLabel = doc.getLabel();
	    	if(docLabel.equalsIgnoreCase(masterName)){
	    		doc.setLabel(masterName);
	    		doc.setSbiObjLabel(masterName);
	    		Text out = modifyNavigationWizardPage.getMasterParamName();	    		
	    		//campo out a cui vengono assegnati
	    		//ciclo sui parameters del doc master 
	    		
	    		//se presente gli aggiunge il type e i rafresh
	    		Parameters params = doc.getParameters();
	    		boolean found = false;
	    		if(params != null){
		    		/*probabilmente nn serve........................*/
		    		for (int j = 0; j<params.getParameter().size(); j++){
		    			Parameter param = params.getParameter().elementAt(j);
		    			if(param.getLabel().equalsIgnoreCase(out.getText())){
		    				fillNavigationParam(param, out);
		    				found = true;
		    			}
		    		}
	    		}
	    		/*probabilm sarà solo questo................*/
	    		if(!found){
	    			//altrimenti lo aggiunge
	    			Parameter newParam = new Parameter();
	    			fillNavigationParam(newParam, out);
	    			if(params == null){
	    				params = new Parameters();
	    			}
	    			Vector parameter =params.getParameter();
	    			if(parameter == null){
	    				parameter = new Vector<Parameter>();
	    			}
	    			parameter.add(newParam);
    				params.setParameter(parameter);
    				doc.setParameters(params);
	    		}
	    	}
	    }
	    Activator.getDefault().setDocumentComposition(docComp);///////////////NB risetta!!!
	    XmlTemplateGenerator generator = new XmlTemplateGenerator();
	    generator.transformToXml(docComp);
	    return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("Modify navigation");
		
		this.workbench = workbench;
		this.selection = selection;
		
		
		modifyNavigationWizardPage = new ModifyNavigationWizardPage();
	}
	
	public void addPages() {
		super.addPages();
		modifyNavigationWizardPage = new ModifyNavigationWizardPage("Modify navigation");
		addPage(modifyNavigationWizardPage);
		modifyNavigationWizardPage.setPageComplete(true);
		
	}

	private void fillNavigationParam(Parameter param, Text out){
		
		Refresh refresh = new Refresh();
		Vector <RefreshDocLinked> refreshes = new Vector<RefreshDocLinked>();
		
		
		Vector<HashMap> destInfos = modifyNavigationWizardPage.getDestinationInfos();
		for(int k =0; k<destInfos.size(); k++){
			HashMap<String, Text> destInfo = destInfos.elementAt(k);
			Iterator it = destInfo.keySet().iterator();
			while(it.hasNext()){
				RefreshDocLinked refreshDocLinked = new RefreshDocLinked();
				String toRefresh = (String)it.next();
	
				String paramIn = ((Text)destInfo.get(toRefresh)).getText();
	
				refreshDocLinked.setLabelDoc(toRefresh);
				refreshDocLinked.setLabelParam(paramIn);
				refreshes.add(refreshDocLinked);
			}
		
			refresh.setRefreshDocLinked(refreshes);
		}
		param.setRefresh(refresh);

	}
}
