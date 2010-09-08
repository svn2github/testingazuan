package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;

/**
 * @model
 */
public interface PhysicalTable extends EObject {

	/**
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
	 * @model
	 * @generated
	 */
	void setName(String name);

	/**
	 * @model
	 * @generated
	 */
	PhysicalModel getModel();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalTable#getModel <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model</em>' reference.
	 * @see #getModel()
	 * @generated
	 */
	void setModel(PhysicalModel value);

	/**
	 * @model
	 * @generated
	 */
	void setModel(PhysicalModel model);

	
	public abstract String getCatalog();
	public abstract void setCatalog(String catalog);

	
	public abstract String getSchema();
	public abstract void setSchema(String schema);

	/**
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
	 * @model
	 * @generated
	 */
	void setComment(String comment);

	/**
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
	 * @model
	 * @generated
	 */
	void setType(String type);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model columnsMany="true"
	 * @generated
	 */
	void setColumns(EList<PhysicalColumn> columns);

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
	 * @model foreignKeysMany="true"
	 * @generated
	 */
	void setForeignKeys(EList<PhysicalForeignKey> foreignKeys);

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @model foreignKeysMany="true"
	 * @generated
	 */
	void setIncomingKeys(EList<PhysicalForeignKey> foreignKeys);

	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalColumn> getColumns();

	public abstract void addColumn(PhysicalColumn column);

	public abstract PhysicalColumn getColumn(String columnName);

	
	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalPrimaryKey> getPrimaryKeys();
	
	public abstract void addPrimaryKey(PhysicalPrimaryKey primaryKey);

	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalForeignKey> getForeignKeys();
	
	public abstract void addForeignKey(PhysicalForeignKey foreignKey);

	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalForeignKey> getIncomingKeys();
	
	public abstract void addIncomingKey(PhysicalForeignKey foreignKey);

}