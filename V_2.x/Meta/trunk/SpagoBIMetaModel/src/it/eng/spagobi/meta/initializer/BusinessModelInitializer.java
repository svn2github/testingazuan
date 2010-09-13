/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class BusinessModelInitializer {
	
	static public BusinessModelFactory FACTORY = BusinessModelFactory.eINSTANCE;
	
	public BusinessModel initialize(String modelName, PhysicalModel physicalModel) {
		BusinessModel model;
		
		try {
			model = FACTORY.createBusinessModel();
			model.setName(modelName);
			log("model: " + model.getName()); 
			
			// for each physical model object create a related business object
			
			// tables
			
			// columns
			
			// relationships-foreign keys
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to initialize business model", t);
		}
		
		return model;
	}

	//  --------------------------------------------------------
	//	Static methods
	//  --------------------------------------------------------
	
	private static void log(String msg) {
		System.out.println(msg);
	}
}
