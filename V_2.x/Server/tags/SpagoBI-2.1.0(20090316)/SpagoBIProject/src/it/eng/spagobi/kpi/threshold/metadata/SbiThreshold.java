package it.eng.spagobi.kpi.threshold.metadata;
// Generated 5-nov-2008 17.17.42 by Hibernate Tools 3.1.0 beta3

import it.eng.spagobi.commons.metadata.SbiDomains;

import java.util.HashSet;
import java.util.Set;


/**
 * SbiThreshold generated by hbm2java
 */

public class SbiThreshold  implements java.io.Serializable {


    // Fields    

     private Integer thresholdId;
     private SbiDomains sbiDomains;
     private String name;
     private String description;
     private String code;
     private Set sbiKpis = new HashSet(0);
     private Set sbiKpiInstanceHistories = new HashSet(0);
     private Set sbiKpiInstances = new HashSet(0);
     private Set sbiThresholdValues = new HashSet(0);


    // Constructors

    /** default constructor */
    public SbiThreshold() {
    }

	/** minimal constructor */
    public SbiThreshold(Integer thresholdId, SbiDomains sbiDomains) {
        this.thresholdId = thresholdId;
        this.sbiDomains = sbiDomains;
    }
    
    /** full constructor */
    public SbiThreshold(Integer thresholdId, SbiDomains sbiDomains, String name, String description, String code, Set sbiKpis, Set sbiKpiInstanceHistories, Set sbiKpiInstances, Set sbiThresholdValues) {
        this.thresholdId = thresholdId;
        this.sbiDomains = sbiDomains;
        this.name = name;
        this.description = description;
        this.code = code;
        this.sbiKpis = sbiKpis;
        this.sbiKpiInstanceHistories = sbiKpiInstanceHistories;
        this.sbiKpiInstances = sbiKpiInstances;
        this.sbiThresholdValues = sbiThresholdValues;
    }
    

   
    // Property accessors

    public Integer getThresholdId() {
        return this.thresholdId;
    }
    
    public void setThresholdId(Integer thresholdId) {
        this.thresholdId = thresholdId;
    }

    public SbiDomains getSbiDomains() {
        return this.sbiDomains;
    }
    
    public void setSbiDomains(SbiDomains sbiDomains) {
        this.sbiDomains = sbiDomains;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
    	return this.code;
    }
    
    public void setCode(String code) {
    	this.code = code;
    }
    public Set getSbiKpis() {
        return this.sbiKpis;
    }
    
    public void setSbiKpis(Set sbiKpis) {
        this.sbiKpis = sbiKpis;
    }

    public Set getSbiKpiInstanceHistories() {
        return this.sbiKpiInstanceHistories;
    }
    
    public void setSbiKpiInstanceHistories(Set sbiKpiInstanceHistories) {
        this.sbiKpiInstanceHistories = sbiKpiInstanceHistories;
    }

    public Set getSbiKpiInstances() {
        return this.sbiKpiInstances;
    }
    
    public void setSbiKpiInstances(Set sbiKpiInstances) {
        this.sbiKpiInstances = sbiKpiInstances;
    }

    public Set getSbiThresholdValues() {
        return this.sbiThresholdValues;
    }
    
    public void setSbiThresholdValues(Set sbiThresholdValues) {
        this.sbiThresholdValues = sbiThresholdValues;
    }
   
}
