/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getPhysicalModel <em>Physical Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getTables <em>Tables</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getRelationships <em>Relationships</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel()
 * @model
 * @generated
 */
public interface BusinessModel extends EObject {
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
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Physical Model</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Model</em>' reference.
	 * @see #setPhysicalModel(PhysicalModel)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_PhysicalModel()
	 * @model required="true"
	 * @generated
	 */
	PhysicalModel getPhysicalModel();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessModel#getPhysicalModel <em>Physical Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Physical Model</em>' reference.
	 * @see #getPhysicalModel()
	 * @generated
	 */
	void setPhysicalModel(PhysicalModel value);

	/**
	 * Returns the value of the '<em><b>Tables</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessTable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tables</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tables</em>' reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Tables()
	 * @model
	 * @generated
	 */
	EList<BusinessTable> getTables();

	/**
	 * Returns the value of the '<em><b>Relationships</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessRelationship}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relationships</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relationships</em>' reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Relationships()
	 * @model
	 * @generated
	 */
	EList<BusinessRelationship> getRelationships();

} // BusinessModel
