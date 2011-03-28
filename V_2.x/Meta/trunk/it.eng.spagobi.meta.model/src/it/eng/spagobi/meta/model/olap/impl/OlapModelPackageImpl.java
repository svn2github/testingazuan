/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.olap.impl;

import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.analytical.AnalyticalModelPackage;
import it.eng.spagobi.meta.model.analytical.impl.AnalyticalModelPackageImpl;
import it.eng.spagobi.meta.model.behavioural.BehaviouralModelPackage;
import it.eng.spagobi.meta.model.behavioural.impl.BehaviouralModelPackageImpl;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.impl.BusinessModelPackageImpl;
import it.eng.spagobi.meta.model.impl.ModelPackageImpl;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.OlapModelFactory;
import it.eng.spagobi.meta.model.olap.OlapModelPackage;
import it.eng.spagobi.meta.model.physical.PhysicalModelPackage;
import it.eng.spagobi.meta.model.physical.impl.PhysicalModelPackageImpl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class OlapModelPackageImpl extends EPackageImpl implements OlapModelPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass olapModelEClass = null;

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
	 * @see it.eng.spagobi.meta.model.olap.OlapModelPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private OlapModelPackageImpl() {
		super(eNS_URI, OlapModelFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 * 
	 * <p>This method is used to initialize {@link OlapModelPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static OlapModelPackage init() {
		if (isInited) return (OlapModelPackage)EPackage.Registry.INSTANCE.getEPackage(OlapModelPackage.eNS_URI);

		// Obtain or create and register package
		OlapModelPackageImpl theOlapModelPackage = (OlapModelPackageImpl)(EPackage.Registry.INSTANCE.get(eNS_URI) instanceof OlapModelPackageImpl ? EPackage.Registry.INSTANCE.get(eNS_URI) : new OlapModelPackageImpl());

		isInited = true;

		// Obtain or create and register interdependencies
		ModelPackageImpl theModelPackage = (ModelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI) instanceof ModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(ModelPackage.eNS_URI) : ModelPackage.eINSTANCE);
		PhysicalModelPackageImpl thePhysicalModelPackage = (PhysicalModelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(PhysicalModelPackage.eNS_URI) instanceof PhysicalModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(PhysicalModelPackage.eNS_URI) : PhysicalModelPackage.eINSTANCE);
		BusinessModelPackageImpl theBusinessModelPackage = (BusinessModelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(BusinessModelPackage.eNS_URI) instanceof BusinessModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(BusinessModelPackage.eNS_URI) : BusinessModelPackage.eINSTANCE);
		BehaviouralModelPackageImpl theBehaviouralModelPackage = (BehaviouralModelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(BehaviouralModelPackage.eNS_URI) instanceof BehaviouralModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(BehaviouralModelPackage.eNS_URI) : BehaviouralModelPackage.eINSTANCE);
		AnalyticalModelPackageImpl theAnalyticalModelPackage = (AnalyticalModelPackageImpl)(EPackage.Registry.INSTANCE.getEPackage(AnalyticalModelPackage.eNS_URI) instanceof AnalyticalModelPackageImpl ? EPackage.Registry.INSTANCE.getEPackage(AnalyticalModelPackage.eNS_URI) : AnalyticalModelPackage.eINSTANCE);

		// Create package meta-data objects
		theOlapModelPackage.createPackageContents();
		theModelPackage.createPackageContents();
		thePhysicalModelPackage.createPackageContents();
		theBusinessModelPackage.createPackageContents();
		theBehaviouralModelPackage.createPackageContents();
		theAnalyticalModelPackage.createPackageContents();

		// Initialize created meta-data
		theOlapModelPackage.initializePackageContents();
		theModelPackage.initializePackageContents();
		thePhysicalModelPackage.initializePackageContents();
		theBusinessModelPackage.initializePackageContents();
		theBehaviouralModelPackage.initializePackageContents();
		theAnalyticalModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theOlapModelPackage.freeze();

  
		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(OlapModelPackage.eNS_URI, theOlapModelPackage);
		return theOlapModelPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass getOlapModel() {
		return olapModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OlapModelFactory getOlapModelFactory() {
		return (OlapModelFactory)getEFactoryInstance();
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
		olapModelEClass = createEClass(OLAP_MODEL);
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
		initEClass(olapModelEClass, OlapModel.class, "OlapModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
	}

} //OlapModelPackageImpl
