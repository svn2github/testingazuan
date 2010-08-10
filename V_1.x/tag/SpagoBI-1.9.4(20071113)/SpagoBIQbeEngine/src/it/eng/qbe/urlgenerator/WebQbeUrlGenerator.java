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
package it.eng.qbe.urlgenerator;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Andrea Zoppello
 * 
 * The implementation of IQbeUrlGenerator used when QBE is used as a STANDALONE WEB APPLICATIONS
 */
public class WebQbeUrlGenerator implements IQbeUrlGenerator{

	private String baseURL="../servlet/AdapterHTTP";

	public String getUrl(HttpServletRequest aHttpServletRequest, Map parameters) {
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
				sb.append(paramName+"="+paramValue.toString());
				
			}
		}
		return sb.toString();
	}
	
	public String conformStaticResourceLink(HttpServletRequest aHttpServletRequest, String originalUrl){
		return originalUrl;
	}

	
	
	
}
