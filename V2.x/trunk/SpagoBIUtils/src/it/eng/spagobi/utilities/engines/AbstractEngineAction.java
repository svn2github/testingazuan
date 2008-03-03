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

import java.io.IOException;
import java.util.List;

import it.eng.spago.base.SourceBean;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;
import it.eng.spagobi.utilities.service.AbstractBaseHttpAction;

import org.apache.log4j.Logger;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractEngineAction extends AbstractBaseHttpAction {
	
	public static final String USER_ID = IEngUserProfile.ENG_USER_PROFILE;
	public static final String AUDIT_ID = "SPAGOBI_AUDIT_ID";
	public static final String DOCUMENT_ID = "document";
	public static final String ANALYSIS_METADATA = "ANALYSIS_METADATA";
	public static final String ANALYSIS_STATE = "ANALYSIS_STATE";
	
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
		return getAttributeFromSessionAsString( USER_ID );
	}
	
	public void setUserId(String userId) {
		setAttributeInSession(USER_ID,userId);
	}	
	
	public String getDocumentId() {
		return getAttributeFromSessionAsString( DOCUMENT_ID );
	}
	
	public void setDocumentId(String documentId) {
		setAttributeInSession(DOCUMENT_ID,documentId);
	}
	
	public String getAuditId() {
		return getAttributeFromSessionAsString( AUDIT_ID );
	}
	
	public void setAuditId(String auditId) {
		setAttributeInSession(AUDIT_ID,auditId);
	}
	
	public EngineAnalysisMetadata getAnalysisMetadata() {
		return (EngineAnalysisMetadata)getAttributeFromSession( ANALYSIS_METADATA );
	}
	
	public void setAuditId(EngineAnalysisMetadata analysisMetadata) {
		setAttributeInSession(ANALYSIS_METADATA,analysisMetadata);
	}
	
	public EngineAnalysisState getAnalysisState() {
		return (EngineAnalysisState)getAttributeFromSession( ANALYSIS_STATE );
	}
	
	public void setAuditId(EngineAnalysisState analysisState) {
		setAttributeInSession(ANALYSIS_STATE,analysisState);
	}
}
