/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.emf.impl;

import it.eng.spagobi.meta.model.physical.emf.EmfFactory;
import it.eng.spagobi.meta.model.physical.emf.EmfPackage;
import it.eng.spagobi.meta.model.physical.emf.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.emf.PhysicalModel;
import it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.emf.PhysicalTable;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EmfPackageImpl extends EPackageImpl implements EmfPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass physicalColumnEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass physicalForeignKeyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass physicalModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass physicalPrimaryKeyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass physicalTableEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private EmfPackageImpl() {
		super(eNS_URI, EmfFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this
	 * model, and for any others upon which it depends.  Simple
	 * dependencies are satisfied by calling this method on all
	 * dependent packages before doing anything else.  This method drives
	 * initialization for interdependent packages directly, in parallel
	 * with this package, itself.
	 * <p>Of this package and its interdependencies, all packages which
	 * have not yet been registered by their URI values are first created
	 * and registered.  The packages are then initialized in two steps:
	 * meta-model objects for all of the packages are created before any
	 * are initialized, since one package's meta-model objects may refer to
	 * those of another.
	 * <p>Invocation of this method will not affect any packages that have
	 * already been initialized.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static EmfPackage init() {
		if (isInited) return (EmfPackage)EPackage.Registry.INSTANCE.getEPackage(EmfPackage.eNS_URI);

		// Obtain or create and register package
		EmfPackageImpl theEmfPackage = (EmfPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(eNS_URI) instanceof EmfPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(eNS_URI) : new EmfPackageImpl());

		isInited = true;

		// Create package meta-data objects
		theEmfPackage.createPackageContents();

		// Initialize created meta-data
		theEmfPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theEmfPackage.freeze();

		return theEmfPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPhysicalColumn() {
		return physicalColumnEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_Name() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_Comment() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_DataType() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_TypeName() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_OctectLength() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_DecimalDigits() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_Radix() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_DefaultValue() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_Nullable() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalColumn_Position() {
		return (EAttribute)physicalColumnEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalColumn_Table() {
		return (EReference)physicalColumnEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPhysicalForeignKey() {
		return physicalForeignKeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalForeignKey_FkName() {
		return (EAttribute)physicalForeignKeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalForeignKey_PkName() {
		return (EAttribute)physicalForeignKeyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalForeignKey_FkColumns() {
		return (EReference)physicalForeignKeyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalForeignKey_PkColumns() {
		return (EReference)physicalForeignKeyEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalForeignKey_PkTable() {
		return (EReference)physicalForeignKeyEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalForeignKey_FkTable() {
		return (EReference)physicalForeignKeyEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPhysicalModel() {
		return physicalModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalModel_Name() {
		return (EAttribute)physicalModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalModel_DatabaseName() {
		return (EAttribute)physicalModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalModel_DatabaseVersion() {
		return (EAttribute)physicalModelEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalModel_Catalog() {
		return (EAttribute)physicalModelEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalModel_Schema() {
		return (EAttribute)physicalModelEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalModel_Tables() {
		return (EReference)physicalModelEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalModel_PrimaryKeys() {
		return (EReference)physicalModelEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalModel_ForeignKeys() {
		return (EReference)physicalModelEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPhysicalPrimaryKey() {
		return physicalPrimaryKeyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalPrimaryKey_PkName() {
		return (EAttribute)physicalPrimaryKeyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalPrimaryKey_Table() {
		return (EReference)physicalPrimaryKeyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalPrimaryKey_Columns() {
		return (EReference)physicalPrimaryKeyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getPhysicalTable() {
		return physicalTableEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalTable_Name() {
		return (EAttribute)physicalTableEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalTable_Model() {
		return (EReference)physicalTableEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalTable_Comment() {
		return (EAttribute)physicalTableEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EAttribute getPhysicalTable_Type() {
		return (EAttribute)physicalTableEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalTable_Columns() {
		return (EReference)physicalTableEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalTable_PrimaryKeys() {
		return (EReference)physicalTableEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalTable_ForeignKeys() {
		return (EReference)physicalTableEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EReference getPhysicalTable_IncomingKeys() {
		return (EReference)physicalTableEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmfFactory getEmfFactory() {
		return (EmfFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		physicalColumnEClass = createEClass(PHYSICAL_COLUMN);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__NAME);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__COMMENT);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__DATA_TYPE);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__TYPE_NAME);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__OCTECT_LENGTH);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__DECIMAL_DIGITS);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__RADIX);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__DEFAULT_VALUE);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__NULLABLE);
		createEAttribute(physicalColumnEClass, PHYSICAL_COLUMN__POSITION);
		createEReference(physicalColumnEClass, PHYSICAL_COLUMN__TABLE);

		physicalForeignKeyEClass = createEClass(PHYSICAL_FOREIGN_KEY);
		createEAttribute(physicalForeignKeyEClass, PHYSICAL_FOREIGN_KEY__FK_NAME);
		createEAttribute(physicalForeignKeyEClass, PHYSICAL_FOREIGN_KEY__PK_NAME);
		createEReference(physicalForeignKeyEClass, PHYSICAL_FOREIGN_KEY__FK_COLUMNS);
		createEReference(physicalForeignKeyEClass, PHYSICAL_FOREIGN_KEY__PK_COLUMNS);
		createEReference(physicalForeignKeyEClass, PHYSICAL_FOREIGN_KEY__PK_TABLE);
		createEReference(physicalForeignKeyEClass, PHYSICAL_FOREIGN_KEY__FK_TABLE);

		physicalModelEClass = createEClass(PHYSICAL_MODEL);
		createEAttribute(physicalModelEClass, PHYSICAL_MODEL__NAME);
		createEAttribute(physicalModelEClass, PHYSICAL_MODEL__DATABASE_NAME);
		createEAttribute(physicalModelEClass, PHYSICAL_MODEL__DATABASE_VERSION);
		createEAttribute(physicalModelEClass, PHYSICAL_MODEL__CATALOG);
		createEAttribute(physicalModelEClass, PHYSICAL_MODEL__SCHEMA);
		createEReference(physicalModelEClass, PHYSICAL_MODEL__TABLES);
		createEReference(physicalModelEClass, PHYSICAL_MODEL__PRIMARY_KEYS);
		createEReference(physicalModelEClass, PHYSICAL_MODEL__FOREIGN_KEYS);

		physicalPrimaryKeyEClass = createEClass(PHYSICAL_PRIMARY_KEY);
		createEAttribute(physicalPrimaryKeyEClass, PHYSICAL_PRIMARY_KEY__PK_NAME);
		createEReference(physicalPrimaryKeyEClass, PHYSICAL_PRIMARY_KEY__TABLE);
		createEReference(physicalPrimaryKeyEClass, PHYSICAL_PRIMARY_KEY__COLUMNS);

		physicalTableEClass = createEClass(PHYSICAL_TABLE);
		createEAttribute(physicalTableEClass, PHYSICAL_TABLE__NAME);
		createEReference(physicalTableEClass, PHYSICAL_TABLE__MODEL);
		createEAttribute(physicalTableEClass, PHYSICAL_TABLE__COMMENT);
		createEAttribute(physicalTableEClass, PHYSICAL_TABLE__TYPE);
		createEReference(physicalTableEClass, PHYSICAL_TABLE__COLUMNS);
		createEReference(physicalTableEClass, PHYSICAL_TABLE__PRIMARY_KEYS);
		createEReference(physicalTableEClass, PHYSICAL_TABLE__FOREIGN_KEYS);
		createEReference(physicalTableEClass, PHYSICAL_TABLE__INCOMING_KEYS);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes and features; add operations and parameters
		initEClass(physicalColumnEClass, PhysicalColumn.class, "PhysicalColumn", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPhysicalColumn_Name(), ecorePackage.getEString(), "name", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_Comment(), ecorePackage.getEString(), "comment", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_DataType(), ecorePackage.getEShort(), "dataType", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_TypeName(), ecorePackage.getEString(), "typeName", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_OctectLength(), ecorePackage.getEInt(), "octectLength", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_DecimalDigits(), ecorePackage.getEInt(), "decimalDigits", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_Radix(), ecorePackage.getEInt(), "radix", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_DefaultValue(), ecorePackage.getEString(), "defaultValue", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_Nullable(), ecorePackage.getEBoolean(), "nullable", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalColumn_Position(), ecorePackage.getEInt(), "position", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalColumn_Table(), this.getPhysicalTable(), null, "table", null, 0, 1, PhysicalColumn.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		EOperation op = addEOperation(physicalColumnEClass, null, "setName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setComment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "comment", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setDataType", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEShort(), "dataType", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setTypeName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "typeName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setSize", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "size", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setOctectLength", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "octectLength", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setDecimalDigits", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "decimalDigits", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setRadix", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "radix", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setDefaultValue", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "defaultValue", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setNullable", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEBoolean(), "nullable", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setPosition", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEInt(), "position", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalColumnEClass, null, "setTable", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalTable(), "table", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(physicalForeignKeyEClass, PhysicalForeignKey.class, "PhysicalForeignKey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPhysicalForeignKey_FkName(), ecorePackage.getEString(), "fkName", null, 0, 1, PhysicalForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalForeignKey_PkName(), ecorePackage.getEString(), "pkName", null, 0, 1, PhysicalForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalForeignKey_FkColumns(), this.getPhysicalColumn(), null, "fkColumns", null, 0, -1, PhysicalForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalForeignKey_PkColumns(), this.getPhysicalColumn(), null, "pkColumns", null, 0, -1, PhysicalForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalForeignKey_PkTable(), this.getPhysicalTable(), null, "pkTable", null, 0, 1, PhysicalForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalForeignKey_FkTable(), this.getPhysicalTable(), null, "fkTable", null, 0, 1, PhysicalForeignKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(physicalForeignKeyEClass, null, "setFkName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "fkName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalForeignKeyEClass, null, "setPkName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "pkName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalForeignKeyEClass, null, "setFkColumns", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalColumn(), "fkColumns", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalForeignKeyEClass, null, "setPkColumns", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalColumn(), "pkColumns", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalForeignKeyEClass, null, "setPkTable", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalTable(), "pkTable", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalForeignKeyEClass, null, "setFkTable", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalTable(), "fkTable", 0, 1, IS_UNIQUE, IS_ORDERED);

		initEClass(physicalModelEClass, PhysicalModel.class, "PhysicalModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPhysicalModel_Name(), ecorePackage.getEString(), "name", null, 0, 1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalModel_DatabaseName(), ecorePackage.getEString(), "databaseName", null, 0, 1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalModel_DatabaseVersion(), ecorePackage.getEString(), "databaseVersion", null, 0, 1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalModel_Catalog(), ecorePackage.getEString(), "catalog", null, 0, 1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalModel_Schema(), ecorePackage.getEString(), "schema", null, 0, 1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalModel_Tables(), this.getPhysicalTable(), null, "tables", null, 0, -1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalModel_PrimaryKeys(), this.getPhysicalPrimaryKey(), null, "primaryKeys", null, 0, -1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalModel_ForeignKeys(), this.getPhysicalForeignKey(), null, "foreignKeys", null, 0, -1, PhysicalModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setDatabaseName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "databaseName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setDatabaseVersion", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "databaseVersion", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setCatalog", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "catalog", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setSchema", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "schema", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setTables", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalTable(), "tables", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setPrimaryKeys", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalPrimaryKey(), "primaryKeys", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalModelEClass, null, "setForeignKeys", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalForeignKey(), "primaryKeys", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(physicalPrimaryKeyEClass, PhysicalPrimaryKey.class, "PhysicalPrimaryKey", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPhysicalPrimaryKey_PkName(), ecorePackage.getEString(), "pkName", null, 0, 1, PhysicalPrimaryKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalPrimaryKey_Table(), this.getPhysicalTable(), null, "table", null, 0, 1, PhysicalPrimaryKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalPrimaryKey_Columns(), this.getPhysicalColumn(), null, "columns", null, 0, -1, PhysicalPrimaryKey.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(physicalPrimaryKeyEClass, null, "setPkName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "pkName", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalPrimaryKeyEClass, null, "setTable", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalTable(), "table", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalPrimaryKeyEClass, null, "setColumns", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalColumn(), "columns", 0, -1, IS_UNIQUE, IS_ORDERED);

		initEClass(physicalTableEClass, PhysicalTable.class, "PhysicalTable", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPhysicalTable_Name(), ecorePackage.getEString(), "name", null, 0, 1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalTable_Model(), this.getPhysicalModel(), null, "model", null, 0, 1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalTable_Comment(), ecorePackage.getEString(), "comment", null, 0, 1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPhysicalTable_Type(), ecorePackage.getEString(), "type", null, 0, 1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalTable_Columns(), this.getPhysicalColumn(), null, "columns", null, 0, -1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalTable_PrimaryKeys(), this.getPhysicalPrimaryKey(), null, "primaryKeys", null, 0, -1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalTable_ForeignKeys(), this.getPhysicalForeignKey(), null, "foreignKeys", null, 0, -1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getPhysicalTable_IncomingKeys(), this.getPhysicalForeignKey(), null, "incomingKeys", null, 0, -1, PhysicalTable.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setName", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "name", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setModel", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalModel(), "model", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setComment", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "comment", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setType", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, ecorePackage.getEString(), "type", 0, 1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setColumns", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalColumn(), "columns", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setPrimaryKeys", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalPrimaryKey(), "primaryKeys", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setForeignKeys", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalForeignKey(), "foreignKeys", 0, -1, IS_UNIQUE, IS_ORDERED);

		op = addEOperation(physicalTableEClass, null, "setIncomingKeys", 0, 1, IS_UNIQUE, IS_ORDERED);
		addEParameter(op, this.getPhysicalForeignKey(), "foreignKeys", 0, -1, IS_UNIQUE, IS_ORDERED);

		// Create resource
		createResource(eNS_URI);
	}

} //EmfPackageImpl
