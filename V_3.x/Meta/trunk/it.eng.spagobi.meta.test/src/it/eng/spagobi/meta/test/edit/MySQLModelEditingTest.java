package it.eng.spagobi.meta.test.edit;

import java.util.List;

import it.eng.spagobi.meta.generator.jpamapping.JpaMappingCodeGenerator;
import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelFactory;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.initializer.TestConnectionFactory;

import org.junit.Before;


public class MySQLModelEditingTest extends AbstractModelEditingTest{

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
	
	
	public void testBusinessModelEditing() {
		BusinessTable businessTable = businessModel.getBusinessTables().get(0);
		BusinessIdentifier  businessIdentifier = businessTable.getIdentifier();
		List<BusinessRelationship> businessRelationships = businessTable.getRelationships();
		String businessTableName = businessTable.getName(); 
		
		assertNotNull("Business table cannot be null", businessModel.getBusinessTable(businessTableName));
		if(businessIdentifier != null) {
			assertNotNull("Business identifier cannot be null", businessModel.getIdentifier(businessTable));
		}
		if(businessRelationships != null && businessRelationships.size() > 0) {
			for(BusinessRelationship r : businessRelationships) {
				assertNotNull("Business identifier cannot be null", businessModel.getRelationships().contains(r));
			}
		}
		
		assertTrue("Impossible to delete table", businessModel.deleteBusinessTable(businessTableName));
		
		assertTrue( "Business Tabele has not been removes properly", businessModel.getBusinessTable(businessTableName) == null);
		
		if(businessIdentifier != null) {
			assertTrue("Business identifier cannot be null", businessModel.getIdentifier(businessTable) == null);
		}
		if(businessRelationships != null && businessRelationships.size() > 0) {
			for(BusinessRelationship r : businessRelationships) {
				assertTrue("Business identifier cannot be null", businessModel.getRelationships().contains(r) == false);
			}
		}
	}
}
