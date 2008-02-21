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

import it.eng.spagobi.utilities.service.AbstractEngineStartServlet;

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

    /**
     * process jasper report execution requests
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	logger.debug("Start processing a new request...");
    	
    	setRequest(request);  
    	setResponse(response);
    	
    	String documentId = getDocumentId();
    	logger.debug("documentId:"+documentId);
    	
    	String userId = getUserId();
    	logger.debug("userId:"+userId);
    	
    	String auditId = getAuditId();
    	logger.debug("auditId:"+auditId);
    	
    	String jpaloUrl = "Application.html";
    	try {
			JPaloEngineTemplate template = new JPaloEngineTemplate( getRowTemplate() );
			jpaloUrl += "?server=" + template.getDatabaseName();
			jpaloUrl += "&schema=" + template.getSchemaName();
			jpaloUrl += "&cube=" + template.getCubeName();
			jpaloUrl += "&table-only=" + template.getTableOnly();
			jpaloUrl += "&editor-only=" + template.getEditorOnly();
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new  IOException(e);			
		}
    			
		//RequestDispatcher dispatcher = request.getRequestDispatcher( jpaloUrl );
		//dispatcher.forward(request, response);
		
		String urlWithSessionID = response.encodeRedirectURL( jpaloUrl);
	    response.sendRedirect( urlWithSessionID );

    }
}
