package it.eng.spagobi.kpi.model.metadata;
// Generated 5-nov-2008 17.17.20 by Hibernate Tools 3.1.0 beta3



/**
 * SbiKpiModelResources generated by hbm2java
 */

public class SbiKpiModelResources  implements java.io.Serializable {


    // Fields    
	
     private Integer kpiModelResourcesId;
     private SbiKpiModelInst sbiKpiModelInst;
     private SbiResources sbiResources;


    // Constructors

    /** default constructor */
    public SbiKpiModelResources() {
    }

    
    /** full constructor */
    public SbiKpiModelResources(Integer kpiModelResourcesId, SbiKpiModelInst sbiKpiModelInst, SbiResources sbiResources) {
        this.kpiModelResourcesId = kpiModelResourcesId;
        this.sbiKpiModelInst = sbiKpiModelInst;
        this.sbiResources = sbiResources;
    }
    

   
    // Property accessors

    public Integer getKpiModelResourcesId() {
        return this.kpiModelResourcesId;
    }
    
    public void setKpiModelResourcesId(Integer kpiModelResourcesId) {
        this.kpiModelResourcesId = kpiModelResourcesId;
    }

    public SbiKpiModelInst getSbiKpiModelInst() {
        return this.sbiKpiModelInst;
    }
    
    public void setSbiKpiModelInst(SbiKpiModelInst sbiKpiModelInst) {
        this.sbiKpiModelInst = sbiKpiModelInst;
    }

    public SbiResources getSbiResources() {
        return this.sbiResources;
    }
    
    public void setSbiResources(SbiResources sbiResources) {
        this.sbiResources = sbiResources;
    }
   








}
