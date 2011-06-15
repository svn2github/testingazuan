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
package it.eng.spagobi.meta.model.business;

import it.eng.spagobi.meta.model.physical.PhysicalTable;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessTable#getPhysicalTable <em>Physical Table</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessTable()
 * @model
 * @generated
 */
public interface BusinessTable extends BusinessColumnSet {
	/**
	 * Returns the value of the '<em><b>Physical Table</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Physical Table</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Physical Table</em>' reference.
	 * @see #setPhysicalTable(PhysicalTable)
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessTable_PhysicalTable()
	 * @model required="true"
	 * @generated
	 */
	PhysicalTable getPhysicalTable();

	/**
	 * Sets the value of the '{@link it.eng.spagobi.meta.model.business.BusinessTable#getPhysicalTable <em>Physical Table</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Physical Table</em>' reference.
	 * @see #getPhysicalTable()
	 * @generated
	 */
	void setPhysicalTable(PhysicalTable value);

	

} // BusinessTable
