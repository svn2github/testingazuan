package it.eng.qbe.graph;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.statement.graph.PathInspector;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultEdge;

public class AmbiguousPathsTestCase  extends AbstractGraphTestCase {
	

	
	
//	public void testAmbiguousPaths() {
//		
//		PathInspector pathInspector = new PathInspector(graph);
//		Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge>>> map = pathInspector.getAmbiguousEntitiesPathsMap();
//		assertEquals(map.keySet().size(), 3);
//		Iterator<IModelEntity> iter = map.keySet().iterator();
//		while(iter.hasNext()){
//			IModelEntity myMe = iter.next();
//			assertTrue(mappaths.containsKey(myMe));
//			Set<GraphPath<IModelEntity, DefaultEdge>> paths = map.get(myMe);
//			Set<GraphPath<IModelEntity, DefaultEdge>> paths2 = mappaths.get(myMe);
//			assertEquals(paths.size(), paths2.size());
//
//		}
//		
//		
//	}
	
	public void testAmbiguousPaths() {
		
		PathInspector pathInspector = new PathInspector(graph);
		Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge>>> map = pathInspector.getAmbiguousEntitiesAllPathsMap();
		//assertEquals(map.keySet().size(), 3);
		Iterator<IModelEntity> iter = map.keySet().iterator();
		while(iter.hasNext()){
			IModelEntity myMe = iter.next();
			assertTrue(mappaths.containsKey(myMe));
			Set<GraphPath<IModelEntity, DefaultEdge>> paths = map.get(myMe);
			Set<GraphPath<IModelEntity, DefaultEdge>> paths2 = mappaths.get(myMe);
		//	assertEquals(paths.size(), paths2.size());

		}
	}

}
