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
 * Defines a Domain object.
 * 
 * @author sulis
 *
 */

public class Domain  implements Serializable  {
	
	private Integer valueId ;
	private String valueCd = "";
	private String valueName = "";

	/**
	 * @return Returns the valueCd.
	 */
	public String getValueCd() {
		return valueCd;
	}
	/**
	 * @param valueCd The valueCd to set.
	 */
	public void setValueCd(String valueCd) {
		this.valueCd = valueCd;
	}
	/**
	 * @return Returns the valueId.
	 */
	public Integer  getValueId() {
		return valueId;
	}
	/**
	 * @param valueId The valueId to set.
	 */
	public void setValueId(Integer  valueId) {
		this.valueId = valueId;
	}
	/**
	 * @return Returns the valueName.
	 */
	public String getValueName() {
		return valueName;
	}
	/**
	 * @param valueName The valueName to set.
	 */
	public void setValueName(String valueName) {
		this.valueName = valueName;
	}
	
	/*
	public static Domain getDomain(String codeDomain, String codeValue) throws EMFUserError {
		DomainDAOImpl domaindao = new DomainDAOImpl();
	    return domaindao.load(codeDomain, codeValue);
	}
	*/
}
	
	


	