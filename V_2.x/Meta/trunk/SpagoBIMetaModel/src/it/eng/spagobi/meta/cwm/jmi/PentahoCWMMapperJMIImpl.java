/**
 * 
 */
package it.eng.spagobi.meta.cwm.jmi;

import it.eng.spagobi.meta.cwm.ICWM;
import it.eng.spagobi.meta.cwm.ICWMMapper;
import it.eng.spagobi.meta.model.physical.PhysicalModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.Collection;
import java.util.List;

import org.pentaho.pms.cwm.pentaho.meta.relational.CwmCatalog;
import org.pentaho.pms.cwm.pentaho.meta.relational.CwmTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PentahoCWMMapperJMIImpl  implements ICWMMapper {
	
	static public PhysicalModelFactory FACTORY = PhysicalModelFactory.eINSTANCE;
	
	
    // -----------------------------------------------------------------------------
	// DECODE
	// -----------------------------------------------------------------------------
    
	public PhysicalModel decodeModel(ICWM cwm) {
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
	
	public PentahoCWMJMIImpl encodeModel(PhysicalModel model) {
		
		PentahoCWMJMIImpl cwm = new PentahoCWMJMIImpl(model.getName());
		
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
	
	public CwmTable encodeTable(PentahoCWMJMIImpl cwm, PhysicalTable table) {
		CwmTable t;
		t = cwm.createTable(table.getName());
		return t;
	}

}
