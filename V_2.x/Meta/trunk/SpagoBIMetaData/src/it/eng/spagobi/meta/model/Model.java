/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model;

import it.eng.spagobi.meta.model.business.BusinessModel;

import it.eng.spagobi.meta.model.physical.PhysicalModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.Model#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.Model#getPhysicalModels <em>Physical Models</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.Model#getBusinessModels <em>Business Models</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.Model#getPropertyTypes <em>Property Types</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.ModelPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject {
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
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.Model#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Physical Models</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.PhysicalModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Models</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Models</em>' reference list.
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModel_PhysicalModels()
	 * @model
	 * @generated
	 */
	EList<PhysicalModel> getPhysicalModels();

	/**
	 * Returns the value of the '<em><b>Business Models</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessModel}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Business Models</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Business Models</em>' reference list.
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModel_BusinessModels()
	 * @model
	 * @generated
	 */
	EList<BusinessModel> getBusinessModels();

	/**
	 * Returns the value of the '<em><b>Property Types</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.ModelPropertyType}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Property Types</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Property Types</em>' reference list.
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModel_PropertyTypes()
	 * @model
	 * @generated
	 */
	EList<ModelPropertyType> getPropertyTypes();

} // Model
