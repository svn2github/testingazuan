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
package it.eng.spagobi.analiticalmodel.document.metadata;

import it.eng.spagobi.commons.metadata.SbiBinContents;

import java.util.Date;


public class SbiObjTemplates  implements java.io.Serializable {

     private Integer objTempId;
     private SbiObjects sbiObject;
     private SbiBinContents sbiBinContents;
     private String name;
     private Integer prog;
     private Date creationDate;
     private Boolean active;
     private String dimension=null;     
     private String creationUser=null;
     
	public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }




	public String getCreationUser() {
        return creationUser;
    }

    public void setCreationUser(String creationUser) {
        this.creationUser = creationUser;
    }

	public Integer getObjTempId() {
		return objTempId;
	}
	
	public void setObjTempId(Integer objTempId) {
		this.objTempId = objTempId;
	}
	
	public SbiObjects getSbiObject() {
		return sbiObject;
	}
	
	public void setSbiObject(SbiObjects sbiObject) {
		this.sbiObject = sbiObject;
	}
	public SbiBinContents getSbiBinContents() {
		return sbiBinContents;
	}
	
	public void setSbiBinContents(SbiBinContents sbiBinContents) {
		this.sbiBinContents = sbiBinContents;
	}
	
	public Integer getProg() {
		return prog;
	}
	
	public void setProg(Integer prog) {
		this.prog = prog;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}