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

import java.io.Serializable;


// TODO: Auto-generated Javadoc
/**
 * The Interface IField.
 */
public interface IField extends Serializable {
	
	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	String getId();	
	
	/**
	 * Sets the id.
	 * 
	 * @param id the new id
	 */
	void setId(String id);
	
	/**
	 * Gets the field name.
	 * 
	 * @return the field name
	 */
	String getFieldName();
	
	/**
	 * Sets the field name.
	 * 
	 * @param fieldName the new field name
	 */
	void setFieldName(String fieldName);
	
	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	String getType();
	
	/**
	 * Sets the type.
	 * 
	 * @param type the new type
	 */
	void setType(String type);

	/**
	 * Gets the scale.
	 * 
	 * @return the scale
	 */
	Integer getScale();
	
	/**
	 * Sets the scale.
	 * 
	 * @param scale the new scale
	 */
	void setScale(Integer scale);

	/**
	 * Gets the precision.
	 * 
	 * @return the precision
	 */
	Integer getPrecision();
	
	/**
	 * Sets the precision.
	 * 
	 * @param precision the new precision
	 */
	void setPrecision(Integer precision);
}
