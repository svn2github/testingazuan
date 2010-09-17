/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.impl;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

import it.eng.spagobi.meta.model.impl.ModelObjectImpl;
import java.util.Collection;
import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Business Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getModel <em>Model</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getSourceTable <em>Source Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getDestinationTable <em>Destination Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getSourceColumns <em>Source Columns</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getDestinationColumns <em>Destination Columns</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BusinessRelationshipImpl extends ModelObjectImpl implements BusinessRelationship {
	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected BusinessModel model;

	/**
	 * The cached value of the '{@link #getSourceTable() <em>Source Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceTable()
	 * @generated
	 * @ordered
	 */
	protected BusinessTable sourceTable;

	/**
	 * The cached value of the '{@link #getDestinationTable() <em>Destination Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationTable()
	 * @generated
	 * @ordered
	 */
	protected BusinessTable destinationTable;

	/**
	 * The cached value of the '{@link #getSourceColumns() <em>Source Columns</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessColumn> sourceColumns;

	/**
	 * The cached value of the '{@link #getDestinationColumns() <em>Destination Columns</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDestinationColumns()
	 * @generated
	 * @ordered
	 */
	protected EList<BusinessColumn> destinationColumns;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BusinessRelationshipImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return BusinessModelPackage.Literals.BUSINESS_RELATIONSHIP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessModel getModel() {
		if (model != null && model.eIsProxy()) {
			InternalEObject oldModel = (InternalEObject)model;
			model = (BusinessModel)eResolveProxy(oldModel);
			if (model != oldModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL, oldModel, model));
			}
		}
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessModel basicGetModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModel(BusinessModel newModel, NotificationChain msgs) {
		BusinessModel oldModel = model;
		model = newModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL, oldModel, newModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(BusinessModel newModel) {
		if (newModel != model) {
			NotificationChain msgs = null;
			if (model != null)
				msgs = ((InternalEObject)model).eInverseRemove(this, BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS, BusinessModel.class, msgs);
			if (newModel != null)
				msgs = ((InternalEObject)newModel).eInverseAdd(this, BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS, BusinessModel.class, msgs);
			msgs = basicSetModel(newModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL, newModel, newModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessTable getSourceTable() {
		if (sourceTable != null && sourceTable.eIsProxy()) {
			InternalEObject oldSourceTable = (InternalEObject)sourceTable;
			sourceTable = (BusinessTable)eResolveProxy(oldSourceTable);
			if (sourceTable != oldSourceTable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE, oldSourceTable, sourceTable));
			}
		}
		return sourceTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessTable basicGetSourceTable() {
		return sourceTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSourceTable(BusinessTable newSourceTable) {
		BusinessTable oldSourceTable = sourceTable;
		sourceTable = newSourceTable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE, oldSourceTable, sourceTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessTable getDestinationTable() {
		if (destinationTable != null && destinationTable.eIsProxy()) {
			InternalEObject oldDestinationTable = (InternalEObject)destinationTable;
			destinationTable = (BusinessTable)eResolveProxy(oldDestinationTable);
			if (destinationTable != oldDestinationTable) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE, oldDestinationTable, destinationTable));
			}
		}
		return destinationTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public BusinessTable basicGetDestinationTable() {
		return destinationTable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDestinationTable(BusinessTable newDestinationTable) {
		BusinessTable oldDestinationTable = destinationTable;
		destinationTable = newDestinationTable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE, oldDestinationTable, destinationTable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessColumn> getSourceColumns() {
		if (sourceColumns == null) {
			sourceColumns = new EObjectResolvingEList<BusinessColumn>(BusinessColumn.class, this, BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS);
		}
		return sourceColumns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<BusinessColumn> getDestinationColumns() {
		if (destinationColumns == null) {
			destinationColumns = new EObjectResolvingEList<BusinessColumn>(BusinessColumn.class, this, BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS);
		}
		return destinationColumns;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL:
				if (model != null)
					msgs = ((InternalEObject)model).eInverseRemove(this, BusinessModelPackage.BUSINESS_MODEL__RELATIONSHIPS, BusinessModel.class, msgs);
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
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL:
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
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL:
				if (resolve) return getModel();
				return basicGetModel();
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				if (resolve) return getSourceTable();
				return basicGetSourceTable();
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				if (resolve) return getDestinationTable();
				return basicGetDestinationTable();
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS:
				return getSourceColumns();
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS:
				return getDestinationColumns();
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
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL:
				setModel((BusinessModel)newValue);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				setSourceTable((BusinessTable)newValue);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				setDestinationTable((BusinessTable)newValue);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS:
				getSourceColumns().clear();
				getSourceColumns().addAll((Collection<? extends BusinessColumn>)newValue);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS:
				getDestinationColumns().clear();
				getDestinationColumns().addAll((Collection<? extends BusinessColumn>)newValue);
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
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL:
				setModel((BusinessModel)null);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				setSourceTable((BusinessTable)null);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				setDestinationTable((BusinessTable)null);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS:
				getSourceColumns().clear();
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS:
				getDestinationColumns().clear();
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
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__MODEL:
				return model != null;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				return sourceTable != null;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				return destinationTable != null;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_COLUMNS:
				return sourceColumns != null && !sourceColumns.isEmpty();
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_COLUMNS:
				return destinationColumns != null && !destinationColumns.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //BusinessRelationshipImpl
