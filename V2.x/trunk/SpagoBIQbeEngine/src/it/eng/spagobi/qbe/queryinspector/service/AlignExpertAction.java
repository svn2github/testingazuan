/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.queryinspector.service;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.utilities.engines.EngineException;

import org.hibernate.SessionFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class AlignExpertAction.
 * 
 * @author Andrea Zoppello
 * 
 * This action is responsible to put the contents of the query composed automatically
 * in the expert query contents
 */
public class AlignExpertAction extends AbstractQbeEngineAction {
	
		
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException {
		super.service(request, response);
		
		/**
		 * BRUTTO :-(( 
		 * TODO Sistemare i riferimenti incrociati tra DatamartModel e DatamartWizard
		 */
	    getDatamartWizard().composeQuery( getDatamartModel() );
		SessionFactory sf = getDatamartModel().getDataSource().getSessionFactory();
		HqlToSqlQueryRewriter queryRewriter = new HqlToSqlQueryRewriter(sf.openSession());
		String sqlQuery = queryRewriter.rewrite( getDatamartWizard().getFinalQuery() );
		getDatamartWizard().setExpertQueryDisplayed(sqlQuery);
		
	}
}
