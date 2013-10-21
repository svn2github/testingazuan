/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.graph;

import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.model.structure.IModelEntity;
import it.eng.qbe.model.structure.IModelStructure;
import it.eng.qbe.model.structure.ModelStructure.RootEntitiesGraph;
import it.eng.qbe.query.Query;
import it.eng.qbe.statement.graph.bean.PathChoice;
import it.eng.qbe.statement.graph.bean.PathChoicePathTextLengthComparator;
import it.eng.qbe.statement.graph.bean.QueryGraph;
import it.eng.qbe.statement.graph.bean.Relationship;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.jgrapht.GraphPath;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GraphUtilities {
	
	public static final String RELATIONSHIP_ID = "relationshipId";
	
	/**
	 * Removes the subpaths
	 * @param ambiguousModelField
	 * @param sort
	 */
	public static void cleanSubPaths( Set<ModelFieldPaths> ambiguousModelField, String orderDirection){
		StringComparator stringComparator = new StringComparator();
		
		
		if(ambiguousModelField!=null){
			Iterator<ModelFieldPaths> iter = ambiguousModelField.iterator();
			while (iter.hasNext()) {
				List<String> listRelations = new ArrayList<String>();
				ModelFieldPaths modelFieldPaths = (ModelFieldPaths) iter.next();
				Set<PathChoice> pathChoices = modelFieldPaths.getChoices();
				
				if(pathChoices!=null){
					
					Set<PathChoice> pathChoicesFiltered;
					if(orderDirection != null){
						PathChoicePathTextLengthComparator pathChoiceComparator = new PathChoicePathTextLengthComparator(orderDirection);
						pathChoicesFiltered= new TreeSet(pathChoiceComparator);	
					}else{
						pathChoicesFiltered= new HashSet<PathChoice>();
					}
					Iterator<PathChoice> pathChoicesIter = pathChoices.iterator();
					
					while (pathChoicesIter.hasNext()) {
						PathChoice pathChoice2 = (PathChoice) pathChoicesIter.next();
						String relation = pathChoice2.getRelations().toString();
						listRelations.add(relation);
					}
					
					Collections.sort(listRelations, stringComparator);
					
					
					pathChoicesIter = pathChoices.iterator();
					while (pathChoicesIter.hasNext()) {
						PathChoice pathChoice2 = (PathChoice) pathChoicesIter.next();
						String relation = pathChoice2.getRelations().toString();
						if(!isSubPath(relation,listRelations)){
							pathChoicesFiltered.add(pathChoice2);
						}
					}
	
					modelFieldPaths.setChoices(pathChoicesFiltered);
				}
				
			}
		}
	}
	

	
	
	private static class StringComparator implements Comparator<String>{

		public int compare(String arg0, String arg1) {
			if(arg0==null){
				return -1;
			}
			if(arg1==null){
				return 1;
			}
			if(arg0.length()!=arg1.length()){
				return arg0.length()-arg1.length();
			}
			return arg0.compareTo(arg1);
		}
		
	}
	
	

	
	
	public static QueryGraph deserializeGraph(JSONArray relationshipsJSON, Query query, IDataSource dataSource) throws JSONException {
		if (relationshipsJSON == null || relationshipsJSON.length() == 0) {
			return null;
		}
		String modelName = dataSource.getConfiguration().getModelName();
		IModelStructure modelStructure =dataSource.getModelStructure();
		RootEntitiesGraph rootEntitiesGraph  = modelStructure.getRootEntitiesGraph(modelName, true);
		
		Set<String> relationshipsNames = new HashSet<String>();
		Set<PathChoice> choices = new HashSet<PathChoice>();
		for (int i = 0; i < relationshipsJSON.length(); i++) {
			JSONObject relationshipJSON = relationshipsJSON.getJSONObject(i);
			relationshipsNames.add(relationshipJSON.getString(RELATIONSHIP_ID));
		}
		
		List<Relationship> queryRelationships = new ArrayList<Relationship>();
		Set<Relationship> allRelationships = rootEntitiesGraph.getRelationships();
		Iterator<String> it = relationshipsNames.iterator();
		while (it.hasNext()) {
			String id = it.next();
			Relationship relationship = getRelationshipById(allRelationships, id);
			queryRelationships.add(relationship);
		}
		
		QueryGraphBuilder builder = new QueryGraphBuilder();
		QueryGraph queryGraph = builder.buildGraphFromEdges(queryRelationships);
		
		return queryGraph;
	}
	
	private static Relationship getRelationshipById(
			Set<Relationship> allRelationships, String id) {
		Iterator<Relationship> it = allRelationships.iterator();
		while (it.hasNext()) {
			Relationship relationship = it.next();
			if (relationship.getId().equals(id)) {
				return relationship;
			}
		}
		return null;
	}
	
	
	/**
	 * Checks if the 2 paths are equals
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static boolean arePathsEquals(GraphPath<IModelEntity, Relationship> path1, GraphPath<IModelEntity, Relationship> path2){
		Set<String> relationsPath1 = getRelationsSet(path1);
		Set<String> relationsPath2 = getRelationsSet(path2);
		return relationsPath1.equals(relationsPath2);
	}

	private static Set<String> getRelationsSet(GraphPath<IModelEntity, Relationship> path1){
		Set<String> relations = new HashSet<String>();
		List<Relationship> edges =  path1.getEdgeList();
		for(int i=0; i<edges.size();i++){
			relations.add(((Relationship)edges.get(i)).getId());
		}
		return relations;

	}
	
	/**
	 * Checks if the pathChoice1 is a subpath of pathChoice2
	 * @param pathChoice1
	 * @param pathChoice2
	 * @return
	 */
	public static boolean isSubPath(PathChoice pathChoice1, PathChoice pathChoice2){
		
		String path1 = pathChoice1.getRelations().toString();
		String path2 = pathChoice2.getRelations().toString();
		
		return isSubPath(path1, path2);
	}
	
	
	/**
	 * Checks if the string relations is a subpath of at least one of the paths in listRelations
	 * @param relations
	 * @param listRelations
	 * @return
	 */
	public static boolean isSubPath(String relations, List<String> listRelations){
		for(int i=listRelations.size()-1; i>0; i--){
			String aRelation = (listRelations.get(i));
			if( isSubPath(relations, aRelation) ){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Checks if the list of relations in the path1 is a subpath of path2
	 * @param path1
	 * @param path2
	 * @return
	 */
	public static boolean isSubPath(String path1, String path2){
		path1 = path1.substring(1, path1.length()-1);
		path2 = path2.substring(1, path2.length()-1);
			
		if(path2.length()>path1.length() && path2.indexOf(path1)>=0){
			return true;
		}
		return false;
	}
	
}
