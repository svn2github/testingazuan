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
package it.eng.spagobi.engines.geo.dataset.provider;

import it.eng.spagobi.engines.geo.Constants;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class Link.
 * 
 * @author Andrea Gioia (andrea.gioia@eng.it)
 */
public class Link {
	
	/** The parameters. */
	private Map parameters;
	
	/** The Constant DEFAULT_BASE_URL. */
	public static final String DEFAULT_BASE_URL = "javascript:void(0)";
	
	/**
	 * Instantiates a new link.
	 */
	public Link() {
		parameters = new HashMap();
	}
	
	/**
	 * Adds the parameter.
	 * 
	 * @param type the type
	 * @param name the name
	 * @param value the value
	 */
	public void addParameter(String type, String name, String value){
		Parameter parameter = new Parameter(type, name, value);
		parameters.put(parameter.getName(), parameter);
	}
	
	/**
	 * The Class Parameter.
	 */
	public static class Parameter {
		
		/** The type. */
		String type;
		
		/** The name. */
		String name;
		
		/** The value. */
		String value;
		
		/**
		 * Instantiates a new parameter.
		 * 
		 * @param type the type
		 * @param name the name
		 * @param value the value
		 */
		public Parameter(String type, String name, String value) {
			 setType(type);
			 setName(name);
			 setValue(value);
		}

		/**
		 * Gets the type.
		 * 
		 * @return the type
		 */
		public String getType() {
			return type;
		}

		/**
		 * Sets the type.
		 * 
		 * @param type the new type
		 */
		public void setType(String type) {
			this.type = type;
		}

		/**
		 * Gets the name.
		 * 
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Sets the name.
		 * 
		 * @param name the new name
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * Gets the value.
		 * 
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Sets the value.
		 * 
		 * @param value the new value
		 */
		public void setValue(String value) {
			this.value = value;
		}
	}

	/**
	 * To string.
	 * 
	 * @param resultSet the result set
	 * 
	 * @return the string
	 */
	public String toString(ResultSet resultSet, Map env) {
		
		/*String link = null;
			
		if(baseUrl == null) return DEFAULT_BASE_URL;
		
		link = baseUrl + "?";
		*/
		String link = null;
		String executionId = (String) env.get(Constants.EXECUTION_ID);
		String targetDocLabel = "";
		String parametersStr = "";
		
    	try{
    		Iterator it = parameters.keySet().iterator();
    		while(it.hasNext()) {
    			String key = (String)it.next();
    			Parameter param = (Parameter)parameters.get(key);
    			if (param.getName().equalsIgnoreCase("DOCUMENT_LABEL")) {
        			if(param.getType().equalsIgnoreCase("absolute")) {
        				targetDocLabel += param.getValue();
        			} else if(param.getType().equalsIgnoreCase("relative")) {
        				String realValue = resultSet.getString(resultSet.findColumn(param.getValue()));
        				targetDocLabel += realValue;
        			}
    			} else {
        			if(param.getType().equalsIgnoreCase("absolute")) {
        				parametersStr += param.getName() + "=" + param.getValue() + "&";
        			} else if(param.getType().equalsIgnoreCase("relative")) {
        				String realValue = resultSet.getString(resultSet.findColumn(param.getValue()));
        				parametersStr += param.getName() + "=" + realValue + "&";
        			}
    			}
    		}
    		if (parametersStr.endsWith("&")) {
    			parametersStr = parametersStr.substring(0, parametersStr.length()-1);
    		}
    		
    		link = "javascript:parent.execCrossNavigation('iframeexec" + executionId + "', '" + targetDocLabel + "' , '" + parametersStr + "');";
    		
	    	//link += "')";
    	} catch (Exception e) {
    		link = "javascript:void(0)";
    	}
    	return link;
	}
}
