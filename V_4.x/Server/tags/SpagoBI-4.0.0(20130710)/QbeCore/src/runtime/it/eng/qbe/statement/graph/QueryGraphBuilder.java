/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph.Relationship;
import it.eng.spagobi.utilities.assertion.Assert;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;

/**
 * 
 * This class builds a graph starting from a set of paths 
 * 
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class QueryGraphBuilder {
	
	private static transient Logger logger = Logger.getLogger(QueryGraphBuilder.class);
	private List<IModelEntity> vertexes;
	
	public QueryGraphBuilder(){
		vertexes = new ArrayList<IModelEntity>();
	}
	
	public UndirectedGraph<IModelEntity, DefaultEdge> buildGraph(Collection<GraphPath<IModelEntity, DefaultEdge>> paths){
		logger.debug("IN");
		Assert.assertNotNull(paths, "The list of paths is null. Impossbile to create a graph");
		logger.debug("The number of paths is "+paths.size());
		
		UndirectedGraph<IModelEntity, DefaultEdge> graph = new Multigraph<IModelEntity, DefaultEdge>(Relationship.class);
		
		if(paths!=null){
			Iterator<GraphPath<IModelEntity, DefaultEdge>> pathIter = paths.iterator();
			while(pathIter.hasNext()){
				GraphPath<IModelEntity, DefaultEdge> path = pathIter.next();
				addPathToGraph(graph, path);
			}
		}

		logger.debug("OUT");
		return graph;
	}
	
	public void addPathToGraph(UndirectedGraph<IModelEntity, DefaultEdge> graph, GraphPath<IModelEntity, DefaultEdge> path ){
		logger.debug("IN");
		List<DefaultEdge> edges = path.getEdgeList();
		if(edges!=null){
			for(int i=0; i<edges.size(); i++){
				Relationship edge = (Relationship)edges.get(i);
				IModelEntity src= edge.getSourceEntity();
				IModelEntity target= edge.getTargetEntity();
				
				if(!vertexes.contains(src)){
					logger.debug("Add the vertex "+src.getName());
					vertexes.add(src);
					graph.addVertex(src);
				}
				if(!vertexes.contains(target)){
					logger.debug("Add the vertex "+src.getName());
					vertexes.add(target);
					graph.addVertex(target);
				}
				
				logger.debug("Add the edge "+src.getName()+"--"+target.getName());
				graph.addEdge(src, target, edge);
			}
		}
		logger.debug("OUT");
	}
	

	
}
