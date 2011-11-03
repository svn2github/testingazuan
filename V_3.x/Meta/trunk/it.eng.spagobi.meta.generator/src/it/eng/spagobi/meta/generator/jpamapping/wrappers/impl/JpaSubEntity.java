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
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaSubEntity;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaView;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class JpaSubEntity implements IJpaSubEntity {
	
	public static final String DESTINATION_ROLE = "structural.destinationRole";

	Object root; // table or view
	BusinessRelationship relationship;

	JpaSubEntity parent; // null if parent is equal to root
	List<JpaSubEntity> children;

	private static Logger logger = LoggerFactory.getLogger(JpaSubEntity.class);

	protected JpaSubEntity(Object root, JpaSubEntity parent, BusinessRelationship relationship) {
		this.root = root;
		this.parent = parent;
		this.relationship = relationship;
		this.children = new ArrayList<JpaSubEntity>();
	}

	public JpaSubEntity getParent() {
		return parent;
	}
	
	public List<IJpaSubEntity> getChildren() {
		List<IJpaSubEntity> subEntities = new ArrayList<IJpaSubEntity>();
		
		for(BusinessRelationship r : relationship.getDestinationTable().getRelationships()) {
			if(r.getSourceTable() != relationship.getDestinationTable()) continue;
			
			JpaSubEntity subEntity = new JpaSubEntity(root, this, r);
			subEntities.add(subEntity);
		}
		return subEntities;
	}
	
	public IJpaTable getParentTable() {
		IJpaTable parentTable = null;
		BusinessColumnSet parentColumnSet = relationship.getSourceTable();
		if(parentColumnSet instanceof BusinessTable) {
			parentTable = new JpaTable((BusinessTable)parentColumnSet);
		} else if(parentColumnSet instanceof BusinessView) {
			BusinessView businessView = (BusinessView)parentColumnSet;
			PhysicalTable physicalTable = relationship.getSourceSimpleBusinessColumns().get(0).getPhysicalColumn().getTable();
			parentTable = new JpaViewInnerTable(businessView, physicalTable);
		}
		
		return parentTable;
	}
	
	public IJpaColumn getParentColumn() {
		JpaTable sourceTable = null;
		IJpaColumn sourceColumn = null;
		BusinessColumnSet businessColumnSet = relationship.getSourceColumns().get(0).getTable();
		if (businessColumnSet instanceof BusinessTable){
			sourceTable = new JpaTable( (BusinessTable)relationship.getSourceColumns().get(0).getTable() );
			sourceColumn = new JpaColumn(sourceTable, relationship.getSourceSimpleBusinessColumns().get(0));
		} else if (businessColumnSet instanceof BusinessView){
			//TODO: check this cases
			BusinessView businessView = (BusinessView)relationship.getSourceColumns().get(0).getTable() ;
			PhysicalTable physicalTable = relationship.getSourceSimpleBusinessColumns().get(0).getPhysicalColumn().getTable();
			JpaViewInnerTable sourceView = new JpaViewInnerTable(businessView, physicalTable);
			sourceColumn = new JpaColumn(sourceView, relationship.getSourceSimpleBusinessColumns().get(0));
			//***********
		}
		
		return sourceColumn;
	}
	
	public IJpaTable getTable() {
		IJpaTable table = null;
		BusinessColumnSet columnSet = relationship.getDestinationTable();
		if(columnSet instanceof BusinessTable) {
			table = new JpaTable((BusinessTable)columnSet);
		} else if(columnSet instanceof BusinessView) {
			BusinessView businessView = (BusinessView)columnSet;
			PhysicalTable physicalTable = relationship.getDestinationSimpleBusinessColumns().get(0).getPhysicalColumn().getTable();
			table = new JpaViewInnerTable(businessView, physicalTable);
		}
		
		return table;
	}
	
	public BusinessColumnSet getBusinessColumnSet(){
		BusinessColumnSet columnSet = relationship.getDestinationTable();
		return columnSet;
	}
	
	public String getRootQualifiedClassName() {
		String uniqueName;
		
		uniqueName = null;
		
		if(root instanceof BusinessTable) {
			IJpaTable rootTable = new JpaTable( (BusinessTable)root );
			uniqueName = rootTable.getQualifiedClassName();
		} else if(root instanceof BusinessView) {
			IJpaView rootView = new JpaView( (BusinessView)root );
			uniqueName = rootView.getQualifiedClassName();
		}
		
		return uniqueName;
	}
	
	
	public String getName() {
		String name;
		
		name = null;
		
		IJpaTable table = getTable();
		IJpaColumn jpaColumn = getParentColumn();
		if (jpaColumn!=null){
			//name = table.getClassName() + "(" + getParentColumn().getPropertyName().toLowerCase() + ")";
			name = getParentColumn().getPropertyName() + "(" + getParentColumn().getPropertyName().toLowerCase() + ")";
		}
		else {
			logger.debug("Cannot retrieve parent column of [{}]",this);
		}
		
		return name;
	}
	
	@Override
	public String getUniqueName() {
		String uniqueName;
		int counter = 0;
		uniqueName = "";
		
		JpaSubEntity targetEntity = this;
		while(targetEntity != null) {
			uniqueName = "//" + targetEntity.getName() + uniqueName;
			targetEntity = targetEntity.getParent();
			counter++;
		}
		
		uniqueName = getRootQualifiedClassName() + uniqueName;
		
		//Important: for entity of level => 3 we use an uniqueName with single / instead of //
		if (counter > 1){
			uniqueName = uniqueName.replace("//", "/");
		}
		
		return uniqueName;
	}
	
	@Override
	public List<String> getColumnUniqueNames() {
		List<String> uniqueNames = new ArrayList<String>();
		
		IJpaTable table = getTable();
		for(IJpaColumn column : table.getColumns()) {
			uniqueNames.add(getUniqueName()  + "/" + column.getUnqualifiedUniqueName());
		}
		
		return uniqueNames;
	}
	
	public List<String> getColumnsNameWithPath()  {
		String subEntityUniqueName = this.getUniqueName();
		List<IJpaColumn> columns = getColumns();
		List<String> columnsNameWithPath = new ArrayList<String>();
		
		for(IJpaColumn column : columns) {
			columnsNameWithPath.add(subEntityUniqueName  + "/" + column.getUnqualifiedUniqueName());
		}
		
		return columnsNameWithPath;
	}
	
	@Override
	public List<String> getColumnNames() {
		List<String> columnsNames = new ArrayList<String>();
		
		IJpaTable table = getTable();
		for(IJpaColumn column : table.getColumns()) {
			columnsNames.add(column.getUnqualifiedUniqueName());
		}
		
		return columnsNames;
	}
	
	@Override
	public List<IJpaColumn> getColumns() {
		IJpaTable table = getTable();
		return table.getColumns();
	}
	
	public String getLabel() {
		return getAttribute("label");
	}
	
	public String getTooltip(){
		return getAttribute("tooltip");
	}
	
	@Override
	public String getAttribute(String name) {
		if ( (name.equals("label")) || (name.equals("tooltip")) ){
			return relationship.getProperties().get(DESTINATION_ROLE).getValue();
		}
		else {
			return this.getTable().getAttribute(name);
		}

	}
}
