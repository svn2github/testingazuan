package it.eng.spagobi.kpi.bo;

import java.util.List;

public class ModelInstance {
	
	Model model = null;//Model to which this instance refers
	ModelNode root =null; //root of the tree made of ModelInstanceNodes, representing the model
	List childrenNodes = null;//List of ModelInstanceNodes children
	String name = null;//name of the complete model instance(like "my own CMMI")
	String description = null;//description of the complete model instance

}
