/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.service.initializer;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dispatching.action.AbstractHttpAction;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.commons.service.AbstractEngineStartAction;
import it.eng.spagobi.engines.geo.commons.service.SpagoBIRequest;
import it.eng.spagobi.engines.geo.commons.service.SpagoBISubObject;
import it.eng.spagobi.engines.geo.configuration.Constants;
import it.eng.spagobi.engines.geo.configuration.MapConfiguration;
import it.eng.spagobi.engines.geo.configuration.MapRendererConfiguration;
import it.eng.spagobi.utilities.ParametersDecoder;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;

/**
 * Spago Action which executes the map producing request  
 */
public class GeoEngineStartAction extends AbstractEngineStartAction {
	
	// request
	public static final String INPUT_PAR_USER = "username";	
	public static final String INPUT_PAR_SPAGOBI_URL = "spagobiurl";	
	public static final String INPUT_PAR_TEMPLATE_PATH = "templatePath";
	
	//response 
	public static final String IS_SUBOBJECT = "isSubObject";
	public static final String BASE_URL = "baseUrl";
	
	// session
	public static final String MAP_CONFIGURATION = "CONFIGURATION";
	public static final String SPAGOBI_REQUEST = "SPAGOBI_REQUEST";
	public static final String SPAGOBI_TEMPLATE = "template";
	public static final String SPAGOBI_DATASOURCE = "SPAGOBI_DATASOURCE";
	public static final String SPAGOBI_SUBOBJECT_DETAILS = "SUBOBJECT";
	
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(GeoEngineStartAction.class);
	
	public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws GeoEngineException {
		logger.debug("Starting service method...");
		
		SpagoBIRequest spagobiRequest;
		//SpagoBISubObject spagbiSubObject;
		SubObjectDetails spagbiSubObject;
		String baseUrl;
		String selectedLayers;
		String user;
		String spagobiurl;
		String templatePath;
		
		super.service(serviceRequest, serviceResponse);
		
		// deprecated		
		user = getAttributeAsString(INPUT_PAR_USER);
		spagobiurl = getAttributeAsString(INPUT_PAR_SPAGOBI_URL);
		templatePath = getAttributeAsString(INPUT_PAR_TEMPLATE_PATH);
		// deprecated
		
		spagobiRequest = getSpagoBIRequest();
		//spagbiSubObject = getSpagoBISubObject();
		spagbiSubObject = getSubObjectDetails( getSpagoBISubObject() );
		

		MapCatalogueAccessUtils mapCatalogueAccessUtils = new MapCatalogueAccessUtils( getHttpSession(), getUserId() );
		MapConfiguration.setMapCatalogueAccessUtils( mapCatalogueAccessUtils );		
		
		MapConfiguration mapConfiguration = null;
		
		baseUrl = null;		
		try{
			baseUrl = "http://" + getHttpRequest().getServerName() + ":" + getHttpRequest().getServerPort() + getHttpRequest().getContextPath();			
			
			
			String standardHierarchy = mapCatalogueAccessUtils.getStandardHierarchy( );
			mapConfiguration = new MapConfiguration(baseUrl, getTemplate().toString().getBytes(), serviceRequest, 
					standardHierarchy, getDataSource());			
		} catch (Exception e) {
			logger.error("Error while reading map configuration", e);
		}
		
		selectedLayers = null;
		if(spagbiSubObject != null) {
			
			Properties subObjectProperties = spagbiSubObject.getSubobjectProperties();
			
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
			
			setAttribute(IS_SUBOBJECT, "true");
		} else {			
			spagbiSubObject = new SubObjectDetails();
			spagbiSubObject.setName("");
			spagbiSubObject.setDescription("");
			spagbiSubObject.setScope("");
			spagbiSubObject.setData(null);
			spagbiSubObject.setUser(user);
			spagbiSubObject.setSpagobiurl(spagobiurl);
			spagbiSubObject.setTemplatePath(templatePath);
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
			setAttribute(parName, parValue);
			props.setProperty(parName, parValue);			
		}		
		
		mapConfiguration.getDatamartProviderConfiguration().setParameters(props);	
		
		
		if(selectedLayers != null) setAttribute("selectedLayers", selectedLayers.split(","));
		//setAttribute("configuration", mapConfiguration);
		setAttribute(BASE_URL, baseUrl);
		
		setAttributeInSession(SPAGOBI_SUBOBJECT_DETAILS, spagbiSubObject);
		setAttributeInSession(SPAGOBI_REQUEST, spagobiRequest);
		setAttributeInSession(MAP_CONFIGURATION, mapConfiguration);
		setAttributeInSession(SPAGOBI_TEMPLATE, getTemplate() );
		setAttributeInSession(SPAGOBI_DATASOURCE, getDataSource() );
		
		
		logger.debug("End service method");
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
	
	private SubObjectDetails getSubObjectDetails(SpagoBISubObject spagobiSubobject)  {
		
		SubObjectDetails spagobiSubobjectDetails;
		String user;
		String spagobiurl;
		String templatePath;		
		
		spagobiSubobjectDetails = null;
		spagobiSubobject = getSpagoBISubObject();		
		if(spagobiSubobject != null) {
			spagobiSubobjectDetails = new SubObjectDetails();
			
			/* DEPRECATED
			user = (String)serviceRequest.getAttribute(INPUT_PAR_USER);
			spagobiurl = (String)serviceRequest.getAttribute(INPUT_PAR_SPAGOBI_URL);
			templatePath = (String)serviceRequest.getAttribute(INPUT_PAR_TEMPLATE_PATH);		
			 */
			
			spagobiSubobjectDetails.setName( spagobiSubobject.getName() );
			spagobiSubobjectDetails.setDescription( spagobiSubobject.getDescription() );
			spagobiSubobjectDetails.setScope( spagobiSubobject.getIsPublic()? "Public": "Private" );
			spagobiSubobjectDetails.setData( spagobiSubobject.getContent() );
			//subObjectDetails.setUser(user);
			//subObjectDetails.setSpagobiurl(spagobiurl);
			//subObjectDetails.setTemplatePath(templatePath);
		}
		
		
		
		return spagobiSubobjectDetails;		
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