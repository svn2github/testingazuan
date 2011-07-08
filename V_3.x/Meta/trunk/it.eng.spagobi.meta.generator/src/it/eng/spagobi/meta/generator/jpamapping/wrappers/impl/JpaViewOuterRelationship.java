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

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaView;
import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class JpaViewOuterRelationship {
	
	BusinessRelationship businessRelationship;
	JpaView jpaView;
	boolean isOutbound;
	private static Logger logger = LoggerFactory.getLogger(JpaViewOuterRelationship.class);
	boolean isSourceTableView = false;
	boolean isDestinationTableView = false;


	protected JpaViewOuterRelationship(JpaView jpaView, BusinessRelationship businessRelationship, boolean isOutbound){
		this.jpaView = jpaView;
		this.businessRelationship = businessRelationship;
		this.isOutbound = isOutbound;
	}

	public String getSourceTable() {
		IJpaTable jpaTable = null;
		String name = null;
		BusinessColumnSet businessColumnSet = businessRelationship.getSourceTable();
		if (businessColumnSet instanceof BusinessTable){
			jpaTable = new JpaTable((BusinessTable)businessColumnSet);
			name = jpaTable.getClassName();
		} else if (businessColumnSet instanceof BusinessView){
			BusinessView businessView = (BusinessView)businessColumnSet;
			name = StringUtils.tableNameToVarName(businessView.getName());
			name = StringUtils.initUpper(name);	
			isSourceTableView = true;
		}
		return name;

	}
	
	public String getDestinationTable() {
		IJpaTable jpaTable = null;
		String name = null;
		BusinessColumnSet businessColumnSet = businessRelationship.getDestinationTable();
		if (businessColumnSet instanceof BusinessTable){		
			jpaTable = new JpaTable((BusinessTable)businessColumnSet);
			name = jpaTable.getClassName();
		} else if (businessColumnSet instanceof BusinessView){			
			BusinessView businessView = (BusinessView)businessColumnSet;
			name = StringUtils.tableNameToVarName(businessView.getName());
			name = StringUtils.initUpper(name);	
			isDestinationTableView = true;
		}
		return name;
	}
	
	
	public List<String> getSourceColumns() {
		List<IJpaColumn> sourceColumns;
		List<PhysicalColumn> columns = new ArrayList();
		List<BusinessColumn> businessColumns;
		List<IJpaTable> innerTables;
		
 		List<String> columnsNames = new ArrayList<String>();
		
		sourceColumns = new ArrayList<IJpaColumn>();
		businessColumns = businessRelationship.getSourceColumns();
		for (BusinessColumn businessColumn : businessColumns){
			columns.add(businessColumn.getPhysicalColumn());
		}
		innerTables = jpaView.getInnerTables();
		
		//check if is a outbound relationship
		if(isOutboundRelationship()){
			for (BusinessColumn businessColumn : businessColumns){
				columnsNames.add(StringUtils.columnNameToVarName(businessColumn.getName()));
			}

		} else {
			//inbound relationship
			AbstractJpaTable jpaTable = null;
			if(businessRelationship.getSourceTable() instanceof BusinessTable) {
				jpaTable = new JpaTable((BusinessTable)businessRelationship.getSourceTable());
			}else if (businessRelationship.getSourceTable() instanceof BusinessView){
				PhysicalTable physicalTMP=findPhysicalTable((BusinessView)businessRelationship.getSourceTable(),businessRelationship.getSourceColumns());
				jpaTable = new JpaViewInnerTable((BusinessView)businessRelationship.getSourceTable(),physicalTMP); 
			}
			
			for(BusinessColumn businessColumn: businessColumns) {
				JpaColumn jpaColumn = new JpaColumn(jpaTable, businessColumn);
				if (!sourceColumns.contains(jpaColumn))
					sourceColumns.add( jpaColumn );
			}
			
			for (IJpaColumn sourceColumn:sourceColumns){
				String name;
				if (sourceColumn.getJpaTable().hasFakePrimaryKey()|| ( sourceColumn.getJpaTable().hasCompositeKey() && sourceColumn.isIdentifier())){
					name = sourceColumn.getJpaTable().getCompositeKeyPropertyName()+"."+sourceColumn.getPropertyName();
				}
				else {
					name = sourceColumn.getPropertyName();
				}
				columnsNames.add(name);
					
			}
		}

		return columnsNames;
	}
	
	
	public List<String> getDestinationColumns(){
		List<IJpaColumn> destinationColumns;
		List<PhysicalColumn> columns = new ArrayList();
		List<BusinessColumn> businessColumns;
		List<IJpaTable> innerTables;
 		List<String> columnsNames = new ArrayList<String>();

		
 		
		destinationColumns = new ArrayList<IJpaColumn>();
		businessColumns = businessRelationship.getDestinationColumns();
		for (BusinessColumn businessColumn : businessColumns){
			columns.add(businessColumn.getPhysicalColumn());
		}
		innerTables = jpaView.getInnerTables();
		
		//check if is a inbound relationship
		if(!isOutboundRelationship()){
			for (BusinessColumn businessColumn : businessColumns){
				columnsNames.add(StringUtils.columnNameToVarName(businessColumn.getName()));
			}
		} else {
			//outbound relationship
			AbstractJpaTable jpaTable = null;
			if(businessRelationship.getDestinationTable() instanceof BusinessTable) {
				jpaTable = new JpaTable((BusinessTable)businessRelationship.getDestinationTable());
			}else if (businessRelationship.getDestinationTable() instanceof BusinessView){
				PhysicalTable physicalTMP=findPhysicalTable((BusinessView)businessRelationship.getDestinationTable(),businessRelationship.getDestinationColumns());
				jpaTable = new JpaViewInnerTable((BusinessView)businessRelationship.getDestinationTable(),physicalTMP); 
			}
			
			for(BusinessColumn businessColumn: businessColumns) {
				JpaColumn jpaColumn = new JpaColumn(jpaTable, businessColumn);
				if (!destinationColumns.contains(jpaColumn))
					destinationColumns.add( jpaColumn );
			}
			
			for (IJpaColumn destinationColumn:destinationColumns){
				String name;
				if (destinationColumn.getJpaTable().hasFakePrimaryKey()|| ( destinationColumn.getJpaTable().hasCompositeKey() && destinationColumn.isIdentifier())){
					name = destinationColumn.getJpaTable().getCompositeKeyPropertyName()+"."+destinationColumn.getPropertyName();
				}
				else {
					name = destinationColumn.getPropertyName();
				}
				columnsNames.add(name);
					
			}
		}

		return columnsNames;
	}
	
	
	public String getPackage(){
		return jpaView.getPackage();
	}
	
	//check if is a outbound relationship
	public boolean isOutboundRelationship(){
		return isOutbound;
	}
	
	/**
	 * @return the isSourceTableView
	 */
	public boolean isSourceTableView() {
		return isSourceTableView;
	}
	
	public String getSourceTableView(){
		return new Boolean(isSourceTableView).toString();
	}
	
	/**
	 * @return the isDestinationTableView
	 */
	public boolean isDestinationTableView() {
		return isDestinationTableView;
	}
	
	public String getDestinationTableView(){
		return new Boolean(isDestinationTableView).toString();
	}
	
	
	
	

	private PhysicalTable findPhysicalTable(BusinessView bv,List<BusinessColumn> columns){
		// the destination physical tables
		List<PhysicalTable> physicaltables=bv.getPhysicalTables();
		PhysicalTable result=null;
		for (PhysicalTable phyt : physicaltables){
			boolean found=false;
			for (BusinessColumn bc : columns){
				PhysicalColumn fc=findPhysicalColumn(phyt.getColumns(),bc);
				if (fc != null){
					logger.info("Physical Column FOUND "+bc.getName());
					found=true;
				}
					
			}
			if (found) result=phyt;
		}
		return result;
	}
	
	/**
	 * return true if the BC is included into the Physical column list
	 * @param phy
	 * @param column
	 * @return
	 */
	protected PhysicalColumn findPhysicalColumn (List<PhysicalColumn> fColumn,BusinessColumn bColumn){
		for (PhysicalColumn fc : fColumn){
			if (bColumn.getPhysicalColumn().getName().equals(fc.getName())){
				logger.info("FOUND the "+fc.getName()+" Physical Column");
				return fc;
			}
		}	
		logger.info("No Physical Column FOUND");
		return null;
	}

}
