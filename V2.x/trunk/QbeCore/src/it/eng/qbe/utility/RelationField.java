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
package it.eng.qbe.utility;

// TODO: Auto-generated Javadoc
/**
 * The Class RelationField.
 * 
 * @author Zoppello
 */
public class RelationField {
	
	/** The field name. */
	private String fieldName = null;
	
	/** The class name. */
	private String className = null;
	
	/** The relation on column name. */
	private String relationOnColumnName = null;
	
	/**
	 * Instantiates a new relation field.
	 * 
	 * @param aFieldName the a field name
	 * @param className the class name
	 */
	public RelationField(String aFieldName, String className) {
		this.fieldName = aFieldName;
		this.className = className;
	
	}
	
	/**
	 * Instantiates a new relation field.
	 * 
	 * @param aFieldName the a field name
	 * @param className the class name
	 * @param relationOnColumnName the relation on column name
	 */
	public RelationField(String aFieldName, String className, String relationOnColumnName) {
		this.fieldName = aFieldName;
		this.className = className;
		this.relationOnColumnName = relationOnColumnName;
	}
	
	/**
	 * Gets the class name.
	 * 
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}
	
	/**
	 * Sets the class name.
	 * 
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * Gets the field name.
	 * 
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	
	/**
	 * Sets the field name.
	 * 
	 * @param fieldName The fieldName to set.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	/**
	 * Gets the relation on column name.
	 * 
	 * @return the relation on column name
	 */
	public String getRelationOnColumnName() {
		return relationOnColumnName;
	}
	
	/**
	 * Sets the relation on column name.
	 * 
	 * @param relationOnColumnName the new relation on column name
	 */
	public void setRelationOnColumnName(String relationOnColumnName) {
		this.relationOnColumnName = relationOnColumnName;
	}
}
