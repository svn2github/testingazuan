package it.eng.qbe.graph;

import java.util.ArrayList;
import java.util.List;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph.Relationship;
import it.eng.qbe.statement.graph.PathInspector;
import it.eng.qbe.statement.graph.QueryGraphBuilder;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphPathImpl;

public class PathInspectorTestCase  extends AbstractGraphTestCase {
	

	
	
	public void testBuildGraph() {

		Relationship r3 = new Relationship();
		r3.setSourceFields(entities.get(0).getAllFields());
		r3.setTargetFields(entities.get(2).getAllFields());
		List<DefaultEdge> edges = new ArrayList<DefaultEdge>();
		edges.add(r3);
		
		List<GraphPath<IModelEntity, DefaultEdge>> paths = new ArrayList<GraphPath<IModelEntity, DefaultEdge>>();
		GraphPath<IModelEntity, DefaultEdge> path = new GraphPathImpl<IModelEntity, DefaultEdge>(graph, entities.get(0), entities.get(2), edges, 9.0);
		paths.add(path);
		
		
		QueryGraphBuilder qgb = new QueryGraphBuilder();
		UndirectedGraph<IModelEntity, DefaultEdge> graph = qgb.buildGraph(paths);
		
	}
	
	public void testPathInspectorEBuildGraph() {
		
		PathInspector pathInspector = new PathInspector(graph);
		QueryGraphBuilder qgb = new QueryGraphBuilder();
		Graph<IModelEntity, DefaultEdge> graph = qgb.buildGraph(pathInspector.getAllGraphPaths());
	}

}
