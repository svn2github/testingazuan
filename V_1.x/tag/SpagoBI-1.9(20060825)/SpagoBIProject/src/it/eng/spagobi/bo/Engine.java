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

import java.io.Serializable;



/**
 * Defines an <code>engine</code> object
 * 
 * @author sulis
 *
 */

public class Engine implements Serializable {
	
	private Integer id;
	private Integer criptable; 
	private String name = "";
	private String description = "";
	private String url = "";
	private String secondaryUrl = "";
	private String dirUpload = "";
	private String dirUsable = "";
	private String driverName = "";	
	private String label = "";
	private String className = "";
	private Integer biobjTypeId;
	private Integer engineTypeId;

	/**
	 * @return Returns the criptable.
	 */
	public Integer getCriptable() {
		return criptable;
	}
	/**
	 * @param criptable The criptable to set.
	 */
	public void setCriptable(Integer criptable) {
		this.criptable = criptable;
	}
	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the dirUpload.
	 */
	public String getDirUpload() {
		return dirUpload;
	}
	/**
	 * @param dirUpload The dirUpload to set.
	 */
	public void setDirUpload(String dirUpload) {
		this.dirUpload = dirUpload;
	}
	/**
	 * @return Returns the dirUsable.
	 */
	public String getDirUsable() {
		return dirUsable;
	}
	/**
	 * @param dirUsable The dirUsable to set.
	 */
	public void setDirUsable(String dirUsable) {
		this.dirUsable = dirUsable;
	}
	/**
	 * @return Returns the driverName.
	 */
	public String getDriverName() {
		return driverName;
	}
	/**
	 * @param driverName The driverName to set.
	 */
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	/**
	 * @return Returns the id.
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the secondaryUrl.
	 */
	public String getSecondaryUrl() {
		return secondaryUrl;
	}
	/**
	 * @param secondaryUrl The secondaryUrl to set.
	 */
	public void setSecondaryUrl(String secondaryUrl) {
		this.secondaryUrl = secondaryUrl;
	}
	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}
	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Integer getEngineTypeId() {
		return engineTypeId;
	}
	public void setEngineTypeId(Integer engineTypeId) {
		this.engineTypeId = engineTypeId;
	}
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Integer getBiobjTypeId() {
		return biobjTypeId;
	}
	public void setBiobjTypeId(Integer biobjTypeId) {
		this.biobjTypeId = biobjTypeId;
	}
}
