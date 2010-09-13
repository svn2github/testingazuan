/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business Column</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessColumn#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessColumn#getPhysicalColumn <em>Physical Column</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessColumn()
 * @model
 * @generated
 */
public interface BusinessColumn extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessColumn_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessColumn#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Physical Column</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Column</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Column</em>' reference.
	 * @see #setPhysicalColumn(PhysicalColumn)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessColumn_PhysicalColumn()
	 * @model required="true"
	 * @generated
	 */
	PhysicalColumn getPhysicalColumn();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessColumn#getPhysicalColumn <em>Physical Column</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Physical Column</em>' reference.
	 * @see #getPhysicalColumn()
	 * @generated
	 */
	void setPhysicalColumn(PhysicalColumn value);

} // BusinessColumn
