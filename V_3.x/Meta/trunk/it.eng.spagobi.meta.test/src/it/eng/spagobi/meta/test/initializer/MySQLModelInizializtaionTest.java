package it.eng.spagobi.meta.test.initializer;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.junit.Before;


public class MySQLModelInizializtaionTest extends AbstractModelInizializtaionTest{

	public void setUp() throws Exception {
		super.setUp();
		try {
			rootModel = ModelFactory.eINSTANCE.createModel();
			rootModel.setName("modelDemo");
			
			physicalModelInitializer.setRootModel(rootModel);		
			physicalModel = physicalModelInitializer.initialize( 
	        		"physicalModelDemo", 
	        		TestConnectionFactory.createConnection(dbType),
	        		"Test Connection",
	        		TestConnectionFactory.MYSQL_DRIVER,
	        		TestConnectionFactory.MYSQL_URL,
	        		TestConnectionFactory.MYSQL_USER,
	        		TestConnectionFactory.MYSQL_PWD,
	        		"DB Name",
	        		TestConnectionFactory.getDefaultCatalogue(dbType), 
	        		TestConnectionFactory.getDefaultSchema(dbType));
			
	        
	        businessModel = businessModelInitializer.initialize("businessModelDemo", physicalModel);
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	// add specific test here
	
	
	public void testPhysicalModelTablesInitialization() {
		int tableNo = physicalModel.getTables().size();
		for(PhysicalTable table : physicalModel.getTables()) {
			assertNotNull("Name of physical table cannot be null", table.getName());
			//assertNotNull("Unique name of physical table cannot be null", table.getUniqueName());			
		}
	}
}
