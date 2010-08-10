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
package it.eng.spagobi.engines.geo.commons.tag;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class WebUrlGenerator {
	
	private static WebUrlGenerator instance;
	
	
	
	public static WebUrlGenerator getInstance() {
		if(instance == null) {
			instance = new WebUrlGenerator();
		}
		
		return instance;
	}
	
	
	private String baseURL="../servlet/AdapterHTTP";

	
	
	private WebUrlGenerator() {}
	
	public String getActionUrl(HttpServletRequest httpServletRequest, Map parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append(baseURL);
		
		if (parameters != null){
			Iterator keysIt = parameters.keySet().iterator();
			
			boolean isFirst = true;
			String paramName = null;
			Object paramValue = null;
			while (keysIt.hasNext()){
				paramName = (String)keysIt.next();
				paramValue = parameters.get(paramName); 
				if (isFirst){
					sb.append("?");
					isFirst = false;
				}else{
					sb.append("&");
				}
				if(paramValue == null) paramValue = "x";
				sb.append(paramName + "=" + paramValue.toString());
				
			}
		}
		return sb.toString();
	}
	
	/* (non-Javadoc)
	 * @see it.eng.spagobi.qbe.commons.urlgenerator.IQbeUrlGenerator#getResourceUrl(javax.servlet.http.HttpServletRequest, java.lang.String)
	 */
	public String getResourceUrl(HttpServletRequest httpServletRequest, String url){
		return url;
	}
}
