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
import it.eng.qbe.utility.CalculatedField;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tools.ant.taskdefs.Sleep;

public class SelectClause implements ISelectClause {

	private List selectFields = null;
	private List calcuatedFields = null;
	
	
	public SelectClause() {		
		super();
		setSelectFields( new ArrayList() );
		setCalcuatedFields( new ArrayList() );
	}
	
	public List getSelectFields() {
		return this.selectFields;
	}
	
	public void setSelectFields(List aList) {
		this.selectFields = aList;		
	}
	
	public int getSelectFieldsNumber() {
		return getSelectFields().size();
	}	
	
	public ISelectField getSelectFieldByPosition(int n) {
		return (ISelectField)getSelectFields().get(n);
	}
	
	public List getCalcuatedFields() {
		return calcuatedFields;
	}
	
	public void setCalcuatedFields(List calcuatedFields) {
		this.calcuatedFields = calcuatedFields;
	}	
	
	public void addSelectField(ISelectField selectField) {
		getSelectFields().add(selectField);
	}
	
	public void delSelectField(String fieldId) {
		int positionOfElement = -1;
		
		ISelectField field = null;
		for (int i=0; i < getSelectFieldsNumber(); i++){
			field = getSelectFieldByPosition(i);
			if (field.getId().equalsIgnoreCase(fieldId)){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement > -1){
			getSelectFields().remove(positionOfElement);
		}		
	}	
	
	public void moveUp(String fieldId) {
		int positionOfElement = -1;
		
		ISelectField field = null;
		for (int i=0; i < getSelectFieldsNumber(); i++){
			field = getSelectFieldByPosition(i);
			if (field.getId().equalsIgnoreCase( fieldId )){
				positionOfElement = i;
				break;
			}
		}
		
		if (positionOfElement == 0){
			Logger.debug(SelectClause.class,"Cannot Move Up Position is 0");
		}else{
			int newPosition = positionOfElement - 1;
			
			ISelectField swap = (ISelectField)getSelectFields().set(newPosition, field);
			getSelectFields().set(positionOfElement, swap);
		}		
	}

	public void moveDown(String fieldId) {		
			int positionOfElement = -1;
			
			ISelectField field = null;
			for (int i=0; i < getSelectFieldsNumber(); i++){
				field = getSelectFieldByPosition(i);
				if (field.getId().equalsIgnoreCase( fieldId )){
					positionOfElement = i;
					break;
				}
			}
			
			if (positionOfElement == getSelectFieldsNumber() - 1){
				Logger.debug(SelectClause.class,"Cannot Move Element is at last position "+ (getSelectFieldsNumber() - 1));
			}else{
				int newPosition = positionOfElement + 1;
				
				ISelectField swap = (ISelectField)getSelectFields().set(newPosition, field);
				getSelectFields().set(positionOfElement, swap);
			}	
	}	

	public void addCalculatedField(CalculatedField calculatedField) {
		getCalcuatedFields().add(calculatedField);		
	}
	
	public int getCalculatedFieldsNumber() {
		return getSelectFields().size();
	}
	
	public CalculatedField getCalculatedFieldByPosition(int n) {
		return (CalculatedField)getCalcuatedFields().get(n);
	}
	
	public void deleteCalculatedField(String calculatedFieldId) {
		CalculatedField calculatedField = null;
		for (int i=0; i < getCalculatedFieldsNumber(); i++){
			calculatedField = getCalculatedFieldByPosition(i);
			if (calculatedField.getId().equalsIgnoreCase(calculatedFieldId))
				break;
		}
		if (calculatedField != null) {
			getCalcuatedFields().remove(calculatedField);
		}
	}
	
	
	public boolean isEmpty() {
		return ( (getSelectFieldsNumber() + getCalculatedFieldsNumber()) == 0 );
	}
	
	public void deleteCalculatedFields() {
		setCalcuatedFields( new ArrayList() );
	}
	
	public void deleteSelectFields() {
		setSelectFields( new ArrayList() );
	}
	
	public void deleteAllFields() {
		deleteSelectFields();
		deleteCalculatedFields();
	}
	
	
	
	
	public ISelectClause getCopy() {
		ISelectClause selectClause = new SelectClause();
		
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
