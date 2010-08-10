package it.eng.spagobi.utilities.urls;

import it.eng.spago.navigation.LightNavigationManager;

public class UrlUtilities {

	public static String addNavigatorDisabledParameter(String url) {
		String urltoreturn = url;
		if(url.indexOf("?")==-1) {
			urltoreturn = url + "?" + LightNavigationManager.LIGHT_NAVIGATOR_DISABLED + "=TRUE";
		} else {
			if(url.indexOf(LightNavigationManager.LIGHT_NAVIGATOR_DISABLED)==-1) {
				if(url.endsWith("&") || url.endsWith("&amp;") || url.endsWith("?")) {
					urltoreturn = url + LightNavigationManager.LIGHT_NAVIGATOR_DISABLED + "=TRUE";
				} else {
					urltoreturn = url + "&" + LightNavigationManager.LIGHT_NAVIGATOR_DISABLED + "=TRUE";
				}
			}
		} 
		return urltoreturn;
	}
	
}
