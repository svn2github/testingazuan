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

	protected JpaTable(BusinessTable businessTable) {
		super(businessTable.getPhysicalTable());
		this.businessTable = businessTable;
		initColumnTypesMap();
	}
	
	List<BusinessColumn> getBusinessColumns() {
		return businessTable.getColumns();
	}
	
	public List<BusinessRelationship> getBusinessRelationships() {
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
	public List<IJpaColumn> getColumns() {
		if (jpaColumns == null) {
			jpaColumns = new ArrayList<IJpaColumn>();
			for (BusinessColumn businessColumn : businessTable.getColumns()) {
				JpaColumn jpaColumn = new JpaColumn(this, businessColumn);
				jpaColumns.add(jpaColumn);
				logger.debug("Business table [{}] contains column [{}]", businessTable.getName(), businessColumn.getName());
			        
			}
		}
		return jpaColumns;
	}

	
	
	
	/*
	 * (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable#hasFakePrimaryKey()
	 */
	public boolean hasFakePrimaryKey() {
		return !(businessTable.getIdentifier() != null? businessTable.getIdentifier().getColumns().size() > 0 : false);
	}
	
	
	/*
	 * (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable#hasCompositeKey()
	 */
	public boolean hasCompositeKey() {	
		boolean hasCompositeKey = false;
		
		if(businessTable.getIdentifier() != null) { // if there's a key...
			if(businessTable.getIdentifier().getColumns().size() > 1) { // ...and it is composed by more then one column
				hasCompositeKey = true;
			}
		} else { // if there isn't a key 
			hasCompositeKey = true;
			// we return true because we are going to generate a fake key composed by
			// all columns in the table in order to keep jpa runtime happy (in jpa as in 
			// hibernate any persisted object must have a key)
		}

		return hasCompositeKey;
	}
	
	
	
	

	
	/**
	 * @return the <code>JpaRelationship</code> that contains this table
	 */
	public List<IJpaRelationship> getRelationships() {
		List<IJpaRelationship> jpaRelationships;
		JpaRelationship jpaRelationship;
		
		logger.trace("IN");
	
		jpaRelationships = new ArrayList<IJpaRelationship>();
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
	
	

	@Override
	public String getName() {
		return businessTable.getName();
	}

	@Override
	public String getDescription() {
		return businessTable.getDescription() != null? businessTable.getDescription(): getName();
	}

	@Override
	public String getSqlName() {
		return businessTable.getPhysicalTable().getName();
	}
	

}
