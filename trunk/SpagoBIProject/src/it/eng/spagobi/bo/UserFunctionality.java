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
import java.util.List;

/**
 * Defines a <code>UserFunctionality</code> object.
 */
public class UserFunctionality  implements Serializable  {

	private Integer id;
	
	private Integer parentId;
	
	private String code = "";

	private String name = "";

	private String description = "";

	private String path = null;
	
	private List biObjects = null;
	
	private Integer prog = null;
	
	private Role[] execRoles = null;
	
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

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List getBiObjects() {
		return biObjects;
	}

	public void setBiObjects(List biObjects) {
		this.biObjects = biObjects;
	}

	public Integer getProg() {
		return prog;
	}

	public void setProg(Integer prog) {
		this.prog = prog;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Role[] getExecRoles() {
		return execRoles;
	}

	public void setExecRoles(Role[] execRoles) {
		this.execRoles = execRoles;
	}
	
}
