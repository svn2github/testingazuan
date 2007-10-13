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


public class SbiObjParuseId  implements java.io.Serializable {

    // Fields    
     private SbiParuse sbiParuse;
     private SbiObjPar sbiObjPar;
     private SbiObjPar sbiObjParFather;
     private String filterOperation;


    // Constructors
    /** default constructor */
    public SbiObjParuseId() {
    }
    

    // Getter and Setter
    public String getFilterOperation() {
    	return filterOperation;
    }

    public void setFilterOperation(String filterOperation) {
    	this.filterOperation = filterOperation;
    }

    public SbiObjPar getSbiObjPar() {
    	return sbiObjPar;
    }

    public void setSbiObjPar(SbiObjPar sbiObjPar) {
    	this.sbiObjPar = sbiObjPar;
    }

    public SbiObjPar getSbiObjParFather() {
    	return sbiObjParFather;
    }

    public void setSbiObjParFather(SbiObjPar sbiObjParFather) {
    	this.sbiObjParFather = sbiObjParFather;
    }

    public SbiParuse getSbiParuse() {
    	return sbiParuse;
    }

    public void setSbiParuse(SbiParuse sbiParuse) {
    	this.sbiParuse = sbiParuse;
    }   
    
    
    // hashcode generator
    public int hashCode() {
        int result = 17;
        result = 37 * result + this.getSbiParuse().hashCode();
        result = 37 * result + this.getSbiObjPar().hashCode();
        result = 37 * result + this.getSbiObjParFather().hashCode();
        result = 37 * result + this.getFilterOperation().hashCode();
        return result;
    }
    
    // override equals method
    public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SbiObjParuseId) ) return false;
		 SbiObjParuseId castOther = ( SbiObjParuseId ) other; 
         return (this.getSbiParuse()==castOther.getSbiParuse()) || 
                (this.getSbiParuse()!=null && castOther.getSbiParuse()!=null && this.getSbiParuse().equals(castOther.getSbiParuse()) ) &&
                (this.getSbiObjPar()==castOther.getSbiObjPar()) || 
                (this.getSbiObjPar()!=null && castOther.getSbiObjPar()!=null && this.getSbiObjPar().equals(castOther.getSbiObjPar()) &&
                (this.getSbiObjParFather()==castOther.getSbiObjParFather()) || 
                (this.getSbiObjParFather()!=null && castOther.getSbiObjParFather()!=null && this.getSbiObjParFather().equals(castOther.getSbiObjParFather()) ) &&
                (this.getFilterOperation()==castOther.getFilterOperation()) || 
                (this.getFilterOperation()!=null && castOther.getFilterOperation()!=null && this.getFilterOperation().equals(castOther.getFilterOperation()) ) );	
   }
   
   









}