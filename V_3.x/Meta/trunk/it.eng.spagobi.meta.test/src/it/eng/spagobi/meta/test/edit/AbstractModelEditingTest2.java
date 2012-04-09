package it.eng.spagobi.meta.test.edit;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.test.TestCostants;
import junit.framework.TestCase;


public class AbstractModelEditingTest2 extends TestCase {

	TestCostants.DatabaseType dbType;
	Model rootModel;
	PhysicalModel physicalModel;
	BusinessModel businessModel;
	PhysicalModelInitializer physicalModelInitializer;
	BusinessModelInitializer businessModelInitializer;
        
	public void setUp() throws Exception {
		try {
			dbType = TestCostants.DatabaseType.MYSQL;
			physicalModelInitializer = new PhysicalModelInitializer();
			businessModelInitializer = new BusinessModelInitializer();
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	protected void tearDown() throws Exception {
		dbType = null;
		rootModel=null;
		physicalModel=null;
		businessModel=null;
		physicalModelInitializer=null;
		businessModelInitializer=null;
	}
	
	// add general test here
	
	public void testModelEditingSmoke() {
		assertTrue("Metamodel must have one business model ", rootModel.getBusinessModels().size() == 1);	
	}
}
