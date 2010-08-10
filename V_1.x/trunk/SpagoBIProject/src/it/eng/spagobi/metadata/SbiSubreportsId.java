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

import java.io.Serializable;

public class SbiSubreportsId implements Serializable {
    
    private SbiObjects masterReport;
    private SbiObjects subReport;

    /** default constructor */
    public SbiSubreportsId() {}

	public SbiObjects getMasterReport() {
		return masterReport;
	}
	public void setMasterReport(SbiObjects masterReport) {
		this.masterReport = masterReport;
	}
	public SbiObjects getSubReport() {
		return subReport;
	}
	public void setSubReport(SbiObjects subReport) {
		this.subReport = subReport;
	}
	public boolean equals(Object other) {
		if ( (this == other ) ) return true;
		if ( (other == null ) ) return false;
		if ( !(other instanceof SbiSubreportsId) ) return false;
		SbiSubreportsId castOther = ( SbiSubreportsId ) other;
		return (this.getMasterReport()==castOther.getMasterReport()) || ( this.getMasterReport()!=null && castOther.getMasterReport()!=null && this.getMasterReport().equals(castOther.getMasterReport()) )
			&& (this.getSubReport()==castOther.getSubReport()) || ( this.getSubReport()!=null && castOther.getSubReport()!=null && this.getSubReport().equals(castOther.getSubReport()) );
	}
	public int hashCode() {
	      int result = 17;
	      result = 37 * result + this.getMasterReport().hashCode();
	      result = 37 * result + this.getSubReport().hashCode();
	      return result;
	}
}
