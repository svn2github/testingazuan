/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.behavioural;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see it.eng.spagobi.meta.model.behavioural.BehaviouralModelPackage
 * @generated
 */
public interface BehaviouralModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	BehaviouralModelFactory eINSTANCE = it.eng.spagobi.meta.model.behavioural.impl.BehaviouralModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Behavioural Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Behavioural Model</em>'.
	 * @generated
	 */
	BehaviouralModel createBehaviouralModel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	BehaviouralModelPackage getBehaviouralModelPackage();

} //BehaviouralModelFactory
