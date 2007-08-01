/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
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
		
			
		
		String nameSubObject = (String)serviceRequest.getAttribute("nameSubObject");
		String descriptionSubObject = (String)serviceRequest.getAttribute("descriptionSubObject");
		String visibilitySubObject = (String)serviceRequest.getAttribute("visibilitySubObject");
		String subobjectdataBase64Coded = (String)serviceRequest.getAttribute("subobjectdata");
		
		String user = (String)serviceRequest.getAttribute("username");
		String spagobiurl = (String)serviceRequest.getAttribute("spagobiurl");
		String templatePath = (String)serviceRequest.getAttribute("templatePath");
		
		
		if(nameSubObject != null) serviceRequest.delAttribute("nameSubObject");
		if(descriptionSubObject != null) serviceRequest.delAttribute("descriptionSubObject");
		if(visibilitySubObject != null) serviceRequest.delAttribute("visibilitySubObject");
		Properties subObjectProperties = new Properties();
		byte[] subobjectdata = null;
		if(subobjectdataBase64Coded != null) {
			serviceRequest.delAttribute("subobjectdata");
		
			
			BASE64Decoder bASE64Decoder = new BASE64Decoder();
			try{
				subobjectdata = bASE64Decoder.decodeBuffer(subobjectdataBase64Coded);
				String data = new String(subobjectdata);
				String[] properties = data.split(";");
				for(int i = 0; i < properties.length; i++) {
					String[] propChunk = properties[i].split("=");
					String pName = propChunk[0];
					String pValue = propChunk[1];
					subObjectProperties.setProperty(pName, pValue);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}			
			
		}	
		
		SubObjectDetails subObjectDetails = new SubObjectDetails();
		subObjectDetails.setName(nameSubObject==null?"":nameSubObject);
		subObjectDetails.setDescription(descriptionSubObject==null?"":descriptionSubObject);
		subObjectDetails.setScope(visibilitySubObject==null?"":visibilitySubObject);
		subObjectDetails.setData(subobjectdata);
		subObjectDetails.setUser(user);
		subObjectDetails.setSpagobiurl(spagobiurl);
		subObjectDetails.setTemplatePath(templatePath);
		getRequestContainer().getSessionContainer().setAttribute("SUBOBJECT", subObjectDetails);	
		
		
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
		String selectedHiearchy = (String)subObjectProperties.getProperty("selected_hierachy");
		String selectedHierarchyLevel = (String)subObjectProperties.getProperty("selected_hierarchy_level");
		String selectedMap = (String)subObjectProperties.getProperty("selected_map");
		String selectedLayers = (String)subObjectProperties.getProperty("selected_layers");
	
		
		if(selectedHiearchy != null) mapConfiguration.getDatamartProviderConfiguration().setHierarchyName(selectedHiearchy);
		if(selectedHierarchyLevel != null) mapConfiguration.getDatamartProviderConfiguration().setHierarchyLevel(selectedHierarchyLevel);
		if(selectedMap != null) {
			mapConfiguration.setMapName(selectedMap);
			mapConfiguration.getMapProviderConfiguration().setMapName(selectedMap);
		}
		
		if(selectedLayers != null) serviceResponse.setAttribute("selectedLayers", selectedLayers.split(","));
		serviceResponse.setAttribute("configuration", mapConfiguration);
		
		RequestContainer requestContainer = getRequestContainer();
		SessionContainer sessionContainer = requestContainer.getSessionContainer();
		sessionContainer.setAttribute("CONFIGURATION", mapConfiguration);
	}
	
	
	public static class SubObjectDetails {
		String name;
		String description;
		String scope;
		byte[] data;
		
		String user;
		String spagobiurl;
		String templatePath;
		
		public SubObjectDetails() {
			name = "";
			description = "";
			scope = "Public";
			data = null;
		}
		
		public byte[] getData() {
			return data;
		}
		public void setData(byte[] data) {
			this.data = data;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getScope() {
			return scope;
		}
		public void setScope(String scope) {
			this.scope = scope;
		}

		public String getSpagobiurl() {
			return spagobiurl;
		}

		public void setSpagobiurl(String spagobiurl) {
			this.spagobiurl = spagobiurl;
		}

		public String getUser() {
			return user;
		}

		public void setUser(String user) {
			this.user = user;
		}

		public String getTemplatePath() {
			return templatePath;
		}

		public void setTemplatePath(String templatePath) {
			this.templatePath = templatePath;
		}
	}
	

}