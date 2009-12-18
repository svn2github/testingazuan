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
package it.eng.spagobi.engines.qbe.services;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import it.eng.qbe.model.HQLStatement;
import it.eng.qbe.model.IStatement;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONSuccess;

/**
 * This action is responsible validate current query.
 * It actually executes the query .
 */
public class ValidateQueryAction extends AbstractQbeEngineAction {
	
	// INPUT PARAMETERS
	public static final String QUERY = "query";
	
	public static transient Logger logger = Logger.getLogger(ValidateQueryAction.class);
	
	public void service(SourceBean request, SourceBean response) {
		
		logger.debug("IN");
		
		try {
			super.service(request, response);
			
			boolean validationResult = false;
			IStatement statement = getEngineInstance().getStatment();	
			statement.setParameters( getEnv() );
			String hqlQuery = statement.getQueryString();
			String sqlQuery = ((HQLStatement)statement).getSqlQueryString();
			logger.debug("Validating query (HQL): [" +  hqlQuery+ "]");
			logger.debug("Validating query (SQL): [" + sqlQuery + "]");
			try {
				statement.execute(0, 1, 1, true);
				logger.info("Query execution did not throw any exception. Validation successful.");
				validationResult = true;
			} catch (Throwable t) {
				logger.info("Query execution thrown an exception. Validation failed.");
				logger.debug(t);
				validationResult = false;
			}
			JSONObject result = new JSONObject();
			result.put("validationResult", validationResult);
			writeBackToClient( new JSONSuccess(result) );
		} catch(Throwable t) {
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), t);
		} finally {
			logger.debug("OUT");
		}
		
	}
}
