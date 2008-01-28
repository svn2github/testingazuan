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

import it.eng.qbe.log.Logger;

import java.util.ArrayList;
import java.util.List;

public class OrderByClause implements IOrderByClause {

	private List orderByFields = null;

	
	public OrderByClause(){
		super();
		this.orderByFields = new ArrayList();
	}
	
	public List getOrderByFields() {
		return this.orderByFields;
	}

	public void setOrderByFields(List aList) {
		this.orderByFields = aList;
		
	}

	public void addOrderByField(IOrderByField aOrderGroupByField) {
		this.orderByFields.add(aOrderGroupByField);
		
	}

	public void deleteOrderByField(String fieldId) {
		int positionOfElement = -1;
		
		IOrderByField tmp = null;
		for (int i=0; i < this.orderByFields.size(); i++){
			tmp = (IOrderByField)this.orderByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(fieldId)){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.orderByFields.remove(positionOfElement);
		}
		
	}
	
	public void moveUp(String fieldId) {
		int positionOfElement = -1;
		
		IOrderByField tmp = null;
		for (int i=0; i < this.orderByFields.size(); i++){
			tmp = (IOrderByField)this.orderByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(fieldId)){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(OrderByClause.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IOrderByField swap = (IOrderByField)this.orderByFields.set(newPosition, tmp);
			this.orderByFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(String fieldId) {
		
			int positionOfElement = -1;
			
			IOrderByField tmp = null;
			for (int i=0; i < this.orderByFields.size(); i++){
				tmp = (IOrderByField)this.orderByFields.get(i);
				if (tmp.getId().equalsIgnoreCase(fieldId)){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.orderByFields.size() -1){
				Logger.debug(OrderByClause.class,"Cannot Move Element is at last position "+ (this.orderByFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IOrderByField swap = (IOrderByField)this.orderByFields.set(newPosition, tmp);
				this.orderByFields.set(positionOfElement, swap);
			}		
	}
	
	public IOrderByClause getCopy(){
		IOrderByClause orderByClause = new OrderByClause();
		
		for(int i = 0; i < orderByFields.size(); i++) {
			IOrderByField orderField = (IOrderByField)orderByFields.get(i);
			orderByClause.addOrderByField(orderField.getCopy());			
		}
		
		return orderByClause;
	}
	

	
}
