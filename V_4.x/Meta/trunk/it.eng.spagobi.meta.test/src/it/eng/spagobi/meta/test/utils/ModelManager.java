/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
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
