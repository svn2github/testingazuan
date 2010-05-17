


import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.EngineStartServletIOManager;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

/**
 * @author Monica Franceschini
 *
 */
public class JPaloEngineStartServlet extends AbstractEngineStartServlet {
	
	private static final String PALO_BASE_URL = "SpagoBIJPaloEngine.html";
	
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

   
    public void doService( EngineStartServletIOManager servletIOManager ) throws SpagoBIEngineException {
    	//JPaloEngineTemplate template;
    	String jpaloUrl;

    	logger.debug("IN");
		
		try {		
		
			//template = new JPaloEngineTemplate( servletIOManager.getTemplateAsSourceBean() );			
			//http://localhost:8888/com.tensegrity.wpalo.SpagoBIJPaloEngine/SpagoBIJPaloEngine.html?options=user="admin";pass="ISMvKXpXpadDiUoOSoAfww==";openview="Sales"
	    	jpaloUrl = PALO_BASE_URL+"?options=";	

			jpaloUrl += "user=\"admin\";pass=\"ISMvKXpXpadDiUoOSoAfww==\"";
			jpaloUrl += ";openview=\"";
			//jpaloUrl += template.getViewName();
			jpaloUrl += "Sales";
			jpaloUrl += "\"";
			jpaloUrl += ",hidenavigator";
			jpaloUrl += ",hideviewtabs";


			String urlWithSessionID = servletIOManager.getResponse().encodeRedirectURL( jpaloUrl );
			servletIOManager.getResponse().sendRedirect( urlWithSessionID );
	    
		} catch(Throwable t) {
			throw new SpagoBIEngineException("An unpredicted error occured while executing palo-engine. Please check the log for more informations on the causes",
			"an.unpredicted.error.occured", t);				
		} finally {
			servletIOManager.auditServiceEndEvent();
			logger.debug("OUT");
		}

    }
}
