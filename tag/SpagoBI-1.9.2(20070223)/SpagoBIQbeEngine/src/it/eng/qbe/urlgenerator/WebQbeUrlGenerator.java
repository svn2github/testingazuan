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
