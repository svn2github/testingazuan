/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.jpa;

import it.eng.qbe.statement.AbstractStatementClause;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbstractJPQLStatementClause extends AbstractStatementClause {
	
	
	
	private static final SimpleDateFormat TIMESTAMP_FORMATTER = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss" );
	public static transient Logger logger = Logger.getLogger(JPQLStatementSelectClause.class);
	


	
}
