/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.ModelPropertyType#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.ModelPropertyType#getDescription <em>Description</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.ModelPropertyType#getCategory <em>Category</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.ModelPropertyType#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.ModelPropertyType#getAdmissibleValues <em>Admissible Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.ModelPackage#getModelPropertyType()
 * @model
 * @generated
 */
public interface ModelPropertyType extends EObject {
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
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModelPropertyType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.ModelPropertyType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModelPropertyType_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.ModelPropertyType#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Category</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.ModelPropertyCategory#getPropertyTypes <em>Property Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Category</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Category</em>' reference.
	 * @see #setCategory(ModelPropertyCategory)
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModelPropertyType_Category()
	 * @see it.eng.spagobi.meta.model.ModelPropertyCategory#getPropertyTypes
	 * @model opposite="propertyTypes" required="true"
	 * @generated
	 */
	ModelPropertyCategory getCategory();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.ModelPropertyType#getCategory <em>Category</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Category</em>' reference.
	 * @see #getCategory()
	 * @generated
	 */
	void setCategory(ModelPropertyCategory value);

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Default Value</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' attribute.
	 * @see #setDefaultValue(String)
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModelPropertyType_DefaultValue()
	 * @model
	 * @generated
	 */
	String getDefaultValue();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.ModelPropertyType#getDefaultValue <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' attribute.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(String value);

	/**
	 * Returns the value of the '<em><b>Admissible Values</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Admissible Values</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Admissible Values</em>' attribute.
	 * @see #setAdmissibleValues(String)
	 * @see it.eng.spagobi.meta.model.ModelPackage#getModelPropertyType_AdmissibleValues()
	 * @model
	 * @generated
	 */
	String getAdmissibleValues();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.ModelPropertyType#getAdmissibleValues <em>Admissible Values</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Admissible Values</em>' attribute.
	 * @see #getAdmissibleValues()
	 * @generated
	 */
	void setAdmissibleValues(String value);

} // ModelPropertyType
