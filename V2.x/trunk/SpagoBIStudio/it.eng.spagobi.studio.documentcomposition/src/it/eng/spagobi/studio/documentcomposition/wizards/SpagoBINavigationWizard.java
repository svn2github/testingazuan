package it.eng.spagobi.studio.documentcomposition.wizards;

import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Document;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameter;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.Parameters;
import it.eng.spagobi.studio.documentcomposition.util.XmlTemplateGenerator;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardDestinDocPage;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardMasterDocPage;
import it.eng.spagobi.studio.documentcomposition.wizards.pages.NewNavigationWizardPage;

import java.util.Vector;

import org.eclipse.jface.viewers.IStructuredSelection;
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
	public boolean performFinish() {
	    // Create the entry based on the inputs
	    Document docMaster = new Document();
	    
	    
	    //in realtà va a prendere il doc master corrispondente a quello selezionato dall'utente
	    //all'interno del doc master costrisce Parametro con Refresh (navigazione)
	    
	    String masterName= newNavigationWizardMasterDocPage.getMasterDocNameText().getText();
	    docMaster.setLabel(masterName);
	    docMaster.setSbiObjLabel(masterName);
	    
	    Vector<Text> out = newNavigationWizardMasterDocPage.getMasterDocOutputParams();
	    Parameters params = new Parameters();
	    
	    Vector parametersVector = new Vector();
	    
	    for(int i=0; i<out.size(); i++){
	    	Parameter p = new Parameter();
	    	p.setLabel(((Text)out.get(i)).getText());
	    	parametersVector.add(p);
	    }
	    params.setParameter(parametersVector);
	    docMaster.setParameters(params);
	    
	    //destinazione
	    Document docDest = new Document();
	    Parameters paramsDest = new Parameters();
	    
	    int selectedDest = newNavigationWizardDestinDocPage.getDestinationDocNameCombo().getSelectionIndex();
	    String sel = newNavigationWizardDestinDocPage.getDestinationDocNameCombo().getItem(selectedDest);

	    docDest.setLabel(sel);
	    docDest.setSbiObjLabel(sel);

	    Vector<Text> in = newNavigationWizardDestinDocPage.getDestinationOutputParams();
	    Vector<String> inParams = new Vector<String>();
	    
	    Vector parametersVectorDest = new Vector();
	    for(int i=0; i<in.size(); i++){
	    	Parameter p = new Parameter();
	    	p.setLabel(((Text)in.get(i)).getText());
	    	parametersVectorDest.add(p);
	    }
	    
	    paramsDest.setParameter(parametersVectorDest);
	    docDest.setParameters(paramsDest);
	    // Return true to exit wizard
	    
	    DocumentComposition docComp = new DocumentComposition();
	    Vector<Document> docs = new Vector<Document>();
	    
	    docs.add(docMaster);
	    docs.add(docDest);
	    
    
	    //docComp.setNavigationName(newNavigationWizardPage.getNavigationNameText().getText());
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


}
