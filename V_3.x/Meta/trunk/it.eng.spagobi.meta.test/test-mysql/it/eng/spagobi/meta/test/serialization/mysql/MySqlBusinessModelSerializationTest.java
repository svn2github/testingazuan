package it.eng.spagobi.meta.test.serialization.mysql;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.serialization.AbstractModelSerializationTest;
import it.eng.spagobi.meta.test.serialization.EmfXmiSerializer;
import it.eng.spagobi.meta.test.serialization.IModelSerializer;

import java.io.File;

import org.junit.Assert;


public class MySqlBusinessModelSerializationTest extends AbstractModelSerializationTest {

	public void setUp() throws Exception {
		try {
			// if this is the first test on postgres after the execution
			// of tests on an other database force a tearDown to clean
			// and regenerate properly all the static variables contained in
			// parent class AbstractSpagoBIMetaTest
			if(dbType != TestCostants.DatabaseType.MYSQL){
				doTearDown();
			}
			super.setUp();
			
			if(dbType == null) dbType = TestCostants.DatabaseType.MYSQL;
			
			if(rootModel == null) {
				rootModel = TestModelFactory.createModel( dbType );
				if(rootModel != null && rootModel.getPhysicalModels() != null && rootModel.getPhysicalModels().size() > 0) {
					physicalModel = rootModel.getPhysicalModels().get(0);
				}
				if(rootModel != null && rootModel.getBusinessModels() != null && rootModel.getBusinessModels().size() > 0) {
					businessModel = rootModel.getBusinessModels().get(0);
				}
			}
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	public void testModelInitializationSmoke() {
		super.testModelInitializationSmoke();
	}
	
	public void testPhysicalModelInitializationSmoke() {
		super.testPhysicalModelInitializationSmoke();
	}
	
	public void testBusinessModelInitializationSmoke() {
		super.testBusinessModelInitializationSmoke();	
	}
	
	public void testSerializationSmoke() {
		File modelFile = new File(TestCostants.outputFolder, "emfmodel.xmi");
		if(modelFile.exists()) modelFile.delete();
		try {
			IModelSerializer serializer = new EmfXmiSerializer();
			serializer.serialize(rootModel, modelFile);
		} catch(Throwable t) {
			t.printStackTrace();
			fail();
		}
		
		Assert.assertTrue(modelFile.exists());
	}
	
	public void testDeserializationSmoke() {
		Model model = null;
		File modelFile = new File(TestCostants.outputFolder, "emfmodel.xmi");
		try {
			IModelSerializer serializer = new EmfXmiSerializer();
			model = serializer.deserialize( modelFile );
		} catch(Throwable t) {
			t.printStackTrace();
			fail();
		}
		
		Assert.assertNotNull(model);
		Assert.assertNotNull(model.getBusinessModels());
		Assert.assertEquals(1, model.getPhysicalModels().size());
		Assert.assertEquals(1, model.getBusinessModels().size());
	}
	
	
}
