/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2008 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.engines.drivers;

import java.util.Map;

public class EngineURL {
	
	private String mainURL = null;
	
	private Map parameters = null;

	/**
	 * Instantiates a new engine url.
	 * 
	 * @param mainURL the main url
	 * @param parameters the parameters
	 */
	public EngineURL(String mainURL, Map parameters) {
		this.mainURL = mainURL;
		this.parameters = parameters;
	}
	
	/**
	 * Gets the main url.
	 * 
	 * @return the main url
	 */
	public String getMainURL() {
		return mainURL;
	}

	/**
	 * Sets the main url.
	 * 
	 * @param mainURL the new main url
	 */
	public void setMainURL(String mainURL) {
		this.mainURL = mainURL;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 */
	public Map getParameters() {
		return parameters;
	}

	/**
	 * Sets the parameters.
	 * 
	 * @param parameters the new parameters
	 */
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}

	/**
	 * Adds the parameter.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public void addParameter(String key, Object value) {
		parameters.put(key, value);
	}
	
	/**
	 * Adds the parameters.
	 * 
	 * @param parameters the parameters
	 */
	public void addParameters(Map parameters) {
		this.parameters.putAll(parameters);
	}
}
