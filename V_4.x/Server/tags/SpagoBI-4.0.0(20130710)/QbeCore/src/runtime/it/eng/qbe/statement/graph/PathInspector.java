/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph.Relationship;

import java.util.ArrayList;
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
	private Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> pathsInvolvingEntityMap;
	private Set<GraphPath<IModelEntity, DefaultEdge>> allGraphPaths; // all the paths of the graph

	public PathInspector( Graph<IModelEntity, DefaultEdge> graph){
		this.graph = graph;

		buildPaths();
	}

	/**
	 * Builds the set of paths between all the couple of vertexes
	 */
	private void buildPaths(){

		logger.debug("IN");

		pathsInvolvingEntityMap = new HashMap<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >>();
		paths = new HashSet<EntitiesPath>();

		//build all the path between entities
		Iterator<IModelEntity> vertexesIter =  this.graph.vertexSet().iterator();

		//First loop for the source of the path
		while(vertexesIter.hasNext()){
			IModelEntity startVertex = vertexesIter.next();
			logger.debug("Building the list of path starting from "+startVertex.getName());
			//inner loop for the target of the path
			Iterator<IModelEntity> vertexesInnerIter =  this.graph.vertexSet().iterator();
			while(vertexesInnerIter.hasNext()){
				IModelEntity endVertex = vertexesInnerIter.next();
				if(startVertex!=endVertex){
					EntitiesPath entitiesPath = new EntitiesPath(startVertex, endVertex);
					if(!this.paths.contains(entitiesPath)){
						logger.debug("Building the list of path between the vertexes ["+startVertex.getName()+","+endVertex.getName()+"]");
						KShortestPaths<IModelEntity, DefaultEdge> kshortestPath = new KShortestPaths<IModelEntity, DefaultEdge>(graph,startVertex,maxPathLength );
						List<GraphPath<IModelEntity, DefaultEdge>> graphPaths = kshortestPath.getPaths(endVertex);

						//if there is at least one path between the 2 vertex
						if(graphPaths!=null){
							entitiesPath.setPaths(graphPaths);

							//updating the class variables	
							addEntitiesPath(entitiesPath);
						}
					}
				}
			}		
		}	

		logger.debug("OUT");
	}

	/**
	 * gets the ambiguous paths: we have an ambiguous path
	 * where there is more than one path that connects
	 * two entities.
	 * @return the ambiguous paths. If there is no ambiguous path the set is empty 
	 */
	public  Set<EntitiesPath> getAmbiguousEntitiesPaths(){
		logger.debug("IN: finding the ambiguous paths");
		Set<EntitiesPath> ambiguouPaths = new HashSet<EntitiesPath>();
		if(pathsInvolvingEntityMap!=null){
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
				if(!ambiguouPathsMap.containsKey(entitiesPath.getSource())){
					pathsInvolvingStartEntity = getPathsOfAmbigousEntities(entitiesPath.getSource());
					ambiguouPathsMap.put(entitiesPath.getSource(), pathsInvolvingStartEntity);
				}

				Set<GraphPath<IModelEntity, DefaultEdge>> pathsInvolvingEndEntity;
				if(!ambiguouPathsMap.containsKey(entitiesPath.getTarget())){
					pathsInvolvingEndEntity = getPathsOfAmbigousEntities(entitiesPath.getTarget());
					ambiguouPathsMap.put(entitiesPath.getTarget(), pathsInvolvingEndEntity);
				}
			} 
		}
		logger.debug("OUT");
		return ambiguouPathsMap;
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
	
	/**
	 * Add the entitiesPath to the internal structures
	 * @param entitiesPath
	 */
	public void addEntitiesPath(EntitiesPath entitiesPath){
		this.paths.add(entitiesPath);
		addEntitiesPath(pathsInvolvingEntityMap, entitiesPath);
	}

	/**
	 * Add the entitiesPath to the internal map "map"
	 * @param map
	 * @param entitiesPath
	 */
	public void addEntitiesPath(Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> map, EntitiesPath entitiesPath){

		Set<GraphPath<IModelEntity, DefaultEdge>> pathsInvolvingStartEntity;
		if(!map.containsKey(entitiesPath.getSource())){
			pathsInvolvingStartEntity = new HashSet<GraphPath<IModelEntity,DefaultEdge>>();
			map.put(entitiesPath.getSource(), pathsInvolvingStartEntity);
		}else{
			pathsInvolvingStartEntity = map.get(entitiesPath.getSource());
		}

		Set<GraphPath<IModelEntity, DefaultEdge>> pathsInvolvingEndEntity;
		if(!map.containsKey(entitiesPath.getTarget())){
			pathsInvolvingEndEntity = new HashSet<GraphPath<IModelEntity,DefaultEdge>>();
			map.put(entitiesPath.getTarget(), pathsInvolvingEndEntity);
		}else{
			pathsInvolvingEndEntity = map.get(entitiesPath.getTarget());
		}

		pathsInvolvingStartEntity.addAll(entitiesPath.getPaths());
		pathsInvolvingEndEntity.addAll(entitiesPath.getPaths());

	}

	public Map<IModelEntity, Set<GraphPath<IModelEntity, DefaultEdge> >> getPathsInvolvingEntityMap(){
		return pathsInvolvingEntityMap;
	}

	public Set<GraphPath<IModelEntity, DefaultEdge>> getAllGraphPaths() {
		return getAllGraphPaths(false);
	}

	public Set<GraphPath<IModelEntity, DefaultEdge>> getAllGraphPaths(boolean refresh) {
		
		if(pathsInvolvingEntityMap!=null && (refresh || allGraphPaths==null )){
			allGraphPaths = new HashSet<GraphPath<IModelEntity, DefaultEdge>>();
			Iterator<Set<GraphPath<IModelEntity, DefaultEdge>>> iter = pathsInvolvingEntityMap.values().iterator();

			while(iter.hasNext()){
				allGraphPaths.addAll(iter.next());
			}
		}
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

		public IModelEntity getSource() {
			return source;
		}

		public IModelEntity getTarget() {
			return target;
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
