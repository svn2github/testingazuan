/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;

public class DefaultCover {

	public static void applyDefault(Set<ModelFieldPaths> ambiguousModelField,  UndirectedGraph<IModelEntity, Relationship> rootEntitiesGraph, Set<IModelEntity> entities){
		IDefaultCoverGraph sp = new ShortestPathsCoverGraph();
		Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship>>> defaultConnections =   sp.getConnectingRelatiosnhips(rootEntitiesGraph, entities);	
		DefaultCover.applyDefault(defaultConnections, ambiguousModelField);
	}
	
	public static QueryGraph getCoverGraph(UndirectedGraph<IModelEntity, Relationship> rootEntitiesGraph, Set<IModelEntity> entities){
		IDefaultCoverGraph sp = new ShortestPathsCoverGraph();
		return sp.getCoverGraph(rootEntitiesGraph, entities);
	}
	
	
	
	public static void applyDefault(Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship>>> defaultConnections, Set<ModelFieldPaths> ambiguousModelField){			
		if(ambiguousModelField!=null && defaultConnections!=null){
			Iterator<ModelFieldPaths> mfpIter = ambiguousModelField.iterator();
			while (mfpIter.hasNext()) {
				ModelFieldPaths modelFieldPaths = (ModelFieldPaths) mfpIter.next();
				IModelEntity entity = modelFieldPaths.getModelEntity();
				if(modelFieldPaths.getChoices()!=null){
					Iterator<PathChoice> amfpChoicesIter =modelFieldPaths.getChoices().iterator();
					while (amfpChoicesIter.hasNext()) {
						PathChoice pathChoice = (PathChoice) amfpChoicesIter.next();
						Set<GraphPath<IModelEntity, Relationship>> shortest = defaultConnections.get(entity);
						if(shortest!=null){
							Iterator<GraphPath<IModelEntity, Relationship>> pathIter = shortest.iterator();
							while (pathIter.hasNext()) {
								GraphPath<IModelEntity, Relationship> graphPath = (GraphPath<IModelEntity, Relationship>) pathIter.next();
								boolean activeChoice = pathChoice.isTheSamePath(graphPath);
								pathChoice.setActive(activeChoice);
								if(activeChoice){
									break;
								}
							}
						}
					}
				}
			}
		}
	}
	
}
