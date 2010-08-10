/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;


/**
 * @author Zoppello
 */
public class WhereFieldSourceBeanImpl 
		implements IWhereField {

	private String id = null;

	private String fieldName = null;
	
	private String fieldOperator = null;
	
	private String fieldValue = null;
	
	private String hibernateType = null;
	
	private String nextBooleanOperator = null;
	
	private EntityClass fieldEntityClassForRightCondition = null;
	
	private EntityClass fieldEntityClassForLeftCondition = null;
	
	private int rightBracketsNum;	
	private int leftBracketsNum;	

	public WhereFieldSourceBeanImpl(){
		
		this.id = "where_"+ String.valueOf(System.currentTimeMillis());
	
	}
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

	public String getHibernateType() {
		return hibernateType;
	}

	public void setHibernateType(String hibernateType) {
		this.hibernateType = hibernateType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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
