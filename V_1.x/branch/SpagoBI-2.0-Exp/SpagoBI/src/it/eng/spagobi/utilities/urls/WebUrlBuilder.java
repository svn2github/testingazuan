package it.eng.spagobi.utilities.urls;

import it.eng.spagobi.utilities.ChannelUtilities;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * The implementation of IUrlBuilder used when SpagoBI is used as a STANDALONE WEB APPLICATION
 */
public class WebUrlBuilder implements IUrlBuilder{

	public String getUrl(HttpServletRequest aHttpServletRequest, Map parameters) {
		String contextName = ChannelUtilities.getSpagoBIContextName(aHttpServletRequest);
		String baseURL = contextName + "/servlet/AdapterHTTP";
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
		String contextName = ChannelUtilities.getSpagoBIContextName(aHttpServletRequest);
		originalUrl = originalUrl.trim();
		if(originalUrl.startsWith("/")) {
			originalUrl = originalUrl.substring(1);
		}
		originalUrl = contextName + "/" + originalUrl;
		return originalUrl;
	}

	
	
	
}
