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
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;

import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractEngineAction extends AbstractBaseHttpAction {
	
	public static final String PUBLIC_SCOPE = "Public";
	public static final String PRIVATE_SCOPE = "Private";
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractEngineAction.class);
    
    
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	public void service(SourceBean request, SourceBean response) {
		setRequest( request );
		setResponse( response );
	}	
	
	public IEngineInstance getEngineInstance() {
    	return (IEngineInstance)getAttributeFromSession( EngineConstants.ENGINE_INSTANCE );
    }
	
	public Map getEnv() {
		return getEngineInstance().getEnv();
	}
	
	public String xsaveAnalysisState() throws SpagoBIEngineException {
		IEngineInstance engineInstance = null;
		String documentId = null;
		EngineAnalysisMetadata analysisMetadata = null;
		IEngineAnalysisState analysisState = null;
		ContentServiceProxy  contentServiceProxy = null;
		String serviceResponse= null;	
		
		engineInstance = getEngineInstance();
		analysisMetadata = engineInstance.getAnalysisMetadata();
		analysisState = engineInstance.getAnalysisState();
		
		Assert.assertNotNull(getEnv(),"ENV cannot be null in order to save the analysis state");
		Assert.assertNotNull(getEnv().get( EngineConstants.ENV_CONTENT_SERVICE_PROXY ),"ENV property " + EngineConstants.ENV_CONTENT_SERVICE_PROXY + " cannot be null in order to save the analysis state");
		Assert.assertNotNull(getEnv().get( EngineConstants.ENV_DOCUMENT_ID ),"ENV property " + EngineConstants.ENV_DOCUMENT_ID + " cannot be null in order to save the analysis state");
				
		contentServiceProxy = (ContentServiceProxy)getEnv().get( EngineConstants.ENV_CONTENT_SERVICE_PROXY );		
		documentId = (String)getEnv().get( EngineConstants.ENV_DOCUMENT_ID );
		
		serviceResponse = contentServiceProxy.saveSubObject(documentId, 
				analysisMetadata.getName(),
				analysisMetadata.getDescription(), 
				PUBLIC_SCOPE.equalsIgnoreCase(analysisMetadata.getScope())? "true": "false", 
				new String(analysisState.store()) );
		
		return serviceResponse;
	}
	
	
	public String saveAnalysisState() throws SpagoBIEngineException {
		IEngineInstance engineInstance = null;
		String documentId = null;
		EngineAnalysisMetadata analysisMetadata = null;
		IEngineAnalysisState analysisState = null;
		ContentServiceProxy  contentServiceProxy = null;
		String serviceResponse= null;
		
		
		
		engineInstance = getEngineInstance();
		analysisMetadata = engineInstance.getAnalysisMetadata();
		analysisState = engineInstance.getAnalysisState();

		if(getEnv() == null) {
			return "KO - Missing environment";
		}
		
		contentServiceProxy = (ContentServiceProxy)getEnv().get( EngineConstants.ENV_CONTENT_SERVICE_PROXY );
		if(contentServiceProxy == null) {
			return "KO - Missing content service proxy";
		}
		
		documentId = (String)getEnv().get( EngineConstants.ENV_DOCUMENT_ID );
		if(documentId == null) {
			return "KO - Missing document id";
		}
		
	    String isPublic = "false";
	    if (PUBLIC_SCOPE.equalsIgnoreCase(analysisMetadata.getScope())) 
	    	isPublic = "true";
		
		serviceResponse = contentServiceProxy.saveSubObject(documentId, 
				analysisMetadata.getName(),
				analysisMetadata.getDescription(), 
				isPublic, 
				new String(analysisState.store()) );
		
		return serviceResponse;
	}

	public Locale getLocale() {
		return  (Locale)getEnv().get(EngineConstants.ENV_LOCALE);
	}

	/**
	 * Sets the qbe engine locale.
	 * 
	 * @param locale the new qbe engine locale
	 */
	public void setLocale(Locale locale) {
		getEnv().put(EngineConstants.ENV_LOCALE, locale);
	}
	
	public AuditServiceProxy getAuditServiceProxy() {
		return (AuditServiceProxy)getEnv().get(EngineConstants.ENV_AUDIT_SERVICE_PROXY);
	}
	
	/*
	
	public String getUserId() {
		return getAttributeFromSessionAsString( EngineConstants.USER_ID );
	}
	
	
	public void setUserId(String userId) {
		setAttributeInSession(EngineConstants.USER_ID,userId);
	}	
	
	
	public String getDocumentId() {
		return getAttributeFromSessionAsString( EngineConstants.DOCUMENT_ID );
	}
	
	
	public void setDocumentId(String documentId) {
		setAttributeInSession(EngineConstants.DOCUMENT_ID,documentId);
	}
	
	
	public String getAuditId() {
		return getAttributeFromSessionAsString( EngineConstants.AUDIT_ID );
	}
	
	
	public void setAuditId(String auditId) {
		setAttributeInSession(EngineConstants.AUDIT_ID,auditId);
	}
	
	
	public EngineAnalysisMetadata getAnalysisMetadata() {
		return (EngineAnalysisMetadata)getAttributeFromSession( EngineConstants.ANALYSIS_METADATA );
	}
	
	
	public void setAnalysisMetadata(EngineAnalysisMetadata analysisMetadata) {
		setAttributeInSession(EngineConstants.ANALYSIS_METADATA,analysisMetadata);
	}
	
	
	public IEngineAnalysisState getAnalysisState() {
		return (IEngineAnalysisState)getAttributeFromSession( EngineConstants.ANALYSIS_STATE );
	}
	
	
	public void setAnalysisState(IEngineAnalysisState analysisState) {
		setAttributeInSession(EngineConstants.ANALYSIS_STATE,analysisState);
	}
	
	*/
}
