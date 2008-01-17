/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.qbe.bo;

import java.util.Properties;

/**
 * @author Andrea Gioia
 *
 */
public class DatamartProperties {
	private Properties  properties;
	
	public static final String VISIBLE = "visible";
	
	public DatamartProperties() {
		this(new Properties());
	}	
	
	public DatamartProperties(Properties  properties) {
		setProperties(properties);
	}	
	
	
	private boolean getBooleanProperties(String key, boolean defaultValue) {
		String value = properties.getProperty(key);
		if(value == null) return defaultValue;
		return value.equalsIgnoreCase("TRUE");
	}

	
	private String getProperty(String key) {
		return properties.getProperty(key);
	}
	
	private Properties getProperties() {
		return properties;
	}

	private void setProperties(Properties properties) {
		this.properties = properties;
	}
}
