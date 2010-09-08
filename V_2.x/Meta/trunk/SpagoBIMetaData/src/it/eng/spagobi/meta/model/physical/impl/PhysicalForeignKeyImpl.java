/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.impl;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalPackage;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Foreign Key</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl#getFkName <em>Fk Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl#getPkName <em>Pk Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl#getFkColumns <em>Fk Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl#getPkColumns <em>Pk Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl#getPkTable <em>Pk Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalForeignKeyImpl#getFkTable <em>Fk Table</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PhysicalForeignKeyImpl extends EObjectImpl implements PhysicalForeignKey {
	/**
	 * The default value of the '{@link #getFkName() <em>Fk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFkName()
	 * @generated
	 * @ordered
	 */
	protected static final String FK_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFkName() <em>Fk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFkName()
	 * @generated
	 * @ordered
	 */
	protected String fkName = FK_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getPkName() <em>Pk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPkName()
	 * @generated
	 * @ordered
	 */
	protected static final String PK_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPkName() <em>Pk Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPkName()
	 * @generated
	 * @ordered
	 */
	protected String pkName = PK_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getFkColumns() <em>Fk Columns</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFkColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalColumn> fkColumns;

	/**
	 * The cached value of the '{@link #getPkColumns() <em>Pk Columns</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPkColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalColumn> pkColumns;

	/**
	 * The cached value of the '{@link #getPkTable() <em>Pk Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPkTable()
	 * @generated
	 * @ordered
	 */
	protected PhysicalTable pkTable;

	/**
	 * The cached value of the '{@link #getFkTable() <em>Fk Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFkTable()
	 * @generated
	 * @ordered
	 */
	protected PhysicalTable fkTable;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PhysicalForeignKeyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PhysicalPackage.Literals.PHYSICAL_FOREIGN_KEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFkName() {
		return fkName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFkName(String newFkName) {
		String oldFkName = fkName;
		fkName = newFkName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_NAME, oldFkName, fkName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPkName() {
		return pkName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPkName(String newPkName) {
		String oldPkName = pkName;
		pkName = newPkName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_NAME, oldPkName, pkName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalColumn> getFkColumns() {
		if (fkColumns == null) {
			fkColumns = new EObjectResolvingEList<PhysicalColumn>(PhysicalColumn.class, this, PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_COLUMNS);
		}
		return fkColumns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalColumn> getPkColumns() {
		if (pkColumns == null) {
			pkColumns = new EObjectResolvingEList<PhysicalColumn>(PhysicalColumn.class, this, PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_COLUMNS);
		}
		return pkColumns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable getPkTable() {
		if (pkTable != null && pkTable.eIsProxy()) {
			InternalEObject oldPkTable = (InternalEObject)pkTable;
			pkTable = (PhysicalTable)eResolveProxy(oldPkTable);
			if (pkTable != oldPkTable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_TABLE, oldPkTable, pkTable));
			}
		}
		return pkTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable basicGetPkTable() {
		return pkTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPkTable(PhysicalTable newPkTable) {
		PhysicalTable oldPkTable = pkTable;
		pkTable = newPkTable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_TABLE, oldPkTable, pkTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable getFkTable() {
		if (fkTable != null && fkTable.eIsProxy()) {
			InternalEObject oldFkTable = (InternalEObject)fkTable;
			fkTable = (PhysicalTable)eResolveProxy(oldFkTable);
			if (fkTable != oldFkTable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_TABLE, oldFkTable, fkTable));
			}
		}
		return fkTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable basicGetFkTable() {
		return fkTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFkTable(PhysicalTable newFkTable) {
		PhysicalTable oldFkTable = fkTable;
		fkTable = newFkTable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_TABLE, oldFkTable, fkTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_NAME:
				return getFkName();
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_NAME:
				return getPkName();
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_COLUMNS:
				return getFkColumns();
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_COLUMNS:
				return getPkColumns();
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_TABLE:
				if (resolve) return getPkTable();
				return basicGetPkTable();
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_TABLE:
				if (resolve) return getFkTable();
				return basicGetFkTable();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_NAME:
				setFkName((String)newValue);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_NAME:
				setPkName((String)newValue);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_COLUMNS:
				getFkColumns().clear();
				getFkColumns().addAll((Collection<? extends PhysicalColumn>)newValue);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_COLUMNS:
				getPkColumns().clear();
				getPkColumns().addAll((Collection<? extends PhysicalColumn>)newValue);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_TABLE:
				setPkTable((PhysicalTable)newValue);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_TABLE:
				setFkTable((PhysicalTable)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_NAME:
				setFkName(FK_NAME_EDEFAULT);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_NAME:
				setPkName(PK_NAME_EDEFAULT);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_COLUMNS:
				getFkColumns().clear();
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_COLUMNS:
				getPkColumns().clear();
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_TABLE:
				setPkTable((PhysicalTable)null);
				return;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_TABLE:
				setFkTable((PhysicalTable)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_NAME:
				return FK_NAME_EDEFAULT == null ? fkName != null : !FK_NAME_EDEFAULT.equals(fkName);
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_NAME:
				return PK_NAME_EDEFAULT == null ? pkName != null : !PK_NAME_EDEFAULT.equals(pkName);
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_COLUMNS:
				return fkColumns != null && !fkColumns.isEmpty();
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_COLUMNS:
				return pkColumns != null && !pkColumns.isEmpty();
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__PK_TABLE:
				return pkTable != null;
			case PhysicalPackage.PHYSICAL_FOREIGN_KEY__FK_TABLE:
				return fkTable != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (fkName: ");
		result.append(fkName);
		result.append(", pkName: ");
		result.append(pkName);
		result.append(')');
		return result.toString();
	}

	public void addFkColumn(PhysicalColumn fkColumn) {
		// TODO Auto-generated method stub
		
	}

	public void addFkColumnName(String columnName) {
		// TODO Auto-generated method stub
		
	}

	public void addPkColumn(PhysicalColumn pkColumn) {
		// TODO Auto-generated method stub
		
	}

	public void addPkColumnName(String columnName) {
		// TODO Auto-generated method stub
		
	}

	public List<String> getFkColumnNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getFkTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<String> getPkColumnNames() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	public String getPkTableName() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setFkColumnNames(List<String> fkColumnNames) {
		// TODO Auto-generated method stub
		
	}

	public void setFkColumns(List<PhysicalColumn> fkColumns) {
		// TODO Auto-generated method stub
		
	}

	public void setFkTableName(String fkTableName) {
		// TODO Auto-generated method stub
		
	}

	public void setPkColumnNames(List<String> pkColumnNames) {
		// TODO Auto-generated method stub
		
	}

	public void setPkColumns(List<PhysicalColumn> pkColumns) {
		// TODO Auto-generated method stub
		
	}

	public void setPkTableName(String pkTableName) {
		// TODO Auto-generated method stub
		
	}

} //PhysicalForeignKeyImpl
