/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.Map;
import java.util.Set;

import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;

public interface IDefaultCoverGraph {

	
	public QueryGraph  getCoverGraph( UndirectedGraph<IModelEntity, Relationship> rootEntitiesGraph, Set<IModelEntity> entities);
	public Map<IModelEntity, Set<GraphPath<IModelEntity, Relationship> >>  getConnectingRelatiosnhips( UndirectedGraph<IModelEntity, Relationship> rootEntitiesGraph, Set<IModelEntity> entities);
	public void applyDefault(Set<ModelFieldPaths> ambiguousModelField,  UndirectedGraph<IModelEntity, Relationship> rootEntitiesGraph, Set<IModelEntity> entities);
}
