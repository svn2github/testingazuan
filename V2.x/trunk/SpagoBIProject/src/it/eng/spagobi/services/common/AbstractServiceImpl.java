package it.eng.spagobi.services.common;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

/**
 * Abstract class for all Service Implementation
 */
public abstract class AbstractServiceImpl {

    static private Logger logger = Logger.getLogger(AbstractServiceImpl.class);

    protected String validateUrl = null;
    protected String validateService = null;
    protected boolean activeSso = false;
    private String pass = null;

    /**
     * 
     */
    public AbstractServiceImpl() {
	init();
    }

    private void init() {
	logger.debug("IN");
	String SpagoBiUrl = it.eng.spagobi.commons.utilities.GeneralUtilities.getBackEndSpagoBiContextAddress();
	ConfigSingleton config = ConfigSingleton.getInstance();
	SourceBean validateSB = (SourceBean) config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.URL");
	validateUrl = (String) validateSB.getCharacters();
	logger.debug("Read validateUrl=" + validateUrl);
	validateSB = (SourceBean) config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.SERVICE");
	validateService = SpagoBiUrl + (String) validateSB.getCharacters();
	logger.debug("Read validateService=" + validateService);
	validateSB = (SourceBean) config.getAttribute("SPAGOBI_SSO.ACTIVE");
	String active = (String) validateSB.getCharacters();
	if (active != null && active.equals("true"))
	    activeSso = true;
	logger.debug("Read activeSso=" + activeSso);
	validateSB = (SourceBean) config.getAttribute("SPAGOBI_SSO.PASS");
	pass = (String) validateSB.getCharacters();

    }

    /**
     * check the ticket used for verify the user authentication
     * 
     * @param ticket
     *                String
     * @return String
     * @throws SecurityException
     */
    protected void validateTicket(String ticket, String userId) throws SecurityException {
	logger.debug("IN");
	if (ticket==null){
	    logger.warn("Ticket is NULL!!!!");
	    throw new SecurityException();	    
	}
	if (userId == null) {
	    logger.warn("UserID is NULL!!!!");
	    throw new SecurityException();
	}
	if (activeSso) {
	    logger.debug("activeSso checks are ON");
	    if (ticket.equals(pass)) {
		logger.debug("JUMP che ticket validation");
	    } else {
		IProxyService proxyService = IProxyServiceFactory.createProxyService();
		proxyService.validateTicket(ticket, userId, validateUrl, validateService);
	    }
	}
	logger.debug("OUT");

    }

}
