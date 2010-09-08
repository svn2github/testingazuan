/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.util;

import it.eng.spagobi.meta.model.physical.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see it.eng.spagobi.meta.model.physical.PhysicalPackage
 * @generated
 */
public class PhysicalAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static PhysicalPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = PhysicalPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PhysicalSwitch<Adapter> modelSwitch =
		new PhysicalSwitch<Adapter>() {
			@Override
			public Adapter casePhysicalColumn(PhysicalColumn object) {
				return createPhysicalColumnAdapter();
			}
			@Override
			public Adapter casePhysicalForeignKey(PhysicalForeignKey object) {
				return createPhysicalForeignKeyAdapter();
			}
			@Override
			public Adapter casePhysicalModel(PhysicalModel object) {
				return createPhysicalModelAdapter();
			}
			@Override
			public Adapter casePhysicalPrimaryKey(PhysicalPrimaryKey object) {
				return createPhysicalPrimaryKeyAdapter();
			}
			@Override
			public Adapter casePhysicalTable(PhysicalTable object) {
				return createPhysicalTableAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn <em>Column</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn
	 * @generated
	 */
	public Adapter createPhysicalColumnAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey <em>Foreign Key</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey
	 * @generated
	 */
	public Adapter createPhysicalForeignKeyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link it.eng.spagobi.meta.model.physical.PhysicalModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel
	 * @generated
	 */
	public Adapter createPhysicalModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey <em>Primary Key</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey
	 * @generated
	 */
	public Adapter createPhysicalPrimaryKeyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link it.eng.spagobi.meta.model.physical.PhysicalTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalTable
	 * @generated
	 */
	public Adapter createPhysicalTableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //PhysicalAdapterFactory
