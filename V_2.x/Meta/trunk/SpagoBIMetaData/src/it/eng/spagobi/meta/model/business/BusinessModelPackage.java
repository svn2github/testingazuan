/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

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
 * @see it.eng.spagobi.meta.model.business.BusinessModelFactory
 * @model kind="package"
 * @generated
 */
public interface BusinessModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "business";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///it/eng/spagobi/meta/model/businessl.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "it.eng.spagobi.meta.model.business";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BusinessModelPackage eINSTANCE = it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessModelImpl <em>Business Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelImpl
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessModel()
	 * @generated
	 */
	int BUSINESS_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_MODEL__NAME = 0;

	/**
	 * The feature id for the '<em><b>Physical Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_MODEL__PHYSICAL_MODEL = 1;

	/**
	 * The feature id for the '<em><b>Tables</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_MODEL__TABLES = 2;

	/**
	 * The feature id for the '<em><b>Relationships</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_MODEL__RELATIONSHIPS = 3;

	/**
	 * The number of structural features of the '<em>Business Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_MODEL_FEATURE_COUNT = 4;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessTableImpl <em>Business Table</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessTableImpl
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessTable()
	 * @generated
	 */
	int BUSINESS_TABLE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_TABLE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Physical Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_TABLE__PHYSICAL_TABLE = 1;

	/**
	 * The number of structural features of the '<em>Business Table</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_TABLE_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessColumnImpl <em>Business Column</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessColumnImpl
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessColumn()
	 * @generated
	 */
	int BUSINESS_COLUMN = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_COLUMN__NAME = 0;

	/**
	 * The feature id for the '<em><b>Physical Column</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_COLUMN__PHYSICAL_COLUMN = 1;

	/**
	 * The number of structural features of the '<em>Business Column</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_COLUMN_FEATURE_COUNT = 2;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl <em>Business Relationship</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessRelationship()
	 * @generated
	 */
	int BUSINESS_RELATIONSHIP = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_RELATIONSHIP__NAME = 0;

	/**
	 * The feature id for the '<em><b>Source Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_RELATIONSHIP__SOURCE_TABLE = 1;

	/**
	 * The feature id for the '<em><b>Destination Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_RELATIONSHIP__DESTINATION_TABLE = 2;

	/**
	 * The number of structural features of the '<em>Business Relationship</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_RELATIONSHIP_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessViewImpl <em>Business View</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessViewImpl
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessView()
	 * @generated
	 */
	int BUSINESS_VIEW = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_VIEW__NAME = 0;

	/**
	 * The feature id for the '<em><b>Columns</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_VIEW__COLUMNS = 1;

	/**
	 * The feature id for the '<em><b>Join Relationships</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_VIEW__JOIN_RELATIONSHIPS = 2;

	/**
	 * The number of structural features of the '<em>Business View</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_VIEW_FEATURE_COUNT = 3;

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessDomainImpl <em>Business Domain</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessDomainImpl
	 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessDomain()
	 * @generated
	 */
	int BUSINESS_DOMAIN = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_DOMAIN__NAME = 0;

	/**
	 * The number of structural features of the '<em>Business Domain</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BUSINESS_DOMAIN_FEATURE_COUNT = 1;


	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.business.BusinessModel <em>Business Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Business Model</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessModel
	 * @generated
	 */
	EClass getBusinessModel();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.business.BusinessModel#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessModel#getName()
	 * @see #getBusinessModel()
	 * @generated
	 */
	EAttribute getBusinessModel_Name();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.business.BusinessModel#getPhysicalModel <em>Physical Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Physical Model</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessModel#getPhysicalModel()
	 * @see #getBusinessModel()
	 * @generated
	 */
	EReference getBusinessModel_PhysicalModel();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.business.BusinessModel#getTables <em>Tables</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Tables</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessModel#getTables()
	 * @see #getBusinessModel()
	 * @generated
	 */
	EReference getBusinessModel_Tables();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.business.BusinessModel#getRelationships <em>Relationships</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Relationships</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessModel#getRelationships()
	 * @see #getBusinessModel()
	 * @generated
	 */
	EReference getBusinessModel_Relationships();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.business.BusinessTable <em>Business Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Business Table</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessTable
	 * @generated
	 */
	EClass getBusinessTable();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.business.BusinessTable#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessTable#getName()
	 * @see #getBusinessTable()
	 * @generated
	 */
	EAttribute getBusinessTable_Name();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.business.BusinessTable#getPhysicalTable <em>Physical Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Physical Table</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessTable#getPhysicalTable()
	 * @see #getBusinessTable()
	 * @generated
	 */
	EReference getBusinessTable_PhysicalTable();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.business.BusinessColumn <em>Business Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Business Column</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessColumn
	 * @generated
	 */
	EClass getBusinessColumn();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.business.BusinessColumn#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessColumn#getName()
	 * @see #getBusinessColumn()
	 * @generated
	 */
	EAttribute getBusinessColumn_Name();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.business.BusinessColumn#getPhysicalColumn <em>Physical Column</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Physical Column</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessColumn#getPhysicalColumn()
	 * @see #getBusinessColumn()
	 * @generated
	 */
	EReference getBusinessColumn_PhysicalColumn();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.business.BusinessRelationship <em>Business Relationship</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Business Relationship</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessRelationship
	 * @generated
	 */
	EClass getBusinessRelationship();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessRelationship#getName()
	 * @see #getBusinessRelationship()
	 * @generated
	 */
	EAttribute getBusinessRelationship_Name();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getSourceTable <em>Source Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source Table</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessRelationship#getSourceTable()
	 * @see #getBusinessRelationship()
	 * @generated
	 */
	EReference getBusinessRelationship_SourceTable();

	/**
	 * Returns the meta object for the reference '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getDestinationTable <em>Destination Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Destination Table</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessRelationship#getDestinationTable()
	 * @see #getBusinessRelationship()
	 * @generated
	 */
	EReference getBusinessRelationship_DestinationTable();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.business.BusinessView <em>Business View</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Business View</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessView
	 * @generated
	 */
	EClass getBusinessView();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.business.BusinessView#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessView#getName()
	 * @see #getBusinessView()
	 * @generated
	 */
	EAttribute getBusinessView_Name();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.business.BusinessView#getColumns <em>Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Columns</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessView#getColumns()
	 * @see #getBusinessView()
	 * @generated
	 */
	EReference getBusinessView_Columns();

	/**
	 * Returns the meta object for the reference list '{@link it.eng.spagobi.meta.model.business.BusinessView#getJoinRelationships <em>Join Relationships</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Join Relationships</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessView#getJoinRelationships()
	 * @see #getBusinessView()
	 * @generated
	 */
	EReference getBusinessView_JoinRelationships();

	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.business.BusinessDomain <em>Business Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Business Domain</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessDomain
	 * @generated
	 */
	EClass getBusinessDomain();

	/**
	 * Returns the meta object for the attribute '{@link it.eng.spagobi.meta.model.business.BusinessDomain#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see it.eng.spagobi.meta.model.business.BusinessDomain#getName()
	 * @see #getBusinessDomain()
	 * @generated
	 */
	EAttribute getBusinessDomain_Name();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	BusinessModelFactory getBusinessModelFactory();

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
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessModelImpl <em>Business Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelImpl
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessModel()
		 * @generated
		 */
		EClass BUSINESS_MODEL = eINSTANCE.getBusinessModel();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUSINESS_MODEL__NAME = eINSTANCE.getBusinessModel_Name();

		/**
		 * The meta object literal for the '<em><b>Physical Model</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_MODEL__PHYSICAL_MODEL = eINSTANCE.getBusinessModel_PhysicalModel();

		/**
		 * The meta object literal for the '<em><b>Tables</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_MODEL__TABLES = eINSTANCE.getBusinessModel_Tables();

		/**
		 * The meta object literal for the '<em><b>Relationships</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_MODEL__RELATIONSHIPS = eINSTANCE.getBusinessModel_Relationships();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessTableImpl <em>Business Table</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessTableImpl
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessTable()
		 * @generated
		 */
		EClass BUSINESS_TABLE = eINSTANCE.getBusinessTable();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUSINESS_TABLE__NAME = eINSTANCE.getBusinessTable_Name();

		/**
		 * The meta object literal for the '<em><b>Physical Table</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_TABLE__PHYSICAL_TABLE = eINSTANCE.getBusinessTable_PhysicalTable();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessColumnImpl <em>Business Column</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessColumnImpl
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessColumn()
		 * @generated
		 */
		EClass BUSINESS_COLUMN = eINSTANCE.getBusinessColumn();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUSINESS_COLUMN__NAME = eINSTANCE.getBusinessColumn_Name();

		/**
		 * The meta object literal for the '<em><b>Physical Column</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_COLUMN__PHYSICAL_COLUMN = eINSTANCE.getBusinessColumn_PhysicalColumn();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl <em>Business Relationship</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessRelationship()
		 * @generated
		 */
		EClass BUSINESS_RELATIONSHIP = eINSTANCE.getBusinessRelationship();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUSINESS_RELATIONSHIP__NAME = eINSTANCE.getBusinessRelationship_Name();

		/**
		 * The meta object literal for the '<em><b>Source Table</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_RELATIONSHIP__SOURCE_TABLE = eINSTANCE.getBusinessRelationship_SourceTable();

		/**
		 * The meta object literal for the '<em><b>Destination Table</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_RELATIONSHIP__DESTINATION_TABLE = eINSTANCE.getBusinessRelationship_DestinationTable();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessViewImpl <em>Business View</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessViewImpl
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessView()
		 * @generated
		 */
		EClass BUSINESS_VIEW = eINSTANCE.getBusinessView();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUSINESS_VIEW__NAME = eINSTANCE.getBusinessView_Name();

		/**
		 * The meta object literal for the '<em><b>Columns</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_VIEW__COLUMNS = eINSTANCE.getBusinessView_Columns();

		/**
		 * The meta object literal for the '<em><b>Join Relationships</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BUSINESS_VIEW__JOIN_RELATIONSHIPS = eINSTANCE.getBusinessView_JoinRelationships();

		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.business.impl.BusinessDomainImpl <em>Business Domain</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessDomainImpl
		 * @see it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl#getBusinessDomain()
		 * @generated
		 */
		EClass BUSINESS_DOMAIN = eINSTANCE.getBusinessDomain();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BUSINESS_DOMAIN__NAME = eINSTANCE.getBusinessDomain_Name();

	}

} //BusinessModelPackage
