package it.eng.spagobi.utilities.urls;

import it.eng.spago.base.ApplicationContainer;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;

public class UrlBuilderFactory {

	public static IUrlBuilder getUrlBuilder() {
		ApplicationContainer spagoContext = ApplicationContainer.getInstance();
		IUrlBuilder urlBuilder = (IUrlBuilder)spagoContext.getAttribute(SpagoBIConstants.URL_BUILDER);
		if(urlBuilder==null) {
			ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
			// get mode of execution
			String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
			// based on mode get spago object and url builder
			if (sbiMode.equalsIgnoreCase("WEB")) {
				urlBuilder = new WebUrlBuilder();		
			} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
				urlBuilder = new PortletUrlBuilder();
			}
			spagoContext.setAttribute(SpagoBIConstants.URL_BUILDER, urlBuilder);
		}	
		return urlBuilder;
	}
	
	
}
