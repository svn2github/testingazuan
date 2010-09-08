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
 * A representation of the model object '<em><b>Physical Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getDatabaseName <em>Database Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getDatabaseVersion <em>Database Version</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getCatalog <em>Catalog</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getSchema <em>Schema</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getTables <em>Tables</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getPrimaryKeys <em>Primary Keys</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getForeignKeys <em>Foreign Keys</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel()
 * @model
 * @generated
 */
public interface PhysicalModel extends EObject {
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
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Database Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Database Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Database Name</em>' attribute.
	 * @see #setDatabaseName(String)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_DatabaseName()
	 * @model
	 * @generated
	 */
	String getDatabaseName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getDatabaseName <em>Database Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database Name</em>' attribute.
	 * @see #getDatabaseName()
	 * @generated
	 */
	void setDatabaseName(String value);

	/**
	 * Returns the value of the '<em><b>Database Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Database Version</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Database Version</em>' attribute.
	 * @see #setDatabaseVersion(String)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_DatabaseVersion()
	 * @model
	 * @generated
	 */
	String getDatabaseVersion();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getDatabaseVersion <em>Database Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database Version</em>' attribute.
	 * @see #getDatabaseVersion()
	 * @generated
	 */
	void setDatabaseVersion(String value);

	/**
	 * Returns the value of the '<em><b>Catalog</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Catalog</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Catalog</em>' attribute.
	 * @see #setCatalog(String)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_Catalog()
	 * @model
	 * @generated
	 */
	String getCatalog();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getCatalog <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Catalog</em>' attribute.
	 * @see #getCatalog()
	 * @generated
	 */
	void setCatalog(String value);

	/**
	 * Returns the value of the '<em><b>Schema</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Schema</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Schema</em>' attribute.
	 * @see #setSchema(String)
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_Schema()
	 * @model
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.emf.PhysicalModel#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

	/**
	 * Returns the value of the '<em><b>Tables</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.emf.PhysicalTable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Tables</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Tables</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_Tables()
	 * @model
	 * @generated
	 */
	EList<PhysicalTable> getTables();

	/**
	 * Returns the value of the '<em><b>Primary Keys</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Primary Keys</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primary Keys</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_PrimaryKeys()
	 * @model
	 * @generated
	 */
	EList<PhysicalPrimaryKey> getPrimaryKeys();

	/**
	 * Returns the value of the '<em><b>Foreign Keys</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Foreign Keys</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Foreign Keys</em>' reference list.
	 * @see it.eng.spagobi.meta.model.physical.emf.EmfPackage#getPhysicalModel_ForeignKeys()
	 * @model
	 * @generated
	 */
	EList<PhysicalForeignKey> getForeignKeys();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setName(String name);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setDatabaseName(String databaseName);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setDatabaseVersion(String databaseVersion);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setCatalog(String catalog);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model
	 * @generated
	 */
	void setSchema(String schema);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model tablesMany="true"
	 * @generated
	 */
	void setTables(EList<PhysicalTable> tables);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model primaryKeysMany="true"
	 * @generated
	 */
	void setPrimaryKeys(EList<PhysicalPrimaryKey> primaryKeys);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model primaryKeysMany="true"
	 * @generated
	 */
	void setForeignKeys(EList<PhysicalForeignKey> primaryKeys);

} // PhysicalModel
