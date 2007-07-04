/**
 * 
 * LICENSE: see LICENSE.txt file
 * 
 */
package it.eng.spagobi.jpivotaddins.engines;

import it.eng.spagobi.jpivotaddins.util.SecurityUtilities;
import it.eng.spagobi.jpivotaddins.util.SessionObjectRemoval;
import it.eng.spagobi.utilities.messages.EngineMessageBundle;

import java.io.IOException;
import java.security.PublicKey;
import java.util.Locale;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tonbeller.wcf.controller.RequestContext;

public class JPivotServlet extends HttpServlet {

    private transient PublicKey publicKeyDSASbi = null;
    private transient Logger logger = Logger.getLogger(this.getClass());
    private transient SecurityUtilities secUt = null;
    private transient boolean securityAble = true;
    
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	ServletContext context = this.getServletContext();
		String secAblePar = context.getInitParameter("SECURITY_ABLE");
		if (!secAblePar.equalsIgnoreCase("true")) {
        	securityAble = false;
        }
        if (securityAble) {
			secUt = new SecurityUtilities(logger);
			publicKeyDSASbi = secUt.getPublicKey();
		} 
	}
    
	public void service(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.debug("Starting service method...");
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
		
		boolean authorized = true;
		Object auth = session.getAttribute("authorized");
		if (auth!=null) {	
			if(securityAble) {
				authorized = false;
				String token = request.getParameter("TOKEN_SIGN");
				String tokenclear = request.getParameter("TOKEN_CLEAR");
				if((token!=null) && !token.trim().equals("") &&  (tokenclear!=null) && !tokenclear.trim().equals("")) {
		    		if(secUt.authenticate(token, tokenclear, publicKeyDSASbi)) {
		    			authorized = true;
		    			session.setAttribute("authorized", "true");
		    		}
				}
			} else {
				String token = request.getParameter("TOKEN_SIGN");
				if(token!=null) {
					authorized = false;
					logger.error("The engine security check is not active but the driver in sending secure calls." + 
						     	"Please turn on the security check of the engine");
				}
			}
		}
		
		if(!authorized) {
			String unauthorized = EngineMessageBundle.getMessage("Unauthorized", locale);
			if (unauthorized == null) unauthorized = "Unauthorized access.";
			ServletOutputStream out = response.getOutputStream();
			out.write("<html><body><center><h2>".getBytes());
			out.write(unauthorized.getBytes());
			out.write("</h2></center></body></html>".getBytes());
			out.flush();
			out.close();
			return;
		}
		
		// if is the first request the following parameters have a request value
	    // and they are put in session, otherwise their values are taken from the session
	    String jcrPath = request.getParameter("templatePath");
		String spagoBIBaseUrl = request.getParameter("spagobiurl");
		String user = request.getParameter("user");
		String role = request.getParameter("role");
		
		if (jcrPath != null) session.setAttribute("templatePath", jcrPath);
	    //else jcrPath = (String)session.getAttribute("templatePath");
	    if (spagoBIBaseUrl != null) session.setAttribute("spagobiurl", spagoBIBaseUrl);
	    //else spagoBIBaseUrl = (String)session.getAttribute("spagobiurl");
	    if (user != null) session.setAttribute("user", user);
	    //else user = (String)session.getAttribute("user");
	    if (role != null) session.setAttribute("role", role);
	    //else role = (String)session.getAttribute("role");
	    
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
		
	}
	
}
