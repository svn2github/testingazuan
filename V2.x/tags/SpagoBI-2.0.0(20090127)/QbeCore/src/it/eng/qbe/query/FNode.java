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
package it.eng.qbe.query;


// TODO: Auto-generated Javadoc
/**
 * The Class FNode.
 */
public class FNode  {
	
	/** The class name. */
	String className;
	
	/** The field name. */
	String fieldName;
	
	/** The class prefix. */
	String classPrefix = null;
			
	/**
	 * Instantiates a new f node.
	 * 
	 * @param className the class name
	 * @param fieldName the field name
	 */
	public FNode(String className, String fieldName) {
		this.className = className;
		this.fieldName = fieldName;
		this.classPrefix = "a";
	}
	
	/**
	 * Instantiates a new f node.
	 * 
	 * @param className the class name
	 * @param fieldName the field name
	 * @param classPrefix the class prefix
	 */
	public FNode(String className, String fieldName, String classPrefix) {
		this.className = className;
		this.fieldName = fieldName;
		this.classPrefix = classPrefix;
	}
	
	/**
	 * Instantiates a new f node.
	 * 
	 * @param field the field
	 * @param classPrefix the class prefix
	 */
	public FNode(ISelectField field, String classPrefix) {
		
			this.className = field.getFieldEntityClass().getClassName();
			this.fieldName = field.getFieldNameWithoutOperators();
			if (classPrefix == null)
				this.classPrefix = "a";
			else
				this.classPrefix = classPrefix;
	
			fieldName = fieldName.substring(getClassAlias().length() + 1, fieldName.length());
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
	 * Gets the field name.
	 * 
	 * @return the field name
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * Gets the class alias.
	 * 
	 * @return the class alias
	 */
	public String getClassAlias() {
		return classPrefix + className.replace(".", "_");
	}
	
	/**
	 * Gets the field alias.
	 * 
	 * @return the field alias
	 */
	public String getFieldAlias() {
		return getClassAlias() + "." + fieldName;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public String getId() {	
		return className + ";" + fieldName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return getId().hashCode();
	}

	/**
	 * Gets the class prefix.
	 * 
	 * @return the class prefix
	 */
	public String getClassPrefix() {
		return classPrefix;
	}

	/**
	 * Sets the class prefix.
	 * 
	 * @param classPrefix the new class prefix
	 */
	public void setClassPrefix(String classPrefix) {
		this.classPrefix = classPrefix;
	}
}
