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
package it.eng.spagobi.meta.generator.jpamapping;




import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
// JpaRelationship è un decorator che aggiunge una serie di metodi utili a velocity per 
// estrarre informazioni da una determinata business relationship durante la creazione dl mapping
public class JpaRelationship {
	JpaTable jpaTable;
	BusinessRelationship businessRelationship;
	
	public JpaRelationship(JpaTable jpaTable, BusinessRelationship businessRelationship) {
		this.jpaTable = jpaTable;
		this.businessRelationship = businessRelationship;
	}
	
	public boolean isSourceRole() {
		return businessRelationship.getSourceTable().equals(jpaTable.getBusinessTable());
	}
	
	public JpaTable getReferencedTable()  {
		if ( isSourceRole() ) {
			if(businessRelationship.getDestinationTable() instanceof BusinessTable) {
				return new JpaTable((BusinessTable)businessRelationship.getDestinationTable());
			}
		} else {
			if(businessRelationship.getSourceTable() instanceof BusinessTable) {
				return new JpaTable((BusinessTable)businessRelationship.getSourceTable());
			}
		}
		return null;
	}
	
	public String getCardinality() {
		return "one-to-many";
	}
	
	/**
	 * Returns a descriptive string used in a comment in the generated 
	 * file (from the Velocity template).
	 */
	public String getDescription()  {
		/*
		String directionality;
		if (getAssociation().getDirectionality().equals(Association.BI_DI)) {
			directionality = "bi-directional";
		} else {
			directionality = "uni-directional";
		}
		*/
		return /*directionality + " " + */ getCardinality() + " association to " + getReferencedTable().getClassName();
	}
	
	
	public BusinessRelationship getBusinessRelationship() {
		return businessRelationship;
	}

	public void setBusinessRelationship(BusinessRelationship businessRelationship) {
		this.businessRelationship = businessRelationship;
	}

	public JpaTable getJpaTable() {
		return jpaTable;
	}
	public void setJpaTable(JpaTable jpaTable) {
		this.jpaTable = jpaTable;
	}
	
	
}
