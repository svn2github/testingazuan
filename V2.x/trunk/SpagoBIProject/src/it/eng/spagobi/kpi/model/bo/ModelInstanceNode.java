package it.eng.spagobi.kpi.model.bo;

import it.eng.spagobi.kpi.config.bo.KpiInstance;

import java.util.ArrayList;
import java.util.List;

public class ModelInstanceNode {
	
	Boolean isRoot = null;
	String name = null;
	String descr = null;
	ModelNode reference = null;
	Integer modelInstanceNodeId = null;
	Integer fatherId = null;
	List childrenIds = null;// List of ModelNodesInstances children
	KpiInstance kpiInstanceAssociated = null;
	List resources = null ; //Resources to which this model has to be applied
	
	public Boolean getIsRoot() {
		return isRoot;
	}

	public void setIsRoot(Boolean isRoot) {
		this.isRoot = isRoot;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}


	public ModelNode getModelReference() {
		return reference;
	}

	public void setModelReference(ModelNode reference) {
		this.reference = reference;
	}

	public KpiInstance getKpiInstanceAssociated() {
		return kpiInstanceAssociated;
	}

	public void setKpiInstanceAssociated(KpiInstance kpiInstanceAssociated) {
		this.kpiInstanceAssociated = kpiInstanceAssociated;
	}

	public List getResources() {
		return resources;
	}

	public void setResources(List resources) {
		this.resources = resources;
	}

	public ModelInstanceNode() {
		super();
		this.childrenIds = new ArrayList();	
		this.resources = new ArrayList();	
	}

	public Integer getModelInstanceNodeId() {
		return modelInstanceNodeId;
	}

	public void setModelInstanceNodeId(Integer modelInstanceNodeId) {
		this.modelInstanceNodeId = modelInstanceNodeId;
	}

	public List getChildrenIds() {
		return childrenIds;
	}

	public void setChildrenIds(List childrenIds) {
		this.childrenIds = childrenIds;
	}

	public Integer getFatherId() {
		return fatherId;
	}

	public void setFatherId(Integer fatherId) {
		this.fatherId = fatherId;
	}
	

}
