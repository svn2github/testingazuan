/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalModelDefaultPropertiesInitializer implements IPropertiesInitializer {
	public void addProperties(ModelObject o) {
		
		if(o instanceof PhysicalColumn) {
			initColumnProperties((PhysicalColumn)o);
		} else if(o instanceof PhysicalModel) {
			initModelProperties((PhysicalModel)o);
		} else if(o instanceof PhysicalForeignKey) {
			initForeignKeyProperty((PhysicalForeignKey)o);
		} else if(o instanceof PhysicalPrimaryKey) {
			initPrimaryKeyProperties((PhysicalPrimaryKey)o);
		} else if(o instanceof PhysicalTable) {
			initTableProperties((PhysicalTable)o);
		} else {
			
		}
	}
	
	private void initModelProperties(PhysicalModel o) {
		
	}

	private void initTableProperties(PhysicalTable o) {
		
	}
	
	private void initColumnProperties(PhysicalColumn o) {
		
	}
	
	private void initPrimaryKeyProperties(PhysicalPrimaryKey o) {
		
	}

	private void initForeignKeyProperty(PhysicalForeignKey o) {
	
	}
}
