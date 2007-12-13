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
/*
 * Created on 4-mag-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.services;

import it.eng.spagobi.commons.services.PortletLoginAction;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;


public class ReadTicketServlet extends HttpServlet{
	

    static Logger logger = Logger.getLogger(ReadTicketServlet.class);	
	/**
	 * Init method definition
	 */
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
     } 
	

	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException{
	    logger.debug("IN");
	    String tiket="";
		try{
			HttpSession session=request.getSession();
			CASReceipt cr = (CASReceipt)session.getAttribute(CASFilter.CAS_FILTER_RECEIPT);
			logger.debug("CR:"+cr);
			tiket=ProxyTicketReceptor.getProxyTicket(cr.getPgtIou(), "https://localhost:8443/SpagoBI/proxyReceptor");
			logger.debug("tiket:"+tiket);
			response.getOutputStream().write(tiket.getBytes());
		        response.getOutputStream().flush();		        

		} catch (IOException e) {
		    e.printStackTrace();
		    throw e;
		}finally {
		    logger.debug("OUT");
		}
	}
		
	
}	

