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

import java.util.Set;

/**
 * @author Gioia
 *
 */
public class SbiSubreports implements java.io.Serializable {

    // Fields    

     private Integer master_rpt_id;
     private Integer sub_rpt_id;


    // Constructors

    /** default constructor */
    public SbiSubreports() {
    }
    
    /** constructor with id */
    public SbiSubreports(Integer master_rpt_id, Integer sub_rpt_id) {
        this.master_rpt_id = master_rpt_id;
        this.sub_rpt_id = sub_rpt_id;
    }
    

    // Property accessors
    
    public Integer getMaster_rpt_id() {
		return master_rpt_id;
	}

	public void setMaster_rpt_id(Integer master_rpt_id) {
		this.master_rpt_id = master_rpt_id;
	}

	public Integer getSub_rpt_id() {
		return sub_rpt_id;
	}

	public void setSub_rpt_id(Integer sub_rpt_id) {
		this.sub_rpt_id = sub_rpt_id;
	}
}
