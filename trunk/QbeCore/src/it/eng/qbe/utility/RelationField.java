/*
 * Created on 2-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.utility;

/**
 * @author Zoppello
 *
 */
public class RelationField {
	private String fieldName = null;
	private String className = null;
	private String relationOnColumnName = null;
	
	/**
	 * 
	 */
	public RelationField(String aFieldName, String className) {
		this.fieldName = aFieldName;
		this.className = className;
	
	}
	public RelationField(String aFieldName, String className, String relationOnColumnName) {
		this.fieldName = aFieldName;
		this.className = className;
		this.relationOnColumnName = relationOnColumnName;
	}
	/**
	 * @return Returns the className.
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className The className to set.
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return Returns the fieldName.
	 */
	public String getFieldName() {
		return fieldName;
	}
	/**
	 * @param fieldName The fieldName to set.
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getRelationOnColumnName() {
		return relationOnColumnName;
	}
	public void setRelationOnColumnName(String relationOnColumnName) {
		this.relationOnColumnName = relationOnColumnName;
	}
}
