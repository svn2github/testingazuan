package it.eng.spagobi.meta.test.initializer.postgres;

import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.test.TestCostants;
import it.eng.spagobi.meta.test.TestModelFactory;
import it.eng.spagobi.meta.test.initializer.AbstractPhysicalModelInizializtaionTest;

import java.util.HashSet;
import java.util.List;
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
	
	/**
	 * Test that all tables contained in the database are imported in the physical model
	 */
	public void testPhysicalModelTables() {
		
		Assert.assertEquals(TestCostants.POSTGRES_TABLE_NAMES.length, physicalModel.getTables().size());
		
		for(int i = 0; i < TestCostants.POSTGRES_TABLE_NAMES.length; i++) {
			PhysicalTable table = physicalModel.getTable(TestCostants.POSTGRES_TABLE_NAMES[i]);
			Assert.assertNotNull("Physical model does not contain table [" + TestCostants.POSTGRES_TABLE_NAMES[i] + "]", table);
		}	
	}
	
	/**
	 * Test that table comments are imported properly in the physical model
	 * 
	 * TODO test encoding related problems
	 */
	public void testPhysicalModelTableComments() {
		PhysicalTable table = null;
		table = physicalModel.getTable("currency");
		Assert.assertEquals("currency table&apos;s comment", table.getComment());
	}
	
	/**
	 * Test that table types (TABLE or VIEW) are imported properly in the physical model
	 * 
	 * TODO find out why postgres is unable to distinguish view from table
	 */
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
	
	/**
	 * Test that column of table <code>currency</code> and view <code>cUrReNcY</code> 
	 * are imported properly in the physical model
	 */
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

	/**
	 * Test that properties id and uniqueName of physical columns is null.
	 * This two properties are used in the business model but not in the physical model.
	 */
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
	
	/**
	 * Test physical column property related to column type:
	 *  - Type name
	 *  - Data type
	 *  - Radix
	 *  - Decimal digits
	 *  - Octect length
	 *  - Size
	 */
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
	
	/**
	 * Test that column comments are imported properly in the physical model
	 * 
	 * NOTE: if comment is not set MYSQL returns an empty string while POSTGRES 
	 * returns null.
	 * 
	 * TODO uniform the behaviour in the two cases
	 */
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
		
		Assert.assertEquals(null, column.getComment());	
		Assert.assertEquals(null, column.getDescription());
	}
	
	/**
	 * Test the physical columns nullable property
	 * 
	 * NOTE: in POSTGRES a view does not inherit the value of the nullable property 
	 * from the original table as in MYSQL
	 */
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
		
		// @see NOTE above
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
	
	/**
	 * Test the physical columns default value
	 * 
	 * NOTE: POSTGRES returns the default value together with its type.
	 * 
	 * TODO remove the  type and store in the model only the default value
	 */
	public void testPhysicalModelColumnDefaultValue() {
		PhysicalTable table = null;
		PhysicalColumn column = null;
		
		table = physicalModel.getTable("currency");
		column = table.getColumn("currency_id");	
		Assert.assertEquals(null, column.getDefaultValue());
		column = table.getColumn("currency");	
		
		// @see the NOTE above
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
	
	/**
	 * Test that if a table have no primary key in the database it 
	 * also have no primary key in the physical model
	 */
	public void testPhysicalModelNoPK() {
		PhysicalTable view = physicalModel.getTable("cUrReNcY");
		
		PhysicalPrimaryKey pk = view.getPrimaryKey();
		Assert.assertNull("PrimaryKey of view [cUrReNcY] must be null", pk);
		
		for(PhysicalColumn column : view.getColumns()) {
			Assert.assertFalse(column.isPrimaryKey());
			Assert.assertFalse(column.isPartOfCompositePrimaryKey());
		}
		
	}
	
	/** 
	 * Test that simple primary keys (i.e. primary key composed by only one column)
	 * are imported properly in the physical model.
	 * 
	 * NOTE: The name of the primary key in MYSQL is always equals to PRIMARY while
	 * in POSTGRES it is equals to the name specified by the user or automatically generated
	 * by the database
	 */
	public void testPhysicalModelSimplePK() {
		PhysicalTable table = physicalModel.getTable("customer");
		PhysicalPrimaryKey pk = table.getPrimaryKey();
		Assert.assertNotNull("PrimaryKey of table [customer] cannot be null", pk);
		// @see NOTE above
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
	
	/** 
	 * Test that composed primary keys (i.e. primary key composed by more than one column)
	 * are imported properly in the physical model.
	 */
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
	
	/**
	 * Test that if a table have no foreign key in the database it 
	 * also have no foreign key in the physical model
	 */
	public void testPhysicalModeNoFK() {
		PhysicalTable table = physicalModel.getTable("product_class");
		
		List<PhysicalForeignKey> foreignKeys = table.getForeignKeys();
		assertNotNull(foreignKeys);
		assertEquals(0, foreignKeys.size());
	}
	
	/**
	 * Test that if a table have one foreign key in the database it 
	 * also have one foreign key in the physical model
	 */
	public void testPhysicalModeSingleFK() {
		PhysicalTable table = physicalModel.getTable("store");
		
		List<PhysicalForeignKey> foreignKeys = table.getForeignKeys();
		assertNotNull(foreignKeys);
		assertEquals(1, foreignKeys.size());
		
		PhysicalForeignKey foreignKey = foreignKeys.get(0);
		
		assertEquals(null, foreignKey.getId());
		assertEquals(null, foreignKey.getName());
		assertEquals(null, foreignKey.getUniqueName());
		assertEquals(null, foreignKey.getDescription());
			
		// source
		assertEquals("fk_store_1", foreignKey.getSourceName());
		
		assertNotNull(foreignKey.getSourceTable());
		assertEquals(table, foreignKey.getSourceTable());
		
		assertNotNull(foreignKey.getSourceColumns());
		assertEquals(1, foreignKey.getSourceColumns().size());
		PhysicalColumn sourceColumn = foreignKey.getSourceColumns().get(0);
		assertNotNull(sourceColumn);
		assertEquals("region_id", sourceColumn.getName());
		assertEquals(table, sourceColumn.getTable());
			
		// destination
		assertEquals("region_pkey", foreignKey.getDestinationName());
		
		assertNotNull(foreignKey.getDestinationTable());
		PhysicalTable destinationTable = physicalModel.getTable("region");
		assertEquals(destinationTable, foreignKey.getDestinationTable());
		
		assertNotNull(foreignKey.getDestinationColumns());
		assertEquals(1, foreignKey.getDestinationColumns().size());
		PhysicalColumn destinationColumn = foreignKey.getDestinationColumns().get(0);
		assertNotNull(destinationColumn);
		assertEquals("region_id", destinationColumn.getName());
		assertEquals(destinationTable, destinationColumn.getTable());
		assertTrue(destinationColumn.isPrimaryKey());
		assertFalse(destinationColumn.isPartOfCompositePrimaryKey());
		
		assertEquals(physicalModel, foreignKey.getModel());
		assertEquals(0, foreignKey.getProperties().size());
	}
	
	/**
	 * Test that if a table have more than one foreign key in the database it 
	 * also have more than one foreign key in the physical model
	 */
	public void testPhysicalModeMultipleFK() {
		PhysicalTable table = physicalModel.getTable("sales_fact_1998");
		
		List<PhysicalForeignKey> foreignKeys = table.getForeignKeys();
		assertNotNull(foreignKeys);
		assertEquals(5, foreignKeys.size());
	}
	
	/**
	 * Test that composite FK (i.e. FK composed by more than one column) are
	 * imported properly in the physical model.
	 */
	public void testPhysicalModeCompositeFK() {
		PhysicalTable table = physicalModel.getTable("salary");
		
		List<PhysicalForeignKey> foreignKeys = table.getForeignKeys();
		assertNotNull(foreignKeys);
		assertEquals(3, foreignKeys.size());
		
		PhysicalForeignKey foreignKey = null;
		for(PhysicalForeignKey fk : foreignKeys) {
			if(fk.getSourceName().equals("fk_salary_3")) {
				foreignKey = fk;
				break;
			}
		}
		assertNotNull(foreignKey);
		
		assertEquals(null, foreignKey.getId());
		assertEquals(null, foreignKey.getName());
		assertEquals(null, foreignKey.getUniqueName());
		assertEquals(null, foreignKey.getDescription());
			
		// source
		assertEquals("fk_salary_3", foreignKey.getSourceName());
		
		assertNotNull(foreignKey.getSourceTable());
		assertEquals(table, foreignKey.getSourceTable());
		
		assertNotNull(foreignKey.getSourceColumns());
		assertEquals(2, foreignKey.getSourceColumns().size());
		PhysicalColumn sourceColumn1 = foreignKey.getSourceColumns().get(0);
		assertNotNull(sourceColumn1);
		assertEquals("pay_date", sourceColumn1.getName());
		assertEquals(table, sourceColumn1.getTable());
		PhysicalColumn sourceColumn2 = foreignKey.getSourceColumns().get(1);
		assertNotNull(sourceColumn2);
		assertEquals("currency_id", sourceColumn2.getName());
		assertEquals(table, sourceColumn2.getTable());
			
		// destination
		assertEquals("currency_pkey", foreignKey.getDestinationName());
		
		assertNotNull(foreignKey.getDestinationTable());
		PhysicalTable destinationTable = physicalModel.getTable("currency");
		assertEquals(destinationTable, foreignKey.getDestinationTable());
		
		assertNotNull(foreignKey.getDestinationColumns());
		assertEquals(2, foreignKey.getDestinationColumns().size());
		
		PhysicalColumn destinationColumn1 = foreignKey.getDestinationColumns().get(0);
		assertNotNull(destinationColumn1);
		assertEquals("date", destinationColumn1.getName());
		assertEquals(destinationTable, destinationColumn1.getTable());
		assertTrue(destinationColumn1.isPrimaryKey());
		assertTrue(destinationColumn1.isPartOfCompositePrimaryKey());
		
		
		PhysicalColumn destinationColumn2 = foreignKey.getDestinationColumns().get(1);
		assertNotNull(destinationColumn2);
		assertEquals("currency_id", destinationColumn2.getName());
		assertEquals(destinationTable, destinationColumn2.getTable());
		assertTrue(destinationColumn2.isPrimaryKey());
		assertTrue(destinationColumn2.isPartOfCompositePrimaryKey());
		
		assertEquals(physicalModel, foreignKey.getModel());
		assertEquals(0, foreignKey.getProperties().size());
	}
}
