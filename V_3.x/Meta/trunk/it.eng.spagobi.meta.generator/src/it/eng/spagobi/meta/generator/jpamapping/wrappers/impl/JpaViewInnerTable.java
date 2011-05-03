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
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This class wrap a physical table used within a business view and provide all the utility methods used by the template engine
 * in order to generate the java class mapping
 * 
 * @authors
 * 	Angelo Bernabei( angelo.bernabei@eng.it)
 * 	Andrea Gioia (andrea.gioia@eng.it)
 */
public class JpaViewInnerTable extends AbstractJpaTable {

	private BusinessView businessView;
	
	
	private static Logger logger = LoggerFactory.getLogger(JpaViewInnerTable.class);

	/**
	 * @param businessView The business view that contains the physical table
	 * @param physicalTable The physical table used to write this java class
	 */
	protected JpaViewInnerTable(BusinessView businessView, PhysicalTable physicalTable) {
		super(physicalTable);
		
		Assert.assertNotNull("Parameter [businessView] cannot be null", businessView);
		Assert.assertTrue("Parameter [physicalTable] is not contained in parameter [businessView]", businessView.getPhysicalTables().contains(physicalTable));
		
		logger.debug("Inner table [" + physicalTable.getName() + "] of view [" + businessView.getName() + "]");
		
		this.businessView = businessView;
		this.physicalTable= physicalTable;
		
		initColumnTypesMap();
	}
	
	public List<BusinessColumn> getBusinessColumns() {
		return businessView.getColumns();
	}
	
	public List<BusinessRelationship> getBusinessRelationships() {
		return businessView.getRelationships();
	}
	
	
	
	protected BusinessModel getModel(){
		return businessView.getModel();
	}

	public BusinessView getBusinessView() {
		return businessView;
	}
	public void setBusinessView(BusinessView businessView) {
		this.businessView = businessView;
	}
	
	public List<IJpaColumn> getColumns() {
		
		logger.trace("IN");
		
		if (jpaColumns == null) {
			jpaColumns = new ArrayList<IJpaColumn>();
			
			for (PhysicalColumn physicalColumn : physicalTable.getColumns()){
				BusinessColumn businessColumn = findColumnInBusinessView(physicalColumn);
				// if the colums belong to the BusinessView
				if (businessColumn!=null){
						JpaColumn jpaColumn = new JpaColumn(this, businessColumn);
						jpaColumns.add(jpaColumn);
						logger.info("Add "+jpaColumn.getSqlName()+" Column to the BV "+businessView.getName());
					}					
				}	
		}
		
		logger.trace("OUT");
		
		return jpaColumns;
	}
	/**
	 * Check if the physical column belong to the view
	 * 
	 * @param physicalColumn
	 * 
	 * @return the business column that wrap the physical column if it belong to the view. null otherwise
	 */
	protected BusinessColumn findColumnInBusinessView(PhysicalColumn physicalColumn){
		
		for (BusinessColumn businessColumn : businessView.getColumns()) {
			if (physicalColumn.getName().equals(businessColumn.getPhysicalColumn().getName())){
					return businessColumn;
			}					
		}	
		return null;
	}
	
		

	/*
	 * (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable#hasFakePrimaryKey()
	 */
	public boolean hasFakePrimaryKey() {
		return true;
		//return !(physicalTable.getPrimaryKey() != null? physicalTable.getPrimaryKey().getColumns().size() > 0 : false);
	}
	
	/*
	 * (non-Javadoc)
	 * @see it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable#hasCompositeKey()
	 */
	public boolean hasCompositeKey() {	
		return true;
		/*
		boolean hasCompositeKey = false;
		
		if(physicalTable.getPrimaryKey() != null) { // if there's a key...
			if(physicalTable.getPrimaryKey().getColumns().size() > 1) { // ...and it is composed by more then one column
				hasCompositeKey = true;
			}
		} else { // if there isn't a key 
			hasCompositeKey = true;
			// we return true because we are going to generate a fake key composed by
			// all columns in the table in order to keep jpa runtime happy (in jpa as in 
			// hibernate any persisted object must have a key)
		}

		return hasCompositeKey;
		*/
	}
	
	public String getClassName() {
		String name;
		
		name = null;
		try {
			name = StringUtils.tableNameToVarName(businessView.getName() + "_" + physicalTable.getName());
			name = StringUtils.initUpper(name);
		} catch (Throwable t) {
			logger.error("Impossible to get class name", t);
		}
		return name;
	}
	
	/**
	 * Return the <code>JpaRelationship</code> that contains this table
	 * We have to ADD only the relationship belong to this Physical Table.
	 * @return
	 */
	public List<IJpaRelationship> getRelationships() {
		
		logger.trace("IN");
		
		List<IJpaRelationship> jpaRelationships;
		
		jpaRelationships = super.getRelationships();
		/*
		for (BusinessViewInnerJoinRelationship innerJoin : businessView.getJoinRelationships()){
			JpaInnerRelationship jpainnerRelationship = new JpaInnerRelationship(this, innerJoin);
			
			if (jpainnerRelationship.getBusinessInnerRelationship()==null || 
					jpainnerRelationship.getBusinessInnerRelationship().getSourceTable()==null){
				logger.error("There is a problem , the relationship doesn't have any source Table");
				continue;
			}
			if (jpainnerRelationship.getBusinessInnerRelationship()==null || 
					jpainnerRelationship.getBusinessInnerRelationship().getDestinationTable()==null){
				logger.error("There is a problem , the relationship doesn't have any destination Table");
				continue;
			}
			
			if (jpainnerRelationship!=null ) {
				jpaRelationships.add(jpainnerRelationship);
				logger.info("ADD the relationship : "+innerJoin.getName());
			}else {
				logger.info("Don't ADD the relationship : "+innerJoin.getName());
			}			
		}
		*/
		logger.debug("OUT");		
		return jpaRelationships;		
	}
	

	
	/**
	 * 
	 * @param t
	 * @param r
	 * @return
	 */
	private boolean isBelongToRelationship(PhysicalTable t,BusinessRelationship r ){
		if (r!=null &&  r.getSourceTable()!=null && t.getName()!=null){
			List<BusinessColumn>  source=r.getSourceColumns();
			
			for (BusinessColumn bc:source){
				PhysicalTable fTable=bc.getPhysicalColumn().getTable();
				if (t.getName().equals(fTable.getName()))return true;	
			}

		}
		if (r!=null &&  r.getDestinationTable()!=null && t.getName()!=null){
			List<BusinessColumn>  source=r.getDestinationColumns();
			
			for (BusinessColumn bc:source){
				PhysicalTable fTable=bc.getPhysicalColumn().getTable();
				if (t.getName().equals(fTable.getName()))return true;	
			}
		}		
		
		return false;
	}

	@Override
	public String getName() {
		return businessView.getName() + " > " + StringUtils.initUpper(physicalTable.getName().replace("_", " "));
	}

	@Override
	public String getDescription() {
		return physicalTable.getDescription() != null? physicalTable.getDescription(): "";
	}

	@Override
	public String getSqlName() {
		return physicalTable.getName();
	}


	
}
