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
package it.eng.spagobi.meta.editor.olap.editor.hierarchies;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.olap.OlapModel;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class HierarchiesEditorMainPage extends Dialog {
	private Table table;
	
	OlapModel olapModel;
	BusinessColumnSet businessColumnSet;
	OlapModelInitializer olapModelInitializer;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public HierarchiesEditorMainPage(Shell parentShell, OlapModel olapModel, BusinessColumnSet businessColumnSet) {
		super(parentShell);
		setShellStyle(SWT.DIALOG_TRIM | SWT.RESIZE | SWT.PRIMARY_MODAL);
		this.olapModel = olapModel;
		this.businessColumnSet = businessColumnSet;
		olapModelInitializer = new OlapModelInitializer();
		
	}
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("Hierarchies Editor");
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		
		Composite composite = new Composite(container, SWT.NONE);
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblCreateOrEdit = new Label(composite, SWT.NONE);
		lblCreateOrEdit.setText("Create or Edit Hierarchies for this Dimension");
		
		Group grpHierarchies = new Group(composite, SWT.NONE);
		grpHierarchies.setText("Hierarchies");
		grpHierarchies.setLayout(new GridLayout(1, false));
		grpHierarchies.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Composite compositeButtons = new Composite(grpHierarchies, SWT.NONE);
		compositeButtons.setLayout(new GridLayout(1, false));
		compositeButtons.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Button btnAddHierarchy = new Button(compositeButtons, SWT.NONE);
		btnAddHierarchy.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				HierarchyEditor hierarchyEditor = new HierarchyEditor(getShell(), olapModel, businessColumnSet, olapModelInitializer );
				hierarchyEditor.create();
				hierarchyEditor.open();
			}
		});
		btnAddHierarchy.setText("Add Hierarchy");
		
		Composite compositeTableHierarchies = new Composite(grpHierarchies, SWT.NONE);
		compositeTableHierarchies.setLayout(new GridLayout(1, false));
		compositeTableHierarchies.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		compositeTableHierarchies.setBounds(0, 0, 64, 64);
		
		Group grpCurrentHierarchies = new Group(compositeTableHierarchies, SWT.NONE);
		grpCurrentHierarchies.setText("Current Hierarchies");
		grpCurrentHierarchies.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		grpCurrentHierarchies.setLayout(new GridLayout(1, false));
		
		table = new Table(grpCurrentHierarchies, SWT.BORDER | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		TableColumn tblclmnName = new TableColumn(table, SWT.NONE);
		tblclmnName.setWidth(100);
		tblclmnName.setText("Name");
		
		TableColumn tblclmnPrimaryKey = new TableColumn(table, SWT.NONE);
		tblclmnPrimaryKey.setWidth(100);
		tblclmnPrimaryKey.setText("Primary Key");
		
		TableColumn tblclmnEdit = new TableColumn(table, SWT.NONE);
		tblclmnEdit.setWidth(100);
		tblclmnEdit.setText("Edit");
		
		TableColumn tblclmnRemove = new TableColumn(table, SWT.NONE);
		tblclmnRemove.setWidth(100);
		tblclmnRemove.setText("Remove");

		return container;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(608, 497);
	}
}
