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

import it.eng.qbe.wizard.EntityClass;


// TODO: Auto-generated Javadoc
/**
 * The Class WhereField.
 */
public class WhereField extends Field implements IWhereField {
	
	
	
	/** The field operator. */
	private String fieldOperator = null;
	
	/** The field value. */
	private String fieldValue = null;
		
	/** The next boolean operator. */
	private String nextBooleanOperator = null;
	
	/** The field entity class for right condition. */
	private EntityClass fieldEntityClassForRightCondition = null;
	
	/** The field entity class for left condition. */
	private EntityClass fieldEntityClassForLeftCondition = null;
	
	/** The right brackets num. */
	private int rightBracketsNum;	
	
	/** The left brackets num. */
	private int leftBracketsNum;	

	
	
	/**
	 * Creates the new id.
	 * 
	 * @return the string
	 */
	private static String createNewId() {
		return "where_"+ String.valueOf(System.currentTimeMillis());
	}
	
	/**
	 * Instantiates a new where field.
	 */
	public WhereField(){	
		super( createNewId() );	
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getCopy()
	 */
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
	
	
	
	

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getFieldOperator()
	 */
	public String getFieldOperator() {
		return fieldOperator;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#setFieldOperator(java.lang.String)
	 */
	public void setFieldOperator(String fieldOperator) {
		this.fieldOperator = fieldOperator;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getFieldValue()
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#setFieldValue(java.lang.String)
	 */
	public void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}	

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getNextBooleanOperator()
	 */
	public String getNextBooleanOperator() {
		return nextBooleanOperator;
	}

	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#setNextBooleanOperator(java.lang.String)
	 */
	public void setNextBooleanOperator(String nextBooleanOperator) {
		this.nextBooleanOperator = nextBooleanOperator;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getFieldEntityClassForLeftCondition()
	 */
	public EntityClass getFieldEntityClassForLeftCondition() {
		return fieldEntityClassForLeftCondition;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#setFieldEntityClassForLeftCondition(it.eng.qbe.wizard.EntityClass)
	 */
	public void setFieldEntityClassForLeftCondition(EntityClass fieldEntityClassForLeftCondition) {
		this.fieldEntityClassForLeftCondition = fieldEntityClassForLeftCondition;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getFieldEntityClassForRightCondition()
	 */
	public EntityClass getFieldEntityClassForRightCondition() {
		return fieldEntityClassForRightCondition;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#setFieldEntityClassForRightCondition(it.eng.qbe.wizard.EntityClass)
	 */
	public void setFieldEntityClassForRightCondition(EntityClass fieldEntityClassForRightCondition) {
		this.fieldEntityClassForRightCondition = fieldEntityClassForRightCondition;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getLeftBracketsNum()
	 */
	public int getLeftBracketsNum() {
		return leftBracketsNum;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#setLeftBracketsNum(int)
	 */
	public void setLeftBracketsNum(int leftBracketsNum) {
		this.leftBracketsNum = leftBracketsNum>0? leftBracketsNum: 0;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#getRightBracketsNum()
	 */
	public int getRightBracketsNum() {
		return rightBracketsNum;
	}
	
	/* (non-Javadoc)
	 * @see it.eng.qbe.query.IWhereField#setRightBracketsNum(int)
	 */
	public void setRightBracketsNum(int rightBracketsNum) {
		this.rightBracketsNum = rightBracketsNum>0? rightBracketsNum: 0;
	}
	
	
	
	
	

}
