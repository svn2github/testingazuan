package it.eng.spagobi.meta.test.initializer.mysql;

import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.initializer.AbstractBusinessModelInizializtaionTest;

import org.junit.Assert;


public class MySQLBusinessModelInizializtaionTest extends AbstractBusinessModelInizializtaionTest {

	public MySQLBusinessModelInizializtaionTest() {
		this.setName("Busienss model initialization tests on MySql");
	}
	
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
	
	// add specific test here...
	
	public void testModelInitializationSmoke() {
		super.testModelInitializationSmoke();
	}
	
	public void testPhysicalModelInitializationSmoke() {
		super.testPhysicalModelInitializationSmoke();
	}
	
	public void testBusinessModelInitializationSmoke() {
		super.testBusinessModelInitializationSmoke();
	}
	

	// =======================================================
	// PROPERTIES
	// =======================================================
	
	// TODO ...
	
	// =======================================================
	// TABLES
	// =======================================================
	
	public void testBusinessModelTables() {
		String[] tableNames = new String[]{
			"currency"
			, "currency_view"
			, "customer"
			, "days"
			, "department"
			, "employee"
			, "employee_closure"
			, "inventory_fact_1998"
			, "position"
			, "product"
			, "product_class"
			, "promotion"
			, "region"
			, "reserve_employee"
			, "salary"
			, "sales_fact_1998"
			, "sales_region"
			, "store"
			, "store_ragged"
			, "time_by_day"
			, "tmpsbiqbe_bi"
			, "tmpsbiqbe_biadmin"
			, "warehouse"
			, "warehouse_class"
		};
		
		Assert.assertEquals(tableNames.length, businessModel.getTables().size());
		
		for(int i = 0; i < tableNames.length; i++) {
			PhysicalTable physicalTable = physicalModel.getTable(tableNames[i]);
			BusinessTable businessTable = businessModel.getBusinessTable(physicalTable);
			Assert.assertNotNull("Business model does not contain table [" + tableNames[i] + "]", businessTable);
		}	
	}
	
	
	
	// =======================================================
	// COLUMNS
	// =======================================================
	
	
	// =======================================================
	// P-KEYS
	// =======================================================
	
	
	
	// =======================================================
	// F-KEYS
	// =======================================================
}
