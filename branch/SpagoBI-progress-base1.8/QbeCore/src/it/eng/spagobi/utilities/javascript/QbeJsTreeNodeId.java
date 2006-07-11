/**
 * 
 */
package it.eng.spagobi.utilities.javascript;

import it.eng.qbe.wizard.ISelectField;

/**
 * @author Gioia
 *
 */
public class QbeJsTreeNodeId implements IJsTreeNodeId {
	
	String className;
	String fieldName;
			
	public QbeJsTreeNodeId(String className, String fieldName) {
		this.className = className;
		this.fieldName = fieldName;
	}
	
	public QbeJsTreeNodeId(ISelectField field) {
		this.className = field.getFieldEntityClass().getClassName();
		this.fieldName = field.getFieldNameWithoutOperators();
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
			classAlias = "a" + className.substring(className.lastIndexOf(".")+1);
		}else{
			classAlias = "a" + className;
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
	
	/*
	public int equals(Object o) {
		return 
	}
	*/
}
