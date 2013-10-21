/* SpagoBI, the Open Source Business Intelligence suite

 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice. 
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. */
package it.eng.qbe.statement.graph;

import it.eng.qbe.model.structure.IModelEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.UndirectedGraph;

public class GraphValidatorInspector {

	
	public static boolean isValid(UndirectedGraph G, Set<IModelEntity> unjoinedEntities){

		List<IGraphValidator> validators = getValidatorsList();
		
		// the empty graph is always valid
		if((unjoinedEntities==null || unjoinedEntities.size()==0) && (G==null || G.vertexSet().size()==0)){
			return true;
		}
		
		for(int i=0; i<validators.size(); i++){
			if(!validators.get(i).validate(G)){
				return false;
			}
		}
		
		return true;
		
	}
	
	private static List<IGraphValidator> getValidatorsList(){
		List<IGraphValidator> validators;
		validators = new ArrayList<IGraphValidator>();
		validators.add(new ConnectionValidator());
		return validators;
	}
	
}
