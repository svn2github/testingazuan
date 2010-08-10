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
package it.eng.qbe.wizard;


/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class OrderByFieldSourceBeanImpl  implements IOrderGroupByField{

	private String id = null;
	
	private String fieldName = null;
	
	boolean ascendingOrder = true;

	public OrderByFieldSourceBeanImpl(){
		
		this.id =createNewId();
		//this.id = "orderby"+ String.valueOf(System.currentTimeMillis());
	
	}
	
	public IOrderGroupByField getCopy() {
		OrderByFieldSourceBeanImpl orderGroupByField = new OrderByFieldSourceBeanImpl();
		
		orderGroupByField.setId(id);
		orderGroupByField.setFieldName(fieldName);
		orderGroupByField.setAscendingOrder(ascendingOrder);
		
		return orderGroupByField;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	// TODO generate unique id in a safer mode (i.e. without overflow risk)
	private static long idcounter = 0;
	private static String createNewId() {
		return "orderby" + String.valueOf(idcounter++);
	}

	public boolean isAscendingOrder() {
		return ascendingOrder;
	}

	public void setAscendingOrder(boolean ascendingOrder) {
		this.ascendingOrder = ascendingOrder;
	}
	
}


	

	
	
	

