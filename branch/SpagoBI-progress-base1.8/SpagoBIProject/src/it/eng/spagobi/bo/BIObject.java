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
package it.eng.spagobi.bo;

import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

import java.io.Serializable;
import java.util.List;
import java.util.TreeMap;

/**
 * Defines a Business Intelligence object.
 * 
 * @author Zoppello This class map the SBI_OBJECT table
 */
public class BIObject implements Serializable {

	// BIOBJ_ID NUMBER N Business Intelligence Object identifier
	private Integer id = null;

	// ENGINE_ID NUMBER N Engine idenitifier (FK)
	private Engine engine = null;

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
	
	//private List templateVersions = null;
	private TreeMap templateVersions = null;
	
	private TemplateVersion currentTemplateVersion = null;
	
	private String nameCurrentTemplateVersion = null;
	
	private UploadedFile template = null;

	private String path = null;
	
	private String uuid = null;
	
	private List functionalities = null;


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
	/**
	 * @return Returns the template.
	 */
	public UploadedFile getTemplate() {
		return template;
	}
	/**
	 * @param template The template to set.
	 */
	public void setTemplate(UploadedFile template) {
		this.template = template;
	}
	
	/**
	 * Loads a Template.
	 **/
	public void loadTemplate() {
		try{
			IBIObjectCMSDAO cmsdao = DAOFactory.getBIObjectCMSDAO();
			cmsdao.fillBIObjectTemplate(this);
		} catch(Exception e) {
			SpagoBITracer.major("BiObject", this.getClass().getName(),
								"loadTemplate", "cannot load template", e);
		}
	}
	/**
	 * Gets the template version 
	 * 
	 * @return The template version to get
	 */
	public TemplateVersion getCurrentTemplateVersion() {
		return currentTemplateVersion;
	}
	/**
	 * Sets the current template version
	 * 
	 * @param currentTemplateVersion	the template version to set
	 */
	public void setCurrentTemplateVersion(TemplateVersion currentTemplateVersion) {
		this.currentTemplateVersion = currentTemplateVersion;
	}
	/**
	 * Gets the template versions list.
	 * 
	 * @return The template versions List
	 */
	
	//public List getTemplateVersions() {
	public TreeMap getTemplateVersions() {
		return templateVersions;
	}
	/**
	 * Sets the template versions list.
	 * 
	 * @param templateVersions The list to set.
	 */
	//public void setTemplateVersions(List templateVersions) {
	public void setTemplateVersions(TreeMap templateVersions) {
		this.templateVersions = templateVersions;
	}
	/**
	 * Gets the nameCurrentTeplate version.
	 * 
	 * @return the nameCurrentTeplate version.
	 */
	public String getNameCurrentTemplateVersion() {
		return nameCurrentTemplateVersion;
	}
	/**
	 * Sets the nameCurrentTeplate version.
	 * 
	 * @param nameCurrentTemplateVersion the nameCurrentTeplate version to set.
	 */
	public void setNameCurrentTemplateVersion(String nameCurrentTemplateVersion) {
		this.nameCurrentTemplateVersion = nameCurrentTemplateVersion;
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

	public class SubObjectDetail implements Serializable {
		private String path = null;
		private String name = null;
		private boolean publicVisible = false;
		private String owner = null;
		private String description = null;
		
		public SubObjectDetail(String name, String path, String owner, String descr, boolean vis) {
			this.path = path;
			this.name = name;
			this.owner = owner;
			this.publicVisible = vis;
			this.description = descr;
		}
	
		public String getName() {
			return name;
		}
	
		public String getOwner() {
			return owner;
		}
	
		public String getPath() {
			return path;
		}
	
		public boolean isPublicVisible() {
			return publicVisible;
		}

		public String getDescription() {
			return description;
		}
	}

}
