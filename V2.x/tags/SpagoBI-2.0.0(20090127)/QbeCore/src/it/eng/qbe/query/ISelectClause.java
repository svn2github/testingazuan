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

import it.eng.qbe.utility.CalculatedField;

import java.io.Serializable;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface ISelectClause.
 */
public interface ISelectClause extends  Serializable {
	
	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	boolean isEmpty();
	
	/**
	 * Gets the select fields.
	 * 
	 * @return the select fields
	 */
	List getSelectFields();	
	
	/**
	 * Gets the calcuated fields.
	 * 
	 * @return the calcuated fields
	 */
	List getCalcuatedFields();
	

	/**
	 * Adds the select field.
	 * 
	 * @param selectField the select field
	 */
	void addSelectField(ISelectField selectField);	
	
	/**
	 * Adds the calculated field.
	 * 
	 * @param calculatedField the calculated field
	 */
	void addCalculatedField(CalculatedField calculatedField);
	
	/**
	 * Del select field.
	 * 
	 * @param fieldId the field id
	 */
	void delSelectField(String fieldId);	
	
	/**
	 * Delete select fields.
	 */
	void deleteSelectFields();
	
	/**
	 * Delete calculated field.
	 * 
	 * @param calculatedField the calculated field
	 */
	void deleteCalculatedField(String calculatedField);	
	
	/**
	 * Delete calculated fields.
	 */
	void deleteCalculatedFields();
	
	/**
	 * Delete all fields.
	 */
	void deleteAllFields();		
	
	/**
	 * Sets the select fields.
	 * 
	 * @param aList the new select fields
	 */
	void setSelectFields(List aList);
	
	
	/**
	 * Move up.
	 * 
	 * @param fieldId the field id
	 */
	void moveUp(String fieldId);
	
	/**
	 * Move down.
	 * 
	 * @param fieldId the field id
	 */
	void moveDown(String fieldId);
	
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	ISelectClause getCopy();	
}
