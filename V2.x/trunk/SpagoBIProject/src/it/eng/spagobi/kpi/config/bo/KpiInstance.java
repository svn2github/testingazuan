package it.eng.spagobi.kpi.config.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KpiInstance {
	
	Integer kpiInstanceId = null;
	Integer periodicity = null;//id of periodicity of valability in seconds
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
	}

	public Integer getKpiInstanceId() {
		return kpiInstanceId;
	}

	public void setKpiInstanceId(Integer kpiId) {
		this.kpiInstanceId = kpiId;
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
