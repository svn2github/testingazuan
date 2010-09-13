/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.olap;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see it.eng.spagobi.meta.model.olap.OlapModelPackage
 * @generated
 */
public interface OlapModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OlapModelFactory eINSTANCE = it.eng.spagobi.meta.model.olap.impl.OlapModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Olap Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Olap Model</em>'.
	 * @generated
	 */
	OlapModel createOlapModel();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	OlapModelPackage getOlapModelPackage();

} //OlapModelFactory
