/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.emf.impl;

import it.eng.spagobi.meta.model.physical.emf.EmfPackage;
import it.eng.spagobi.meta.model.physical.emf.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.emf.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.emf.PhysicalModel;
import it.eng.spagobi.meta.model.physical.emf.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.emf.PhysicalTable;

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
 * An implementation of the model object '<em><b>Physical Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getType <em>Type</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getColumns <em>Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getPrimaryKeys <em>Primary Keys</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getForeignKeys <em>Foreign Keys</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.emf.impl.PhysicalTableImpl#getIncomingKeys <em>Incoming Keys</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class PhysicalTableImpl extends EObjectImpl implements PhysicalTable {
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
	 * The default value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected static final String COMMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComment() <em>Comment</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComment()
	 * @generated
	 * @ordered
	 */
	protected String comment = COMMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected static final String TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getType() <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getType()
	 * @generated
	 * @ordered
	 */
	protected String type = TYPE_EDEFAULT;

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
	 * The cached value of the '{@link #getPrimaryKeys() <em>Primary Keys</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKeys()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalPrimaryKey> primaryKeys;

	/**
	 * The cached value of the '{@link #getForeignKeys() <em>Foreign Keys</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getForeignKeys()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalForeignKey> foreignKeys;

	/**
	 * The cached value of the '{@link #getIncomingKeys() <em>Incoming Keys</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIncomingKeys()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalForeignKey> incomingKeys;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PhysicalTableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return EmfPackage.Literals.PHYSICAL_TABLE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, EmfPackage.PHYSICAL_TABLE__NAME, oldName, name));
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
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, EmfPackage.PHYSICAL_TABLE__MODEL, oldModel, model));
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
	public void setModel(PhysicalModel newModel) {
		PhysicalModel oldModel = model;
		model = newModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfPackage.PHYSICAL_TABLE__MODEL, oldModel, model));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComment(String newComment) {
		String oldComment = comment;
		comment = newComment;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfPackage.PHYSICAL_TABLE__COMMENT, oldComment, comment));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getType() {
		return type;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setType(String newType) {
		String oldType = type;
		type = newType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, EmfPackage.PHYSICAL_TABLE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalColumn> getColumns() {
		if (columns == null) {
			columns = new EObjectResolvingEList<PhysicalColumn>(PhysicalColumn.class, this, EmfPackage.PHYSICAL_TABLE__COLUMNS);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalPrimaryKey> getPrimaryKeys() {
		if (primaryKeys == null) {
			primaryKeys = new EObjectResolvingEList<PhysicalPrimaryKey>(PhysicalPrimaryKey.class, this, EmfPackage.PHYSICAL_TABLE__PRIMARY_KEYS);
		}
		return primaryKeys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalForeignKey> getForeignKeys() {
		if (foreignKeys == null) {
			foreignKeys = new EObjectResolvingEList<PhysicalForeignKey>(PhysicalForeignKey.class, this, EmfPackage.PHYSICAL_TABLE__FOREIGN_KEYS);
		}
		return foreignKeys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalForeignKey> getIncomingKeys() {
		if (incomingKeys == null) {
			incomingKeys = new EObjectResolvingEList<PhysicalForeignKey>(PhysicalForeignKey.class, this, EmfPackage.PHYSICAL_TABLE__INCOMING_KEYS);
		}
		return incomingKeys;
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
	public void setPrimaryKeys(EList<PhysicalPrimaryKey> primaryKeys) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setForeignKeys(EList<PhysicalForeignKey> foreignKeys) {
		// TODO: implement this method
		// Ensure that you remove @generated or mark it @generated NOT
		throw new UnsupportedOperationException();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setIncomingKeys(EList<PhysicalForeignKey> foreignKeys) {
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
			case EmfPackage.PHYSICAL_TABLE__NAME:
				return getName();
			case EmfPackage.PHYSICAL_TABLE__MODEL:
				if (resolve) return getModel();
				return basicGetModel();
			case EmfPackage.PHYSICAL_TABLE__COMMENT:
				return getComment();
			case EmfPackage.PHYSICAL_TABLE__TYPE:
				return getType();
			case EmfPackage.PHYSICAL_TABLE__COLUMNS:
				return getColumns();
			case EmfPackage.PHYSICAL_TABLE__PRIMARY_KEYS:
				return getPrimaryKeys();
			case EmfPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				return getForeignKeys();
			case EmfPackage.PHYSICAL_TABLE__INCOMING_KEYS:
				return getIncomingKeys();
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
			case EmfPackage.PHYSICAL_TABLE__NAME:
				setName((String)newValue);
				return;
			case EmfPackage.PHYSICAL_TABLE__MODEL:
				setModel((PhysicalModel)newValue);
				return;
			case EmfPackage.PHYSICAL_TABLE__COMMENT:
				setComment((String)newValue);
				return;
			case EmfPackage.PHYSICAL_TABLE__TYPE:
				setType((String)newValue);
				return;
			case EmfPackage.PHYSICAL_TABLE__COLUMNS:
				getColumns().clear();
				getColumns().addAll((Collection<? extends PhysicalColumn>)newValue);
				return;
			case EmfPackage.PHYSICAL_TABLE__PRIMARY_KEYS:
				getPrimaryKeys().clear();
				getPrimaryKeys().addAll((Collection<? extends PhysicalPrimaryKey>)newValue);
				return;
			case EmfPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				getForeignKeys().clear();
				getForeignKeys().addAll((Collection<? extends PhysicalForeignKey>)newValue);
				return;
			case EmfPackage.PHYSICAL_TABLE__INCOMING_KEYS:
				getIncomingKeys().clear();
				getIncomingKeys().addAll((Collection<? extends PhysicalForeignKey>)newValue);
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
			case EmfPackage.PHYSICAL_TABLE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case EmfPackage.PHYSICAL_TABLE__MODEL:
				setModel((PhysicalModel)null);
				return;
			case EmfPackage.PHYSICAL_TABLE__COMMENT:
				setComment(COMMENT_EDEFAULT);
				return;
			case EmfPackage.PHYSICAL_TABLE__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case EmfPackage.PHYSICAL_TABLE__COLUMNS:
				getColumns().clear();
				return;
			case EmfPackage.PHYSICAL_TABLE__PRIMARY_KEYS:
				getPrimaryKeys().clear();
				return;
			case EmfPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				getForeignKeys().clear();
				return;
			case EmfPackage.PHYSICAL_TABLE__INCOMING_KEYS:
				getIncomingKeys().clear();
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
			case EmfPackage.PHYSICAL_TABLE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case EmfPackage.PHYSICAL_TABLE__MODEL:
				return model != null;
			case EmfPackage.PHYSICAL_TABLE__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
			case EmfPackage.PHYSICAL_TABLE__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case EmfPackage.PHYSICAL_TABLE__COLUMNS:
				return columns != null && !columns.isEmpty();
			case EmfPackage.PHYSICAL_TABLE__PRIMARY_KEYS:
				return primaryKeys != null && !primaryKeys.isEmpty();
			case EmfPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				return foreignKeys != null && !foreignKeys.isEmpty();
			case EmfPackage.PHYSICAL_TABLE__INCOMING_KEYS:
				return incomingKeys != null && !incomingKeys.isEmpty();
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
		result.append(", comment: ");
		result.append(comment);
		result.append(", type: ");
		result.append(type);
		result.append(')');
		return result.toString();
	}

} //PhysicalTableImpl
