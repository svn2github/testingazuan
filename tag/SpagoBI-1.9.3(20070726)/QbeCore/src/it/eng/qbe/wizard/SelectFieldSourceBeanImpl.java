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
	private String fieldCompleteName = null;
	private String hibType = null;
	private String scale = null;
	private String precision = null;
	
	public SelectFieldSourceBeanImpl(){
		this.id = createNewId();
	}
	public String getFieldAlias() {
		return fieldAlias;
	}
	
	public ISelectField getCopy() {
		ISelectField selectField = new SelectFieldSourceBeanImpl();
		
		selectField.setId(id);
		selectField.setFieldName(fieldName);	
		selectField.setFieldAlias(fieldAlias);
		selectField.setFieldCompleteName(fieldCompleteName);
		if(entityClass != null) selectField.setFieldEntityClass(entityClass.getCopy());	
		selectField.setHibType(hibType);
		selectField.setScale(scale);
		selectField.setPrecision(precision);		
		
		return selectField;
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
	
	// TODO generate unique id in a safer mode (i.e. without overflow risk)
	private static long idcounter = 0;
	private static String createNewId() {
		return "select_" + String.valueOf(idcounter++);
	}
	public String getFieldCompleteName() {
		return fieldCompleteName;
	}
	public void setFieldCompleteName(String fieldCompleteName) {
		this.fieldCompleteName = fieldCompleteName;
	}
	public String getHibType() {
		return hibType;
	}
	public String getPrecision() {
		return precision;
	}
	public String getScale() {
		return scale;
	}
	public void setHibType(String type) {
		this.hibType = type;
		
	}
	public void setPrecision(String precision) {
		this.precision = precision;
		
	}
	public void setScale(String scale) {
		this.scale = scale;
		
	}
	

	
	
	
}


	

	
	
	

