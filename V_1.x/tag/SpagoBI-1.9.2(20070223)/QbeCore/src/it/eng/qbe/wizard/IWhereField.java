/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;

import java.io.Serializable;

/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface IWhereField extends Serializable{
	
	public String getId();
	
	public void setId(String id);
	
	public String getFieldName();

	public String getFieldOperator();
	
	public String getFieldValue();
	
	public String getHibernateType();
	
	public void setHibernateType(String hibernateType);
	
	public void setFieldName(String fieldName);

	public void setFieldOperator(String fieldOperator);
	
	public void setFieldValue(String fieldValue);
	
	public String getNextBooleanOperator();
	
	public void setNextBooleanOperator(String nextBooleanOperator);
	
	public void setFieldEntityClassForRightCondition(EntityClass ec);
	
	public void setFieldEntityClassForLeftCondition(EntityClass ec);
	
	public EntityClass getFieldEntityClassForRightCondition();
	
	public EntityClass getFieldEntityClassForLeftCondition();
	
	public int getLeftBracketsNum() ;
	
	public void setLeftBracketsNum(int leftBracketsNum);
	
	public int getRightBracketsNum();
	
	public void setRightBracketsNum(int rightBracketsNum);
}
