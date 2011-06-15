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

import java.util.List;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Business View</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link it.eng.spagobi.meta.model.business.BusinessView#getJoinRelationships <em>Join Relationships</em>}</li>
 * </ul>
 * </p>
 *
 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessView()
 * @model
 * @generated
 */
public interface BusinessView extends BusinessColumnSet {
	/**
	 * Returns the value of the '<em><b>Join Relationships</b></em>' reference list.
	 * The list contents are of type {@link it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Join Relationships</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Join Relationships</em>' reference list.
	 * @see it.eng.spagobi.meta.model.business.BusinessModelPackage#getBusinessView_JoinRelationships()
	 * @model
	 * @generated
	 */
	EList<BusinessViewInnerJoinRelationship> getJoinRelationships();
	
	// =========================================================================
	// Utility methods
	// =========================================================================
	List<PhysicalTable> getPhysicalTables();
	
	List<PhysicalTable> getPhysicalTablesOccurrences();
	
	//if the PhysicalTable has more occurrence, return the BusinessInnerJoinRelationship corresponding at the occurence numer specified
	BusinessViewInnerJoinRelationship getBusinessViewInnerJoinRelationshipAtOccurrenceNumber(PhysicalTable physicalTable, int index);

} // BusinessView
