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
package it.eng.spagobi.engines.dossier.metadata;

import it.eng.spagobi.analiticalmodel.document.metadata.SbiObjects;

import java.util.HashSet;
import java.util.Set;


/**
 * @author zerbetto (davide.zerbetto@eng.it)
 */

public class SbiDossierPartsTemp  implements java.io.Serializable {


    // Fields    

     private Integer partId;
     private SbiObjects sbiObject;
     private Integer pageId;
     private Long workflowProcessId;
     private Set sbiDossierBinaryContentsTemps = new HashSet(0);


    // Constructors

    /** default constructor */
    public SbiDossierPartsTemp() {
    }

	/** minimal constructor */
    public SbiDossierPartsTemp(Integer partId) {
        this.partId = partId;
    }
    
    /** full constructor */
    public SbiDossierPartsTemp(Integer partId, SbiObjects sbiObject, Long workflowProcessId, Set sbiDossierBinaryContentsTemps) {
        this.partId = partId;
        this.sbiObject = sbiObject;
        this.workflowProcessId = workflowProcessId;
        this.sbiDossierBinaryContentsTemps = sbiDossierBinaryContentsTemps;
    }
    

   
    // Property accessors

    public Integer getPartId() {
        return this.partId;
    }
    
    public void setPartId(Integer partId) {
        this.partId = partId;
    }

    public SbiObjects getSbiObject() {
        return this.sbiObject;
    }
    
    public void setSbiObject(SbiObjects sbiObject) {
        this.sbiObject = sbiObject;
    }

    public Long getWorkflowProcessId() {
        return this.workflowProcessId;
    }
    
    public void setWorkflowProcessId(Long workflowProcessId) {
        this.workflowProcessId = workflowProcessId;
    }

    public Set getSbiDossierBinaryContentsTemps() {
        return this.sbiDossierBinaryContentsTemps;
    }
    
    public void setSbiDossierBinaryContentsTemps(Set sbiDossierBinaryContentsTemps) {
        this.sbiDossierBinaryContentsTemps = sbiDossierBinaryContentsTemps;
    }

	public Integer getPageId() {
		return pageId;
	}

	public void setPageId(Integer pageId) {
		this.pageId = pageId;
	}
   
}
