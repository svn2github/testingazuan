/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2008 Engineering Ingegneria Informatica S.p.A.

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
 * Created on 7-lug-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.spagobi.commons.utilities;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFErrorCategory;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.BIObjectParameter;
import it.eng.spagobi.behaviouralmodel.analyticaldriver.bo.Parameter;
import it.eng.spagobi.behaviouralmodel.lov.bo.ILovDetail;
import it.eng.spagobi.behaviouralmodel.lov.bo.LovDetailFactory;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.dao.IModalitiesValueDAO;
import it.eng.spagobi.commons.bo.UserProfile;
import it.eng.spagobi.commons.constants.SpagoBIConstants;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.utilities.messages.IMessageBuilder;
import it.eng.spagobi.commons.utilities.messages.MessageBuilderFactory;
import it.eng.spagobi.services.common.SsoServiceFactory;
import it.eng.spagobi.services.common.SsoServiceInterface;
import it.eng.spagobi.services.security.bo.SpagoBIUserProfile;
import it.eng.spagobi.services.security.exceptions.SecurityException;
import it.eng.spagobi.services.security.service.ISecurityServiceSupplier;
import it.eng.spagobi.services.security.service.SecurityServiceSupplierFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Contains some SpagoBI's general utilities.
 */
public class GeneralUtilities extends SpagoBIUtilities{

    private static transient Logger logger = Logger.getLogger(GeneralUtilities.class);
    
    public static final int MAX_DEFAULT_TEMPLATE_SIZE = 5242880;
    public static String SPAGOBI_HOST = null;    
    

    /**
     * Substitutes the substrings with sintax "${code,bundle}" or "${code}" (in
     * the second case bundle is assumed to be the default value "messages")
     * with the correspondent internationalized messages in the input String.
     * This method calls <code>PortletUtilities.getMessage(key, bundle)</code>.
     * 
     * @param message The string to be modified
     * 
     * @return The message with the internationalized substrings replaced.
     */
    public static String replaceInternationalizedMessages(String message) {
	if (message == null)
	    return null;
	int startIndex = message.indexOf("${");
	if (startIndex == -1)
	    return message;
	else
	    return replaceInternationalizedMessages(message, startIndex);
    }

    private static String replaceInternationalizedMessages(String message, int startIndex) {
	logger.debug("IN");
	IMessageBuilder msgBuilder = MessageBuilderFactory.getMessageBuilder();
	int endIndex = message.indexOf("}", startIndex);
	if (endIndex == -1 || endIndex < startIndex)
	    return message;
	String toBeReplaced = message.substring(startIndex + 2, endIndex).trim();
	String key = "";
	String bundle = "messages";
	String[] splitted = toBeReplaced.split(",");
	if (splitted != null) {
	    key = splitted[0].trim();
	    if (splitted.length == 1) {
		String replacement = msgBuilder.getMessage(key, bundle);
		// if (!replacement.equalsIgnoreCase(key)) message =
		// message.replaceAll("${" + toBeReplaced + "}", replacement);
		if (!replacement.equalsIgnoreCase(key))
		    message = message.replaceAll("\\$\\{" + toBeReplaced + "\\}", replacement);
	    }
	    if (splitted.length == 2) {
		if (splitted[1] != null && !splitted[1].trim().equals(""))
		    bundle = splitted[1].trim();
		String replacement = msgBuilder.getMessage(key, bundle);
		// if (!replacement.equalsIgnoreCase(key)) message =
		// message.replaceAll("${" + toBeReplaced + "}", replacement);
		if (!replacement.equalsIgnoreCase(key))
		    message = message.replaceAll("\\$\\{" + toBeReplaced + "\\}", replacement);
	    }
	}
	startIndex = message.indexOf("${", endIndex);
	if (startIndex != -1)
	    message = replaceInternationalizedMessages(message, startIndex);
	logger.debug("OUT");
	return message;
    }

 
    /**
     * Subsitute bi object parameters lov profile attributes.
     * 
     * @param obj the obj
     * @param session the session
     * 
     * @throws Exception the exception
     * @throws EMFInternalError the EMF internal error
     */
    public static void subsituteBIObjectParametersLovProfileAttributes(BIObject obj, SessionContainer session)
	    throws Exception, EMFInternalError {
	logger.debug("IN");
	List biparams = obj.getBiObjectParameters();
	Iterator iterParams = biparams.iterator();
	while (iterParams.hasNext()) {
	    // if the param is a Fixed Lov, Make the profile attribute
	    // substitution at runtime
	    BIObjectParameter biparam = (BIObjectParameter) iterParams.next();
	    Parameter param = biparam.getParameter();
	    ModalitiesValue modVal = param.getModalityValue();
	    if (modVal.getITypeCd().equals(SpagoBIConstants.INPUT_TYPE_FIX_LOV_CODE)) {
		String value = modVal.getLovProvider();
		int profileAttributeStartIndex = value.indexOf("${");
		if (profileAttributeStartIndex != -1) {
		    IEngUserProfile profile = (IEngUserProfile) session.getPermanentContainer().getAttribute(
			    IEngUserProfile.ENG_USER_PROFILE);
		    value = SpagoBIUtilities.substituteProfileAttributesInString(value, profile,
			    profileAttributeStartIndex);
		    biparam.getParameter().getModalityValue().setLovProvider(value);
		}
	    }
	}
	logger.debug("OUT");
    }

 
    /**
     * Gets the lov map result.
     * 
     * @param lovs the lovs
     * 
     * @return the lov map result
     */
    public static String getLovMapResult(Map lovs) {
	logger.debug("IN");
	String toReturn = "<DATA>";
	Set keys = lovs.keySet();
	Iterator keyIter = keys.iterator();
	while (keyIter.hasNext()) {
	    String key = (String) keyIter.next();
	    String lovname = (String) lovs.get(key);
	    String lovResult = "";
	    try {
		lovResult = getLovResult(lovname);
	    } catch (Exception e) {
		logger.error("Error while getting result of the lov " + lovname
			+ ", the result of the won't be inserted into the response", e);
		continue;
	    }
	    toReturn = toReturn + "<" + key + ">";
	    toReturn = toReturn + lovResult;
	    toReturn = toReturn + "</" + key + ">";
	}
	toReturn = toReturn + "</DATA>";
	logger.debug("OUT:" + toReturn);
	return toReturn;
    }

    /**
     * Gets the lov result.
     * 
     * @param lovLabel the lov label
     * 
     * @return the lov result
     * 
     * @throws Exception the exception
     */
    public static String getLovResult(String lovLabel) throws Exception {
	logger.debug("IN");
	IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
	ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(lovLabel);
	String toReturn = getLovResult(lov, null);
	logger.debug("OUT:" + toReturn);
	return toReturn;
    }

    /**
     * Gets the lov result.
     * 
     * @param lovLabel the lov label
     * @param profile the profile
     * 
     * @return the lov result
     * 
     * @throws Exception the exception
     */
    public static String getLovResult(String lovLabel, IEngUserProfile profile) throws Exception {
	logger.debug("IN");
	IModalitiesValueDAO lovDAO = DAOFactory.getModalitiesValueDAO();
	ModalitiesValue lov = lovDAO.loadModalitiesValueByLabel(lovLabel);
	String toReturn = getLovResult(lov, profile);
	logger.debug("OUT" + toReturn);
	return toReturn;
    }

    private static String getLovResult(ModalitiesValue lov, IEngUserProfile profile) throws Exception {
	logger.debug("IN");
	if (profile == null) {
	    profile = new UserProfile("anonymous");
	}
	String dataProv = lov.getLovProvider();
	ILovDetail lovDetail = LovDetailFactory.getLovFromXML(dataProv);
	logger.debug("OUT:" + lovDetail.getLovResult(profile));
	return lovDetail.getLovResult(profile);
    }


    
    /**
     * Creates a new user profile, given his identifier.
     * 
     * @param userId The user identifier
     * 
     * @return The newly created user profile
     * 
     * @throws Exception the exception
     */
    public static IEngUserProfile createNewUserProfile(String userId) throws Exception {
    	logger.debug("IN");
    	IEngUserProfile profile = null;
	    try {
	    	ISecurityServiceSupplier supplier = SecurityServiceSupplierFactory.createISecurityServiceSupplier();
			SpagoBIUserProfile user = supplier.createUserProfile(userId);
			user.setFunctions(UserUtilities.readFunctionality(user.getRoles()));
			profile = new UserProfile(user);
	    } catch (Exception e) {
			logger.error("An error occurred while creating user profile for user [" + userId + "]");
			throw new SecurityException("An error occurred while creating user profile for user [" + userId + "]", e);
	    } finally {
	    	logger.debug("OUT");
	    }
	    return profile;
    }


	/**
	 * Returns the complete HTTP URL and puts it into a
	 * string.
	 * 
	 * @param userId the user id
	 * 
	 * @return A String with complete HTTP Url
	 */ 
	public static String getSpagoBIProfileBaseUrl(String userId) {
		logger.debug("IN.Trying to recover spago Adapter HTTP Url. userId="+userId);
		String url = "";
		String path = "";
		String adapUrlStr = "";
		try {
			adapUrlStr = getSpagoAdapterHttpUrl();
			path= getSpagoBiHost()+getSpagoBiContext();
			
	        ConfigSingleton config = ConfigSingleton.getInstance();
	        SourceBean configSB = (SourceBean) config.getAttribute("SPAGOBI_SSO.ACTIVE");
		    String active = (String) configSB.getCharacters();
		    logger.debug("active SSO: " + active);
		    if (active != null && active.equals("true") ){
		    	url = path + adapUrlStr + "?NEW_SESSION=TRUE";
		    }else{
		    	url = path + adapUrlStr + "?NEW_SESSION=TRUE&"+SsoServiceInterface.USER_ID+"="+userId;	
		    }

			
			logger.debug("using URL: " + url);
		} catch (Exception e) {
			logger.error("Error while recovering complete HTTP Url", e);
		}
		logger.debug("OUT");
		return url;
	}   
	
}
