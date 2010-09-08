/**
 * 
 */
package it.eng.spagobi.meta.model.physical;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalTable {
		
	/* ----------------------------------------------------------------
	 * Table metadata
	 * ---------------------------------------------------------------- 
	 */
	
	/*
	 * table name
	 */
	private String name;
	
	/*
	 * explanatory comment on the table
	 */
	private String comment;
	
	/*
	 * table type. Typical types are “TABLE”, “VIEW”, “SYSTEM TABLE”,”GLOBAL TEMPORARY”, “LOCAL TEMPORARY”, “ALIAS”, “SYNONYM”.
	 */
	private String type;
	
	
	/* ----------------------------------------------------------------
	 * Referenced objects
	 * ---------------------------------------------------------------- 
	 */
	
	// OWNED BY ....
	
	private PhysicalModel model;
	
	/*
	 * table catalog (may be null)
	 */
	private String catalog;
	
	/*
	 * table schema (may be null)
	 */
	private String schema;
	
	// OWNER OF ....
	
	/*
	 * table's columns
	 */
	private List<PhysicalColumn> columns;
	
	/*
	 * table's primary key
	 */
	private List<PhysicalPrimaryKey> primaryKeys;
	
	/*
	 * table's foreign keys
	 */
	private List<PhysicalForeignKey> foreignKeys;
	
	private List<PhysicalForeignKey> incomingKeys;
	
	
	
	
	/* ----------------------------------------------------------------
	 * Cached objects
	 * ---------------------------------------------------------------- 
	 */
	
	private Map<String, PhysicalColumn> columnsLookupMap;
	
	
	
	/* ----------------------------------------------------------------
	 * Costructor methods
	 * ---------------------------------------------------------------- 
	 */
	
	public PhysicalTable() {
		columns = new ArrayList<PhysicalColumn>(); 
		columnsLookupMap = new HashMap<String, PhysicalColumn>(); 
		primaryKeys = new ArrayList<PhysicalPrimaryKey>(); 
		foreignKeys = new ArrayList<PhysicalForeignKey>(); 
		incomingKeys = new ArrayList<PhysicalForeignKey>(); 
	}
	
	
	/* ----------------------------------------------------------------
	 * Accessor methods 
	 * ---------------------------------------------------------------- 
	 */
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public PhysicalModel getModel() {
		return model;
	}

	public void setModel(PhysicalModel model) {
		this.model = model;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<PhysicalColumn> getColumns() {
		return columns;
	}
	public void setColumns(List<PhysicalColumn> columns) {
		this.columns = columns;
	}
	
	public void addColumn(PhysicalColumn column) {
		columns.add(column);
		column.setTable(this);
		
		columnsLookupMap.put(column.getName(), column);
	}
	
	public PhysicalColumn getColumn(String columnName) {
		return this.columnsLookupMap.get(columnName);
	}

	public void addPrimaryKey(PhysicalPrimaryKey primaryKey) {
		primaryKeys.add(primaryKey);		
	}

	public void addForeignKey(PhysicalForeignKey foreignKey) {
		foreignKeys.add(foreignKey);
		
	}

	public void addIncomingKey(PhysicalForeignKey foreignKey) {
		incomingKeys.add(foreignKey);
	}
	
}
