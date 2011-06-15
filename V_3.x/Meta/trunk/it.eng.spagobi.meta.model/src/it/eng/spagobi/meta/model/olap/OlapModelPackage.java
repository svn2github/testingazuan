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
package it.eng.spagobi.meta.model.olap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see it.eng.spagobi.meta.model.olap.OlapModelFactory
 * @model kind="package"
 * @generated
 */
public interface OlapModelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "olap";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http:///it/eng/spagobi/meta/model/olapl.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "it.eng.spagobi.meta.model.olap";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OlapModelPackage eINSTANCE = it.eng.spagobi.meta.model.olap.impl.OlapModelPackageImpl.init();

	/**
	 * The meta object id for the '{@link it.eng.spagobi.meta.model.olap.impl.OlapModelImpl <em>Olap Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see it.eng.spagobi.meta.model.olap.impl.OlapModelImpl
	 * @see it.eng.spagobi.meta.model.olap.impl.OlapModelPackageImpl#getOlapModel()
	 * @generated
	 */
	int OLAP_MODEL = 0;

	/**
	 * The number of structural features of the '<em>Olap Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OLAP_MODEL_FEATURE_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link it.eng.spagobi.meta.model.olap.OlapModel <em>Olap Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Olap Model</em>'.
	 * @see it.eng.spagobi.meta.model.olap.OlapModel
	 * @generated
	 */
	EClass getOlapModel();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	OlapModelFactory getOlapModelFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link it.eng.spagobi.meta.model.olap.impl.OlapModelImpl <em>Olap Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see it.eng.spagobi.meta.model.olap.impl.OlapModelImpl
		 * @see it.eng.spagobi.meta.model.olap.impl.OlapModelPackageImpl#getOlapModel()
		 * @generated
		 */
		EClass OLAP_MODEL = eINSTANCE.getOlapModel();

	}

} //OlapModelPackage
