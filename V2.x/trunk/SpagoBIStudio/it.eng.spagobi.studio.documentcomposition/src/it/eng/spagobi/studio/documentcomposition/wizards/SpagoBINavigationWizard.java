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

public class SpagoBINavigationWizard extends Wizard implements INewWizard{

	// workbench selection when the wizard was started
	protected IStructuredSelection selection;
	// the workbench instance
	protected IWorkbench workbench;

	// dashboard creation page
	private NewNavigationWizardPage newNavigationWizardPage;
	private NewNavigationWizardMasterDocPage newNavigationWizardMasterDocPage;
	private NewNavigationWizardDestinDocPage newNavigationWizardDestinDocPage;
	
	private String selectedMaster;

	public String getSelectedMaster() {
		return selectedMaster;
	}


	public void setSelectedMaster(String selectedMaster) {
		this.selectedMaster = selectedMaster;
	}


	public SpagoBINavigationWizard() {
		super();
	}

	
	@Override
	public void addPage(IWizardPage page) {
		// TODO Auto-generated method stub
		super.addPage(page);
	}
	@Override
	public boolean performFinish() {
		
		//////////////CODICE DEFINITIVO//////////////////////
		//*INSERISCE NELLA LISTA DELLE NAVIGATION LA NUOVA NAVIGAZIONE*/
		// get the folder selected:  
		Object objSel = selection.toList().get(0);
		// FolderSel is the folder in wich to insert the new template
		Table listOfNavigations = (Table)objSel;
	    TableItem item = new TableItem(listOfNavigations, SWT.NONE);
		RGB rgb=new RGB(192,0,0);
		final Color color = new Color(listOfNavigations.getShell().getDisplay(), rgb);
	    item.setForeground(color);
	    item.setText(0, newNavigationWizardPage.getNavigationNameText().getText());
	    listOfNavigations.getShell().redraw();
		////////////////////////////////////
		
		//recupera da plugin oggetto DocumentComposition
		
		DocumentComposition docComp = Activator.getDefault().getDocumentComposition();
		
		String masterName= newNavigationWizardMasterDocPage.getName();
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
	    		Text out = newNavigationWizardMasterDocPage.getMasterDocOutputParam();	    		
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
		setWindowTitle("New navigation creation");
		
		this.workbench = workbench;
		this.selection = selection;
		
		newNavigationWizardPage = new NewNavigationWizardPage();
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage();
		newNavigationWizardDestinDocPage = new NewNavigationWizardDestinDocPage();
	}
	
	public void addPages() {
		super.addPages();
		newNavigationWizardPage = new NewNavigationWizardPage("New Document");
		addPage(newNavigationWizardPage);
		newNavigationWizardPage.setPageComplete(false);
		
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage("Master document");
		addPage(newNavigationWizardMasterDocPage);
		newNavigationWizardMasterDocPage.setPageComplete(false);
		
		newNavigationWizardDestinDocPage = new NewNavigationWizardDestinDocPage("Destination document");
		addPage(newNavigationWizardDestinDocPage);
		
	}

	private void fillNavigationParam(Parameter param, Text out){
		param.setLabel(out.getText());
		param.setSbiParLabel(out.getText());
		param.setNavigationName(newNavigationWizardPage.getNavigationNameText().getText());
		
		Refresh refresh = new Refresh();
		Vector <RefreshDocLinked> refreshes = new Vector<RefreshDocLinked>();
		
		
		HashMap<String, Text> destInfos = newNavigationWizardDestinDocPage.getDestinationInfo();
		Iterator it = destInfos.keySet().iterator();
		while(it.hasNext()){
			RefreshDocLinked refreshDocLinked = new RefreshDocLinked();
			String toRefresh = (String)it.next();
			System.out.println("to refresh::");
			String paramIn = ((Text)destInfos.get(toRefresh)).getText();

			refreshDocLinked.setLabelDoc(toRefresh);
			refreshDocLinked.setLabelParam(paramIn);
			refreshes.add(refreshDocLinked);
		}
		
		refresh.setRefreshDocLinked(refreshes);
		param.setRefresh(refresh);
		
		param.setType("OUT");
	}
}
