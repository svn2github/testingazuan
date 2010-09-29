/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business Relationship</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getSourceTable <em>Source Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getDestinationTable <em>Destination Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getSourceColumns <em>Source Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getDestinationColumns <em>Destination Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getPhysicalForeignKey <em>Physical Foreign Key</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship()
 * @model
 * @generated
 */
public interface BusinessRelationship extends ModelObject {
	/**
	 * Returns the value of the '<em><b>Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.business.BusinessModel#getRelationships <em>Relationships</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' container reference.
	 * @see #setModel(BusinessModel)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship_Model()
	 * @see it.eng.spagobi.meta.model.business.BusinessModel#getRelationships
	 * @model opposite="relationships" required="true" transient="false"
	 * @generated
	 */
	BusinessModel getModel();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getModel <em>Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' container reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(BusinessModel value);

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

	/**
	 * Returns the value of the '<em><b>Source Columns</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessColumn}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Source Columns</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source Columns</em>' reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship_SourceColumns()
	 * @model
	 * @generated
	 */
	EList<BusinessColumn> getSourceColumns();

	/**
	 * Returns the value of the '<em><b>Destination Columns</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessColumn}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Destination Columns</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Destination Columns</em>' reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship_DestinationColumns()
	 * @model
	 * @generated
	 */
	EList<BusinessColumn> getDestinationColumns();

	/**
	 * Returns the value of the '<em><b>Physical Foreign Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Foreign Key</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Foreign Key</em>' reference.
	 * @see #setPhysicalForeignKey(PhysicalForeignKey)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessRelationship_PhysicalForeignKey()
	 * @model
	 * @generated
	 */
	PhysicalForeignKey getPhysicalForeignKey();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessRelationship#getPhysicalForeignKey <em>Physical Foreign Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Physical Foreign Key</em>' reference.
	 * @see #getPhysicalForeignKey()
	 * @generated
	 */
	void setPhysicalForeignKey(PhysicalForeignKey value);

} // BusinessRelationship
