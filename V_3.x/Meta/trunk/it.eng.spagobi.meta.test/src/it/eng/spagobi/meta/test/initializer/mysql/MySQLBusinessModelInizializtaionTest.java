package it.eng.spagobi.meta.test.initializer.mysql;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
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
		
		Assert.assertEquals(tableNames.length, businessModel.getTables().size());
		
		for(int i = 0; i < tableNames.length; i++) {
			List<BusinessTable> businessTables = businessModel.getBusinessTableByPhysicalTable(tableNames[i]);
			Assert.assertNotNull(businessTables);
			Assert.assertFalse("Business model does not contain table [" + tableNames[i] + "]", businessTables.size() == 0);
			Assert.assertFalse("Business model contains table [" + tableNames[i] + "] more than one time", businessTables.size() > 1);
		}	
	}
	
	public void testBusinessModelTableUniqueNames() {
		Map<String, String> tableUniqueNames = new HashMap<String, String>();
		for(BusinessTable table: businessModel.getBusinessTables()) {
			String tableUniqueName = table.getUniqueName();
			String physicalTableName = table.getPhysicalTable().getName();
			Assert.assertFalse("Unique name [] of table [" + physicalTableName + "] is equal to unique name of table [" + tableUniqueNames.get(tableUniqueName)+ "]"
					, tableUniqueNames.containsKey(tableUniqueName));
			tableUniqueNames.put(tableUniqueName, physicalTableName);
		}
	}
	
	public void testBusinessModelColumnUniqueNames() {
		for(BusinessTable table: businessModel.getBusinessTables()) {
			String physicalTableName = table.getPhysicalTable().getName();
			Map<String, String> columnUniqueNames = new HashMap<String, String>();
			for(BusinessColumn column: table.getColumns()) {
				String columnUniqueName = column.getUniqueName();
				String physicalColumnName = column.getName();
				if(column instanceof SimpleBusinessColumn) {
					physicalColumnName = ((SimpleBusinessColumn)column).getPhysicalColumn().getName();
				}
				
				Assert.assertFalse("Column [" + physicalColumnName + "] and column [" + columnUniqueNames.get(columnUniqueName) + "] of table [" + physicalTableName + "] have the same unique name [" + columnUniqueName + "]"
						, columnUniqueNames.containsKey(columnUniqueName));
				
				columnUniqueNames.put(columnUniqueName, physicalColumnName);
			}
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
