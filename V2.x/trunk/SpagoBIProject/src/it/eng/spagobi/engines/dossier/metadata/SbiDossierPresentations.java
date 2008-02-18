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
import it.eng.spagobi.commons.metadata.SbiBinContents;

import java.util.Date;


/**
 * @author zerbetto (davide.zerbetto@eng.it)
 */

public class SbiDossierPresentations  implements java.io.Serializable {


    // Fields    

     private Integer presentationId;
     private Long workflowProcessId;
     private SbiObjects sbiObject;
     private SbiBinContents sbiBinaryContent;
     private String name;
     private Integer prog;
     private Date creationDate;
     private Short approved;


    // Constructors

    /** default constructor */
    public SbiDossierPresentations() {
    }

	/** minimal constructor */
    public SbiDossierPresentations(Integer presentationId, Integer prog) {
        this.presentationId = presentationId;
        this.prog = prog;
    }
    
    /** full constructor */
    public SbiDossierPresentations(Integer presentationId, Long workflowProcessId, SbiObjects sbiObject, SbiBinContents sbiBinaryContent, String name, Integer prog, Date creationDate, Short approved) {
        this.presentationId = presentationId;
        this.workflowProcessId = workflowProcessId;
        this.sbiObject = sbiObject;
        this.sbiBinaryContent = sbiBinaryContent;
        this.name = name;
        this.prog = prog;
        this.creationDate = creationDate;
        this.approved = approved;
    }
    

   
    // Property accessors

    public Integer getPresentationId() {
        return this.presentationId;
    }
    
    public void setPresentationId(Integer presentationId) {
        this.presentationId = presentationId;
    }

    public SbiObjects getSbiObject() {
        return this.sbiObject;
    }
    
    public void setSbiObject(SbiObjects sbiObject) {
        this.sbiObject = sbiObject;
    }

    public SbiBinContents getSbiBinaryContent() {
        return this.sbiBinaryContent;
    }
    
    public void setSbiBinaryContent(SbiBinContents sbiBinaryContent) {
        this.sbiBinaryContent = sbiBinaryContent;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public Integer getProg() {
        return this.prog;
    }
    
    public void setProg(Integer prog) {
        this.prog = prog;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Short getApproved() {
        return this.approved;
    }
    
    public void setApproved(Short approved) {
        this.approved = approved;
    }

	public Long getWorkflowProcessId() {
		return workflowProcessId;
	}

	public void setWorkflowProcessId(Long workflowProcessId) {
		this.workflowProcessId = workflowProcessId;
	}
    
}
