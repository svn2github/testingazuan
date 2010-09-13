/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.impl;

import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.ModelPropertyCategory;
import it.eng.spagobi.meta.model.ModelPropertyType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelPropertyTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelPropertyTypeImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelPropertyTypeImpl#getCategory <em>Category</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelPropertyTypeImpl#getDefaultValue <em>Default Value</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.impl.ModelPropertyTypeImpl#getAdmissibleValues <em>Admissible Values</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ModelPropertyTypeImpl extends EObjectImpl implements ModelPropertyType {
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
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCategory() <em>Category</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCategory()
	 * @generated
	 * @ordered
	 */
	protected ModelPropertyCategory category;

	/**
	 * The default value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected static final String DEFAULT_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected String defaultValue = DEFAULT_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getAdmissibleValues() <em>Admissible Values</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdmissibleValues()
	 * @generated
	 * @ordered
	 */
	protected static final String ADMISSIBLE_VALUES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAdmissibleValues() <em>Admissible Values</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAdmissibleValues()
	 * @generated
	 * @ordered
	 */
	protected String admissibleValues = ADMISSIBLE_VALUES_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelPropertyTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.MODEL_PROPERTY_TYPE;
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
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MODEL_PROPERTY_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MODEL_PROPERTY_TYPE__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelPropertyCategory getCategory() {
		if (category != null && category.eIsProxy()) {
			InternalEObject oldCategory = (InternalEObject)category;
			category = (ModelPropertyCategory)eResolveProxy(oldCategory);
			if (category != oldCategory) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY, oldCategory, category));
			}
		}
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ModelPropertyCategory basicGetCategory() {
		return category;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCategory(ModelPropertyCategory newCategory, NotificationChain msgs) {
		ModelPropertyCategory oldCategory = category;
		category = newCategory;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY, oldCategory, newCategory);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCategory(ModelPropertyCategory newCategory) {
		if (newCategory != category) {
			NotificationChain msgs = null;
			if (category != null)
				msgs = ((InternalEObject)category).eInverseRemove(this, ModelPackage.MODEL_PROPERTY_CATEGORY__PROPERTY_TYPES, ModelPropertyCategory.class, msgs);
			if (newCategory != null)
				msgs = ((InternalEObject)newCategory).eInverseAdd(this, ModelPackage.MODEL_PROPERTY_CATEGORY__PROPERTY_TYPES, ModelPropertyCategory.class, msgs);
			msgs = basicSetCategory(newCategory, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY, newCategory, newCategory));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDefaultValue(String newDefaultValue) {
		String oldDefaultValue = defaultValue;
		defaultValue = newDefaultValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MODEL_PROPERTY_TYPE__DEFAULT_VALUE, oldDefaultValue, defaultValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getAdmissibleValues() {
		return admissibleValues;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setAdmissibleValues(String newAdmissibleValues) {
		String oldAdmissibleValues = admissibleValues;
		admissibleValues = newAdmissibleValues;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.MODEL_PROPERTY_TYPE__ADMISSIBLE_VALUES, oldAdmissibleValues, admissibleValues));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY:
				if (category != null)
					msgs = ((InternalEObject)category).eInverseRemove(this, ModelPackage.MODEL_PROPERTY_CATEGORY__PROPERTY_TYPES, ModelPropertyCategory.class, msgs);
				return basicSetCategory((ModelPropertyCategory)otherEnd, msgs);
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
			case ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY:
				return basicSetCategory(null, msgs);
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
			case ModelPackage.MODEL_PROPERTY_TYPE__NAME:
				return getName();
			case ModelPackage.MODEL_PROPERTY_TYPE__DESCRIPTION:
				return getDescription();
			case ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY:
				if (resolve) return getCategory();
				return basicGetCategory();
			case ModelPackage.MODEL_PROPERTY_TYPE__DEFAULT_VALUE:
				return getDefaultValue();
			case ModelPackage.MODEL_PROPERTY_TYPE__ADMISSIBLE_VALUES:
				return getAdmissibleValues();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ModelPackage.MODEL_PROPERTY_TYPE__NAME:
				setName((String)newValue);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY:
				setCategory((ModelPropertyCategory)newValue);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__DEFAULT_VALUE:
				setDefaultValue((String)newValue);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__ADMISSIBLE_VALUES:
				setAdmissibleValues((String)newValue);
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
			case ModelPackage.MODEL_PROPERTY_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY:
				setCategory((ModelPropertyCategory)null);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__DEFAULT_VALUE:
				setDefaultValue(DEFAULT_VALUE_EDEFAULT);
				return;
			case ModelPackage.MODEL_PROPERTY_TYPE__ADMISSIBLE_VALUES:
				setAdmissibleValues(ADMISSIBLE_VALUES_EDEFAULT);
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
			case ModelPackage.MODEL_PROPERTY_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ModelPackage.MODEL_PROPERTY_TYPE__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case ModelPackage.MODEL_PROPERTY_TYPE__CATEGORY:
				return category != null;
			case ModelPackage.MODEL_PROPERTY_TYPE__DEFAULT_VALUE:
				return DEFAULT_VALUE_EDEFAULT == null ? defaultValue != null : !DEFAULT_VALUE_EDEFAULT.equals(defaultValue);
			case ModelPackage.MODEL_PROPERTY_TYPE__ADMISSIBLE_VALUES:
				return ADMISSIBLE_VALUES_EDEFAULT == null ? admissibleValues != null : !ADMISSIBLE_VALUES_EDEFAULT.equals(admissibleValues);
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
		result.append(", description: ");
		result.append(description);
		result.append(", defaultValue: ");
		result.append(defaultValue);
		result.append(", admissibleValues: ");
		result.append(admissibleValues);
		result.append(')');
		return result.toString();
	}

} //ModelPropertyTypeImpl
