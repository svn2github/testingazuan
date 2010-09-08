/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.emf.impl;

import it.eng.spagobi.meta.model.physical.emf.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class EmfFactoryImpl extends EFactoryImpl implements EmfFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static EmfFactory init() {
		try {
			EmfFactory theEmfFactory = (EmfFactory)EPackage.Registry.INSTANCE.getEFactory("http:///it/eng/spagobi/meta/model/physical/emf.ecore"); 
			if (theEmfFactory != null) {
				return theEmfFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new EmfFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmfFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case EmfPackage.PHYSICAL_COLUMN: return createPhysicalColumn();
			case EmfPackage.PHYSICAL_FOREIGN_KEY: return createPhysicalForeignKey();
			case EmfPackage.PHYSICAL_MODEL: return createPhysicalModel();
			case EmfPackage.PHYSICAL_PRIMARY_KEY: return createPhysicalPrimaryKey();
			case EmfPackage.PHYSICAL_TABLE: return createPhysicalTable();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalColumn createPhysicalColumn() {
		PhysicalColumnImpl physicalColumn = new PhysicalColumnImpl();
		return physicalColumn;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalForeignKey createPhysicalForeignKey() {
		PhysicalForeignKeyImpl physicalForeignKey = new PhysicalForeignKeyImpl();
		return physicalForeignKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalModel createPhysicalModel() {
		PhysicalModelImpl physicalModel = new PhysicalModelImpl();
		return physicalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalPrimaryKey createPhysicalPrimaryKey() {
		PhysicalPrimaryKeyImpl physicalPrimaryKey = new PhysicalPrimaryKeyImpl();
		return physicalPrimaryKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable createPhysicalTable() {
		PhysicalTableImpl physicalTable = new PhysicalTableImpl();
		return physicalTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EmfPackage getEmfPackage() {
		return (EmfPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static EmfPackage getPackage() {
		return EmfPackage.eINSTANCE;
	}

} //EmfFactoryImpl
