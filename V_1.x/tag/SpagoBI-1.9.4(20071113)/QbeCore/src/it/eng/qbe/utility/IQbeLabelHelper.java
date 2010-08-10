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
 * @author Andrea Zoppello
 * 
 * This is the interface for classes that implements logic
 * to retrieve labels for the entities(classes) and the fields
 * of the datamart model
 * 
 */
public interface IQbeLabelHelper{

	/**
	 * @param completeClassName, the entity(class) to retrieve label
	 * @return the label for entity(class) identified by completeClassName
	 */
	public String getLabelForClass(String completeClassName);
	
	/**
	 * @param completeFieldName the fieldName to retrieve label
	 * @return the label for field identified by completeFieldName
	 */
	public String getLabelForField(String completeFieldName);

}
