


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.utilities.engines.AbstractEngineStartServlet;
import it.eng.spagobi.utilities.engines.EngineStartServletIOManager;
import it.eng.spagobi.utilities.engines.SpagoBIEngineException;

/**
 * @author Monica Franceschini
 *
 */
public class JPaloEngineStartServlet extends AbstractEngineStartServlet {
	
	private static final String PALO_BASE_URL = "SpagoBIJPaloEngine.html";
	private static String DOCUMENT_ID="document";
	
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
    	JPaloEngineTemplate template = null;
    	String jpaloUrl;

    	logger.debug("IN");
		
		try {		
			HttpSession session = servletIOManager.getHttpSession();
			IEngUserProfile profile = (IEngUserProfile) session.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			ContentServiceProxy contentProxy = new ContentServiceProxy((String)profile.getUserUniqueIdentifier(),session);
			String documentId = (String) servletIOManager.getRequest().getParameter(DOCUMENT_ID);
			Content templateContent = contentProxy.readTemplate(documentId,new HashMap());

			byte[] byteContent = null;
			try {
				BASE64Decoder bASE64Decoder = new BASE64Decoder();
				byteContent = bASE64Decoder.decodeBuffer(templateContent.getContent());
				System.out.println("metodo 1::"+byteContent.length);
				String xmlSourceBean = new String(byteContent);
				System.out.println(xmlSourceBean);
				SourceBean sb =SourceBean.fromXMLString(xmlSourceBean);
				template = new JPaloEngineTemplate(sb);
				System.out.println(template.getViewName());
			}catch (Throwable t){
				logger.warn("Error on decompile",t); 
			}
			//template = new JPaloEngineTemplate( servletIOManager.getTemplateAsSourceBean() );			
			//http://localhost:8888/com.tensegrity.wpalo.SpagoBIJPaloEngine/SpagoBIJPaloEngine.html?options=user="admin";pass="ISMvKXpXpadDiUoOSoAfww==";openview="Sales"
	    	jpaloUrl = PALO_BASE_URL+"?options=";	

			jpaloUrl += "user=\"admin\";pass=\"ISMvKXpXpadDiUoOSoAfww==\"";
			jpaloUrl += ";openview=\"";
			jpaloUrl += template.getViewName();
			//jpaloUrl += "Sales";
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
