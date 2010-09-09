/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Physical Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getComment <em>Comment</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getType <em>Type</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getColumns <em>Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getPrimaryKey <em>Primary Key</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getForeignKeys <em>Foreign Keys</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getReverseForeignKeys <em>Reverse Foreign Keys</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable()
 * @model
 * @generated
 */
public interface PhysicalTable extends EObject {
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
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Comment</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Comment</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Comment</em>' attribute.
	 * @see #setComment(String)
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_Comment()
	 * @model
	 * @generated
	 */
	String getComment();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getComment <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Comment</em>' attribute.
	 * @see #getComment()
	 * @generated
	 */
	void setComment(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_Type()
	 * @model
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Model</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getTables <em>Tables</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Model</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model</em>' container reference.
	 * @see #setModel(PhysicalModel)
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_Model()
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModel#getTables
	 * @model opposite="tables" required="true" transient="false"
	 * @generated
	 */
	PhysicalModel getModel();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getModel <em>Model</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' container reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(PhysicalModel value);

	/**
	 * Returns the value of the '<em><b>Columns</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.PhysicalColumn}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.physical.PhysicalColumn#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Columns</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Columns</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_Columns()
	 * @see it.eng.spagobi.meta.model.physical.PhysicalColumn#getTable
	 * @model opposite="table"
	 * @generated
	 */
	EList<PhysicalColumn> getColumns();

	/**
	 * Returns the value of the '<em><b>Primary Key</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getTable <em>Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary Key</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary Key</em>' reference.
	 * @see #setPrimaryKey(PhysicalPrimaryKey)
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_PrimaryKey()
	 * @see it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getTable
	 * @model opposite="table"
	 * @generated
	 */
	PhysicalPrimaryKey getPrimaryKey();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getPrimaryKey <em>Primary Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primary Key</em>' reference.
	 * @see #getPrimaryKey()
	 * @generated
	 */
	void setPrimaryKey(PhysicalPrimaryKey value);

	/**
	 * Returns the value of the '<em><b>Foreign Keys</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceTable <em>Source Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Foreign Keys</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Foreign Keys</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_ForeignKeys()
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getSourceTable
	 * @model opposite="sourceTable"
	 * @generated
	 */
	EList<PhysicalForeignKey> getForeignKeys();

	/**
	 * Returns the value of the '<em><b>Reverse Foreign Keys</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey}.
	 * It is bidirectional and its opposite is '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationTable <em>Destination Table</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Reverse Foreign Keys</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Reverse Foreign Keys</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage#getPhysicalTable_ReverseForeignKeys()
	 * @see it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getDestinationTable
	 * @model opposite="destinationTable"
	 * @generated
	 */
	EList<PhysicalForeignKey> getReverseForeignKeys();

} // PhysicalTable
