/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.analytical;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see it.eng.spagobi.meta.model.analytical.AnalyticalModelPackage
 * @generated
 */
public interface AnalyticalModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnalyticalModelFactory eINSTANCE = it.eng.spagobi.meta.model.analytical.impl.AnalyticalModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Analytical Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Analytical Model</em>'.
	 * @generated
	 */
	AnalyticalModel createAnalyticalModel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AnalyticalModelPackage getAnalyticalModelPackage();

} //AnalyticalModelFactory
