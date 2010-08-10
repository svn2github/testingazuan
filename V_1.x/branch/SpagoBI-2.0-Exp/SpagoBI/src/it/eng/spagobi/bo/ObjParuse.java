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
 * Defines a Business Intelligence object
 */
public class ObjParuse implements Serializable {

	private Integer objParId;
	private Integer paruseId;
    private Integer objParFatherId;
    private Integer prog;
    private String filterColumn;
    private String filterOperation;
    private String preCondition;
    private String postCondition;
    private String logicOperator;
    
	public String getFilterColumn() {
		return filterColumn;
	}
	public void setFilterColumn(String filterColumn) {
		this.filterColumn = filterColumn;
	}
	public String getFilterOperation() {
		return filterOperation;
	}
	public void setFilterOperation(String filterOperation) {
		this.filterOperation = filterOperation;
	}
	public Integer getObjParFatherId() {
		return objParFatherId;
	}
	public void setObjParFatherId(Integer objParFatherId) {
		this.objParFatherId = objParFatherId;
	}
	public Integer getObjParId() {
		return objParId;
	}
	public void setObjParId(Integer objParId) {
		this.objParId = objParId;
	}
	public Integer getParuseId() {
		return paruseId;
	}
	public void setParuseId(Integer paruseId) {
		this.paruseId = paruseId;
	}
	public String getLogicOperator() {
		return logicOperator;
	}
	public void setLogicOperator(String logicOperator) {
		this.logicOperator = logicOperator;
	}
	public String getPostCondition() {
		return postCondition;
	}
	public void setPostCondition(String postCondition) {
		this.postCondition = postCondition;
	}
	public String getPreCondition() {
		return preCondition;
	}
	public void setPreCondition(String preCondition) {
		this.preCondition = preCondition;
	}
	public Integer getProg() {
		return prog;
	}
	public void setProg(Integer prog) {
		this.prog = prog;
	}

}
