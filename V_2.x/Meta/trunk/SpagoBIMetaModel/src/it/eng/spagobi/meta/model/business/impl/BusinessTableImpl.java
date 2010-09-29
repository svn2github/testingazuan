/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.impl;

import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessIdentifier;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessTable;

import it.eng.spagobi.meta.model.impl.ModelObjectImpl;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Business Table</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessTableImpl#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessTableImpl#getPhysicalTable <em>Physical Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessTableImpl#getColumns <em>Columns</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BusinessTableImpl extends ModelObjectImpl implements BusinessTable {
	/**
	 * The cached value of the '{@link #getPhysicalTable() <em>Physical Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPhysicalTable()
	 * @generated
	 * @ordered
	 */
	protected PhysicalTable physicalTable;

	/**
	 * The cached value of the '{@link #getColumns() <em>Columns</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessColumn> columns;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BusinessTableImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BusinessModelPackage.Literals.BUSINESS_TABLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessModel getModel() {
		if (eContainerFeatureID() != BusinessModelPackage.BUSINESS_TABLE__MODEL) return null;
		return (BusinessModel)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModel(BusinessModel newModel, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModel, BusinessModelPackage.BUSINESS_TABLE__MODEL, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(BusinessModel newModel) {
		if (newModel != eInternalContainer() || (eContainerFeatureID() != BusinessModelPackage.BUSINESS_TABLE__MODEL && newModel != null)) {
			if (EcoreUtil.isAncestor(this, newModel))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModel != null)
				msgs = ((InternalEObject)newModel).eInverseAdd(this, BusinessModelPackage.BUSINESS_MODEL__TABLES, BusinessModel.class, msgs);
			msgs = basicSetModel(newModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_TABLE__MODEL, newModel, newModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable getPhysicalTable() {
		if (physicalTable != null && physicalTable.eIsProxy()) {
			InternalEObject oldPhysicalTable = (InternalEObject)physicalTable;
			physicalTable = (PhysicalTable)eResolveProxy(oldPhysicalTable);
			if (physicalTable != oldPhysicalTable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BusinessModelPackage.BUSINESS_TABLE__PHYSICAL_TABLE, oldPhysicalTable, physicalTable));
			}
		}
		return physicalTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PhysicalTable basicGetPhysicalTable() {
		return physicalTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPhysicalTable(PhysicalTable newPhysicalTable) {
		PhysicalTable oldPhysicalTable = physicalTable;
		physicalTable = newPhysicalTable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_TABLE__PHYSICAL_TABLE, oldPhysicalTable, physicalTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessColumn> getColumns() {
		if (columns == null) {
			columns = new EObjectContainmentWithInverseEList<BusinessColumn>(BusinessColumn.class, this, BusinessModelPackage.BUSINESS_TABLE__COLUMNS, BusinessModelPackage.BUSINESS_COLUMN__TABLE);
		}
		return columns;
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
			case BusinessModelPackage.BUSINESS_TABLE__MODEL:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModel((BusinessModel)otherEnd, msgs);
			case BusinessModelPackage.BUSINESS_TABLE__COLUMNS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getColumns()).basicAdd(otherEnd, msgs);
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
			case BusinessModelPackage.BUSINESS_TABLE__MODEL:
				return basicSetModel(null, msgs);
			case BusinessModelPackage.BUSINESS_TABLE__COLUMNS:
				return ((InternalEList<?>)getColumns()).basicRemove(otherEnd, msgs);
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
			case BusinessModelPackage.BUSINESS_TABLE__MODEL:
				return eInternalContainer().eInverseRemove(this, BusinessModelPackage.BUSINESS_MODEL__TABLES, BusinessModel.class, msgs);
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
			case BusinessModelPackage.BUSINESS_TABLE__MODEL:
				return getModel();
			case BusinessModelPackage.BUSINESS_TABLE__PHYSICAL_TABLE:
				if (resolve) return getPhysicalTable();
				return basicGetPhysicalTable();
			case BusinessModelPackage.BUSINESS_TABLE__COLUMNS:
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
			case BusinessModelPackage.BUSINESS_TABLE__MODEL:
				setModel((BusinessModel)newValue);
				return;
			case BusinessModelPackage.BUSINESS_TABLE__PHYSICAL_TABLE:
				setPhysicalTable((PhysicalTable)newValue);
				return;
			case BusinessModelPackage.BUSINESS_TABLE__COLUMNS:
				getColumns().clear();
				getColumns().addAll((Collection<? extends BusinessColumn>)newValue);
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
			case BusinessModelPackage.BUSINESS_TABLE__MODEL:
				setModel((BusinessModel)null);
				return;
			case BusinessModelPackage.BUSINESS_TABLE__PHYSICAL_TABLE:
				setPhysicalTable((PhysicalTable)null);
				return;
			case BusinessModelPackage.BUSINESS_TABLE__COLUMNS:
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
			case BusinessModelPackage.BUSINESS_TABLE__MODEL:
				return getModel() != null;
			case BusinessModelPackage.BUSINESS_TABLE__PHYSICAL_TABLE:
				return physicalTable != null;
			case BusinessModelPackage.BUSINESS_TABLE__COLUMNS:
				return columns != null && !columns.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	// =========================================================================
	// Utility methods
	// =========================================================================
	
	@Override
	public BusinessIdentifier getIdentifier() {
		return (getModel() != null)? getModel().getIdentifier(this): null;
	}
	
	@Override
	public BusinessColumn getColumn(String name) {
		for(int i = 0; i < getColumns().size(); i++) {
			if(getColumns().get(i).getName().equals(name)) {
				return getColumns().get(i);
			}
		}
		return null;
	}

	@Override
	public BusinessColumn getColumn(PhysicalColumn physicalColumn) {
		for(int i = 0; i < getColumns().size(); i++) {
			if(getColumns().get(i).getPhysicalColumn().equals(physicalColumn)) {
				return getColumns().get(i);
			} 
		}
		return null;
	}
	
	@Override
	public EList<ModelPropertyType> getPropertyTypes() {
		return getModel().getParentModel().getPropertyTypes();
	}

} //BusinessTableImpl
