/**
 * 
 */
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.cwm.jmi.CWMJMIImpl;
import it.eng.spagobi.meta.cwm.jmi.CWMMapperJMIImpl;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.sql.Connection;
import java.sql.DriverManager;


/**
 * @author agioia
 *
 */
public class LoadModelTest {
	
	public static String FILENAME = "D:\\Documenti\\Progetti\\metadati\\workspace\\SpagoBIMetaData\\model.xml";

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		CWMJMIImpl cwm;
		CWMMapperJMIImpl modelMapper;
		PhysicalModel model;
		
        // xmi -> cwm (jmi) -> spagobi model
        
        cwm = new CWMJMIImpl("modeldemo");
        cwm.importFromXMI(FILENAME);
        
        modelMapper = new CWMMapperJMIImpl();
        model = modelMapper.decodeModel(cwm);
        
        System.out.println("Model name: " + model.getName());
        System.out.println("Model catalog: " + model.getCatalog());
	}

}
