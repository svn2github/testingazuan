/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.impl;

import it.eng.spagobi.meta.model.business.BusinessDomain;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;

import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.impl.ModelObjectImpl;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Business Domain</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessDomainImpl#getBusinesslModel <em>Businessl Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessDomainImpl#getTables <em>Tables</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessDomainImpl#getRelationships <em>Relationships</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BusinessDomainImpl extends ModelObjectImpl implements BusinessDomain {
	/**
	 * The cached value of the '{@link #getBusinesslModel() <em>Businessl Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBusinesslModel()
	 * @generated
	 * @ordered
	 */
	protected BusinessModel businesslModel;

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
	protected BusinessDomainImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BusinessModelPackage.Literals.BUSINESS_DOMAIN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessModel getBusinesslModel() {
		if (businesslModel != null && businesslModel.eIsProxy()) {
			InternalEObject oldBusinesslModel = (InternalEObject)businesslModel;
			businesslModel = (BusinessModel)eResolveProxy(oldBusinesslModel);
			if (businesslModel != oldBusinesslModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BusinessModelPackage.BUSINESS_DOMAIN__BUSINESSL_MODEL, oldBusinesslModel, businesslModel));
			}
		}
		return businesslModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessModel basicGetBusinesslModel() {
		return businesslModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBusinesslModel(BusinessModel newBusinesslModel) {
		BusinessModel oldBusinesslModel = businesslModel;
		businesslModel = newBusinesslModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_DOMAIN__BUSINESSL_MODEL, oldBusinesslModel, businesslModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessTable> getTables() {
		if (tables == null) {
			tables = new EObjectResolvingEList<BusinessTable>(BusinessTable.class, this, BusinessModelPackage.BUSINESS_DOMAIN__TABLES);
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
			relationships = new EObjectResolvingEList<BusinessRelationship>(BusinessRelationship.class, this, BusinessModelPackage.BUSINESS_DOMAIN__RELATIONSHIPS);
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
			case BusinessModelPackage.BUSINESS_DOMAIN__BUSINESSL_MODEL:
				if (resolve) return getBusinesslModel();
				return basicGetBusinesslModel();
			case BusinessModelPackage.BUSINESS_DOMAIN__TABLES:
				return getTables();
			case BusinessModelPackage.BUSINESS_DOMAIN__RELATIONSHIPS:
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
			case BusinessModelPackage.BUSINESS_DOMAIN__BUSINESSL_MODEL:
				setBusinesslModel((BusinessModel)newValue);
				return;
			case BusinessModelPackage.BUSINESS_DOMAIN__TABLES:
				getTables().clear();
				getTables().addAll((Collection<? extends BusinessTable>)newValue);
				return;
			case BusinessModelPackage.BUSINESS_DOMAIN__RELATIONSHIPS:
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
			case BusinessModelPackage.BUSINESS_DOMAIN__BUSINESSL_MODEL:
				setBusinesslModel((BusinessModel)null);
				return;
			case BusinessModelPackage.BUSINESS_DOMAIN__TABLES:
				getTables().clear();
				return;
			case BusinessModelPackage.BUSINESS_DOMAIN__RELATIONSHIPS:
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
			case BusinessModelPackage.BUSINESS_DOMAIN__BUSINESSL_MODEL:
				return businesslModel != null;
			case BusinessModelPackage.BUSINESS_DOMAIN__TABLES:
				return tables != null && !tables.isEmpty();
			case BusinessModelPackage.BUSINESS_DOMAIN__RELATIONSHIPS:
				return relationships != null && !relationships.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //BusinessDomainImpl
