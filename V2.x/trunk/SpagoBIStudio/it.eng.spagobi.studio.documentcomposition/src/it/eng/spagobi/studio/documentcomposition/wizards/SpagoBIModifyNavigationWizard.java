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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Combo;
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
	
	private void completePageDataCollection(){
		if(modifyNavigationWizardPage.isPageComplete()){
			DestinationInfo destinationInfo = new DestinationInfo();
			int destinCounter= modifyNavigationWizardPage.getDestinCounter();
			if(destinCounter!= -1){
				if(!modifyNavigationWizardPage.getDestinationDocNameCombo().elementAt(destinCounter).isDisposed()){
					int sel = modifyNavigationWizardPage.getDestinationDocNameCombo().elementAt(destinCounter).getSelectionIndex();
					destinationInfo.setDocDestName(modifyNavigationWizardPage.getDestinationDocNameCombo().elementAt(destinCounter).getItem(sel));
					int selIn = modifyNavigationWizardPage.getDestinationInputParam().elementAt(destinCounter).getSelectionIndex();		
					destinationInfo.setParamDestName(modifyNavigationWizardPage.getDestinationInputParam().elementAt(destinCounter).getItem(selIn));
					destinationInfo.setParamDefaultValue(modifyNavigationWizardPage.getDestinationInputParamDefaultValue().elementAt(destinCounter));
					modifyNavigationWizardPage.getDestinationInfos().add(destinationInfo);	
				}
			}
		}
	}
	private void redrawTable(){
		//*INSERISCE NELLA LISTA DELLE NAVIGATION LA NUOVA NAVIGAZIONE*/
		 
		Object objSel = selection.toList().get(0);
		Table listOfNavigations = (Table)objSel;
		
		TableItem[] items = listOfNavigations.getSelection();
      
	    StringBuffer dest = new StringBuffer();
	    
		Vector<DestinationInfo> destInfos = modifyNavigationWizardPage.getDestinationInfos();
		for(int k =0; k<destInfos.size(); k++){
			DestinationInfo destInfo = destInfos.elementAt(k);
			String destinationDoc = destInfo.getDocDestName();
			if(destinationDoc != null){
	    		dest.append((destInfos.elementAt(k)).getDocDestName());
	    		if(k != destInfos.size()-1){
	    			dest.append(" - ");
	    		}
			}
			
		}

		items[0].setText(2, dest.toString());
    
	    listOfNavigations.getShell().redraw();
		////////////////////////////////////
		
	}
	@Override
	public boolean performFinish() {
		XmlTemplateGenerator generator = new XmlTemplateGenerator();
		
		
		//completePageDataCollection();
		
		redrawTable();
		//recupera da plugin oggetto DocumentComposition
		
		DocumentComposition docComp = Activator.getDefault().getDocumentComposition();

		String masterName= modifyNavigationWizardPage.getMasterDocName().getText();
	    //in realtà prende il doc master corrispondente a quello selezionato dall'utente
	    //all'interno del doc master costrisce Parametro con Refresh (navigazione)	
		//DocumentComposition docComp = new DocumentComposition ();

		DocumentsConfiguration docConf = docComp.getDocumentsConfiguration();
	    Vector documents = docConf.getDocuments();
	    if(documents != null){
		    //elabora documento master
		    for (int i = 0; i< documents.size(); i++){
		    	Document doc = (Document)documents.get(i);
		    	String docLabel = doc.getSbiObjLabel();
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
		    		//aggiunge parametro OUT per doc master
	    			Parameter newParam = new Parameter();
	    			fillNavigationOutParam(newParam, masterPar);
	
	
	    			parameters.add(newParam);
					params.setParameter(parameters);
					doc.setParameters(params);
		    	}else{
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
			
		    		
		    		params.setParameter(parameters);
					doc.setParameters(params);
		    	}
		    }
	    }
	    //Activator.getDefault().setDocumentComposition(docComp);///////////////NB risetta!!!
	    
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

	private void fillNavigationOutParam(Parameter param, String masterParam){

		param.setSbiParLabel(masterParam);
		param.setNavigationName(modifyNavigationWizardPage.getNavigationNameText().getText());
		param.setDefaultVal(modifyNavigationWizardPage.getMasterDefaultValueOutputParam().getText());
		
		Refresh refresh = param.getRefresh();
		Vector <RefreshDocLinked> refreshes = refresh.getRefreshDocLinked();
		
		Vector<DestinationInfo> destInfos = modifyNavigationWizardPage.getDestinationInfos();
		for(int k =0; k<destInfos.size(); k++){
			DestinationInfo destInfo = destInfos.elementAt(k);

			
			HashMap<String, String> docInfoUtil= modifyNavigationWizardPage.getDocInfoUtil();
			String toRefresh = docInfoUtil.get(destInfo.getDocDestName());

			String paramIn =destInfo.getParamDestName();
			RefreshDocLinked refreshDocLinked = refreshDocAlreadyExists(paramIn, toRefresh, refreshes);
			if(refreshDocLinked == null){
				refreshDocLinked = new RefreshDocLinked();
			}
			
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
		HashMap<String, String> docInfoUtil= modifyNavigationWizardPage.getDocInfoUtil();
		Vector<DestinationInfo> destInfos = modifyNavigationWizardPage.getDestinationInfos();
		for(int k =0; k<destInfos.size(); k++){
			DestinationInfo destInfo = destInfos.elementAt(k);
			String destinationDoc = destInfo.getDocDestName();
			//recupera da hashmap di utilità la label corrispondente
			String destLabel = docInfoUtil.get(destinationDoc);
			
			if(destLabel != null && destLabel.equals(doc.getSbiObjLabel())){
				String paramName = destInfo.getParamDestName();		
				Parameter param = inParameterAlreadyExists(paramName, parameters);
				if(param == null){
					param = new Parameter();
				}
				//altrimenti lo prende dal DocumentSComposition - Parameters
				param.setType("IN");
				param.setSbiParLabel(paramName);
				param.setDefaultVal(destInfo.getParamDefaultValue().getText());
				
				parameters.add(param);
			}			
		}
	}
	private Parameter inParameterAlreadyExists(String paramName, Vector<Parameter> parameters){
		Parameter paramFound = null; 
		for(int i=0; i<parameters.size(); i++){
			Parameter param = parameters.elementAt(i);
			if(param.getSbiParLabel().equals(paramName)){
				paramFound = param;
			}
		}
		
		return paramFound;
	}
	private RefreshDocLinked refreshDocAlreadyExists(String paramName, String docName, Vector<RefreshDocLinked> refreshes){
		RefreshDocLinked docRefrFound = null; 
		for(int i=0; i<refreshes.size(); i++){
			RefreshDocLinked doc = refreshes.elementAt(i);
			if(doc.getLabelDoc().equals(docName) && doc.getLabelParam().equals(paramName)){
				docRefrFound = doc;
			}
		}
		
		return docRefrFound;
	}
}
