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

import it.eng.spagobi.meta.generator.utils.StringUtil;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.util.JDBCTypeMapper;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaColumn {
	BusinessColumn businessColumn;
	AbstractJpaTable jpaTable;
	
	/*get/set and field scopes*/
	public static final String PUBLIC_SCOPE = "public"; //$NON-NLS-1$
	public static final String PROTECTED_SCOPE = "protected"; //$NON-NLS-1$
	public static final String PRIVATE_SCOPE = "private"; //$NON-NLS-1$
	
	private static Logger logger = LoggerFactory.getLogger(JpaColumn.class);
	
	public JpaColumn(BusinessColumn businessColumn) {
		this.businessColumn = businessColumn;
	}
	
	public boolean isIdentifier() {
		return businessColumn.isIdentifier();
	}
	/**
	 * if the PK is composite and we are writing PK Class Property
	 * @return
	 */
	public boolean isPKReadOnly() {
		if (jpaTable.hasCompositeKey() && businessColumn.isIdentifier())
		return true;
		else return false;
	}	
	
	/**
	 * Return true if this Column belong to any relationship
	 * @return
	 */
	public boolean isColumnInRelationship() {
		boolean isColumnInRelationship;
		
		List<BusinessRelationship> relationships;
		
		isColumnInRelationship = false;
		relationships =jpaTable.getBusinessRelationships();
		
		logger.trace("The OBJECT "+jpaTable.getClassName()+" has "+relationships.size()+" Relationship");
		for(BusinessRelationship relationship : relationships) {
			logger.info("The RELATIONSHIP IS : "+relationship.getName());
			List<BusinessColumn> columns = null; 
			if (relationship.getSourceTable()==null){
				logger.trace("The relationship "+relationship.getName()+" doesn't have any source table");
				continue;
			}
			if (relationship.getDestinationTable()==null){
				logger.trace("The relationship "+relationship.getName()+" doesn't have any destination table");
				continue;
			}
			
			if (jpaTable instanceof JpaViewInnerTable){
				if(relationship.getSourceTable().equals( ((JpaViewInnerTable)jpaTable).getBusinessView() )) {
					columns =  relationship.getSourceColumns();
				} else {
					columns =  relationship.getDestinationColumns();
				}					
			}else if (jpaTable instanceof JpaTable){
				if(relationship.getSourceTable().equals( ((JpaTable)jpaTable).getBusinessTable() )) {
					columns =  relationship.getSourceColumns();
				} else {
					columns =  relationship.getDestinationColumns();
				}				
			}

			
			if (columns!=null){
				// scann columns
				for(BusinessColumn column : columns) {
					if(column.equals(businessColumn)) {
						isColumnInRelationship = true;
						logger.debug("Column [{}] belong to a relationship", getColumnName());
					}
				}
			}else {
				logger.error("The Columns are null");
			}
		}
		// check if the column belong to a innerJoin
		if (jpaTable instanceof JpaViewInnerTable){
			for (BusinessViewInnerJoinRelationship innerJoin : ((JpaViewInnerTable)jpaTable).getBusinessView().getJoinRelationships()){
				List<PhysicalColumn> columns = null; 
				logger.trace("The INNER RELATIONSHIP IS : " + innerJoin.getName());
				if (innerJoin==null || 
						innerJoin.getSourceTable()==null){
					logger.error("There is a problem , the innerRelationship doesn't have any source Table");
					continue;
				}
				if (innerJoin==null || 
						innerJoin.getDestinationTable()==null){
					logger.error("There is a problem , the innerRelationship doesn't have any destination Table");
					continue;
				}
				if(innerJoin.getSourceTable().equals( jpaTable.getPhysicalTable())) {
					columns =  innerJoin.getSourceColumns();
				} else {
					columns =  innerJoin.getDestinationColumns();
				}
				if (columns!=null){
					// scann columns
					for(PhysicalColumn column : columns) {
						if(column.getName().equals(businessColumn.getPhysicalColumn().getName())) {
							isColumnInRelationship = true;
							logger.debug("Column [{}] belong to an inner relationship", getColumnName());
						}
					}
				}else {
					logger.error("The Columns are null");
				}		
				
			}
		}
		
		if(!isColumnInRelationship) logger.debug("Column [{}] doesn't belong to any relationship", getColumnName());
		
		return isColumnInRelationship;
	}
	
	/**
	 * Returns the generated bean property name for the given column.
	 * Does not return null.
	 */
	public String getPropertyName() {
		String name;
		name = StringUtil.tableNameToVarName(businessColumn.getPhysicalColumn().getName());	
		return name;
	}
	

	public String getSimplePropertyType()  {
		String result=null;
		result= getPropertyType().substring( getPropertyType().lastIndexOf('.')+1 );
		return result;
	}
	
	/**
	 * Returns the column type as JAVA Object
	 * Does not return null.
	 */
	public String getPropertyType()  {
		String type;
		
		ModelProperty property = businessColumn.getProperties().get(JpaProperties.COLUMN_DATATYPE);
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
	public AbstractJpaTable getJpaTable() {
		return jpaTable;
	}
	public void setJpaTable(AbstractJpaTable jpaTable) {
		this.jpaTable = jpaTable;
	}
	/**
	 * TODO ... da verificare !!  
	 * @return
	 */
	public boolean isDataTypeLOB(){
		ModelProperty property = businessColumn.getProperties().get(JpaProperties.COLUMN_DATATYPE);
		String modelType = property.getValue();
		if (modelType.equals("BLOB") || modelType.equals("CLOB")) return true;
		else return false;
	}
	
	/**
	 * Return the phisical column name 
	 * @return
	 */
	public String getColumnName(){
		return businessColumn.getPhysicalColumn().getName();
	}
	public String getColumnNameDoubleQuoted(){
		return StringUtil.doubleQuote(businessColumn.getPhysicalColumn().getName());
	}
	
	public boolean needMapTemporalType(){
		if (getPropertyType().equals("java.util.Date")
					|| getPropertyType().equals("java.util.Calendar")) return true;
		else return false;
	}
	public String getMapTemporalType(){
		if (getPropertyType().equals("java.sql.Date") ) return "DATE";
		if (getPropertyType().equals("java.tim.Date") ) return "TIME";
		if (getPropertyType().equals("java.sql.Timestamp") ) return "TIMESTAMP";
		else return "";
	}	
	
	/**
	 * Return the name of the metod GETTER
	 * @return
	 */
	public String getPropertyNameGetter() {
		return "get"+StringUtil.initUpper(getPropertyName());
	}
	/**
	 * Return the name of the metod SETTER
	 * @return
	 */
	public String getPropertyNameSetter() {
		return "set"+StringUtil.initUpper(getPropertyName());
	}	
}
