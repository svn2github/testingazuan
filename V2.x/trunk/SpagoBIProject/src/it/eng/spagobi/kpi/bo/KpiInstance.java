package it.eng.spagobi.kpi.bo;

import java.util.ArrayList;
import java.util.List;

public class KpiInstance {
	
	Integer kpiId = null;
	List kpiHistoryValues = null;//a List of all past values with relative thresholds	
	KpiValue lastValue = null;//last calculated value with relative thresholds
	Kpi kpi = null ;// type of the kpi instance
	
	
	public KpiInstance() {
		super();
		this.kpiHistoryValues = new ArrayList();
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

	public KpiValue getValue() {
		return lastValue;
	}

	public void setValue(KpiValue value) {
		this.lastValue = value;
	}

	public Kpi getKpi() {
		return kpi;
	}

	public void setKpi(Kpi kpi) {
		this.kpi = kpi;
	}
	

}
