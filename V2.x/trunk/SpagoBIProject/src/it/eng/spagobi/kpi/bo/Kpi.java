package it.eng.spagobi.kpi.bo;

import it.eng.spagobi.tools.dataset.bo.DataSetConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Kpi {
	
	Integer kpiId = null;
	String kpiName = null;
	DataSetConfig kpiDs = null;// Related DataSet
	String documentLabel = null; //document related to this KPI
	Boolean isRoot = null;
	Kpi father = null;
	List kpiChildren = null; // List of Kpis children
	List roles = null;	//roles that can view this KPI
	List Thresholds = null;
	List kpiInstances = null; //all the instances of that KPI
	Double standardWeight = null;
	String metric = null;
	String description = null;	
	String scaleCode = null;
	String scaleName = null;
	
	
	
	public Kpi() {
		super();
		this.kpiChildren = new ArrayList();
		this.isRoot = false;	
		this.Thresholds = new ArrayList();
		this.roles = new ArrayList();
		this.kpiInstances = new ArrayList();
	}



	public Integer getKpiId() {
		return kpiId;
	}



	public void setKpiId(Integer kpiId) {
		this.kpiId = kpiId;
	}



	public String getKpiName() {
		return kpiName;
	}



	public void setKpiName(String kpiName) {
		this.kpiName = kpiName;
	}



	public DataSetConfig getKpiDs() {
		return kpiDs;
	}



	public void setKpiDs(DataSetConfig kpiDs) {
		this.kpiDs = kpiDs;
	}



	public String getDocumentLabel() {
		return documentLabel;
	}



	public void setDocumentLabel(String documentLabel) {
		this.documentLabel = documentLabel;
	}



	public Kpi getFather() {
		return father;
	}



	public void setFather(Kpi father) {
		this.father = father;
	}



	public List getKpiChildren() {
		return kpiChildren;
	}



	public void setKpiChildren(List kpiChildren) {
		this.kpiChildren = kpiChildren;
	}



	public Boolean getIsParent() {
		return isRoot;
	}



	public void setIsParent(Boolean isParent) {
		this.isRoot = isParent;
	}



	public List getThresholds() {
		return Thresholds;
	}



	public void setThresholds(List thresholds) {
		Thresholds = thresholds;
	}


	public Double getStandardWeight() {
		return standardWeight;
	}



	public void setStandardWeight(Double standardWeight) {
		this.standardWeight = standardWeight;
	}



	public String getMetric() {
		return metric;
	}



	public void setMetric(String metric) {
		this.metric = metric;
	}



	public String getDescription() {
		return description;
	}



	public void setDescription(String description) {
		this.description = description;
	}



	public List getRoles() {
		return roles;
	}



	public void setRoles(List roles) {
		this.roles = roles;
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



	public Boolean getIsRoot() {
		return isRoot;
	}



	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}



	public List getKpiInstances() {
		return kpiInstances;
	}



	public void setKpiInstances(List kpiInstances) {
		this.kpiInstances = kpiInstances;
	}
	

}
