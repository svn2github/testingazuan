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
package it.eng.spagobi.meta.model.physical;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see it.eng.spagobi.meta.model.physical.PhysicalModelPackage
 * @generated
 */
public interface PhysicalModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	PhysicalModelFactory eINSTANCE = it.eng.spagobi.meta.model.physical.impl.PhysicalModelFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Physical Column</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Physical Column</em>'.
	 * @generated
	 */
	PhysicalColumn createPhysicalColumn();

	/**
	 * Returns a new object of class '<em>Physical Foreign Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Physical Foreign Key</em>'.
	 * @generated
	 */
	PhysicalForeignKey createPhysicalForeignKey();

	/**
	 * Returns a new object of class '<em>Physical Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Physical Model</em>'.
	 * @generated
	 */
	PhysicalModel createPhysicalModel();

	/**
	 * Returns a new object of class '<em>Physical Primary Key</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Physical Primary Key</em>'.
	 * @generated
	 */
	PhysicalPrimaryKey createPhysicalPrimaryKey();

	/**
	 * Returns a new object of class '<em>Physical Table</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Physical Table</em>'.
	 * @generated
	 */
	PhysicalTable createPhysicalTable();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	PhysicalModelPackage getPhysicalModelPackage();

} //PhysicalModelFactory
