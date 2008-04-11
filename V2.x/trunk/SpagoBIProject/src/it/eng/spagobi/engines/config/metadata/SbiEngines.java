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
package it.eng.spagobi.engines.config.metadata;

import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.tools.datasource.metadata.SbiDataSource;





/**
 * SbiEngines generated by hbm2java
 */
public class SbiEngines  implements java.io.Serializable {

    // Fields    

     private Integer engineId;
     private Short encrypt;
     private String name;
     private String descr;
     private String mainUrl;
     private String secnUrl;
     private String objUplDir;
     private String objUseDir;
     private String driverNm;
     private String label;     
     private SbiDomains engineType;
     private String classNm;
     private SbiDomains biobjType; 
     private SbiDataSource dataSource;
     private Integer useDataSource;
     private Integer useDataSet;


    // Constructors

	/** default constructor */
    public SbiEngines() {
    }
    
    /** constructor with id */
    public SbiEngines(Integer engineId) {
        this.engineId = engineId;
    }
   
    
    

    // Property accessors

    /**
     * 
     */
    public Integer getEngineId() {
        return this.engineId;
    }
    
    public void setEngineId(Integer engineId) {
        this.engineId = engineId;
    }

    /**
     * 
     */
    public Short getEncrypt() {
        return this.encrypt;
    }
    
    public void setEncrypt(Short encrypt) {
        this.encrypt = encrypt;
    }

    /**
     * 
     */
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     */
    public String getDescr() {
        return this.descr;
    }
    
    public void setDescr(String descr) {
        this.descr = descr;
    }

    /**
     * 
     */
    public String getMainUrl() {
        return this.mainUrl;
    }
    
    public void setMainUrl(String mainUrl) {
        this.mainUrl = mainUrl;
    }

    /**
     * 
     */
    public String getSecnUrl() {
        return this.secnUrl;
    }
    
    public void setSecnUrl(String secnUrl) {
        this.secnUrl = secnUrl;
    }

    /**
     * 
     */
    public String getObjUplDir() {
        return this.objUplDir;
    }
    
    public void setObjUplDir(String objUplDir) {
        this.objUplDir = objUplDir;
    }

    /**
     * 
     */
    public String getObjUseDir() {
        return this.objUseDir;
    }
    
    public void setObjUseDir(String objUseDir) {
        this.objUseDir = objUseDir;
    }

    /**
     * 
     */
    public String getDriverNm() {
        return this.driverNm;
    }
    
    public void setDriverNm(String driverNm) {
        this.driverNm = driverNm;
    }

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
    public SbiDomains getBiobjType() {
		return biobjType;
	}

	public void setBiobjType(SbiDomains biobjType) {
		this.biobjType = biobjType;
	}

	public String getClassNm() {
		return classNm;
	}

	public void setClassNm(String classNm) {
		this.classNm = classNm;
	}

	public SbiDomains getEngineType() {
		return engineType;
	}

	public void setEngineType(SbiDomains engineType) {
		this.engineType = engineType;
	}

	public SbiDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(SbiDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Integer getUseDataSource() {
		return useDataSource;
	}

	public void setUseDataSource(Integer useDataSource) {
		this.useDataSource = useDataSource;
	}

	public Integer getUseDataSet() {
		return useDataSet;
	}

	public void setUseDataSet(Integer useDataSet) {
		this.useDataSet = useDataSet;
	}


}