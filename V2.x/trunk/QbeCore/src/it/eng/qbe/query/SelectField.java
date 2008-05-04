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


public class SelectField extends Field implements ISelectField {

	private String fieldAlias = null;
	private String originalFieldName = null;
	private EntityClass entityClass = null;
	private String fieldCompleteName = null;
	private boolean visible = true;

	private static long idcounter = 0;
	private static String createNewId() {
		return "select_" + String.valueOf(idcounter++);
	}
	
	public SelectField(){
		super( createNewId() );
	}
	public String getFieldAlias() {
		return fieldAlias;
	}
	
	public ISelectField getCopy() {
		ISelectField selectField = new SelectField();
		
		selectField.setId(getId());
		selectField.setFieldName(getFieldName());	
		selectField.setFieldAlias(fieldAlias);
		selectField.setFieldCompleteName(fieldCompleteName);
		if(entityClass != null) selectField.setFieldEntityClass(entityClass.getCopy());	
		selectField.setType(getType());
		selectField.setScale(getScale());
		selectField.setPrecision(getPrecision());		
		
		return selectField;
	}
	
	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}


	public void setFieldName(String fieldName) {
		super.setFieldName(fieldName);
		if (getOriginalFieldName() == null)
			setOriginalFieldName(fieldName);
	}
	public String getOriginalFieldName() {
		return originalFieldName;
	}
	public void setOriginalFieldName(String originalFieldName) {
		this.originalFieldName = originalFieldName;
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
	
	
	
	public String getFieldCompleteName() {
		return fieldCompleteName;
	}
	public void setFieldCompleteName(String fieldCompleteName) {
		this.fieldCompleteName = fieldCompleteName;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
	
	

	
	
	
}


	

	
	
	

