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
 * A representation of the model object '<em><b>Physical Foreign Key</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getFkName <em>Fk Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getPkName <em>Pk Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getFkColumns <em>Fk Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getPkColumns <em>Pk Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getPkTable <em>Pk Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getFkTable <em>Fk Table</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalForeignKey()
 * @model
 * @generated
 */
public interface PhysicalForeignKey extends EObject {
	/**
	 * Returns the value of the '<em><b>Fk Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fk Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fk Name</em>' attribute.
	 * @see #setFkName(String)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalForeignKey_FkName()
	 * @model
	 * @generated
	 */
	String getFkName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getFkName <em>Fk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fk Name</em>' attribute.
	 * @see #getFkName()
	 * @generated
	 */
	void setFkName(String value);

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
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalForeignKey_PkName()
	 * @model
	 * @generated
	 */
	String getPkName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getPkName <em>Pk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pk Name</em>' attribute.
	 * @see #getPkName()
	 * @generated
	 */
	void setPkName(String value);

	/**
	 * Returns the value of the '<em><b>Fk Columns</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.emf.PhysicalColumn}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fk Columns</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fk Columns</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalForeignKey_FkColumns()
	 * @model
	 * @generated
	 */
	EList<PhysicalColumn> getFkColumns();

	/**
	 * Returns the value of the '<em><b>Pk Columns</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.emf.PhysicalColumn}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pk Columns</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pk Columns</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalForeignKey_PkColumns()
	 * @model
	 * @generated
	 */
	EList<PhysicalColumn> getPkColumns();

	/**
	 * Returns the value of the '<em><b>Pk Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Pk Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Pk Table</em>' reference.
	 * @see #setPkTable(PhysicalTable)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalForeignKey_PkTable()
	 * @model
	 * @generated
	 */
	PhysicalTable getPkTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getPkTable <em>Pk Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pk Table</em>' reference.
	 * @see #getPkTable()
	 * @generated
	 */
	void setPkTable(PhysicalTable value);

	/**
	 * Returns the value of the '<em><b>Fk Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Fk Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Fk Table</em>' reference.
	 * @see #setFkTable(PhysicalTable)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalForeignKey_FkTable()
	 * @model
	 * @generated
	 */
	PhysicalTable getFkTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey#getFkTable <em>Fk Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fk Table</em>' reference.
	 * @see #getFkTable()
	 * @generated
	 */
	void setFkTable(PhysicalTable value);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setFkName(String fkName);

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
	 * @model fkColumnsMany="true"
	 * @generated
	 */
	void setFkColumns(EList<PhysicalColumn> fkColumns);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model pkColumnsMany="true"
	 * @generated
	 */
	void setPkColumns(EList<PhysicalColumn> pkColumns);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setPkTable(PhysicalTable pkTable);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setFkTable(PhysicalTable fkTable);

} // PhysicalForeignKey
