package it.eng.qbe.graph;

import java.util.ArrayList;
import java.util.List;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.statement.graph.PathInspector;
import it.eng.qbe.statement.graph.QueryGraphBuilder;
import it.eng.qbe.statement.graph.Relationship;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.GraphPathImpl;

public class PathInspectorTestCase  extends AbstractGraphTestCase {
	

	
	
	public void testBuildGraph() {

		Relationship r3 = new Relationship();
		r3.setSourceFields(entities.get(0).getAllFields());
		r3.setTargetFields(entities.get(2).getAllFields());
		List<Relationship> edges = new ArrayList<Relationship>();
		edges.add(r3);
		
		List<GraphPath<IModelEntity, Relationship>> paths = new ArrayList<GraphPath<IModelEntity, Relationship>>();
		GraphPath<IModelEntity, Relationship> path = new GraphPathImpl<IModelEntity, Relationship>(graph, entities.get(0), entities.get(2), edges, 9.0);
		paths.add(path);
		
		
		QueryGraphBuilder qgb = new QueryGraphBuilder();
	//	UndirectedGraph<IModelEntity, Relationship> graph = qgb.buildGraph(paths);
		
	}
	
	public void testPathInspectorEBuildGraph() {
		
		PathInspector pathInspector = new PathInspector(graph, entities);
		QueryGraphBuilder qgb = new QueryGraphBuilder();
		//Graph<IModelEntity, Relationship> graph = qgb.buildGraph(pathInspector.getAllGraphPaths());
	}

}
