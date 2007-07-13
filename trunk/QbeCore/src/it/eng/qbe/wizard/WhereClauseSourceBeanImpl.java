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
public class WhereClauseSourceBeanImpl implements IWhereClause {

	
	
	
	
	private List whereFields = null;
	
	
	
	public WhereClauseSourceBeanImpl(){
		this.whereFields = new ArrayList();
	}

	public IWhereClause getCopy() {
		IWhereClause whereClause = new WhereClauseSourceBeanImpl();
		
		for(int i = 0; i < whereFields.size(); i++) {
			IWhereField whereField = (IWhereField)whereFields.get(i);
			whereClause.addWhereField(whereField.getCopy());			
		}
		
		return whereClause;
	}
	
	public List getWhereFields() {
		return this.whereFields;
	}

	public void setWhereFields(List aList) {
		this.whereFields = aList;
		
	}

	public void addWhereField(IWhereField whereField) {
		this.whereFields.add(whereField);	
	}
	
	public void delWhereField(IWhereField whereField) {
		int positionOfElement = -1;
		
		IWhereField tmp = null;
		for (int i=0; i < this.whereFields.size(); i++){
			tmp = (IWhereField)this.whereFields.get(i);
			if (tmp.getId().equalsIgnoreCase(whereField.getId())){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.whereFields.remove(positionOfElement);
		}
	}
	
	
	public void moveUp(IWhereField whereField) {
		int positionOfElement = -1;
		
		IWhereField tmp = null;
		for (int i=0; i < this.whereFields.size(); i++){
			tmp = (IWhereField)this.whereFields.get(i);
			if (tmp.getId().equalsIgnoreCase(whereField.getId())){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(WhereClauseSourceBeanImpl.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IWhereField swap = (IWhereField)this.whereFields.set(newPosition, tmp);
			this.whereFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(IWhereField whereField) {
		
			int positionOfElement = -1;
			
			IWhereField tmp = null;
			for (int i=0; i < this.whereFields.size(); i++){
				tmp = (IWhereField)this.whereFields.get(i);
				if (tmp.getId().equalsIgnoreCase(whereField.getId())){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.whereFields.size() -1){
				Logger.debug(WhereClauseSourceBeanImpl.class,"Cannot Move Element is at last position "+ (this.whereFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IWhereField swap = (IWhereField)this.whereFields.set(newPosition, tmp);
				this.whereFields.set(positionOfElement, swap);
			}
			
		
		
	}

	
	
	
	
}
