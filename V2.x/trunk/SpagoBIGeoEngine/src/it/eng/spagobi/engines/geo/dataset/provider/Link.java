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
package it.eng.spagobi.engines.geo.dataset.provider;

import it.eng.spago.base.SourceBean;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class Link {
	
	private String baseUrl;
	private Map parameters;
	
	public static final String DEFAULT_BASE_URL = "javascript:void(0)";
	
	public Link() {
		this(null);
	}
	
	public Link(String baseUrl) {
		this.baseUrl = baseUrl;
		parameters = new HashMap();
	}
	
	public void addParameter(String type, String name, String value){
		Parameter parameter = new Parameter(type, name, value);
		parameters.put(parameter.getName(), parameter);
	}
	
	public static class Parameter {
		String type;
		String name;
		String value;
		
		public Parameter(String type, String name, String value) {
			 setType(type);
			 setName(name);
			 setValue(value);
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}
	}

	public String toString(ResultSet resultSet) {
		
		/*String link = null;
			
		if(baseUrl == null) return DEFAULT_BASE_URL;
		
		link = baseUrl + "?";
		*/
    	String link = "javascript:parent.execDrill(this.name,'/SpagoBIGeoEngine/SpagoBIDrillServlet?";
		
    	try{
    		Iterator it = parameters.keySet().iterator();
    		while(it.hasNext()) {
    			String key = (String)it.next();
    			Parameter param = (Parameter)parameters.get(key);
    			if(param.getType().equalsIgnoreCase("absolute")) {
    				link += param.getName() + "=" + param.getValue() + "&";
    			} else if(param.getType().equalsIgnoreCase("relative")) {
    				String realValue = resultSet.getString(resultSet.findColumn(param.getValue()));
	    			link += param.getName() + "=" + realValue + "&";
    			}
    		}
    		link = link.substring(0, link.length()-1);
    		
	    	//link += "')";
    	} catch (Exception e) {
    		link = "javascript:void(0)";
    	}
    	return link;
	}
}
