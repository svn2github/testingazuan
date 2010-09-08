package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;

/**
 * @model
 */
public interface PhysicalPrimaryKey extends EObject {

	/**
	 * @model
	 */
	public abstract String getPkName();
	public abstract void setPkName(String pkName);

	public abstract String getTableName();
	public abstract void setTableName(String tableName);

	/**
	 * @model
	 */
	public abstract PhysicalTable getTable();
	public abstract void setTable(PhysicalTable table);

	public abstract List<String> getColumnNames();
	public abstract void addColumnNames(List<String> columnNames);
	public abstract void addColumnName(String columnName);

	/**
	 * @model
	 */
	public abstract EList<PhysicalColumn> getColumns();
	public abstract void setColumns(List<PhysicalColumn> columns);

	public abstract void addColumns(List<PhysicalColumn> columns);
	public abstract void addColumn(PhysicalColumn column);

}