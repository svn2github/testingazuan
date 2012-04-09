package it.eng.spagobi.meta.test.generator;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.TestConnectionFactory;
import it.eng.spagobi.meta.test.TestCostants;

import org.junit.Before;


public class JpaMappingGeneratorTest {

	TestCostants.DatabaseType dbType=TestCostants.DatabaseType.MYSQL;
	Model rootModel=null;
	PhysicalModel physicalModel=null;
	PhysicalModelInitializer modelInitializer=null;
	BusinessModel businessModel=null;
	BusinessModelInitializer businessModelInitializer=null;
        
	JpaMappingCodeGenerator gen = null;
	
	@Before
	public void setUp() throws Exception {
		// initialize model
		
		rootModel = ModelFactory.eINSTANCE.createModel();
		rootModel.setName("modelDemo");
		
		modelInitializer = new PhysicalModelInitializer();
		modelInitializer.setRootModel(rootModel);		
		physicalModel = modelInitializer.initialize( 
        		"physicalModelDemo", 
        		TestConnectionFactory.createConnection(dbType),
        		"Test Connection",
        		TestCostants.MYSQL_DRIVER,
        		TestCostants.MYSQL_URL,
        		TestCostants.MYSQL_USER,
        		TestCostants.MYSQL_PWD,
        		"DB Name",
        		TestConnectionFactory.getDefaultCatalogue(dbType), 
        		TestConnectionFactory.getDefaultSchema(dbType));
		
        businessModelInitializer = new BusinessModelInitializer();
        businessModel = businessModelInitializer.initialize("businessModelDemo", physicalModel);	
        
        gen = new JpaMappingCodeGenerator();
	}
}
