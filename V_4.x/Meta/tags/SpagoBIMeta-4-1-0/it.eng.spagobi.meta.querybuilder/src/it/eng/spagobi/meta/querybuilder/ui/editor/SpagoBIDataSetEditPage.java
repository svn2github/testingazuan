/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.editor;

import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBIDataSetEditPage extends SashForm {

	private static Logger logger = LoggerFactory.getLogger(SpagoBIDataSetEditPage.class);

	public SpagoBIDataSetEditPage(Composite container, QueryBuilder builder) {
		super(container, SWT.HORIZONTAL);
		
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
	
		//Create Business Model Tree 
		logger.debug("Business Model Tree creation");
		builder.createEditBusinessModelTree(this);
		
		//Create Query Filters
		logger.debug("Business Model Query Tables (Select,Where,Having) creation");
		builder.createEditGroup(this);
		
		//Set SashForm Properties
		this.setWeights(new int[] { 2, 8});
		this.SASH_WIDTH = 5;
	}
	
	


}
