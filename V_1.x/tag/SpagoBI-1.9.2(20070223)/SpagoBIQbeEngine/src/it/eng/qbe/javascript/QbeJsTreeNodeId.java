/**
 * 
 */
package it.eng.qbe.javascript;

import it.eng.qbe.wizard.ISelectField;

/**
 * @author Gioia
 *
 */
public class QbeJsTreeNodeId implements IJsTreeNodeId {
	
	String className;
	String fieldName;
	String classPrefix = null;
			
	public QbeJsTreeNodeId(String className, String fieldName) {
		this.className = className;
		this.fieldName = fieldName;
		this.classPrefix = "a";
	}
	
	public QbeJsTreeNodeId(String className, String fieldName, String classPrefix) {
		this.className = className;
		this.fieldName = fieldName;
		this.classPrefix = classPrefix;
	}
	/*
	public QbeJsTreeNodeId(ISelectField field) {
		this.className = field.getFieldEntityClass().getClassName();
		this.fieldName = field.getFieldNameWithoutOperators();
		fieldName = fieldName.substring(getClassAlias().length() + 1, fieldName.length());
	}
	*/
	public QbeJsTreeNodeId(ISelectField field, String classPrefix) {
		
			this.className = field.getFieldEntityClass().getClassName();
			this.fieldName = field.getFieldNameWithoutOperators();
			if (classPrefix == null)
				this.classPrefix = "a";
			else
				this.classPrefix = classPrefix;
	
			fieldName = fieldName.substring(getClassAlias().length() + 1, fieldName.length());
	}
	
	public String getClassName() {
		return className;
	}

	public String getFieldName() {
		return fieldName;
	}
	
	public String getClassAlias() {
		String classAlias = null;
		if (className.indexOf(".") > 0){
			classAlias = classPrefix + className.substring(className.lastIndexOf(".")+1);
		}else{
			classAlias = classPrefix + className;
		}
		return classAlias;
	}
	
	public String getFieldAlias() {
		return getClassAlias() + "." + fieldName;
	}

	public String getId() {	
		return className + ";" + fieldName;
	}
	
	public int hashCode() {
		return getId().hashCode();
	}

	public String getClassPrefix() {
		return classPrefix;
	}

	public void setClassPrefix(String classPrefix) {
		this.classPrefix = classPrefix;
	}
	
	/*
	public int equals(Object o) {
		return 
	}
	*/
}
