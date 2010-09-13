/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.meta.model.physical.PhysicalTable;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessTable#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessTable#getPhysicalTable <em>Physical Table</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessTable()
 * @model
 * @generated
 */
public interface BusinessTable extends EObject {
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
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessTable_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessTable#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Physical Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Table</em>' reference.
	 * @see #setPhysicalTable(PhysicalTable)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessTable_PhysicalTable()
	 * @model required="true"
	 * @generated
	 */
	PhysicalTable getPhysicalTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessTable#getPhysicalTable <em>Physical Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Physical Table</em>' reference.
	 * @see #getPhysicalTable()
	 * @generated
	 */
	void setPhysicalTable(PhysicalTable value);

} // BusinessTable
