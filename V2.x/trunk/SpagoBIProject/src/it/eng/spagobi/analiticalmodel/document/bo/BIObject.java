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
package it.eng.spagobi.analiticalmodel.document.bo;

import it.eng.spagobi.analiticalmodel.document.dao.IObjTemplateDAO;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.SpagoBITracer;
import it.eng.spagobi.engines.config.bo.Engine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Defines a Business Intelligence object.

 */
public class BIObject implements Serializable {

	// BIOBJ_ID NUMBER N Business Intelligence Object identifier
	private Integer id = null;

	// ENGINE_ID NUMBER N Engine idenitifier (FK)
	private Engine engine = null;
	
	// DATA_SOURCE_ID NUMBER N DataSource idenitifier (FK)
	private Integer dataSourceId = null;

	// DESCR VARCHAR2(128) Y BI Object description
	private String name = null;
	
	// DESCR VARCHAR2(128) Y BI Object description
	private String description = null;

	// LABEL VARCHAR2(36) Y Engine label (short textual identifier)
	private String label = null;

	// ENCRYPT NUMBER Y Parameter encryption request.
	private Integer encrypt = null;
	
	// VISIBLE NUMBER Y Parameter visible request.
	private Integer visible = null;

	// REL_NAME VARCHAR2(256) Y Relative path + file object name
	private String relName = null;

	// STATE_ID NUMBER N State identifier (actually not used)
	private Integer stateID = null;

	// STATE_CD VARCHAR2(18) N State code. Initially hard-coded valued, in the
	// future, managed by a states workflow with historical storage.
	private String stateCode = null;

	// BIOBJ_TYPE_ID NUMBER N Business Intelligence Object Type identifier.
	private Integer biObjectTypeID = null;

	// BIOBJ_TYPE_CD VARCHAR2(18) N Business Intelligence Object Type code (ex.
	// report, OLAP, Data mining, Dashboard). Denormalizated attribute from
	// SBI_DOMAINS.
	private String biObjectTypeCode = null;

	private List biObjectParameters = null;

	private String path = null;
	
	private String uuid = null;
	
	private List functionalities = null;
	
	// add this properties for metadata
	private String extendedDescription=null;
	private String objectve=null;
	private Short rating=null;
	private String language=null;
	private Date creationDate=null;
	private String creationUser=null;	


	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param businessObjectID
	 *            The id to set.
	 */
	public void setId(Integer businessObjectID) {
		this.id = businessObjectID;
	}

	/**
	 * @return Returns the biObjectParameters.
	 */
	public List getBiObjectParameters() {
		return biObjectParameters;
	}

	/**
	 * @param businessObjectParameters
	 *            The biObjectParameters to set.
	 */
	public void setBiObjectParameters(List businessObjectParameters) {
		this.biObjectParameters = businessObjectParameters;
	}

	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return Returns the encrypt.
	 */
	public Integer getEncrypt() {
		return encrypt;
	}

	/**
	 * @param encrypt
	 *            The encrypt to set.
	 */
	public void setEncrypt(Integer encrypt) {
		this.encrypt = encrypt;
	}

	public Integer getVisible() {
		return visible;
	}

	public void setVisible(Integer visible) {
		this.visible = visible;
	}
	
	/**
	 * @return Returns the engine.
	 */
	public Engine getEngine() {
		return engine;
	}

	/**
	 * @param engine
	 *            The engine to set.
	 */
	public void setEngine(Engine engine) {
		this.engine = engine;
	}
	
	/**
	 * @return Returns the datasource.
	 */
	public Integer getDataSourceId() {
		return dataSourceId;
	}

	/**
	 * @param datasource  The data source to set.
	 */
	public void setDataSourceId(Integer dataSourceId) {
		this.dataSourceId = dataSourceId;
	}

	
	/**
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return Returns the relName.
	 */
	public String getRelName() {
		return relName;
	}

	/**
	 * @param relName
	 *            The relName to set.
	 */
	public void setRelName(String relName) {
		this.relName = relName;
	}

	/**
	 * @return Returns the biObjectTypeCode.
	 */
	public String getBiObjectTypeCode() {
		return biObjectTypeCode;
	}

	/**
	 * @param businessObjectTypeCD
	 *            The biObjectTypeCode to set.
	 */
	public void setBiObjectTypeCode(String businessObjectTypeCD) {
		this.biObjectTypeCode = businessObjectTypeCD;
	}

	/**
	 * @return Returns the biObjectTypeID.
	 */
	public Integer getBiObjectTypeID() {
		return biObjectTypeID;
	}

	/**
	 * @param biObjectTypeID
	 *            The biObjectTypeID to set.
	 */
	public void setBiObjectTypeID(Integer biObjectTypeID) {
		this.biObjectTypeID = biObjectTypeID;
	}

	/**
	 * @return Returns the stateCode.
	 */
	public String getStateCode() {
		return stateCode;
	}

	/**
	 * @param stateCD
	 *            The stateCode to set.
	 */
	public void setStateCode(String stateCD) {
		this.stateCode = stateCD;
	}

	/**
	 * @return Returns the stateID.
	 */
	public Integer getStateID() {
		return stateID;
	}

	/**
	 * @param stateID
	 *            The stateID to set.
	 */
	public void setStateID(Integer stateID) {
		this.stateID = stateID;
	}
	/**
	 * @return Returns the path.
	 */
	public String getPath() {
		return path;
	}
	/**
	 * @param path The path to set.
	 */
	public void setPath(String path) {
		this.path = path;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public List getFunctionalities() {
		return functionalities;
	}

	public void setFunctionalities(List functionalities) {
		this.functionalities = functionalities;
	}

	
	public ObjTemplate getActiveTemplate() {
		ObjTemplate template = null;
		try{
			IObjTemplateDAO objtempdao = DAOFactory.getObjTemplateDAO();
			template = objtempdao.getBIObjectActiveTemplate(this.getId());
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
								"getActiveTemplate", "Error while recovering current template \n", e);
		}
		return template;
	}
	
	
	public List getTemplateList() {
		List templates = new ArrayList();
		try{
			IObjTemplateDAO objtempdao = DAOFactory.getObjTemplateDAO();
			templates = objtempdao.getBIObjectTemplateList(this.getId());
		} catch(Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(),
								"getTemplateList", "Error while recovering template list\n", e);
		}
		return templates;
	}

	public String getExtendedDescription() {
	    return extendedDescription;
	}

	public void setExtendedDescription(String extendedDescription) {
	    this.extendedDescription = extendedDescription;
	}

	public String getObjectve() {
	    return objectve;
	}

	public void setObjectve(String objectve) {
	    this.objectve = objectve;
	}

	public Short getRating() {
	    return rating;
	}

	public void setRating(Short rating) {
	    this.rating = rating;
	}

	public String getLanguage() {
	    return language;
	}

	public void setLanguage(String language) {
	    this.language = language;
	}

	public Date getCreationDate() {
	    return creationDate;
	}

	public void setCreationDate(Date creationDate) {
	    this.creationDate = creationDate;
	}

	public String getCreationUser() {
	    return creationUser;
	}

	public void setCreationUser(String creationUser) {
	    this.creationUser = creationUser;
	}
	

}
