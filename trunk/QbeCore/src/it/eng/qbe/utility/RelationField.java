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
package it.eng.qbe.utility;

/**
 * @author Zoppello
 *
 */
public class RelationField {
	private String fieldName = null;
	private String className = null;
	private String relationOnColumnName = null;
	
	/**
	 * 
	 */
	public RelationField(String aFieldName, String className) {
		this.fieldName = aFieldName;
		this.className = className;
	
	}
	public RelationField(String aFieldName, String className, String relationOnColumnName) {
		this.fieldName = aFieldName;
		this.className = className;
		this.relationOnColumnName = relationOnColumnName;
	}
	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName The fieldName to set.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRelationOnColumnName() {
		return relationOnColumnName;
	}
	public void setRelationOnColumnName(String relationOnColumnName) {
		this.relationOnColumnName = relationOnColumnName;
	}
}
