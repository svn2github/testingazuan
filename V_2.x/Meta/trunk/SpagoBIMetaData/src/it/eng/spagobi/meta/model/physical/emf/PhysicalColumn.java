package it.eng.spagobi.meta.model.physical.emf;

import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * @model
 */
public interface PhysicalColumn {

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
	public abstract String getComment();

	/**
	 * @model
	 */
	public abstract void setComment(String comment);

	/**
	 * @model
	 */
	public abstract short getDataType();

	/**
	 * @model
	 */
	public abstract void setDataType(short dataType);

	/**
	 * @model
	 */
	public abstract String getTypeName();

	/**
	 * @model
	 */
	public abstract void setTypeName(String typeName);

	public abstract int getSize();

	/**
	 * @model
	 */
	public abstract void setSize(int size);

	/**
	 * @model
	 */
	public abstract int getOctectLength();

	/**
	 * @model
	 */
	public abstract void setOctectLength(int octectLength);

	/**
	 * @model
	 */
	public abstract int getDecimalDigits();

	/**
	 * @model
	 */
	public abstract void setDecimalDigits(int decimalDigits);

	/**
	 * @model
	 */
	public abstract int getRadix();

	/**
	 * @model
	 */
	public abstract void setRadix(int radix);

	/**
	 * @model
	 */
	public abstract String getDefaultValue();

	/**
	 * @model
	 */
	public abstract void setDefaultValue(String defaultValue);

	/**
	 * @model
	 */
	public abstract boolean isNullable();

	/**
	 * @model
	 */
	public abstract void setNullable(boolean nullable);

	/**
	 * @model
	 */
	public abstract int getPosition();

	/**
	 * @model
	 */
	public abstract void setPosition(int position);

	/**
	 * @model
	 */
	public abstract PhysicalTable getTable();

	/**
	 * @model
	 */
	public abstract void setTable(PhysicalTable table);

}