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
    
	
	public void init(SourceBean config) {
        super.init(config);
    } 
	
	public void service(SourceBean request, SourceBean response) throws EngineException {
		setRequest( request );
		setResponse( response );
	}	
	
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
	
	public EngineAnalysisState getAnalysisState() {
		return (EngineAnalysisState)getAttributeFromSession( EngineConstants.ANALYSIS_STATE );
	}
	
	public void setAnalysisState(EngineAnalysisState analysisState) {
		setAttributeInSession(EngineConstants.ANALYSIS_STATE,analysisState);
	}
}
