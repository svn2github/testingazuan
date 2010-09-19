/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.meta.model.ModelObject;
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
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessColumn#getPhysicalColumn <em>Physical Column</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessColumn#getTable <em>Table</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessColumn()
 * @model
 * @generated
 */
public interface BusinessColumn extends ModelObject {
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

	/**
	 * Returns the value of the '<em><b>Table</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.business.BusinessTable#getColumns <em>Columns</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Table</em>' container reference.
	 * @see #setTable(BusinessTable)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessColumn_Table()
	 * @see it.eng.spagobi.meta.model.business.BusinessTable#getColumns
	 * @model opposite="columns" required="true" transient="false"
	 * @generated
	 */
	BusinessTable getTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessColumn#getTable <em>Table</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table</em>' container reference.
	 * @see #getTable()
	 * @generated
	 */
	void setTable(BusinessTable value);

} // BusinessColumn
