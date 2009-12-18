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
 * The Class OrderByField.
 */
public class OrderByField  extends Field implements IOrderByField {

	/** The ascending order. */
	boolean ascendingOrder = true;

	/** The idcounter. */
	private static long idcounter = 0;
	
	/**
	 * Creates the new id.
	 * 
	 * @return the string
	 */
	private static String createNewId() {
		return "orderby" + String.valueOf(idcounter++);
	}
	
	/**
	 * Instantiates a new order by field.
	 */
	public OrderByField(){		
		super( createNewId() );	
	}
		
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IOrderByField#isAscendingOrder()
	 */
	public boolean isAscendingOrder() {
		return ascendingOrder;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IOrderByField#setAscendingOrder(boolean)
	 */
	public void setAscendingOrder(boolean ascendingOrder) {
		this.ascendingOrder = ascendingOrder;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IOrderByField#getCopy()
	 */
	public IOrderByField getCopy() {
		OrderByField orderByField = new OrderByField();
		
		orderByField.setId(getId());
		orderByField.setFieldName(getFieldName());
		orderByField.setAscendingOrder(isAscendingOrder());
		
		return orderByField;
	}	
}


	

	
	
	

