package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;

/**
 * @model
 */
public interface PhysicalTable extends EObject {

	/**
	 * @model
	 */
	public abstract String getName();
	public abstract void setName(String name);

	/**
	 * @model
	 */
	public abstract PhysicalModel getModel();
	public abstract void setModel(PhysicalModel model);

	
	public abstract String getCatalog();
	public abstract void setCatalog(String catalog);

	public abstract String getSchema();
	public abstract void setSchema(String schema);

	/**
	 * @model
	 */
	public abstract String getComment();
	public abstract void setComment(String comment);

	/**
	 * @model
	 */
	public abstract String getType();
	public abstract void setType(String type);

	/**
	 * @model
	 */
	public abstract EList<PhysicalColumn> getColumns();
	public abstract void setColumns(List<PhysicalColumn> columns);

	public abstract void addColumn(PhysicalColumn column);
	public abstract PhysicalColumn getColumn(String columnName);

	
	/**
	 * @model
	 */
	public abstract EList<PhysicalPrimaryKey> getPrimaryKeys();
	public abstract void setPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);
	
	public abstract void addPrimaryKey(PhysicalPrimaryKey primaryKey);

	/**
	 * @model
	 */
	public abstract EList<PhysicalForeignKey> getForeignKeys();
	public abstract void setForeignKeys(List<PhysicalForeignKey> foreignKeys);
	
	public abstract void addForeignKey(PhysicalForeignKey foreignKey);

	/**
	 * @model
	 */
	public abstract EList<PhysicalForeignKey> getIncomingKeys();
	public abstract void setIncomingKeys(List<PhysicalForeignKey> foreignKeys);
	
	public abstract void addIncomingKey(PhysicalForeignKey foreignKey);

}