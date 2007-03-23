package it.eng.spagobi.utilities;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerAccess;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.ResponseContainerAccess;
import it.eng.spago.base.ResponseContainerPortletAccess;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.SpagoBIConstants;

import javax.portlet.ActionRequest;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.portlet.PortletFileUpload;

public class ChannelUtilities {

	public static RequestContainer getRequestContainer(HttpServletRequest httpRequest) {
		RequestContainer reqCont = null;
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			reqCont = RequestContainerAccess.getRequestContainer(httpRequest);
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			reqCont = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		}
		return reqCont;
	}
	
	public static ResponseContainer getResponseContainer(HttpServletRequest httpRequest) {
		ResponseContainer respCont = null;
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			respCont = ResponseContainerAccess.getResponseContainer(httpRequest);
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			respCont = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
		}
		return respCont;
	}
	
	
	public static String getPreferenceValue(SourceBean request, String preferenceName, String defaultValue) {
		String prefValue = defaultValue;
		try{
			ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
			// get mode of execution
			String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
			// based on mode get spago object and url builder
			if (sbiMode.equalsIgnoreCase("WEB")) {
				prefValue = defaultValue;
			} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
				PortletRequest portReq = PortletUtilities.getPortletRequest();
				PortletPreferences prefs = portReq.getPreferences();
				prefValue = (String)prefs.getValue(preferenceName, defaultValue);
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, ChannelUtilities.class.getName(), 
					            "getPreferenceValue", "Error while recovering preference value", e);
			prefValue = defaultValue;
		}
		return prefValue;
	}
	
	
	public static String getSpagoBIContextName(HttpServletRequest httpRequest) {
		String contextName = "spagobi";
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			contextName = httpRequest.getContextPath();
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			contextName = GeneralUtilities.getSpagoBiContextAddress();
		}
		return contextName;
	}
	
	public static SourceBean getSpagoRequestFromMultipart() {
		SourceBean request = null;
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if(sbiMode.equalsIgnoreCase("PORTLET")){
			PortletRequest portletRequest = PortletUtilities.getPortletRequest();
			if (portletRequest instanceof ActionRequest) {
				ActionRequest actionRequest = (ActionRequest) portletRequest;
				if (PortletFileUpload.isMultipartContent(actionRequest)) {
					request = PortletUtilities.getServiceRequestFromMultipartPortletRequest(portletRequest);
				}
			}
		}
		return request;
	}
	
	
	public static boolean isWebRunning() {
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		if( (sbiMode!=null) && sbiMode.equalsIgnoreCase("WEB") ) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static boolean isPortletRunning() {
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		if( (sbiMode==null) || !sbiMode.equalsIgnoreCase("WEB")){
			return true;
		} else {
			return false;
		}
	}
	
	
}
