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
