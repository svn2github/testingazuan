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

public class GroupByClause  implements IGroupByClause {

	public List groupByFields = null;
	
	public GroupByClause(){
		super();
		this.groupByFields = new ArrayList();
	}
	
	public List getGroupByFields() {
		return this.groupByFields;
	}

	public void setGroupByFields(List aList) {
		this.groupByFields = aList;
		
	}

	public void addGroupByField(IGroupByField groupByField) {
		this.groupByFields.add(groupByField);	
	}

	public void deleteGroupByField(String fieldId) {
		int positionOfElement = -1;
		
		IGroupByField tmp = null;
		for (int i=0; i < this.groupByFields.size(); i++){
			tmp = (IGroupByField)this.groupByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(fieldId)){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.groupByFields.remove(positionOfElement);
		}
		
	}
	
	public void moveUp(String fieldId) {
		int positionOfElement = -1;
		
		IGroupByField tmp = null;
		for (int i=0; i < this.groupByFields.size(); i++){
			tmp = (IGroupByField)this.groupByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(fieldId)){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(GroupByClause.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IGroupByField swap = (IGroupByField)this.groupByFields.set(newPosition, tmp);
			this.groupByFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(String fieldId) {
		
			int positionOfElement = -1;
			
			IGroupByField tmp = null;
			for (int i=0; i < this.groupByFields.size(); i++){
				tmp = (IGroupByField)this.groupByFields.get(i);
				if (tmp.getId().equalsIgnoreCase(fieldId)){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.groupByFields.size() -1){
				Logger.debug(GroupByClause.class,"Cannot Move Element is at last position "+ (this.groupByFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IGroupByField swap = (IGroupByField)this.groupByFields.set(newPosition, tmp);
				this.groupByFields.set(positionOfElement, swap);
			}		
	}
	
	public IGroupByClause getCopy(){
		IGroupByClause groupByClause = new GroupByClause();
		
		for(int i = 0; i < groupByFields.size(); i++) {
			IGroupByField orderField = (IGroupByField)groupByFields.get(i);
			groupByClause.addGroupByField(orderField.getCopy());			
		}
		
		return groupByClause;
	}
}


