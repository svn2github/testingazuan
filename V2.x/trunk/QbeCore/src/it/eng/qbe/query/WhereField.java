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

import it.eng.qbe.wizard.EntityClass;


public class WhereField extends Field implements IWhereField {
	
	
	
	private String fieldOperator = null;
	
	private String fieldValue = null;
		
	private String nextBooleanOperator = null;
	
	private EntityClass fieldEntityClassForRightCondition = null;
	
	private EntityClass fieldEntityClassForLeftCondition = null;
	
	private int rightBracketsNum;	
	private int leftBracketsNum;	

	
	
	private static String createNewId() {
		return "where_"+ String.valueOf(System.currentTimeMillis());
	}
	
	public WhereField(){	
		super( createNewId() );	
	}
	
	public IWhereField getCopy() {
		IWhereField whereField = new WhereField();
		
		whereField.setId(getId());
		whereField.setFieldName(getFieldName());
		whereField.setFieldOperator(fieldOperator);
		whereField.setFieldValue(fieldValue);
		whereField.setType(getType());
		whereField.setNextBooleanOperator(nextBooleanOperator);
		if(fieldEntityClassForRightCondition != null) whereField.setFieldEntityClassForRightCondition(fieldEntityClassForRightCondition.getCopy());
		if(fieldEntityClassForLeftCondition != null) whereField.setFieldEntityClassForLeftCondition(fieldEntityClassForLeftCondition.getCopy());
		whereField.setRightBracketsNum(rightBracketsNum);
		whereField.setLeftBracketsNum(leftBracketsNum);
		
		return whereField;
	}
	
	
	
	

	public String getFieldOperator() {
		return fieldOperator;
	}

	public void setFieldOperator(String fieldOperator) {
		this.fieldOperator = fieldOperator;
	}

	public String getFieldValue() {
		return fieldValue;
	}

	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}	

	public String getNextBooleanOperator() {
		return nextBooleanOperator;
	}

	public void setNextBooleanOperator(String nextBooleanOperator) {
		this.nextBooleanOperator = nextBooleanOperator;
	}
	public EntityClass getFieldEntityClassForLeftCondition() {
		return fieldEntityClassForLeftCondition;
	}
	public void setFieldEntityClassForLeftCondition(EntityClass fieldEntityClassForLeftCondition) {
		this.fieldEntityClassForLeftCondition = fieldEntityClassForLeftCondition;
	}
	public EntityClass getFieldEntityClassForRightCondition() {
		return fieldEntityClassForRightCondition;
	}
	public void setFieldEntityClassForRightCondition(EntityClass fieldEntityClassForRightCondition) {
		this.fieldEntityClassForRightCondition = fieldEntityClassForRightCondition;
	}
	public int getLeftBracketsNum() {
		return leftBracketsNum;
	}
	public void setLeftBracketsNum(int leftBracketsNum) {
		this.leftBracketsNum = leftBracketsNum>0? leftBracketsNum: 0;
	}
	public int getRightBracketsNum() {
		return rightBracketsNum;
	}
	public void setRightBracketsNum(int rightBracketsNum) {
		this.rightBracketsNum = rightBracketsNum>0? rightBracketsNum: 0;
	}
	
	
	
	
	

}
