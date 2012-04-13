package it.eng.spagobi.meta.test.initializer.mysql;

import it.eng.spagobi.meta.generator.jpamapping.wrappers.JpaProperties;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.initializer.AbstractBusinessModelInizializtaionTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;


public class MySQLBusinessModelInizializtaionTest extends AbstractBusinessModelInizializtaionTest {

	public MySQLBusinessModelInizializtaionTest() {
		this.setName("Busienss model initialization tests on MySql");
	}
	
	public void setUp() throws Exception {
		super.setUp();
		try {
			if(dbType == null) dbType = TestCostants.DatabaseType.MYSQL;
			
			if(model == null) {
				model = TestModelFactory.createModel( dbType );
				if(model != null && model.getPhysicalModels() != null && model.getPhysicalModels().size() > 0) {
					physicalModel = model.getPhysicalModels().get(0);
				}
				if(model != null && model.getBusinessModels() != null && model.getBusinessModels().size() > 0) {
					businessModel = model.getBusinessModels().get(0);
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
	public void testBusinessModelPakageProperty() {
		ModelProperty packageProperty =  businessModel.getProperties().get(JpaProperties.MODEL_PACKAGE);
	    Assert.assertNotNull("Property [" + JpaProperties.MODEL_PACKAGE + "] is not defined in business model", packageProperty);
		
		String packageName = null;
		//check if property is setted, else get default value
	    if (packageProperty.getValue() != null){
	    	packageName = packageProperty.getValue();
	    } else {
	    	packageName = packageProperty.getPropertyType().getDefaultValue();
	    }
	    Assert.assertNotNull("Property [" + JpaProperties.MODEL_PACKAGE + "] have no value", packageName);
    }
    
	
	// =======================================================
	// TABLES
	// =======================================================
	
	public void testBusinessModelTables() {
		
		Assert.assertEquals(TestCostants.MYSQL_TABLE_NAMES.length, businessModel.getTables().size());
		
		for(int i = 0; i < TestCostants.MYSQL_TABLE_NAMES.length; i++) {
			List<BusinessTable> businessTables = businessModel.getBusinessTableByPhysicalTable(TestCostants.MYSQL_TABLE_NAMES[i]);
			Assert.assertNotNull(businessTables);
			Assert.assertFalse("Business model does not contain table [" + TestCostants.MYSQL_TABLE_NAMES[i] + "]", businessTables.size() == 0);
			Assert.assertFalse("Business model contains table [" + TestCostants.MYSQL_TABLE_NAMES[i] + "] more than one time", businessTables.size() > 1);
		}	
	}
	
	public void testBusinessModelTableUniqueNames() {
		Map<String, String> tableUniqueNames = new HashMap<String, String>();
		
		for(BusinessTable table: businessModel.getBusinessTables()) {
			String tableUniqueName = table.getUniqueName();
			String physicalTableName = table.getPhysicalTable().getName();
			
			Assert.assertNotNull("Business table associated with physical table [" + physicalTableName + "] have no unique name", tableUniqueName);
			Assert.assertNotNull("Business table associated with physical table [" + physicalTableName + "] have an empty unique name", physicalTableName.trim().length() > 0 );
			
			char firstChar = tableUniqueName.charAt(0);
			Assert.assertTrue("The unique name [" + tableUniqueName + "] of business table associated with physical table [" + physicalTableName + "] does not start with a letter", Character.isLetter(firstChar));
						
			Assert.assertFalse("Unique name [" + tableUniqueName + "] of table [" + physicalTableName + "] is equal to unique name of table [" + tableUniqueNames.get(tableUniqueName.toLowerCase())+ "]"
					, tableUniqueNames.containsKey(tableUniqueName.toLowerCase()));
			
			// note: name must be unique in a case unsensitive way: !name1.equalsIgnoreCase(name2)
			tableUniqueNames.put(tableUniqueName.toLowerCase(), physicalTableName);
		}
	}
	
	public void testBusinessModelTableNames() {
		for(BusinessTable table: businessModel.getBusinessTables()) {
			String tableUniqueName = table.getName();
			Assert.assertNotNull("Business table associated with physical table [" + table.getPhysicalTable().getName() + "] have no name", tableUniqueName);
		}
	}

	
	// =======================================================
	// COLUMNS
	// =======================================================
	

	public void testBusinessModelColumnUniqueNames() {
		for(BusinessTable table: businessModel.getBusinessTables()) {
			String physicalTableName = table.getPhysicalTable().getName();
			Map<String, String> columnUniqueNames = new HashMap<String, String>();
			for(SimpleBusinessColumn column: table.getSimpleBusinessColumns()) {
				String columnUniqueName = column.getUniqueName();
				String physicalColumnName = column.getPhysicalColumn().getName();				
				
				Assert.assertNotNull("Business column associated to column [" + physicalColumnName + "] of physical table [" + physicalTableName + "] have no  unique name", columnUniqueName);
				Assert.assertFalse("Column [" + physicalColumnName + "] and column [" + columnUniqueNames.get(columnUniqueName) + "] of table [" + physicalTableName + "] have the same unique name [" + columnUniqueName + "]"
						, columnUniqueNames.containsKey(columnUniqueName));
				
				columnUniqueNames.put(columnUniqueName, physicalColumnName);
			}
		}
	}
	
	public void testBusinessModelColumnNames() {
		for(BusinessTable table: businessModel.getBusinessTables()) {
			String physicalTableName = table.getPhysicalTable().getName();
			for(SimpleBusinessColumn column: table.getSimpleBusinessColumns()) {
				String columnName = column.getName();
				Assert.assertNotNull("Business column associated with physical column [" + column.getPhysicalColumn().getName() + "] of table [" + physicalTableName + "] have no name ", columnName);
			}
		}
	}
	
	// =======================================================
	// IDENTIFIERS
	// =======================================================
	
	
	
	// =======================================================
	// RELATIONSHIPS
	// =======================================================
}
