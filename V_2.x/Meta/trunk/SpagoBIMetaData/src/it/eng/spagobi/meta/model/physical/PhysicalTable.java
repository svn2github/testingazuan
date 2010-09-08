package it.eng.spagobi.meta.model.physical;

import java.util.List;

/**
 * @model
 */
public interface PhysicalTable {

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
	public abstract PhysicalModel getModel();

	/**
	 * @model
	 */
	public abstract void setModel(PhysicalModel model);

	
	public abstract String getCatalog();
	public abstract void setCatalog(String catalog);

	
	public abstract String getSchema();
	public abstract void setSchema(String schema);

	/**
	 * @model
	 */
	public abstract String getComment();

	/**
	 * @model
	 */
	public abstract void setComment(String comment);

	/**
	 * @model
	 */
	public abstract String getType();

	/**
	 * @model
	 */
	public abstract void setType(String type);

	/**
	 * @model
	 */
	public abstract List<PhysicalColumn> getColumns();

	/**
	 * @model
	 */
	public abstract void setColumns(List<PhysicalColumn> columns);

	
	public abstract void addColumn(PhysicalColumn column);

	public abstract PhysicalColumn getColumn(String columnName);

	
	/**
	 * @model
	 */
	public abstract void setPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);
	
	/**
	 * @model
	 */
	public abstract List<PhysicalPrimaryKey> getPrimaryKeys();
	
	public abstract void addPrimaryKey(PhysicalPrimaryKey primaryKey);

	/**
	 * @model
	 */
	public abstract void setForeignKeys(List<PhysicalForeignKey> foreignKeys);
	
	/**
	 * @model
	 */
	public abstract List<PhysicalForeignKey> getForeignKeys();
	
	public abstract void addForeignKey(PhysicalForeignKey foreignKey);

	/**
	 * @model
	 */
	public abstract void setIncomingKeys(List<PhysicalForeignKey> foreignKeys);
	
	/**
	 * @model
	 */
	public abstract List<PhysicalForeignKey> getIncomingKeys();
	
	public abstract void addIncomingKey(PhysicalForeignKey foreignKey);

}