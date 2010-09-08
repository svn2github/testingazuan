package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;

/**
 * @model
 */
public interface PhysicalPrimaryKey extends EObject {

	/**
	 * @model
	 * @generated
	 */
	String getPkName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey#getPkName <em>Pk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pk Name</em>' attribute.
	 * @see #getPkName()
	 * @generated
	 */
	void setPkName(String value);

	/**
	 * @model
	 * @generated
	 */
	void setPkName(String pkName);

	public abstract String getTableName();

	public abstract void setTableName(String tableName);

	/**
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

	public abstract List<String> getColumnNames();

	public abstract void addColumnNames(List<String> columnNames);

	public abstract void addColumnName(String columnName);

	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalColumn> getColumns();
	
	public abstract void addColumns(List<PhysicalColumn> columns);

	public abstract void addColumn(PhysicalColumn column);

}