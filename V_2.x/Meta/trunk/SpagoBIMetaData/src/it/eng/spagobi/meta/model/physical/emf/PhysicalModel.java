package it.eng.spagobi.meta.model.physical.emf;

import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.List;

public interface PhysicalModel {

	public abstract String getName();

	public abstract void setName(String name);

	// OWNED OBJECTS
	public abstract String getDatabaseName();

	public abstract void setDatabaseName(String databaseName);

	public abstract String getDatabaseVersion();

	public abstract void setDatabaseVersion(String databaseVersion);

	public abstract void setDatabaseVersion(String databaseName,
			String databaseVersion);

	public abstract String getCatalog();

	public abstract void setCatalog(String catalog);

	public abstract String getSchema();

	public abstract void setSchema(String schema);

	// tables ...	
	public abstract List<PhysicalTable> getTables();

	public abstract void addTables(List<PhysicalTable> tables);

	public abstract void addTable(PhysicalTable table);

	// primary keys 
	public abstract void addPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);

	public abstract void addPrimaryKey(PhysicalPrimaryKey primaryKey);

	// foreign keys 
	public abstract void addForeignKeys(List<PhysicalForeignKey> foreignKeys);

	public abstract void addForeignKey(PhysicalForeignKey foreignKey);

	/* ----------------------------------------------------------------
	 * Lookup methods 
	 * ---------------------------------------------------------------- 
	 */
	public abstract PhysicalTable getTable(String tableName);

}