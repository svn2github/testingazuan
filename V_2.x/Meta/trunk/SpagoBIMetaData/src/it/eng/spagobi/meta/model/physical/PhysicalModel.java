package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;

/**
 * @model
 */
public interface PhysicalModel extends EObject {

	/**
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * @model
	 * @generated
	 */
	void setName(String name);

	/**
	 * @model
	 * @generated
	 */
	String getDatabaseName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getDatabaseName <em>Database Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database Name</em>' attribute.
	 * @see #getDatabaseName()
	 * @generated
	 */
	void setDatabaseName(String value);

	/**
	 * @model
	 * @generated
	 */
	void setDatabaseName(String databaseName);

	/**
	 * @model
	 * @generated
	 */
	String getDatabaseVersion();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getDatabaseVersion <em>Database Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Database Version</em>' attribute.
	 * @see #getDatabaseVersion()
	 * @generated
	 */
	void setDatabaseVersion(String value);

	/**
	 * @model
	 * @generated
	 */
	void setDatabaseVersion(String databaseVersion);

	public abstract void setDatabaseVersion(String databaseName, String databaseVersion);

	/**
	 * @model
	 * @generated
	 */
	String getCatalog();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getCatalog <em>Catalog</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Catalog</em>' attribute.
	 * @see #getCatalog()
	 * @generated
	 */
	void setCatalog(String value);

	/**
	 * @model
	 * @generated
	 */
	void setCatalog(String catalog);

	/**
	 * @model
	 * @generated
	 */
	String getSchema();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalModel#getSchema <em>Schema</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Schema</em>' attribute.
	 * @see #getSchema()
	 * @generated
	 */
	void setSchema(String value);

	/**
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

	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalTable> getTables();

	public abstract void addTables(List<PhysicalTable> tables);
		
	public abstract void addTable(PhysicalTable table);

	public abstract PhysicalTable getTable(String tableName);
	
	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalPrimaryKey> getPrimaryKeys();
	
	public abstract void addPrimaryKeys(List<PhysicalPrimaryKey> primaryKeys);

	public abstract void addPrimaryKey(PhysicalPrimaryKey primaryKey);

	
	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalForeignKey> getForeignKeys();
	
	public abstract void addForeignKeys(List<PhysicalForeignKey> foreignKeys);

	public abstract void addForeignKey(PhysicalForeignKey foreignKey);
	

	

}