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
import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Interface IOrderByClause.
 */
public interface IOrderByClause extends  Serializable {	
	
	/**
	 * Gets the order by fields.
	 * 
	 * @return the order by fields
	 */
	public List getOrderByFields();
	
	/**
	 * Sets the order by fields.
	 * 
	 * @param aList the new order by fields
	 */
	public void setOrderByFields(List aList);
	
	/**
	 * Adds the order by field.
	 * 
	 * @param aOrderGroupByField the a order group by field
	 */
	public void addOrderByField(IOrderByField aOrderGroupByField);
	
	/**
	 * Delete order by field.
	 * 
	 * @param fieldId the field id
	 */
	public void deleteOrderByField(String fieldId);

	/**
	 * Move up.
	 * 
	 * @param fieldId the field id
	 */
	public void moveUp(String fieldId);
	
	/**
	 * Move down.
	 * 
	 * @param fieldId the field id
	 */
	public void moveDown(String fieldId);
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	public IOrderByClause getCopy();
}
