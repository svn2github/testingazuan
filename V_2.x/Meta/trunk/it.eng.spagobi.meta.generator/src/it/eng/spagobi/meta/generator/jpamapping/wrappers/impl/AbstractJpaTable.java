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
package it.eng.spagobi.meta.generator.jpamapping.wrappers.impl;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaRelationship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.JpaProperties;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.util.JDBCTypeMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class extended by <code>JpaTable</code> and <code>JpaViewInnerTable</code>
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public abstract class AbstractJpaTable implements IJpaTable{
	
	PhysicalTable physicalTable;

	// cache
	List<IJpaColumn> jpaColumns;
	HashMap<String, String> columnTypesMap;
	
	private static Logger logger = LoggerFactory.getLogger(AbstractJpaTable.class);
	
	public AbstractJpaTable(PhysicalTable physicalTable) {
		Assert.assertNotNull("Parameter [physicalTable] cannot be null", physicalTable);
		this.physicalTable = physicalTable;
	}	
	
	/**
	 * build the hashmap that contains all the types of the members 
	 * contained in the java class that map this table
	 */
	protected void initColumnTypesMap() {
		
		ModelProperty columnDataTypeProperty;
		String columnDataType;
		String columnJavaDataType;
		List<BusinessColumn> businessColumns;
		
		if(columnTypesMap != null) return;
		
		columnTypesMap = new HashMap<String, String>();
		
		businessColumns = getBusinessColumns();
		for (BusinessColumn column :businessColumns) {
			columnDataTypeProperty = column.getProperties().get(JpaProperties.COLUMN_DATATYPE);
			columnDataType = columnDataTypeProperty.getValue();
			columnJavaDataType = JDBCTypeMapper.getJavaTypeName(columnDataType);
			if ( !columnJavaDataType.startsWith("java.lang") && columnJavaDataType.indexOf('.') > 0) {
				String simpleJavaType = columnJavaDataType.substring(columnJavaDataType.lastIndexOf('.') + 1);
				columnTypesMap.put(columnJavaDataType, simpleJavaType);
			}
		}
	}
	
	/**
	 * @return the <code>JpaRelationship</code> that contains this table
	 */
	public List<IJpaRelationship> getRelationships() {
		List<IJpaRelationship> jpaRelationships;
		JpaRelationship jpaRelationship;
		
		logger.trace("IN");
	
		jpaRelationships = new ArrayList<IJpaRelationship>();
	
		for(BusinessRelationship relationship : getBusinessRelationships()) {
			jpaRelationship = new JpaRelationship(this, relationship);		
			jpaRelationships.add(jpaRelationship);	
		}
		logger.trace("OUT");		
		return jpaRelationships;		
	}
	
	public PhysicalTable getPhysicalTable() {
		return physicalTable;
	}
	
	/**
	 * TODO .. da implementare
	 * @return
	 */
	public String getDefaultFetch(){
		return "lazy";
	}
	
	abstract List<BusinessColumn> getBusinessColumns();
	
	abstract List<BusinessRelationship> getBusinessRelationships();
	
	abstract protected BusinessModel getModel();
	/**
	 * @return the package name of mapping class associated with this table
	 */
	public String getPackage() {
		String packageName;
		ModelProperty packageProperty;
		
		logger.trace("IN");
		
		packageProperty =  getModel().getProperties().get(JpaProperties.MODEL_PACKAGE);
        
		//check if property is setted, else get default value
        if (packageProperty.getValue() != null){
        	packageName = packageProperty.getValue();
        } else {
        	packageName = packageProperty.getPropertyType().getDefaultValue();
        }
        
        logger.trace("OUT");  
        
        return packageName;
	}
	
	/**
	 * @return the composite key java class name (not qualified).
	 */
	public String getCompositeKeyClassName() {
		return getClassName() + "PK"; //$NON-NLS-1$
	}
	
	/**
	 * Returns the <code>JpaColumn</code> objects for the the columns that
	 * are not part of any association.
	 * 
	 * @param genOnly  			Whether to include only the columns marked for generation.
	 * 
	 * @param includePk  		Whether to include the primary key column(s).
	 * 
	 * @param includeInherited 	Whether to include the columns associated with Jjava properties
	 *            				that exist in the super class (if any).
	 */
	public List<IJpaColumn> getSimpleColumns(boolean genOnly, boolean includePk, boolean includeInherited) {
		List<IJpaColumn> result = new ArrayList<IJpaColumn>();
		List<IJpaColumn> columns = getColumns();
	
		for (int i = 0, n = columns.size(); i < n; ++i) {
			IJpaColumn column = columns.get(i);
			
			if (column.isIdentifier()) {
				if (!includePk || hasCompositeKey()) {
					continue;
				} else {
					result.add(0, column);
					continue;
				}
			} else if (column.isColumnInRelationship()) {
				continue;
			}
			result.add(column);
		}
		return result;
	}

	public List<IJpaColumn> getSimpleColumns() {
		return getSimpleColumns(true/* genOnly */, true/* includePk */, true/* includeInherited */);
	}

	/**
	 * @return all the import statements needed in the java class that map this table
	 */
	public String getImportStatements(){
		
		Collection<String> packages;
		StringBuilder importStatement;
		
		logger.trace("IN");
		
		packages = columnTypesMap.keySet();
		
		importStatement = new StringBuilder();
		for ( String s : packages ) {
			importStatement.append("import " + s + ";\n"); //$NON-NLS-1$
		}

		List<IJpaRelationship> relationships = getRelationships();
		for ( IJpaRelationship relationship :  relationships ) {
			if ( relationship.isOneToMany() || relationship.isManyToMany() ) {
				importStatement.append( "import " + relationship.getCollectionType() + ";\n"); //$NON-NLS-1$
				break;
			}
		}

		//logger.debug("Business table [{}] imported statements block is equal to [{}]", businessTable.getName(), importStatement.toString());
		
		logger.trace("OUT");
		
		return importStatement.toString();
	}
	
	
}
