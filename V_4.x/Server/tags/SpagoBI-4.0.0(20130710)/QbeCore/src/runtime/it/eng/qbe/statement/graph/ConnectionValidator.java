/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;

/**
 * This implementation of the Graph Validator check if the Graph is connected
 * 
 * 
 * @authors
 *  Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ConnectionValidator implements IGraphValidator {

	/**
	 * Check if the graph is connected
	 */
	public boolean validate(UndirectedGraph<IModelEntity, DefaultEdge> G) {
		ConnectivityInspector<IModelEntity, DefaultEdge> inspector = new ConnectivityInspector<IModelEntity, DefaultEdge>(G);
		return inspector.isGraphConnected() ;
	}

}
