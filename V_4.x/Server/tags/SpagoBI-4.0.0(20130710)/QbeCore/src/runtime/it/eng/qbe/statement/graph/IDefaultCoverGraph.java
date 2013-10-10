package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.Map;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public interface IDefaultCoverGraph {

	public Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship> >>  getConnectingRelatiosnhips( UndirectedGraph<IModelEntity, DefaultEdge> rootEntitiesGraph, Set<IModelEntity> entities);
	
}
