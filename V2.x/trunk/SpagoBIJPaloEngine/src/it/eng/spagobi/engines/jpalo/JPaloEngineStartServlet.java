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
package it.eng.spagobi.engines.jpalo;


import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JPaloEngineStartServlet extends AbstractEngineStartServlet {
	
	private static final String PALO_BASE_URL = "Application.html";
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(JPaloEngineStartServlet.class);

    /**
     * Initialize the engine
     */
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		logger.debug("Initializing SpagoBI JPalo Engine...");
    }

   
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	JPaloEngineTemplate template;
    	String jpaloUrl;
    	
    	logger.debug("IN");
		
		try {		
			
			super.service(request, response);
			
			auditServiceStartEvent();
		
		
			logger.debug("User Id: " + getUserId());
			logger.debug("Audit Id: " + getAuditId());
			logger.debug("Document Id: " + getDocumentId());
			logger.debug("Template: " + getTemplate());
		
			template = new JPaloEngineTemplate( getTemplate() );			
    	
	    	jpaloUrl = PALO_BASE_URL;	    	
			jpaloUrl += "?server=" + template.getDatabaseName();
			jpaloUrl += "&schema=" + template.getSchemaName();
			jpaloUrl += "&cube=" + template.getCubeName();
			jpaloUrl += "&table-only=" + template.getTableOnly();
			jpaloUrl += "&editor-only=" + template.getEditorOnly();
	    			
			
			String urlWithSessionID = response.encodeRedirectURL( jpaloUrl );
		    response.sendRedirect( urlWithSessionID );
	    
		} catch(Throwable t) {
			logger.error(t.getMessage());
			auditServiceErrorEvent(t.getMessage());			
			
			response.getOutputStream().write("An unpredicted error occured while executing palo-engine. Please chek the log for more informations on the causes".getBytes());
		} finally {
			this.auditServiceEndEvent();
			logger.debug("OUT");
		}

    }
}
