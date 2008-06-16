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
package it.eng.spagobi.qbe.core.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.AbstractQbeEngineAction;
import it.eng.spagobi.qbe.commons.service.JSONAcknowledge;
import it.eng.spagobi.qbe.commons.service.JSONFailure;
import it.eng.spagobi.utilities.engines.EngineException;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class DanielaAction extends AbstractQbeEngineAction {
	/** The Constant LIMIT. */
	public static final String LIMIT = "limit";
	
	/** The Constant START. */
	public static final String START = "start";
	
	public static String[] rows = new String[] {
		" { 'id': 1, 'name': 'Pisolo', 'occupation': 'Minatore' } ",
        " { 'id': 2, 'name': 'Brontolo', 'occupation': 'Minatore' } ",
        " { 'id': 3, 'name': 'Mammolo', 'occupation': 'Minatore' } ",
        " { 'id': 4, 'name': 'Dotto', 'occupation': 'Minatore' } ",
        " { 'id': 5, 'name': 'Eolo', 'occupation': 'Minatore' } ",
        
		" { 'id': 11, 'name': 'Gongolo', 'occupation': 'Minatore' } ",
        " { 'id': 12, 'name': 'Cucciolo', 'occupation': 'Minatore' } ",
        " { 'id': 13, 'name': 'Atos', 'occupation': 'Moschettiere' } ",
        " { 'id': 14, 'name': 'Portos', 'occupation': 'Moschettiere' } ",
        " { 'id': 15, 'name': 'Aramis', 'occupation': 'Moschettiere' } ",
        
		" { 'id': 11, 'name': 'Bill', 'occupation': 'Gardener' } ",
        " { 'id': 12, 'name': 'Ben', 'occupation': 'Horticulturalist' } ",
        " { 'id': 13, 'name': 'ciccio', 'occupation': 'Horticulturalist' } ",
        " { 'id': 14, 'name': 'pelliccio', 'occupation': 'Horticulturalist' } ",
        " { 'id': 15, 'name': 'pluto', 'occupation': 'Horticulturalist' } "		
	};
	
	
	/** Logger component. */
    public static transient Logger logger = Logger.getLogger(DanielaAction.class);
	
	public void service(SourceBean request, SourceBean response) throws EngineException  {			
		String jsonData = null;
		Integer limit = null;
		Integer start = null;
		
		logger.debug("IN");
		
		try {
			
			super.service(request, response);		
		
			limit = getAttributeAsInteger( LIMIT );
			start = getAttributeAsInteger( START );	
			
			logger.debug("limit: [" + limit + "]");
			logger.debug("start: [" + start + "]");
			
			
			
			if(start >= rows.length) {
				throw new QbeEngineException("Impossible to start scanning resultset from record " + start +". Query resultset contains only " + rows.length + " records.");
			}
			
			if((start + limit) >= rows.length) {
				limit = rows.length - start; 
			}
			
			jsonData = "";
            jsonData += "{" +
            				"'metaData': {	" +
            					"totalProperty: 'results'," +
            					"root: 'rows'," +
            					"id: 'id'," + 
            					"fields: [" +
            						"{name: 'id', header: 'id', width: 30, sortable: false, dataIndex: 'id'}, " + 
            						"{name: 'name', header: 'Name Test', width: 30, sortable: false, dataIndex: 'name'}," + 
            						"{name: 'occupation', header: 'Occupation test', width: 150, sortable: true, dataIndex: 'occupation'}" +
            					"]" + 
            				"}," +
            				"'results': 15, " +
            				"'rows': [";
            
            for(int i = start; i < start+limit; i++) {
            	jsonData += rows[i] + ",";
            }
            jsonData = jsonData.substring(0, jsonData.length() -1);
            jsonData += "]}"; 
            
            logger.debug(jsonData);
            
			try {
				freezeHttpResponse();
				HttpServletResponse httpResp = getHttpResponse();
				httpResp.getOutputStream().print( jsonData );
				httpResp.getOutputStream().flush();
			} catch (IOException e) {
				throw new EngineException("Impossible to write back the responce to the client", e);
			}
		} catch (Exception e) {
			if(e instanceof QbeEngineException) throw (QbeEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			QbeEngineException engineException = new QbeEngineException("Service error", description, hints, e);
			try {
				writeBackToClient( new JSONFailure( engineException ) );
			} catch (IOException ioe) {
				throw new EngineException("Impossible to write back the responce to the client", e);
			}
			throw engineException;
		} finally {
			logger.debug("OUT");
		}
	}
}
