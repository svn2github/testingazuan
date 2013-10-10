package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.List;

import org.jgrapht.GraphPath;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PathChoice {
	GraphPath<IModelEntity, Relationship> path;
	private boolean active;
	private List<Relationship> relations;
	
	public PathChoice( List<Relationship> relations, boolean active) {
		super();
		this.relations = relations;
		this.active = active;
	}

	public PathChoice(GraphPath<IModelEntity, Relationship> path) {
		super();
		this.path = path;
		this.active =  false;
	}
	
	public String getStart(){
		return path.getStartVertex().getUniqueName();
	}

	public boolean isActive() {
		return active;
	}

	public String getEnd(){
		return path.getEndVertex().getUniqueName();
	}
	
	public List<Relationship> getNodes() {
		if(path!=null){
			return (path.getEdgeList());
		}
		return null;
	}

	@JsonIgnore
	public  List<Relationship> getRelations() {
		if(this.relations==null){
			this.relations = this.path.getEdgeList();
		}
		return relations;
	}

	
	
	
}
