/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.configuration;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.engines.geo.application.GeoEngineCLI;
import it.eng.spagobi.engines.geo.commons.service.GeoEngineAnalysisState;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

public class MapConfiguration {

	private MapRendererConfiguration mapRendererConfiguration = null;

	private MapProviderConfiguration mapProviderConfiguration = null;

	private DatamartProviderConfiguration datamartProviderConfiguration = null;

	
	private static MapCatalogueAccessUtils mapCatalogueAccessUtils;
	
	/**
     * Logger component
     */
    public static transient Logger logger = Logger.getLogger(MapConfiguration.class);
	
    
	
	public MapConfiguration() {
		mapRendererConfiguration = new MapRendererConfiguration(this);
		mapProviderConfiguration = new MapProviderConfiguration(this);
		datamartProviderConfiguration = new DatamartProviderConfiguration(this);
	}
	

	/**
	 * Builds the MapConfiguration object loading configuration from xml template into SourceBeans
	 * @param template The byte array of the xml template
	 * @throws ConfigurationException raised If some configurations is missing or wrong
	 */ 
	public MapConfiguration(String template) throws ConfigurationException {
		this(template, null, null);
	}
	
	public MapConfiguration(String template, 
							SourceBean servReq, 
							String standardHierarchy) throws ConfigurationException {
		
		
		
		SourceBean map = null;
		try {
			map = SourceBean.fromXMLString(new String(template));
		} catch (Exception e) {
			throw new ConfigurationException("Cannot load template into SourceBean", e);
		}
		
		if (map == null) {
			throw new ConfigurationException("Cannot load map configuration from template");
		}
		
		
		
		// load mapRendererConfiguration
		logger.debug("Start parsing map render configuration block from template");
		SourceBean mapRendererConfigurationSB = null;
		mapRendererConfigurationSB = (SourceBean) map.getAttribute(Constants.MAP_RENDERER);
		if(mapRendererConfigurationSB == null) {
			throw new ConfigurationException("cannot load MAP RENDERER configuration: tag " 
					 + Constants.MAP_RENDERER);
		}
		mapRendererConfiguration = new MapRendererConfiguration(this, mapRendererConfigurationSB);
		logger.debug("Parsing of map render configuration block ended successfully");
		
		// load mapProviderConfiguration
		logger.debug("Start parsing map provider configuration block from template");
		SourceBean mapProviderConfigurationSB = null;
		mapProviderConfigurationSB = (SourceBean) map.getAttribute(Constants.MAP_PROVIDER);
		if(mapProviderConfigurationSB == null) {
			throw new ConfigurationException("cannot load MAP PROVIDER configuration: tag " 
					 + Constants.MAP_PROVIDER);
		}
		mapProviderConfiguration = new MapProviderConfiguration(this, mapProviderConfigurationSB);
		logger.debug("Parsing of map provider configuration block ended successfully");
			
				
		// load datamartProviderConfiguration		
		logger.debug("Start parsing datamart provider configuration block from template");
		SourceBean datamartProviderConfigurationSB = null;
		datamartProviderConfigurationSB = (SourceBean) map.getAttribute(Constants.DATAMART_PROVIDER);
		if(datamartProviderConfigurationSB == null) {
			throw new ConfigurationException("cannot load DATAMART PROVIDER configuration: tag " 
					 + Constants.DATAMART_PROVIDER);
		}		
		datamartProviderConfiguration = new DatamartProviderConfiguration(this, datamartProviderConfigurationSB, 
																			standardHierarchy);
		logger.debug("Parsing of datamart provider configuration block ended successfully");
		
				
		if(servReq != null) {
			datamartProviderConfiguration.setParameters(getParametersFromRequest(servReq));
		} else {
			datamartProviderConfiguration.setParameters(new Properties());
		}
			
	}

	
	public Properties getParametersFromRequest(SourceBean servReq) {
		Properties parameters = new Properties();
		List list = servReq.getContainedAttributes();
		for(int i = 0; i < list.size(); i++) {
			SourceBeanAttribute attrSB = (SourceBeanAttribute)list.get(i);
			if(attrSB.getKey().equals("template")) continue;
			if(attrSB.getKey().equals("ACTION_NAME")) continue;
			if(attrSB.getKey().equals("NEW_SESSION")) continue;
			String className = attrSB.getClass().getName();
			parameters.setProperty(attrSB.getKey(), attrSB.getValue().toString());
		}
		return parameters;
	}
		
	
	
	
	
	public void setAnalysisState( GeoEngineAnalysisState analysisState ) {	
		
		String selectedHiearchy = analysisState.getSelectedHierarchy();
		String selectedHierarchyLevel = analysisState.getSelectedHierarchyLevel();
		String selectedMap = analysisState.getSelectedMap();
		String selectedLayers = analysisState.getSelectedLayers();
		
		if(selectedHiearchy != null) {
			getDatamartProviderConfiguration().setHierarchyName(selectedHiearchy);
		}
		
		if(selectedHierarchyLevel != null) getDatamartProviderConfiguration().setHierarchyLevel(selectedHierarchyLevel);
		if(selectedMap != null) {
			getMapProviderConfiguration().setMapName(selectedMap);
		}	
		if(selectedLayers != null) {
			getMapRendererConfiguration().resetLayers();
			String[] layers = selectedLayers.split(",");			
			for(int i = 0; i < layers.length; i++) {
				
				MapRendererConfiguration.Layer layer = getMapRendererConfiguration().getLayer(layers[i]);
				if(layer != null) {
					layer.setSelected(true);
				} else {
					layer = new MapRendererConfiguration.Layer();
					layer.setName(layers[i]);
					layer.setDescription(layers[i]);
					layer.setSelected(true);
					getMapRendererConfiguration().addLayer(layer);
				}
			}
		}
	}
	
	
	
	
	
	
	
	
	
	/**
	 * Gets the DatamartProvider Configuration 
	 * @return SourceBean that contains the configuration
	 */
	public DatamartProviderConfiguration getDatamartProviderConfiguration() {
		return datamartProviderConfiguration;
	}

	/**
	 * Sets the DatamartProvider Configuration 
	 * @param datamartProviderConfiguration SourceBean that contains the configuration
	 */
	public void setDatamartProviderConfiguration(DatamartProviderConfiguration datamartProviderConfiguration) {
		this.datamartProviderConfiguration = datamartProviderConfiguration;
	}

	/**
	 * Gets the MapProvider Configuration 
	 * @return SourceBean that contains the configuration
	 */
	public MapProviderConfiguration getMapProviderConfiguration() {
		return mapProviderConfiguration;
	}
    
	/**
	 * Sets the MapProvider Configuration 
	 * @param mapProviderConfiguration SourceBean that contains the configuration
	 */
	public void setMapProviderConfiguration(MapProviderConfiguration mapProviderConfiguration) {
		this.mapProviderConfiguration = mapProviderConfiguration;
	}

	
	public MapRendererConfiguration getMapRendererConfiguration() {
		return mapRendererConfiguration;
	}

	public void setMapRendererConfiguration(
			MapRendererConfiguration mapRendererConfiguration) {
		this.mapRendererConfiguration = mapRendererConfiguration;
	}


	public static MapCatalogueAccessUtils getMapCatalogueAccessUtils() {
		return mapCatalogueAccessUtils;
	}


	public static void setMapCatalogueAccessUtils(
			MapCatalogueAccessUtils mapCatalogueAccessUtils) {
		MapConfiguration.mapCatalogueAccessUtils = mapCatalogueAccessUtils;
	}

}