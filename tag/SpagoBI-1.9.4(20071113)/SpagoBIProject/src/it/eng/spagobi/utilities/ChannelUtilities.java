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
		// try to find the RequestContainer
		reqCont = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		if (reqCont == null) reqCont = RequestContainerAccess.getRequestContainer(httpRequest);
		
		/*
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			reqCont = RequestContainerAccess.getRequestContainer(httpRequest);
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			reqCont = RequestContainerPortletAccess.getRequestContainer(httpRequest);
		}
		*/
		
		return reqCont;
	}
	
	public static ResponseContainer getResponseContainer(HttpServletRequest httpRequest) {
		ResponseContainer respCont = null;
		// try to find the ResponseContainer
		respCont = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
		if (respCont == null) respCont = ResponseContainerAccess.getResponseContainer(httpRequest);
		
		/*
		ConfigSingleton spagoconfig = ConfigSingleton.getInstance();
		// get mode of execution
		String sbiMode = (String)spagoconfig.getAttribute("SPAGOBI.SPAGOBI-MODE.mode");   
		// based on mode get spago object and url builder
		if (sbiMode.equalsIgnoreCase("WEB")) {
			respCont = ResponseContainerAccess.getResponseContainer(httpRequest);
		} else if  (sbiMode.equalsIgnoreCase("PORTLET")){
			respCont = ResponseContainerPortletAccess.getResponseContainer(httpRequest);
		}
		*/
		
		return respCont;
	}
	
	
	public static String getPreferenceValue(RequestContainer requestContainer, String preferenceName, String defaultValue) {
		String prefValue = defaultValue;
		try{
			// get mode of execution
			String channelType = requestContainer.getChannelType();
			String sbiMode = null;
			if ("PORTLET".equalsIgnoreCase(channelType)) sbiMode = "PORTLET";
			else sbiMode = "WEB";
			// based on mode get spago object and url builder
			if (sbiMode.equalsIgnoreCase("WEB")) {
				Object attribute = requestContainer.getSessionContainer().getAttribute(preferenceName);
				if (attribute != null) prefValue = attribute.toString();
				else prefValue = defaultValue;
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
			//contextName = GeneralUtilities.getSpagoBiContextAddress();
			PortletRequest portletRequest = PortletUtilities.getPortletRequest();
			contextName = portletRequest.getContextPath();
		}
		return contextName;
	}
	
	
	public static String getSpagoBiContentRepositoryServlet(HttpServletRequest httpRequest){
	    return getSpagoBIContextName(httpRequest) + "/ContentRepositoryServlet";
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
