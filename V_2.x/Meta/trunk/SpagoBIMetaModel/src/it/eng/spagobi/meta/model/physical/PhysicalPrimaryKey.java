/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical;

import it.eng.spagobi.meta.model.ModelObject;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Physical Primary Key</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getTable <em>Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getColumns <em>Columns</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalPrimaryKey()
 * @model
 * @generated
 */
public interface PhysicalPrimaryKey extends ModelObject {
	/**
	 * Returns the value of the '<em><b>Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getPrimaryKeys <em>Primary Keys</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' container reference.
	 * @see #setModel(PhysicalModel)
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalPrimaryKey_Model()
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getPrimaryKeys
	 * @model opposite="primaryKeys" required="true" transient="false"
	 * @generated
	 */
	PhysicalModel getModel();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getModel <em>Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' container reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(PhysicalModel value);

	/**
	 * Returns the value of the '<em><b>Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Table</em>' reference.
	 * @see #setTable(PhysicalTable)
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalPrimaryKey_Table()
	 * @model
	 * @generated
	 */
	PhysicalTable getTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getTable <em>Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table</em>' reference.
	 * @see #getTable()
	 * @generated
	 */
	void setTable(PhysicalTable value);

	/**
	 * Returns the value of the '<em><b>Columns</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.PhysicalColumn}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Columns</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Columns</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalPrimaryKey_Columns()
	 * @model required="true"
	 * @generated
	 */
	EList<PhysicalColumn> getColumns();

} // PhysicalPrimaryKey
