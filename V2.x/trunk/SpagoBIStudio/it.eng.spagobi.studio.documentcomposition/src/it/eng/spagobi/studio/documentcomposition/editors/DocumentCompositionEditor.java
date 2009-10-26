package it.eng.spagobi.studio.documentcomposition.editors;


import java.util.Iterator;

import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo.ModelBO;
import it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView;
import it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

import test.Designer;
import test.DocContainer;


public class DocumentCompositionEditor extends EditorPart {

	String templateName="";
	int height=0;
	int width=0;
	Composite parent;
	DocumentComposition documentComposition;
	Designer designer;

	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub

	}

	@Override
	public void doSaveAs() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
	throws PartInitException {
		SpagoBILogger.infoLog(DocumentCompositionEditor.class.toString()+": init function");
		this.setPartName(input.getName());
		FileEditorInput fei = (FileEditorInput) input;
		setInput(input);
		setSite(site);
		IFile file = fei.getFile();
		templateName=file.getName();
		ModelBO bo=new ModelBO();
		try {
			documentComposition = bo.createModel(file);
			bo.saveModel(documentComposition);
		} catch (CoreException e) {
			e.printStackTrace();
			SpagoBILogger.errorLog(DocumentCompositionEditor.class.toString()+": Error in reading template", e);
			throw(new PartInitException("Error in reading template"));
		}
		SpagoBILogger.infoLog("END "+DocumentCompositionEditor.class.toString()+": init function");
	}

	@Override
	public boolean isDirty() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createPartControl(Composite _parent) {
		SpagoBILogger.infoLog(DocumentCompositionEditor.class.toString()+": Create Part Control function");
		
		//inserisce le 3 viste
        try {
            IWorkbenchPage iworkbenchpage = PlatformUI.getWorkbench()
                            .getActiveWorkbenchWindow().getActivePage();
            if(iworkbenchpage != null){
	            if (iworkbenchpage.findView("it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView") == null ){
	                        iworkbenchpage.showView("it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView");
	            }
	            if (iworkbenchpage.findView("it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView") == null ){
	                iworkbenchpage.showView("it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView");
	            }
	            if (iworkbenchpage.findView("it.eng.spagobi.studio.documentcomposition.views.NavigationView") == null ){
	                iworkbenchpage.showView("it.eng.spagobi.studio.documentcomposition.views.NavigationView");
	            }
            }
        } catch (PartInitException partinitexception) {
                partinitexception.printStackTrace();
        }
		
		
		
		parent=_parent;
		FormToolkit toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);
		//		TableWrapLayout layout = new TableWrapLayout();
		//		layout.numColumns = 1;
		//		layout.horizontalSpacing = 20;
		//		layout.verticalSpacing = 10;
		//		layout.topMargin = 20;
		//		layout.leftMargin = 20;
		FillLayout fill = new FillLayout();

		form.getBody().setLayout(fill);

		Section section = toolkit.createSection(form.getBody(), 
				Section.TITLE_BAR);
		//section.setLayoutData()
		section.setSize(1000, 1000);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		section.setText("Document Composition designer");
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setSize(1000, 1000);

		designer=new Designer(sectionClient);
		designer.setEditor(this);
		section.setClient(sectionClient);

		// Add the control resize, shell resize all the document Containers
		width=parent.getBounds().width;
		height=parent.getBounds().height;

		// Now initialize Editor with DocumentComposition retrieved
		initializeEditor(documentComposition);

		SpagoBILogger.infoLog("END "+DocumentCompositionEditor.class.toString()+": create Part Control function");

	}


	public void initializeEditor(DocumentComposition documentComposition){
		SpagoBILogger.infoLog("START: "+DocumentCompositionEditor.class.toString()+" initialize Editor");	
		// clean the properties View
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		// Document properties
		IWorkbenchPage aa=a.getActivePage();
		try{
			IViewReference w=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView");
			Object p=w.getPart(false);
			if(p!=null){
				DocumentPropertiesView view=(DocumentPropertiesView)p;
				view.cleanSizeAndProperties();
			}
			else{
				// View not present
			}
		}
		catch (Exception e) {
			SpagoBILogger.warningLog("Window not active, could not empty the property view");	
		}
		try{
			IViewReference w2=aa.findViewReference("it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView");
			Object p2=w2.getPart(false);
			if(p2!=null){
				DocumentParametersView view=(DocumentParametersView)p2;
				view.cleanParameters();
			}
			else{
				// View not present
			}
		}
		catch (Exception e) {
			SpagoBILogger.warningLog("Window not active, could not empty the property view");	
		}

		// Initialize Designer
		designer.initializeDesigner(documentComposition);
		SpagoBILogger.infoLog("END: "+DocumentCompositionEditor.class.toString()+" initialize Editor");	
	}


	@Override
	public void setFocus() {
		// TODO Auto-generated method stub

	}

	public DocumentComposition getDocumentComposition() {
		return documentComposition;
	}

	public void setDocumentComposition(DocumentComposition documentComposition) {
		this.documentComposition = documentComposition;
	}

	public Designer getDesigner() {
		return designer;
	}

	public void setDesigner(Designer designer) {
		this.designer = designer;
	}



}
