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
import it.eng.spagobi.studio.documentcomposition.wizards.pages.util.DestinationInfo;

import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.widgets.Combo;
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
	
	private void completePageDataCollection(){
		if(modifyNavigationWizardPage.isPageComplete()){
			DestinationInfo destinationInfo = new DestinationInfo();
			int destinCounter= modifyNavigationWizardPage.getDestinCounter();
			int sel = modifyNavigationWizardPage.getDestinationDocNameCombo().elementAt(destinCounter).getSelectionIndex();
			destinationInfo.setDocDestName(modifyNavigationWizardPage.getDestinationDocNameCombo().elementAt(destinCounter).getItem(sel));
			int selIn = modifyNavigationWizardPage.getDestinationInputParam().elementAt(destinCounter).getSelectionIndex();		
			destinationInfo.setParamDestName(modifyNavigationWizardPage.getDestinationInputParam().elementAt(destinCounter).getItem(selIn));
			destinationInfo.setParamDefaultValue(modifyNavigationWizardPage.getDestinationInputParamDefaultValue().elementAt(destinCounter));
			modifyNavigationWizardPage.getDestinationInfos().add(destinationInfo);	
		}
	}
	
	@Override
	public boolean performFinish() {
		completePageDataCollection();
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

				String masterPar = modifyNavigationWizardPage.getMasterParamName().getText();
	    		//modifica le destinazioni
	    		Parameters params = doc.getParameters();//tag già presente nel modello riempito precedentemente
    			if(params == null){
    				params = new Parameters();//altrimenti lo crea
    			}
    			
    			Vector<Parameter> parameters =params.getParameter();
    			if(parameters == null){
    				parameters = new Vector<Parameter>();
    			}
	    		//aggiunge il parameter IN per la dstinazione
	    		fillInNavigationParams(parameters, doc);
	    		
	    		//aggiunge parametro OUT per doc master
    			Parameter newParam = new Parameter();
    			fillNavigationOutParam(newParam, masterPar);


    			parameters.add(newParam);
				params.setParameter(parameters);
				doc.setParameters(params);
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

	private void fillNavigationOutParam(Parameter param, String out){
		param.setLabel(out);
		param.setSbiParLabel(out);
		param.setNavigationName(modifyNavigationWizardPage.getNavigationNameText().getText());
		param.setDefaultVal(modifyNavigationWizardPage.getMasterDefaultValueOutputParam().getText());
		
		Refresh refresh = new Refresh();
		Vector <RefreshDocLinked> refreshes = new Vector<RefreshDocLinked>();
		
		Vector<DestinationInfo> destInfos = modifyNavigationWizardPage.getDestinationInfos();
		for(int k =0; k<destInfos.size(); k++){
			DestinationInfo destInfo = destInfos.elementAt(k);
			RefreshDocLinked refreshDocLinked = new RefreshDocLinked();
			String toRefresh = destInfo.getDocDestName();

			String paramIn =destInfo.getParamDestName();

			refreshDocLinked.setLabelDoc(toRefresh);
			refreshDocLinked.setLabelParam(paramIn);
			refreshes.add(refreshDocLinked);
			
			refresh.setRefreshDocLinked(refreshes);
		}
		param.setRefresh(refresh);		
		param.setType("OUT");
	}
	private void fillInNavigationParams(Vector<Parameter> parameters, Document doc){
		//cicla su destinazioni
		Vector<DestinationInfo> destInfos = modifyNavigationWizardPage.getDestinationInfos();
		for(int k =0; k<destInfos.size(); k++){
			DestinationInfo destInfo = destInfos.elementAt(k);
			String destinationDoc = destInfo.getDocDestName();
			if(destinationDoc != null && destinationDoc.equals(doc.getLabel())){
				String paramName = destInfo.getParamDestName();
				Parameter param = new Parameter();
				param.setType("IN");
				param.setLabel(paramName);
				param.setSbiParLabel(paramName);
				param.setDefaultVal(destInfo.getParamDefaultValue().getText());
				
				parameters.add(param);
			}
			
		}

	}
}
