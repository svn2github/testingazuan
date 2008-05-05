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
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractEngineAction extends AbstractBaseHttpAction {
	
	
	
	/**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractEngineAction.class);
    
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.service.AbstractBaseHttpAction#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	/* (non-Javadoc)
	 * @see it.eng.spago.dispatching.service.ServiceIFace#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean request, SourceBean response) throws EngineException {
		setRequest( request );
		setResponse( response );
	}	
	
	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return getAttributeFromSessionAsString( EngineConstants.USER_ID );
	}
	
	/**
	 * Sets the user id.
	 * 
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		setAttributeInSession(EngineConstants.USER_ID,userId);
	}	
	
	/**
	 * Gets the document id.
	 * 
	 * @return the document id
	 */
	public String getDocumentId() {
		return getAttributeFromSessionAsString( EngineConstants.DOCUMENT_ID );
	}
	
	/**
	 * Sets the document id.
	 * 
	 * @param documentId the new document id
	 */
	public void setDocumentId(String documentId) {
		setAttributeInSession(EngineConstants.DOCUMENT_ID,documentId);
	}
	
	/**
	 * Gets the audit id.
	 * 
	 * @return the audit id
	 */
	public String getAuditId() {
		return getAttributeFromSessionAsString( EngineConstants.AUDIT_ID );
	}
	
	/**
	 * Sets the audit id.
	 * 
	 * @param auditId the new audit id
	 */
	public void setAuditId(String auditId) {
		setAttributeInSession(EngineConstants.AUDIT_ID,auditId);
	}
	
	/**
	 * Gets the analysis metadata.
	 * 
	 * @return the analysis metadata
	 */
	public EngineAnalysisMetadata getAnalysisMetadata() {
		return (EngineAnalysisMetadata)getAttributeFromSession( EngineConstants.ANALYSIS_METADATA );
	}
	
	/**
	 * Sets the analysis metadata.
	 * 
	 * @param analysisMetadata the new analysis metadata
	 */
	public void setAnalysisMetadata(EngineAnalysisMetadata analysisMetadata) {
		setAttributeInSession(EngineConstants.ANALYSIS_METADATA,analysisMetadata);
	}
	
	/**
	 * Gets the analysis state.
	 * 
	 * @return the analysis state
	 */
	public EngineAnalysisState getAnalysisState() {
		return (EngineAnalysisState)getAttributeFromSession( EngineConstants.ANALYSIS_STATE );
	}
	
	/**
	 * Sets the analysis state.
	 * 
	 * @param analysisState the new analysis state
	 */
	public void setAnalysisState(EngineAnalysisState analysisState) {
		setAttributeInSession(EngineConstants.ANALYSIS_STATE,analysisState);
	}
}
