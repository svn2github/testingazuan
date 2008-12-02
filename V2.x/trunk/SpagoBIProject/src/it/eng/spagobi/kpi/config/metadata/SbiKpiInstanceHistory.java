package it.eng.spagobi.kpi.config.metadata;
// Generated 2-dic-2008 10.47.59 by Hibernate Tools 3.1.0 beta3

import it.eng.spagobi.commons.metadata.SbiDomains;
import it.eng.spagobi.kpi.threshold.metadata.SbiThreshold;

import java.util.Date;


/**
 * SbiKpiInstanceHistory generated by hbm2java
 */

public class SbiKpiInstanceHistory  implements java.io.Serializable {


    // Fields    

     private Integer idKpiInstanceHistory;
     private SbiDomains sbiDomains;
     private SbiKpiInstance sbiKpiInstance;
     private SbiThreshold sbiThreshold;
     private SbiMeasureUnit sbiMeasureUnit;
     private double weight;
     private Date beginDt;
     private Date endDt;


    // Constructors

    /** default constructor */
    public SbiKpiInstanceHistory() {
    }

	/** minimal constructor */
    public SbiKpiInstanceHistory(Integer idKpiInstanceHistory, SbiDomains sbiDomains, SbiKpiInstance sbiKpiInstance, SbiThreshold sbiThreshold, SbiMeasureUnit sbiMeasureUnit) {
        this.idKpiInstanceHistory = idKpiInstanceHistory;
        this.sbiDomains = sbiDomains;
        this.sbiKpiInstance = sbiKpiInstance;
        this.sbiThreshold = sbiThreshold;
        this.sbiMeasureUnit = sbiMeasureUnit;
    }
    
    /** full constructor */
    public SbiKpiInstanceHistory(Integer idKpiInstanceHistory, SbiDomains sbiDomains, SbiKpiInstance sbiKpiInstance, SbiThreshold sbiThreshold, SbiMeasureUnit sbiMeasureUnit, double weight, Date beginDt, Date endDt) {
        this.idKpiInstanceHistory = idKpiInstanceHistory;
        this.sbiDomains = sbiDomains;
        this.sbiKpiInstance = sbiKpiInstance;
        this.sbiThreshold = sbiThreshold;
        this.sbiMeasureUnit = sbiMeasureUnit;
        this.weight = weight;
        this.beginDt = beginDt;
        this.endDt = endDt;
    }
    

   
    // Property accessors

    public Integer getIdKpiInstanceHistory() {
        return this.idKpiInstanceHistory;
    }
    
    public void setIdKpiInstanceHistory(Integer idKpiInstanceHistory) {
        this.idKpiInstanceHistory = idKpiInstanceHistory;
    }

    public SbiDomains getSbiDomains() {
        return this.sbiDomains;
    }
    
    public void setSbiDomains(SbiDomains sbiDomains) {
        this.sbiDomains = sbiDomains;
    }

    public SbiKpiInstance getSbiKpiInstance() {
        return this.sbiKpiInstance;
    }
    
    public void setSbiKpiInstance(SbiKpiInstance sbiKpiInstance) {
        this.sbiKpiInstance = sbiKpiInstance;
    }

    public SbiThreshold getSbiThreshold() {
        return this.sbiThreshold;
    }
    
    public void setSbiThreshold(SbiThreshold sbiThreshold) {
        this.sbiThreshold = sbiThreshold;
    }

    public SbiMeasureUnit getSbiMeasureUnit() {
        return this.sbiMeasureUnit;
    }
    
    public void setSbiMeasureUnit(SbiMeasureUnit sbiMeasureUnit) {
        this.sbiMeasureUnit = sbiMeasureUnit;
    }

    public double getWeight() {
        return this.weight;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Date getBeginDt() {
        return this.beginDt;
    }
    
    public void setBeginDt(Date beginDt) {
        this.beginDt = beginDt;
    }

    public Date getEndDt() {
        return this.endDt;
    }
    
    public void setEndDt(Date endDt) {
        this.endDt = endDt;
    }
   
}
