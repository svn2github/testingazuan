/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph.Relationship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DefaultEdge;

/**
 * 
 * Starting frm a connected graph this class manage all the path between vertex
 * 
 * @authors
 *  Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */
public class PathInspector {

	private static transient Logger logger = Logger.getLogger(PathInspector.class);
	private Graph<IModelEntity, DefaultEdge> graph;
	private static final int maxPathLength = 10;
	
	private Set<EntitiesPath> paths;
	private Set<GraphPath<IModelEntity, DefaultEdge>> allGraphPaths; // all the paths of the graph

	public PathInspector( Graph<IModelEntity, DefaultEdge> graph, Collection<IModelEntity> queryEntities){
		this.graph = graph;
		buildPaths(queryEntities);
	}

	/**
	 * Builds the set of paths between all the couple of vertexes
	 */
	private void buildPaths( Collection<IModelEntity> queryEntities){

		logger.debug("IN");

		allGraphPaths = new HashSet<GraphPath<IModelEntity, DefaultEdge>>();
		paths = new HashSet<EntitiesPath>();

		//build all the path between entities
		Iterator<IModelEntity> vertexesIter =  queryEntities.iterator();

		//First loop for the source of the path
		while(vertexesIter.hasNext()){
			IModelEntity startVertex = vertexesIter.next();
			logger.debug("Building the list of path starting from "+startVertex.getName());
			//inner loop for the target of the path
			Iterator<IModelEntity> vertexesInnerIter =  queryEntities.iterator();
			while(vertexesInnerIter.hasNext()){
				IModelEntity endVertex = vertexesInnerIter.next();
				if(!startVertex.equals(endVertex)){
					EntitiesPath entitiesPath = new EntitiesPath(startVertex, endVertex);
					if(!this.paths.contains(entitiesPath)){
						logger.debug("Building the list of path between the vertexes ["+startVertex.getName()+","+endVertex.getName()+"]");
						KShortestPaths<IModelEntity, DefaultEdge> kshortestPath = new KShortestPaths<IModelEntity, DefaultEdge>(graph,startVertex,maxPathLength );
						List<GraphPath<IModelEntity, DefaultEdge>> graphPaths = kshortestPath.getPaths(endVertex);
						
						//if there is at least one path between the 2 vertex
						if(graphPaths!=null){
							entitiesPath.setPaths(graphPaths);

							//updating the class variables	
							this.paths.add(entitiesPath);
							for(int i=0; i<graphPaths.size(); i++){
								GraphPath<IModelEntity, DefaultEdge> path = graphPaths.get(i);
								if(!containsPath(this.allGraphPaths, path)){
									this.allGraphPaths.add(path);
								}
							}							
						}
					}
				}
			}		
		}	
		logger.debug("OUT");
	}

	/**
	 * Checks if the path is already contained in the map
	 * @param map
	 * @param path
	 * @return
	 */
	private static boolean containsPath(Set<GraphPath<IModelEntity, DefaultEdge>> map, GraphPath<IModelEntity, DefaultEdge> path){
		logger.debug("IN");
		Iterator<GraphPath<IModelEntity, DefaultEdge>> iter = map.iterator();
		while(iter.hasNext()){
			GraphPath<IModelEntity, DefaultEdge> graphPath = iter.next();
			if(arePathsEquals(graphPath, path)){
				logger.debug("Adding the path in the map"+path.toString());
				return true;
			}
		}
		logger.debug("OUT");
		return false;
	}
	
	/**
	 * Checks if the 2 paths are equals
	 * @param path1
	 * @param path2
	 * @return
	 */
	private static boolean arePathsEquals(GraphPath<IModelEntity, DefaultEdge> path1, GraphPath<IModelEntity, DefaultEdge> path2){
		Set<String> relationsPath1 = getRelationsSet(path1);
		Set<String> relationsPath2 = getRelationsSet(path2);
		return relationsPath1.equals(relationsPath2);
	}
	
	private static Set<String> getRelationsSet(GraphPath<IModelEntity, DefaultEdge> path1){
		Set<String> relations = new HashSet<String>();
		List<DefaultEdge> edges =  path1.getEdgeList();
		for(int i=0; i<edges.size();i++){
			relations.add(((Relationship)edges.get(i)).getId());
		}
		return relations;
		
	}
	
	
	/**
	 * Gets the map where the key are ambiguous Entities and the values
	 * are all the paths that pass through the ambiguous entities 
	 * @return
	 */
	public  Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> getAmbiguousEntitiesAllPathsMap(){
		logger.debug("IN");
		Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> ambiguouPathsMap = new HashMap<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >>();
		Set<EntitiesPath> ambiguouPaths = getAmbiguousEntitiesPaths();
		if(ambiguouPaths!=null){
			Iterator<EntitiesPath> iter = ambiguouPaths.iterator();
		
			//add the paths to the ambiguous path map
			while(iter.hasNext()){
				EntitiesPath entitiesPath = iter.next();
				Set<GraphPath<IModelEntity, DefaultEdge>> pathsInvolvingStartEntity;
				if(!ambiguouPathsMap.containsKey(entitiesPath.getEndPoints().get(0))){
					pathsInvolvingStartEntity = getPathsOfAmbigousEntities(entitiesPath.getEndPoints().get(0));
					ambiguouPathsMap.put(entitiesPath.getEndPoints().get(0), pathsInvolvingStartEntity);
				}

				Set<GraphPath<IModelEntity, DefaultEdge>> pathsInvolvingEndEntity;
				if(!ambiguouPathsMap.containsKey(entitiesPath.getEndPoints().get(1))){
					pathsInvolvingEndEntity = getPathsOfAmbigousEntities(entitiesPath.getEndPoints().get(1));
					ambiguouPathsMap.put(entitiesPath.getEndPoints().get(1), pathsInvolvingEndEntity);
				}
			} 
		}
		logger.debug("OUT");
		return ambiguouPathsMap;
	}
	

	/**
	 * gets the ambiguous paths: we have an ambiguous path
	 * where there is more than one path that connects
	 * two entities.
	 * @return the ambiguous paths. If there is no ambiguous path the set is empty 
	 */
	private  Set<EntitiesPath> getAmbiguousEntitiesPaths(){
		logger.debug("IN: finding the ambiguous paths");
		Set<EntitiesPath> ambiguouPaths = new HashSet<EntitiesPath>();
		if(paths!=null){
			logger.debug("Checking if there is more than one path between all the connected entities");
			Iterator<EntitiesPath> iter = paths.iterator();

			while(iter.hasNext()){
				EntitiesPath path = iter.next();
				if(path.getPaths()!=null && path.getPaths().size()>1){
					ambiguouPaths.add(path);
				}
			} 
		}
		logger.debug("OUT");
		return ambiguouPaths;
	}
	
	/**
	 * 
	 * @param path
	 * @param vertex
	 * @return
	 */
	private boolean containsVertex(GraphPath<IModelEntity,DefaultEdge> path, IModelEntity vertex){
		List<DefaultEdge> edges = path.getEdgeList();
		for (int i = 0; i < edges.size(); i++) {
			Relationship r =(Relationship) edges.get(i);
			if(r.getSourceEntity().equals(vertex) || r.getTargetEntity().equals(vertex) ){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get all the paths passing from the passed vertex
	 * @param vertex 
	 * @return
	 */
	private Set<GraphPath<IModelEntity, DefaultEdge>> getPathsOfAmbigousEntities(IModelEntity vertex){
		logger.debug("IN");
		logger.debug("Getting all the paths passing through the vertex "+vertex.getName());
		Iterator<GraphPath<IModelEntity, DefaultEdge>> iter = getAllGraphPaths().iterator();
		Set<GraphPath<IModelEntity, DefaultEdge>> toReturn = new HashSet<GraphPath<IModelEntity,DefaultEdge>>();
		while(iter.hasNext()){
			GraphPath<IModelEntity, DefaultEdge> path = iter.next();
			if(containsVertex(path, vertex)){
				toReturn.add(path);
			}
		}
		logger.debug("The path passing through the vertex are "+toReturn.size());
		logger.debug("OUT");
		return toReturn;
	}


	public Set<GraphPath<IModelEntity, DefaultEdge>> getAllGraphPaths() {
		return allGraphPaths;
	}




	public static class EntitiesPath{

		/**
		 * This object is the list of paths that connect 2 vertex
		 */
		private IModelEntity source;
		private IModelEntity target;
		private List<GraphPath<IModelEntity, DefaultEdge> > paths;

		public EntitiesPath(IModelEntity source, IModelEntity target){
			this.source = source;
			this.target = target;
			paths = new ArrayList<GraphPath<IModelEntity, DefaultEdge> >();
		}

		public void addPath(GraphPath<IModelEntity, DefaultEdge> path){
			paths.add(path);
		}

		public List<GraphPath<IModelEntity, DefaultEdge>> getPaths() {
			return paths;
		}

		public void setPaths(List<GraphPath<IModelEntity, DefaultEdge>> paths) {
			this.paths = paths;
		}

		public List<IModelEntity> getEndPoints() {
			List<IModelEntity> vertexes = new ArrayList<IModelEntity>();
			vertexes.add(source);
			vertexes.add(target);
			return vertexes;
		}



		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((source == null) ? 0 : source.hashCode());
			result = prime * result
					+ ((target == null) ? 0 : target.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			EntitiesPath other = (EntitiesPath) obj;
			if (source == null) {
				if (other.source != null)
					return false;
			} else if (!source.equals(other.source))
				return false;
			if (target == null) {
				if (other.target != null)
					return false;
			} else if (!target.equals(other.target))
				return false;
			return true;
		}
	}
	
//	
//	
//	
//	
//	/**
//	 * Gets the map where the key are ambiguous Entities and the values
//	 * are the ambiguous paths that pass through the ambiguous entities 
//	 * @return
//	 */
//	private  Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> getOnlyAmbiguousEntitiesPathsMap(){
//		logger.debug("IN");
//		Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> ambiguouPathsMap = new HashMap<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >>();
//		Set<EntitiesPath> ambiguouPaths = getAmbiguousEntitiesPaths();
//		if(ambiguouPaths!=null){
//			Iterator<EntitiesPath> iter = ambiguouPaths.iterator();
//			while(iter.hasNext()){
//				EntitiesPath path = iter.next();
//				addEntitiesPath(ambiguouPathsMap, path);
//				ambiguouPaths.add(path);
//			} 
//		}
//		logger.debug("OUT");
//		return ambiguouPathsMap;
//	}
//
//	/**
//	 * Gets the map where the key are ambiguous Entities and the values
//	 * are all the paths that pass through the ambiguous entities 
//	 * @return
//	 */
//	private  Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> getAmbiguousEntitiesPathsMap(){
//		logger.debug("IN");
//		Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> ambiguouPathsMap = new HashMap<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >>();
//		Set<EntitiesPath> ambiguouPaths = getAmbiguousEntitiesPaths();
//		if(ambiguouPaths!=null){
//			Iterator<EntitiesPath> iter = ambiguouPaths.iterator();
//			while(iter.hasNext()){
//				EntitiesPath entitiesPath = iter.next();
//				
//				Set<GraphPath<IModelEntity, DefaultEdge>> pathsInvolvingStartEntity;
//				if(!ambiguouPathsMap.containsKey(entitiesPath.getSource())){
//					pathsInvolvingStartEntity = new HashSet<GraphPath<IModelEntity,DefaultEdge>>();
//					ambiguouPathsMap.put(entitiesPath.getSource(), pathsInvolvingStartEntity);
//				}else{
//					pathsInvolvingStartEntity = ambiguouPathsMap.get(entitiesPath.getSource());
//				}
//
//				Set<GraphPath<IModelEntity, DefaultEdge>> pathsInvolvingEndEntity;
//				if(!ambiguouPathsMap.containsKey(entitiesPath.getTarget())){
//					pathsInvolvingEndEntity = new HashSet<GraphPath<IModelEntity,DefaultEdge>>();
//					ambiguouPathsMap.put(entitiesPath.getTarget(), pathsInvolvingEndEntity);
//				}else{
//					pathsInvolvingEndEntity = ambiguouPathsMap.get(entitiesPath.getTarget());
//				}
//
//				pathsInvolvingStartEntity.addAll(getPathsInvolvingEntityMap().get(entitiesPath.getSource()));
//				pathsInvolvingEndEntity.addAll(getPathsInvolvingEntityMap().get(entitiesPath.getTarget()));
//				
//			} 
//		}
//		logger.debug("OUT");
//		return ambiguouPathsMap;
//	}

}
