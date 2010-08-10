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
 * The Interface IWhereField.
 */
public interface IWhereField extends IField{

	/**
	 * Gets the field operator.
	 * 
	 * @return the field operator
	 */
	String getFieldOperator();
	
	/**
	 * Sets the field operator.
	 * 
	 * @param fieldOperator the new field operator
	 */
	void setFieldOperator(String fieldOperator);
	
	/**
	 * Gets the field value.
	 * 
	 * @return the field value
	 */
	String getFieldValue();	
	
	/**
	 * Sets the field value.
	 * 
	 * @param fieldValue the new field value
	 */
	void setFieldValue(String fieldValue);
	
	/**
	 * Gets the next boolean operator.
	 * 
	 * @return the next boolean operator
	 */
	String getNextBooleanOperator();	
	
	/**
	 * Sets the next boolean operator.
	 * 
	 * @param nextBooleanOperator the new next boolean operator
	 */
	void setNextBooleanOperator(String nextBooleanOperator);
	
	/**
	 * Sets the field entity class for right condition.
	 * 
	 * @param ec the new field entity class for right condition
	 */
	void setFieldEntityClassForRightCondition(EntityClass ec);
	
	/**
	 * Gets the field entity class for right condition.
	 * 
	 * @return the field entity class for right condition
	 */
	EntityClass getFieldEntityClassForRightCondition();
	
	/**
	 * Sets the field entity class for left condition.
	 * 
	 * @param ec the new field entity class for left condition
	 */
	void setFieldEntityClassForLeftCondition(EntityClass ec);	
	
	/**
	 * Gets the field entity class for left condition.
	 * 
	 * @return the field entity class for left condition
	 */
	EntityClass getFieldEntityClassForLeftCondition();	
	
	/**
	 * Gets the left brackets num.
	 * 
	 * @return the left brackets num
	 */
	int getLeftBracketsNum() ;
	
	/**
	 * Sets the left brackets num.
	 * 
	 * @param leftBracketsNum the new left brackets num
	 */
	void setLeftBracketsNum(int leftBracketsNum);
	
	/**
	 * Gets the right brackets num.
	 * 
	 * @return the right brackets num
	 */
	int getRightBracketsNum();
	
	/**
	 * Sets the right brackets num.
	 * 
	 * @param rightBracketsNum the new right brackets num
	 */
	void setRightBracketsNum(int rightBracketsNum);
	
	/**
	 * Gets the copy.
	 * 
	 * @return the copy
	 */
	IWhereField getCopy();
}
