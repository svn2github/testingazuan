/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.impl;

import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

import it.eng.spagobi.meta.model.physical.PhysicalModel;

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
 * An implementation of the model object '<em><b>Business Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessModelImpl#getPhysicalModel <em>Physical Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessModelImpl#getTables <em>Tables</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessModelImpl#getRelationships <em>Relationships</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BusinessModelImpl extends EObjectImpl implements BusinessModel {
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
	 * The cached value of the '{@link #getPhysicalModel() <em>Physical Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalModel()
	 * @generated
	 * @ordered
	 */
	protected PhysicalModel physicalModel;

	/**
	 * The cached value of the '{@link #getTables() <em>Tables</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTables()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessTable> tables;

	/**
	 * The cached value of the '{@link #getRelationships() <em>Relationships</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRelationships()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessRelationship> relationships;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BusinessModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BusinessModelPackage.Literals.BUSINESS_MODEL;
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
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalModel getPhysicalModel() {
		if (physicalModel != null && physicalModel.eIsProxy()) {
			InternalEObject oldPhysicalModel = (InternalEObject)physicalModel;
			physicalModel = (PhysicalModel)eResolveProxy(oldPhysicalModel);
			if (physicalModel != oldPhysicalModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BusinessModelPackage.BUSINESS_MODEL__PHYSICAL_MODEL, oldPhysicalModel, physicalModel));
			}
		}
		return physicalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalModel basicGetPhysicalModel() {
		return physicalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPhysicalModel(PhysicalModel newPhysicalModel) {
		PhysicalModel oldPhysicalModel = physicalModel;
		physicalModel = newPhysicalModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_MODEL__PHYSICAL_MODEL, oldPhysicalModel, physicalModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessTable> getTables() {
		if (tables == null) {
			tables = new EObjectResolvingEList<BusinessTable>(BusinessTable.class, this, BusinessModelPackage.BUSINESS_MODEL__TABLES);
		}
		return tables;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessRelationship> getRelationships() {
		if (relationships == null) {
			relationships = new EObjectResolvingEList<BusinessRelationship>(BusinessRelationship.class, this, BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS);
		}
		return relationships;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BusinessModelPackage.BUSINESS_MODEL__NAME:
				return getName();
			case BusinessModelPackage.BUSINESS_MODEL__PHYSICAL_MODEL:
				if (resolve) return getPhysicalModel();
				return basicGetPhysicalModel();
			case BusinessModelPackage.BUSINESS_MODEL__TABLES:
				return getTables();
			case BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS:
				return getRelationships();
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
			case BusinessModelPackage.BUSINESS_MODEL__NAME:
				setName((String)newValue);
				return;
			case BusinessModelPackage.BUSINESS_MODEL__PHYSICAL_MODEL:
				setPhysicalModel((PhysicalModel)newValue);
				return;
			case BusinessModelPackage.BUSINESS_MODEL__TABLES:
				getTables().clear();
				getTables().addAll((Collection<? extends BusinessTable>)newValue);
				return;
			case BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS:
				getRelationships().clear();
				getRelationships().addAll((Collection<? extends BusinessRelationship>)newValue);
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
			case BusinessModelPackage.BUSINESS_MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
			case BusinessModelPackage.BUSINESS_MODEL__PHYSICAL_MODEL:
				setPhysicalModel((PhysicalModel)null);
				return;
			case BusinessModelPackage.BUSINESS_MODEL__TABLES:
				getTables().clear();
				return;
			case BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS:
				getRelationships().clear();
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
			case BusinessModelPackage.BUSINESS_MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case BusinessModelPackage.BUSINESS_MODEL__PHYSICAL_MODEL:
				return physicalModel != null;
			case BusinessModelPackage.BUSINESS_MODEL__TABLES:
				return tables != null && !tables.isEmpty();
			case BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS:
				return relationships != null && !relationships.isEmpty();
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

} //BusinessModelImpl
