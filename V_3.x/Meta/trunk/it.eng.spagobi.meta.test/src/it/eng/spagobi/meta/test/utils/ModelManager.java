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
package it.eng.spagobi.meta.test.utils;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.descriptor.BusinessViewInnerJoinRelationshipDescriptor;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
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
	BusinessModelInitializer initializer;
	
	
	public ModelManager(Model model) {
		this.model = model;
		physicalModel = model.getPhysicalModels().get(0);
		businessModel = model.getBusinessModels().get(0);
		initializer = new BusinessModelInitializer();
	}
	
	public BusinessView createView(BusinessTable businessTable, BusinessViewInnerJoinRelationshipDescriptor innerJoinRelationshipDescriptor) {
		return initializer.upgradeBusinessTableToBusinessView(businessTable, innerJoinRelationshipDescriptor);
	}

	public void addBusinessTable(PhysicalTable physicalTable) {
		initializer.addTable(physicalTable,  businessModel, false);	
	}
	
	public void addBusinessColumn(PhysicalColumn physicalColumn, BusinessColumnSet businessColumnSet) {
		initializer.addColumn(physicalColumn, businessColumnSet);
	}
}
