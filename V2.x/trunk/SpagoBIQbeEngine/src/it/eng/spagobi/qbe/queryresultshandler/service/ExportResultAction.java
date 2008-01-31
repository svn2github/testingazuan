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
package it.eng.spagobi.qbe.queryresultshandler.service;

import it.eng.qbe.log.Logger;
import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;

import org.hibernate.HibernateException;


/**
 * @author Andrea Gioia
 */
public class ExportResultAction extends AbstractQbeEngineAction {
	
	public static String QUERY_RESPONSE_SOURCE_BEAN = "QUERY_RESPONSE_SOURCE_BEAN"; 
		
	public void service(SourceBean request, SourceBean response) {

		
		String query = null;
		getDatamartWizard().composeQuery( getDatamartModel() );
		if (getDatamartWizard().isUseExpertedVersion()){
			query = getDatamartWizard().getExpertQueryDisplayed();
		}else{
			try{
				query =  getDatamartWizard().getFinalSqlQuery( getDatamartModel() );
			}catch(Throwable t){
				t.printStackTrace();
			}
		} 
		
		if(query == null) {
			
			returnError(response, "Query is null !!!");
		
		}else{
		
			try {
		
				SourceBean queryResponseSourceBean = getDatamartWizard().executeSqlQuery(getDatamartModel(), query, 0, 10);
				setAttributeInSession (QUERY_RESPONSE_SOURCE_BEAN, queryResponseSourceBean);
			
			}catch (HibernateException he) {
			
				Logger.error(ExecuteQueryAction.class, he);
				returnError(response, he.getCause().getMessage());
			
			}catch (java.sql.SQLException se) {
			
				Logger.error(ExecuteQueryAction.class, se);
				returnError(response, se.getMessage());
			
			}catch(Exception e){
			
				Logger.error(ExecuteQueryAction.class, e);
				returnError(response, e.getMessage());					
			
			}
		}				
	}	
	
	private void returnError(SourceBean response, String errorMsg) {
		delAttributeFromSession(QUERY_RESPONSE_SOURCE_BEAN);		
		setAttribute("ERROR_MSG", errorMsg);
	}
}
