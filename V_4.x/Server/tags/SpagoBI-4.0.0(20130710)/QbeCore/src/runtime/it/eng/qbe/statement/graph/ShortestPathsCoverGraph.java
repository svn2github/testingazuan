package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;

public class ShortestPathsCoverGraph implements IDefaultCoverGraph{
	
	public static transient Logger logger = Logger.getLogger(ShortestPathsCoverGraph.class);
	
	public Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship>>>  getConnectingRelatiosnhips( UndirectedGraph<IModelEntity, DefaultEdge> rootEntitiesGraph, Set<IModelEntity> entities) {
		
		Iterator<IModelEntity> it = entities.iterator();
		IModelEntity entity = it.next();
		DijkstraShortestPath dsp = new DijkstraShortestPath(rootEntitiesGraph, entity, entities);
		
		GraphPath minimumPath = dsp.getPath();
		
		List<Relationship> relationships = (List<Relationship>)minimumPath.getEdgeList();
		QueryGraphBuilder qb = new QueryGraphBuilder();
		QueryGraph monimumGraph = qb.buildGraphFromEdges(relationships);
		PathInspector pi = new PathInspector(monimumGraph, monimumGraph.vertexSet());
		Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship> >> minimumPaths = pi.getAllEntitiesPathsMap();
		
		
		
		return minimumPaths;
	}
	

}
