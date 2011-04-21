
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.initializer.TestConnectionFactory;
import it.eng.spagobi.meta.util.ModelPrinter;

import java.io.File;


/**
 * @author Andrea
 *
 */
public class SerializationTest {
	
	public static String FILENAME = "emfmodel.xmi";
	
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	
		TestConnectionFactory.DatabaseType dbType;
		Model rootModel;
		PhysicalModel physicalModel;
		PhysicalModelInitializer modelInitializer;
		BusinessModel businessModel;
		BusinessModelInitializer businessModelInitializer;
	        
		dbType = TestConnectionFactory.DatabaseType.MYSQL;
	
		// initialize model
		
		rootModel = ModelFactory.eINSTANCE.createModel();
		rootModel.setName("modelDemo");
		
		modelInitializer = new PhysicalModelInitializer();
		modelInitializer.setRootModel(rootModel);		
		physicalModel = modelInitializer.initialize( 
        		"physicalModelDemo", 
        		TestConnectionFactory.createConnection(dbType),
        		"Test Connection",
        		TestConnectionFactory.MYSQL_URL,
        		TestConnectionFactory.MYSQL_USER,
        		TestConnectionFactory.MYSQL_PWD,
        		"DB Name",
        		TestConnectionFactory.getDefaultCatalogue(dbType), 
        		TestConnectionFactory.getDefaultSchema(dbType));
        
        
        businessModelInitializer = new BusinessModelInitializer();
        businessModel = businessModelInitializer.initialize("businessModelDemo", physicalModel);
       
        
        
        // test serialization
        
        ModelPrinter.print(businessModel);
       
        IModelSerializer serializer = new EmfXmiSerializer();
        serializer.serialize(rootModel, new File(FILENAME));
       
        rootModel = serializer.deserialize(new File(FILENAME));
        businessModel = rootModel.getBusinessModels().get(0);
        
        ModelPrinter.print(businessModel);
        
	}

}
