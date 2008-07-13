/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.utilities.engines.EngineException;


public class ShowAnalysisDetailsAction extends AbstractGeoEngineAction {
	
	
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.utilities.engines.AbstractEngineAction#service(it.eng.spago.base.SourceBean, it.eng.spago.base.SourceBean)
	 */
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws EngineException  {
		
		super.service(serviceRequest, serviceResponse);
		
		String selectedHiearchy = (String)serviceRequest.getAttribute("selected_hierachy");
		String selectedHierarchyLevel = (String)serviceRequest.getAttribute("selected_hierarchy_level");
		String selectedMap = (String)serviceRequest.getAttribute("selected_map");
		String selectedLayers = (String)serviceRequest.getAttribute("selected_layers");
		selectedLayers = selectedLayers.replaceAll(";", ",");
		
		String data = "";
		data += "selected_hierachy=" + selectedHiearchy + ";";
		data += "selected_hierarchy_level=" + selectedHierarchyLevel + ";";
		data += "selected_map=" + selectedMap + ";";
		data += "selected_layers=" + selectedLayers + ";";
		
		GeoEngineAnalysisState analysisState = (GeoEngineAnalysisState)getEngineInstance().getAnalysisState();
		analysisState.load( data.getBytes() );				
	}
	

}