/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see it.eng.spagobi.meta.model.physical.PhysicalModelFactory
 * @model kind="package"
 * @generated
 */
public interface PhysicalModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "physical";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///it/eng/spagobi/meta/model/physical.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "it.eng.spagobi.meta.model.physical";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PhysicalModelPackage eINSTANCE = it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalColumnImpl <em>Physical Column</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalColumnImpl
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalColumn()
	 * @generated
	 */
	int PHYSICAL_COLUMN = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__NAME = 0;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__COMMENT = 1;

	/**
	 * The feature id for the '<em><b>Data Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__DATA_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__TYPE_NAME = 3;

	/**
	 * The feature id for the '<em><b>Size</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__SIZE = 4;

	/**
	 * The feature id for the '<em><b>Octect Length</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__OCTECT_LENGTH = 5;

	/**
	 * The feature id for the '<em><b>Decimal Digits</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__DECIMAL_DIGITS = 6;

	/**
	 * The feature id for the '<em><b>Radix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__RADIX = 7;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__DEFAULT_VALUE = 8;

	/**
	 * The feature id for the '<em><b>Nullable</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__NULLABLE = 9;

	/**
	 * The feature id for the '<em><b>Position</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__POSITION = 10;

	/**
	 * The feature id for the '<em><b>Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN__TABLE = 11;

	/**
	 * The number of structural features of the '<em>Physical Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_COLUMN_FEATURE_COUNT = 12;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl <em>Physical Foreign Key</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalForeignKey()
	 * @generated
	 */
	int PHYSICAL_FOREIGN_KEY = 1;

	/**
	 * The feature id for the '<em><b>Source Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY__SOURCE_NAME = 0;

	/**
	 * The feature id for the '<em><b>Source Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY__SOURCE_TABLE = 1;

	/**
	 * The feature id for the '<em><b>Source Columns</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY__SOURCE_COLUMNS = 2;

	/**
	 * The feature id for the '<em><b>Destination Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY__DESTINATION_NAME = 3;

	/**
	 * The feature id for the '<em><b>Destination Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY__DESTINATION_TABLE = 4;

	/**
	 * The feature id for the '<em><b>Destination Columns</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY__DESTINATION_COLUMNS = 5;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY__MODEL = 6;

	/**
	 * The number of structural features of the '<em>Physical Foreign Key</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_FOREIGN_KEY_FEATURE_COUNT = 7;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalModelImpl <em>Physical Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelImpl
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalModel()
	 * @generated
	 */
	int PHYSICAL_MODEL = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Database Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__DATABASE_NAME = 1;

	/**
	 * The feature id for the '<em><b>Database Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__DATABASE_VERSION = 2;

	/**
	 * The feature id for the '<em><b>Catalog</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__CATALOG = 3;

	/**
	 * The feature id for the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__SCHEMA = 4;

	/**
	 * The feature id for the '<em><b>Tables</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__TABLES = 5;

	/**
	 * The feature id for the '<em><b>Primary Keys</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__PRIMARY_KEYS = 6;

	/**
	 * The feature id for the '<em><b>Foreign Keys</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL__FOREIGN_KEYS = 7;

	/**
	 * The number of structural features of the '<em>Physical Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_MODEL_FEATURE_COUNT = 8;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl <em>Physical Primary Key</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalPrimaryKey()
	 * @generated
	 */
	int PHYSICAL_PRIMARY_KEY = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_PRIMARY_KEY__NAME = 0;

	/**
	 * The feature id for the '<em><b>Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_PRIMARY_KEY__MODEL = 1;

	/**
	 * The feature id for the '<em><b>Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_PRIMARY_KEY__TABLE = 2;

	/**
	 * The feature id for the '<em><b>Columns</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_PRIMARY_KEY__COLUMNS = 3;

	/**
	 * The number of structural features of the '<em>Physical Primary Key</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_PRIMARY_KEY_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl <em>Physical Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl
	 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalTable()
	 * @generated
	 */
	int PHYSICAL_TABLE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__COMMENT = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__TYPE = 2;

	/**
	 * The feature id for the '<em><b>Model</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__MODEL = 3;

	/**
	 * The feature id for the '<em><b>Columns</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__COLUMNS = 4;

	/**
	 * The feature id for the '<em><b>Primary Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__PRIMARY_KEY = 5;

	/**
	 * The feature id for the '<em><b>Foreign Keys</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__FOREIGN_KEYS = 6;

	/**
	 * The feature id for the '<em><b>Reverse Foreign Keys</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS = 7;

	/**
	 * The number of structural features of the '<em>Physical Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PHYSICAL_TABLE_FEATURE_COUNT = 8;


	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn <em>Physical Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Physical Column</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn
	 * @generated
	 */
	EClass getPhysicalColumn();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getName()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getComment()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_Comment();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getDataType <em>Data Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Data Type</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getDataType()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_DataType();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getTypeName <em>Type Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getTypeName()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_TypeName();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getSize <em>Size</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Size</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getSize()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_Size();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getOctectLength <em>Octect Length</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Octect Length</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getOctectLength()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_OctectLength();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getDecimalDigits <em>Decimal Digits</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Decimal Digits</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getDecimalDigits()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_DecimalDigits();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getRadix <em>Radix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Radix</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getRadix()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_Radix();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Default Value</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getDefaultValue()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_DefaultValue();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#isNullable <em>Nullable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Nullable</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#isNullable()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_Nullable();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getPosition <em>Position</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Position</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getPosition()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EAttribute getPhysicalColumn_Position();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Table</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getTable()
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	EReference getPhysicalColumn_Table();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey <em>Physical Foreign Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Physical Foreign Key</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey
	 * @generated
	 */
	EClass getPhysicalForeignKey();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceName <em>Source Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceName()
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	EAttribute getPhysicalForeignKey_SourceName();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceTable <em>Source Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source Table</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceTable()
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	EReference getPhysicalForeignKey_SourceTable();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceColumns <em>Source Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Source Columns</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceColumns()
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	EReference getPhysicalForeignKey_SourceColumns();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationName <em>Destination Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Destination Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationName()
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	EAttribute getPhysicalForeignKey_DestinationName();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationTable <em>Destination Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Destination Table</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationTable()
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	EReference getPhysicalForeignKey_DestinationTable();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationColumns <em>Destination Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Destination Columns</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationColumns()
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	EReference getPhysicalForeignKey_DestinationColumns();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Model</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getModel()
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	EReference getPhysicalForeignKey_Model();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.physical.PhysicalModel <em>Physical Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Physical Model</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel
	 * @generated
	 */
	EClass getPhysicalModel();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getName()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EAttribute getPhysicalModel_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getDatabaseName <em>Database Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Database Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getDatabaseName()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EAttribute getPhysicalModel_DatabaseName();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getDatabaseVersion <em>Database Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Database Version</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getDatabaseVersion()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EAttribute getPhysicalModel_DatabaseVersion();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getCatalog <em>Catalog</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Catalog</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getCatalog()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EAttribute getPhysicalModel_Catalog();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getSchema <em>Schema</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Schema</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getSchema()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EAttribute getPhysicalModel_Schema();

	/**
	 * Returns the meta object for the containment reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getTables <em>Tables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Tables</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getTables()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EReference getPhysicalModel_Tables();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getPrimaryKeys <em>Primary Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Primary Keys</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getPrimaryKeys()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EReference getPhysicalModel_PrimaryKeys();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getForeignKeys <em>Foreign Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Foreign Keys</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getForeignKeys()
	 * @see #getPhysicalModel()
	 * @generated
	 */
	EReference getPhysicalModel_ForeignKeys();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey <em>Physical Primary Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Physical Primary Key</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey
	 * @generated
	 */
	EClass getPhysicalPrimaryKey();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getName()
	 * @see #getPhysicalPrimaryKey()
	 * @generated
	 */
	EAttribute getPhysicalPrimaryKey_Name();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Model</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getModel()
	 * @see #getPhysicalPrimaryKey()
	 * @generated
	 */
	EReference getPhysicalPrimaryKey_Model();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Table</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getTable()
	 * @see #getPhysicalPrimaryKey()
	 * @generated
	 */
	EReference getPhysicalPrimaryKey_Table();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getColumns <em>Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Columns</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getColumns()
	 * @see #getPhysicalPrimaryKey()
	 * @generated
	 */
	EReference getPhysicalPrimaryKey_Columns();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.physical.PhysicalTable <em>Physical Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Physical Table</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable
	 * @generated
	 */
	EClass getPhysicalTable();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getName()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EAttribute getPhysicalTable_Name();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getComment <em>Comment</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Comment</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getComment()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EAttribute getPhysicalTable_Comment();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Type</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getType()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EAttribute getPhysicalTable_Type();

	/**
	 * Returns the meta object for the container reference '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Model</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getModel()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EReference getPhysicalTable_Model();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getColumns <em>Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Columns</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getColumns()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EReference getPhysicalTable_Columns();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getPrimaryKey <em>Primary Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Primary Key</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getPrimaryKey()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EReference getPhysicalTable_PrimaryKey();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getForeignKeys <em>Foreign Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Foreign Keys</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getForeignKeys()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EReference getPhysicalTable_ForeignKeys();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getReverseForeignKeys <em>Reverse Foreign Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Reverse Foreign Keys</em>'.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable#getReverseForeignKeys()
	 * @see #getPhysicalTable()
	 * @generated
	 */
	EReference getPhysicalTable_ReverseForeignKeys();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	PhysicalModelFactory getPhysicalModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalColumnImpl <em>Physical Column</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalColumnImpl
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalColumn()
		 * @generated
		 */
		EClass PHYSICAL_COLUMN = eINSTANCE.getPhysicalColumn();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__NAME = eINSTANCE.getPhysicalColumn_Name();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__COMMENT = eINSTANCE.getPhysicalColumn_Comment();

		/**
		 * The meta object literal for the '<em><b>Data Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__DATA_TYPE = eINSTANCE.getPhysicalColumn_DataType();

		/**
		 * The meta object literal for the '<em><b>Type Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__TYPE_NAME = eINSTANCE.getPhysicalColumn_TypeName();

		/**
		 * The meta object literal for the '<em><b>Size</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__SIZE = eINSTANCE.getPhysicalColumn_Size();

		/**
		 * The meta object literal for the '<em><b>Octect Length</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__OCTECT_LENGTH = eINSTANCE.getPhysicalColumn_OctectLength();

		/**
		 * The meta object literal for the '<em><b>Decimal Digits</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__DECIMAL_DIGITS = eINSTANCE.getPhysicalColumn_DecimalDigits();

		/**
		 * The meta object literal for the '<em><b>Radix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__RADIX = eINSTANCE.getPhysicalColumn_Radix();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__DEFAULT_VALUE = eINSTANCE.getPhysicalColumn_DefaultValue();

		/**
		 * The meta object literal for the '<em><b>Nullable</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__NULLABLE = eINSTANCE.getPhysicalColumn_Nullable();

		/**
		 * The meta object literal for the '<em><b>Position</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_COLUMN__POSITION = eINSTANCE.getPhysicalColumn_Position();

		/**
		 * The meta object literal for the '<em><b>Table</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_COLUMN__TABLE = eINSTANCE.getPhysicalColumn_Table();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl <em>Physical Foreign Key</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalForeignKey()
		 * @generated
		 */
		EClass PHYSICAL_FOREIGN_KEY = eINSTANCE.getPhysicalForeignKey();

		/**
		 * The meta object literal for the '<em><b>Source Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_FOREIGN_KEY__SOURCE_NAME = eINSTANCE.getPhysicalForeignKey_SourceName();

		/**
		 * The meta object literal for the '<em><b>Source Table</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_FOREIGN_KEY__SOURCE_TABLE = eINSTANCE.getPhysicalForeignKey_SourceTable();

		/**
		 * The meta object literal for the '<em><b>Source Columns</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_FOREIGN_KEY__SOURCE_COLUMNS = eINSTANCE.getPhysicalForeignKey_SourceColumns();

		/**
		 * The meta object literal for the '<em><b>Destination Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_FOREIGN_KEY__DESTINATION_NAME = eINSTANCE.getPhysicalForeignKey_DestinationName();

		/**
		 * The meta object literal for the '<em><b>Destination Table</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_FOREIGN_KEY__DESTINATION_TABLE = eINSTANCE.getPhysicalForeignKey_DestinationTable();

		/**
		 * The meta object literal for the '<em><b>Destination Columns</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_FOREIGN_KEY__DESTINATION_COLUMNS = eINSTANCE.getPhysicalForeignKey_DestinationColumns();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_FOREIGN_KEY__MODEL = eINSTANCE.getPhysicalForeignKey_Model();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalModelImpl <em>Physical Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelImpl
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalModel()
		 * @generated
		 */
		EClass PHYSICAL_MODEL = eINSTANCE.getPhysicalModel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_MODEL__NAME = eINSTANCE.getPhysicalModel_Name();

		/**
		 * The meta object literal for the '<em><b>Database Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_MODEL__DATABASE_NAME = eINSTANCE.getPhysicalModel_DatabaseName();

		/**
		 * The meta object literal for the '<em><b>Database Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_MODEL__DATABASE_VERSION = eINSTANCE.getPhysicalModel_DatabaseVersion();

		/**
		 * The meta object literal for the '<em><b>Catalog</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_MODEL__CATALOG = eINSTANCE.getPhysicalModel_Catalog();

		/**
		 * The meta object literal for the '<em><b>Schema</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_MODEL__SCHEMA = eINSTANCE.getPhysicalModel_Schema();

		/**
		 * The meta object literal for the '<em><b>Tables</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_MODEL__TABLES = eINSTANCE.getPhysicalModel_Tables();

		/**
		 * The meta object literal for the '<em><b>Primary Keys</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_MODEL__PRIMARY_KEYS = eINSTANCE.getPhysicalModel_PrimaryKeys();

		/**
		 * The meta object literal for the '<em><b>Foreign Keys</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_MODEL__FOREIGN_KEYS = eINSTANCE.getPhysicalModel_ForeignKeys();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl <em>Physical Primary Key</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalPrimaryKey()
		 * @generated
		 */
		EClass PHYSICAL_PRIMARY_KEY = eINSTANCE.getPhysicalPrimaryKey();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_PRIMARY_KEY__NAME = eINSTANCE.getPhysicalPrimaryKey_Name();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_PRIMARY_KEY__MODEL = eINSTANCE.getPhysicalPrimaryKey_Model();

		/**
		 * The meta object literal for the '<em><b>Table</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_PRIMARY_KEY__TABLE = eINSTANCE.getPhysicalPrimaryKey_Table();

		/**
		 * The meta object literal for the '<em><b>Columns</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_PRIMARY_KEY__COLUMNS = eINSTANCE.getPhysicalPrimaryKey_Columns();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl <em>Physical Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl
		 * @see it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl#getPhysicalTable()
		 * @generated
		 */
		EClass PHYSICAL_TABLE = eINSTANCE.getPhysicalTable();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_TABLE__NAME = eINSTANCE.getPhysicalTable_Name();

		/**
		 * The meta object literal for the '<em><b>Comment</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_TABLE__COMMENT = eINSTANCE.getPhysicalTable_Comment();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PHYSICAL_TABLE__TYPE = eINSTANCE.getPhysicalTable_Type();

		/**
		 * The meta object literal for the '<em><b>Model</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_TABLE__MODEL = eINSTANCE.getPhysicalTable_Model();

		/**
		 * The meta object literal for the '<em><b>Columns</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_TABLE__COLUMNS = eINSTANCE.getPhysicalTable_Columns();

		/**
		 * The meta object literal for the '<em><b>Primary Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_TABLE__PRIMARY_KEY = eINSTANCE.getPhysicalTable_PrimaryKey();

		/**
		 * The meta object literal for the '<em><b>Foreign Keys</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_TABLE__FOREIGN_KEYS = eINSTANCE.getPhysicalTable_ForeignKeys();

		/**
		 * The meta object literal for the '<em><b>Reverse Foreign Keys</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS = eINSTANCE.getPhysicalTable_ReverseForeignKeys();

	}

} //PhysicalModelPackage
