/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.model.analytical.impl;

import it.eng.spagobi.meta.model.analytical.*;
import it.eng.spagobi.meta.model.analytical.AnalyticalModel;
import it.eng.spagobi.meta.model.analytical.AnalyticalModelFactory;
import it.eng.spagobi.meta.model.analytical.AnalyticalModelPackage;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalyticalModelFactoryImpl extends EFactoryImpl implements AnalyticalModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AnalyticalModelFactory init() {
		try {
			AnalyticalModelFactory theAnalyticalModelFactory = (AnalyticalModelFactory)EPackage.Registry.INSTANCE.getEFactory("http:///it/eng/spagobi/meta/model/analytical.ecore"); 
			if (theAnalyticalModelFactory != null) {
				return theAnalyticalModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AnalyticalModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticalModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case AnalyticalModelPackage.ANALYTICAL_MODEL: return createAnalyticalModel();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticalModel createAnalyticalModel() {
		AnalyticalModelImpl analyticalModel = new AnalyticalModelImpl();
		return analyticalModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalyticalModelPackage getAnalyticalModelPackage() {
		return (AnalyticalModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AnalyticalModelPackage getPackage() {
		return AnalyticalModelPackage.eINSTANCE;
	}

} //AnalyticalModelFactoryImpl
