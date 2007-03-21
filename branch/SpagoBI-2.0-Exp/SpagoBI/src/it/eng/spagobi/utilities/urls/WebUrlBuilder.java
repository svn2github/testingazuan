package it.eng.spagobi.utilities.urls;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * The implementation of IUrlBuilder used when SpagoBI is used as a STANDALONE WEB APPLICATION
 */
public class WebUrlBuilder implements IUrlBuilder{

	private String baseURL="/spagobi/servlet/AdapterHTTP";
	private String baseResourceURL="/spagobi/";

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
	
	public String getResourceLink(HttpServletRequest aHttpServletRequest, String originalUrl){
		originalUrl = originalUrl.trim();
		if(originalUrl.startsWith("/")) {
			originalUrl = originalUrl.substring(1);
		}
		originalUrl = baseResourceURL + originalUrl;
		return originalUrl;
	}

	
	
	
}
