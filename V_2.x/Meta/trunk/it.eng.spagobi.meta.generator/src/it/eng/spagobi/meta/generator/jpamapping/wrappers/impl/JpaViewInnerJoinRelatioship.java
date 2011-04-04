package it.eng.spagobi.meta.generator.jpamapping.wrappers.impl;

import java.util.ArrayList;
import java.util.List;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaColumn;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

public class JpaViewInnerJoinRelatioship {
	
	BusinessView businessView;
	BusinessViewInnerJoinRelationship joinRelationship;
	
	public JpaViewInnerJoinRelatioship(BusinessView businessView, BusinessViewInnerJoinRelationship joinRelationship) {
		this.businessView = businessView;
		this.joinRelationship = joinRelationship;
	}
	
	public IJpaTable getSourceTable() {
		IJpaTable jpaTable;
		PhysicalTable viewInnerTable = joinRelationship.getSourceTable();
		jpaTable = new JpaViewInnerTable(businessView, viewInnerTable);
		
		return jpaTable;
	}
	
	public IJpaTable getDestinationTable() {
		IJpaTable jpaTable;
		PhysicalTable viewInnerTable = joinRelationship.getSourceTable();
		jpaTable = new JpaViewInnerTable(businessView, viewInnerTable);
		
		return jpaTable;
	}
	
	public List<IJpaColumn> getSourceColumns() {
		List<IJpaColumn> sourceColumns;
		List<PhysicalColumn> columns;
		JpaViewInnerTable innerSourceTable;
		
		sourceColumns = new ArrayList<IJpaColumn>();
		columns = joinRelationship.getSourceColumns();
		innerSourceTable = (JpaViewInnerTable)getSourceTable();
		for(PhysicalColumn physicalColumn: columns) {
			BusinessColumn businessColumn = innerSourceTable.findColumnInBusinessView(physicalColumn);
			if(businessColumn != null){
				JpaColumn jpaColumn = new JpaColumn(businessColumn);
				jpaColumn.setJpaTable(innerSourceTable);
				sourceColumns.add( jpaColumn );
			}
		}
		
		return sourceColumns;
	}
	
	public List<IJpaColumn> getDestinationColumns() {
		List<IJpaColumn> destinationColumns;
		List<PhysicalColumn> columns;
		JpaViewInnerTable innerDestinationTable;
		
		destinationColumns = new ArrayList<IJpaColumn>();
		columns = joinRelationship.getDestinationColumns();
		innerDestinationTable = (JpaViewInnerTable)getDestinationTable();
		for(PhysicalColumn physicalColumn: columns) {
			BusinessColumn businessColumn = innerDestinationTable.findColumnInBusinessView(physicalColumn);
			if(businessColumn != null){
				JpaColumn jpaColumn = new JpaColumn(businessColumn);
				jpaColumn.setJpaTable(innerDestinationTable);
				destinationColumns.add( jpaColumn );
			}
		}
		
		return destinationColumns;
	}
}
