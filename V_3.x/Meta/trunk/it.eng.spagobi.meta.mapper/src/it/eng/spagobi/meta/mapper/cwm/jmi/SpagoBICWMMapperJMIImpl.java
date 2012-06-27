/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.mapper.cwm.jmi;

import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.relational.CwmCatalog;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.relational.CwmTable;
import it.eng.spagobi.meta.mapper.cwm.ICWM;
import it.eng.spagobi.meta.mapper.cwm.ICWMMapper;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.Collection;
import java.util.List;



/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBICWMMapperJMIImpl  extends ICWMMapper {
	
	static public PhysicalModelFactory FACTORY = PhysicalModelFactory.eINSTANCE;
	
	
    // -----------------------------------------------------------------------------
	// DECODE
	// -----------------------------------------------------------------------------
    
	public PhysicalModel decodeICWM(ICWM cwm) {
		return decodeModel((SpagoBICWMJMIImpl)cwm);
	}
	
	public PhysicalModel decodeModel(SpagoBICWMJMIImpl cwm) {
		PhysicalModel model = FACTORY.createPhysicalModel();
		model.setName(cwm.getName());
		
		model.setCatalog( cwm.getCatalog().getName() );
		
		return model;
	}

	// -----------------------------------------------------------------------------
	// ENDECODE
	// -----------------------------------------------------------------------------
	
	public SpagoBICWMJMIImpl encodeICWM(PhysicalModel model) {
		
		SpagoBICWMJMIImpl cwm = new SpagoBICWMJMIImpl(model.getName());
		
		CwmCatalog catalog = cwm.createCatalog(model.getCatalog());
		
		List<PhysicalTable> tables = model.getTables();
		CwmTable table;
		Collection<CwmTable> tc = catalog.getOwnedElement();
		for(int i = 0; i < tables.size(); i++) {
			table = encodeTable(cwm, tables.get(i));
			tc.add(table);	
			table.setNamespace(catalog);
		}
		
		return cwm;
	}	 
	
	public CwmTable encodeTable(SpagoBICWMJMIImpl cwm, PhysicalTable table) {
		CwmTable t;
		t = cwm.createTable(table.getName());
		return t;
	}

}
