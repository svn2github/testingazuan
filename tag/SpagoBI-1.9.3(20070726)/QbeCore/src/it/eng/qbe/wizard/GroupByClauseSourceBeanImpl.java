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

import it.eng.qbe.utility.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupByClauseSourceBeanImpl 
		implements IGroupByClause {

	public List groupByFields = null;
	
	public GroupByClauseSourceBeanImpl(){
		super();
		this.groupByFields = new ArrayList();
	}
	
	public IGroupByClause getCopy(){
		IGroupByClause groupByClause = new GroupByClauseSourceBeanImpl();
		
		for(int i = 0; i < groupByFields.size(); i++) {
			IOrderGroupByField orderField = (IOrderGroupByField)groupByFields.get(i);
			groupByClause.addGroupByField(orderField.getCopy());			
		}
		
		return groupByClause;
	}
	
	public List getGroupByFields() {
		return this.groupByFields;
	}

	public void setGroupByFields(List aList) {
		this.groupByFields = aList;
		
	}

	public void addGroupByField(IOrderGroupByField aOrderGroupByField) {
		this.groupByFields.add(aOrderGroupByField);	
	}

	public void delGroupByField(IOrderGroupByField aOrderGroupByField) {
		int positionOfElement = -1;
		
		IOrderGroupByField tmp = null;
		for (int i=0; i < this.groupByFields.size(); i++){
			tmp = (IOrderGroupByField)this.groupByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.groupByFields.remove(positionOfElement);
		}
		
	}
	
	public void moveUp(IOrderGroupByField aOrderGroupByField) {
		int positionOfElement = -1;
		
		IOrderGroupByField tmp = null;
		for (int i=0; i < this.groupByFields.size(); i++){
			tmp = (IOrderGroupByField)this.groupByFields.get(i);
			if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(GroupByClauseSourceBeanImpl.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IOrderGroupByField swap = (IOrderGroupByField)this.groupByFields.set(newPosition, tmp);
			this.groupByFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(IOrderGroupByField aOrderGroupByField) {
		
			int positionOfElement = -1;
			
			IOrderGroupByField tmp = null;
			for (int i=0; i < this.groupByFields.size(); i++){
				tmp = (IOrderGroupByField)this.groupByFields.get(i);
				if (tmp.getId().equalsIgnoreCase(aOrderGroupByField.getId())){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.groupByFields.size() -1){
				Logger.debug(GroupByClauseSourceBeanImpl.class,"Cannot Move Element is at last position "+ (this.groupByFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IOrderGroupByField swap = (IOrderGroupByField)this.groupByFields.set(newPosition, tmp);
				this.groupByFields.set(positionOfElement, swap);
			}
			
		
		
	}
}


