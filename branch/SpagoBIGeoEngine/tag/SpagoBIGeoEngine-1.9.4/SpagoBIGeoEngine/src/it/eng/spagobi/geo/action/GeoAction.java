/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.action;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.geo.configuration.Constants;
import it.eng.spagobi.geo.configuration.MapConfiguration;
import it.eng.spagobi.geo.configuration.MapRendererConfiguration;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

/**
 * Spago Action which executes the map producing request  
 */
public class GeoAction extends AbstractHttpAction {
	
	public static final String INPUT_PAR_USER = "username";	
	public static final String INPUT_PAR_SPAGOBI_URL = "spagobiurl";	
	public static final String INPUT_PAR_MAP_CATALOGUE_MANAGER_URL = "mapCatalogueManagerUrl";	
	public static final String INPUT_PAR_TEMPLATE = "Template";	
	public static final String INPUT_PAR_TEMPLATE_PATH = "templatePath";
	
	public static final String INPUT_PAR_SO_FUNC_ACTIVE = "isSaveSubObjFuncActive";	
	public static final String INPUT_PAR_SO_NAME = "nameSubObject";
	public static final String INPUT_PAR_SO_DESC = "descriptionSubObject";
	public static final String INPUT_PAR_SO_SCOPE = "visibilitySubObject";
	public static final String INPUT_PAR_SO_DATA = "subobjectdata";
	
	
	
	public static final String OUTPUT_PAR_SO_FLAG = "isSubObject";
	public static final String OUTPUT_PAR_SELECTED_LAYERS = "selectedLayers";
	public static final String OUTPUT_PAR_MAP_CONFIGURATION = "configuration";
	public static final String OUTPUT_PAR_BASE_URL = "baseUrl";
	public static final String OUTPUT_PAR_SO_FUNC_ACTIVE = INPUT_PAR_SO_FUNC_ACTIVE;	
	
	
	
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {
		String mapCatalogueManagerUrl = null;
		byte[] template = null;
		SubObjectDetails subObjectDetails = null;
		String baseUrl = null;
		String selectedLayers = null;
		String user = null;
		String spagobiurl = null;
		String templatePath = null;
		String isSaveSubObjFuncActive = null;
		
		
		
		mapCatalogueManagerUrl = (String) serviceRequest.getAttribute(INPUT_PAR_MAP_CATALOGUE_MANAGER_URL);
		user = (String)serviceRequest.getAttribute(INPUT_PAR_USER);
		spagobiurl = (String)serviceRequest.getAttribute(INPUT_PAR_SPAGOBI_URL);
		templatePath = (String)serviceRequest.getAttribute(INPUT_PAR_TEMPLATE_PATH);
		isSaveSubObjFuncActive = (String)serviceRequest.getAttribute(INPUT_PAR_SO_FUNC_ACTIVE);
		if(isSaveSubObjFuncActive == null) isSaveSubObjFuncActive = "TRUE";
		
		template = getTemplate(serviceRequest);
		String templateStr = new String(template);
		System.out.println("----------------------\n" + templateStr);
		subObjectDetails = getSubObjectDetails(serviceRequest, true);
		
		
		MapCatalogueAccessUtils mapCatalogueAccessUtils = new MapCatalogueAccessUtils(mapCatalogueManagerUrl);
		MapConfiguration.setMapCatalogueAccessUtils(mapCatalogueAccessUtils);
		MapConfiguration mapConfiguration = null;
		
		// read the map configuration		
		try{
			baseUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + getHttpRequest().getContextPath();			
			mapConfiguration = new MapConfiguration(baseUrl, template, serviceRequest);			
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
					"GeoAction :: service : " +
					"Error while reading map configuration", e);
		}
		
		if(subObjectDetails != null) {
			
			Properties subObjectProperties = subObjectDetails.getSubobjectProperties();
			
			String selectedHiearchy = (String)subObjectProperties.getProperty("selected_hierachy");
			String selectedHierarchyLevel = (String)subObjectProperties.getProperty("selected_hierarchy_level");
			String selectedMap = (String)subObjectProperties.getProperty("selected_map");
			selectedLayers = (String)subObjectProperties.getProperty("selected_layers");
			
			if(selectedHiearchy != null) mapConfiguration.getDatamartProviderConfiguration().setHierarchyName(selectedHiearchy);
			if(selectedHierarchyLevel != null) mapConfiguration.getDatamartProviderConfiguration().setHierarchyLevel(selectedHierarchyLevel);
			if(selectedMap != null) {
				mapConfiguration.setMapName(selectedMap);
				mapConfiguration.getMapProviderConfiguration().setMapName(selectedMap);
			}	
			if(selectedLayers != null) {
				String[] layers = selectedLayers.split(",");
				for(int i = 0; i < layers.length; i++) {
					MapRendererConfiguration.Layer layer = mapConfiguration.getMapRendererConfiguration().getLayer(layers[i]);
					if(layer != null) {
						layer.setSelected(true);
					} else {
						layer = new MapRendererConfiguration.Layer();
						layer.setName(layers[i]);
						layer.setDescription(layers[i]);
						layer.setSelected(true);
						mapConfiguration.getMapRendererConfiguration().addLayer(layer);
					}
				}
			}
			
			serviceResponse.setAttribute("isSubObject", "true");
		} else {
				
			
			subObjectDetails = new SubObjectDetails();
			subObjectDetails.setName("");
			subObjectDetails.setDescription("");
			subObjectDetails.setScope("");
			subObjectDetails.setData(null);
			subObjectDetails.setUser(user);
			subObjectDetails.setSpagobiurl(spagobiurl);
			subObjectDetails.setTemplatePath(templatePath);
		}
		
		
		Properties props = new Properties();
		Enumeration enumer = getHttpRequest().getParameterNames();
		String parName = null;
		String parValue = null;
		while (enumer.hasMoreElements()) {
			parName = (String) enumer.nextElement();
			parValue = getHttpRequest().getParameter(parName);
			if(parName.equalsIgnoreCase("NEW_SESSION")) continue;
			if(parName.equalsIgnoreCase("ACTION_NAME")) continue;
			parValue = decodeParameterValue(parValue);
			serviceResponse.setAttribute(parName, parValue);
			props.setProperty(parName, parValue);			
		}		
		
		mapConfiguration.getDatamartProviderConfiguration().setParameters(props);	
		
		
		if(selectedLayers != null) serviceResponse.setAttribute("selectedLayers", selectedLayers.split(","));
		serviceResponse.setAttribute("configuration", mapConfiguration);
		serviceResponse.setAttribute("baseUrl", baseUrl);
		
		//serviceResponse.setAttribute(OUTPUT_PAR_SO_FUNC_ACTIVE, isSaveSubObjFuncActive);
		
		getRequestContainer().getSessionContainer().setAttribute("SUBOBJECT", subObjectDetails);		
		getRequestContainer().getSessionContainer().setAttribute("CONFIGURATION", mapConfiguration);
	}
	
	private String decodeParameterValue(String parValue) {
		String newParValue;
			
		ParametersDecoder decoder = new ParametersDecoder();
		if(decoder.isMultiValues(parValue)) {			
			List values = decoder.decode(parValue);
			newParValue = "";
			for(int i = 0; i < values.size(); i++) {
				newParValue += (i>0?",":"");
				newParValue += values.get(i);
			}
		} else {
			newParValue = parValue;
		}
			
		return newParValue;
	}
	
	private SubObjectDetails getSubObjectDetails(SourceBean serviceRequest, boolean clearRequest) throws Exception {
		
		SubObjectDetails subObjectDetails = null;
		
		String nameSubObject = null;
		String descriptionSubObject = null;
		String visibilitySubObject = null;
		String subobjectdataBase64Coded =  null;
		byte[] subobjectdata = null;
		String user =  null;
		String spagobiurl =  null;
		String templatePath =  null;
		
		
		nameSubObject = (String)serviceRequest.getAttribute(INPUT_PAR_SO_NAME);
		descriptionSubObject = (String)serviceRequest.getAttribute(INPUT_PAR_SO_DESC);
		visibilitySubObject = (String)serviceRequest.getAttribute(INPUT_PAR_SO_SCOPE);
		subobjectdataBase64Coded = (String)serviceRequest.getAttribute(INPUT_PAR_SO_DATA);		
		user = (String)serviceRequest.getAttribute(INPUT_PAR_USER);
		spagobiurl = (String)serviceRequest.getAttribute(INPUT_PAR_SPAGOBI_URL);
		templatePath = (String)serviceRequest.getAttribute(INPUT_PAR_TEMPLATE_PATH);		
		
		if(clearRequest) {
			if(nameSubObject != null) serviceRequest.delAttribute(INPUT_PAR_SO_NAME);
			if(descriptionSubObject != null) serviceRequest.delAttribute(INPUT_PAR_SO_DESC);
			if(visibilitySubObject != null) serviceRequest.delAttribute(INPUT_PAR_SO_SCOPE);
			if(subobjectdataBase64Coded != null) serviceRequest.delAttribute(INPUT_PAR_SO_DATA);
			if(user != null) serviceRequest.delAttribute(INPUT_PAR_USER);
			if(spagobiurl != null) serviceRequest.delAttribute(INPUT_PAR_SPAGOBI_URL);
			if(templatePath != null) serviceRequest.delAttribute(INPUT_PAR_TEMPLATE_PATH);
		}				
		
		if(subobjectdataBase64Coded != null) {
			BASE64Decoder bASE64Decoder = new BASE64Decoder();			
			subobjectdata = bASE64Decoder.decodeBuffer(subobjectdataBase64Coded);
		}
		
		if(nameSubObject != null) {
			subObjectDetails = new SubObjectDetails();
			subObjectDetails.setName(nameSubObject==null?"":nameSubObject);
			subObjectDetails.setDescription(descriptionSubObject==null?"":descriptionSubObject);
			subObjectDetails.setScope(visibilitySubObject==null?"":visibilitySubObject);
			subObjectDetails.setData(subobjectdata);
			subObjectDetails.setUser(user);
			subObjectDetails.setSpagobiurl(spagobiurl);
			subObjectDetails.setTemplatePath(templatePath);
		}
		
		return subObjectDetails;		
	}

	public byte[] getTemplate(SourceBean serviceRequest) {
		byte[] template = null;
		
		String templateBase64Coded = (String) serviceRequest.getAttribute(INPUT_PAR_TEMPLATE);
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		
		try{
			template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		} catch (Exception e) {
			TracerSingleton.log(Constants.LOG_NAME, TracerSingleton.MAJOR, 
        						"GeoAction :: service : " +
        						"Error while decoding base64 template", e);
			 return null;
		}
		
		return template;
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
			scope = "public";
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
		
		public Properties getSubobjectProperties() {
			Properties properties = new Properties();
			
			String str = null;
			String[] chuncks = null;
			
			if(data == null) return null;
			
			str = new String(data);
			chuncks = str.split(";");
			for(int i = 0; i < chuncks.length; i++) {
				String[] propChunk = chuncks[i].split("=");
				String pName = propChunk[0];
				String pValue = propChunk[1];
				properties.setProperty(pName, pValue);
			}
			
			return properties;
		}
	}
	

}