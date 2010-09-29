/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.impl;

import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessView;

import it.eng.spagobi.meta.model.impl.ModelObjectImpl;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Business View</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessViewImpl#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessViewImpl#getColumns <em>Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessViewImpl#getJoinRelationships <em>Join Relationships</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BusinessViewImpl extends ModelObjectImpl implements BusinessView {
	/**
	 * The cached value of the '{@link #getColumns() <em>Columns</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessColumn> columns;

	/**
	 * The cached value of the '{@link #getJoinRelationships() <em>Join Relationships</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJoinRelationships()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessRelationship> joinRelationships;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BusinessViewImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BusinessModelPackage.Literals.BUSINESS_VIEW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessModel getModel() {
		if (eContainerFeatureID() != BusinessModelPackage.BUSINESS_VIEW__MODEL) return null;
		return (BusinessModel)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModel(BusinessModel newModel, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModel, BusinessModelPackage.BUSINESS_VIEW__MODEL, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(BusinessModel newModel) {
		if (newModel != eInternalContainer() || (eContainerFeatureID() != BusinessModelPackage.BUSINESS_VIEW__MODEL && newModel != null)) {
			if (EcoreUtil.isAncestor(this, newModel))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModel != null)
				msgs = ((InternalEObject)newModel).eInverseAdd(this, BusinessModelPackage.BUSINESS_MODEL__VIEWS, BusinessModel.class, msgs);
			msgs = basicSetModel(newModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_VIEW__MODEL, newModel, newModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessColumn> getColumns() {
		if (columns == null) {
			columns = new EObjectResolvingEList<BusinessColumn>(BusinessColumn.class, this, BusinessModelPackage.BUSINESS_VIEW__COLUMNS);
		}
		return columns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessRelationship> getJoinRelationships() {
		if (joinRelationships == null) {
			joinRelationships = new EObjectResolvingEList<BusinessRelationship>(BusinessRelationship.class, this, BusinessModelPackage.BUSINESS_VIEW__JOIN_RELATIONSHIPS);
		}
		return joinRelationships;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BusinessModelPackage.BUSINESS_VIEW__MODEL:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModel((BusinessModel)otherEnd, msgs);
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
			case BusinessModelPackage.BUSINESS_VIEW__MODEL:
				return basicSetModel(null, msgs);
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
		switch (eContainerFeatureID()) {
			case BusinessModelPackage.BUSINESS_VIEW__MODEL:
				return eInternalContainer().eInverseRemove(this, BusinessModelPackage.BUSINESS_MODEL__VIEWS, BusinessModel.class, msgs);
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
			case BusinessModelPackage.BUSINESS_VIEW__MODEL:
				return getModel();
			case BusinessModelPackage.BUSINESS_VIEW__COLUMNS:
				return getColumns();
			case BusinessModelPackage.BUSINESS_VIEW__JOIN_RELATIONSHIPS:
				return getJoinRelationships();
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
			case BusinessModelPackage.BUSINESS_VIEW__MODEL:
				setModel((BusinessModel)newValue);
				return;
			case BusinessModelPackage.BUSINESS_VIEW__COLUMNS:
				getColumns().clear();
				getColumns().addAll((Collection<? extends BusinessColumn>)newValue);
				return;
			case BusinessModelPackage.BUSINESS_VIEW__JOIN_RELATIONSHIPS:
				getJoinRelationships().clear();
				getJoinRelationships().addAll((Collection<? extends BusinessRelationship>)newValue);
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
			case BusinessModelPackage.BUSINESS_VIEW__MODEL:
				setModel((BusinessModel)null);
				return;
			case BusinessModelPackage.BUSINESS_VIEW__COLUMNS:
				getColumns().clear();
				return;
			case BusinessModelPackage.BUSINESS_VIEW__JOIN_RELATIONSHIPS:
				getJoinRelationships().clear();
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
			case BusinessModelPackage.BUSINESS_VIEW__MODEL:
				return getModel() != null;
			case BusinessModelPackage.BUSINESS_VIEW__COLUMNS:
				return columns != null && !columns.isEmpty();
			case BusinessModelPackage.BUSINESS_VIEW__JOIN_RELATIONSHIPS:
				return joinRelationships != null && !joinRelationships.isEmpty();
		}
		return super.eIsSet(featureID);
	}
	
	// =========================================================================
	// Utility methods
	// =========================================================================
	
	@Override
	public EList<ModelPropertyType> getPropertyTypes() {
		return null;
	}

} //BusinessViewImpl
