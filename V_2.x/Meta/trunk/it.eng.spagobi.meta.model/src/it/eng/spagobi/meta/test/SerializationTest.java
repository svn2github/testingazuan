/**
 * 
 */
package it.eng.spagobi.meta.test;

import it.eng.spagobi.meta.cwm.CWMImplType;
import it.eng.spagobi.meta.cwm.CWMMapperFactory;
import it.eng.spagobi.meta.cwm.ICWM;
import it.eng.spagobi.meta.cwm.ICWMMapper;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelFactory;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.serializer.IModelSerializer;
import it.eng.spagobi.meta.test.TestConnectionFactory.DatabaseType;

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
