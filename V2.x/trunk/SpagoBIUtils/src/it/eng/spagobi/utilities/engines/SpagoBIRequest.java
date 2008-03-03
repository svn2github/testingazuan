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
	
	
	
	
	public SpagoBIRequest(String userId, String auditId, String documentId, 
						  SpagoBiDataSource dataSource, 
						  Locale locale) {
		setUserId( userId );
		setAuditId( auditId );
		setDocumentId( documentId );
		setDataSource( dataSource );
		setLocale( locale );
	}
	

	public Locale getLocale() {
		return this.locale;
	}

	
	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public SpagoBiDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(SpagoBiDataSource dataSource) {
		this.dataSource = dataSource;
	}


	public SourceBean getRequest() {
		return request;
	}

	public void setRequest(SourceBean request) {
		this.request = request;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDocumentId() {
		return documentId;
	}

	public void setDocumentId(String documentId) {
		this.documentId = documentId;
	}

	public String getAuditId() {
		return auditId;
	}

	public void setAuditId(String auditId) {
		this.auditId = auditId;
	}
}
