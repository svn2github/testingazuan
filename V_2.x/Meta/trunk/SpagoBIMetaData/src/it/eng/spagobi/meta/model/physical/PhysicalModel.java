package it.eng.spagobi.meta.model.physical;

import java.util.List;

/**
 * @model
 */
public interface PhysicalModel {

	/**
	 * @model
	 */
	public abstract String getName();

	/**
	 * @model
	 */
	public abstract void setName(String name);

	/**
	 * @model
	 */
	public abstract String getDatabaseName();

	/**
	 * @model
	 */
	public abstract void setDatabaseName(String databaseName);

	/**
	 * @model
	 */
	public abstract String getDatabaseVersion();

	/**
	 * @model
	 */
	public abstract void setDatabaseVersion(String databaseVersion);

	public abstract void setDatabaseVersion(String databaseName, String databaseVersion);

	/**
	 * @model
	 */
	public abstract String getCatalog();

	/**
	 * @model
	 */
	public abstract void setCatalog(String catalog);

	/**
	 * @model
	 */
	public abstract String getSchema();

	/**
	 * @model
	 */
	public abstract void setSchema(String schema);

	/**
	 * @model
	 */
	public abstract List<PhysicalTable> getTables();

	/**
	 * @model
	 */
	public abstract void setTables(List<PhysicalTable> tables);
		
	public abstract void addTables(List<PhysicalTable> tables);
		
	public abstract void addTable(PhysicalTable table);

	public abstract PhysicalTable getTable(String tableName);
	
	/**
	 * @model
	 */
	public abstract void setPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);
	
	/**
	 * @model
	 */
	public abstract List<PhysicalPrimaryKey> getPrimaryKeys();
	
	public abstract void addPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);

	public abstract void addPrimaryKey(PhysicalPrimaryKey primaryKey);

	
	/**
	 * @model
	 */
	public abstract void setForeignKeys(List<PhysicalForeignKey> primaryKeys);
	
	/**
	 * @model
	 */
	public abstract List<PhysicalForeignKey> getForeignKeys();
	
	public abstract void addForeignKeys(List<PhysicalForeignKey> foreignKeys);

	public abstract void addForeignKey(PhysicalForeignKey foreignKey);
	

	

}