package it.eng.spagobi.meta.querybuilder.ui.editor;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Locale;

import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.json.JSONObject;
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
		
		try {
			super.init(site, editorInput);
		
			logger.debug("editorInput parameter type is equal to [{}]", editorInput.getClass());
			if (editorInput instanceof FileEditorInput) {
				FileEditorInput fileEditorInput = (FileEditorInput)editorInput;
				InputStream inputStram = fileEditorInput.getFile().getContents();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStram));
				String line = null;
				StringBuffer stringBuffer  = new StringBuffer();
				while((line = reader.readLine())!= null) {
					stringBuffer.append(line);
				}
				reader.close();
				
				String queryString = stringBuffer.toString();
				if(queryString.trim().equals("")) {
					logger.debug("No previously saved query to load");
				} else {
					queryBuilder.setQuery( SerializerFactory.getDeserializer("application/json").deserializeQuery(queryString, queryBuilder.getDataSource()) );
				}
			} else {
				throw new PartInitException("Editor class [" + this.getClass().getName() + "] is unable to manage input of type [" + editorInput.getClass().getName() + "]");
			} 
		} catch(Throwable t) {
			logger.error("Impossible to initialize editor [" + this.getClass().getName()+ "]", t);
			throw new PartInitException("Impossible to initialize editor [" + this.getClass().getName()+ "]: " + t.getCause().getMessage());
		} finally {
			logger.trace("OUT");
		}
	}
	

	protected void createPages() {
		createEditPage();
		createResultsPage();

	}
	
	void createEditPage() {
		queryEditPage = new SpagoBIDataSetEditPage(getContainer(), queryBuilder);
		int index = addPage(queryEditPage);
		setPageText(index, "Edit");
	}
	
	void createResultsPage() {

		queryResultPage = new SpagoBIDataSetResultPage(getContainer(), queryBuilder);
		int index = addPage(queryResultPage);
		setPageText(index, "Results");
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
	
	public boolean isDirty() {
		return true;
	}
	
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		Query query;
		JSONObject queryJSON;
		FileEditorInput fileEditorInput;
		
		logger.trace("IN");
		
		query = queryBuilder.getQuery();
		
		fileEditorInput = null;
		if (getEditorInput() instanceof FileEditorInput) {
			fileEditorInput = (FileEditorInput)getEditorInput();
			query.setId(fileEditorInput.getURI().toString());
			query.setDescription(fileEditorInput.getURI().toString());
			query.setDistinctClauseEnabled(false);
		} else {
			throw new RuntimeException("Editor class [" + this.getClass().getName() + "] is unable to manage input of type [" + getEditorInput().getClass().getName() + "]");
		}
		
		queryJSON = null;
		try {
			queryJSON = (JSONObject)SerializerFactory.getSerializer("application/json").serialize(query, queryBuilder.getDataSource(), Locale.ENGLISH);
			logger.debug(queryJSON.toString());	
		} catch (SerializationException e) {
			throw new RuntimeException("Impossible to save query", e);
		}
		
		
		File file = new File(fileEditorInput.getURI());
		BufferedWriter out = null;
		ByteArrayInputStream in = null;
		try {
//			FileWriter fstream = new FileWriter(file);
//			out = new BufferedWriter(fstream);
//			out.write(queryJSON.toString(3));
			in = new ByteArrayInputStream(queryJSON.toString(3).getBytes(fileEditorInput.getFile().getCharset()));
			fileEditorInput.getFile().setContents(in, true, true, monitor);
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		logger.trace("OUT");
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		logger.trace("IN");
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
		logger.trace("OUT");
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
	 * Calculates the contents of Result page when the it is activated.
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
