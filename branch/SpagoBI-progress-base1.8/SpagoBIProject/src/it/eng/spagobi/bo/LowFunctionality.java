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

import it.eng.spago.security.IEngUserProfile;

/**
 * Defines a <code>LowFunctionality</code> object.
 * 
 * @author sulis
 */
public class LowFunctionality  implements Serializable  {

	private Integer id;

	private String name = "";

	private String description = "";

	private String codType = ""; // code of the type of the functionality

	private String code = ""; // code of the functionality

	private String path = null;

	private Role[] execRoles = null;

	private Role[] devRoles = null;

	private Role[] testRoles = null;

	private IEngUserProfile profile = null;
	
	/**
	 * 
	 * @return Low Functionality description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description The low functionality description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * 
	 * @return Low Functionality id
	 */
	public Integer getId() {
		return id;
	}
	/**
	 * 
	 * @param id The low functionality id to set
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return Low Functionality name
	 */
	public String getName() {
		return name;
	}
	/**
	 * 
	 * @param name The low functionality name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return Low Functionality code Type
	 */
	public String getCodType() {
		return codType;
	}
	/**
	 * 
	 * @param codType The low functionality code type to set
	 */
	public void setCodType(String codType) {
		this.codType = codType;
	}

	/**
	 * 
	 * @return Low Functionality code
	 */
	public String getCode() {
		return code;
	}
	/**
	 * 
	 * @param code The low functionality code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	
	/**
	 * constructor
	 *
	 */
	public LowFunctionality() {
		this.setCodType("LOW_FUNCT");
	}
	
	/**
	 * 
	 * @return Low Functionality develop roles
	 */
	public Role[] getDevRoles() {
		return devRoles;
	}
	/**
	 * 
	 * @param devRoles The low functionality develop roles to set
	 */
	public void setDevRoles(Role[] devRoles) {
		this.devRoles = devRoles;
	}
	/**
	 * 
	 * @return Low Functionality execution roles
	 */
	public Role[] getExecRoles() {
		return execRoles;
	}
	/**
	 * 
	 * @param execRoles The low functionality execution roles to set
	 */
	public void setExecRoles(Role[] execRoles) {
		this.execRoles = execRoles;
	}
	/**
	 * 
	 * @return Low Functionality path
	 */
	public String getPath() {
		return path;
	}
	/**
	 * 
	 * @param path The low functionality path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
	/**
	 * 
	 * @return Low Functionality test roles
	 */
	public Role[] getTestRoles() {
		return testRoles;
	}
	/**
	 * 
	 * @param testRoles The low functionality test roles to set
	 */
	public void setTestRoles(Role[] testRoles) {
		this.testRoles = testRoles;
	}

	
}
