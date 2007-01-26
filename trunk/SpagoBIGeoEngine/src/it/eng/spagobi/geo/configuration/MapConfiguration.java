/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.configuration;

import it.eng.spago.base.SourceBean;
import java.util.Iterator;
import java.util.List;

public class MapConfiguration {

	private String mapName = null;

	private SourceBean mapProviderConfiguration = null;

	private SourceBean datamartProviderConfiguration = null;

	private SourceBean legenda = null;
	

	/**
	 * Builds the MapConfiguration object loading configuration from xml template into SourceBeans
	 * @param template The byte array of the xml template
	 * @throws ConfigurationException raised If some configurations is missing or wrong
	 */ 
	public MapConfiguration(byte[] template) throws ConfigurationException {
		// load a predefined map provider SourceBean
		String mapProvConfStr = "<MAP_PROVIDER class_name=\"it.eng.geo.map.DefaultMapProvider\" " +
		                        "uri=\"\" " +
		                        "xml_parser_impl=\"it.eng.geo.document.XercesXMLDocument\"/>";
		try {
			mapProviderConfiguration = SourceBean.fromXMLString(mapProvConfStr);
		} catch (Exception e) {
			throw new ConfigurationException("Cannot load map provider configuration", e);
		}
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
		if(mapName==null)  {
			throw new ConfigurationException("cannot load map name from template");
		}
		// recover legenda
		try {
			legenda = (SourceBean) map.getAttribute(Constants.CONFIGURATION	+ "." + Constants.LEGEND);
		} catch (Exception e) {
			throw new ConfigurationException("cannot find legenda configuration: tag "+ Constants.CONFIGURATION	+ "." + Constants.LEGEND, e);
		}
		if (legenda == null) {
			throw new ConfigurationException("cannot load legenda configuration from template");
		}
		// recover DATAMART_PROVIDER configuration
		try {
			datamartProviderConfiguration = (SourceBean) map.getAttribute(Constants.DATAMART_PROVIDER);
		} catch (Exception e) {
			throw new ConfigurationException("cannot load DATAMART PROVIDER configuration: tag " + Constants.DATAMART_PROVIDER, e);
		}
		if (datamartProviderConfiguration == null) {
			throw new ConfigurationException("Cannot load DATAMART PROVIDER configuration from template");
		}
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

		List hueList = legenda.getAttributeAsList(Constants.LEVELS + "."+ Constants.LEVEL);
		String description = "";
		String x = (String) legenda.getAttribute(Constants.X);
		String y = (String) legenda.getAttribute(Constants.Y);
		String width = (String) legenda.getAttribute(Constants.WIDTH);
		String height = (String) legenda.getAttribute(Constants.HEIGHT);
		String style = (String) legenda.getAttribute(Constants.STYLE);
		/* legend box template */
		String legendString = "<defs>" + "	<rect id=\"legendBox\" x=\"" + x
				+ "\" y=\"" + y + "\" width=\"" + width + "\" height=\""
				+ height + "\" style=\"" + style + "\" />" + "</defs>" + "<g>";
		/* legend title */
		SourceBean legendTitle = (SourceBean) legenda
				.getAttribute(Constants.TITLE);
		style = (String) legendTitle.getAttribute(Constants.STYLE);
		description = (String) legendTitle.getAttribute(Constants.DESCRIPTION);
		legendString = legendString + "<text  x=\"" + x + "\" y=\""
		+ y + "\" style=\"" + style + "\">" + description
		+ "</text>";
		int i = 0;
		int xOffSet = Integer.parseInt(width)
				+ Math.round(Integer.parseInt(width) / 5);
		int xTest = Integer.parseInt(x) + xOffSet;
		for (Iterator iterator = hueList.iterator(); iterator.hasNext();) {
			int yOffSet = i * Integer.parseInt(height)
					+ Math.round(Integer.parseInt(height) / 5);
			int yText = Integer.parseInt(y) + yOffSet
					+ Integer.parseInt(height) / 2;

			/* legend box */
			SourceBean level = (SourceBean) iterator.next();
			style = (String) level.getAttribute(Constants.STYLE);
			legendString = legendString + "<use xlink:href=\"#legendBox\" "
					+ "transform=\"translate(0," + yOffSet + ")\" style=\""
					+ style + "\"/>";

			/* legend text */
			SourceBean text = (SourceBean) level.getAttribute(Constants.TEXT);
			style = (String) text.getAttribute(Constants.STYLE);
			description = (String) text.getAttribute(Constants.DESCRIPTION);
			legendString = legendString + "<text  x=\"" + xTest + "\" y=\""
					+ yText + "\" style=\"" + style + "\">" + description
					+ "</text>";
			i++;
		}
		legendString = legendString + "</g>";
		return legendString;
	}

	
	
	
	
	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public SourceBean getDatamartProviderConfiguration() {
		return datamartProviderConfiguration;
	}

	public void setDatamartProviderConfiguration(
			SourceBean datamartProviderConfiguration) {
		this.datamartProviderConfiguration = datamartProviderConfiguration;
	}

	public SourceBean getMapProviderConfiguration() {
		return mapProviderConfiguration;
	}

	public void setMapProviderConfiguration(SourceBean mapProviderConfiguration) {
		this.mapProviderConfiguration = mapProviderConfiguration;
	}

}