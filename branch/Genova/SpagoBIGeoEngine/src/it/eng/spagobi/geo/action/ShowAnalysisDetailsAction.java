/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

/**
 * Spago Action which executes the map producing request  
 */
public class ShowAnalysisDetailsAction extends AbstractHttpAction {
	
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		GeoAction.SubObjectDetails subObjectDetails = (GeoAction.SubObjectDetails)getRequestContainer().getSessionContainer().getAttribute("SUBOBJECT");
		
		
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
		
		
		subObjectDetails.setData(data.getBytes());
		
	}
	

}