/**
Copyright (c) 2005-2008, Engineering Ingegneria Informatica s.p.a.
All rights reserved.

Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright notice, this list of 
      conditions and the following disclaimer.
      
    * Redistributions in binary form must reproduce the above copyright notice, this list of 
      conditions and the following disclaimer in the documentation and/or other materials 
      provided with the distribution.
      
    * Neither the name of the Engineering Ingegneria Informatica s.p.a. nor the names of its contributors may
      be used to endorse or promote products derived from this software without specific
      prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND 
CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, 
INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE 
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR 
CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE

**/
package it.eng.spagobi.utilities.engines;



import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.configuration.FileCreatorConfiguration;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.EventServiceProxy;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.service.AbstractBaseServlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public abstract class AbstractEngineStartServlet extends AbstractBaseServlet {
	
	private String userId;
	private String userUniqueIdentifier;
	private String auditId;
	private String documentId;
	private Locale locale;
	
	private SourceBean template;
	
	private ContentServiceProxy contentProxy;
	private AuditServiceProxy auditProxy;
	private EventServiceProxy eventProxy;
	
	private Map env;
	
	private static final BASE64Decoder DECODER = new BASE64Decoder();
	
	public static final String USER_ID = IEngUserProfile.ENG_USER_PROFILE;
	public static final String AUDIT_ID = "SPAGOBI_AUDIT_ID";
	public static final String DOCUMENT_ID = "document";
	
	public static final String COUNTRY = "country";
	public static final String LANGUAGE = "language";


    /**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractEngineStartServlet.class);
    
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);	
    	
    	String path = getServletConfig().getServletContext().getRealPath("/WEB-INF");
    	ConfigSingleton.setConfigurationCreation( new FileCreatorConfiguration( path ) );
    	ConfigSingleton.setRootPath( path );
    	ConfigSingleton.setConfigFileName("/empty.xml");    	
    }
    
    public void doService() throws SpagoBIEngineException {
    	
    	logger.debug("User Id: " + getUserId());
		logger.debug("Audit Id: " + getAuditId());
		logger.debug("Document Id: " + getDocumentId());
		logger.debug("Template: " + getTemplate());
		
    }

    public void handleException(Throwable t) {
    	logger.error("Service execution failed", t);
    	
    	auditServiceErrorEvent(t.getMessage());			
		
    	String reponseMessage = getLocalizedMessage("an.unpredicted.error.occured");
    	if(t instanceof SpagoBIEngineException) {
    		SpagoBIEngineException e = (SpagoBIEngineException)t;
    		reponseMessage = getLocalizedMessage(e.getErrorDescription());
    		  		
    	} 		
    	
    	tryToWriteBackToClient( reponseMessage );		
    }
    

    public UserProfile getUserProfile() {
		return (UserProfile) getAttributeFromHttpSession( USER_ID ); 
	}

    public String getUserId() {
    	UserProfile profile = null;
    	
    	if(userId == null) {  	
        	userId = (String)getUserProfile().getUserId();
    	}
    	
    	return userId;
    }
    
    public String getUserIdentifier() {
    	IEngUserProfile profile = null;
    	
    	if(userUniqueIdentifier == null) {	
    		userUniqueIdentifier = (String)getUserProfile().getUserUniqueIdentifier();
    	}
    	
    	return userUniqueIdentifier;
    }
    
    public String getDocumentId() {
    	String documentIdInSection = null;
        
    	if(documentId == null) {
    		documentIdInSection = getAttributeFromHttpSessionAsString( DOCUMENT_ID );
	    	logger.debug("documentId in Session:" + documentIdInSection);
	    	
	    	if( requestContainsParameter( DOCUMENT_ID ) ) {
	    		documentId = getParameterAsString( DOCUMENT_ID );
	    	} else {
	    		documentId = documentIdInSection;
	    		logger.debug("documentId has been taken from session");
	    	}
    	}
    	
    	return documentId;
    }
    
    /**
     * Gets the audit id.
     * 
     * @return the audit id
     */
    public String getAuditId() {    	
    	if(auditId == null) {
    		auditId = getParameterAsString( AUDIT_ID );
    	}
    	return auditId;
		
    }
    
    /**
     * Gets the template.
     * 
     * @return the template
     */
    public SourceBean getTemplate() {
    	if(template == null) {
    		template = getTemplate( getUserId(), getDocumentId() );
    	}
    	return template;
    }
    
    private SourceBean getTemplate(String userId, String documentId) {
		SourceBean templateSB = null;
		Content template = null;
		String templateContent = null;
		
		contentProxy = getContentServiceProxy();
		HashMap requestParameters = ParametersDecoder.getDecodedRequestParameters(getRequest());
		template = contentProxy.readTemplate(documentId, requestParameters);
		logger.debug("Read the template."+ template.getFileName());	
		
		
		try {
			templateContent = new String( DECODER.decodeBuffer(template.getContent()) );
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
    
    public Locale getLocale() {
    	String language;
		String country;
		
    	if(locale == null) {
    		
    		logger.debug("IN");
    		
    		language = getParameterAsString( LANGUAGE );
    		country = getParameterAsString( COUNTRY );
    		logger.debug("Locale parameters received: language = [" + language + "] ; country = [" + country + "]");

    		try {
    		    locale = new Locale(language, country); 
    		} catch (Exception e) {
    		    logger.debug("Error while creating Locale object from input parameters: language = [" + language
    			    + "] ; country = [" + country + "]");
    		    logger.debug("Creating default locale [en,US].");
    		    locale = new Locale("en", "US");
    		}
    		
    		logger.debug("IN");
    	}
    	
    	return locale;
    }
    
    public void auditServiceStartEvent() {
    	if(getAuditServiceProxy() != null) {
    		getAuditServiceProxy().notifyServiceStartEvent();
    	} else {
    		logger.warn("Impossible to log START-EVENT because the audit proxy has not been instatiated properly");
    	}
	}

	public void auditServiceErrorEvent(String msg) {
		if(getAuditServiceProxy() != null) {
    		getAuditServiceProxy().notifyServiceErrorEvent(msg);
    	} else {
    		logger.warn("Impossible to log ERROR-EVENT because the audit proxy has not been instatiated properly");
    	}
	}

	public void auditServiceEndEvent() {
		if(getAuditServiceProxy() != null) {
    		getAuditServiceProxy().notifyServiceEndEvent();
    	} else {
    		logger.warn("Impossible to log END-EVENT because the audit proxy has not been instatiated properly");
    	}		
	}
    
    
    public ContentServiceProxy getContentServiceProxy() {
 	   if(contentProxy == null) {
 		   contentProxy = new ContentServiceProxy(getUserIdentifier(), getHttpSession());
 	   }	   
 	    
 	   return contentProxy;
    }
    
    public AuditServiceProxy getAuditServiceProxy() {
 	   if(auditProxy == null) {
 		   auditProxy = new AuditServiceProxy(getAuditId(), getUserIdentifier(), getHttpSession());
 	   }	   
 	    
 	   return auditProxy;
    }
    
    public EventServiceProxy getEventServiceProxy() {
  	   if(eventProxy == null) {
  		 eventProxy = new EventServiceProxy(getUserIdentifier(), getHttpSession());
  	   }	   
  	    
  	   return eventProxy;
     }
    
    
    
    public Map getEnv() {
       if(eventProxy == null) {
	 	   env = new HashMap();
	 	   
	 	   copyRequestParametersIntoEnv(env);
	 	   //env.put(EngineConstants.ENV_DATASOURCE, getDataSource());
	 	   env.put(EngineConstants.ENV_DOCUMENT_ID, getDocumentId());
	 	   env.put(EngineConstants.ENV_CONTENT_SERVICE_PROXY, getContentServiceProxy());
	 	   env.put(EngineConstants.ENV_AUDIT_SERVICE_PROXY, getAuditServiceProxy() );
	 	   env.put(EngineConstants.ENV_EVENT_SERVICE_PROXY, getEventServiceProxy() );
	 	   env.put(EngineConstants.ENV_LOCALE, getLocale()); 
       }
 		
 	   return env;
    }
     
    /**
 	 * Copy request parameters into env.
 	 * 
 	 * @param env the env
 	 * @param serviceRequest the service request
 	 */
 	public void copyRequestParametersIntoEnv(Map env) {
 		Set parameterStopList = null;
 		Enumeration parameterNames = null;
 		
 		logger.debug("IN");
 		
 		parameterStopList = new HashSet();
 		parameterStopList.add("template");
 		parameterStopList.add("ACTION_NAME");
 		parameterStopList.add("NEW_SESSION");
 		parameterStopList.add("document");
 		parameterStopList.add("spagobicontext");
 		parameterStopList.add("BACK_END_SPAGOBI_CONTEXT");
 		parameterStopList.add("userId");
 		parameterStopList.add("auditId");
 		
 		
 		
 		parameterNames = getRequest().getParameterNames();
 		while( parameterNames.hasMoreElements() ) {
 			String parameterName = (String)parameterNames.nextElement();
 			Object parameterValue = this.getParameter(parameterName);
 			logger.debug("Parameter [" + parameterName + "] has been read from request");
 			logger.debug("Parameter [" + parameterName + "] is of type  " + parameterValue.getClass().getName());
 			logger.debug("Parameter [" + parameterName + "] is equal to " + parameterValue);
 			
 			if(parameterStopList.contains(parameterName)) {
 				logger.debug("Parameter [" + parameterName + "] copyed into environment parameters list: FALSE");
 				continue;
 			}
 			
 			env.put(parameterName, decodeParameterValue("" + parameterValue) );
 			logger.debug("Parameter [" + parameterName + "] copyed into environment parameters list: TRUE");
 		}

 		logger.debug("OUT");
 	}
     
    
 	/**
 	 * Decode parameter value.
 	 * 
 	 * @param parValue the par value
 	 * 
 	 * @return the string
 	 */
 	private String decodeParameterValue(String parValue) {
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
 			
 		return newParValue;
 	}
 	
 	protected String getLocalizedMessage(String msg) {
		if(msg == null) return "";
		return EngineMessageBundle.getMessage(msg, getLocale());
	}
	
	
}
