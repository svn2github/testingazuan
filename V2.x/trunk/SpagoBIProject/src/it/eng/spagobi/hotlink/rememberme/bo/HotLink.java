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
package it.eng.spagobi.hotlink.rememberme.bo;


/**
 * This class is a bean that contains all information needed to build a hot link:
 * document identifier and other document attributes, and execution parameters.
 * 
 * @author Zerbetto (davide.zerbetto@eng.it)
 *
 */
public class HotLink {

	protected Integer objId;
	protected String documentLabel;
	protected String documentName;
	protected String documentDescription;
	protected String documentType;
	protected String parameters;
	protected String engineName;
	
	public Integer getObjId() {
		return objId;
	}
	public void setObjId(Integer objId) {
		this.objId = objId;
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
	public String getDocumentDescription() {
		return documentDescription;
	}
	public void setDocumentDescription(String documentDescription) {
		this.documentDescription = documentDescription;
	}
	public String getDocumentType() {
		return documentType;
	}
	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	public String getEngineName() {
		return engineName;
	}
	public void setEngineName(String engineName) {
		this.engineName = engineName;
	}
	
}
