package it.eng.spagobi.meta.model.physical.emf;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.List;

/**
 * @model
 */
public interface PhysicalPrimaryKey {

	/**
	 * @model
	 */
	public abstract String getPkName();

	/**
	 * @model
	 */
	public abstract void setPkName(String pkName);

	public abstract String getTableName();

	public abstract void setTableName(String tableName);

	/**
	 * @model
	 */
	public abstract PhysicalTable getTable();

	/**
	 * @model
	 */
	public abstract void setTable(PhysicalTable table);

	public abstract List<String> getColumnNames();

	public abstract void addColumnNames(List<String> columnNames);

	public abstract void addColumnName(String columnName);

	/**
	 * @model
	 */
	public abstract List<PhysicalColumn> getColumns();
	
	/**
	 * @model
	 */
	public abstract void setColumns(List<PhysicalColumn> columns);

	public abstract void addColumns(List<PhysicalColumn> columns);

	public abstract void addColumn(PhysicalColumn column);

}