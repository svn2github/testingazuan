/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getSourceTable <em>Source Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getDestinationTable <em>Destination Table</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship()
 * @model
 * @generated
 */
public interface BusinessRelationship extends EObject {
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
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Source Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Table</em>' reference.
	 * @see #setSourceTable(BusinessTable)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship_SourceTable()
	 * @model required="true"
	 * @generated
	 */
	BusinessTable getSourceTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getSourceTable <em>Source Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Table</em>' reference.
	 * @see #getSourceTable()
	 * @generated
	 */
	void setSourceTable(BusinessTable value);

	/**
	 * Returns the value of the '<em><b>Destination Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination Table</em>' reference.
	 * @see #setDestinationTable(BusinessTable)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship_DestinationTable()
	 * @model required="true"
	 * @generated
	 */
	BusinessTable getDestinationTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getDestinationTable <em>Destination Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Destination Table</em>' reference.
	 * @see #getDestinationTable()
	 * @generated
	 */
	void setDestinationTable(BusinessTable value);

} // BusinessRelationship
