package it.eng.spagobi.meta.test.edit.mysql;

import java.io.File;
import java.util.List;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.edit.AbstractModelEditingTest;
import it.eng.spagobi.meta.test.initializer.AbstractBusinessModelInizializtaionTest;
import it.eng.spagobi.meta.test.serialization.AbstractModelSerializationTest;
import it.eng.spagobi.meta.test.serialization.EmfXmiSerializer;
import it.eng.spagobi.meta.test.serialization.IModelSerializer;

import org.junit.Assert;


public class MySqlModelEditingTest extends AbstractModelEditingTest {

	public void setUp() throws Exception {
		super.setUp();
		try {
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
