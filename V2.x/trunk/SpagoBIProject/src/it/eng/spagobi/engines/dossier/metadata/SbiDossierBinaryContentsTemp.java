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

import java.util.Date;


/**
 * @author zerbetto (davide.zerbetto@eng.it)
 */

public class SbiDossierBinaryContentsTemp  implements java.io.Serializable {


    // Fields    

     private Integer binId;
     private SbiDossierPartsTemp sbiDossierPartsTemp;
     private String name;
     private byte[] binContent;
     private String type;
     private Date creationDate;


    // Constructors

    /** default constructor */
    public SbiDossierBinaryContentsTemp() {
    }

	/** minimal constructor */
    public SbiDossierBinaryContentsTemp(Integer binId) {
        this.binId = binId;
    }
    
    /** full constructor */
    public SbiDossierBinaryContentsTemp(Integer binId, SbiDossierPartsTemp sbiDossierPartsTemp, String name, byte[] binContent, String type, Date creationDate) {
        this.binId = binId;
        this.sbiDossierPartsTemp = sbiDossierPartsTemp;
        this.name = name;
        this.binContent = binContent;
        this.type = type;
        this.creationDate = creationDate;
    }
    

   
    // Property accessors

    public Integer getBinId() {
        return this.binId;
    }
    
    public void setBinId(Integer binId) {
        this.binId = binId;
    }

    public SbiDossierPartsTemp getSbiDossierPartsTemp() {
        return this.sbiDossierPartsTemp;
    }
    
    public void setSbiDossierPartsTemp(SbiDossierPartsTemp sbiDossierPartsTemp) {
        this.sbiDossierPartsTemp = sbiDossierPartsTemp;
    }

    public byte[] getBinContent() {
        return this.binContent;
    }
    
    public void setBinContent(byte[] binContent) {
        this.binContent = binContent;
    }

    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }
    
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
    
}
