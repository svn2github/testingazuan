/**
 * 
 */
package it.eng.spagobi.meta.model.physical;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class PhysicalColumn {
	
	/* ----------------------------------------------------------------
	 * Column metadata
	 * ---------------------------------------------------------------- 
	 */
	
	// column name
	private String name;
	
	//comment describing column (may be null)
	private String comment;
	
	// SQL type from java.sql.Types
	private short dataType;
	
	//data source dependent type name, for a UDT the type name is fully qualified
	private String typeName;
	
	// for char or date types this is the maximum number of characters (length), for numeric or decimal types this is precision.
	private int size;
	
	// for char types the maximum number of bytes in the column
	private int octectLength;
	
	// for numeric or decimal types this is the number of fractional digits
	private int decimalDigits;
	
	// for numeric or decimal types this is the radix (typically either 10 or 2)
	private int radix;
	
	// column's default value (may be null)
	private String defaultValue;
	
	/*
	 * true if the column allows null value; false otherwise
	 */
	private boolean nullable;
	
	// index of column in table (starting at 1)
	private int position;
	
	/* ----------------------------------------------------------------
	 * Referenced objects
	 * ---------------------------------------------------------------- 
	 */
	
	// OWNED BY ....
	
	private PhysicalTable table;


	
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


	public String getComment() {
		return comment;
	}


	public void setComment(String comment) {
		this.comment = comment;
	}


	public short getDataType() {
		return dataType;
	}


	public void setDataType(short dataType) {
		this.dataType = dataType;
	}


	public String getTypeName() {
		return typeName;
	}


	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}


	public int getSize() {
		return size;
	}


	public void setSize(int size) {
		this.size = size;
	}


	public int getOctectLength() {
		return octectLength;
	}


	public void setOctectLength(int octectLength) {
		this.octectLength = octectLength;
	}


	public int getDecimalDigits() {
		return decimalDigits;
	}


	public void setDecimalDigits(int decimalDigits) {
		this.decimalDigits = decimalDigits;
	}


	public int getRadix() {
		return radix;
	}


	public void setRadix(int radix) {
		this.radix = radix;
	}


	public String getDefaultValue() {
		return defaultValue;
	}


	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}


	public boolean isNullable() {
		return nullable;
	}


	public void setNullable(boolean nullable) {
		this.nullable = nullable;
	}


	public int getPosition() {
		return position;
	}


	public void setPosition(int position) {
		this.position = position;
	}


	public PhysicalTable getTable() {
		return table;
	}


	public void setTable(PhysicalTable table) {
		this.table = table;
	}
	
	
	
	
}
