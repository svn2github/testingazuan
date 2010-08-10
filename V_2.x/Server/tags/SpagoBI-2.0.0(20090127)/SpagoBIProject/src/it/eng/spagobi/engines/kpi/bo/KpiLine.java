package it.eng.spagobi.engines.kpi.bo;

import it.eng.spagobi.kpi.config.bo.KpiValue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class KpiLine {
	
	String modelNodeName = null;
	Boolean alarm = false ; //if the kpi is under alarm control
	KpiValue value = null;
	String thresholdsJsArray = "";
	Color semaphorColor = null;
	ChartImpl chartBullet = null;	
	List children = null;//List ok kpiLineChildren
	List documents = null;//List of documents related to the Kpi
	
	public KpiLine() {
		super();
		this.children = new ArrayList();
		this.documents = new ArrayList();
	}
	public KpiLine(String modelNodeName,String arrayJs, Boolean alarm, KpiValue value,
			Color semaphorColor, ChartImpl chartBullet, List children,
			List documents) {
		super();
		this.thresholdsJsArray = arrayJs;
		this.modelNodeName = modelNodeName;
		this.alarm = alarm;
		this.value = value;
		this.semaphorColor = semaphorColor;
		this.chartBullet = chartBullet;
		this.children = children;
		this.documents = documents;
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
}
