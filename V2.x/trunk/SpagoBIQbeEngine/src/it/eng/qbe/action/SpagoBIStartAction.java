/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.qbe.action;

import it.eng.qbe.conf.QbeEngineConf;
import it.eng.qbe.conf.QbeTemplate;
import it.eng.qbe.utility.SpagoBIInfo;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.qbe.commons.service.SpagoBIRequest;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sun.misc.BASE64Decoder;

import org.apache.log4j.Logger;

import com.jamonapi.Monitor;
import com.jamonapi.MonitorFactory;


/**
 * @author Andrea Gioia
 */
public class SpagoBIStartAction extends AbstractHttpAction {	
	
	// INPUT PARAMETERS
	public static final String IP_AUDIT_ID = "SPAGOBI_AUDIT_ID";
	public static final String IP_DOCUMENT_ID = "document";
	public static final String IP_QUERY_ID = "query";
	public static final String IP_COUNTRY = "country";
	public static final String IP_LANGUAGE = "language";
	
	
	// OUTPUT PARAMETERS
	public static final String OP_SPAGOBI_REQUEST = "SPAGOBI_REQUEST";
	public static final String OP_DATAMART_PROPERTIES = "DATAMART_PROPERTIES";
	
	// SESSION PARAMETRES	
	public static final String IP_USER_PROFILE = IEngUserProfile.ENG_USER_PROFILE;
	
	
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(SpagoBIStartAction.class);
	
	
    public void service(SourceBean request, SourceBean response) {
    	logger.debug("Start processing a new request...");
       
    	SessionContainer session = getRequestContainer().getSessionContainer();
    	
    	String userId = getUserId();
    	logger.debug("userId:" + userId);
    	
    	String auditId = getAuditId();
    	logger.debug("auditId:" + auditId);
    	
    	// AUDIT UPDATE
		updateAudit(auditId, userId);		
		session.setAttribute("SPAGOBI_AUDIT_ID", auditId);
    	    
		Map parameters = getParameters(request);
    	logger.debug("Request parameters read sucesfully");
    	
		String documentId = getDocumentId(parameters);   
    	logger.debug("documentId:" + documentId);    	
    		
		String queryId = (String)parameters.get(IP_QUERY_ID);
		logger.debug("queryId:" + documentId);  
		parameters.remove("query");
		
		SourceBean templateSB = getTemplate(userId, documentId);
		QbeTemplate template = new QbeTemplate(templateSB);
		
		SpagoBiDataSource dataSource = getDataSource(userId, documentId);		
			
		Locale locale = getLocale(parameters);
		
		/*
		 MapCatalogueAccessUtils mapCatalogueAccessUtils = new MapCatalogueAccessUtils(getHttpRequest().getSession());
			setAttributeInSession("MAP_CATALOGUE_CLIENT", mapCatalogueAccessUtils);
			setAttributeInSession("MAP_CATALOGUE_CLIENT_URL", mapCatalogueManagerUrl);
		 */
		
		SpagoBIRequest spagoBIRequest = new SpagoBIRequest(request);	
		spagoBIRequest.setUserId(userId);
		spagoBIRequest.setAuditId(auditId);
		spagoBIRequest.setDocumentId(documentId);
		spagoBIRequest.setQueryId(queryId);		
		spagoBIRequest.setTemplate(template);
		spagoBIRequest.setDataSource(dataSource);
		spagoBIRequest.setLocale(locale);	
		
		
		String props = "";
		Iterator it = parameters.keySet().iterator();
		while(it.hasNext()) {
			String parameterName = (String)it.next();
			Object parameterValue = parameters.get(parameterName);
			if(parameterValue.getClass().getName().equalsIgnoreCase(String.class.getName())) {
				props += parameterName + "=" + parameterValue + "\n";
			}
		}
		
		
		session.delAttribute("FUNCTIONALITIES");			
		Map functionalities = template.getFunctionalities();
		if(functionalities != null) session.setAttribute("FUNCTIONALITIES", functionalities);
		
		
		try {			
			response.setAttribute(OP_SPAGOBI_REQUEST, spagoBIRequest);
			response.setAttribute(OP_DATAMART_PROPERTIES, props.toString());
		} catch (SourceBeanException e) {
			e.printStackTrace();
		} 
	}
	
    
    private String getAuditId() {
    	String auditId = null;
    	auditId = getHttpRequest().getParameter(IP_AUDIT_ID);
    	return auditId;
		
    }
    
    private void updateAudit(String auditId, String userId) {
    	//HttpServletRequest httpRequest = (HttpServletRequest) this.getRequestContainer().getInternalRequest();
		HttpSession session = getHttpRequest().getSession();
		AuditAccessUtils auditAccessUtils = (AuditAccessUtils) session.getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditId != null && auditAccessUtils != null) {
			auditAccessUtils.updateAudit(session, getUserId(), auditId, new Long(System
				    .currentTimeMillis()), null, "EXECUTION_STARTED", null, null);
		}
    }
    private String getDocumentId(Map parameters) {
    	String documentId = null;
    	String documentIdInSection = null;
    
    	documentIdInSection = (String)getHttpRequest().getSession().getAttribute(IP_DOCUMENT_ID);
    	logger.debug("documentId in Session:" + documentIdInSection);
    	
    	if(parameters.containsKey(IP_DOCUMENT_ID)) {
    		documentId = (String) parameters.get(IP_DOCUMENT_ID);
    	} else {
    		documentId = documentIdInSection;
    		logger.debug("documentId has been taken from session");
    	}
    	
    	return documentId;   	
    }
    
	/**
	 * User profile is loaded by the SpagoBiAccessFilter
	 * 
	 * @return the unique id of the given user
	 */
    private String getUserId() {
    	IEngUserProfile profile = null;
    	String userId = null;
    		
    	profile =(IEngUserProfile) getHttpRequest().getSession().getAttribute(IP_USER_PROFILE);    	
    	userId = (String)profile.getUserUniqueIdentifier();
    	
    	return userId;
    }
	
	/**
	 * Read all parameters available in the request into e map object
	 * 
	 * @param request
	 * @return
	 */
	private Map getParameters(SourceBean request) {
		logger.debug("IN");
		
		Map parameters = null;
		Iterator attributes = null;
		SourceBeanAttribute attribute = null;
		String parName = null;
		String parValue = null;
    	
		
		parameters = new HashMap();
    	attributes = request.getContainedAttributes().iterator();
    	
    	logger.debug("Reading request parameters...");
    	while (attributes.hasNext()) {
    		attribute = (SourceBeanAttribute)attributes.next();
			parName = attribute.getKey();
			parValue = (String)attribute.getValue();
    	    addParToParMap(parameters, parName, parValue);
    	    logger.debug("Read parameter [" + parName + "] with value ["+ parValue + "] from request");
    	}
    	
    	logger.debug("OUT");
    	return parameters;
	}
	
	/**
	 * Convert multi-value parameters in a comma separated list of values
	 * 
	 * @param params
	 * @param parName
	 * @param parValue
	 */
	private void addParToParMap(Map params, String parName, String parValue) {
		String newParValue;
		
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			for(int i = 0; i < values.size(); i++) {
				newParValue += (i>0?",":"");
				newParValue += values.get(i);
			}
			
		} else {
			newParValue = parValue;
		}
		
		params.put(parName, newParValue);
	}
	
	private SourceBean getTemplate(String userId, String documentId) {
		SourceBean templateSB = null;
		ContentServiceProxy contentProxy = null;
		Content template = null;
		String templateContent = null;
		BASE64Decoder bASE64Decoder = null;
		
		contentProxy = new ContentServiceProxy(userId, getHttpRequest().getSession());		
		template = contentProxy.readTemplate(documentId);
		logger.debug("Read the template."+ template.getFileName());	
		
		
		try {
			bASE64Decoder = new BASE64Decoder();
			templateContent = new String( bASE64Decoder.decodeBuffer(template.getContent()) );
			templateSB = SourceBean.fromXMLString(templateContent);
			logger.debug("Read the template."+ template.getFileName());	
		} catch (IOException e) {
			logger.error("Impossible to get content from template\n" + e);
			e.printStackTrace();
		} catch (SourceBeanException e) {
			logger.error("Impossible to decode template's content\n" + e);
			e.printStackTrace();
		}		
		
		
		return templateSB;
	}
	
	private SpagoBiDataSource getDataSource(String userId, String documentId) {
		DataSourceServiceProxy proxyDS = null;
		SpagoBiDataSource dataSource = null;
		
		proxyDS = new DataSourceServiceProxy(userId, getHttpRequest().getSession());
    	dataSource = proxyDS.getDataSource(documentId);
    	
    	return dataSource;
	}
	
	private Locale getLocale(Map parameters) {
		Locale locale = null;
		String country = null;
		String language = null;
		
		country = (String)parameters.get(IP_COUNTRY);
		logger.debug("country: " + country);
		parameters.remove("country");
		
		language = (String)parameters.get(IP_LANGUAGE);
		logger.debug("language: " + language);
		parameters.remove("language");	
		
		if(country != null && language != null) {
			locale =  new Locale(language, country);
		} else {
			logger.warn("Ipossible to recover from request SpagoBI's locale");
			locale = Locale.getDefault();			
			logger.warn("Default locale will be used (" + locale.getCountry() + "," + locale.getLanguage() + ")");			
		}
		
		return locale;
	}
}
