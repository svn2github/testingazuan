package it.eng.spagobi.meta.model.physical;


/**
 * @model
 */
public interface PhysicalColumn {

	/**
	 * @model
	 */
	public abstract String getName();
	public abstract void setName(String name);

	/**
	 * @model
	 */
	public abstract String getComment();
	public abstract void setComment(String comment);

	/**
	 * @model
	 */
	public abstract short getDataType();
	public abstract void setDataType(short dataType);

	/**
	 * @model
	 */
	public abstract String getTypeName();
	public abstract void setTypeName(String typeName);

	/**
	 * @model
	 */
	public abstract int getSize();
	public abstract void setSize(int size);

	/**
	 * @model
	 */
	public abstract int getOctectLength();
	public abstract void setOctectLength(int octectLength);

	/**
	 * @model
	 */
	public abstract int getDecimalDigits();
	public abstract void setDecimalDigits(int decimalDigits);

	/**
	 * @model
	 */
	public abstract int getRadix();
	public abstract void setRadix(int radix);

	/**
	 * @model
	 */
	public abstract String getDefaultValue();
	public abstract void setDefaultValue(String defaultValue);

	/**
	 * @model
	 */
	public abstract boolean isNullable();
	public abstract void setNullable(boolean nullable);

	/**
	 * @model
	 */
	public abstract int getPosition();
	public abstract void setPosition(int position);

	/**
	 * @model
	 */
	public abstract PhysicalTable getTable();
	public abstract void setTable(PhysicalTable table);

}