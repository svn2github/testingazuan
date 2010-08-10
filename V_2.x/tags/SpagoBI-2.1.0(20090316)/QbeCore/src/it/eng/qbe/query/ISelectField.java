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

import it.eng.qbe.wizard.EntityClass;

// TODO: Auto-generated Javadoc
/**
 * The Interface ISelectField.
 */
public interface ISelectField extends IField {	
		
	/**
	 * Gets the field name without operators.
	 * 
	 * @return the field name without operators
	 */
	String getFieldNameWithoutOperators();

	/**
	 * Gets the field alias.
	 * 
	 * @return the field alias
	 */
	String getFieldAlias();	
	
	/**
	 * Sets the field alias.
	 * 
	 * @param aFieldAlias the new field alias
	 */
	void setFieldAlias(String aFieldAlias);
	
	/**
	 * Sets the field entity class.
	 * 
	 * @param ec the new field entity class
	 */
	void setFieldEntityClass(EntityClass ec);
	
	/**
	 * Gets the field entity class.
	 * 
	 * @return the field entity class
	 */
	EntityClass getFieldEntityClass();

	/**
	 * Sets the field complete name.
	 * 
	 * @param fieldCompleteName the new field complete name
	 */
	void setFieldCompleteName(String fieldCompleteName);	
	
	/**
	 * Gets the field complete name.
	 * 
	 * @return the field complete name
	 */
	String getFieldCompleteName();
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	ISelectField getCopy();

	/**
	 * Sets the visible.
	 * 
	 * @param visible the new visible
	 */
	void setVisible(boolean visible);
	
	/**
	 * Checks if is visible.
	 * 
	 * @return true, if is visible
	 */
	boolean isVisible();
}
