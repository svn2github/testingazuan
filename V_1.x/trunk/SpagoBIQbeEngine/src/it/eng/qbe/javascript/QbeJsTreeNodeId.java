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
package it.eng.qbe.javascript;

import it.eng.qbe.wizard.ISelectField;

/**
 * @author Gioia
 *
 */
public class QbeJsTreeNodeId implements IJsTreeNodeId {
	
	String className;
	String fieldName;
	String classPrefix = null;
			
	public QbeJsTreeNodeId(String className, String fieldName) {
		this.className = className;
		this.fieldName = fieldName;
		this.classPrefix = "a";
	}
	
	public QbeJsTreeNodeId(String className, String fieldName, String classPrefix) {
		this.className = className;
		this.fieldName = fieldName;
		this.classPrefix = classPrefix;
	}
	
	public QbeJsTreeNodeId(ISelectField field, String classPrefix) {
		
			this.className = field.getFieldEntityClass().getClassName();
			this.fieldName = field.getFieldNameWithoutOperators();
			if (classPrefix == null)
				this.classPrefix = "a";
			else
				this.classPrefix = classPrefix;
	
			fieldName = fieldName.substring(getClassAlias().length() + 1, fieldName.length());
	}
	
	public String getClassName() {
		return className;
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public String getClassAlias() {
		/*
		String classAlias = null;
		if (className.indexOf(".") > 0){
			classAlias = classPrefix + className.substring(className.lastIndexOf(".")+1);
		}else{
			classAlias = classPrefix + className;
		}
		classAlias;
		*/
		return classPrefix + className.replace(".", "_");
	}
	
	public String getFieldAlias() {
		return getClassAlias() + "." + fieldName;
	}

	public String getId() {	
		return className + ";" + fieldName;
	}
	
	public int hashCode() {
		return getId().hashCode();
	}

	public String getClassPrefix() {
		return classPrefix;
	}

	public void setClassPrefix(String classPrefix) {
		this.classPrefix = classPrefix;
	}
	
	/*
	public int equals(Object o) {
		return 
	}
	*/
}
