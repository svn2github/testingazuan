/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.ModelObject#getPropertyTypes <em>Property Types</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.ModelObject#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.ModelPackage#getModelObject()
 * @model
 * @generated
 */
public interface ModelObject extends EObject {
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
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModelObject_PropertyTypes()
	 * @model
	 * @generated
	 */
	EList<ModelPropertyType> getPropertyTypes();

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.ModelProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' reference list.
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModelObject_Properties()
	 * @model
	 * @generated
	 */
	EList<ModelProperty> getProperties();

} // ModelObject
