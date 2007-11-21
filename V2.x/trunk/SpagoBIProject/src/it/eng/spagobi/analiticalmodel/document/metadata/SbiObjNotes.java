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


public class SbiObjNotes  implements java.io.Serializable {

     private Integer objNoteId;
     private SbiObjects sbiObject;
     private SbiBinContents sbiBinContents;
     private String execReq;
     
	public Integer getObjNoteId() {
		return objNoteId;
	}
	
	public void setObjNoteId(Integer objNoteId) {
		this.objNoteId = objNoteId;
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

	public String getExecReq() {
		return execReq;
	}

	public void setExecReq(String execReq) {
		this.execReq = execReq;
	}
	
	
}