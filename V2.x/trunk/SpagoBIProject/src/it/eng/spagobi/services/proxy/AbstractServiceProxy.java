package it.eng.spagobi.services.proxy;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import edu.yale.its.tp.cas.client.CASReceipt;
import edu.yale.its.tp.cas.client.filter.CASFilter;
import edu.yale.its.tp.cas.proxy.ProxyTicketReceptor;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;

public abstract class AbstractServiceProxy {

    static private Logger logger = Logger.getLogger(AbstractServiceProxy.class);
    
    protected HttpSession session = null;
    protected boolean ssoIsActive=false;
    protected String filterReceipt=null;
    
    protected String readTicket() throws IOException {
	CASReceipt cr = (CASReceipt) session
		.getAttribute(CASFilter.CAS_FILTER_RECEIPT);
	return ProxyTicketReceptor.getProxyTicket(cr.getPgtIou(),filterReceipt);

    }
    
    public AbstractServiceProxy(HttpSession session) {
	this.session = session;
	init();
    }
    public AbstractServiceProxy() {
    }    
    
    protected void init(){
	ConfigSingleton config = ConfigSingleton.getInstance();
        SourceBean validateSB = (SourceBean)config.getAttribute("ENGINE-CONFIGURATION.ACTIVE_SSO");
        if (validateSB!=null){
            // sono sui motori...
            String active = (String)validateSB.getCharacters();
            if (active!=null && active.equals("true")) ssoIsActive=true;
            logger.debug("Read activeSso="+ssoIsActive);
            validateSB = (SourceBean)config.getAttribute("ENGINE-CONFIGURATION.FILTER_RECEIPT");
            filterReceipt = (String)validateSB.getCharacters();
            logger.debug("Read filterReceipt="+filterReceipt);
        }else {
            // sono all'interno del contesto SpagoBI
            validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.ACTIVE");
            String active = (String)validateSB.getCharacters();
            if (active!=null && active.equals("true")) ssoIsActive=true;
            logger.debug("Read activeSso="+ssoIsActive); 
            validateSB = (SourceBean)config.getAttribute("SPAGOBI_SSO.FILTER_RECEIPT");
            filterReceipt = (String)validateSB.getCharacters();   
            logger.debug("Read filterReceipt="+filterReceipt);
        }
    }
    
}
