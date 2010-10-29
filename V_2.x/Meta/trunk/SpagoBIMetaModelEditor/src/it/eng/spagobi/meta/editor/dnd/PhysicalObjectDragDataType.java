/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.editor.dnd;

/**
 * This DataType is used with the custom PhysicalObjectTransfer for drag and drop
 * 
 * @author cortella
 *
 */
public class PhysicalObjectDragDataType {
	
	private static final String TYPE_TABLE = "Table";
	private static final String TYPE_COLUMN = "Column";
	
	private String ObjectType;
	private String ObjectName;
	private String ObjectParent; //may be null
	
	public PhysicalObjectDragDataType(String ObjectType, String ObjectName, String ObjectParent){
		this.ObjectType = ObjectType;
		this.ObjectName = ObjectName;
		this.ObjectParent = ObjectParent;
	}
	
	/**
	 * @param objectType the objectType to set
	 */
	public void setObjectType(String objectType) {
		ObjectType = objectType;
	}

	/**
	 * @return the objectType
	 */
	public String getObjectType() {
		return ObjectType;
	}
	
	/**
	 * @param objectName the objectName to set
	 */
	public void setObjectName(String objectName) {
		ObjectName = objectName;
	}
	
	/**
	 * @return the objectName
	 */
	public String getObjectName() {
		return ObjectName;
	}
	
	/**
	 * @param objectParent the objectParent to set
	 */
	public void setObjectParent(String objectParent) {
		ObjectParent = objectParent;
	}
	
	/**
	 * @return the objectParent
	 */
	public String getObjectParent() {
		return ObjectParent;
	}
}
