package it.eng.spagobi.studio.documentcomposition.editors;


import it.eng.spagobi.studio.core.log.SpagoBILogger;
import it.eng.spagobi.studio.documentcomposition.Activator;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.DocumentComposition;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.bo.ModelBO;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataBO;
import it.eng.spagobi.studio.documentcomposition.editors.model.documentcomposition.metadata.MetadataDocumentComposition;
import it.eng.spagobi.studio.documentcomposition.util.DocCompUtilities;
import it.eng.spagobi.studio.documentcomposition.util.XmlTemplateGenerator;
import it.eng.spagobi.studio.documentcomposition.views.DocumentParametersView;
import it.eng.spagobi.studio.documentcomposition.views.DocumentPropertiesView;
import it.eng.spagobi.studio.documentcomposition.views.NavigationView;
import it.eng.spagobi.studio.documentcomposition.views.VideoSizeView;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IViewPart;
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
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.internal.EditorReference;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;


public class DocumentCompositionEditor extends EditorPart {

	String templateName="";
	int height=0;
	int width=0;
	//DocumentComposition documentComposition;
	MetadataDocumentComposition metadataDocumentComposition;
	Designer designer;
	private XmlTemplateGenerator generator = new XmlTemplateGenerator();
	protected boolean isDirty = false;

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		super.dispose();

		IViewPart navigationView = DocCompUtilities.getViewReference(DocCompUtilities.NAVIGATION_VIEW_ID);
		if(navigationView != null && navigationView instanceof NavigationView){
			navigationView = (NavigationView)navigationView;
			getSite().getPage().hideView(navigationView);

		}
		IViewPart propertiesView = DocCompUtilities.getViewReference(DocCompUtilities.DOCUMENT_PROPERTIES_VIEW_ID);
		if(propertiesView != null && propertiesView instanceof DocumentPropertiesView){
			propertiesView = (DocumentPropertiesView)propertiesView;
			getSite().getPage().hideView(propertiesView);
		}
		IViewPart parametersView = DocCompUtilities.getViewReference(DocCompUtilities.DOCUMENT_PARAMETERS_VIEW_ID);
		if(parametersView != null && parametersView instanceof DocumentParametersView){
			parametersView = (DocumentParametersView)parametersView;
			getSite().getPage().hideView(parametersView);
		}
		IViewPart videoView = DocCompUtilities.getViewReference(DocCompUtilities.VIDEO_SIZE_VIEW_ID);
		if(videoView != null && videoView instanceof VideoSizeView){
			videoView = (VideoSizeView)videoView;
			getSite().getPage().hideView(videoView);
		}
	}
	@Override
	public void doSave(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		SpagoBILogger.infoLog("Start Saving Document Composition Template File");
		ByteArrayInputStream bais = null;

		// Before Saving recalculate all containers style because they could have changed with video size
		DocumentComposition documentComposition=(new ModelBO()).getModel();

		// reload styles
		documentComposition.reloadAllStylesContained();


		try {
			FileEditorInput fei = (FileEditorInput) getEditorInput();
			IFile file = fei.getFile();
			String newContent =  generator.transformToXml(documentComposition);
			System.out.println("******** SAVING ***************");
			System.out.println(newContent);
			byte[] bytes = newContent.getBytes();
			bais = new ByteArrayInputStream(bytes);
			file.setContents(bais, IFile.FORCE, null);

		} catch (CoreException e) {
			SpagoBILogger.errorLog("Error while Saving Chart Template File",e);
			e.printStackTrace();
		}	
		finally { 
			if (bais != null)
				try {
					bais.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		setIsDirty(false);

	}
	public void setIsDirty(boolean isDirty) {
		this.isDirty = isDirty;
		firePropertyChange(PROP_DIRTY);
	}

	public boolean isDirty() {
		return isDirty;
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
			DocumentComposition documentComposition = bo.createModel(file);
			bo.saveModel(documentComposition);

			metadataDocumentComposition = new MetadataDocumentComposition();
			Activator.getDefault().setMetadataDocumentComposition(metadataDocumentComposition);
		} catch (CoreException e) {
			e.printStackTrace();
			SpagoBILogger.errorLog(DocumentCompositionEditor.class.toString()+": Error in reading template", e);
			throw(new PartInitException("Error in reading template"));
		}
		SpagoBILogger.infoLog("END "+DocumentCompositionEditor.class.toString()+": init function");
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
				if (iworkbenchpage.findView(DocCompUtilities.DOCUMENT_PROPERTIES_VIEW_ID) == null ){
					iworkbenchpage.showView(DocCompUtilities.DOCUMENT_PROPERTIES_VIEW_ID);
				}
				if (iworkbenchpage.findView(DocCompUtilities.DOCUMENT_PARAMETERS_VIEW_ID) == null ){
					iworkbenchpage.showView(DocCompUtilities.DOCUMENT_PARAMETERS_VIEW_ID);
				}
				if (iworkbenchpage.findView(DocCompUtilities.NAVIGATION_VIEW_ID) == null ){
					iworkbenchpage.showView(DocCompUtilities.NAVIGATION_VIEW_ID);
				}
				if (iworkbenchpage.findView(DocCompUtilities.VIDEO_SIZE_VIEW_ID) == null ){
					iworkbenchpage.showView(DocCompUtilities.VIDEO_SIZE_VIEW_ID);
				}
				IViewPart object=DocCompUtilities.getViewReference(DocCompUtilities.VIDEO_SIZE_VIEW_ID);
				if(object!=null){
					VideoSizeView view=(VideoSizeView)object;
					view.reloadSizes();						
				}

			}
		} catch (PartInitException partinitexception) {
			partinitexception.printStackTrace();
		}


		FormToolkit toolkit = new FormToolkit(_parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(_parent);

		TableWrapLayout layout = new TableWrapLayout();
		layout.numColumns = 1;
		layout.horizontalSpacing = 20;
		layout.verticalSpacing = 10;
		layout.topMargin = 20;
		layout.leftMargin = 20;
		//		FillLayout layout = new FillLayout();

		form.getBody().setLayout(layout);

		final Section section = toolkit.createSection(form.getBody(), 
				Section.TITLE_BAR | SWT.NO_REDRAW_RESIZE);
		//section.setLayoutData()
		section.setSize(1000, 1000);
		section.addExpansionListener(new ExpansionAdapter() {
			public void expansionStateChanged(ExpansionEvent e) {
				//parent.setSize(width, height);
				form.reflow(true);
			}
		});
		section.setText("Document Composition designer: Screen");
		Composite sectionClient = toolkit.createComposite(section, SWT.NO_REDRAW_RESIZE);
		//sectionClient.setSize(1000, 1000);
		Composite composite=new Group(sectionClient,SWT.BORDER_DOT);
		//		composite.setText("Video");
		composite.setBackground(new Color(composite.getDisplay(), new RGB(240,240,240)));
		composite.setLocation(0, 0);
		composite.setSize(800, 600);

		designer=new Designer(composite, this);
		designer.setEditor(this);
		section.setClient(sectionClient);

		// Add the control resize, shell resize all the document Containers
		width=sectionClient.getBounds().width;
		height=sectionClient.getBounds().height;

		// Now initialize Editor with DocumentComposition retrieved
		initializeEditor(new ModelBO().getModel());

		SpagoBILogger.infoLog("END "+DocumentCompositionEditor.class.toString()+": create Part Control function");

	}


	public void initializeEditor(DocumentComposition documentComposition){
		SpagoBILogger.infoLog("START: "+DocumentCompositionEditor.class.toString()+" initialize Editor");	
		// clean the properties View
		IWorkbenchWindow a=PlatformUI.getWorkbench().getWorkbenchWindows()[0];
		// Document properties
		IWorkbenchPage aa=a.getActivePage();
		try{

			IViewPart object=DocCompUtilities.getViewReference(DocCompUtilities.DOCUMENT_PROPERTIES_VIEW_ID);
			if(object!=null){
				DocumentPropertiesView view=(DocumentPropertiesView)object;
				view.cleanSizeAndProperties();
			}
			else{
				SpagoBILogger.warningLog("view Document Propertiess closed");
			}
		}
		catch (Exception e) {
			SpagoBILogger.warningLog("Window not active, could not empty the property view");	
		}

		try{
			IViewPart object=DocCompUtilities.getViewReference(DocCompUtilities.DOCUMENT_PARAMETERS_VIEW_ID);
			if(object!=null){
				DocumentParametersView view=(DocumentParametersView)object;
				view.cleanParameters();
			}
			else{
				SpagoBILogger.warningLog("view Document Parameters closed");
			}

		}
		catch (Exception e) {
			SpagoBILogger.warningLog("Window not active, could not empty the property view");	
		}

		try{
			IViewPart object=DocCompUtilities.getViewReference(DocCompUtilities.NAVIGATION_VIEW_ID);
			if(object!=null){
				NavigationView view=(NavigationView)object;
				view.cleanParameters();
			}
			else{
				SpagoBILogger.warningLog("view Navigation not present");
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



	public Designer getDesigner() {
		return designer;
	}

	public void setDesigner(Designer designer) {
		this.designer = designer;
	}

	
	


}
