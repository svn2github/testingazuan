package it.eng.qbe.graph;

import it.eng.qbe.datasource.AbstractDataSourceTestCase;
import it.eng.qbe.datasource.DriverManager;
import it.eng.qbe.datasource.configuration.FileDataSourceConfiguration;
import it.eng.qbe.datasource.configuration.IDataSourceConfiguration;
import it.eng.qbe.datasource.jpa.JPADriver;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelField;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph.Relationship;
import it.eng.qbe.statement.graph.QueryGraphBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.GraphPathImpl;
import org.jgrapht.graph.Multigraph;

public class QueryGraphTestCase  extends AbstractDataSourceTestCase {
	
	IModelStructure modelStructure;
	private static final String QBE_FILE = "test-resources/jpa/jpaImpl/eclipselink/datamart.jar";
	

	
	@Override
	protected void setUpDataSource() {
		IDataSourceConfiguration configuration;
		
		modelName = "foodmart"; 
		
		File file = new File(QBE_FILE);
		configuration = new FileDataSourceConfiguration(modelName, file);
		configuration.loadDataSourceProperties().put("connection", connection);
		dataSource = DriverManager.getDataSource(JPADriver.DRIVER_ID, configuration);
		
		modelStructure = dataSource.getModelStructure();
	}
	
	public void testDoTests() {
		QueryGraphBuilder qgb = new QueryGraphBuilder();
		
		Iterator<IModelEntity> meIter = modelStructure.getRootEntityIterator("foodmart");
		
		IModelEntity me = meIter.next();
		IModelField mf = me.getAllFields().get(0);
		List<IModelField> fields = new ArrayList<IModelField>();
		fields.add(mf);
		
		IModelEntity me2 =  meIter.next();
		IModelField mf1 =  me2.getAllFields().get(0);
		List<IModelField> fields2 = new ArrayList<IModelField>();
		fields2.add(mf1);
		
		Relationship r = new Relationship();
		r.setSourceFields(fields);
		r.setTargetFields(fields2);
		
		
		Multigraph<IModelEntity, DefaultEdge> g = new Multigraph<IModelEntity, DefaultEdge>(Relationship.class);
		g.addVertex(me);
		g.addVertex(me2);
		g.addEdge(me, me2, r);
		
		List<DefaultEdge> edges = new ArrayList<DefaultEdge>();
		edges.add(r);
		
		modelStructure.addEntity(me);
		modelStructure.addEntity(me2);
		
		List<GraphPath<IModelEntity, DefaultEdge>> paths = new ArrayList<GraphPath<IModelEntity, DefaultEdge>>();
		GraphPath<IModelEntity, DefaultEdge> path = new GraphPathImpl<IModelEntity, DefaultEdge>(g, me, me2, edges, 9.0);
		paths.add(path);
		
		UndirectedGraph<IModelEntity, DefaultEdge> graph = qgb.buildGraph(paths);
		
	}

}
