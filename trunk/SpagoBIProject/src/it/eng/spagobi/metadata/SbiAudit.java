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
package it.eng.spagobi.metadata;

import java.util.Date;

public class SbiAudit implements java.io.Serializable  {

	//	Fields
	private Integer id;
	private String userName;
	private String userGroup;
	private SbiObjects sbiObject;
	private Integer documentId;
	private String documentLabel;
	private String documentName;
	private String documentType;
	private String documentState;
	private String documentParameters;
	private SbiEngines sbiEngine;
	private Integer engineId;
	private String engineLabel;
	private String engineName;
	private String engineType;
	private String engineUrl;
	private String engineDriver;
	private String engineClass;
	private Date requestTime;
	private Date executionStartTime;
	private Date executionEndTime;
	private Integer executionTime;
	private String executionState;
	private Short error;
	private String errorMessage;
	private String errorCode;
	private String executionModality;
	
    // Constructors

	/** default constructor */
    public SbiAudit() {
    }
    
    /** constructor with id */
    public SbiAudit(Integer id) {
        this.id = id;
    }
    
	public Integer getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Integer documentId) {
		this.documentId = documentId;
	}
    
	public String getDocumentLabel() {
		return documentLabel;
	}

	public void setDocumentLabel(String documentLabel) {
		this.documentLabel = documentLabel;
	}

	public String getDocumentName() {
		return documentName;
	}

	public void setDocumentName(String documentName) {
		this.documentName = documentName;
	}

	public String getDocumentParameters() {
		return documentParameters;
	}

	public void setDocumentParameters(String documentParameters) {
		this.documentParameters = documentParameters;
	}

	public String getDocumentType() {
		return documentType;
	}

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

	public String getDocumentState() {
		return documentState;
	}

	public void setDocumentState(String documentState) {
		this.documentState = documentState;
	}
	
	public String getEngineClass() {
		return engineClass;
	}

	public void setEngineClass(String engineClass) {
		this.engineClass = engineClass;
	}

	public String getEngineDriver() {
		return engineDriver;
	}

	public void setEngineDriver(String engineDriver) {
		this.engineDriver = engineDriver;
	}

	public Integer getEngineId() {
		return engineId;
	}

	public void setEngineId(Integer engineId) {
		this.engineId = engineId;
	}
	
	public String getEngineLabel() {
		return engineLabel;
	}

	public void setEngineLabel(String engineLabel) {
		this.engineLabel = engineLabel;
	}

	public String getEngineName() {
		return engineName;
	}

	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}

	public String getEngineType() {
		return engineType;
	}

	public void setEngineType(String engineType) {
		this.engineType = engineType;
	}

	public String getEngineUrl() {
		return engineUrl;
	}

	public void setEngineUrl(String engineUrl) {
		this.engineUrl = engineUrl;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Date getExecutionEndTime() {
		return executionEndTime;
	}

	public void setExecutionEndTime(Date executionEndTime) {
		this.executionEndTime = executionEndTime;
	}

	public String getExecutionModality() {
		return executionModality;
	}

	public void setExecutionModality(String executionModality) {
		this.executionModality = executionModality;
	}

	public Date getExecutionStartTime() {
		return executionStartTime;
	}

	public void setExecutionStartTime(Date executionStartTime) {
		this.executionStartTime = executionStartTime;
	}

	public String getExecutionState() {
		return executionState;
	}

	public void setExecutionState(String executionState) {
		this.executionState = executionState;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getRequestTime() {
		return requestTime;
	}

	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}

	public SbiEngines getSbiEngine() {
		return sbiEngine;
	}

	public void setSbiEngine(SbiEngines sbiEngine) {
		this.sbiEngine = sbiEngine;
	}

	public SbiObjects getSbiObject() {
		return sbiObject;
	}

	public void setSbiObject(SbiObjects sbiObject) {
		this.sbiObject = sbiObject;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Short getError() {
		return error;
	}

	public void setError(Short error) {
		this.error = error;
	}

	public Integer getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Integer executionTime) {
		this.executionTime = executionTime;
	}
	
}
