package it.eng.spagobi.kpi.model.bo;

import java.util.List;

public class ModelInstance {
	
	private Integer id = null;
	private Model model = null;//Model to which this instance refers
	private ModelNode root =null; //root of the tree made of ModelInstance, representing the model
	private List childrenNodes = null;//List of ModelInstanceNodes children
	private String name = null;//name of the complete model instance(like "my own CMMI")
	private String description = null;//description of the complete model instance
	private Integer parentId = null;
	
	public Integer getParentId() {
		return parentId;
	}
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Model getModel() {
		return model;
	}
	public void setModel(Model model) {
		this.model = model;
	}
	public ModelNode getRoot() {
		return root;
	}
	public void setRoot(ModelNode root) {
		this.root = root;
	}
	public List getChildrenNodes() {
		return childrenNodes;
	}
	public void setChildrenNodes(List childrenNodes) {
		this.childrenNodes = childrenNodes;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	

}
