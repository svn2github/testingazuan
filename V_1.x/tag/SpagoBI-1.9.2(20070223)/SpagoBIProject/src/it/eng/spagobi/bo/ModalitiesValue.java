/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.bo;

import java.io.Serializable;


/**
 * Defines a Value object for the Predefined LOV 
 * 
 * @author sulis
 *
 */



public class ModalitiesValue implements Serializable  {
	
	private Integer id;
	private String name ="";
	private String description ="";
	private String lovProvider ="";
	private String iTypeCd ="";
	private String iTypeId = "";
	private String label ="";
	private String selectionType ="";
	private boolean multivalue = true;



	/**
	 * @return Returns the description.
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description The description to set.
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return Returns the id.
	 */
	public Integer  getId() {
		return id;
	}
	/**
	 * @param id The id to set.
	 */
	public void setId(Integer  id) {
		this.id = id;
	}
	/**
	 * @return Returns the iTypeCd.
	 */
	public String getITypeCd() {
		return iTypeCd;
	}
	/**
	 * @param typeCd The iTypeCd to set.
	 */
	public void setITypeCd(String typeCd) {
		iTypeCd = typeCd;
	}
	/**
	 * @return Returns the lovProvider.
	 */
	public String getLovProvider() {
		return lovProvider;
	}
	/**
	 * @param lovProvider The lovProvider to set.
	 */
	public void setLovProvider(String lovProvider) {
		this.lovProvider = lovProvider;
	}
	/**
	 * @return Returns the name.
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return Returns the iTypeId.
	 */
	public String getITypeId() {
		return iTypeId;
	}
	/**
	 * @param typeId The iTypeId to set.
	 */
	public void setITypeId(String typeId) {
		iTypeId = typeId;
	}



	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getSelectionType() {
		return selectionType;
	}
	public void setSelectionType(String selectionType) {
		this.selectionType = selectionType;
	}
	public boolean isMultivalue() {
		return multivalue;
	}
	public void setMultivalue(boolean multivalue) {
		this.multivalue = multivalue;
	}
}
