/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.querybuilder.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.custom.ScrolledComposite;
import com.swtdesigner.SWTResourceManager;

/**
 * @author cortella
 * 
 */
public class TempClass extends EditorPart {

	public static final String ID = "it.eng.spagobi.meta.querybuilder.ui.TempClass"; //$NON-NLS-1$

	public TempClass() {
	}

	/**
	 * Create contents of the editor part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
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
		
		TreeViewer treeViewer = new TreeViewer(groupBusinessModelTree, SWT.BORDER);
		Tree businessModelTreeViewer = treeViewer.getTree();
		
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

	}

	@Override
	public void setFocus() {
		// Set the focus
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		// Do the Save operation
	}

	@Override
	public void doSaveAs() {
		// Do the Save As operation
	}

	@Override
	public void init(IEditorSite site, IEditorInput input)
			throws PartInitException {
		// Initialize the editor part
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}
}
