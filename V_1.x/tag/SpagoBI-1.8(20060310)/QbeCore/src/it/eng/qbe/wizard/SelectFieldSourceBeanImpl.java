/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.wizard;


/**
 * @author Zoppello
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SelectFieldSourceBeanImpl implements ISelectField{

	
	private String id = null;
	private String fieldName = null;
	private String fieldAlias = null;
	private String originalFieldName = null;
	private EntityClass entityClass = null;
	
	
	public SelectFieldSourceBeanImpl(){
		this.id = "select_"+ String.valueOf(System.currentTimeMillis());
	}
	public String getFieldAlias() {
		return fieldAlias;
	}
	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
		if (this.originalFieldName == null)
			setOriginalFieldName(fieldName);
	}
	public String getOriginalFieldName() {
		return originalFieldName;
	}
	public void setOriginalFieldName(String originalFieldName) {
		this.originalFieldName = originalFieldName;
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
		
	}
	public String getFieldNameWithoutOperators() {
		return originalFieldName;
	}
	public void setFieldEntityClass(EntityClass ec) {
		this.entityClass = ec;
		
	}
	public EntityClass getFieldEntityClass() {
		// TODO Auto-generated method stub
		return this.entityClass;
	}
	
	
	

	
	
	
}


	

	
	
	

