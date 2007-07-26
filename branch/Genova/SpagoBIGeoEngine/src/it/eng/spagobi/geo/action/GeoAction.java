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
public class GeoAction extends AbstractHttpAction {
	
	public static final String MAP_CATALOGUE_MANAGER_URL = "mapCatalogueManagerUrl";

	private MapConfiguration getConfiguration(SourceBean serviceRequest) {
		MapConfiguration mapConfiguration = null;
		
		String templateBase64Coded = (String) serviceRequest.getAttribute(Constants.TEMPLATE_PARAMETER);
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] template = null;
		try{
			template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
        						"GeoAction :: service : " +
        						"Error while decoding base64 template", e);
			 return null;
		}
		
		// read the map configuration		
		try{
			String baseUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + getHttpRequest().getContextPath();
			
			
			mapConfiguration = new MapConfiguration(baseUrl, template, serviceRequest);
			
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
					"GeoAction :: service : " +
					"Error while reading map configuration", e);
			
			return null;
		}
		
		return mapConfiguration;
	}
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		HttpServletRequest request = this.getHttpRequest(); 
		
				
		// get the http response 
		HttpServletResponse response = this.getHttpResponse();
		//this.freezeHttpResponse();
		
		Properties props = new Properties();
		Enumeration enumer = request.getParameterNames();
		String parName = null;
		String parValue = null;
		while (enumer.hasMoreElements()) {
			parName = (String) enumer.nextElement();
			parValue = request.getParameter(parName);
			//System.out.println(parName + " = " + parValue);
			if(parName.equalsIgnoreCase("NEW_SESSION")) continue;
			if(parName.equalsIgnoreCase("ACTION_NAME")) continue;
			serviceResponse.setAttribute(parName, parValue);
			props.setProperty(parName, parValue);			
		}	
		
		
		
		//serviceResponse.setAttribute("mapCatalogueAccessUtils", mapCatalogueAccessUtils);
		String map_catalogue_manager_url = (String) serviceRequest.getAttribute(MAP_CATALOGUE_MANAGER_URL);
		MapCatalogueAccessUtils mapCatalogueAccessUtils = new MapCatalogueAccessUtils(map_catalogue_manager_url);
		MapConfiguration.setMapCatalogueAccessUtils(mapCatalogueAccessUtils);
		MapConfiguration mapConfiguration = getConfiguration(serviceRequest);
		mapConfiguration.getDatamartProviderConfiguration().setParameters(props);
		
		
		serviceResponse.setAttribute("configuration", mapConfiguration);
	}
	

}