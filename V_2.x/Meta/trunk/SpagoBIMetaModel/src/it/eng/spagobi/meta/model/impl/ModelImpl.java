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

import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelImpl#getPhysicalModels <em>Physical Models</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelImpl#getBusinessModels <em>Business Models</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelImpl#getPropertyTypes <em>Property Types</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelImpl extends EObjectImpl implements Model {
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MODEL__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<PhysicalModel> getPhysicalModels() {
		if (physicalModels == null) {
			physicalModels = new EObjectResolvingEList<PhysicalModel>(PhysicalModel.class, this, ModelPackage.MODEL__PHYSICAL_MODELS);
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
			businessModels = new EObjectResolvingEList<BusinessModel>(BusinessModel.class, this, ModelPackage.MODEL__BUSINESS_MODELS);
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ModelPackage.MODEL__NAME:
				return getName();
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
			case ModelPackage.MODEL__NAME:
				setName((String)newValue);
				return;
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
			case ModelPackage.MODEL__NAME:
				setName(NAME_EDEFAULT);
				return;
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
			case ModelPackage.MODEL__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.MODEL__PHYSICAL_MODELS:
				return physicalModels != null && !physicalModels.isEmpty();
			case ModelPackage.MODEL__BUSINESS_MODELS:
				return businessModels != null && !businessModels.isEmpty();
			case ModelPackage.MODEL__PROPERTY_TYPES:
				return propertyTypes != null && !propertyTypes.isEmpty();
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

} //ModelImpl
