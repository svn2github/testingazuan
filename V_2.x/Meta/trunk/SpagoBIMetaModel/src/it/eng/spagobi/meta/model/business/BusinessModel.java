/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

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
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getParentModel <em>Parent Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getPhysicalModel <em>Physical Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getTables <em>Tables</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getRelationships <em>Relationships</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getIdentifiers <em>Identifiers</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getViews <em>Views</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessModel#getDomains <em>Domains</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel()
 * @model
 * @generated
 */
public interface BusinessModel extends ModelObject {
	/**
	 * Returns the value of the '<em><b>Parent Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.Model#getBusinessModels <em>Business Models</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent Model</em>' container reference.
	 * @see #setParentModel(Model)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_ParentModel()
	 * @see it.eng.spagobi.meta.model.Model#getBusinessModels
	 * @model opposite="businessModels" required="true" transient="false"
	 * @generated
	 */
	Model getParentModel();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessModel#getParentModel <em>Parent Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent Model</em>' container reference.
	 * @see #getParentModel()
	 * @generated
	 */
	void setParentModel(Model value);

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
	 * Returns the value of the '<em><b>Tables</b></em>' containment reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessTable}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.business.BusinessTable#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tables</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tables</em>' containment reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Tables()
	 * @see it.eng.spagobi.meta.model.business.BusinessTable#getModel
	 * @model opposite="model" containment="true"
	 * @generated
	 */
	EList<BusinessTable> getTables();

	/**
	 * Returns the value of the '<em><b>Relationships</b></em>' containment reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessRelationship}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Relationships</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Relationships</em>' containment reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Relationships()
	 * @see it.eng.spagobi.meta.model.business.BusinessRelationship#getModel
	 * @model opposite="model" containment="true"
	 * @generated
	 */
	EList<BusinessRelationship> getRelationships();
	
	/**
	 * Returns the value of the '<em><b>Identifiers</b></em>' containment reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessIdentifier}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.business.BusinessIdentifier#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Identifiers</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Identifiers</em>' containment reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Identifiers()
	 * @see it.eng.spagobi.meta.model.business.BusinessIdentifier#getModel
	 * @model opposite="model" containment="true"
	 * @generated
	 */
	EList<BusinessIdentifier> getIdentifiers();

	/**
	 * Returns the value of the '<em><b>Views</b></em>' containment reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessView}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.business.BusinessView#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Views</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Views</em>' containment reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Views()
	 * @see it.eng.spagobi.meta.model.business.BusinessView#getModel
	 * @model opposite="model" containment="true"
	 * @generated
	 */
	EList<BusinessView> getViews();

	/**
	 * Returns the value of the '<em><b>Domains</b></em>' containment reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessDomain}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.business.BusinessDomain#getModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Domains</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Domains</em>' containment reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessModel_Domains()
	 * @see it.eng.spagobi.meta.model.business.BusinessDomain#getModel
	 * @model opposite="model" containment="true"
	 * @generated
	 */
	EList<BusinessDomain> getDomains();

	// =========================================================================
	// Utility methods
	// =========================================================================
	
	
	BusinessIdentifier getIdentifier(String name);
	
	BusinessIdentifier getIdentifier(BusinessTable table);
	
	/**
	 * @return the business table with the given name
	 */
	BusinessTable getTable(String name);
	
	/**
	 * @return the business table associated to the given physical table
	 */
	BusinessTable getTable(PhysicalTable physicalTable);

} // BusinessModel
