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
package it.eng.qbe.query;


public class OrderByField  extends Field implements IOrderByField {

	boolean ascendingOrder = true;

	private static long idcounter = 0;
	private static String createNewId() {
		return "orderby" + String.valueOf(idcounter++);
	}
	
	public OrderByField(){		
		super( createNewId() );	
	}
		
	public boolean isAscendingOrder() {
		return ascendingOrder;
	}

	public void setAscendingOrder(boolean ascendingOrder) {
		this.ascendingOrder = ascendingOrder;
	}
	
	public IOrderByField getCopy() {
		OrderByField orderByField = new OrderByField();
		
		orderByField.setId(getId());
		orderByField.setFieldName(getFieldName());
		orderByField.setAscendingOrder(isAscendingOrder());
		
		return orderByField;
	}	
}


	

	
	
	

