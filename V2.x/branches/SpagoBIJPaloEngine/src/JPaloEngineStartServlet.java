


import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.palo.viewapi.services.ServiceProvider;
import org.palo.viewapi.services.ViewService;

import com.tensegrity.palo.gwt.core.server.services.UserSession;

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
				String xmlSourceBean = new String(byteContent);
				SourceBean sb =SourceBean.fromXMLString(xmlSourceBean);
				template = new JPaloEngineTemplate(sb);		
				

			}catch (Throwable t){
				logger.warn("Error on decompile",t); 
			}
			
			//looks for cube name to create view 
			//NB: methods to create view dinamically available only if already logged in Jpalo
			String cubeName = template.getCubeName();

	    	jpaloUrl = PALO_BASE_URL;	

	    	jpaloUrl += "?theme=gray&options=(";
			//adds information about spagobi context of execution
	    	if(documentId != null && profile != null){
	    		jpaloUrl += "spagobiusr=\""+(String)profile.getUserUniqueIdentifier()+"\"";	    	
	    		jpaloUrl += ",spagobidoc=\""+documentId+"\",";
	    	}
	    	//jpalo informations
			jpaloUrl += "user=\"admin\",pass=\"ISMvKXpXpadDiUoOSoAfww==\"";
			if(cubeName != null && !cubeName.equals("")){				
				System.out.println(cubeName);
				jpaloUrl += ",openview=\"";
				jpaloUrl += "\"";
				jpaloUrl += ",cubename=\"";
				jpaloUrl += cubeName;
				jpaloUrl += "\"";
			}else{
				jpaloUrl += ",openview=\"";
				jpaloUrl += template.getViewName();
				jpaloUrl += "\"";
			}
			jpaloUrl += ",hidestaticfilter";
			jpaloUrl += ",hidenavigator";
			jpaloUrl += ",hideviewtabs";
			jpaloUrl += ")";

			String urlWithSessionID = servletIOManager.getResponse().encodeRedirectURL( jpaloUrl );
			servletIOManager.getResponse().sendRedirect( urlWithSessionID );
	    
		} catch(Throwable t) {
			t.printStackTrace();
			throw new SpagoBIEngineException("An unpredicted error occured while executing palo-engine. Please check the log for more informations on the causes",
			"an.unpredicted.error.occured", t);				
		} finally {
			servletIOManager.auditServiceEndEvent();
			logger.debug("OUT");
		}

    }
}
