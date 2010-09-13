/**
 * 
 */
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.cwm.jmi.SpagoBICWMJMIImpl;
import it.eng.spagobi.meta.cwm.jmi.SpagoBICWMMapperJMIImpl;
import it.eng.spagobi.meta.model.physical.PhysicalModel;


/**
 * @author Andrea Gioia (andrea.gioi@eng.it)
 *
 */
public class LoadModelTest {
	
	public static String FILENAME = "D:\\Documenti\\Progetti\\metadati\\workspace\\SpagoBIMetaData\\model.xml";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		SpagoBICWMJMIImpl cwm;
		SpagoBICWMMapperJMIImpl modelMapper;
		PhysicalModel model;
		
        // xmi -> cwm (jmi) -> spagobi model
        
        cwm = new SpagoBICWMJMIImpl("modeldemo");
        cwm.importFromXMI(FILENAME);
        
        modelMapper = new SpagoBICWMMapperJMIImpl();
        model = modelMapper.decodeModel(cwm);
        
        System.out.println("Model name: " + model.getName());
        System.out.println("Model catalog: " + model.getCatalog());
	}

}
