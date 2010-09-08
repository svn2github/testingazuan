package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.ecore.EObject;


/**
 * @model
 */
public interface PhysicalColumn extends EObject {

	/**
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * @model
	 * @generated
	 */
	void setName(String name);

	/**
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * @model
	 * @generated
	 */
	void setComment(String comment);

	/**
	 * @model
	 * @generated
	 */
	short getDataType();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getDataType <em>Data Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Data Type</em>' attribute.
	 * @see #getDataType()
	 * @generated
	 */
	void setDataType(short value);

	/**
	 * @model
	 * @generated
	 */
	void setDataType(short dataType);

	/**
	 * @model
	 * @generated
	 */
	String getTypeName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getTypeName <em>Type Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type Name</em>' attribute.
	 * @see #getTypeName()
	 * @generated
	 */
	void setTypeName(String value);

	/**
	 * @model
	 * @generated
	 */
	void setTypeName(String typeName);

	public abstract int getSize();

	/**
	 * @model
	 * @generated
	 */
	void setSize(int size);

	/**
	 * @model
	 * @generated
	 */
	int getOctectLength();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getOctectLength <em>Octect Length</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Octect Length</em>' attribute.
	 * @see #getOctectLength()
	 * @generated
	 */
	void setOctectLength(int value);

	/**
	 * @model
	 * @generated
	 */
	void setOctectLength(int octectLength);

	/**
	 * @model
	 * @generated
	 */
	int getDecimalDigits();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getDecimalDigits <em>Decimal Digits</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Decimal Digits</em>' attribute.
	 * @see #getDecimalDigits()
	 * @generated
	 */
	void setDecimalDigits(int value);

	/**
	 * @model
	 * @generated
	 */
	void setDecimalDigits(int decimalDigits);

	/**
	 * @model
	 * @generated
	 */
	int getRadix();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getRadix <em>Radix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Radix</em>' attribute.
	 * @see #getRadix()
	 * @generated
	 */
	void setRadix(int value);

	/**
	 * @model
	 * @generated
	 */
	void setRadix(int radix);

	/**
	 * @model
	 * @generated
	 */
	String getDefaultValue();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(String value);

	/**
	 * @model
	 */
	public abstract void setDefaultValue(String defaultValue);

	/**
	 * @model
	 * @generated
	 */
	boolean isNullable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#isNullable <em>Nullable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nullable</em>' attribute.
	 * @see #isNullable()
	 * @generated
	 */
	void setNullable(boolean value);

	/**
	 * @model
	 * @generated
	 */
	void setNullable(boolean nullable);

	/**
	 * @model
	 * @generated
	 */
	int getPosition();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getPosition <em>Position</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Position</em>' attribute.
	 * @see #getPosition()
	 * @generated
	 */
	void setPosition(int value);

	/**
	 * @model
	 * @generated
	 */
	void setPosition(int position);

	/**
	 * @model
	 * @generated
	 */
	PhysicalTable getTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getTable <em>Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table</em>' reference.
	 * @see #getTable()
	 * @generated
	 */
	void setTable(PhysicalTable value);

	/**
	 * @model
	 * @generated
	 */
	void setTable(PhysicalTable table);

}