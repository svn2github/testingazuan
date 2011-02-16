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

import it.eng.spagobi.meta.commons.JDBCTypeMapper;
import it.eng.spagobi.meta.initializer.BusinessModelDefaultPropertiesInitializer;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Angelo Bernabei( angelo.bernabei@eng.it)
 * This class extends JpaTable in order to write java classes that depends from Business View
 */
public class JpaView extends JpaTable {

	private static Logger logger = LoggerFactory.getLogger(JpaView.class);
	private BusinessView businessView=null;
	private PhysicalTable physicalTable=null;
	

	
	/**
	 * @param businessView
	 * @param physicalTable The physical Table used to write this Java Class
	 */
	public JpaView(BusinessView businessView,PhysicalTable physicalTable) {
		setBusinessView(businessView);
		this.physicalTable=physicalTable;
	}
	@Override
	public PhysicalTable getPhysicalTable() {
		return physicalTable;
	}

	
	public BusinessView getBusinessView() {
		return businessView;
	}
	public void setBusinessView(BusinessView businessView) {
		this.businessView = businessView;
	}
	
	
	@Override
	protected List<JpaColumn> getColumns() {
		logger.debug("IN");
		if (jpaColumns == null) {
			//List<PhysicalColumn> colums=phisicalTable.getColumns();
			jpaColumns = new ArrayList<JpaColumn>();
			
			for (PhysicalColumn pysC : physicalTable.getColumns()){
				BusinessColumn bc=findColumnInBV(pysC,businessView);
				// if the colums belong to the BusinessView
				if (bc!=null){
						JpaColumn jpaColumn = new JpaColumn(bc);
						jpaColumn.setJpaTable(this);
						jpaColumns.add(jpaColumn);
						logger.info("Add "+jpaColumn.getColumnName()+" Column to the BV "+businessView.getName());
					}					
				}	
		}
		logger.debug("OUT");
		return jpaColumns;
	}
	/**
	 * Check IF the column belong to the BV
	 * @param pysC
	 * @param businessView
	 * @return
	 */
	private BusinessColumn findColumnInBV(PhysicalColumn pysC,BusinessView businessView){
		
		for (BusinessColumn c : businessView.getColumns()) {
			if (pysC.getName().equals(c.getPhysicalColumn().getName())){
					return c;
			}					
		}	
		logger.warn("Return NULL, no column in BV");
		return null;
	}
	
	
	/**
	 * build the hasmap that contains the properties type of this "Business Table"
	 * @return
	 */
	@Override
	protected void buildColumnTypesMap() {
		logger.debug("IN");
		if (columnTypesMap == null) {
			columnTypesMap = new HashMap<String, String>();
			for (BusinessColumn column : businessView.getColumns()) {
				ModelProperty property = column.getProperties().get(BusinessModelDefaultPropertiesInitializer.COLUMN_DATATYPE);
				String modelType = property.getValue();
				String javaType = JDBCTypeMapper.getJavaTypeName(modelType);
				if ( !javaType.startsWith("java.lang")
						&& javaType.indexOf('.') > 0) {
					String simpleJavaType = javaType.substring(javaType
							.lastIndexOf('.') + 1);
					columnTypesMap.put(javaType, simpleJavaType);
				}
			}
		}
		logger.debug("OUT");
	}
	

	/**
	 * Returns true if the table has a Primary KEY
	 */
	@Override
	public boolean hasPrimaryKey() {		
		return physicalTable.getPrimaryKey() != null? physicalTable.getPrimaryKey().getColumns().size() > 0 : false;
	}
	@Override
	public boolean hasCompositeKey() {		
		return physicalTable.getPrimaryKey() != null? physicalTable.getPrimaryKey().getColumns().size() > 1 : false;
	}
	@Override
	public String getClassName() {
		String name;
		name = StringUtil.tableNameToVarName(physicalTable.getName());
		name = StringUtil.initUpper(name);
		return name;
	}
	
	/**
	 * Return the <code>JpaRelationship</code> that contains this table
	 * We have to ADD only the relationship belong to this Physical Table.
	 * @return
	 */
	public List<JpaRelationship> getRelationships() {
		logger.debug("IN");
		List<JpaRelationship> jpaRelationships;
		JpaRelationship jpaRelationship=null;
		
		jpaRelationships = new ArrayList<JpaRelationship>();
		logger.info("Number of relationschip of OBJECT "+physicalTable.getName()+" : "+businessView.getRelationships().size());
		for(BusinessRelationship relationship : businessView.getRelationships()) {
			jpaRelationship = new JpaRelationship(this, relationship);
			logger.info("The RELATIONSHIP IS : "+relationship.getName());
			if (jpaRelationship.getBusinessRelationship()==null || 
					jpaRelationship.getBusinessRelationship().getSourceTable()==null){
				logger.error("There is a problem , the relationship doesn't have any source Table");
				continue;
			}
			if (jpaRelationship.getBusinessRelationship()==null || 
					jpaRelationship.getBusinessRelationship().getDestinationTable()==null){
				logger.error("There is a problem , the relationship doesn't have any destination Table");
				continue;
			}				
			if (jpaRelationship.getBusinessRelationship().getSourceTable().equals(businessView)){
				// many-to-one
				jpaRelationship.setCardinality(JpaRelationship.MANY_TO_ONE);
			}else if (jpaRelationship.getBusinessRelationship().getDestinationTable().equals(businessView)){
				// one-to-many
				jpaRelationship.setCardinality(JpaRelationship.ONE_TO_MANY);				
			}
			if (jpaRelationship!=null && isBelongToRelationship(physicalTable,relationship)) {
				jpaRelationship.setBidirectional(true);
				jpaRelationships.add(jpaRelationship);
				logger.info("ADD the relationship : "+relationship.getName());
			}else {
				logger.info("Don't ADD the relationship : "+relationship.getName());
			}
		}
		for (BusinessViewInnerJoinRelationship innerJoin : businessView.getJoinRelationships()){
			JpaInnerRelationship jpainnerRelationship = new JpaInnerRelationship(this, innerJoin);
			logger.info("The INNER RELATIONSHIP IS : "+innerJoin.getName());
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
			if (jpainnerRelationship.getBusinessInnerRelationship().getSourceTable().equals(physicalTable)){
				// many-to-one
				jpainnerRelationship.setCardinality(JpaRelationship.MANY_TO_ONE);
			}else if (jpainnerRelationship.getBusinessInnerRelationship().getDestinationTable().equals(physicalTable)){
				// one-to-many
				jpainnerRelationship.setCardinality(JpaRelationship.ONE_TO_MANY);				
			}	
			if (jpainnerRelationship!=null ) {
				jpainnerRelationship.setBidirectional(true);
				jpaRelationships.add(jpainnerRelationship);
				logger.info("ADD the relationship : "+innerJoin.getName());
			}else {
				logger.info("Don't ADD the relationship : "+innerJoin.getName());
			}			
		}
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

}
