/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.engines;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.jpivotaddins.util.SessionObjectRemoval;
import it.eng.spagobi.services.proxy.SecurityServiceProxy;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tonbeller.wcf.controller.RequestContext;

public class JPivotServlet extends HttpServlet {


    private transient Logger logger = Logger.getLogger(this.getClass());

    
	public void service(HttpServletRequest request, HttpServletResponse response) 
			throws IOException, ServletException {
		
		logger.debug("Starting service method...");
		// USER PROFILE
		String documentId=(String)request.getParameter("document");
		String userId=(String)request.getParameter("userId");
		
		try {
		    HttpSession session=request.getSession();
		    IEngUserProfile profile=(IEngUserProfile)session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
		    if (profile==null){
			SecurityServiceProxy proxy=new SecurityServiceProxy();
			profile = proxy.getUserProfile(userId);
			session.setAttribute(IEngUserProfile.ENG_USER_PROFILE,profile);
		    }
		} catch (SecurityException e1) {
		    logger.error("SecurityException",e1);
		    throw new ServletException();
		}
			
		String language = request.getParameter("language");
		String country = request.getParameter("country");
		logger.debug("Locale parameters received: language = [" + language + "] ; country = [" + country + "]");
		
		Locale locale = null;
		
		try {
			locale = new Locale(language, country);
		} catch (Exception e) {
			logger.debug("Error while creating Locale object from input parameters: language = [" + language + "] ; country = [" + country + "]");
			logger.debug("Creating default locale [en,US].");
			locale = new Locale("en", "US");
		}
		
		HttpSession session = request.getSession();
		SessionObjectRemoval.removeSessionObjects(session);
		
		RequestContext context = RequestContext.instance();
		context.setLocale(locale);

		
		session.setAttribute("document", documentId);
	    
	    String dimAccRulStr = request.getParameter("dimension_access_rules");
	    session.setAttribute("dimension_access_rules", dimAccRulStr);
		
	    String forward = request.getParameter("forward");
	    if (forward == null || forward.trim().equals("")) {
	    	forward = "jpivotOlap.jsp";
	    }
	    
		try {
			request.getRequestDispatcher(forward).forward(request, response);
		} catch (ServletException e) {
			logger.error("Error while forwarding to " + forward, e);
		}
		logger.debug("End service method");	
	}	
}

