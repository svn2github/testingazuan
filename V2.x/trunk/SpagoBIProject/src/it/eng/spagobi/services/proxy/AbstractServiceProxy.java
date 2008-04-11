package it.eng.spagobi.services.proxy;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.services.common.EnginConf;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.common.SsoServiceFactory;

/**
 * Abstract Class of all Proxy 
 */
public abstract class AbstractServiceProxy {

    static private Logger logger = Logger.getLogger(AbstractServiceProxy.class);
    private HttpSession session = null;
    private boolean ssoIsActive = false;
    private String filterReceipt = null;
    protected URL serviceUrl = null;
    protected String userId = null;
    protected boolean isSecure=true; // if false don't sent a valid ticket
    private String pass=null;

   
    /**
     * 
     * @return String Ticket for SSO control
     * @throws IOException 
     */
    protected String readTicket() throws IOException {
	if (!isSecure){
	    return pass;
	}
	if (ssoIsActive && ! UserProfile.isSchedulerUser(userId) ) {
	    SsoServiceInterface proxyService=SsoServiceFactory.createProxyService();
	    return proxyService.readTicket(session, filterReceipt);
	} else
	    return "";

    }

    /**
     * 
     * @param user String , user ID
     * @param session Http Session
     */
    public AbstractServiceProxy(String user,HttpSession session) {
	this.session = session;
	this.userId=user;
	init();
    }

    protected AbstractServiceProxy() {
	init();
    }

    /**
     * Initilize the configuration
     */
    protected void init() {
	String secureAttributes=(String)session.getAttribute("isBackend");
	if (secureAttributes!=null && secureAttributes.equals("true")){
	    isSecure=false;
	}
	
	String className = this.getClass().getSimpleName();
	logger.debug("Read className=" + className);
	SourceBean engineConfig = EnginConf.getInstance().getConfig();
	String spagoContext = (String) session.getAttribute(SpagoBIConstants.BACK_END_SBICONTEXTURL);
	logger.debug("Read spagoContext=" + spagoContext);
	if (spagoContext == null)
	    logger.warn("BACK END SPAGO CONTEXT IS NULL!!!!");
	if (engineConfig != null) {
	    // sono sui motori...
	    if (EnginConf.getInstance().isSsoActive()) ssoIsActive = true;
	    logger.debug("Read activeSso=" + ssoIsActive);
	    SourceBean validateSB = (SourceBean) engineConfig.getAttribute("FILTER_RECEIPT");
	    filterReceipt = (String) validateSB.getCharacters();
	    logger.debug("Read filterReceipt=" + filterReceipt);
	    filterReceipt = spagoContext + filterReceipt;
	    validateSB = (SourceBean) engineConfig.getAttribute(className + "_URL");
	    String serviceUrlStr = (String) validateSB.getCharacters();
	    logger.debug("Read sericeUrl=" + serviceUrlStr);
	    try {
		serviceUrl = new URL(spagoContext + serviceUrlStr);
	    } catch (MalformedURLException e) {
		logger.error("MalformedURLException:" + serviceUrlStr, e);
	    }
	    pass=EnginConf.getInstance().getPass();
	    if (pass==null) logger.warn("PassTicked don't set");
	} else {
	    ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	    String SpagoBiUrl = it.eng.spagobi.commons.utilities.GeneralUtilities.getBackEndSpagoBiContextAddress();
	    // sono all'interno del contesto SpagoBI
	    SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
	    String active = (String) validateSB.getCharacters();
	    if (active != null && active.equals("true"))
		ssoIsActive = true;
	    logger.debug("Read activeSso=" + ssoIsActive);
	    validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.FILTER_RECEIPT");
	    filterReceipt = SpagoBiUrl + (String) validateSB.getCharacters();
	    logger.debug("Read filterReceipt=" + filterReceipt);
	    validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.PASS");
	    pass=(String) validateSB.getCharacters();
	    if (pass==null) logger.warn("PassTicked don't set");
	}
    }

}
