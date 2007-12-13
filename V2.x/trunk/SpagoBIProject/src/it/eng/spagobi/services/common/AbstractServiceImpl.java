package it.eng.spagobi.services.common;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.services.security.exceptions.SecurityException;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.ProxyTicketValidator;

public abstract class AbstractServiceImpl {
    
    static private Logger logger = Logger.getLogger(AbstractServiceImpl.class);
    
    protected String validateUrl =null;
    protected String validateService =null;
    protected boolean activeSso=false;
   
    public AbstractServiceImpl(){
	init();
    }
    
    private void init(){
		logger.debug("IN");
		ConfigSingleton config = ConfigSingleton.getInstance();
		        SourceBean validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.URL");
		validateUrl = (String)validateSB.getCharacters();
		logger.debug("Read validateUrl="+validateUrl);
		        validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.VALIDATE-USER.SERVICE");
		validateService = (String)validateSB.getCharacters();
		logger.debug("Read validateService="+validateService);
	        validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.ACTIVE");
		String active = (String)validateSB.getCharacters();
		if (active!=null && active.equals("true")) activeSso=true;
		logger.debug("Read activeSso="+activeSso);
	    
    }
    /**
     * check the ticket used for verify the user authentication
     * @param ticket
     * @return
     * @throws SecurityException
     */    
    protected boolean validateTicket(String ticket,String userId) throws SecurityException {
	logger.debug("IN");

        try {
            ProxyTicketValidator pv = null;       
            pv = new ProxyTicketValidator();
            pv.setCasValidateUrl(validateUrl);
            pv.setServiceTicket(ticket);
            pv.setService(validateService);
            pv.setRenew(false);
            pv.validate();
            if (pv.isAuthenticationSuccesful()) {
        	String tmpUserId = pv.getUser();
        	if ( userId!=null && !userId.equals(tmpUserId)){
        	    logger.error("Proxy and application users are not the same !!!!! "+userId+"-"+tmpUserId);
        	    return false;
        	}
                return true;
            }
            else {
        	logger.error("Token NOT VALID");
                return false;
            }
        }catch (Exception e) {
            logger.error("Exception",e);
           throw new SecurityException();
        }finally{
	        logger.debug("OUT");
        }
        
}

}
