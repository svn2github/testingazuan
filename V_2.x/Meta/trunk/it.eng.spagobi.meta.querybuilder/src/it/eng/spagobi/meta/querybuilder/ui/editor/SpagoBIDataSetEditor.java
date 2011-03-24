package it.eng.spagobi.meta.querybuilder.ui.editor;


import it.eng.qbe.model.structure.ViewModelStructure;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.meta.querybuilder.ui.shared.edit.tree.ModelStructureBuilder;

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



/**
 * @author cortella
 *	This class create an Editor for the QueryBuilder
 *  - First Page is for editing the query
 *  - Second Page is for showing query results
 */
public class SpagoBIDataSetEditor extends MultiPageEditorPart implements IResourceChangeListener{

	private QueryBuilder queryBuilderUI;
	private SpagoBIDataSetEditPage queryEditPage;
	private SpagoBIDataSetResultPage queryResultPage;
	private ViewModelStructure datamartStructure;

	/**
	 * Creates a multi-page editor for the Query Builder. This version use
	 * the passed CreateQueryBuilderUI object.
	 */
	public SpagoBIDataSetEditor(QueryBuilder queryBuilderUI) {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		if (queryBuilderUI == null){
			datamartStructure = ModelStructureBuilder.build();
			queryBuilderUI = new QueryBuilder(datamartStructure);
		}
		else{
			this.queryBuilderUI = queryBuilderUI;
		}		
	}
	
	/**
	 * Creates a multi-page editor for the Query Builder.
	 */
	public SpagoBIDataSetEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		datamartStructure = ModelStructureBuilder.build();
		queryBuilderUI = new QueryBuilder(datamartStructure);
	}
	/**
	 * Creates Edit Page of the multi-page editor,
	 * which contains UI for editing the query.
	 */
	void createEditPage() {
		/*
		Composite container = queryBuilderUI.createEditComponent(getContainer());
		int index = addPage(container);
		*/
		queryEditPage = new SpagoBIDataSetEditPage(getContainer(), queryBuilderUI);
		int index = addPage(queryEditPage);
		setPageText(index, "Edit");
	}
	
	/**
	 * Creates the Query Result Page
	 */
	void createResultsPage() {
//		Composite container = queryBuilderUI.createResultsComponent(getContainer());
//		int index = addPage(container);
		
		queryResultPage = new SpagoBIDataSetResultPage(getContainer(), queryBuilderUI);
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
	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method
	 * checks that the input is an instance of <code>IFileEditorInput</code>.
	 */
	public void init(IEditorSite site, IEditorInput editorInput)
		throws PartInitException {
		//if (!(editorInput instanceof IFileEditorInput))
		//	throw new PartInitException("Invalid Input: Must be IFileEditorInput");
		super.init(site, editorInput);
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
