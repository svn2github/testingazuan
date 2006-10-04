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
 * Defines a <code>Role</code> object. 
 * 
 * @author sulis
 */


public class Role  implements Serializable  {
	
	private Integer id;
	private String name = "";
	private String description = null;
	private String roleTypeCD = null;
	private String code = null;
	private Integer roleTypeID;

	/**
	 * Class constructor
	 *
	 */
	public Role() {
		super();
	
	}
	/**
	 * Constructor
	 * 
	 * @param name
	 * @param description
	 */
	public Role(String name, String description) {
		super();
		this.name = name;
		this.description = description;
	}

	/**
	 * 
	 * @return role description
	 */
	
	public String getDescription() {
		return description;
	}
	/**
	 * 
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 
	 * @return role id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 
	 * @param id the role id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}
	/**
	 * 
	 * @return the role name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return Returns the roleTypeCD.
	 */
	public String getRoleTypeCD() {
		return roleTypeCD;
	}
	/**
	 * @param roleTypeCD The roleTypeCD to set.
	 */
	public void setRoleTypeCD(String roleTypeCD) {
		this.roleTypeCD = roleTypeCD;
	}
	/**
	 * @return Returns the roleTypeID.
	 */
	public Integer getRoleTypeID() {
		return roleTypeID;
	}
	/**
	 * @param roleTypeID The roleTypeID to set.
	 */
	public void setRoleTypeID(Integer roleTypeID) {
		this.roleTypeID = roleTypeID;
	}
	/**
	 * @return Returns the code.
	 */
	public String getCode() {
		return code;
	}
	/**
	 * @param code The code to set.
	 */
	public void setCode(String code) {
		this.code = code;
	}
	
	
}
