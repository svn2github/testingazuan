/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.configuration;

import it.eng.spago.base.SourceBean;

import java.util.Iterator;
import java.util.List;

/**
 * <p>
 * </p>
 */
public class MapConfiguration {

	/**
	 * <p>
	 * Represents map name
	 * </p>
	 */
	private String mapName = null;

	/**
	 * <p>
	 * Represents mapProviderConfiguration
	 * </p>
	 */
	private SourceBean mapProviderConfiguration = null;

	/**
	 * <p>
	 * Represents datamartProviderConfiguration
	 * </p>
	 */
	private SourceBean datamartProviderConfiguration = null;

	/**
	 * <p>
	 * Represents legenda
	 * </p>
	 */
	private SourceBean legenda = null;

	/**
	 * <p>
	 * Represents type
	 * </p>
	 */
	private String type = null;

	/**
	 * <p>
	 * Constructor
	 * </p>
	 * 
	 * @param template
	 * 
	 * @throws ConfigurationException
	 */
	public MapConfiguration(byte[] template) throws ConfigurationException {
		SourceBean maps = null;
		try {
			maps = SourceBean.fromXMLString(new String(template));
		} catch (Exception e) {
			throw new ConfigurationException(
					"Impossibile recuperare dal template la mappa richiesta");
		}
		SourceBean map = null;
		try {
			map = (SourceBean) maps.getFilteredSourceBeanAttribute(
					Constants.MAP, Constants.ENABLED, "TRUE");
		} catch (Exception e) {
			throw new ConfigurationException(
					"Impossibile recuperare dal template la mappa richiesta");
		}

		if (map == null) {
			throw new ConfigurationException(
					"Impossibile configurare la mappa richiesta; nel template deve essere presente una ed una sola mappa ENABLED");
		}
//		String name =  "";
//		// NAME
//		try {
//			name = (String) map.getAttribute(Constants.NAME);
//		} catch (Exception e) {
//			throw new ConfigurationException(
//					"Impossibile recuperare il mime type");
//		}
		// MIME TYPE
		try {
			type = (String) map.getAttribute(Constants.TYPE);
		} catch (Exception e) {
			throw new ConfigurationException(
					"Impossibile recuperare il mime type");
		}
		// LEGENDA
		try {
			legenda = (SourceBean) map.getAttribute(Constants.CONFIGURATION
					+ "." + Constants.LEGEND);
		} catch (Exception e) {
			throw new ConfigurationException(
					"Impossibile configurare la legenda dei colori associati alla mappa");
		}
		if (legenda == null) {
			throw new ConfigurationException(
					"Impossibile configurare la legenda dei colori associati alla mappa");
		}

		// MAP_PROVIDER
		try {
			mapProviderConfiguration = (SourceBean) map
					.getAttribute(Constants.MAP_PROVIDER);
		} catch (Exception e) {
			throw new ConfigurationException(
					"Impossibile configurare il MAP PROVIDER");
		}
		if (mapProviderConfiguration == null) {
			throw new ConfigurationException(
					"Impossibile configurare il MAP PROVIDER");
		}

		// DATAMART_PROVIDER
		try {
			datamartProviderConfiguration = (SourceBean) map
					.getAttribute(Constants.DATAMART_PROVIDER);
		} catch (Exception e) {
			throw new ConfigurationException(
					"Impossibile configurare il DATAMART PROVIDER");
		}
		if (datamartProviderConfiguration == null) {
			throw new ConfigurationException(
					"Impossibile configurare il DATAMART PROVIDER");
		}
	}

	public String getStyle(int value) {

		List hueList = legenda.getAttributeAsList(Constants.LEVELS + "."
				+ Constants.LEVEL);
		Iterator iterator = hueList.iterator();
		SourceBean level = (SourceBean) iterator.next();
		String lowThreshold = (String) level.getAttribute(Constants.THRESHOLD);

		String style = (String) level.getAttribute(Constants.STYLE);

		if (value < Integer.parseInt(lowThreshold)) {
			/* exit when value < lowThreshold */
			return style;
		}
		while (iterator.hasNext()) {
			level = (SourceBean) iterator.next();
			String highThreshold = (String) level
					.getAttribute(Constants.THRESHOLD);
			if (value >= Integer.parseInt(lowThreshold)
					&& value < Integer.parseInt(highThreshold)) {
				/* exits when lowThreshold < value < highThreshold */
				return style;
			} else {
				lowThreshold = highThreshold;
				style = (String) level.getAttribute(Constants.STYLE);
			}
		}
		/* exit when value > highThreshold */
		return style;
	}

	public String getLegend() {

		List hueList = legenda.getAttributeAsList(Constants.LEVELS + "."
				+ Constants.LEVEL);
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}