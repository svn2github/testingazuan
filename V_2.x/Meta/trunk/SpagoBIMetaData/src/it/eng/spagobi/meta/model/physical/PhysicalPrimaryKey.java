/**
 * 
 */
package it.eng.spagobi.meta.model.physical;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalPrimaryKey {
	
	/* ----------------------------------------------------------------
	 * PrimaryKey metadata
	 * ---------------------------------------------------------------- 
	 */
	
	// primary key name (may be null or not unique)
	private String pkName;
	
	// The table on which this primary key has been defined on
	private String tableName;
	
	// PrimaryKey column names
	private List<String> columnNames;
	
	/* ----------------------------------------------------------------
	 * Referenced objects
	 * ---------------------------------------------------------------- 
	 */
	
	// OWNED BY ...
	
	private PhysicalTable table;
	
	// OWNER OF ...
	
	// columns
	private List<PhysicalColumn> columns;
	
	/* ----------------------------------------------------------------
	 * Costructor methods
	 * ---------------------------------------------------------------- 
	 */
	
	public PhysicalPrimaryKey() {
		columnNames = new ArrayList<String>();
		columns = new ArrayList<PhysicalColumn>();
	}

	
	/* ----------------------------------------------------------------
	 * Accessor methods 
	 * ---------------------------------------------------------------- 
	 */
	
	// META
	public String getPkName() {
		return pkName;
	}
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	// OWNER OBJECTS
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public PhysicalTable getTable() {
		return table;
	}
	public void setTable(PhysicalTable table) {
		this.table = table;
	}
	
	// OWNED OBJECTS
	public List<String> getColumnNames() {
		return columnNames;
	}
	public void addColumnNames(List<String> columnNames) {
		for(int i = 0; i < columnNames.size(); i++) {
			addColumnName(columnNames.get(i));
		}
	}
	public void addColumnName(String columnName) {
		this.columnNames.add(columnName);
	}

	public List<PhysicalColumn> getColumns() {
		return columns;
	}
	public void addColumns(List<PhysicalColumn> columns) {
		for(int i = 0; i < columns.size(); i++) {
			addColumn(columns.get(i));
		}
	}
	public void addColumn(PhysicalColumn column) {
		this.columns.add(column);
	}	
}
