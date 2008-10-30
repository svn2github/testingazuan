/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
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
     * The Constructor.
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
	String spagoContext = (String) session.getAttribute(SpagoBIConstants.SBI_CONTEXT);
	String spagoUrlBackEnd = (String) session.getAttribute(SpagoBIConstants.SBI_BACK_END_HOST);
	String backEndUrl=spagoUrlBackEnd+spagoContext;
	logger.debug("Read spagoUrlBackEnd=" + backEndUrl);
	if (backEndUrl == null)
	    logger.warn("BACK END SPAGO CONTEXT IS NULL!!!!");
	if (engineConfig != null) {
	    // sono sui motori...
	    if (EnginConf.getInstance().isSsoActive()) ssoIsActive = true;
	    logger.debug("Read activeSso=" + ssoIsActive);

	    SourceBean sourceBeanConf = (SourceBean) engineConfig.getAttribute("SPAGOBI_SERVER_URL");
	    String spagoBiServerURL= (String) sourceBeanConf.getCharacters();
	    logger.debug("Read spagoBiServerURL=" + spagoBiServerURL);
	    if (spagoBiServerURL!=null && spagoBiServerURL.length()>0){
		    // N.B. if engine-config.xml contains tag SPAGOBI_SERVER_URL
		    // the proxy use this value for invoke WebService
		backEndUrl=spagoBiServerURL;
	    }
	    sourceBeanConf = (SourceBean) engineConfig.getAttribute("FILTER_RECEIPT");
	    filterReceipt = (String) sourceBeanConf.getCharacters();
	    logger.debug("Read filterReceipt=" + filterReceipt);
	    filterReceipt = backEndUrl + filterReceipt;    
	    sourceBeanConf = (SourceBean) engineConfig.getAttribute(className + "_URL");
	    String serviceUrlStr = (String) sourceBeanConf.getCharacters();
	    logger.debug("Read sericeUrl=" + serviceUrlStr);
	    try {
		serviceUrl = new URL(backEndUrl + serviceUrlStr);
	    } catch (MalformedURLException e) {
		logger.error("MalformedURLException:" + backEndUrl + serviceUrlStr, e);
	    }
	    pass=EnginConf.getInstance().getPass();
	    if (pass==null) logger.warn("PassTicked don't set");
	} else {
	    ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	    String spagoBackEndUrl = it.eng.spagobi.commons.utilities.GeneralUtilities.getSpagoBiHostBackEnd()+it.eng.spagobi.commons.utilities.GeneralUtilities.getSpagoBiContext();
	    // sono all'interno del contesto SpagoBI
	    SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
	    String active = (String) validateSB.getCharacters();
	    if (active != null && active.equals("true"))
		ssoIsActive = true;
	    logger.debug("Read activeSso=" + ssoIsActive);
	    validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.FILTER_RECEIPT");
	    filterReceipt = spagoBackEndUrl + (String) validateSB.getCharacters();
	    logger.debug("Read filterReceipt=" + filterReceipt);
	    validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.PASS");
	    pass=(String) validateSB.getCharacters();
	    if (pass==null) logger.warn("PassTicked don't set");
	}
    }

}
