/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package it.eng.spagobi.meta.model.business.impl;

import it.eng.spagobi.meta.model.business.BusinessModelPackage;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Business Relationship</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getName <em>Name</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getSourceTable <em>Source Table</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.business.impl.BusinessRelationshipImpl#getDestinationTable <em>Destination Table</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BusinessRelationshipImpl extends EObjectImpl implements BusinessRelationship {
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
			eNotify(new ENotificationImpl(this, Notification.SET, BusinessModelPackage.BUSINESS_RELATIONSHIP__NAME, oldName, name));
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
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__NAME:
				return getName();
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				if (resolve) return getSourceTable();
				return basicGetSourceTable();
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				if (resolve) return getDestinationTable();
				return basicGetDestinationTable();
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
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__NAME:
				setName((String)newValue);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				setSourceTable((BusinessTable)newValue);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				setDestinationTable((BusinessTable)newValue);
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
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__NAME:
				setName(NAME_EDEFAULT);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				setSourceTable((BusinessTable)null);
				return;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				setDestinationTable((BusinessTable)null);
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
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__SOURCE_TABLE:
				return sourceTable != null;
			case BusinessModelPackage.BUSINESS_RELATIONSHIP__DESTINATION_TABLE:
				return destinationTable != null;
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

} //BusinessRelationshipImpl
