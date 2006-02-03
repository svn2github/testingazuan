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
import java.util.Date;
import java.util.List;

/**
 * Defines a <code>ParameterUse</code> object. 
 * 
 * @author sulis
 */


public class ParameterUse  implements Serializable  {
	
	Integer useID;
	Integer id; // in realt� questo � par_id nella tabella
	Integer idLov;
	String name = "";
	String label = "";
	String description = "";
	
	List associatedRoles = null;
	List associatedChecks = null;
	
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
	 * @return Returns the idLov.
	 */
	public Integer getIdLov() {
		return idLov;
	}
	/**
	 * @param idLov The idLov to set.
	 */
	public void setIdLov(Integer idLov) {
		this.idLov = idLov;
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
	 * @return Returns the associatedRoles.
	 */
	public List getAssociatedRoles() {
		return associatedRoles; 
	}
	/**
	 * @param listRoles The associatedRoles to set.
	 */
	public void setAssociatedRoles(List listRoles) {
		this.associatedRoles = listRoles;
	}
	/**
	 * @return Returns the useID.
	 */
	public Integer getUseID() {
		return useID;
	}
	/**
	 * @param useID The UseID to set.
	 */
	public void setUseID(Integer useID) {
		this.useID = useID;
	}
	/**
	 * @return Returns the associatedChecks.
	 */
	public List getAssociatedChecks() {
		return associatedChecks;
	}
	/**
	 * @param associatedChecks The associatedChecks to set.
	 */
	public void setAssociatedChecks(List associatedChecks) {
		this.associatedChecks = associatedChecks;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
}