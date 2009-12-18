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
import it.eng.qbe.utility.CalculatedField;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class SelectClause.
 */
public class SelectClause implements ISelectClause {

	/** The select fields. */
	private List selectFields = null;
	
	/** The calcuated fields. */
	private List calcuatedFields = null;
	
	
	/**
	 * Instantiates a new select clause.
	 */
	public SelectClause() {		
		super();
		setSelectFields( new ArrayList() );
		setCalcuatedFields( new ArrayList() );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#getSelectFields()
	 */
	public List getSelectFields() {
		return this.selectFields;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#setSelectFields(java.util.List)
	 */
	public void setSelectFields(List aList) {
		this.selectFields = aList;		
	}
	
	/**
	 * Gets the select fields number.
	 * 
	 * @return the select fields number
	 */
	public int getSelectFieldsNumber() {
		return getSelectFields().size();
	}	
	
	/**
	 * Gets the select field by position.
	 * 
	 * @param n the n
	 * 
	 * @return the select field by position
	 */
	public ISelectField getSelectFieldByPosition(int n) {
		return (ISelectField)getSelectFields().get(n);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#getCalcuatedFields()
	 */
	public List getCalcuatedFields() {
		return calcuatedFields;
	}
	
	/**
	 * Sets the calcuated fields.
	 * 
	 * @param calcuatedFields the new calcuated fields
	 */
	public void setCalcuatedFields(List calcuatedFields) {
		this.calcuatedFields = calcuatedFields;
	}	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#addSelectField(it.eng.qbe.query.ISelectField)
	 */
	public void addSelectField(ISelectField selectField) {
		getSelectFields().add(selectField);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#delSelectField(java.lang.String)
	 */
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
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#moveUp(java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#moveDown(java.lang.String)
	 */
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

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#addCalculatedField(it.eng.qbe.utility.CalculatedField)
	 */
	public void addCalculatedField(CalculatedField calculatedField) {
		getCalcuatedFields().add(calculatedField);		
	}
	
	/**
	 * Gets the calculated fields number.
	 * 
	 * @return the calculated fields number
	 */
	public int getCalculatedFieldsNumber() {
		return getSelectFields().size();
	}
	
	/**
	 * Gets the calculated field by position.
	 * 
	 * @param n the n
	 * 
	 * @return the calculated field by position
	 */
	public CalculatedField getCalculatedFieldByPosition(int n) {
		return (CalculatedField)getCalcuatedFields().get(n);
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#deleteCalculatedField(java.lang.String)
	 */
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
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#isEmpty()
	 */
	public boolean isEmpty() {
		return ( (getSelectFieldsNumber() + getCalculatedFieldsNumber()) == 0 );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#deleteCalculatedFields()
	 */
	public void deleteCalculatedFields() {
		setCalcuatedFields( new ArrayList() );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#deleteSelectFields()
	 */
	public void deleteSelectFields() {
		setSelectFields( new ArrayList() );
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#deleteAllFields()
	 */
	public void deleteAllFields() {
		deleteSelectFields();
		deleteCalculatedFields();
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.ISelectClause#getCopy()
	 */
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
