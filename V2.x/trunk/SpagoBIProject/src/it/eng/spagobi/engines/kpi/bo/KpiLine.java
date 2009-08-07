package it.eng.spagobi.engines.kpi.bo;

import it.eng.spagobi.kpi.config.bo.KpiValue;
import it.eng.spagobi.kpi.threshold.bo.ThresholdValue;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class KpiLine implements Serializable{
	
	Integer modelInstanceNodeId = null;
	String modelNodeName = null;
	String modelInstanceCode = null;
	Boolean alarm = false ; //if the kpi is under alarm control
	KpiValue value = null;
	String thresholdsJsArray = "";
	Color semaphorColor = null;
	ChartImpl chartBullet = null;	
	ThresholdValue thresholdOfValue = null;
	List children = null;//List ok kpiLineChildren
	List documents = null;//List of documents related to the Kpi
	
	public KpiLine() {
		super();
		this.children = new ArrayList();
		this.documents = new ArrayList();
	}
	public KpiLine(String modelNodeName,String arrayJs, Boolean alarm, KpiValue value,
			Color semaphorColor, ChartImpl chartBullet, List children,
			List documents, Integer modelInstanceNodeId, String modelInstanceCode,ThresholdValue thresholdOfValue) {
		super();
		this.thresholdsJsArray = arrayJs;
		this.modelNodeName = modelNodeName;
		this.alarm = alarm;
		this.value = value;
		this.semaphorColor = semaphorColor;
		this.chartBullet = chartBullet;
		this.children = children;
		this.documents = documents;
		this.modelInstanceNodeId = modelInstanceNodeId;
		this.modelInstanceCode = modelInstanceCode;
		this.thresholdOfValue = thresholdOfValue;
	}
	
	public String getModelNodeName() {
		return modelNodeName;
	}
	public void setModelNodeName(String modelNodeName) {
		this.modelNodeName = modelNodeName;
	}
	public Boolean getAlarm() {
		return alarm;
	}
	public void setAlarm(Boolean alarm) {
		this.alarm = alarm;
	}
	public KpiValue getValue() {
		return value;
	}
	public void setValue(KpiValue value) {
		this.value = value;
	}
	public Color getSemaphorColor() {
		return semaphorColor;
	}
	public void setSemaphorColor(Color semaphorColor) {
		this.semaphorColor = semaphorColor;
	}
	public ChartImpl getChartBullet() {
		return chartBullet;
	}
	public void setChartBullet(ChartImpl chartBullet) {
		this.chartBullet = chartBullet;
	}
	public List getChildren() {
		return children;
	}
	public void setChildren(List children) {
		this.children = children;
	}
	public List getDocuments() {
		return documents;
	}
	public void setDocuments(List documents) {
		this.documents = documents;
	}
	public String getThresholdsJsArray() {
		return thresholdsJsArray;
	}
	public void setThresholdsJsArray(String thresholdsJsArray) {
		this.thresholdsJsArray = thresholdsJsArray;
	}
	public Integer getModelInstanceNodeId() {
		return modelInstanceNodeId;
	}
	public void setModelInstanceNodeId(Integer modelInstanceNodeId) {
		this.modelInstanceNodeId = modelInstanceNodeId;
	}
	public String getModelInstanceCode() {
		return modelInstanceCode;
	}
	public void setModelInstanceCode(String modelInstanceCode) {
		this.modelInstanceCode = modelInstanceCode;
	}
	public ThresholdValue getThresholdOfValue() {
		return thresholdOfValue;
	}
	public void setThresholdOfValue(ThresholdValue thresholdOfValue) {
		this.thresholdOfValue = thresholdOfValue;
	}
	
	public int compareTo(KpiLine l)    {
		return this.modelInstanceCode.compareTo(l.getModelInstanceCode());
	 }
}
