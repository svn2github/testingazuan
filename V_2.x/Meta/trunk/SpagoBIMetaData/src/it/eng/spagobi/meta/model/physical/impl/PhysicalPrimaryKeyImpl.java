/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.impl;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalModelPackage;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Physical Primary Key</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl#getTable <em>Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalPrimaryKeyImpl#getColumns <em>Columns</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PhysicalPrimaryKeyImpl extends EObjectImpl implements PhysicalPrimaryKey {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected PhysicalModel model;

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
		return PhysicalModelPackage.Literals.PHYSICAL_PRIMARY_KEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalModel getModel() {
		if (model != null && model.eIsProxy()) {
			InternalEObject oldModel = (InternalEObject)model;
			model = (PhysicalModel)eResolveProxy(oldModel);
			if (model != oldModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL, oldModel, model));
			}
		}
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalModel basicGetModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModel(PhysicalModel newModel, NotificationChain msgs) {
		PhysicalModel oldModel = model;
		model = newModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL, oldModel, newModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(PhysicalModel newModel) {
		if (newModel != model) {
			NotificationChain msgs = null;
			if (model != null)
				msgs = ((InternalEObject)model).eInverseRemove(this, PhysicalModelPackage.PHYSICAL_MODEL__PRIMARY_KEYS, PhysicalModel.class, msgs);
			if (newModel != null)
				msgs = ((InternalEObject)newModel).eInverseAdd(this, PhysicalModelPackage.PHYSICAL_MODEL__PRIMARY_KEYS, PhysicalModel.class, msgs);
			msgs = basicSetModel(newModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL, newModel, newModel));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE, oldTable, table));
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
	public NotificationChain basicSetTable(PhysicalTable newTable, NotificationChain msgs) {
		PhysicalTable oldTable = table;
		table = newTable;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE, oldTable, newTable);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTable(PhysicalTable newTable) {
		if (newTable != table) {
			NotificationChain msgs = null;
			if (table != null)
				msgs = ((InternalEObject)table).eInverseRemove(this, PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY, PhysicalTable.class, msgs);
			if (newTable != null)
				msgs = ((InternalEObject)newTable).eInverseAdd(this, PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY, PhysicalTable.class, msgs);
			msgs = basicSetTable(newTable, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE, newTable, newTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalColumn> getColumns() {
		if (columns == null) {
			columns = new EObjectResolvingEList<PhysicalColumn>(PhysicalColumn.class, this, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__COLUMNS);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL:
				if (model != null)
					msgs = ((InternalEObject)model).eInverseRemove(this, PhysicalModelPackage.PHYSICAL_MODEL__PRIMARY_KEYS, PhysicalModel.class, msgs);
				return basicSetModel((PhysicalModel)otherEnd, msgs);
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				if (table != null)
					msgs = ((InternalEObject)table).eInverseRemove(this, PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY, PhysicalTable.class, msgs);
				return basicSetTable((PhysicalTable)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL:
				return basicSetModel(null, msgs);
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				return basicSetTable(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__NAME:
				return getName();
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL:
				if (resolve) return getModel();
				return basicGetModel();
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				if (resolve) return getTable();
				return basicGetTable();
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
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
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__NAME:
				setName((String)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL:
				setModel((PhysicalModel)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				setTable((PhysicalTable)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
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
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL:
				setModel((PhysicalModel)null);
				return;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				setTable((PhysicalTable)null);
				return;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
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
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__MODEL:
				return model != null;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE:
				return table != null;
			case PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__COLUMNS:
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //PhysicalPrimaryKeyImpl
