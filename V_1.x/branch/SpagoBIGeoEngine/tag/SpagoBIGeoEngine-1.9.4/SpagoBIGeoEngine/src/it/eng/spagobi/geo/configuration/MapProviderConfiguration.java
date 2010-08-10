/**
 * 
 */
package it.eng.spagobi.geo.configuration;

import it.eng.spago.base.SourceBean;

/**
 * @author giachino
 *
 */
public class MapProviderConfiguration {
	private MapConfiguration parentConfiguration;
	private String className;
	private String mapName;
	
	public MapProviderConfiguration(MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}
	
	public MapProviderConfiguration(MapConfiguration parentConfiguration, SourceBean mapProviderConfigurationSB) {
		setParentConfiguration(parentConfiguration);
		String mapProviderClassName = (String) mapProviderConfigurationSB.getAttribute(Constants.CLASS_NAME);
		setClassName(mapProviderClassName);
		
		String mapName = (String)mapProviderConfigurationSB.getAttribute(Constants.MAP_NAME);
		setMapName(mapName);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMapName() {
		return mapName;
	}

	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	public MapConfiguration getParentConfiguration() {
		return parentConfiguration;
	}

	public void setParentConfiguration(MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}
}
