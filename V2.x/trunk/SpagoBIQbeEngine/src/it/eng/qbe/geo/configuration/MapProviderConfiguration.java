/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.geo.configuration;

import it.eng.spago.base.SourceBean;

// TODO: Auto-generated Javadoc
/**
 * The Class MapProviderConfiguration.
 * 
 * @author giachino
 */
public class MapProviderConfiguration {
	
	/** The parent configuration. */
	private MapConfiguration parentConfiguration;
	
	/** The class name. */
	private String className;
	
	/** The map name. */
	private String mapName;
	
	/**
	 * Instantiates a new map provider configuration.
	 * 
	 * @param parentConfiguration the parent configuration
	 */
	public MapProviderConfiguration(MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}
	
	/**
	 * Instantiates a new map provider configuration.
	 * 
	 * @param parentConfiguration the parent configuration
	 * @param mapProviderConfigurationSB the map provider configuration sb
	 */
	public MapProviderConfiguration(MapConfiguration parentConfiguration, SourceBean mapProviderConfigurationSB) {
		setParentConfiguration(parentConfiguration);
		String mapProviderClassName = (String) mapProviderConfigurationSB.getAttribute(Constants.CLASS_NAME);
		setClassName(mapProviderClassName);
		
		String mapName = (String)mapProviderConfigurationSB.getAttribute(Constants.MAP_NAME);
		setMapName(mapName);
	}

	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Sets the class name.
	 * 
	 * @param className the new class name
	 */
	public void setClassName(String className) {
		this.className = className;
	}

	/**
	 * Gets the map name.
	 * 
	 * @return the map name
	 */
	public String getMapName() {
		return mapName;
	}

	/**
	 * Sets the map name.
	 * 
	 * @param mapName the new map name
	 */
	public void setMapName(String mapName) {
		this.mapName = mapName;
	}

	/**
	 * Gets the parent configuration.
	 * 
	 * @return the parent configuration
	 */
	public MapConfiguration getParentConfiguration() {
		return parentConfiguration;
	}

	/**
	 * Sets the parent configuration.
	 * 
	 * @param parentConfiguration the new parent configuration
	 */
	public void setParentConfiguration(MapConfiguration parentConfiguration) {
		this.parentConfiguration = parentConfiguration;
	}

	/**
	 * To xml.
	 * 
	 * @return the string
	 */
	public String toXml() {
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("<MAP_PROVIDER ");
		buffer.append("\nclass_name=\"" + getClassName()+ "\" ");
		buffer.append("\nmap_name=\"" + getMapName()+ "\" ");
		buffer.append("/>");
		
		return buffer.toString();
	}
}
