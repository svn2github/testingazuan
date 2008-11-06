package it.eng.spagobi.kpi.model.bo;

import it.eng.spagobi.kpi.config.bo.KpiInstance;

import java.util.ArrayList;
import java.util.List;

public class ModelInstanceNode {
	
	Boolean isRoot = null;
	String name = null;
	String code = null;
	String descr = null;
	String type = null;
	ModelNode father = null;
	List children = null;// List of ModelNodesInstances children
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ModelNode getFather() {
		return father;
	}

	public void setFather(ModelNode father) {
		this.father = father;
	}

	public List getChildren() {
		return children;
	}

	public void setChildren(List children) {
		this.children = children;
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
		this.children = new ArrayList();	
		this.resources = new ArrayList();	
	}
	

}
