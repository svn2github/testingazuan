/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.behaviouralmodel.analyticaldriver.metadata;

import java.util.*;




/**
 * SbiParuseDet generated by hbm2java
 */
public class SbiParuseDet  implements java.io.Serializable {

    // Fields    

     private SbiParuseDetId id;
     private Integer prog;
     
     private Short hiddenFl;
     private String defaultVal;


    // Constructors

    /**
     * default constructor.
     */
    public SbiParuseDet() {
    }
    
    /**
     * constructor with id.
     * 
     * @param id the id
     */
    public SbiParuseDet(SbiParuseDetId id) {
        this.id = id;
    }
   
    
    

    // Property accessors

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public SbiParuseDetId getId() {
        return this.id;
    }
    
    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(SbiParuseDetId id) {
        this.id = id;
    }

    /**
     * Gets the prog.
     * 
     * @return the prog
     */
    public Integer getProg() {
        return this.prog;
    }
    
    /**
     * Sets the prog.
     * 
     * @param prog the new prog
     */
    public void setProg(Integer prog) {
        this.prog = prog;
    }



	/**
	 * Gets the default val.
	 * 
	 * @return the default val
	 */
	public String getDefaultVal() {
		return defaultVal;
	}
	
	/**
	 * Sets the default val.
	 * 
	 * @param defaultVal the new default val
	 */
	public void setDefaultVal(String defaultVal) {
		this.defaultVal = defaultVal;
	}
	
	/**
	 * Gets the hidden fl.
	 * 
	 * @return the hidden fl
	 */
	public Short getHiddenFl() {
		return hiddenFl;
	}
	
	/**
	 * Sets the hidden fl.
	 * 
	 * @param hiddenFl the new hidden fl
	 */
	public void setHiddenFl(Short hiddenFl) {
		this.hiddenFl = hiddenFl;
	}
}