package it.eng.spagobi.meta.test.initializer.postgres;

import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.initializer.AbstractPhysicalModelInizializtaionTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;


public class PostgresPhysicalModelInizializtaionTest extends AbstractPhysicalModelInizializtaionTest {

	public void setUp() throws Exception {
		super.setUp();
		try {
			// if this is the first test on postgres after the execution
			// of tests on an other database force a tearDown to clean
			// and regenerate properly all the static variables contained in
			// parent class AbstractSpagoBIMetaTest
			if(dbType != TestCostants.DatabaseType.POSTGRES){
				doTearDown();
			}
			super.setUp();
			
			if(dbType == null) dbType = TestCostants.DatabaseType.POSTGRES;
						
			if(rootModel == null) {
				setRootModel( TestModelFactory.createModel( dbType ) );
			}
		} catch(Exception t) {
			System.err.println("An unespected error occurred during setUp: ");
			t.printStackTrace();
			throw t;
		}
	}
	
	//  generic tests imported from parent class ...
	
	public void testModelInitializationSmoke() {
		super.testModelInitializationSmoke();
	}
	
	public void testPhysicalModelInitializationSmoke() {
		super.testPhysicalModelInitializationSmoke();
	}
	
	public void testBusinessModelInitializationSmoke() {
		super.testBusinessModelInitializationSmoke();
	}
	
	public void testPropertyConnectionName() {
		super.testPropertyConnectionName();		
	}
	
	public void testPropertyConnectionDatabaseName() {
		super.testPropertyConnectionDatabaseName();
	}
	
	// add specific test here...
	
	// =======================================================
	// PROPERTIES
	// =======================================================
	
	
	
	public void testPropertyConnectionDriver() {
		super.testPropertyConnectionName();
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.driver";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertEquals(TestCostants.POSTGRES_DRIVER, modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionUrl() {
		super.testPropertyConnectionName();
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.url";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertEquals(TestCostants.POSTGRES_URL, modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionUser() {
		super.testPropertyConnectionName();
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.username";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertEquals(TestCostants.POSTGRES_USER, modelPropertyType.getDefaultValue());
	}
	
	public void testPropertyConnectionPassword() {
		super.testPropertyConnectionName();
		PhysicalModel physicalModel = rootModel.getPhysicalModels().get(0);
		String modelPropertyTypeName = "connection.password";
		ModelPropertyType modelPropertyType = physicalModel.getPropertyType(modelPropertyTypeName);
		Assert.assertEquals(TestCostants.POSTGRES_PWD, modelPropertyType.getDefaultValue());
	}
	
	public void testPhysicalModelSourceDatabase() {
		super.testPhysicalModelSourceDatabase();
		Assert.assertTrue("Database name [" + physicalModel.getDatabaseName().toLowerCase() + "] does not contain word [mysql]"
				, physicalModel.getDatabaseName().toLowerCase().contains("postgres"));		
	}
	
	public void testPhysicalModelCatalog() {
		Assert.assertNotNull("If the source database is Postgres the catalog property cannot be null", physicalModel.getCatalog());
		Assert.assertEquals(TestCostants.POSTGRES_DEFAULT_CATALOG, physicalModel.getCatalog());
	}
	
	public void testPhysicalModelSchema() {
		Assert.assertNotNull("If the source database is Postgres the schema property cannot be null", physicalModel.getSchema());
		Assert.assertEquals(TestCostants.POSTGRES_DEFAULT_SCHEMA, physicalModel.getSchema());
	}
	
	// =======================================================
	// TABLES
	// =======================================================
	
	public void testPhysicalModelTables() {
		
		Assert.assertEquals(TestCostants.POSTGRES_TABLE_NAMES.length, physicalModel.getTables().size());
		
		for(int i = 0; i < TestCostants.POSTGRES_TABLE_NAMES.length; i++) {
			PhysicalTable table = physicalModel.getTable(TestCostants.POSTGRES_TABLE_NAMES[i]);
			Assert.assertNotNull("Physical model does not contain table [" + TestCostants.POSTGRES_TABLE_NAMES[i] + "]", table);
		}	
	}
	
	// TODO test encoding related problems
	public void testPhysicalModelTableComments() {
		PhysicalTable table = null;
		table = physicalModel.getTable("currency");
		Assert.assertEquals("currency table&apos;s comment", table.getComment());
	}
	
	public void testPhysicalModelTableTypes() {
		PhysicalTable table = null;
		PhysicalTable view = null;
		
		table = physicalModel.getTable("currency");
		Assert.assertEquals("TABLE", table.getType());
		
		// TODO verify why postgres is unable to distinguish view from table
		view = physicalModel.getTable("cUrReNcY");
		Assert.assertEquals("TABLE", table.getType());

		Assert.assertFalse(view.getName().equals(table.getName()));
		Assert.assertFalse(view.equals(table));
	}
	
	// =======================================================
	// COLUMNS
	// =======================================================
	
	public void testPhysicalModelColumns() {
		PhysicalTable table = null;
		PhysicalColumn column = null;

		String[] columnNames = new String[]{
			"currency_id"
			, "date"
			, "currency"
			, "conversion_ratio"
		};
		
		table = physicalModel.getTable("currency");
		Assert.assertEquals(4, table.getColumns().size());
		for(int i = 0; i < columnNames.length; i++ ) {
			column = table.getColumn(columnNames[i]);
			Assert.assertNotNull("Table [currency] does not cotain column [" + columnNames[i] + "] as expected", column);	
			Assert.assertEquals(columnNames[i], column.getName());
			Assert.assertEquals(table, column.getTable());
			Assert.assertEquals((i+1), column.getPosition());
		}
		
		table = physicalModel.getTable("cUrReNcY");
		Assert.assertEquals(4, table.getColumns().size());
		for(int i = 0; i < columnNames.length; i++ ) {
			column = table.getColumn(columnNames[i]);
			Assert.assertNotNull("Table [currency_view] does not cotain column [" + columnNames[i] + "] as expected", column);	
			Assert.assertEquals(columnNames[i], column.getName());
			Assert.assertEquals(table, column.getTable());
			Assert.assertEquals((i+1), column.getPosition());
		}	
	}
	
	public void testPhysicalModelColumnId() {
		PhysicalTable table = null;
		PhysicalColumn column = null;
		
		table = physicalModel.getTable("currency");
		column = table.getColumn("currency_id");	
		Assert.assertEquals(null, column.getId());
		Assert.assertEquals(null, column.getUniqueName());
		column = table.getColumn("conversion_ratio");	
		Assert.assertEquals(null, column.getId());
		Assert.assertEquals(null, column.getUniqueName());
		
		table = physicalModel.getTable("cUrReNcY");
		column = table.getColumn("currency_id");	
		Assert.assertEquals(null, column.getId());
		Assert.assertEquals(null, column.getUniqueName());
		column = table.getColumn("conversion_ratio");	
		Assert.assertEquals(null, column.getId());
		Assert.assertEquals(null, column.getUniqueName());
		
	}
	
	public void testPhysicalModelColumnTypes() {
		PhysicalTable table = null;
		PhysicalColumn column = null;
		
		String[] columnNames = new String[]{
			"currency_id"
			, "date"
			, "currency"
			, "conversion_ratio"
		};
		
		table = physicalModel.getTable("currency");
		
		// `currency_id` int(11) NOT NULL
		column = table.getColumn("currency_id");
		Assert.assertEquals("numeric", column.getTypeName());
		Assert.assertEquals("NUMERIC", column.getDataType());
		Assert.assertEquals(10, column.getRadix());	
		Assert.assertEquals(0, column.getDecimalDigits());
		//Assert.assertEquals(11, column.getOctectLength());
		Assert.assertEquals(11, column.getSize());
		
		// `date` date NOT NULL,
		column = table.getColumn("date");
		Assert.assertEquals("date", column.getTypeName());
		Assert.assertEquals("DATE", column.getDataType());
		Assert.assertEquals(0, column.getDecimalDigits());
		
		//`currency` varchar(30) NOT NULL,
		column = table.getColumn("currency");
		Assert.assertEquals("varchar", column.getTypeName());
		Assert.assertEquals("VARCHAR", column.getDataType());
		Assert.assertEquals(0, column.getDecimalDigits());
		//Assert.assertEquals(30, column.getOctectLength());
		Assert.assertEquals(30, column.getSize());
		
		//`conversion_ratio` decimal(10,4) NOT NULL,
		column = table.getColumn("conversion_ratio");
		Assert.assertEquals("numeric", column.getTypeName());
		Assert.assertEquals("NUMERIC", column.getDataType());
		Assert.assertEquals(10, column.getRadix());	
		Assert.assertEquals(4, column.getDecimalDigits());
		//Assert.assertEquals(10, column.getOctectLength());
		Assert.assertEquals(10, column.getSize());	
	}
	
	public void testPhysicalModelColumnComments() {
		PhysicalTable table = null;
		PhysicalColumn column = null;
		
		table = physicalModel.getTable("currency");
		column = table.getColumn("currency_id");
		Assert.assertEquals("Composed id column 1", column.getComment());	
		Assert.assertEquals(null, column.getDescription());	
		column = table.getColumn("date");
		Assert.assertEquals("Composed id column 2", column.getComment());	
		Assert.assertEquals(null, column.getDescription());
		column = table.getColumn("currency");
		// NOTE mysql if comment is not 
		// set returns an empty string while postgres return null
		// TODO uniform the behaviour in the two cases
		Assert.assertEquals(null, column.getComment());	
		Assert.assertEquals(null, column.getDescription());
	}
	
	public void testPhysicalModelColumnNullableProperty() {
		PhysicalTable table = null;
		PhysicalColumn column = null;
		
		table = physicalModel.getTable("currency");
		column = table.getColumn("currency_id");	
		Assert.assertEquals(false, column.isNullable());	
		
		column = table.getColumn("date");	
		Assert.assertEquals(false, column.isNullable());
		
		column = table.getColumn("currency");	
		Assert.assertEquals(false, column.isNullable());
		
		column = table.getColumn("conversion_ratio");	
		Assert.assertEquals(false, column.isNullable());
		
		// TODO verify why in postgres the view does not inherit
		// the value of the nullable property from the original table
		table = physicalModel.getTable("cUrReNcY");
		column = table.getColumn("currency_id");	
		Assert.assertEquals(true, column.isNullable());	
		
		column = table.getColumn("date");	
		Assert.assertEquals(true, column.isNullable());
		
		column = table.getColumn("currency");	
		Assert.assertEquals(true, column.isNullable());
		
		column = table.getColumn("conversion_ratio");	
		Assert.assertEquals(true, column.isNullable());
		
		table = physicalModel.getTable("customer");
		column = table.getColumn("address1");	
		Assert.assertEquals(true, column.isNullable());
	}
	
	public void testPhysicalModelColumnDefaultValue() {
		PhysicalTable table = null;
		PhysicalColumn column = null;
		
		table = physicalModel.getTable("currency");
		column = table.getColumn("currency_id");	
		Assert.assertEquals(null, column.getDefaultValue());
		column = table.getColumn("currency");	
		
		// NOTE postgres return the default value together with its type
		// TODO remove the  type an store in the model only the default value
		Assert.assertEquals("'USD'::character varying", column.getDefaultValue());
		
		table = physicalModel.getTable("cUrReNcY");
		column = table.getColumn("currency_id");	
		Assert.assertEquals(null, column.getDefaultValue());
		column = table.getColumn("currency");	
		Assert.assertEquals(null, column.getDefaultValue());
	}
	
	
	// =======================================================
	// P-KEYS
	// =======================================================
	
	public void testPhysicalModelNoPK() {
		PhysicalTable view = physicalModel.getTable("cUrReNcY");
		
		PhysicalPrimaryKey pk = view.getPrimaryKey();
		Assert.assertNull("PrimaryKey of view [cUrReNcY] must be null", pk);
		
		for(PhysicalColumn column : view.getColumns()) {
			Assert.assertFalse(column.isPrimaryKey());
			Assert.assertFalse(column.isPartOfCompositePrimaryKey());
		}
		
	}
	
	public void testPhysicalModelSimplePK() {
		PhysicalTable table = physicalModel.getTable("customer");
		PhysicalPrimaryKey pk = table.getPrimaryKey();
		Assert.assertNotNull("PrimaryKey of table [customer] cannot be null", pk);
		// TODO verify why MySQL instead return always PRIMARY
		Assert.assertEquals("customer_pkey", pk.getName());
		
		Assert.assertTrue("PrimaryKey of table [customer] is composed by [" + pk.getColumns().size() + "] column(s) and not 1 as expected", pk.getColumns().size() == 1 );
		Set<String> pkColumnNames = new HashSet<String>();
		pkColumnNames.add(pk.getColumns().get(0).getName());
		Assert.assertTrue("Column [customer_id] of table [customer] is not part of the PK as expected", pkColumnNames.contains("customer_id"));
		
		PhysicalColumn column = table.getColumn("customer_id");
		Assert.assertTrue(column == pk.getColumns().get(0));
		Assert.assertTrue(column.equals(pk.getColumns().get(0)));
		Assert.assertTrue(column.isPrimaryKey());
		Assert.assertFalse(column.isPartOfCompositePrimaryKey());
	}
	
	public void testPhysicalModelComposedPK() {
		PhysicalTable table = physicalModel.getTable("currency");
		PhysicalPrimaryKey pk = table.getPrimaryKey();
		Assert.assertNotNull("PrimaryKey of table [currency] cannot be null", pk);
		Assert.assertEquals("currency_pkey", pk.getName());
		
		Assert.assertTrue("PrimaryKey of table [currency] is composed by [" + pk.getColumns().size() + "] column(s) and not 2 as expected", pk.getColumns().size() == 2 );
		Set<String> pkColumnNames = new HashSet<String>();
		pkColumnNames.add(pk.getColumns().get(0).getName());
		pkColumnNames.add(pk.getColumns().get(1).getName());
		Assert.assertTrue("Column [currency_id] of table [currency] is not part of the PK as expected", pkColumnNames.contains("currency_id"));
		Assert.assertTrue("Column [date] of table [currency] is not part of the PK as expected", pkColumnNames.contains("date"));
		
		PhysicalColumn column = table.getColumn("currency_id");
		Assert.assertTrue(column == pk.getColumns().get(0) || column == pk.getColumns().get(1));
		Assert.assertTrue(column.equals(pk.getColumns().get(0)) || column.equals(pk.getColumns().get(1)));
		Assert.assertTrue(column.isPrimaryKey());
		Assert.assertTrue(column.isPartOfCompositePrimaryKey());
	}
	
	// =======================================================
	// F-KEYS
	// =======================================================
}
