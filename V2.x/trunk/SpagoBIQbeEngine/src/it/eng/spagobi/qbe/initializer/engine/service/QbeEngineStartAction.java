/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.qbe.initializer.engine.service;

import it.eng.qbe.conf.QbeTemplate;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.qbe.commons.exception.QbeEngineException;
import it.eng.spagobi.qbe.commons.service.SpagoBIRequest;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;


// TODO: Auto-generated Javadoc
/**
 * The Class QbeEngineStartAction.
 * 
 * @author Andrea Gioia
 */
public class QbeEngineStartAction extends AbstractHttpAction {	
	
	// INPUT PARAMETERS
	/** The Constant IP_AUDIT_ID. */
	public static final String IP_AUDIT_ID = "SPAGOBI_AUDIT_ID";
	
	/** The Constant IP_DOCUMENT_ID. */
	public static final String IP_DOCUMENT_ID = "document";
	
	/** The Constant IP_QUERY_ID. */
	public static final String IP_QUERY_ID = "query";
	
	/** The Constant IP_COUNTRY. */
	public static final String IP_COUNTRY = "country";
	
	/** The Constant IP_LANGUAGE. */
	public static final String IP_LANGUAGE = "language";
	
	
	// OUTPUT PARAMETERS
	/** The Constant OP_SPAGOBI_REQUEST. */
	public static final String OP_SPAGOBI_REQUEST = "SPAGOBI_REQUEST";
	
	/** The Constant OP_DATAMART_PROPERTIES. */
	public static final String OP_DATAMART_PROPERTIES = "DATAMART_PROPERTIES";
	
	// SESSION PARAMETRES	
	/** The Constant IP_USER_PROFILE. */
	public static final String IP_USER_PROFILE = IEngUserProfile.ENG_USER_PROFILE;
	
	
	
	/** Logger component. */
    private static transient Logger logger = Logger.getLogger(QbeEngineStartAction.class);
	
	
    /* (non-Javadoc)
     * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
     */
    public void service(SourceBean request, SourceBean response) throws QbeEngineException {
    	logger.debug("IN");
       
    	try {
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
			
			
					
			response.setAttribute(OP_SPAGOBI_REQUEST, spagoBIRequest);
			response.setAttribute(OP_DATAMART_PROPERTIES, props.toString());
		} catch (Exception e) {
			//EMFInternalError error = new EMFInternalError(EMFErrorSeverity.ERROR, "Errore orrore", e, "error object description");
			//getErrorHandler().addError( error );
			if(e instanceof QbeEngineException) throw (QbeEngineException)e;
			
			String description = "An unpredicted error occurred while executing " + getActionName() + " service.";
			Throwable rootException = e;
			while(rootException.getCause() != null) rootException = rootException.getCause();
			String str = rootException.getMessage()!=null? rootException.getMessage(): rootException.getClass().getName();
			description += "<br>The root cause of the error is: " + str;
			List hints = new ArrayList();
			hints.add("Sorry, there are no hints available right now on how to fix this problem");
			throw new QbeEngineException("Service error", description, hints, e);
		}
		

		logger.debug("OUT");
	}
	
    
    /**
     * Gets the audit id.
     * 
     * @return the audit id
     */
    private String getAuditId() {
    	String auditId = null;
    	auditId = getHttpRequest().getParameter(IP_AUDIT_ID);
    	return auditId;
		
    }
    
    /**
     * Update audit.
     * 
     * @param auditId the audit id
     * @param userId the user id
     */
    private void updateAudit(String auditId, String userId) {
    	//HttpServletRequest httpRequest = (HttpServletRequest) this.getRequestContainer().getInternalRequest();
		HttpSession session = getHttpRequest().getSession();
		AuditAccessUtils auditAccessUtils = (AuditAccessUtils) session.getAttribute("SPAGOBI_AUDIT_UTILS");
		if (auditId != null && auditAccessUtils != null) {
			auditAccessUtils.updateAudit(session, getUserId(), auditId, new Long(System
				    .currentTimeMillis()), null, "EXECUTION_STARTED", null, null);
		}
    }
    
    /**
     * Gets the document id.
     * 
     * @param parameters the parameters
     * 
     * @return the document id
     */
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
	 * User profile is loaded by the SpagoBiAccessFilter.
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
	 * Read all parameters available in the request into e map object.
	 * 
	 * @param request the request
	 * 
	 * @return the parameters
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
	 * Convert multi-value parameters in a comma separated list of values.
	 * 
	 * @param params the params
	 * @param parName the par name
	 * @param parValue the par value
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
	
	/**
	 * Gets the template.
	 * 
	 * @param userId the user id
	 * @param documentId the document id
	 * 
	 * @return the template
	 */
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
	
	/**
	 * Gets the data source.
	 * 
	 * @param userId the user id
	 * @param documentId the document id
	 * 
	 * @return the data source
	 */
	private SpagoBiDataSource getDataSource(String userId, String documentId) {
		DataSourceServiceProxy proxyDS = null;
		SpagoBiDataSource dataSource = null;
		
		proxyDS = new DataSourceServiceProxy(userId, getHttpRequest().getSession());
    	dataSource = proxyDS.getDataSource(documentId);
    	
    	return dataSource;
	}
	
	/**
	 * Gets the locale.
	 * 
	 * @param parameters the parameters
	 * 
	 * @return the locale
	 */
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
