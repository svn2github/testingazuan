/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.physical.impl;

import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
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

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Physical Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getComment <em>Comment</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getType <em>Type</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getColumns <em>Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getPrimaryKey <em>Primary Key</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getForeignKeys <em>Foreign Keys</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.physical.impl.PhysicalTableImpl#getReverseForeignKeys <em>Reverse Foreign Keys</em>}</li>
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
	 * The cached value of the '{@link #getPrimaryKey() <em>Primary Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimaryKey()
	 * @generated
	 * @ordered
	 */
	protected PhysicalPrimaryKey primaryKey;

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
	 * The cached value of the '{@link #getReverseForeignKeys() <em>Reverse Foreign Keys</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReverseForeignKeys()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalForeignKey> reverseForeignKeys;

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
		return PhysicalModelPackage.Literals.PHYSICAL_TABLE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_TABLE__NAME, oldName, name));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_TABLE__COMMENT, oldComment, comment));
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
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_TABLE__TYPE, oldType, type));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalModel getModel() {
		if (eContainerFeatureID != PhysicalModelPackage.PHYSICAL_TABLE__MODEL) return null;
		return (PhysicalModel)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModel(PhysicalModel newModel, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModel, PhysicalModelPackage.PHYSICAL_TABLE__MODEL, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(PhysicalModel newModel) {
		if (newModel != eInternalContainer() || (eContainerFeatureID != PhysicalModelPackage.PHYSICAL_TABLE__MODEL && newModel != null)) {
			if (EcoreUtil.isAncestor(this, newModel))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModel != null)
				msgs = ((InternalEObject)newModel).eInverseAdd(this, PhysicalModelPackage.PHYSICAL_MODEL__TABLES, PhysicalModel.class, msgs);
			msgs = basicSetModel(newModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_TABLE__MODEL, newModel, newModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalColumn> getColumns() {
		if (columns == null) {
			columns = new EObjectWithInverseResolvingEList<PhysicalColumn>(PhysicalColumn.class, this, PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS, PhysicalModelPackage.PHYSICAL_COLUMN__TABLE);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalPrimaryKey getPrimaryKey() {
		if (primaryKey != null && primaryKey.eIsProxy()) {
			InternalEObject oldPrimaryKey = (InternalEObject)primaryKey;
			primaryKey = (PhysicalPrimaryKey)eResolveProxy(oldPrimaryKey);
			if (primaryKey != oldPrimaryKey) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY, oldPrimaryKey, primaryKey));
			}
		}
		return primaryKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalPrimaryKey basicGetPrimaryKey() {
		return primaryKey;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPrimaryKey(PhysicalPrimaryKey newPrimaryKey, NotificationChain msgs) {
		PhysicalPrimaryKey oldPrimaryKey = primaryKey;
		primaryKey = newPrimaryKey;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY, oldPrimaryKey, newPrimaryKey);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPrimaryKey(PhysicalPrimaryKey newPrimaryKey) {
		if (newPrimaryKey != primaryKey) {
			NotificationChain msgs = null;
			if (primaryKey != null)
				msgs = ((InternalEObject)primaryKey).eInverseRemove(this, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE, PhysicalPrimaryKey.class, msgs);
			if (newPrimaryKey != null)
				msgs = ((InternalEObject)newPrimaryKey).eInverseAdd(this, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE, PhysicalPrimaryKey.class, msgs);
			msgs = basicSetPrimaryKey(newPrimaryKey, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY, newPrimaryKey, newPrimaryKey));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalForeignKey> getForeignKeys() {
		if (foreignKeys == null) {
			foreignKeys = new EObjectWithInverseResolvingEList<PhysicalForeignKey>(PhysicalForeignKey.class, this, PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS, PhysicalModelPackage.PHYSICAL_FOREIGN_KEY__SOURCE_TABLE);
		}
		return foreignKeys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalForeignKey> getReverseForeignKeys() {
		if (reverseForeignKeys == null) {
			reverseForeignKeys = new EObjectWithInverseResolvingEList<PhysicalForeignKey>(PhysicalForeignKey.class, this, PhysicalModelPackage.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS, PhysicalModelPackage.PHYSICAL_FOREIGN_KEY__DESTINATION_TABLE);
		}
		return reverseForeignKeys;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case PhysicalModelPackage.PHYSICAL_TABLE__MODEL:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModel((PhysicalModel)otherEnd, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getColumns()).basicAdd(otherEnd, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY:
				if (primaryKey != null)
					msgs = ((InternalEObject)primaryKey).eInverseRemove(this, PhysicalModelPackage.PHYSICAL_PRIMARY_KEY__TABLE, PhysicalPrimaryKey.class, msgs);
				return basicSetPrimaryKey((PhysicalPrimaryKey)otherEnd, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getForeignKeys()).basicAdd(otherEnd, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getReverseForeignKeys()).basicAdd(otherEnd, msgs);
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
			case PhysicalModelPackage.PHYSICAL_TABLE__MODEL:
				return basicSetModel(null, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS:
				return ((InternalEList<?>)getColumns()).basicRemove(otherEnd, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY:
				return basicSetPrimaryKey(null, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				return ((InternalEList<?>)getForeignKeys()).basicRemove(otherEnd, msgs);
			case PhysicalModelPackage.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS:
				return ((InternalEList<?>)getReverseForeignKeys()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID) {
			case PhysicalModelPackage.PHYSICAL_TABLE__MODEL:
				return eInternalContainer().eInverseRemove(this, PhysicalModelPackage.PHYSICAL_MODEL__TABLES, PhysicalModel.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case PhysicalModelPackage.PHYSICAL_TABLE__NAME:
				return getName();
			case PhysicalModelPackage.PHYSICAL_TABLE__COMMENT:
				return getComment();
			case PhysicalModelPackage.PHYSICAL_TABLE__TYPE:
				return getType();
			case PhysicalModelPackage.PHYSICAL_TABLE__MODEL:
				return getModel();
			case PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS:
				return getColumns();
			case PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY:
				if (resolve) return getPrimaryKey();
				return basicGetPrimaryKey();
			case PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				return getForeignKeys();
			case PhysicalModelPackage.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS:
				return getReverseForeignKeys();
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
			case PhysicalModelPackage.PHYSICAL_TABLE__NAME:
				setName((String)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__COMMENT:
				setComment((String)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__TYPE:
				setType((String)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__MODEL:
				setModel((PhysicalModel)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS:
				getColumns().clear();
				getColumns().addAll((Collection<? extends PhysicalColumn>)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY:
				setPrimaryKey((PhysicalPrimaryKey)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				getForeignKeys().clear();
				getForeignKeys().addAll((Collection<? extends PhysicalForeignKey>)newValue);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS:
				getReverseForeignKeys().clear();
				getReverseForeignKeys().addAll((Collection<? extends PhysicalForeignKey>)newValue);
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
			case PhysicalModelPackage.PHYSICAL_TABLE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__COMMENT:
				setComment(COMMENT_EDEFAULT);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__TYPE:
				setType(TYPE_EDEFAULT);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__MODEL:
				setModel((PhysicalModel)null);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS:
				getColumns().clear();
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY:
				setPrimaryKey((PhysicalPrimaryKey)null);
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				getForeignKeys().clear();
				return;
			case PhysicalModelPackage.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS:
				getReverseForeignKeys().clear();
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
			case PhysicalModelPackage.PHYSICAL_TABLE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case PhysicalModelPackage.PHYSICAL_TABLE__COMMENT:
				return COMMENT_EDEFAULT == null ? comment != null : !COMMENT_EDEFAULT.equals(comment);
			case PhysicalModelPackage.PHYSICAL_TABLE__TYPE:
				return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
			case PhysicalModelPackage.PHYSICAL_TABLE__MODEL:
				return getModel() != null;
			case PhysicalModelPackage.PHYSICAL_TABLE__COLUMNS:
				return columns != null && !columns.isEmpty();
			case PhysicalModelPackage.PHYSICAL_TABLE__PRIMARY_KEY:
				return primaryKey != null;
			case PhysicalModelPackage.PHYSICAL_TABLE__FOREIGN_KEYS:
				return foreignKeys != null && !foreignKeys.isEmpty();
			case PhysicalModelPackage.PHYSICAL_TABLE__REVERSE_FOREIGN_KEYS:
				return reverseForeignKeys != null && !reverseForeignKeys.isEmpty();
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
