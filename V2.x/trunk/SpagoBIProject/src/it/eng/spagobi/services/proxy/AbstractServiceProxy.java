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
import it.eng.spagobi.services.common.IProxyService;
import it.eng.spagobi.services.common.IProxyServiceFactory;

public abstract class AbstractServiceProxy {

    static private Logger logger = Logger.getLogger(AbstractServiceProxy.class);
    private HttpSession session = null;
    private boolean ssoIsActive = false;
    private String filterReceipt = null;
    protected URL serviceUrl = null;
    protected String userId = null;

    protected String readTicket() throws IOException {
	if (ssoIsActive && ! UserProfile.isSchedulerUser(userId) ) {
	    IProxyService proxyService=IProxyServiceFactory.createProxyService();
	    return proxyService.readTicket(session, filterReceipt);
	} else
	    return "";

    }

    public AbstractServiceProxy(String user,HttpSession session) {
	this.session = session;
	this.userId=user;
	init();
    }

    public AbstractServiceProxy() {
	init();
    }

    protected void init() {
	String className = this.getClass().getSimpleName();
	logger.debug("Read className=" + className);
	SourceBean engineConfig = EnginConf.getInstance().getConfig();
	String spagoContext = (String) session.getAttribute(SpagoBIConstants.SBICONTEXTURL);
	logger.debug("Read spagoContext=" + spagoContext);
	if (spagoContext == null)
	    logger.warn("SPAGO CONTEXT IS NULL!!!!");
	if (engineConfig != null) {
	    // sono sui motori...
	    SourceBean validateSB = (SourceBean) engineConfig.getAttribute("ACTIVE_SSO");
	    String active = (String) validateSB.getCharacters();
	    if (active != null && active.equals("true"))
		ssoIsActive = true;
	    logger.debug("Read activeSso=" + ssoIsActive);
	    validateSB = (SourceBean) engineConfig.getAttribute("FILTER_RECEIPT");
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
	} else {
	    ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	    String SpagoBiUrl = it.eng.spagobi.commons.utilities.GeneralUtilities.getSpagoBiContextAddress();
	    // sono all'interno del contesto SpagoBI
	    SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
	    String active = (String) validateSB.getCharacters();
	    if (active != null && active.equals("true"))
		ssoIsActive = true;
	    logger.debug("Read activeSso=" + ssoIsActive);
	    validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.FILTER_RECEIPT");
	    filterReceipt = SpagoBiUrl + (String) validateSB.getCharacters();
	    logger.debug("Read filterReceipt=" + filterReceipt);
	}
    }

}
