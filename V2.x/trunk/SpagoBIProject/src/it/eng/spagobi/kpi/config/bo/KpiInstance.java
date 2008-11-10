package it.eng.spagobi.kpi.config.bo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KpiInstance {
	
	Integer kpiInstanceId = null;
	//List kpiHistoryValues = null;//a List of all past values with relative thresholds	
	List values = null;//last calculated values with relative thresholds for each resource
	Integer kpi = null ;// kpiId related to the kpiInstance
	Date d = null;	
	
	public KpiInstance() {
		super();
		//this.kpiHistoryValues = new ArrayList();
		this.values = new ArrayList();
	}

	public Integer getKpiId() {
		return kpiInstanceId;
	}

	public void setKpiId(Integer kpiId) {
		this.kpiInstanceId = kpiId;
	}

	/*
	public List getKpiHistoryValues() {
		return kpiHistoryValues;
	}

	public void setKpiHistoryValues(List kpiHistoryValues) {
		this.kpiHistoryValues = kpiHistoryValues;
	}*/

	public List getValue() {
		return values;
	}

	public void setValue(List value) {
		this.values = value;
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
	

}
