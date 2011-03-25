package it.eng.spagobi.meta.querybuilder.ui.editor;


import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.qbe.query.Query;
import it.eng.spagobi.meta.querybuilder.ResourceRegistry;
import it.eng.spagobi.meta.querybuilder.ui.ModelStructureBuilder;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * @author cortella
 *	This class create an Editor for the QueryBuilder
 *  - First Page is for editing the query
 *  - Second Page is for showing query results
 */
public class SpagoBIDataSetEditor extends MultiPageEditorPart implements IResourceChangeListener{

	private QueryBuilder queryBuilder;
	private SpagoBIDataSetEditPage queryEditPage;
	private SpagoBIDataSetResultPage queryResultPage;
	
	private static Logger logger = LoggerFactory.getLogger(SpagoBIDataSetEditor.class);
	

	public SpagoBIDataSetEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		queryBuilder = new QueryBuilder();
	}
	
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		logger.trace("IN");
		super.init(site, editorInput);
	
		logger.debug("editorInput parameter type is equal to [{}]", editorInput.getClass());
		if (!(editorInput instanceof FileEditorInput)) throw new PartInitException("Invalid Input: Must be FileEditorInput");
		
		logger.trace("OUT");
	}
	
	/**
	 * Creates Edit Page of the multi-page editor,
	 * which contains UI for editing the query.
	 */
	void createEditPage() {
		queryEditPage = new SpagoBIDataSetEditPage(getContainer(), queryBuilder);
		int index = addPage(queryEditPage);
		setPageText(index, "Edit");
	}
	
	/**
	 * Creates the Query Result Page
	 */
	void createResultsPage() {

		queryResultPage = new SpagoBIDataSetResultPage(getContainer(), queryBuilder);
		int index = addPage(queryResultPage);
		setPageText(index, "Results");
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	protected void createPages() {
		createEditPage();
		createResultsPage();

	}
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		getEditor(0).doSave(monitor);
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of Result pagewhen the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			queryResultPage.refresh();
		}
	}
	
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
//		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
//			Display.getDefault().asyncExec(new Runnable(){
//				public void run(){
//					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
//					for (int i = 0; i<pages.length; i++){
//						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
//							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
//							pages[i].closeEditor(editorPart,true);
//						}
//					}
//				}            
//			});
//		}
	}





	
}
