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

import it.eng.qbe.utility.CalculatedField;
import it.eng.qbe.utility.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.Sleep;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectClauseSourceBeanImpl
		implements ISelectClause {

	private List selectFields = null;
	private List calcuatedFields = null;
	
	
	public SelectClauseSourceBeanImpl() {
		
		super();
		selectFields = new ArrayList();
		
		calcuatedFields = new ArrayList();
	}

	public List getSelectFields() {
		return this.selectFields;
	}

	public void setSelectFields(List aList) {
		this.selectFields = aList;
		
	}

	public void addSelectField(ISelectField selectField) {
		this.selectFields.add(selectField);
		String selectFieldCompleteName = selectField.getFieldCompleteName();
	}

	public void delSelectField(ISelectField selectField) {
		int positionOfElement = -1;
		
		ISelectField tmp = null;
		for (int i=0; i < this.selectFields.size(); i++){
			tmp = (ISelectField)this.selectFields.get(i);
			if (tmp.getId().equalsIgnoreCase(selectField.getId())){
				positionOfElement = i;
				break;
			}
		}
		if (positionOfElement > -1){
			this.selectFields.remove(positionOfElement);
		}
		
	}

	public void moveUp(ISelectField selectField) {
		int positionOfElement = -1;
		
		ISelectField tmp = null;
		for (int i=0; i < this.selectFields.size(); i++){
			tmp = (ISelectField)this.selectFields.get(i);
			if (tmp.getId().equalsIgnoreCase(selectField.getId())){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(SelectClauseSourceBeanImpl.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			ISelectField swap = (ISelectField)this.selectFields.set(newPosition, tmp);
			this.selectFields.set(positionOfElement, swap);
		}
		
	}

	public void moveDown(ISelectField selectField) {
		
			int positionOfElement = -1;
			
			ISelectField tmp = null;
			for (int i=0; i < this.selectFields.size(); i++){
				tmp = (ISelectField)this.selectFields.get(i);
				if (tmp.getId().equalsIgnoreCase(selectField.getId())){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == this.selectFields.size() -1){
				Logger.debug(SelectClauseSourceBeanImpl.class,"Cannot Move Element is at last position "+ (this.selectFields.size() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				ISelectField swap = (ISelectField)this.selectFields.set(newPosition, tmp);
				this.selectFields.set(positionOfElement, swap);
			}	
	}

	public List getCalcuatedFields() {
		return calcuatedFields;
	}

	public void setCalcuatedFields(List calcuatedFields) {
		this.calcuatedFields = calcuatedFields;
	}

	public void addCalculatedField(CalculatedField calculatedField) {
		this.calcuatedFields.add(calculatedField);
		
	}

	public void deleteCalculatedField(String calculatedFieldID) {
		CalculatedField cField = null;
		for (int i=0; i < calcuatedFields.size(); i++){
			cField = (CalculatedField)calcuatedFields.get(i);
			if (cField.getId().equalsIgnoreCase(calculatedFieldID))
				break;
		}
		if (cField != null)
			calcuatedFields.remove(cField);
	}
	
	public ISelectClause getCopy() {
		ISelectClause selectClause = new SelectClauseSourceBeanImpl();
		
		ISelectField selectField = null;
		for (int i=0; i < this.selectFields.size(); i++){
			selectField = (ISelectField)selectFields.get(i);
			selectClause.addSelectField(selectField.getCopy());
		}
		
		CalculatedField calculatedField = null;
		for (int i=0; i < this.calcuatedFields.size(); i++){
			calculatedField = (CalculatedField)calcuatedFields.get(i);
			selectClause.addCalculatedField(calculatedField.getCopy());
		}
		
		return selectClause;
	}
}
