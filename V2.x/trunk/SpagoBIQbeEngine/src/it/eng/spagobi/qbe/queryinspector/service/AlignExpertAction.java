/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.qbe.queryinspector.service;

import it.eng.qbe.export.HqlToSqlQueryRewriter;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import org.hibernate.SessionFactory;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to put the contents of the query composed automatically
 * in the expert query contents
 */
public class AlignExpertAction extends AbstractQbeEngineAction {
	
		
	
	public void service(SourceBean request, SourceBean response) {
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
