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
import java.util.Date;

/**
 * Defines a value constraint object.
 * 
 * @author giachino
 *
 */


public class Viewpoint  implements Serializable   {
	
	private Integer vpId;
    private Integer biobjId;
    private String vpOwner;
    private String vpName;
    private String vpDesc;
    private String vpScope;
    private String vpValueParams;
    private Date   vpCreationDate;
    
	/**
	 * @return the biobjId
	 */
	public Integer getBiobjId() {
		return biobjId;
	}
	/**
	 * @param biobjId the biobjId to set
	 */
	public void setBiobjId(Integer biobjId) {
		this.biobjId = biobjId;
	}
	/**
	 * @return the vpDesc
	 */
	public String getVpDesc() {
		return vpDesc;
	}
	/**
	 * @param vpDesc the vpDesc to set
	 */
	public void setVpDesc(String vpDesc) {
		this.vpDesc = vpDesc;
	}
	/**
	 * @return the vpId
	 */
	public Integer getVpId() {
		return vpId;
	}
	/**
	 * @param vpId the vpId to set
	 */
	public void setVpId(Integer vpId) {
		this.vpId = vpId;
	}
	/**
	 * @return the vpName
	 */
	public String getVpName() {
		return vpName;
	}
	/**
	 * @param vpName the vpName to set
	 */
	public void setVpName(String vpName) {
		this.vpName = vpName;
	}
	/**
	 * @return the vpScope
	 */
	public String getVpScope() {
		return vpScope;
	}
	/**
	 * @param vpScope the vpScope to set
	 */
	public void setVpScope(String vpScope) {
		this.vpScope = vpScope;
	}
	/**
	 * @return the vpValueParams
	 */
	public String getVpValueParams() {
		return vpValueParams;
	}
	/**
	 * @param vpValueParams the vpValueParams to set
	 */
	public void setVpValueParams(String vpValueParams) {
		this.vpValueParams = vpValueParams;
	}
	/**
	 * @return the vpCreationDate
	 */
	public Date getVpCreationDate() {
		return vpCreationDate;
	}
	/**
	 * @param vpCreationDate the vpCreationDate to set
	 */
	public void setVpCreationDate(Date vpCreationDate) {
		this.vpCreationDate = vpCreationDate;
	}
	/**
	 * @return the vpOwner
	 */
	public String getVpOwner() {
		return vpOwner;
	}
	/**
	 * @param vpOwner the vpOwner to set
	 */
	public void setVpOwner(String vpOwner) {
		this.vpOwner = vpOwner;
	}
    

	

}
