/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.graph;

import it.eng.qbe.statement.graph.cover.IDefaultCoverGraph;
import it.eng.qbe.statement.graph.cover.ShortestPathsCoverGraph;
import it.eng.qbe.statement.graph.validator.ConnectionValidator;
import it.eng.qbe.statement.graph.validator.IGraphValidator;

import org.apache.log4j.Logger;

/**
 * 
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 * 
 * Central manager of the graphs
 *
 */
public class GraphManager {
	
	private static IDefaultCoverGraph defaultCoverGraph;
	private static IGraphValidator validator;
	private static transient Logger logger = Logger.getLogger(GraphManager.class);
	
	public synchronized static IDefaultCoverGraph getDefaultCoverGraphInstance(String className) {
		
		try{
			if (defaultCoverGraph == null)
				defaultCoverGraph = (IDefaultCoverGraph)Class.forName(className).newInstance();
		}catch(Exception e) {
			logger.debug("Impossible to load cover graph instance. The IDefaultCoverGraph implementation should be defined in the qbe.xml. The property is QBE.GRAPH-PATH.defaultCoverImpl. Using the default one.",e);
			defaultCoverGraph = new ShortestPathsCoverGraph();
		}
		return defaultCoverGraph;
	}

	public synchronized static IGraphValidator getGraphValidatorInstance(String className) {
		
		try{
			if (validator == null)
				validator = (IGraphValidator)Class.forName(className).newInstance();
		}catch(Exception e) {
			logger.debug("Impossible to load validator instance. The IGraphValidator implementation should be defined in the qbe.xml. The property is QBE.GRAPH-PATH.graphValidatorImpl. Using the default one.",e);
			validator = new ConnectionValidator();
		}
		return validator;
	}
}
