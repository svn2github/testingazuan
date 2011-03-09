package it.eng.spagobi.meta.querybuilder.ui;


import java.io.StringWriter;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.*;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.ide.IDE;

import com.swtdesigner.SWTResourceManager;

/**
 * @author cortella
 *	This class create an Editor for the QueryBuilder
 *  - First Page is for editing the query
 *  - Second Page is for showing query results
 */
public class CreateQueryBuilderEditor extends MultiPageEditorPart implements IResourceChangeListener{

	/** The font chosen in page 1. */
	private Font font;
	private TextEditor editor;

	/**
	 * Creates a multi-page editor for the Query Builder.
	 */
	public CreateQueryBuilderEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}
	/**
	 * Creates Edit Page of the multi-page editor,
	 * which contains UI for editing the query.
	 */
	void createEditPage() {
	/*	
		try {
			editor = new TextEditor();
			int index = addPage(editor, getEditorInput());
			setPageText(index, editor.getTitle());
		} catch (PartInitException e) {
			ErrorDialog.openError(
				getSite().getShell(),
				"Error creating nested text editor",
				null,
				e.getStatus());
		}
	*/
		Composite container = new Composite(getContainer(), SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		
		Composite compositeTree = new Composite(composite, SWT.NONE);
		GridData gd_compositeTree = new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1);
		gd_compositeTree.widthHint = 180;
		compositeTree.setLayoutData(gd_compositeTree);
		compositeTree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Group groupBusinessModelTree = new Group(compositeTree, SWT.NONE);
		groupBusinessModelTree.setText("Business Model");
		groupBusinessModelTree.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		
		//*******************************************
		// TODO: Business Model Tree Viewer Here!
		//*******************************************
		
		TreeViewer businessModelTreeViewer = new TreeViewer(groupBusinessModelTree, SWT.BORDER);
				
		//only for test, use a fake model and content provider
		businessModelTreeViewer.setLabelProvider(new LabelProvider());
		businessModelTreeViewer.setContentProvider(new MyContentProvider());
		businessModelTreeViewer.setInput(createModel());

		
		Composite compositeFilters = new Composite(composite, SWT.NONE);
		compositeFilters.setLayout(new FillLayout(SWT.HORIZONTAL));
		compositeFilters.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Group grpQueryEditor = new Group(compositeFilters, SWT.NONE);
		grpQueryEditor.setText("Query Editor");
		grpQueryEditor.setLayout(new GridLayout(1, false));
		
		Label lblSelectFields = new Label(grpQueryEditor, SWT.NONE);
		lblSelectFields.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblSelectFields.setText("Select Fields");
		
		ScrolledComposite scrolledComposite = new ScrolledComposite(grpQueryEditor, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite.setBackground(SWTResourceManager.getColor(255, 255, 255));
		scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		
		Label lblWhereClauses = new Label(grpQueryEditor, SWT.NONE);
		lblWhereClauses.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblWhereClauses.setText("Where Clause");
		
		ScrolledComposite scrolledComposite_1 = new ScrolledComposite(grpQueryEditor, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_1.setBackground(SWTResourceManager.getColor(255, 255, 255));
		scrolledComposite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite_1.setExpandHorizontal(true);
		scrolledComposite_1.setExpandVertical(true);
		
		Label lblHavingClause = new Label(grpQueryEditor, SWT.NONE);
		lblHavingClause.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		lblHavingClause.setText("Having Clause");
		
		ScrolledComposite scrolledComposite_2 = new ScrolledComposite(grpQueryEditor, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		scrolledComposite_2.setBackground(SWTResourceManager.getColor(255, 255, 255));
		scrolledComposite_2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		scrolledComposite_2.setExpandHorizontal(true);
		scrolledComposite_2.setExpandVertical(true);

		
		int index = addPage(container);
		setPageText(index, "Edit");
	}
	
	/**
	 * Creates page 1 of the multi-page editor,
	 * which allows you to change the font used in page 2.
	 */
	void createResultsPage() {

		Composite composite = new Composite(getContainer(), SWT.NONE);
		GridLayout layout = new GridLayout();
		composite.setLayout(layout);
		layout.numColumns = 2;
		/*
		Button fontButton = new Button(composite, SWT.NONE);
		GridData gd = new GridData(GridData.BEGINNING);
		gd.horizontalSpan = 2;
		fontButton.setLayoutData(gd);
		fontButton.setText("Change Font...");
		
		fontButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				
			}
		});
		*/

		int index = addPage(composite);
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
	 * Calculates the contents of page 2 when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 2) {
			sortWords();
		}
	}
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
			Display.getDefault().asyncExec(new Runnable(){
				public void run(){
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i<pages.length; i++){
						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
							pages[i].closeEditor(editorPart,true);
						}
					}
				}            
			});
		}
	}

	/**
	 * Sorts the words in page 0, and shows them in page 2.
	 */
	void sortWords() {

		String editorText =
			editor.getDocumentProvider().getDocument(editor.getEditorInput()).get();

		StringTokenizer tokenizer =
			new StringTokenizer(editorText, " \t\n\r\f!@#\u0024%^&*()-_=+`~[]{};:'\",.<>/?|\\");
		ArrayList editorWords = new ArrayList();
		while (tokenizer.hasMoreTokens()) {
			editorWords.add(tokenizer.nextToken());
		}

		Collections.sort(editorWords, Collator.getInstance());
		StringWriter displayText = new StringWriter();
		for (int i = 0; i < editorWords.size(); i++) {
			displayText.write(((String) editorWords.get(i)));
			displayText.write(System.getProperty("line.separator"));
		}

	}

	//***********************************************
	// Private Class, only for test. Will be deleted.
	//***********************************************
	private class MyContentProvider implements ITreeContentProvider {

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
		 */
		public Object[] getElements(Object inputElement) {
			return ((MyModel)inputElement).child.toArray();
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
		 */
		public void dispose() {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
		 */
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
		 */
		public Object[] getChildren(Object parentElement) {
			return getElements(parentElement);
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
		 */
		public Object getParent(Object element) {
			if( element == null) {
				return null;
			}

			return ((MyModel)element).parent;
		}

		/* (non-Javadoc)
		 * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
		 */
		public boolean hasChildren(Object element) {
			return ((MyModel)element).child.size() > 0;
		}

	}

	public class MyModel {
		public MyModel parent;
		public ArrayList child = new ArrayList();
		public int counter;
		
		public MyModel(int counter, MyModel parent) {
			this.parent = parent;
			this.counter = counter;
		}
		
		public String toString() {
			String rv = "Item ";
			if( parent != null ) {
				rv = parent.toString() + ".";
			}
			
			rv += counter;
			
			return rv;
		}
	}
	//****** end private classes
	
	private MyModel createModel() {
		
		MyModel root = new MyModel(0,null);
		root.counter = 0;
		
		MyModel tmp;
		for( int i = 1; i < 10; i++ ) {
			tmp = new MyModel(i, root);
			root.child.add(tmp);
			for( int j = 1; j < i; j++ ) {
				tmp.child.add(new MyModel(j,tmp));
			}
		}
		
		return root;
	}

	
}
