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
package it.eng.spagobi.meta.generator.jpamapping.wrappers.impl;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class JpaRelationshipColumnsNames {
	
	private String sourceColumnName;
	private String destinationColumnName;
	
	/**
	 * @return the sourceColumnName
	 */
	public String getSourceColumnName() {
		return sourceColumnName;
	}

	/**
	 * @param sourceColumnName the sourceColumnName to set
	 */
	public void setSourceColumnName(String sourceColumnName) {
		this.sourceColumnName = sourceColumnName;
	}

	
	
	/**
	 * @return the destinationColumnName
	 */
	public String getDestinationColumnName() {
		return destinationColumnName;
	}

	/**
	 * @param destinationColumnName the destinationColumnName to set
	 */
	public void setDestinationColumnName(String destinationColumnName) {
		this.destinationColumnName = destinationColumnName;
	}

	public JpaRelationshipColumnsNames(String sourceColumnName, String destinationColumnName){
		this.sourceColumnName = sourceColumnName;
		this.destinationColumnName = destinationColumnName;
	}

}
