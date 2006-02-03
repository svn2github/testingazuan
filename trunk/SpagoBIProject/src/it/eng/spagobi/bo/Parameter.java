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
import java.util.List;



/**
 * Defines a <code>Parameter</code> object.
 *
 * @author sulis
 */

public class Parameter implements Serializable {
	
	private Integer  id;
	private String 	 description = ""; 
	private Integer  length;
	private String 	 label = "";
	private String 	 name = "";
	private String type = "";
	private String mask = "";
	private Integer typeId;
	private String modality = "";
	
	private ModalitiesValue modalityValue = null;
	private List checks = null;

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
	 * @return Returns the label.
	 */
	public String getLabel() {
		return label;
	}
	/**
	 * @param label The label to set.
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	/**
	 * @return Returns the length.
	 */
	public Integer  getLength() {
		return length;
	}
	/**
	 * @param length The length to set.
	 */
	public void setLength(Integer  length) {
		this.length = length;
	}
	/**
	 * @return Returns the mask.
	 */
	public String getMask() {
		return mask;
	}
	/**
	 * @param mask The mask to set.
	 */
	public void setMask(String mask) {
		this.mask = mask;
	}
	/**
	 * @return Returns the type.
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type The type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	/**
	 * @return Returns the typeId.
	 */
	public Integer getTypeId() {
		return typeId;
	}
	/**
	 * @param typeId The typeId to set.
	 */
	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}
	/**
	 * @return Returns the modalityValue.
	 */
	public ModalitiesValue getModalityValue() {
		return modalityValue;
	}
	/**
	 * @param modalityValue The modalityValue to set.
	 */
	public void setModalityValue(ModalitiesValue modalityValue) {
		this.modalityValue = modalityValue;
	}
	/**
	 * @return Returns the modality.
	 */
	public String getModality() {
		return modality;
	}
	/**
	 * @param modality The modality to set.
	 */
	public void setModality(String modality) {
		this.modality = modality;
	}
	
	/**
	 * @return Returns the checks.
	 */
	public List getChecks() {
		return checks;
	}
	/**
	 * @param checks The checks to set.
	 */
	public void setChecks(List checks) {
		this.checks = checks;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}