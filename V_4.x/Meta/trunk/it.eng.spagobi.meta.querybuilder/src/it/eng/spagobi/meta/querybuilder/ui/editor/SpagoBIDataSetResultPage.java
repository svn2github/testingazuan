/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.editor;

import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.meta.querybuilder.ui.shared.result.ResultTableViewer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIDataSetResultPage extends Composite {
	
	ResultTableViewer tableViewer;
	
	public SpagoBIDataSetResultPage(Composite container, QueryBuilder builder) {
		super(container, SWT.NONE);
		
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite mainContainer = new Composite(this, SWT.NONE);
		FillLayout fl_composite = new FillLayout(SWT.HORIZONTAL);
		fl_composite.marginWidth = 2;
		fl_composite.marginHeight = 2;
		mainContainer.setLayout(fl_composite);
		
		Group groupQueryResult = new Group(mainContainer, SWT.NONE);
		groupQueryResult.setText("Query Result");
		GridLayout gl_groupQueryResult = new GridLayout(1, false);
		gl_groupQueryResult.marginRight = 1;
		gl_groupQueryResult.marginTop = 1;
		gl_groupQueryResult.marginLeft = 1;
		gl_groupQueryResult.marginBottom = 1;
		groupQueryResult.setLayout(gl_groupQueryResult);
		
		tableViewer = builder.createResultsTableViewer(groupQueryResult);

	}

	public void refresh() {
		tableViewer.updateTable();
	}

}
