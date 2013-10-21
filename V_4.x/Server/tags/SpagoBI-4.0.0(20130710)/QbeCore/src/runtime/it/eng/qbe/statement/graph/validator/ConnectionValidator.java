/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package it.eng.qbe.statement.graph.validator;


import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;

/**
 * This implementation of the Graph Validator check if the Graph is connected
 * 
 * 
 * @authors
 *  Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ConnectionValidator extends AbstractGraphValidator {

	/**
	 * Check if the graph is connected
	 */
	public boolean validate(UndirectedGraph G) {
		ConnectivityInspector inspector = new ConnectivityInspector(G);
		return inspector.isGraphConnected() ;
	}

}
