/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.impl;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalPackage;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Primary Key</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl#getPkName <em>Pk Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl#getTable <em>Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl#getColumns <em>Columns</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PhysicalPrimaryKeyImpl extends EObjectImpl implements PhysicalPrimaryKey {
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
	 * The cached value of the '{@link #getTable() <em>Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTable()
	 * @generated
	 * @ordered
	 */
	protected PhysicalTable table;

	/**
	 * The cached value of the '{@link #getColumns() <em>Columns</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalColumn> columns;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PhysicalPrimaryKeyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return PhysicalPackage.Literals.PHYSICAL_PRIMARY_KEY;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalPackage.PHYSICAL_PRIMARY_KEY__PK_NAME, oldPkName, pkName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable getTable() {
		if (table != null && table.eIsProxy()) {
			InternalEObject oldTable = (InternalEObject)table;
			table = (PhysicalTable)eResolveProxy(oldTable);
			if (table != oldTable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PhysicalPackage.PHYSICAL_PRIMARY_KEY__TABLE, oldTable, table));
			}
		}
		return table;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable basicGetTable() {
		return table;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTable(PhysicalTable newTable) {
		PhysicalTable oldTable = table;
		table = newTable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalPackage.PHYSICAL_PRIMARY_KEY__TABLE, oldTable, table));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalColumn> getColumns() {
		if (columns == null) {
			columns = new EObjectResolvingEList<PhysicalColumn>(PhysicalColumn.class, this, PhysicalPackage.PHYSICAL_PRIMARY_KEY__COLUMNS);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setColumns(EList<PhysicalColumn> columns) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__PK_NAME:
				return getPkName();
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				if (resolve) return getTable();
				return basicGetTable();
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
				return getColumns();
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
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__PK_NAME:
				setPkName((String)newValue);
				return;
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				setTable((PhysicalTable)newValue);
				return;
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
				getColumns().clear();
				getColumns().addAll((Collection<? extends PhysicalColumn>)newValue);
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
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__PK_NAME:
				setPkName(PK_NAME_EDEFAULT);
				return;
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				setTable((PhysicalTable)null);
				return;
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
				getColumns().clear();
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
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__PK_NAME:
				return PK_NAME_EDEFAULT == null ? pkName != null : !PK_NAME_EDEFAULT.equals(pkName);
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				return table != null;
			case PhysicalPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
				return columns != null && !columns.isEmpty();
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
		result.append(" (pkName: ");
		result.append(pkName);
		result.append(')');
		return result.toString();
	}

} //PhysicalPrimaryKeyImpl
