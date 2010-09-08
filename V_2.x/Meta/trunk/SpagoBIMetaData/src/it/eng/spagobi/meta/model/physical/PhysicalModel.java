package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;

/**
 * @model
 */
public interface PhysicalModel extends EObject {

	/**
	 * @model
	 */
	public abstract String getName();
	public abstract void setName(String name);

	/**
	 * @model
	 */
	public abstract String getDatabaseName();
	public abstract void setDatabaseName(String databaseName);

	/**
	 * @model
	 */
	public abstract String getDatabaseVersion();
	public abstract void setDatabaseVersion(String databaseVersion);
	
	public abstract void setDatabaseVersion(String databaseName, String databaseVersion);

	/**
	 * @model
	 */
	public abstract String getCatalog();
	public abstract void setCatalog(String catalog);

	/**
	 * @model
	 */
	public abstract String getSchema();
	public abstract void setSchema(String schema);

	/**
	 * @model
	 */
	public abstract EList<PhysicalTable> getTables();
	public abstract void setTables(List<PhysicalTable> tables);
	
	public abstract void addTables(List<PhysicalTable> tables);
	public abstract void addTable(PhysicalTable table);
	public abstract PhysicalTable getTable(String tableName);
	
	/**
	 * @model
	 */
	public abstract EList<PhysicalPrimaryKey> getPrimaryKeys();
	public abstract void setPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);
	
	public abstract void addPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);
	public abstract void addPrimaryKey(PhysicalPrimaryKey primaryKey);

	
	/**
	 * @model
	 */
	public abstract EList<PhysicalForeignKey> getForeignKeys();
	public abstract void setForeignKeys(List<PhysicalForeignKey> foreignKeys);
	
	public abstract void addForeignKeys(List<PhysicalForeignKey> foreignKeys);
	public abstract void addForeignKey(PhysicalForeignKey foreignKey);
	

	

}