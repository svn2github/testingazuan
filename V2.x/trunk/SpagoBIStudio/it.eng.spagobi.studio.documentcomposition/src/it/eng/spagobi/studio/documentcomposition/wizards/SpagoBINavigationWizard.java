package it.eng.spagobi.studio.documentcomposition.wizards;

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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class SpagoBINavigationWizard extends Wizard implements INewWizard{

	// dashboard creation page
	private NewNavigationWizardPage newNavigationWizardPage;
	private NewNavigationWizardMasterDocPage newNavigationWizardMasterDocPage;
	private NewNavigationWizardDestinDocPage newNavigationWizardDestinDocPage;
	

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
		
		System.out.println("finish::1");
		///////////solo x test//////////////
		
		HashMap<String, Text> destInfos = newNavigationWizardDestinDocPage.getDestinationInfo();
		Iterator it = destInfos.keySet().iterator();
		while(it.hasNext()){
			RefreshDocLinked refreshDocLinked = new RefreshDocLinked();
			String toRefresh = (String)it.next();
			String paramIn = ((Text)destInfos.get(toRefresh)).getText();

			System.out.println("name::"+toRefresh);
			System.out.println("param in::"+paramIn);
	
		}
		System.out.println("finish::2");
		////////////////////////////////////
		String masterName= newNavigationWizardMasterDocPage.getName();
	    //in realtà prende il doc master corrispondente a quello selezionato dall'utente
	    //all'interno del doc master costrisce Parametro con Refresh (navigazione)	
		DocumentComposition docComp = new DocumentComposition ();

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
	    		/*probabilmente nn serve........................*/
	    		for (int j = 0; j<params.getParameter().size(); j++){
	    			Parameter param = params.getParameter().elementAt(j);
	    			if(param.getLabel().equalsIgnoreCase(out.getText())){
	    				fillNavigationParam(param, out);
	    				found = true;
	    			}
	    		}
	    		/*probabilm sarà solo questo................*/
	    		if(!found){
	    			//altrimenti lo aggiunge
	    			Parameter newParam = new Parameter();
	    			fillNavigationParam(newParam, out);
    				
	    		}
	    	}
	    }

	    XmlTemplateGenerator generator = new XmlTemplateGenerator();
	    generator.transformToXml(docComp);
	    return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		setWindowTitle("New navigation creation");
		
		newNavigationWizardPage = new NewNavigationWizardPage();
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage();
		newNavigationWizardDestinDocPage = new NewNavigationWizardDestinDocPage();
	}
	
	public void addPages() {
		super.addPages();
		newNavigationWizardPage = new NewNavigationWizardPage("New Document");
		addPage(newNavigationWizardPage);
		newNavigationWizardMasterDocPage = new NewNavigationWizardMasterDocPage("Master document");
		addPage(newNavigationWizardMasterDocPage);
		
		newNavigationWizardDestinDocPage = new NewNavigationWizardDestinDocPage("Destination document");
		addPage(newNavigationWizardDestinDocPage);
		
	}

	private void fillNavigationParam(Parameter param, Text out){
		param.setLabel(out.getText());
		param.setSbiParLabel(out.getText());
		
		Refresh refresh = new Refresh();
		Vector <RefreshDocLinked> refreshes = new Vector<RefreshDocLinked>();
		
		
		HashMap<String, Text> destInfos = newNavigationWizardDestinDocPage.getDestinationInfo();
		Iterator it = destInfos.keySet().iterator();
		while(it.hasNext()){
			RefreshDocLinked refreshDocLinked = new RefreshDocLinked();
			String toRefresh = (String)it.next();
			String paramIn = ((Text)destInfos.get(toRefresh)).toString();

			refreshDocLinked.setLabelDoc(toRefresh);
			refreshDocLinked.setLabelParam(paramIn);
			refreshes.add(refreshDocLinked);
		}
		
		refresh.setRefreshDocLinked(refreshes);
		param.setRefresh(refresh);
		
		param.setType("OUT");
	}
}
