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
package it.eng.spagobi.utilities.service;



import java.util.HashMap;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.services.content.bo.Content;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.services.proxy.ContentServiceProxy;
import it.eng.spagobi.services.proxy.DataSourceServiceProxy;
import it.eng.spagobi.utilities.ParametersDecoder;

import org.apache.log4j.Logger;

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

    
	
	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
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
	
	/**
	 * Gets the document id.
	 * 
	 * @return the document id
	 */
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
	
	/**
	 * Gets the audit id.
	 * 
	 * @return the audit id
	 */
	public String getAuditId() {
		return getParameterAsString( AUDIT_ID );
	}
	
	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public SpagoBiDataSource getDataSource() {
		SpagoBiDataSource dataSource = null;
		
		DataSourceServiceProxy proxyDS = new DataSourceServiceProxy( getUserId() , getSession() );
		dataSource = proxyDS.getDataSource( getDocumentId() );
		
		return dataSource;	
	}
	
	/**
	 * Gets the row template.
	 * 
	 * @return the row template
	 */
	public Content getRowTemplate() {
		ContentServiceProxy contentProxy = new ContentServiceProxy( getUserId(), getSession());
		HashMap requestParameters = ParametersDecoder.getDecodedRequestParameters(this.getRequest());
		Content template = contentProxy.readTemplate( getDocumentId() ,requestParameters);
		
		return template;
	}
}
