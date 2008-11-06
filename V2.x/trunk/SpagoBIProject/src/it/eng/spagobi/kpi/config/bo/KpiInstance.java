package it.eng.spagobi.kpi.config.bo;

import java.util.ArrayList;
import java.util.List;

public class KpiInstance {
	
	Integer kpiId = null;
	List kpiHistoryValues = null;//a List of all past values with relative thresholds	
	List lastValues = null;//last calculated values with relative thresholds for each resource
	Kpi kpi = null ;// type of the kpi instance
	
	
	public KpiInstance() {
		super();
		this.kpiHistoryValues = new ArrayList();
		this.lastValues = new ArrayList();
	}

	public Integer getKpiId() {
		return kpiId;
	}

	public void setKpiId(Integer kpiId) {
		this.kpiId = kpiId;
	}

	public List getKpiHistoryValues() {
		return kpiHistoryValues;
	}

	public void setKpiHistoryValues(List kpiHistoryValues) {
		this.kpiHistoryValues = kpiHistoryValues;
	}

	public List getValue() {
		return lastValues;
	}

	public void setValue(List value) {
		this.lastValues = value;
	}

	public Kpi getKpi() {
		return kpi;
	}

	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}
	

}
