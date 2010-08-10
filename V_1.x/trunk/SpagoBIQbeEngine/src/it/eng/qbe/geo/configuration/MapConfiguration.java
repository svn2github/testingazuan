/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.qbe.geo.configuration;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.utilities.callbacks.mapcatalogue.MapCatalogueAccessUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;

public class MapConfiguration {

	private String mapName = null;
	
	private MapRendererConfiguration mapRendererConfiguration = null;

	private MapProviderConfiguration mapProviderConfiguration = null;

	private DatamartProviderConfiguration datamartProviderConfiguration = null;

	
	private static MapCatalogueAccessUtils mapCatalogueAccessUtils;
	
	private SourceBean legenda = null;
	
	
	public String toXml() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<MAP>");
		buffer.append("\n\n" + mapProviderConfiguration.toXml());
		buffer.append("\n\n" + datamartProviderConfiguration.toXml());
		buffer.append("\n\n" + mapRendererConfiguration.toXml());
		buffer.append("\n\n</MAP>");
		
		return buffer.toString();
	}
	
	
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
	public MapConfiguration(String contextPath, byte[] template, SourceBean servReq) throws ConfigurationException {
		// load template xml string into a sourcebean
		SourceBean map = null;
		
		try {
			map = SourceBean.fromXMLString(new String(template));
		} catch (Exception e) {
			throw new ConfigurationException("Cannot load template into SourceBean", e);
		}
		if (map == null) {
			throw new ConfigurationException("Cannot load map configuration from template");
		}
		
		// load the logical name of the map  from the template
		try {
			mapName = (String) map.getAttribute(Constants.NAME);
		} catch (Exception e) {
			throw new ConfigurationException("Cannot load the name of the map from template", e);
		}
		
		// load mapRendererConfiguration
		SourceBean mapRendererConfigurationSB = (SourceBean) map.getAttribute(Constants.MAP_RENDERER);
		mapRendererConfiguration = new MapRendererConfiguration(this, mapRendererConfigurationSB);
		mapRendererConfiguration.setContextPath(contextPath);
		
		
		SourceBean mapProviderConfigurationSB = (SourceBean) map.getAttribute(Constants.MAP_PROVIDER);
		mapProviderConfiguration = new MapProviderConfiguration(this, mapProviderConfigurationSB);
		if(mapProviderConfiguration.getMapName() == null) {
			mapName = (String) map.getAttribute(Constants.NAME);
			mapProviderConfiguration.setMapName(mapName);
		}		
		
		if(mapProviderConfiguration.getMapName() == null) {
			throw new ConfigurationException("cannot set default datamart provider map name");
		}
		
		// recover legenda
		try {
			legenda = (SourceBean) map.getAttribute(Constants.CONFIGURATION	+ "." + Constants.LEGEND);
		} catch (Exception e) {
			throw new ConfigurationException("cannot find legenda configuration: tag "+ Constants.CONFIGURATION	+ "." + Constants.LEGEND, e);
		}
		/*
		if (legenda == null) {
			throw new ConfigurationException("cannot load legenda configuration from template");
		}
		*/
		
		// recover DATAMART_PROVIDER configuration		
		SourceBean datamartProviderConfigurationSB = null;
		try {
			datamartProviderConfigurationSB = (SourceBean) map.getAttribute(Constants.DATAMART_PROVIDER);
			if(datamartProviderConfigurationSB == null) throw new Exception();
		} catch (Exception e) {
			throw new ConfigurationException("cannot load DATAMART PROVIDER configuration: tag " + Constants.DATAMART_PROVIDER, e);
		}		
		DatamartProviderConfiguration datamartProviderConfiguration = new DatamartProviderConfiguration(this, datamartProviderConfigurationSB);
		datamartProviderConfiguration.setParameters(getParametersFromRequest(servReq));
		/*
		String targetLevelStr = (String)servReq.getAttribute("target_level");
		if(targetLevelStr != null) {
			int targetLevel = Integer.parseInt(targetLevelStr);
			datamartProviderConfiguration.setAggregationLevel(targetLevel);
		}
		*/
		setDatamartProviderConfiguration(datamartProviderConfiguration);		
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
			TracerSingleton.log("", TracerSingleton.MAJOR, 
					"GeoAction :: service : " +
					attrSB.getKey() + "ooooo----------------> " + attrSB.getValue().toString());	
			//if(!attrSB.getClass().getName().endsWith(".String")) continue;
			parameters.setProperty(attrSB.getKey(), attrSB.getValue().toString());
		}
		return parameters;
	}
		
	
	/**
	 * Recovers the svg style associated to the level of the legend which contains the value
	 * @param value The value associated to an svg element of the map
	 * @return the svg style associated to the level of the legend which contains the value
	 */
	public String getStyle(int value) {
		// recover the list of levels
		List hueList = legenda.getAttributeAsList(Constants.LEVELS + "." + Constants.LEVEL);
		Iterator iterator = hueList.iterator();
		// get the first level 
		SourceBean level = (SourceBean) iterator.next();
		// get the Threshold of the level
		String lowThreshold = (String) level.getAttribute(Constants.THRESHOLD);
		// get the style of the first level 
		String style = (String) level.getAttribute(Constants.STYLE);
		if (value < Integer.parseInt(lowThreshold)) {
			// the value is lower than the first Threshold
			return style;
		}
		while (iterator.hasNext()) {
			level = (SourceBean) iterator.next();
			String highThreshold = (String) level.getAttribute(Constants.THRESHOLD);
			if (value >= Integer.parseInt(lowThreshold) && value < Integer.parseInt(highThreshold)) {
				// exits when lowThreshold < value < highThreshold 
				return style;
			} else {
				lowThreshold = highThreshold;
				style = (String) level.getAttribute(Constants.STYLE);
			}
		}
		// exit when value > highThreshold 
		return style;
	}

	
	
	/**
	 * Builds the svg code used for the legend rendering
	 * @return the svg code of the legend
	 */
	public String getLegend() {
		
		String legendString = "<g>";
		// get level definitions
		List hueList = legenda.getAttributeAsList(Constants.LEVELS + "."+ Constants.LEVEL);
		// get position and dimension of the legend
		String x = (String) legenda.getAttribute(Constants.X);
		String y = (String) legenda.getAttribute(Constants.Y);
		String width = (String) legenda.getAttribute(Constants.WIDTH);
		String height = (String) legenda.getAttribute(Constants.HEIGHT);
		String style = (String) legenda.getAttribute(Constants.STYLE);
        // build title for legend
		SourceBean legendTitle = (SourceBean)legenda.getAttribute(Constants.TITLE);
		style = (String) legendTitle.getAttribute(Constants.STYLE);
		String description = (String) legendTitle.getAttribute(Constants.DESCRIPTION);
		legendString = legendString + "<text  x=\"" + x + "\" y=\""
					   + y + "\" style=\"" + style + "\">" + description
					   + "</text>";
		// build legend item for each level
		int i = 0;
		int xOffSet = Integer.parseInt(width) + Math.round(Integer.parseInt(width) / 5);
		int xTest = Integer.parseInt(x) + xOffSet;
		for (Iterator iterator = hueList.iterator(); iterator.hasNext();) {
			int yOffSet = i * Integer.parseInt(height) + Math.round(Integer.parseInt(height) / 5);
			int yRext = Integer.parseInt(y) + yOffSet;
			int yText = Integer.parseInt(y) + yOffSet + Integer.parseInt(height) / 2;
			SourceBean level = (SourceBean) iterator.next();
			style = (String) level.getAttribute(Constants.STYLE);
			legendString += "<rect x=\"" + x + "\" y=\"" + yRext + 
						    "\" width=\"" + width + 
						    "\" height=\"" + height + 
						    "\" style=\"" + style + "\"/>";
			// item text (description)
			SourceBean text = (SourceBean) level.getAttribute(Constants.TEXT);
			style = (String) text.getAttribute(Constants.STYLE);
			description = (String) text.getAttribute(Constants.DESCRIPTION);
			legendString = legendString + "<text  x=\"" + xTest + "\" y=\""
					+ yText + "\" style=\"" + style + "\">" + description
					+ "</text>";
			i++;
		}
		// close legend
		legendString = legendString + "</g>";
		return legendString;
	}

	
	
	
	/**
	 * Gets the map name
	 * @return the map name
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * Sets the map name
	 * @param mapName the map name
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
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

	public SourceBean getLegenda() {
		return legenda;
	}

	public void setLegenda(SourceBean legenda) {
		this.legenda = legenda;
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