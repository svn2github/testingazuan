/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.wizard;

import java.io.Serializable;


// TODO: Auto-generated Javadoc
/**
 * The Class EntityClass.
 */
public class EntityClass implements Serializable {

	/** The class name. */
	private String className = null;
	
	/** The class alias. */
	private String classAlias= null;

	/**
	 * Instantiates a new entity class.
	 */
	public EntityClass(){
		
	}
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	public EntityClass getCopy() {
		return new EntityClass(className, classAlias);
	}
	
	/**
	 * Instantiates a new entity class.
	 * 
	 * @param name the name
	 * @param alias the alias
	 */
	public EntityClass(String name,String alias) {
		super();
		classAlias = alias;
		className = name;
	}

	/**
	 * Gets the class alias.
	 * 
	 * @return the class alias
	 */
	public String getClassAlias() {
		return classAlias;
	}

	/**
	 * Sets the class alias.
	 * 
	 * @param classAlias the new class alias
	 */
	public void setClassAlias(String classAlias) {
		this.classAlias = classAlias;
	}

	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the class name.
	 * 
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	
}
