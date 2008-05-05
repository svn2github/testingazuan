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
package it.eng.spagobi.qbe.commons.service;

import it.eng.qbe.conf.QbeTemplate;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

import java.io.Serializable;
import java.util.Locale;

// TODO: Auto-generated Javadoc
/**
 * The Class SpagoBIRequest.
 * 
 * @author Andrea Gioia
 */
public class SpagoBIRequest implements Serializable {
	
	
	/** The request. */
	private SourceBean request;
	
	/** The user id. */
	private String userId;
	
	/** The audit id. */
	private String auditId;
	
	/** The document id. */
	private String documentId;	
	
	/** The query id. */
	private String queryId;
	
	/** The locale. */
	private Locale locale;
	
	/** The template. */
	private QbeTemplate template;
	
	/** The data source. */
	private SpagoBiDataSource dataSource;
	
	
	
	/**
	 * Instantiates a new spago bi request.
	 * 
	 * @param request the request
	 */
	public SpagoBIRequest(SourceBean request) {
		this.request = request;
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
	 * Gets the template.
	 * 
	 * @return the template
	 */
	public QbeTemplate getTemplate() {
		return template;
	}

	/**
	 * Sets the template.
	 * 
	 * @param template the new template
	 */
	public void setTemplate(QbeTemplate template) {
		this.template = template;
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

	/**
	 * Gets the query id.
	 * 
	 * @return the query id
	 */
	public String getQueryId() {
		return queryId;
	}

	/**
	 * Sets the query id.
	 * 
	 * @param queryId the new query id
	 */
	public void setQueryId(String queryId) {
		this.queryId = queryId;
	}
}
