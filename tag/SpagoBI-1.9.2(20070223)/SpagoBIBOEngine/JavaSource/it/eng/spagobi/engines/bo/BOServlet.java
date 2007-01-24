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
		}else if(operation.equalsIgnoreCase(BOConstants.OPERATION_DRILL)) {
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
