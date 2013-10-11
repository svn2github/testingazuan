package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.Set;

import org.jgrapht.UndirectedGraph;

public class DefaultCover {

	public static void applyDefault(Set<ModelFieldPaths> ambiguousModelField,  UndirectedGraph<IModelEntity, Relationship> rootEntitiesGraph, Set<IModelEntity> entities){
		IDefaultCoverGraph sp = new ShortestPathsCoverGraph();
		sp.applyDefault(ambiguousModelField, rootEntitiesGraph, entities);
	}
}
