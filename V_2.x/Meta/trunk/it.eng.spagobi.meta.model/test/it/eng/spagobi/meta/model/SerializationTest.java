/**
 * 
 */
package it.eng.spagobi.meta.model;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.TestConnectionFactory.DatabaseType;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;


/**
 * @author agioia
 *
 */
public class SerializationTest {
	
	public static String FILENAME = "emfmodel.xmi";
	
	
	
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
	
		DatabaseType dbType;
		Model rootModel;
		PhysicalModel physicalModel;
		PhysicalModelInitializer modelInitializer;
		BusinessModel businessModel;
		BusinessModelInitializer businessModelInitializer;
	        
		dbType = DatabaseType.MYSQL;
	
		// initialize model
		
		rootModel = ModelFactory.eINSTANCE.createModel();
		rootModel.setName("modelDemo");
		
		modelInitializer = new PhysicalModelInitializer();
		modelInitializer.setRootModel(rootModel);		
		physicalModel = modelInitializer.initialize( 
        		"physicalModelDemo", 
        		TestConnectionFactory.createConnection(dbType),  
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
