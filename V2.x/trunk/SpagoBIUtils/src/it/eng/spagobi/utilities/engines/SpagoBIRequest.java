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
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBIRequest implements Serializable {
	
	
	private SourceBean request;
	
	private String userId;
	private String auditId;
	private String documentId;	
	private SpagoBiDataSource dataSource;
	private Locale locale;
	
	
	
	
	/**
	 * Instantiates a new spago bi request.
	 * 
	 * @param userId the user id
	 * @param auditId the audit id
	 * @param documentId the document id
	 * @param dataSource the data source
	 * @param locale the locale
	 */
	public SpagoBIRequest(String userId, String auditId, String documentId, 
						  SpagoBiDataSource dataSource, 
						  Locale locale) {
		setUserId( userId );
		setAuditId( auditId );
		setDocumentId( documentId );
		setDataSource( dataSource );
		setLocale( locale );
	}
	

	/**
	 * Gets the locale.
	 * 
	 * @return the locale
	 */
	public Locale getLocale() {
		return this.locale;
	}

	
	/**
	 * Sets the locale.
	 * 
	 * @param locale the new locale
	 */
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	/**
	 * Gets the data source.
	 * 
	 * @return the data source
	 */
	public SpagoBiDataSource getDataSource() {
		return dataSource;
	}

	/**
	 * Sets the data source.
	 * 
	 * @param dataSource the new data source
	 */
	public void setDataSource(SpagoBiDataSource dataSource) {
		this.dataSource = dataSource;
	}


	/**
	 * Gets the request.
	 * 
	 * @return the request
	 */
	public SourceBean getRequest() {
		return request;
	}

	/**
	 * Sets the request.
	 * 
	 * @param request the new request
	 */
	public void setRequest(SourceBean request) {
		this.request = request;
	}

	/**
	 * Gets the user id.
	 * 
	 * @return the user id
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 * 
	 * @param userId the new user id
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the document id.
	 * 
	 * @return the document id
	 */
	public String getDocumentId() {
		return documentId;
	}

	/**
	 * Sets the document id.
	 * 
	 * @param documentId the new document id
	 */
	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	/**
	 * Gets the audit id.
	 * 
	 * @return the audit id
	 */
	public String getAuditId() {
		return auditId;
	}

	/**
	 * Sets the audit id.
	 * 
	 * @param auditId the new audit id
	 */
	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
}
