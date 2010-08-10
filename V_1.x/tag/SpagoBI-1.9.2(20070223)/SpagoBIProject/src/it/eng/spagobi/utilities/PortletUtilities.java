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
/*
 * Created on 24-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.utilities;

import it.eng.spago.base.PortletAccess;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.RequestContainerPortletAccess;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.constants.ObjectsTreeConstants;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.constants.UtilitiesConstants;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import javax.portlet.ActionRequest;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;


/**
 * Contains some SpagoBI's portlet utilities.
 * 
 * @author Zoppello
 */
public class PortletUtilities {

	/**
	 * Starting from the original URL and the request, creates a string representing the 
	 * Portlet URL.
	 * 
	 * @param aHttpServletRequest	The request object at input
	 * @param originalURL	The starting original URL
	 * @return	A String representing the Portlet URL
	 */
	public static String createPortletURL(HttpServletRequest aHttpServletRequest, String originalURL){

		RenderResponse renderResponse =(RenderResponse)aHttpServletRequest.getAttribute("javax.portlet.response");
		RequestContainer requestContainer = RequestContainerPortletAccess.getRequestContainer(aHttpServletRequest);
		
		PortletURL aPortletURL = renderResponse.createActionURL();
		
		SpagoBITracer.debug(UtilitiesConstants.NAME_MODULE, 
							PortletUtilities.class.getName(), 
							"createPortletURL()",
							"Original URL.... " + originalURL + "indexOf ? is " + originalURL.indexOf("?"));
		String parameters = originalURL.substring(originalURL.indexOf("?")+1);
		
		StringTokenizer st = new StringTokenizer(parameters, "&", false);
		
		String parameterToken = null;
		String parameterName = null;
		String parameterValue = null;
		while (st.hasMoreTokens()){
			parameterToken = st.nextToken();
			SpagoBITracer.debug(UtilitiesConstants.NAME_MODULE, 
					PortletUtilities.class.getName(), 
					"createPortletURL()","Parameter Token [" + parameterToken +"]");
			parameterName = parameterToken.substring(0, parameterToken.indexOf("="));
			parameterValue = parameterToken.substring(parameterToken.indexOf("=") + 1);
			
			
			SpagoBITracer.debug(UtilitiesConstants.NAME_MODULE, 
					PortletUtilities.class.getName(), 
					"createPortletURL()","Parameter Name [" + parameterName +"]");
			SpagoBITracer.debug(UtilitiesConstants.NAME_MODULE, 
					PortletUtilities.class.getName(), 
					"createPortletURL()","Parameter Value [" + parameterValue +"]");
			aPortletURL.setParameter(parameterName, parameterValue);
		}
		
		
		return aPortletURL.toString();
	}
	
	/**
	 * Creates the particular portlet URL for a resource, given its path.
	 * 
	 * @param aHttpServletRequest	The request object at input
	 * @param resourceAbsolutePath	The resource Absolute path
	 * @return	The resource Portlet URL String
	 */
	public static String  createPortletURLForResource(HttpServletRequest aHttpServletRequest, String resourceAbsolutePath){
		RenderResponse renderResponse =(RenderResponse)aHttpServletRequest.getAttribute("javax.portlet.response");
		RenderRequest renderRequest =(RenderRequest)aHttpServletRequest.getAttribute("javax.portlet.request");
		
		return renderResponse.encodeURL(renderRequest.getContextPath() + resourceAbsolutePath).toString();
	}
	
	/**
	 * Gets the <code>PortletRequest</code> object.
	 * @return The portlet request object
	 */
	public static PortletRequest getPortletRequest(){
		return PortletAccess.getPortletRequest();
	}
	/**
	 * Gets the <code>PortletResponse</code> object.
	 * @return The portlet response object
	 */
	public static PortletResponse getPortletResponse(){
		return PortletAccess.getPortletResponse();
	}
	/**
	 * Gets the service request from a Multipart Portlet Request. This method creates a 
	 * new file upload handler, then parses the request and processes the new uploaded items.
	 * In this way a new uploaded file is obtained, which is put into the <code>serviceRequest</code>
	 * object.
	 * 
	 * @param portletRequest	The input portlet request
	 * @return	The <code>serviceRequest</code> SourceBean containing the uploaded file.
	 */
	public static SourceBean getServiceRequestFromMultipartPortletRequest(PortletRequest portletRequest){
		SourceBean serviceRequest = null;
		try{
			serviceRequest = new SourceBean("SERVICEREQUEST");
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//		 Create a new file upload handler
			PortletFileUpload upload = new PortletFileUpload(factory);
		
			//		 Parse the request
			List /* FileItem */ items = upload.parseRequest((ActionRequest)portletRequest);
		
		
			//		 Process the uploaded items
			Iterator iter = items.iterator();
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					serviceRequest.setAttribute(item.getFieldName(), item.getString());
				} else {
					UploadedFile uploadedFile = new UploadedFile();
					uploadedFile.setFileContent(item.get());
					uploadedFile.setFieldNameInForm(item.getFieldName());
					uploadedFile.setSizeInBytes(item.getSize());
					uploadedFile.setFileName(GeneralUtilities.getRelativeFileNames(item.getName()));
					serviceRequest.setAttribute("UPLOADED_FILE", uploadedFile);
				}
			}
		}catch(Exception e){
			SpagoBITracer.major(UtilitiesConstants.NAME_MODULE, PortletUtilities.class.getName(),"getServiceRequestFromMultipartPortletRequets","Cannot parse multipart request", e);
		}
		return serviceRequest;
		
	}
	
	/**
	 * Gets the first uploaded file from a portlet request. This method creates a new file upload handler, 
	 * parses the request, processes the uploaded items and then returns the first file as an
	 * <code>UploadedFile</code> object.
	 * @param portletRequest The input portlet request
	 * @return	The first uploaded file object.
	 */
	public static UploadedFile getFirstUploadedFile(PortletRequest portletRequest){
		UploadedFile uploadedFile = null;
		try{
			
			DiskFileItemFactory factory = new DiskFileItemFactory();
			//		 Create a new file upload handler
			PortletFileUpload upload = new PortletFileUpload(factory);
		
			//		 Parse the request
			List /* FileItem */ items = upload.parseRequest((ActionRequest)portletRequest);
		
		
			//		 Process the uploaded items
			Iterator iter = items.iterator();
			boolean endLoop = false;
			while (iter.hasNext() && !endLoop) {
				FileItem item = (FileItem) iter.next();

				if (item.isFormField()) {
					//serviceRequest.setAttribute(item.getFieldName(), item.getString());
				} else {
					uploadedFile = new UploadedFile();
					uploadedFile.setFileContent(item.get());
					uploadedFile.setFieldNameInForm(item.getFieldName());
					uploadedFile.setSizeInBytes(item.getSize());
					uploadedFile.setFileName(item.getName());
					
					endLoop = true;
				}
			}
		}catch(Exception e){
			SpagoBITracer.major(ObjectsTreeConstants.NAME_MODULE,PortletUtilities.class.getName(),"getServiceRequestFromMultipartPortletRequets","Cannot parse multipart request", e);
		}
		return uploadedFile;
		
	}

	/**
	 * Cleans a string from spaces and tabulation characters.
	 * 
	 * @param original The input string
	 * @return The cleaned string
	 */
	public static String cleanString(String original){
		StringBuffer sb = new StringBuffer();
		char[] arrayChar = original.toCharArray();
		for (int i=0; i < arrayChar.length; i++){
			if ((arrayChar[i] == '\n')
				|| (arrayChar[i] == '\t')
				|| (arrayChar[i] == '\r')){
				
			}else{
				sb.append(arrayChar[i]);
			}
		}
		return sb.toString().trim();
	}
	
	public static Locale getPortalLocale() {
		return PortletAccess.getPortalLocale();
	}
	
	/**
	 * Gets a localized message given its code and bundle 
	 * information. If there isn't any message matching to these infromation, a
	 * warning is traced.
	 * 
	 * @param code	The message's code string
	 * @param bundle The message's bundel string
	 * @return	A string containing the message
	 */
	 public static String getMessage(String code, String bundle) {
		 String portalLang = null;
		 try {
		 	// get the locale and the language of the portal
		 	Locale portalLocale =  PortletAccess.getPortalLocale();
		 	if (portalLocale == null) {
	        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "SpagoBIMessageTag", 
			              "getMessage", "Portal locale not found by PortletAccess.getPortalLocale() method!! " +
			              		"May be there is not a portlet request");
		 	} else {
		 		portalLang = portalLocale.getLanguage();
		 	}
			// Locale portalLocale = (Locale)portletRequest.getPortletSession().getAttribute("LOCALE");
		} catch (Exception e) {
        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "SpagoBIMessageTag", 
		              "getMessage", "Error while getting portal locale", e);
		}
	 	// get the configuration sourceBean/language code/country code of the default language
	 	SourceBean defaultLangSB = (SourceBean)ConfigSingleton.getInstance()
	 	                           .getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
	 	                           		                           "default", "true");
	 	String defaultLang = (String)defaultLangSB.getAttribute("language");
	 	String defaultCountry = (String)defaultLangSB.getAttribute("country");
	 	
	 	// try to get the configuration sourceBean of the language of the portal, if a portal language was found
	 	SourceBean portalLangSB = null;
	 	if (portalLang != null) {
		 	portalLangSB = (SourceBean)ConfigSingleton.getInstance()
		 			.getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
                                              "language", portalLang);
	 	}

	 	// if the portal language as no configuration sourceBean use the default language
	 	Locale locale = null;
	 	if(portalLangSB!=null) {
	 		String lang = (String)portalLangSB.getAttribute("language");
		 	String country = (String)portalLangSB.getAttribute("country");
		 	locale = new Locale(lang, country);
	 	} else {
	 		locale = new Locale(defaultLang, defaultCountry);
	 	}
	 	

	 	ResourceBundle messages = ResourceBundle.getBundle(bundle, locale);
        if (messages == null) {
            return null;
        } 
        String message = code;
        try {
            message = messages.getString(code);
        } 
        catch (Exception ex) {
        	SpagoBITracer.warning(SpagoBIConstants.NAME_MODULE, "SpagoBIMessageTag", 
		              "getMessage", "code [" + code + "] not found ");
        } 
        return message;
    } // public String getMessage(String code)
	 
	 
	 
	 
	 
	 /**
		 * Gets the language code of the user portal language. If it's not possible to gather 
		 * the locale of the portal it returns the default language code   
		 * @return	A string containing the language code
		 */
		 public static String getPortalLanguageCode() {
			 try {
			 	Locale portalLocale =  PortletAccess.getPortalLocale();
			 	if(portalLocale == null) {
		        	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "SpagoBIMessageTag", 
		        			            "getLanguageCode", 
		        			            "Portal locale not found by PortletAccess.getPortalLocale() method!! " +
				              			"May be there is not a portlet request");
			 	} else {
			 		String portalLang = portalLocale.getLanguage();
			 		return portalLang;
			 	}
			 } catch (Exception e) {
				 SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, "SpagoBIMessageTag", 
			              			"getLanguageCode", "Error while getting portal locale", e);
				 
			 }
			 // get the configuration sourceBean/language code/country code of the default language
			 SourceBean defaultLangSB = (SourceBean)ConfigSingleton.getInstance()
		 	                           .getFilteredSourceBeanAttribute("SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE", 
		 	                           		                           "default", "true");
			 String defaultLang = (String)defaultLangSB.getAttribute("language");
		 	 return defaultLang;
	    } 
	 
	 
}
