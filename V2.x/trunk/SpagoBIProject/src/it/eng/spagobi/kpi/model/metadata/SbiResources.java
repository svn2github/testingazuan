package it.eng.spagobi.kpi.model.metadata;
// Generated 5-nov-2008 17.17.20 by Hibernate Tools 3.1.0 beta3

import it.eng.spagobi.commons.metadata.SbiDomains;

import java.util.HashSet;
import java.util.Set;


/**
 * SbiResources generated by hbm2java
 */

public class SbiResources  implements java.io.Serializable {


    // Fields    

     private Integer resourceId;
     private SbiDomains sbiDomains;
     private String tableName;
     private String columnName;
     private String resourceName;
     private Set sbiKpiValues = new HashSet(0);
     private Set sbiKpiModelResourceses = new HashSet(0);


    // Constructors

    /** default constructor */
    public SbiResources() {
    }

	/** minimal constructor */
    public SbiResources(Integer resourceId, SbiDomains sbiDomains) {
        this.resourceId = resourceId;
        this.sbiDomains = sbiDomains;
    }
    
    /** full constructor */
    public SbiResources(Integer resourceId, SbiDomains sbiDomains, String tableName, String columnName, String resourceName, Set sbiKpiValues, Set sbiKpiModelResourceses) {
        this.resourceId = resourceId;
        this.sbiDomains = sbiDomains;
        this.tableName = tableName;
        this.columnName = columnName;
        this.resourceName = resourceName;
        this.sbiKpiValues = sbiKpiValues;
        this.sbiKpiModelResourceses = sbiKpiModelResourceses;
    }
    

   
    // Property accessors

    public Integer getResourceId() {
        return this.resourceId;
    }
    
    public void setResourceId(Integer resourceId) {
        this.resourceId = resourceId;
    }

    public SbiDomains getSbiDomains() {
        return this.sbiDomains;
    }
    
    public void setSbiDomains(SbiDomains sbiDomains) {
        this.sbiDomains = sbiDomains;
    }

    public String getTableName() {
        return this.tableName;
    }
    
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return this.columnName;
    }
    
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getResourceName() {
        return this.resourceName;
    }
    
    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public Set getSbiKpiValues() {
        return this.sbiKpiValues;
    }
    
    public void setSbiKpiValues(Set sbiKpiValues) {
        this.sbiKpiValues = sbiKpiValues;
    }

    public Set getSbiKpiModelResourceses() {
        return this.sbiKpiModelResourceses;
    }
    
    public void setSbiKpiModelResourceses(Set sbiKpiModelResourceses) {
        this.sbiKpiModelResourceses = sbiKpiModelResourceses;
    }
   








}
