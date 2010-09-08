/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.emf;

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
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey#getPkName <em>Pk Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey#getTable <em>Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey#getColumns <em>Columns</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalPrimaryKey()
 * @model
 * @generated
 */
public interface PhysicalPrimaryKey extends EObject {
	/**
	 * Returns the value of the '<em><b>Pk Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pk Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pk Name</em>' attribute.
	 * @see #setPkName(String)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalPrimaryKey_PkName()
	 * @model
	 * @generated
	 */
	String getPkName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey#getPkName <em>Pk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pk Name</em>' attribute.
	 * @see #getPkName()
	 * @generated
	 */
	void setPkName(String value);

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
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalPrimaryKey_Table()
	 * @model
	 * @generated
	 */
	PhysicalTable getTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey#getTable <em>Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Table</em>' reference.
	 * @see #getTable()
	 * @generated
	 */
	void setTable(PhysicalTable value);

	/**
	 * Returns the value of the '<em><b>Columns</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.emf.PhysicalColumn}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Columns</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Columns</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalPrimaryKey_Columns()
	 * @model
	 * @generated
	 */
	EList<PhysicalColumn> getColumns();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setPkName(String pkName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setTable(PhysicalTable table);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model columnsMany="true"
	 * @generated
	 */
	void setColumns(EList<PhysicalColumn> columns);

} // PhysicalPrimaryKey
