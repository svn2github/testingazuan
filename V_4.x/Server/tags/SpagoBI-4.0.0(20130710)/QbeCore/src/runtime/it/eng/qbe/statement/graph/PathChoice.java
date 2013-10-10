package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PathChoice {
	GraphPath<IModelEntity, DefaultEdge> path;
	private boolean active;
	private List<DefaultEdge> relations;
	
	public PathChoice( List<DefaultEdge> relations, boolean active) {
		super();
		this.relations = relations;
		this.active = active;
	}

	public PathChoice(GraphPath<IModelEntity, DefaultEdge> path) {
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
	
	public List<DefaultEdge> getNodes() {
		if(path!=null){
			return (path.getEdgeList());
		}
		return null;
	}

	@JsonIgnore
	public  List<DefaultEdge> getRelations() {
		if(this.relations==null){
			this.relations = this.path.getEdgeList();
		}
		return relations;
	}

	
	
	
}
