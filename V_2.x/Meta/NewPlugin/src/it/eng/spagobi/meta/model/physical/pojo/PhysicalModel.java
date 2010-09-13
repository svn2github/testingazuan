/**
 * 
 */
package it.eng.spagobi.meta.model.physical.pojo;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalModel {
	
	/* ----------------------------------------------------------------
	 * PhysicalModel metadata
	 * ---------------------------------------------------------------- 
	 */
	
	// model name
	private String name;
	
	/* ----------------------------------------------------------------
	 * Referenced objects
	 * ---------------------------------------------------------------- 
	 */
	
	// OWNED BY ...
	
	// none: the PhysicalModel is the highest object in the hierarchy
	
	// OWNER OF ...
	
	// database name (ex. Oracle, MySql, ecc ...)
	private String databaseName;
	
	// database version (ex. 9i, 5.1, ecc ...)
	private String databaseVersion;
	
	// cataog name (maybe null)
	private String catalog;
	
	// schema name (maybe null)
	private String schema;
	
	// tables contained in the given schema
	private List<PhysicalTable> tables;
	
	// foreign keys contained in the given schema
	private List<PhysicalPrimaryKey> primaryKeys;
	
	// foreign keys contained in the given schema
	private List<PhysicalForeignKey> foreignKeys;
	
	
	/* ----------------------------------------------------------------
	 * Cached objects
	 * ---------------------------------------------------------------- 
	 */
	private Map<String, PhysicalTable> tablesLookupMap;
	
	
	/* ----------------------------------------------------------------
	 * Costructor methods
	 * ---------------------------------------------------------------- 
	 */
	
	public PhysicalModel(String name) {
		this.name = name;
		tables = new ArrayList<PhysicalTable>();
		primaryKeys  = new ArrayList<PhysicalPrimaryKey>();
		foreignKeys = new ArrayList<PhysicalForeignKey>();
		tablesLookupMap = new HashMap<String, PhysicalTable>();
	}
	
	
	/* ----------------------------------------------------------------
	 * Accessor methods 
	 * ---------------------------------------------------------------- 
	 */
	
	
	// META
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
	// OWNED OBJECTS
	public String getDatabaseName() {
		return databaseName;
	}
	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}
	public String getDatabaseVersion() {
		return databaseVersion;
	}
	public void setDatabaseVersion(String databaseVersion) {
		this.databaseVersion = databaseVersion;
	}
	public void setDatabaseVersion(String databaseName, String databaseVersion) {
		this.databaseName = databaseName;
		this.databaseVersion = databaseVersion;
	}
	
	public String getCatalog() {
		return catalog;
	}
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	
	// tables ...	
	public List<PhysicalTable> getTables() {
		return tables;
	}
	public void addTables(List<PhysicalTable> tables) {
		for(int i = 0; i < tables.size(); i++) {
			addTable( tables.get(i) );
		}
	}
	public void addTable(PhysicalTable table) {
		table.setModel(this);
		tables.add(table);
		tablesLookupMap.put(table.getName(), table);
	}
	
	
	
	// primary keys 
	public void addPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys) {
		for(int i = 0; i < primaryKeys.size(); i++) {
			addPrimaryKey( primaryKeys.get(i) );
		}
	}	
	public void addPrimaryKey(PhysicalPrimaryKey primaryKey) {
		PhysicalTable table;
		PhysicalColumn column;
		
		try {
			table = getTable(primaryKey.getTableName());
			if(table == null) {
				throw new RuntimeException("Impossible to add primary key [" + primaryKey.getPkName() + "]: table [" + primaryKey.getTableName() +"] not found");
			}
			
			
			for(int i = 0; i < primaryKey.getColumnNames().size(); i++) {
				column = table.getColumn( primaryKey.getColumnNames().get(i) );
				if(column == null) {
					throw new RuntimeException("Impossible to add primary key [" + primaryKey.getPkName() + "]: column [" + primaryKey.getColumnNames().get(i) +"] not found");
				}
				primaryKey.addColumn( column );
			}
			
			table.addPrimaryKey(primaryKey);
			primaryKey.setTable(table);
			
			primaryKeys.add(primaryKey);
		} catch(Throwable t) {
			throw new RuntimeException("Impossible to add primary key [" + primaryKey.getPkName() + "] to model [" + name +"]", t);
		}
	}
	
	// foreign keys 
	public void addForeignKeys(List<PhysicalForeignKey> foreignKeys) {
		for(int i = 0; i < foreignKeys.size(); i++) {
			addForeignKey( foreignKeys.get(i) );
		}
	}	
	public void addForeignKey(PhysicalForeignKey foreignKey) {
		PhysicalTable fkTable = getTable(foreignKey.getFkTableName());
		PhysicalTable pkTable = getTable(foreignKey.getPkTableName());
		int foreignKeySize = foreignKey.getFkColumnNames().size();
		
		for(int i = 0; i < foreignKeySize; i++) {
			PhysicalColumn fkColumn = fkTable.getColumn( foreignKey.getFkColumnNames().get(i) );
			PhysicalColumn pkColumn = pkTable.getColumn( foreignKey.getPkColumnNames().get(i) );
			
			foreignKey.addFkColumn( fkColumn );
			foreignKey.addPkColumn( pkColumn );
		}
		
		fkTable.addForeignKey(foreignKey);
		foreignKey.setFkTable(fkTable);
		pkTable.addIncomingKey(foreignKey);
		foreignKey.setPkTable(pkTable);
		foreignKeys.add(foreignKey);
	}
	
	
	/* ----------------------------------------------------------------
	 * Lookup methods 
	 * ---------------------------------------------------------------- 
	 */
	public PhysicalTable getTable(String tableName) {
		return tablesLookupMap.get( tableName );
	}
	
}
