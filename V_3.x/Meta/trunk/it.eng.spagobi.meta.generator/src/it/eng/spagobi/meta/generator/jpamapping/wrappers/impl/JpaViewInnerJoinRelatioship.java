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
	
	protected JpaViewInnerJoinRelatioship(BusinessView businessView, BusinessViewInnerJoinRelationship joinRelationship) {
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
		PhysicalTable viewInnerTable = joinRelationship.getDestinationTable();
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
				JpaColumn jpaColumn = new JpaColumn(innerSourceTable, businessColumn);
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
				JpaColumn jpaColumn = new JpaColumn(innerDestinationTable, businessColumn);
				destinationColumns.add( jpaColumn );
			}
		}
		
		return destinationColumns;
	}
}
