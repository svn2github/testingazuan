package it.eng.spagobi.kpi.config.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KpiInstance {
	
	Integer kpiInstanceId = null;
	Integer periodicity = null;//period of valability in seconds
	List values = null;//last calculated values with relative thresholds for each resource
	Integer kpi = null ;// kpiId related to the kpiInstance
	Date d = null;	
	Double weight = null; 
	
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public KpiInstance() {
		super();
		//this.kpiHistoryValues = new ArrayList();
		this.values = new ArrayList();
	}

	public Integer getKpiInstanceId() {
		return kpiInstanceId;
	}

	public void setKpiInstanceId(Integer kpiId) {
		this.kpiInstanceId = kpiId;
	}

	/*
	public List getKpiHistoryValues() {
		return kpiHistoryValues;
	}

	public void setKpiHistoryValues(List kpiHistoryValues) {
		this.kpiHistoryValues = kpiHistoryValues;
	}*/

	public List getValues() {
		return values;
	}

	public void setValues(List values) {
		this.values = values;
	}

	public Integer getKpi() {
		return kpi;
	}

	public void setKpi(Integer kpi) {
		this.kpi = kpi;
	}


	public Date getD() {
		return d;
	}

	public void setD(Date d) {
		this.d = d;
	}

	public Integer getPeriodicity() {
		return periodicity;
	}

	public void setPeriodicity(Integer periodicity) {
		this.periodicity = periodicity;
	}
	

}
