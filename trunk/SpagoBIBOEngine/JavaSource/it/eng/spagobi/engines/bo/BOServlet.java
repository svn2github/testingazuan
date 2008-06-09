/**
Copyright (c) 2005, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE
*/
package it.eng.spagobi.engines.bo;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.bo.rebean.wi.ReportEngine;
import com.bo.wibean.WIServer;
import com.bo.wibean.WIServerImpl;
import com.bo.wibean.WISession;


public class BOServlet extends HttpServlet{
	
	private transient Logger logger = Logger.getLogger(BOServlet.class);
	private transient WIServer wiServer = null; 
	private transient String user = "";
	private transient String password = "";
	
	
	
	
	public void init(ServletConfig config) throws ServletException {
        super.init(config);
        String iniuser = config.getInitParameter("BOUSER");
        if(iniuser==null || iniuser.trim().equals("")) {
        	logger.error("Engines"+ this.getClass().getName()+ 
        			     "init() cannot find BOUSER initial parameter, the engine cannot work");
        } else {
        	user = iniuser;
        }
        String inipassword = config.getInitParameter("BOPASSWORD");
        if(inipassword==null || inipassword.trim().equals("")) {
        	logger.error("Engines"+ this.getClass().getName()+ 
        			     "init() cannot find BOPASSWORD initial parameter, the engine cannot work");
        } else { 
        	password = inipassword;
        }
        try {
        	wiServer = new WIServerImpl();
        } catch(Exception e) {
        	wiServer = null;
        	logger.error("Engines"+ this.getClass().getName()+ 
		     			 "init() cannot instane the WIServer bean", e);
        }
        // get servlet context
        ServletContext context = getServletContext();
        // put into context the WebiServer
        context.setAttribute(BOConstants.WEBISERVER, wiServer);
	} 
    
	
	
	
	
	public void service(HttpServletRequest request,
	        			HttpServletResponse response)
	        			throws IOException, ServletException{
		logger.debug("IN");
		try {
			WISession wiSession = null;
			HttpSession httpsession = request.getSession();
			wiSession = (WISession)httpsession.getAttribute(BOConstants.BOSESSION);
			if(wiSession==null) {
				wiSession = getWISession(user, password, response);
				httpsession.setAttribute(BOConstants.BOSESSION, wiSession);
			}
			
			// create and init  reportEngine
			ReportEngine cdzReportEngine = Utils.createReportEngine(wiSession);
			// put the report engine into session
			httpsession.setAttribute(BOConstants.REPORTENGINE, cdzReportEngine);
			
			ServletContext servletContext = getServletContext();
			
			String operation = (String)request.getParameter(BOConstants.OPERATION);
			if(operation==null) {   
				ViewDocumentHandler viewDocHandler = new ViewDocumentHandler();
				viewDocHandler.handle(request, response, servletContext);		
			} else if(operation.equalsIgnoreCase(BOConstants.OPERATION_CHANGEREPORT)){
				ChangeReportHandler changeReportHand = new ChangeReportHandler();
				changeReportHand.handle(request, response, servletContext);
			} else if(operation.equalsIgnoreCase(BOConstants.OPERATION_CHANGEPAGE)){
				ChangeReportPageHandler changeReportPageHandler = new ChangeReportPageHandler();
				changeReportPageHandler.handle(request, response, servletContext);
			} else if(operation.equalsIgnoreCase(BOConstants.OPERATION_CHANGEMODE)) {
				ChangeReportModeHandler changeReportModeHandler = new ChangeReportModeHandler();
				changeReportModeHandler.handle(request, response, servletContext);
			} else if(operation.equalsIgnoreCase(BOConstants.OPERATION_CHANGEPAGEMODE)) {
				ChangePageModeHandler changePageModeHandler = new ChangePageModeHandler();
				changePageModeHandler.handle(request, response, servletContext);
			} else if(operation.equalsIgnoreCase(BOConstants.OPERATION_DRILL)) {
				ReportDrillHandler reportDrillHandler = new ReportDrillHandler();
				reportDrillHandler.handle(request, response, servletContext);
			} else if(operation.equalsIgnoreCase(BOConstants.RELOADFROMDRILL)) {
				ReloadFromDrillHandler reloadFromDrillHandler = new ReloadFromDrillHandler();
				reloadFromDrillHandler.handle(request, response, servletContext);
			} else if(operation.equalsIgnoreCase(BOConstants.OPERATION_ADD_DIM)) {
				AddDimensionHandler addDimensionHandler = new AddDimensionHandler();
				addDimensionHandler.handle(request, response, servletContext);
			} else {
				System.out.println("Operatioon =================== " + operation);
			}
		} finally {
			logger.debug("OUT");
		}
	 }
	
	
	
	
	
	private WISession getWISession(String user, String password, HttpServletResponse response) {
		// get the session for the predefined user
		WISession wiSession = null;
		try {
			wiSession = wiServer.openSession(user, password);
		} catch (Exception e) {
			response.setContentType("text/html");
			logger.error("Engines"+ this.getClass().getName()+ 
						"service() Cannot create session for user " + user);
			PrintWriter writer = null;
			try {
				writer = response.getWriter();
			} catch (IOException ioe) {
				logger.error("Engines"+ this.getClass().getName()+ 
							"sendOutput() Cannot obtain out writer", ioe);
				return null;
			}
			writer.write("Authentication Failed");
			writer.flush();
			writer.close();
			return null;
		}
		// set the cookie for the session into the response
		if(wiSession.isValid()){ 
			Cookie wiCookie = new Cookie("WebIntelligenceSession", wiSession.getSessionID()); 
			wiCookie.setPath("/"); 
			response.addCookie(wiCookie); 
		} 
		return wiSession;
	}

	
}
