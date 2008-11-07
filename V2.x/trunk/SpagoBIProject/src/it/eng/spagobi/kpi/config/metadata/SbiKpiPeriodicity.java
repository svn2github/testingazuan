package it.eng.spagobi.kpi.config.metadata;
// Generated 5-nov-2008 17.16.37 by Hibernate Tools 3.1.0 beta3



/**
 * SbiKpiPeriodicity generated by hbm2java
 */

public class SbiKpiPeriodicity  implements java.io.Serializable {


    // Fields    

     private Integer idKpiInstancePeriod;
     private SbiKpiInstance sbiKpiInstance;
     private Integer value;


    // Constructors

    /** default constructor */
    public SbiKpiPeriodicity() {
    }

	/** minimal constructor */
    public SbiKpiPeriodicity(Integer idKpiInstancePeriod, SbiKpiInstance sbiKpiInstance) {
        this.idKpiInstancePeriod = idKpiInstancePeriod;
        this.sbiKpiInstance = sbiKpiInstance;
    }
    
    /** full constructor */
    public SbiKpiPeriodicity(Integer idKpiInstancePeriod, SbiKpiInstance sbiKpiInstance, Integer value) {
        this.idKpiInstancePeriod = idKpiInstancePeriod;
        this.sbiKpiInstance = sbiKpiInstance;
        this.value = value;
    }
    

   
    // Property accessors

    public Integer getIdKpiInstancePeriod() {
        return this.idKpiInstancePeriod;
    }
    
    public void setIdKpiInstancePeriod(Integer idKpiInstancePeriod) {
        this.idKpiInstancePeriod = idKpiInstancePeriod;
    }

    public SbiKpiInstance getSbiKpiInstance() {
        return this.sbiKpiInstance;
    }
    
    public void setSbiKpiInstance(SbiKpiInstance sbiKpiInstance) {
        this.sbiKpiInstance = sbiKpiInstance;
    }

    public Integer getValue() {
        return this.value;
    }
    
    public void setValue(Integer value) {
        this.value = value;
    }
   








}
