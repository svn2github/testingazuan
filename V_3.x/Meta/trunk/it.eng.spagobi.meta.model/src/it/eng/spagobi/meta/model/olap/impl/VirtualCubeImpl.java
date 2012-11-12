/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.olap.impl;

import it.eng.spagobi.meta.model.ModelPropertyType;
import it.eng.spagobi.meta.model.impl.ModelObjectImpl;

import it.eng.spagobi.meta.model.olap.CalculatedMember;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.OlapModelPackage;
import it.eng.spagobi.meta.model.olap.VirtualCube;
import it.eng.spagobi.meta.model.olap.VirtualCubeDimension;
import it.eng.spagobi.meta.model.olap.VirtualCubeMeasure;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Virtual Cube</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.olap.impl.VirtualCubeImpl#getCubes <em>Cubes</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.olap.impl.VirtualCubeImpl#getDimensions <em>Dimensions</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.olap.impl.VirtualCubeImpl#getMeasures <em>Measures</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.olap.impl.VirtualCubeImpl#getCalculatedMembers <em>Calculated Members</em>}</li>
 *   <li>{@link it.eng.spagobi.meta.model.olap.impl.VirtualCubeImpl#getModel <em>Model</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class VirtualCubeImpl extends ModelObjectImpl implements VirtualCube {
	/**
	 * The cached value of the '{@link #getCubes() <em>Cubes</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCubes()
	 * @generated
	 * @ordered
	 */
	protected EList<Cube> cubes;

	/**
	 * The cached value of the '{@link #getDimensions() <em>Dimensions</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDimensions()
	 * @generated
	 * @ordered
	 */
	protected VirtualCubeDimension dimensions;

	/**
	 * The cached value of the '{@link #getMeasures() <em>Measures</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMeasures()
	 * @generated
	 * @ordered
	 */
	protected EList<VirtualCubeMeasure> measures;

	/**
	 * The cached value of the '{@link #getCalculatedMembers() <em>Calculated Members</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCalculatedMembers()
	 * @generated
	 * @ordered
	 */
	protected EList<CalculatedMember> calculatedMembers;

	/**
	 * The cached value of the '{@link #getModel() <em>Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModel()
	 * @generated
	 * @ordered
	 */
	protected OlapModel model;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected VirtualCubeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OlapModelPackage.Literals.VIRTUAL_CUBE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Cube> getCubes() {
		if (cubes == null) {
			cubes = new EObjectResolvingEList<Cube>(Cube.class, this, OlapModelPackage.VIRTUAL_CUBE__CUBES);
		}
		return cubes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VirtualCubeDimension getDimensions() {
		if (dimensions != null && dimensions.eIsProxy()) {
			InternalEObject oldDimensions = (InternalEObject)dimensions;
			dimensions = (VirtualCubeDimension)eResolveProxy(oldDimensions);
			if (dimensions != oldDimensions) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS, oldDimensions, dimensions));
			}
		}
		return dimensions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VirtualCubeDimension basicGetDimensions() {
		return dimensions;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDimensions(VirtualCubeDimension newDimensions, NotificationChain msgs) {
		VirtualCubeDimension oldDimensions = dimensions;
		dimensions = newDimensions;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS, oldDimensions, newDimensions);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDimensions(VirtualCubeDimension newDimensions) {
		if (newDimensions != dimensions) {
			NotificationChain msgs = null;
			if (dimensions != null)
				msgs = ((InternalEObject)dimensions).eInverseRemove(this, OlapModelPackage.VIRTUAL_CUBE_DIMENSION__VIRTUAL_CUBE, VirtualCubeDimension.class, msgs);
			if (newDimensions != null)
				msgs = ((InternalEObject)newDimensions).eInverseAdd(this, OlapModelPackage.VIRTUAL_CUBE_DIMENSION__VIRTUAL_CUBE, VirtualCubeDimension.class, msgs);
			msgs = basicSetDimensions(newDimensions, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS, newDimensions, newDimensions));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<VirtualCubeMeasure> getMeasures() {
		if (measures == null) {
			measures = new EObjectWithInverseResolvingEList<VirtualCubeMeasure>(VirtualCubeMeasure.class, this, OlapModelPackage.VIRTUAL_CUBE__MEASURES, OlapModelPackage.VIRTUAL_CUBE_MEASURE__VIRTUAL_CUBE);
		}
		return measures;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<CalculatedMember> getCalculatedMembers() {
		if (calculatedMembers == null) {
			calculatedMembers = new EObjectResolvingEList<CalculatedMember>(CalculatedMember.class, this, OlapModelPackage.VIRTUAL_CUBE__CALCULATED_MEMBERS);
		}
		return calculatedMembers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OlapModel getModel() {
		if (model != null && model.eIsProxy()) {
			InternalEObject oldModel = (InternalEObject)model;
			model = (OlapModel)eResolveProxy(oldModel);
			if (model != oldModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OlapModelPackage.VIRTUAL_CUBE__MODEL, oldModel, model));
			}
		}
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OlapModel basicGetModel() {
		return model;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModel(OlapModel newModel, NotificationChain msgs) {
		OlapModel oldModel = model;
		model = newModel;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OlapModelPackage.VIRTUAL_CUBE__MODEL, oldModel, newModel);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setModel(OlapModel newModel) {
		if (newModel != model) {
			NotificationChain msgs = null;
			if (model != null)
				msgs = ((InternalEObject)model).eInverseRemove(this, OlapModelPackage.OLAP_MODEL__VIRTUAL_CUBES, OlapModel.class, msgs);
			if (newModel != null)
				msgs = ((InternalEObject)newModel).eInverseAdd(this, OlapModelPackage.OLAP_MODEL__VIRTUAL_CUBES, OlapModel.class, msgs);
			msgs = basicSetModel(newModel, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OlapModelPackage.VIRTUAL_CUBE__MODEL, newModel, newModel));
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
			case OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS:
				if (dimensions != null)
					msgs = ((InternalEObject)dimensions).eInverseRemove(this, OlapModelPackage.VIRTUAL_CUBE_DIMENSION__VIRTUAL_CUBE, VirtualCubeDimension.class, msgs);
				return basicSetDimensions((VirtualCubeDimension)otherEnd, msgs);
			case OlapModelPackage.VIRTUAL_CUBE__MEASURES:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getMeasures()).basicAdd(otherEnd, msgs);
			case OlapModelPackage.VIRTUAL_CUBE__MODEL:
				if (model != null)
					msgs = ((InternalEObject)model).eInverseRemove(this, OlapModelPackage.OLAP_MODEL__VIRTUAL_CUBES, OlapModel.class, msgs);
				return basicSetModel((OlapModel)otherEnd, msgs);
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
			case OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS:
				return basicSetDimensions(null, msgs);
			case OlapModelPackage.VIRTUAL_CUBE__MEASURES:
				return ((InternalEList<?>)getMeasures()).basicRemove(otherEnd, msgs);
			case OlapModelPackage.VIRTUAL_CUBE__MODEL:
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
			case OlapModelPackage.VIRTUAL_CUBE__CUBES:
				return getCubes();
			case OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS:
				if (resolve) return getDimensions();
				return basicGetDimensions();
			case OlapModelPackage.VIRTUAL_CUBE__MEASURES:
				return getMeasures();
			case OlapModelPackage.VIRTUAL_CUBE__CALCULATED_MEMBERS:
				return getCalculatedMembers();
			case OlapModelPackage.VIRTUAL_CUBE__MODEL:
				if (resolve) return getModel();
				return basicGetModel();
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
			case OlapModelPackage.VIRTUAL_CUBE__CUBES:
				getCubes().clear();
				getCubes().addAll((Collection<? extends Cube>)newValue);
				return;
			case OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS:
				setDimensions((VirtualCubeDimension)newValue);
				return;
			case OlapModelPackage.VIRTUAL_CUBE__MEASURES:
				getMeasures().clear();
				getMeasures().addAll((Collection<? extends VirtualCubeMeasure>)newValue);
				return;
			case OlapModelPackage.VIRTUAL_CUBE__CALCULATED_MEMBERS:
				getCalculatedMembers().clear();
				getCalculatedMembers().addAll((Collection<? extends CalculatedMember>)newValue);
				return;
			case OlapModelPackage.VIRTUAL_CUBE__MODEL:
				setModel((OlapModel)newValue);
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
			case OlapModelPackage.VIRTUAL_CUBE__CUBES:
				getCubes().clear();
				return;
			case OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS:
				setDimensions((VirtualCubeDimension)null);
				return;
			case OlapModelPackage.VIRTUAL_CUBE__MEASURES:
				getMeasures().clear();
				return;
			case OlapModelPackage.VIRTUAL_CUBE__CALCULATED_MEMBERS:
				getCalculatedMembers().clear();
				return;
			case OlapModelPackage.VIRTUAL_CUBE__MODEL:
				setModel((OlapModel)null);
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
			case OlapModelPackage.VIRTUAL_CUBE__CUBES:
				return cubes != null && !cubes.isEmpty();
			case OlapModelPackage.VIRTUAL_CUBE__DIMENSIONS:
				return dimensions != null;
			case OlapModelPackage.VIRTUAL_CUBE__MEASURES:
				return measures != null && !measures.isEmpty();
			case OlapModelPackage.VIRTUAL_CUBE__CALCULATED_MEMBERS:
				return calculatedMembers != null && !calculatedMembers.isEmpty();
			case OlapModelPackage.VIRTUAL_CUBE__MODEL:
				return model != null;
		}
		return super.eIsSet(featureID);
	}

	/* (non-Javadoc)
	 * @see it.eng.spagobi.meta.model.ModelObject#getPropertyTypes()
	 */
	@Override
	public EList<ModelPropertyType> getPropertyTypes() {
		return getModel().getParentModel().getPropertyTypes();

	}

} //VirtualCubeImpl
