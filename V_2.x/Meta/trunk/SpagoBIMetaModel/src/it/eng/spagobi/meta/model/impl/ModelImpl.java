/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.impl;

import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.ModelPropertyType;

import it.eng.spagobi.meta.model.business.BusinessModel;

import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import it.eng.spagobi.meta.model.physical.PhysicalModelPackage;
import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelImpl#getPhysicalModels <em>Physical Models</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelImpl#getBusinessModels <em>Business Models</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelImpl#getPropertyTypes <em>Property Types</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelImpl extends ModelObjectImpl implements Model {
	/**
	 * The cached value of the '{@link #getPhysicalModels() <em>Physical Models</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalModels()
	 * @generated
	 * @ordered
	 */
	protected EList<PhysicalModel> physicalModels;

	/**
	 * The cached value of the '{@link #getBusinessModels() <em>Business Models</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBusinessModels()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessModel> businessModels;

	/**
	 * The cached value of the '{@link #getPropertyTypes() <em>Property Types</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPropertyTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<ModelPropertyType> propertyTypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalModel> getPhysicalModels() {
		if (physicalModels == null) {
			physicalModels = new EObjectWithInverseResolvingEList<PhysicalModel>(PhysicalModel.class, this, ModelPackage.MODEL__PHYSICAL_MODELS, PhysicalModelPackage.PHYSICAL_MODEL__PARENT_MODEL);
		}
		return physicalModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessModel> getBusinessModels() {
		if (businessModels == null) {
			businessModels = new EObjectWithInverseResolvingEList<BusinessModel>(BusinessModel.class, this, ModelPackage.MODEL__BUSINESS_MODELS, BusinessModelPackage.BUSINESS_MODEL__PARENT_MODEL);
		}
		return businessModels;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<ModelPropertyType> getPropertyTypes() {
		if (propertyTypes == null) {
			propertyTypes = new EObjectResolvingEList<ModelPropertyType>(ModelPropertyType.class, this, ModelPackage.MODEL__PROPERTY_TYPES);
		}
		return propertyTypes;
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
			case ModelPackage.MODEL__PHYSICAL_MODELS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getPhysicalModels()).basicAdd(otherEnd, msgs);
			case ModelPackage.MODEL__BUSINESS_MODELS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getBusinessModels()).basicAdd(otherEnd, msgs);
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
			case ModelPackage.MODEL__PHYSICAL_MODELS:
				return ((InternalEList<?>)getPhysicalModels()).basicRemove(otherEnd, msgs);
			case ModelPackage.MODEL__BUSINESS_MODELS:
				return ((InternalEList<?>)getBusinessModels()).basicRemove(otherEnd, msgs);
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
			case ModelPackage.MODEL__PHYSICAL_MODELS:
				return getPhysicalModels();
			case ModelPackage.MODEL__BUSINESS_MODELS:
				return getBusinessModels();
			case ModelPackage.MODEL__PROPERTY_TYPES:
				return getPropertyTypes();
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
			case ModelPackage.MODEL__PHYSICAL_MODELS:
				getPhysicalModels().clear();
				getPhysicalModels().addAll((Collection<? extends PhysicalModel>)newValue);
				return;
			case ModelPackage.MODEL__BUSINESS_MODELS:
				getBusinessModels().clear();
				getBusinessModels().addAll((Collection<? extends BusinessModel>)newValue);
				return;
			case ModelPackage.MODEL__PROPERTY_TYPES:
				getPropertyTypes().clear();
				getPropertyTypes().addAll((Collection<? extends ModelPropertyType>)newValue);
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
			case ModelPackage.MODEL__PHYSICAL_MODELS:
				getPhysicalModels().clear();
				return;
			case ModelPackage.MODEL__BUSINESS_MODELS:
				getBusinessModels().clear();
				return;
			case ModelPackage.MODEL__PROPERTY_TYPES:
				getPropertyTypes().clear();
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
			case ModelPackage.MODEL__PHYSICAL_MODELS:
				return physicalModels != null && !physicalModels.isEmpty();
			case ModelPackage.MODEL__BUSINESS_MODELS:
				return businessModels != null && !businessModels.isEmpty();
			case ModelPackage.MODEL__PROPERTY_TYPES:
				return propertyTypes != null && !propertyTypes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	@Override
	public ModelPropertyType getPropertyType(String name) {
		for(int i = 0; i < getPropertyTypes().size(); i++) {
			if(getPropertyTypes().get(i).getName().equals(name)) {
				return getPropertyTypes().get(i);
			}
		}
		return null;
	}

} //ModelImpl
