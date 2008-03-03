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
package it.eng.spagobi.utilities.service;



import org.apache.log4j.Logger;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractEngineStartServlet extends AbstractBaseServlet {
	
	public static final String USER_ID = IEngUserProfile.ENG_USER_PROFILE;
	public static final String AUDIT_ID = "SPAGOBI_AUDIT_ID";
	public static final String DOCUMENT_ID = "document";
	

    /**
     * Logger component
     */
    private static transient Logger logger = Logger.getLogger(AbstractEngineStartServlet.class);

    
	
	public String getUserId() {
		String userId;
		IEngUserProfile userProfile;
		
		userId =  null;
		userProfile = (IEngUserProfile) getSession().getAttribute( USER_ID );
		if( userProfile != null ) {
			userId = (String)userProfile.getUserUniqueIdentifier();
		}
		
		return userId;
	}
	
	public String getDocumentId() {
		String documentId = null;
	
		if ( requestContainsParameter( DOCUMENT_ID ) ) {
			documentId = getParameterAsString( DOCUMENT_ID );
		} else {
			logger.debug("DocumentId is not in the request so it will be taken from session");
			documentId = (String)getSession().getAttribute( DOCUMENT_ID );
		}
		
		return documentId;
	}
	
	public String getAuditId() {
		return getParameterAsString( AUDIT_ID );
	}
	
	public SpagoBiDataSource getDataSource() {
		SpagoBiDataSource dataSource = null;
		
		DataSourceServiceProxy proxyDS = new DataSourceServiceProxy( getUserId() , getSession() );
		dataSource = proxyDS.getDataSource( getDocumentId() );
		
		return dataSource;	
	}
	
	public Content getRowTemplate() {
		ContentServiceProxy contentProxy = new ContentServiceProxy( getUserId(), getSession());
		Content template = contentProxy.readTemplate( getDocumentId() );
		
		return template;
	}
}
