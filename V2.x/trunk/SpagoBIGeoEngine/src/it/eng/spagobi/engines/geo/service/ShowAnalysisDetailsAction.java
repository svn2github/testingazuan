/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.commons.service.AbstractGeoEngineAction;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.utilities.engines.EngineException;

/**
 * Spago Action which executes the map producing request  
 */
public class ShowAnalysisDetailsAction extends AbstractGeoEngineAction {
	
	
	
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
		
		GeoEngineAnalysisState analysisState = (GeoEngineAnalysisState)getAnalysisState();
		analysisState.setRowData( data.getBytes() );				
	}
	

}