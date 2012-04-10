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




import it.eng.spagobi.meta.generator.utils.StringUtils;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaRelationship extends AbstractJpaRelationship {
	
	BusinessRelationship businessRelationship;

	private static Logger logger = LoggerFactory.getLogger(JpaRelationship.class);
	
	
	protected JpaRelationship(AbstractJpaTable jpaTable, BusinessRelationship businessRelationship) {
		
		Assert.assertNotNull("Parameter [jpaTable] cannot be null", jpaTable);
		Assert.assertNotNull("Parameter [businessRelationship] cannot be null", businessRelationship);
		Assert.assertNotNull("Parameter [businessRelationship] must have a source table", businessRelationship.getSourceTable());
		Assert.assertNotNull("Parameter [businessRelationship] must have a destination table", businessRelationship.getDestinationTable());
		
		this.jpaTable = jpaTable;
		this.businessRelationship = businessRelationship;
		
		if ( isSourceRole() ){
			this.cardinality = JpaRelationship.MANY_TO_ONE;
		} else if ( isDestinationRole() ){
			this.cardinality = JpaRelationship.ONE_TO_MANY;				
		}
		
		this.bidirectional = true;
	}

	private boolean isDestinationRole() {
		boolean isSourceRole;
		
		if(jpaTable instanceof JpaTable) {
			isSourceRole = businessRelationship.getDestinationTable().equals(((JpaTable)jpaTable).getBusinessTable());
		} else {
			isSourceRole = businessRelationship.getDestinationTable().equals( ((JpaViewInnerTable)jpaTable).getBusinessView() );
		}
		
		return isSourceRole;
	}
	
	private boolean isSourceRole() {
		boolean isSourceRole;
		
		if(jpaTable instanceof JpaTable) {
			isSourceRole = businessRelationship.getSourceTable().equals(((JpaTable)jpaTable).getBusinessTable());
		} else {
			isSourceRole = businessRelationship.getSourceTable().equals( ((JpaViewInnerTable)jpaTable).getBusinessView() );
		}
		
		return isSourceRole;
	}
	
	/**
	 * return the destination Physical table of the relationship
	 * @param bv the destination BV of the relationship
	 * @param columns ...
	 * @return
	 */
	private PhysicalTable findPhysicalTable(BusinessView bv,List<SimpleBusinessColumn> columns){
		// the destination physical tables
		List<PhysicalTable> physicaltables=bv.getPhysicalTables();
		PhysicalTable result=null;
		for (PhysicalTable phyt : physicaltables){
			boolean found=false;
			for (SimpleBusinessColumn bc : columns){
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
	protected PhysicalColumn findPhysicalColumn (List<PhysicalColumn> fColumn,SimpleBusinessColumn bColumn){
		for (PhysicalColumn fc : fColumn){
			if (bColumn.getPhysicalColumn().getName().equals(fc.getName())){
				logger.info("FOUND the "+fc.getName()+" Physical Column");
				return fc;
			}
		}	
		logger.info("No Physical Column FOUND");
		return null;
	}

	public AbstractJpaTable getReferencedTable(){
		
		if ( isSourceRole() ) {
			
			if(businessRelationship.getDestinationTable() instanceof BusinessTable) {
				return new JpaTable((BusinessTable)businessRelationship.getDestinationTable());
			}else if (businessRelationship.getDestinationTable() instanceof BusinessView){
				PhysicalTable physicalTMP=findPhysicalTable((BusinessView)businessRelationship.getDestinationTable(),businessRelationship.getDestinationSimpleBusinessColumns());
				return new JpaViewInnerTable((BusinessView)businessRelationship.getDestinationTable(),physicalTMP); 
			}
		} else {
			if(businessRelationship.getSourceTable() instanceof BusinessTable) {
				return new JpaTable((BusinessTable)businessRelationship.getSourceTable());
			}else if (businessRelationship.getSourceTable() instanceof BusinessView){
				PhysicalTable physicalTMP=findPhysicalTable((BusinessView)businessRelationship.getSourceTable(),businessRelationship.getSourceSimpleBusinessColumns());
				return new JpaViewInnerTable((BusinessView)businessRelationship.getSourceTable(),physicalTMP); 

			}
		}
		return null;
		
	}
	

	public BusinessRelationship getBusinessRelationship() {
		return businessRelationship;
	}

	public String getPropertyName(){
		if (getBusinessRelationship().getSourceColumns()!=null){
			return StringUtils.nameToJavaVariableName( "rel_"+getBusinessRelationship().getSourceColumns().get(0).getUniqueName()+"_in_"+getBusinessRelationship().getDestinationTable().getName());
		}
		else return "";
	}
	
	/**
	 * TODO .. da verificare
	 * @return
	 */
	protected String getOppositeRoleName(){
		return StringUtils.nameToJavaVariableName( "rel_"+getBusinessRelationship().getSourceColumns().get(0).getUniqueName()+"_in_"+getBusinessRelationship().getDestinationTable().getName());	
	}
	
	public String getBidirectionalPropertyName(){
		return StringUtils.pluralise(StringUtils.nameToJavaVariableName( getBusinessRelationship().getName()));
	}	
	
	public String getSimpleSourceColumnName(){
		return StringUtils.doubleQuote("`"+getBusinessRelationship().getSourceSimpleBusinessColumns().get(0).getPhysicalColumn().getName()+"`");
	}
	
	public boolean isMultipleRelationship(){
		if (getBusinessRelationship().getSourceColumns().size() > 1){
			return true;
		} else {
			return false;
		}			
	}
	
	public List<String> getSimpleSourceColumnsNames(){
		List<String> sourceColumnsNames = new ArrayList<String>();
		if (isMultipleRelationship()){
			List<SimpleBusinessColumn> sourceColumns = getBusinessRelationship().getSourceSimpleBusinessColumns();
			for (SimpleBusinessColumn column : sourceColumns ){
				sourceColumnsNames.add( StringUtils.doubleQuote(column.getPhysicalColumn().getName()));
			}
		}
		return sourceColumnsNames;
	}
	
	public List<String> getSimpleDestinationColumnsNames(){
		List<String> destinationColumnsNames = new ArrayList<String>();
		if (isMultipleRelationship()){
			List<SimpleBusinessColumn> destinationColumns = getBusinessRelationship().getDestinationSimpleBusinessColumns();
			for (SimpleBusinessColumn column : destinationColumns ){
				destinationColumnsNames.add( StringUtils.doubleQuote(column.getPhysicalColumn().getName()));
			}
		}
		return destinationColumnsNames;
	}
	
	public List<JpaRelationshipColumnsNames> getRelationshipColumnsNames(){
		List<JpaRelationshipColumnsNames> relationshipColumnsNames = new ArrayList<JpaRelationshipColumnsNames>();
		if (isMultipleRelationship()){
			List<SimpleBusinessColumn> destinationColumns = getBusinessRelationship().getDestinationSimpleBusinessColumns();
			List<SimpleBusinessColumn> sourceColumns = getBusinessRelationship().getSourceSimpleBusinessColumns();

			for (int i=0; i < sourceColumns.size(); i++){
				 String sourceColumnName = StringUtils.doubleQuote("`"+sourceColumns.get(i).getPhysicalColumn().getName()+"`");
				 String destinationColumnName =  StringUtils.doubleQuote("`"+destinationColumns.get(i).getPhysicalColumn().getName()+"`");
				 JpaRelationshipColumnsNames relationshipNames = new JpaRelationshipColumnsNames(sourceColumnName,destinationColumnName );
				 relationshipColumnsNames.add(relationshipNames);
			}
		}
		return relationshipColumnsNames;
	}
	
	
}
