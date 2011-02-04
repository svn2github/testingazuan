/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.generator.jpamapping;

import java.util.Collections;
import java.util.List;

import it.eng.spagobi.meta.commons.JDBCTypeMapper;
import it.eng.spagobi.meta.initializer.BusinessModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
//JpaColumn è un decorator che aggiunge una serie di metodi utili a velocity per 
//estrarre informazioni da una determinata business column durante la creazione dl mapping
public class JpaColumn {
	BusinessColumn businessColumn;
	JpaTable jpaTable;
	
	/*get/set and field scopes*/
	public static final String PUBLIC_SCOPE = "public"; //$NON-NLS-1$
	public static final String PROTECTED_SCOPE = "protected"; //$NON-NLS-1$
	public static final String PRIVATE_SCOPE = "private"; //$NON-NLS-1$
	
	public JpaColumn(BusinessColumn businessColumn) {
		this.businessColumn = businessColumn;
	}
	
	public boolean isIdentifier() {
		return businessColumn.isIdentifier();
	}
	
	
	
	public boolean isColumnInRelationship() {
		List<BusinessRelationship> relationships;
		
		relationships = jpaTable.getBusinessTable().getRelationships();
		for(BusinessRelationship relationship : relationships) {
			List<BusinessColumn> columns = null; 
			if(relationship.getSourceTable().equals( jpaTable.getBusinessTable() )) {
				columns =  relationship.getSourceColumns();
			} else {
				columns =  relationship.getDestinationColumns();
			}
			
			// scann columns
			for(BusinessColumn column : columns) {
				if(column.equals(businessColumn)) return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Returns the generated bean property name for the given column.
	 * Does not return null.
	 */
	public String getPropertyName() {
		String name;
		
		name = StringUtil.tableNameToVarName(businessColumn.getPhysicalColumn().getName());
		//name = StringUtil.initUpper(name);
				
		return name;
	}
	
	/**
	 * Returns the generated field member scope, one of {@link #PUBLIC_SCOPE}|{@link #PROTECTED_SCOPE}
	 * |{@link #PRIVATE_SCOPE}.
	 * This method never returns null (defaults to private).
	 */
	public String getFieldScope() {
		return PRIVATE_SCOPE;
	}
	
	public String getSimplePropertyType()  {
		return getPropertyType().substring( getPropertyType().lastIndexOf('.')+1 );
	}
	
	/**
	 * Returns the column type.
	 * Does not return null.
	 */
	public String getPropertyType()  {
		String type;
		
		ModelProperty property = businessColumn.getProperties().get(BusinessModelDefaultPropertiesInitializer.COLUMN_DATATYPE);
		String modelType = property.getValue();
		type = JDBCTypeMapper.getJavaTypeName(modelType);
					
		return type;
	}
	
	public BusinessColumn getBusinessColumn() {
		return businessColumn;
	}
	public void setBusinessColumn(BusinessColumn businessColumn) {
		this.businessColumn = businessColumn;
	}
	public JpaTable getJpaTable() {
		return jpaTable;
	}
	public void setJpaTable(JpaTable jpaTable) {
		this.jpaTable = jpaTable;
	}
	
	
	
	
}
