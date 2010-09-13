/**
 * 
 */
package it.eng.spagobi.meta.model.physical.pojo;


import java.util.ArrayList;
import java.util.List;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalForeignKey {
	
	/* ----------------------------------------------------------------
	 * ForeignKey metadata
	 * ---------------------------------------------------------------- 
	 */
	
	// foreign key name (may be null)
	private String fkName;
	
	// primary key name (may be null)
	private String pkName;
	
	// foreign key table name (...from table)
	private String fkTableName;
	
	// foreign key column names (...from columns)
	private List<String> fkColumnNames;
	
	// primary key table name (...to table)
	private String pkTableName;
	
	// primary key column names (...to columns)
	private List<String> pkColumnNames;
	
	
	/* ----------------------------------------------------------------
	 * Referenced objects
	 * ---------------------------------------------------------------- 
	 */
	
	// OWNED BY ...
	
	private PhysicalTable pkTable;
	
	private PhysicalTable fkTable;
	
	// OWNER OF ...
	
	// foreign key columns (...from columns)
	private List<PhysicalColumn> fkColumns;
	
	// primary key columns  (...to columns)
	private List<PhysicalColumn> pkColumns;
	
	
	/* ----------------------------------------------------------------
	 * Costructor methods
	 * ---------------------------------------------------------------- 
	 */
	
	public PhysicalForeignKey() {
		fkColumnNames = new ArrayList();
		pkColumnNames = new ArrayList();
		fkColumns = new ArrayList();
		pkColumns = new ArrayList();
	}



	/* ----------------------------------------------------------------
	 * Accessor methods 
	 * ---------------------------------------------------------------- 
	 */
	
	// META
	public String getFkName() {
		return fkName;
	}

	public void setFkName(String fkName) {
		this.fkName = fkName;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public String getFkTableName() {
		return fkTableName;
	}

	public void setFkTableName(String fkTableName) {
		this.fkTableName = fkTableName;
	}

	public List<String> getFkColumnNames() {
		return fkColumnNames;
	}

	public void setFkColumnNames(List<String> fkColumnNames) {
		this.fkColumnNames = fkColumnNames;
	}

	public String getPkTableName() {
		return pkTableName;
	}

	public void setPkTableName(String pkTableName) {
		this.pkTableName = pkTableName;
	}

	public List<String> getPkColumnNames() {
		return pkColumnNames;
	}

	public void addFkColumnName(String columnName) {
		pkColumnNames.add( columnName );	
	}
	
	public void addPkColumnName(String columnName) {
		pkColumnNames.add( columnName );	
	}
	
	
	public void setPkColumnNames(List<String> pkColumnNames) {
		this.pkColumnNames = pkColumnNames;
	}

	
	// OWNER 
	public List<PhysicalColumn> getFkColumns() {
		return fkColumns;
	}
	
	public void setFkColumns(List<PhysicalColumn> fkColumns) {
		this.fkColumns = fkColumns;
	}

	public List<PhysicalColumn> getPkColumns() {
		return pkColumns;
	}

	public void setPkColumns(List<PhysicalColumn> pkColumns) {
		this.pkColumns = pkColumns;
	}
	
	public void addFkColumn(PhysicalColumn fkColumn) {
		this.fkColumns.add(fkColumn);
	}
	
	public void addPkColumn(PhysicalColumn pkColumn) {
		this.pkColumns.add(pkColumn);
	}

	// OWNED
	
	public PhysicalTable getPkTable() {
		return pkTable;
	}
	public void setPkTable(PhysicalTable pkTable) {
		this.pkTable = pkTable;
	}

	public PhysicalTable getFkTable() {
		return fkTable;
	}
	public void setFkTable(PhysicalTable fkTable) {
		this.fkTable = fkTable;
	}
		
}
