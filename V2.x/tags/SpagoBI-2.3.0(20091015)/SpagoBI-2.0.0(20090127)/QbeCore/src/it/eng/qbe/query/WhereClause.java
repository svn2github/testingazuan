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

import it.eng.qbe.log.Logger;

import java.util.ArrayList;
import java.util.List;


// TODO: Auto-generated Javadoc
/**
 * The Class WhereClause.
 */
public class WhereClause implements IWhereClause {

	
	
	
	
	/** The where fields. */
	private List whereFields = null;
	
	
	
	/**
	 * Instantiates a new where clause.
	 */
	public WhereClause(){
		this.whereFields = new ArrayList();
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereClause#getCopy()
	 */
	public IWhereClause getCopy() {
		IWhereClause whereClause = new WhereClause();
		
		for(int i = 0; i < whereFields.size(); i++) {
			IWhereField whereField = (IWhereField)whereFields.get(i);
			whereClause.addWhereField(whereField.getCopy());			
		}
		
		return whereClause;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereClause#getWhereFields()
	 */
	public List getWhereFields() {
		return this.whereFields;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereClause#setWhereFields(java.util.List)
	 */
	public void setWhereFields(List aList) {
		this.whereFields = aList;
		
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereClause#addWhereField(it.eng.qbe.query.IWhereField)
	 */
	public void addWhereField(IWhereField whereField) {
		this.whereFields.add(whereField);	
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereClause#delWhereField(java.lang.String)
	 */
	public void delWhereField(String fieldId) {
		int positionOfElement = -1;
		
		IWhereField tmp = null;
		for (int i=0; i < this.whereFields.size(); i++){
			tmp = (IWhereField)this.whereFields.get(i);
			if (tmp.getId().equalsIgnoreCase(fieldId)){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.whereFields.remove(positionOfElement);
		}
	}
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereClause#moveUp(java.lang.String)
	 */
	public void moveUp(String fieldId) {
		int positionOfElement = -1;
		
		IWhereField tmp = null;
		for (int i=0; i < this.whereFields.size(); i++){
			tmp = (IWhereField)this.whereFields.get(i);
			if (tmp.getId().equalsIgnoreCase(fieldId)){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(WhereClause.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			IWhereField swap = (IWhereField)this.whereFields.set(newPosition, tmp);
			this.whereFields.set(positionOfElement, swap);
		}
		
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereClause#moveDown(java.lang.String)
	 */
	public void moveDown(String fieldId) {
		
			int positionOfElement = -1;
			
			IWhereField tmp = null;
			for (int i=0; i < this.whereFields.size(); i++){
				tmp = (IWhereField)this.whereFields.get(i);
				if (tmp.getId().equalsIgnoreCase(fieldId)){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.whereFields.size() -1){
				Logger.debug(WhereClause.class,"Cannot Move Element is at last position "+ (this.whereFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				IWhereField swap = (IWhereField)this.whereFields.set(newPosition, tmp);
				this.whereFields.set(positionOfElement, swap);
			}
			
		
		
	}

	
	
	
	
}
