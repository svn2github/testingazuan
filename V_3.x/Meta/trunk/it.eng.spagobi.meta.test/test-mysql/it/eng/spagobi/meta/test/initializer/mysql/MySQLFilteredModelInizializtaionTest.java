package it.eng.spagobi.meta.test.initializer.mysql;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.initializer.AbstractBusinessModelInizializtaionTest;

import java.util.List;

import org.junit.Assert;


public class MySQLFilteredModelInizializtaionTest extends AbstractBusinessModelInizializtaionTest {
	
	
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
						
			if(filteredModel == null) {
				setFilteredModel( TestModelFactory.createFilteredModel( dbType ) );
			}
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	// add specific test here...
	
	public void testModelInitializationSmoke() {
		assertNotNull("Metamodel cannot be null", filteredModel);
	}
	
	public void testPhysicalModelInitializationSmoke() {
		assertTrue("Metamodel must have one physical model ", filteredModel.getPhysicalModels().size() == 1);
	}
	
	public void testBusinessModelInitializationSmoke() {
		assertTrue("Metamodel must have one business model ", filteredModel.getBusinessModels().size() == 1);	
	}

	
	// =======================================================
	// TABLES
	// =======================================================
	
	public void testPhysicalModelTables() {
		
		Assert.assertEquals(TestCostants.MYSQL_FILTERED_TABLES_FOR_PMODEL.length, filteredPhysicalModel.getTables().size());
		
		for(int i = 0; i < TestCostants.MYSQL_FILTERED_TABLES_FOR_PMODEL.length; i++) {
			PhysicalTable physicalTable = filteredPhysicalModel.getTable( TestCostants.MYSQL_FILTERED_TABLES_FOR_PMODEL[i] );
			Assert.assertNotNull(physicalTable);
		}	
	}
	
	public void testBusinessModelTables() {
		
		Assert.assertEquals(TestCostants.MYSQL_FILTERED_TABLES_FOR_BMODEL.length, filteredBusinessModel.getTables().size());
		
		for(int i = 0; i < TestCostants.MYSQL_FILTERED_TABLES_FOR_BMODEL.length; i++) {
			List<BusinessTable> businessTables = businessModel.getBusinessTableByPhysicalTable(TestCostants.MYSQL_FILTERED_TABLES_FOR_BMODEL[i]);
			Assert.assertNotNull(businessTables);
			Assert.assertFalse("Business model does not contain table [" + TestCostants.MYSQL_TABLE_NAMES[i] + "]", businessTables.size() == 0);
			Assert.assertFalse("Business model contains table [" + TestCostants.MYSQL_TABLE_NAMES[i] + "] more than one time", businessTables.size() > 1);
		}	
	}
	
	
	// =======================================================
	// COLUMNS
	// =======================================================
	

	
	// =======================================================
	// IDENTIFIERS
	// =======================================================
	
	
	
	// =======================================================
	// RELATIONSHIPS
	// =======================================================
}
