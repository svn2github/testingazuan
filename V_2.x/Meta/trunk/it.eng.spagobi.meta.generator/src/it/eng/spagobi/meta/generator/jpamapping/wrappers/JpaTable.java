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
package it.eng.spagobi.meta.generator.jpamapping.wrappers;

import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class wrap a business table and provide all the utility methods used by the template engine
 * in order to generate the java class mapping
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class JpaTable extends AbstractJpaTable {
	
	BusinessTable businessTable;
	
	
	
	private static Logger logger = LoggerFactory.getLogger(JpaTable.class);

	public JpaTable(BusinessTable businessTable) {
		super(businessTable.getPhysicalTable());
		this.businessTable = businessTable;
		initColumnTypesMap();
	}
	
	List<BusinessColumn> getBusinessColumns() {
		return businessTable.getColumns();
	}
	
	List<BusinessRelationship> getBusinessRelationships() {
		return businessTable.getRelationships();
	}
	
	protected BusinessModel getModel(){
		return businessTable.getModel();
	}

	public BusinessTable getBusinessTable() {
		return businessTable;
	}

	public PhysicalTable getPhysicalTable() {
		return businessTable.getPhysicalTable();
	}
	
	/**
	 * Returns the <code>JpaColumn</code> objects to be generated for this
	 * table.
	 */
	public List<JpaColumn> getColumns() {
		if (jpaColumns == null) {
			jpaColumns = new ArrayList<JpaColumn>();
			for (BusinessColumn c : businessTable.getColumns()) {
				JpaColumn jpaColumn = new JpaColumn(c);
				jpaColumn.setJpaTable(this);
				jpaColumns.add(jpaColumn);
				logger.debug("Business table [{}] contains column [{}]", businessTable.getName(), c.getName());
			        
			}
		}
		return jpaColumns;
	}

	
	
	
	/**
	 * @return true if the table has a primary key
	 */
	public boolean hasPrimaryKey() {
		return businessTable.getIdentifier() != null? businessTable.getIdentifier().getColumns().size() > 0 : false;
	}
	
	
	/**
	 * @return true if there is more than 1 column in the table identifier
	 */
	public boolean hasCompositeKey() {	
		return businessTable.getIdentifier() != null? businessTable.getIdentifier().getColumns().size() > 1 : false;
	}
	
	
	
	/**
	 * @return the primary columns
	 */
	public List<JpaColumn> getPrimaryKeyColumns(){
		List<JpaColumn> result = new ArrayList<JpaColumn>();
		List<JpaColumn> columns = getColumns();
	
		for (int i = 0, n = columns.size(); i < n; ++i) {
			JpaColumn column = columns.get(i);
			
			if (column.isIdentifier())	{
				result.add(column);
			}
		}
		return result;		
	}
	/**
	 * @return the composite key property name
	 * 
	 *  * TODO .... da implementare
	 */
	public String getCompositeKeyPropertyName() {
		return "compId"; //$NON-NLS-1$
	}

	/**
	 * @return the name of the metod GETTER
	 */
	public String getCompositeKeyPropertyNameGetter() {
		return "get"+StringUtils.initUpper(getCompositeKeyPropertyName());

	}
	/**
	 * @return the name of the metod SETTER
	 */
	public String getCompositeKeyPropertyNameSetter() {
		return "set"+StringUtils.initUpper(getCompositeKeyPropertyName());
	}	
	
	/**
	 * @return the boolean expression that verify if two primary key are equal
	 */
	public String getPrimaryKeyEqualsClause(){
		String equalsClause;
		List<JpaColumn> columns;
		
		equalsClause = null;
		columns = getPrimaryKeyColumns();
		for (int i = 0, n = columns.size(); i < n; ++i) {
			JpaColumn column = columns.get(i);
			if (equalsClause == null) equalsClause = "( this."+column.getPropertyName()+".equals(castOther."+column.getPropertyName()+") )";
			else equalsClause += " \n && ( this."+column.getPropertyName()+".equals(castOther."+column.getPropertyName()+") )";
		}
		
		if (equalsClause==null) return "";
		else return equalsClause+";";	
		
	}
	
	/**
	 * 
	 * @return the expresion that compute the hash code for a given primary key
	 */
	public String getPrimaryKeyHashCodeClause(){
		String hashcodeClause;
		List<JpaColumn> columns;
		
		hashcodeClause = null;
		columns = getPrimaryKeyColumns();
		for (int i = 0, n = columns.size(); i < n; ++i) {
			JpaColumn column = columns.get(i);
			if (hashcodeClause==null)  hashcodeClause=" hash = hash * prime + this."+column.getPropertyName()+".hashCode() ;\n";
			else hashcodeClause=hashcodeClause+" hash = hash * prime + this."+column.getPropertyName()+".hashCode() ;\n";
		}
		
		if (hashcodeClause==null) return "";
		return hashcodeClause;		
	}
	

	
	/**
	 * @return the <code>JpaRelationship</code> that contains this table
	 */
	public List<AbstractJpaRelationship> getRelationships() {
		List<AbstractJpaRelationship> jpaRelationships;
		JpaRelationship jpaRelationship;
		
		logger.trace("IN");
	
		jpaRelationships = new ArrayList<AbstractJpaRelationship>();
		logger.debug("Business table [{}] have  [{}] relationships", businessTable.getName(), businessTable.getRelationships().size());
        
		for(BusinessRelationship relationship : businessTable.getRelationships()) {
			logger.debug("Business table [{}] contains relationship  [{}] ", businessTable.getName(), relationship.getName());
	        
			jpaRelationship = new JpaRelationship(this, relationship);		
			jpaRelationships.add(jpaRelationship);	
		}
		
		logger.trace("OUT");		
		return jpaRelationships;		
	}
	
	
	
	/**
	 * @returns the generated java class name (not qualified).
	 */
	public String getClassName() {
		String name;
		name = StringUtils.tableNameToVarName(businessTable.getPhysicalTable().getName());
		name = StringUtils.initUpper(name);
		return name;
	}
	
	/**
	 * @returns the generated java class name (qualified).
	 */
	public String getQualifiedClassName() {
		return getPackage() + "."  + getClassName();
	}
	

}
