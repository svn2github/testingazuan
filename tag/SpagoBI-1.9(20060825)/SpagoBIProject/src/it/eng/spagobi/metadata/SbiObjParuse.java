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
package it.eng.spagobi.metadata;

public class SbiObjParuse implements java.io.Serializable {

	private SbiObjParuseId id;
    private SbiObjPar sbiObjParFather;
    private String filterColumn;
    private String filterOperation;
    
    // Constructors

	/** default constructor */
    public SbiObjParuse() {
    }
    
    /** constructor with id */
    public SbiObjParuse(SbiObjParuseId id) {
        this.id = id;
    }

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

	public SbiObjParuseId getId() {
		return id;
	}

	public void setId(SbiObjParuseId id) {
		this.id = id;
	}

	public SbiObjPar getSbiObjParFather() {
		return sbiObjParFather;
	}

	public void setSbiObjParFather(SbiObjPar sbiObjParFather) {
		this.sbiObjParFather = sbiObjParFather;
	}
	
}
