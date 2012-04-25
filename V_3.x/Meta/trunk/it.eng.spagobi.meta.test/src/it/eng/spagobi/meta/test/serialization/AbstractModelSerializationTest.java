package it.eng.spagobi.meta.test.serialization;

import it.eng.spagobi.meta.test.AbstractSpagoBIMetaTest;


public class AbstractModelSerializationTest extends AbstractSpagoBIMetaTest {


	public AbstractModelSerializationTest() {
		super();
	}
	
	
	// add generic tests related to model here ...
	
	public void testModelInitializationSmoke() {
		assertNotNull("Metamodel cannot be null", rootModel);
	}
	
	public void testPhysicalModelInitializationSmoke() {
		assertTrue("Metamodel must have one physical model ", rootModel.getPhysicalModels().size() == 1);
	}
	
	public void testBusinessModelInitializationSmoke() {
		assertTrue("Metamodel must have one business model ", rootModel.getBusinessModels().size() == 1);	
	}
}
