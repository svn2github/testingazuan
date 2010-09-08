package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import java.util.List;
/**
 * @model
 */
public interface PhysicalForeignKey extends EObject {

	/**
	 * @model
	 * @generated
	 */
	String getFkName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getFkName <em>Fk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fk Name</em>' attribute.
	 * @see #getFkName()
	 * @generated
	 */
	void setFkName(String value);

	/**
	 * @model
	 * @generated
	 */
	void setFkName(String fkName);

	/**
	 * @model
	 * @generated
	 */
	String getPkName();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getPkName <em>Pk Name</em>}' attribute.
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

	public abstract String getFkTableName();

	public abstract void setFkTableName(String fkTableName);

	public abstract List<String> getFkColumnNames();

	public abstract void setFkColumnNames(List<String> fkColumnNames);

	public abstract String getPkTableName();

	public abstract void setPkTableName(String pkTableName);

	public abstract List<String> getPkColumnNames();

	public abstract void addFkColumnName(String columnName);

	public abstract void addPkColumnName(String columnName);

	public abstract void setPkColumnNames(List<String> pkColumnNames);

	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalColumn> getFkColumns();

	/**
	 * @model
	 * @generated
	 */
	EList<PhysicalColumn> getPkColumns();

	public abstract void addFkColumn(PhysicalColumn fkColumn);

	public abstract void addPkColumn(PhysicalColumn pkColumn);

	/**
	 * @model
	 * @generated
	 */
	PhysicalTable getPkTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getPkTable <em>Pk Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pk Table</em>' reference.
	 * @see #getPkTable()
	 * @generated
	 */
	void setPkTable(PhysicalTable value);

	/**
	 * @model
	 * @generated
	 */
	void setPkTable(PhysicalTable pkTable);

	/**
	 * @model
	 * @generated
	 */
	PhysicalTable getFkTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.physical.PhysicalForeignKey#getFkTable <em>Fk Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fk Table</em>' reference.
	 * @see #getFkTable()
	 * @generated
	 */
	void setFkTable(PhysicalTable value);

	/**
	 * @model
	 * @generated
	 */
	void setFkTable(PhysicalTable fkTable);

}