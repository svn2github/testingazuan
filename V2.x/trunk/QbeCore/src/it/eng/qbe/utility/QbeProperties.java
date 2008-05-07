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
package it.eng.qbe.utility;

import it.eng.qbe.model.IDataMartModel;

import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * The Class QbeProperties.
 * 
 * @author Andrea Gioia
 */
public class QbeProperties {
	/** The properties. */
	private Properties  properties;
	
	/** The Constant VISIBLE. */
	public static final String VISIBLE = "visible";
	
	/**
	 * Instantiates a new datamart properties.
	 */
	public QbeProperties() {
		this(new Properties());
	}	
	
	/**
	 * Instantiates a new datamart properties.
	 * 
	 * @param properties the properties
	 */
	public QbeProperties(Properties  properties) {
		setProperties(properties);
	}	
	
	
	/**
	 * Gets the boolean properties.
	 * 
	 * @param key the key
	 * @param defaultValue the default value
	 * 
	 * @return the boolean properties
	 */
	private boolean getBooleanProperties(String key, boolean defaultValue) {
		String value = properties.getProperty(key);
		if(value == null) return defaultValue;
		return value.equalsIgnoreCase("TRUE");
	}

	
	/**
	 * Gets the property.
	 * 
	 * @param key the key
	 * 
	 * @return the property
	 */
	private String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * Gets the properties.
	 * 
	 * @return the properties
	 */
	private Properties getProperties() {
		return properties;
	}

	/**
	 * Sets the properties.
	 * 
	 * @param properties the new properties
	 */
	private void setProperties(Properties properties) {
		this.properties = properties;
	}	
}
