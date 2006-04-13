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


/**
 * SbiObjPar generated by hbm2java
 */
public class SbiObjPar  implements java.io.Serializable {

    // Fields    

     private Integer objParId;
     private SbiObjects sbiObject;
     private SbiParameters sbiParameter;
     private Short reqFl;
     private Short modFl;
     private Short viewFl;
     private Short multFl;
     private String label;
     private String parurlNm;
     private Integer prog;


    // Constructors

    /** default constructor */
    public SbiObjPar() {
    }
    
    /** constructor with id */
    public SbiObjPar(Integer objParId) {
        this.objParId = objParId;
    }


    // Property accessors
    
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Short getModFl() {
		return modFl;
	}

	public void setModFl(Short modFl) {
		this.modFl = modFl;
	}

	public Short getMultFl() {
		return multFl;
	}

	public void setMultFl(Short multFl) {
		this.multFl = multFl;
	}

	public Integer getObjParId() {
		return objParId;
	}

	public void setObjParId(Integer objParId) {
		this.objParId = objParId;
	}

	public String getParurlNm() {
		return parurlNm;
	}

	public void setParurlNm(String parurlNm) {
		this.parurlNm = parurlNm;
	}

	public Integer getProg() {
		return prog;
	}

	public void setProg(Integer prog) {
		this.prog = prog;
	}

	public Short getReqFl() {
		return reqFl;
	}

	public void setReqFl(Short reqFl) {
		this.reqFl = reqFl;
	}

	public SbiObjects getSbiObject() {
		return sbiObject;
	}

	public void setSbiObject(SbiObjects sbiObject) {
		this.sbiObject = sbiObject;
	}

	public SbiParameters getSbiParameter() {
		return sbiParameter;
	}

	public void setSbiParameter(SbiParameters sbiParameter) {
		this.sbiParameter = sbiParameter;
	}

	public Short getViewFl() {
		return viewFl;
	}

	public void setViewFl(Short viewFl) {
		this.viewFl = viewFl;
	}
   
}