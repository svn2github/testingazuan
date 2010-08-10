package it.eng.spagobi.kpi.config.bo;

import it.eng.spagobi.kpi.model.bo.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class KpiValue implements Cloneable{
	
	Integer kpiInstanceId = null;
	String value = null;	
	String valueDescr = null;
	List thresholdValues = null;
	Double weight = null;
	Double target = null;
	Date beginDate = null;
	Date endDate = null;//null beginDate + num_validity_days
	String scaleCode = null;
	String scaleName = null;
	Resource r = null;//Resource (project/process) to which refers the value
	String chartType = null;
	
	public String getChartType() {
		return chartType;
	}

	public void setChartType(String chartType) {
		this.chartType = chartType;
	}

	public KpiValue() {
		super();
		this.thresholdValues = new ArrayList();
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getScaleCode() {
		return scaleCode;
	}

	public void setScaleCode(String scaleCode) {
		this.scaleCode = scaleCode;
	}

	public String getScaleName() {
		return scaleName;
	}

	public void setScaleName(String scaleName) {
		this.scaleName = scaleName;
	}

	public List getThresholdValues() {
		return thresholdValues;
	}

	public void setThresholdValues(List thresholds) {
		this.thresholdValues = thresholds;
	}

	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Resource getR() {
		return r;
	}

	public void setR(Resource r) {
		this.r = r;
	}

	public Integer getKpiInstanceId() {
		return kpiInstanceId;
	}

	public void setKpiInstanceId(Integer kpiInstanceId) {
		this.kpiInstanceId = kpiInstanceId;
	}

	public Double getTarget() {
		return target;
	}

	public void setTarget(Double target) {
		this.target = target;
	}

	public String getValueDescr() {
		return valueDescr;
	}

	public void setValueDescr(String valueDescr) {
		this.valueDescr = valueDescr;
	}
	
	public KpiValue clone(){
		 KpiValue toReturn = new KpiValue();
		 toReturn.setBeginDate(beginDate);
		 toReturn.setChartType(chartType);
		 toReturn.setEndDate(endDate);
		 toReturn.setKpiInstanceId(kpiInstanceId);
		 toReturn.setR(r);
		 toReturn.setScaleCode(scaleCode);
		 toReturn.setScaleName(scaleName);
		 toReturn.setTarget(target);
		 toReturn.setThresholdValues(thresholdValues);
		 toReturn.setValue(value);
		 toReturn.setValueDescr(valueDescr);
		 toReturn.setWeight(weight);
		 return toReturn;
	}

}
