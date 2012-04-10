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
package it.eng.spagobi.meta.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaRelationship;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.IJpaTable;
import it.eng.spagobi.meta.generator.jpamapping.wrappers.impl.JpaTable;
import it.eng.spagobi.meta.initializer.descriptor.BusinessRelationshipDescriptor;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModelManager {
	Model model;
	PhysicalModel physicalModel;
	BusinessModel businessModel;
	
	
	public ModelManager(Model model) {
		this.model = model;
		physicalModel = model.getPhysicalModels().get(0);
		businessModel = model.getBusinessModels().get(0);
	}
	
	// TODO move to an utility class
	public BusinessTable getBusinessTableByPhysicalTableName(String physicalTableName) {
		PhysicalTable physicalTable =  physicalModel.getTable(physicalTableName);
		return  businessModel.getBusinessTable(physicalTable);
	}
	
	// TODO move to an utility class
	public BusinessColumn getBusinessColumnByPhysicalColumnName(BusinessTable businessTable, String physicalColumnName) {
		PhysicalColumn physicalColumn = businessTable.getPhysicalTable().getColumn(physicalColumnName);
		return  businessTable.getSimpleBusinessColumn(physicalColumn);
	}
	
	public void testGenerationMoreForeignKeyOnSameColumn() {
		BusinessTable sourceBusinessTable =  getBusinessTableByPhysicalTableName("product");
		BusinessColumn sourceBusinessColumn = getBusinessColumnByPhysicalColumnName(sourceBusinessTable, "product_class_id");		
		List<BusinessColumn> sourceColumns = new ArrayList<BusinessColumn>();
		sourceColumns.add(sourceBusinessColumn);
		
		BusinessTable destinationBusinessTable =  getBusinessTableByPhysicalTableName("customer");
		BusinessColumn destinationBusinessColumn = getBusinessColumnByPhysicalColumnName(destinationBusinessTable, "customer_id");
		List<BusinessColumn> destinationColumns = new ArrayList<BusinessColumn>();
		destinationColumns.add(destinationBusinessColumn);
		
		BusinessRelationshipDescriptor descriptor = new BusinessRelationshipDescriptor(
				sourceBusinessTable, destinationBusinessTable, 
				sourceColumns , destinationColumns, 
				0, "SecondFKOnTheSameColumn");
			
//		BusinessRelationship businessRelationship = businessModelInitializer.addRelationship(descriptor);
//		
//		JpaTable targetJpaTable = null;
//		for(IJpaTable table : jpaModel.getTables()) {
//			if(table instanceof JpaTable) {
//				JpaTable jpaTable = (JpaTable)table;
//				if(jpaTable.getBusinessTable().getPhysicalTable().getName().equals("product")) {
//					targetJpaTable = jpaTable;
//				}
//			}
//		}
//		
//		Set<String> relationshispPropertyNames = new HashSet<String>();
//		for(IJpaRelationship relationship : targetJpaTable.getRelationships()) {
//			String propertyName = relationship.getPropertyName();
//			Assert.assertFalse("Duplicate relationship property name [" + propertyName + "] in table [product]", relationshispPropertyNames.contains(propertyName));
//			relationshispPropertyNames.add(propertyName);
//		}
		
	}
}
