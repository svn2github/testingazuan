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
public class GeneralUtilities {

    private static transient Logger logger = Logger.getLogger(GeneralUtilities.class);
    
    public static final int MAX_DEFAULT_TEMPLATE_SIZE = 5242880;
    public static String SPAGOBI_HOST = null;    
    
    /**
     * The Main method.
     * 
     * @param args String for command line arguments
     */
    public static void main(String[] args) {
    }

    /**
     * Gets the spago adapter http url.
     * 
     * @return the spago adapter http url
     */
    public static String getSpagoAdapterHttpUrl() {
	logger.debug("IN");
	ConfigSingleton config = ConfigSingleton.getInstance();
	String attName = "SPAGOBI.SPAGO_ADAPTERHTTP_URL";
	SourceBean adapUrlSB = (SourceBean) config.getAttribute(attName);
	String adapUrlStr = adapUrlSB.getCharacters();
	adapUrlStr = adapUrlStr.trim();
	logger.debug("OUT:" + adapUrlStr);
	return adapUrlStr;
    }

    /**
     * Gets the default locale.
     * 
     * @return the default locale
     */
    public static Locale getDefaultLocale() {
	logger.debug("IN");
	String country = null;
	String language = null;
	Locale locale = null;
	ConfigSingleton config = ConfigSingleton.getInstance();
	String attName = "SPAGOBI.LANGUAGE_SUPPORTED.LANGUAGE";
	SourceBean languageSB = (SourceBean) config.getFilteredSourceBeanAttribute(attName, "default", "true");
	if (languageSB != null) {
	    country = (String) languageSB.getAttribute("country");
	    language = (String) languageSB.getAttribute("language");
	    if ((country == null) || country.trim().equals("") || (language == null) || language.trim().equals("")) {
		country = "US";
		language = "en";
	    }
	} else {
	    country = "US";
	    language = "en";
	}
	locale = new Locale(language, country);
	logger.debug("OUT:" + locale.toString());
	return locale;
    }

    /**
     * Cleans a string from spaces and tabulation characters.
     * 
     * @param original The input string
     * 
     * @return The cleaned string
     */
    public static String cleanString(String original) {
	logger.debug("IN");
	StringBuffer sb = new StringBuffer();
	char[] arrayChar = original.toCharArray();
	for (int i = 0; i < arrayChar.length; i++) {
	    if ((arrayChar[i] == '\n') || (arrayChar[i] == '\t') || (arrayChar[i] == '\r')) {

	    } else {
		sb.append(arrayChar[i]);
	    }
	}
	logger.debug("OUT:" + sb.toString().trim());
	return sb.toString().trim();
    }

    /**
     * Checks if the Spago errorHandler contains only validation errors.
     * 
     * @param errorHandler The error handler to check
     * 
     * @return true if the errorHandler contains only validation error, false if
     * erroHandler is empty or contains not only validation error.
     */
    public static boolean isErrorHandlerContainingOnlyValidationError(EMFErrorHandler errorHandler) {
	logger.debug("IN");
	boolean contOnlyValImpl = false;
	Collection errors = errorHandler.getErrors();
	if (errors != null && errors.size() > 0) {
	    if (errorHandler.isOKByCategory(EMFErrorCategory.INTERNAL_ERROR)
		    && errorHandler.isOKByCategory(EMFErrorCategory.USER_ERROR)) {
		contOnlyValImpl = true;
	    }
	}
	logger.debug("OUT" + contOnlyValImpl);
	return contOnlyValImpl;
    }

    /**
     * Given an <code>InputStream</code> as input, gets the correspondent
     * bytes array.
     * 
     * @param is The input straeam
     * 
     * @return An array of bytes obtained from the input stream.
     */
    public static byte[] getByteArrayFromInputStream(InputStream is) {
	logger.debug("IN");
	try {
	    java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
	    java.io.BufferedOutputStream bos = new java.io.BufferedOutputStream(baos);

	    int c = 0;
	    byte[] b = new byte[1024];
	    while ((c = is.read(b)) != -1) {
		if (c == 1024)
		    bos.write(b);
		else
		    bos.write(b, 0, c);
	    }
	    bos.flush();
	    byte[] ret = baos.toByteArray();
	    bos.close();
	    return ret;
	} catch (IOException ioe) {
	    logger.error("IOException", ioe);
	    return null;
	} finally {
	    logger.debug("OUT");
	}

    }

    /**
     * Given an <code>InputStream</code> as input flushs the content into an
     * OutputStream and then close the input and output stream.
     * 
     * @param is The input stream
     * @param os The output stream
     * @param closeStreams the close streams
     */
    public static void flushFromInputStreamToOutputStream(InputStream is, OutputStream os, boolean closeStreams) {
	logger.debug("IN");
	try {
	    int c = 0;
	    byte[] b = new byte[1024];
	    while ((c = is.read(b)) != -1) {
		if (c == 1024)
		    os.write(b);
		else
		    os.write(b, 0, c);
	    }
	    os.flush();
	} catch (IOException ioe) {
	    logger.error("IOException", ioe);
	} finally {
	    if (closeStreams) {
		try {
		    if (os != null)
			os.close();
		    if (is != null)
			is.close();
		} catch (IOException e) {
		    logger.error( " Error closing streams", e);
		}

	    }
	    logger.debug("OUT");
	}
    }

    /**
     * From a String identifying the complete name for a file, gets the relative
     * file names, which are substrings of the starting String, according to the
     * java separator "/".
     * 
     * @param completeFileName The string representing the file name
     * 
     * @return relative names substring
     */
    public static String getRelativeFileNames(String completeFileName) {
	logger.debug("IN");
	String linuxSeparator = "/";
	String windowsSeparator = "\\";
	if (completeFileName.indexOf(linuxSeparator) != -1) {
	    completeFileName = completeFileName.substring(completeFileName.lastIndexOf(linuxSeparator) + 1);
	}
	if (completeFileName.indexOf(windowsSeparator) != -1) {
	    completeFileName = completeFileName.substring(completeFileName.lastIndexOf(windowsSeparator) + 1);
	}
	logger.debug("OUT:" + completeFileName);
	return completeFileName;

    }

    /**
     * Returns a string containing the localhost IP address.
     * 
     * @return The IP address String
     */
    public static String getLocalIPAddressAsString() {
	logger.debug("IN");
	String ipAddrStr = "";
	try {
	    InetAddress addr = InetAddress.getLocalHost();
	    byte[] ipAddr = addr.getAddress();

	    // Convert to dot representation

	    for (int i = 0; i < ipAddr.length; i++) {
		if (i > 0) {
		    ipAddrStr += ".";
		}
		ipAddrStr += ipAddr[i] & 0xFF;
	    }
	} catch (UnknownHostException e) {
	    logger.error("UnknownHostException:", e);
	}
	logger.debug("OUT:" + ipAddrStr);
	return ipAddrStr;
    }

    
    /**
     * Returns the context  for SpagoBI 
     * 
     * @return A String with SpagoBI's context 
     */
    public static String getSpagoBiContext() {
	logger.debug("IN");
	String path = "";
	try {
	    logger.debug("Trying to recover spagobi context from ConfigSingleton");
	    ConfigSingleton spagoConfig = ConfigSingleton.getInstance();
	    SourceBean sbTmp = (SourceBean) spagoConfig.getAttribute("SPAGOBI.SPAGOBI_CONTEXT");
	    if (sbTmp!=null){
		path = sbTmp.getCharacters();
	    }else {
		logger.debug("SPAGOBI_CONTEXT not set, using the default value ");
		path="/SpagoBI";
	    }
	    logger.debug("SPAGOBI_CONTEXT: " + path);
	} catch (Exception e) {
	    logger.error("Error while recovering SpagoBI context address", e);
	}
	logger.debug("OUT:" + path);
	return path;
    }      

	private static String readJndiResource(String jndiName) {
		logger.debug("IN.jndiName="+jndiName);
		String value=null;
		try {
			Context ctx = new InitialContext();
			value  = (String)ctx.lookup(jndiName);
			logger.debug("jndiName: " + value);
			 
		} catch (NamingException e) {
		    logger.error(e);
		} catch (Exception e) {
		    logger.error(e);
		} catch (Throwable t) {
		    logger.error(t);
		} finally {
		    logger.debug("OUT.value="+value);
		}
		return value;
	}
	
    /**
     * Returns the address for SpagoBI as an URL and puts it into a
     * string. The information contained are the Server name and port. Before
     * saving, both them are written into the output console.
     * 
     * @return A String with SpagoBI's adderss
     */
    public static String getSpagoBiHost() {
	logger.debug("IN");
	if (SPAGOBI_HOST == null) {
	    try {
		logger.debug("Trying to recover SpagoBiHost from ConfigSingleton");
		ConfigSingleton spagoConfig = ConfigSingleton.getInstance();
		SourceBean sbTmp = (SourceBean) spagoConfig.getAttribute("SPAGOBI.SPAGOBI_HOST_JNDI");
		if (sbTmp != null) {
		    String jndi = sbTmp.getCharacters();
		    SPAGOBI_HOST = readJndiResource(jndi);
		}
		if (SPAGOBI_HOST == null) {
		    logger.debug("SPAGOBI_HOST not set, using the default value ");
		    SPAGOBI_HOST = "http://localhost:8080";
		}
	    } catch (Exception e) {
		logger.error("Error while recovering getSpagoBiHost", e);
	    }
	}
	logger.debug("OUT:" + SPAGOBI_HOST);
	return SPAGOBI_HOST;
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
    	    logger.debug("");
    	    adapUrlStr = getSpagoAdapterHttpUrl();
    	    path= getSpagoBiHost()+getSpagoBiContext();
    	    url = path + adapUrlStr + "?NEW_SESSION=TRUE&userid="+userId;
    	    logger.debug("using URL: " + url);
    	} catch (Exception e) {
    	    logger.error("Error while recovering complete HTTP Url", e);
    	}
    	logger.debug("OUT");
    	return url;
        }
      

    /**
     * Gets the spagoBI's dashboards servlet information as a string.
     * 
     * @return A string containing spagoBI's dashboards servlet information
     */
    public static String getSpagoBiDashboardServlet() {
	return getSpagoBiHost()+getSpagoBiContext() + "/DashboardService";
    }

    
    /**
     * Substitutes parameters with sintax "$P{parameter_name}" whose value is set in the map.
     * 
     * @param statement The string to be modified (tipically a query)
     * @param valuesMap Map name-value
     * 
     * @return The statement with profile attributes replaced by their values.
     * 
     * @throws Exception the exception
     */
    public static String substituteParametersInString(String statement, Map valuesMap)
	    throws Exception {
	logger.debug("IN");
	
	boolean changePars = true;
	while ( changePars ){
		//int profileAttributeStartIndex = statement.indexOf("$P{");
		int profileAttributeStartIndex = statement.indexOf("$P{");
		if (profileAttributeStartIndex != -1) 
		    statement = substituteParametersInString(statement, valuesMap, profileAttributeStartIndex);
		else
		    changePars = false;
		
	}
	logger.debug("OUT");
	return statement;
    }
    
    /**
     * Substitutes the parameters with sintax "$P{attribute_name}" with
     * the correspondent value in the string passed at input.
     * 
     * @param statement
     *                The string to be modified (tipically a query)
     * @param userProfile
     *                The IEngUserProfile object
     * @param profileAttributeStartIndex
     *                The start index for query parsing (useful for recursive
     *                calling)
     * @return The statement with profile attributes replaced by their values.
     * @throws Exception
     */
    private static String substituteParametersInString(String statement, Map valuesMap,
	    int profileAttributeStartIndex) throws Exception {
	logger.debug("IN");
	
	
	int profileAttributeEndIndex = statement.indexOf("}",profileAttributeStartIndex);
	if (profileAttributeEndIndex == -1)
	    throw new Exception("Not closed profile attribute: '}' expected.");
	if (profileAttributeEndIndex < profileAttributeEndIndex)
	    throw new Exception("Not opened profile attribute: '$P{' expected.");
	String attribute = statement.substring(profileAttributeStartIndex + 3, profileAttributeEndIndex).trim();
	int startConfigIndex = attribute.indexOf("(");
	String attributeName = "";
	String prefix = "";
	String split = "";
	String suffix = "";
	boolean attributeExcpetedToBeMultiValue = false;
	if (startConfigIndex != -1) {
	    // the parameter is expected to be multivalue
	    attributeExcpetedToBeMultiValue = true;
	    int endConfigIndex = attribute.length() - 1;
	    if (attribute.charAt(endConfigIndex) != ')')
		throw new Exception(
			"Sintax error: \")\" missing. The expected sintax for "
				+ "attribute profile is $P{attributeProfileName(prefix;split;suffix)} for multivalue profile attributes "
				+ "or $P{attributeProfileName} for singlevalue profile attributes. 'attributeProfileName' must not contain '(' characters.");
	    String configuration = attribute.substring(startConfigIndex + 1, endConfigIndex);
	    String[] configSplitted = configuration.split(";");
	    if (configSplitted == null || configSplitted.length != 3)
		throw new Exception(
			"Sintax error. The expected sintax for "
				+ "attribute profile is $P{attributeProfileName(prefix;split;suffix)} for multivalue profile attributes "
				+ "or $P{attributeProfileName} for singlevalue profile attributes. 'attributeProfileName' must not contain '(' characters. "
				+ "The (prefix;split;suffix) is not properly configured");
	    prefix = configSplitted[0];
	    split = configSplitted[1];
	    suffix = configSplitted[2];
	    logger.debug("Multi-value attribute profile configuration found: prefix: '" + prefix + "'; split: '"
		    + split + "'; suffix: '" + suffix + "'.");
	    attributeName = attribute.substring(0, startConfigIndex);
	    logger.debug("Expected multi-value attribute profile name: '" + attributeName + "'");
	} else {
	    attributeName = attribute;
	    logger.debug("Expected single-value attribute profile name: '" + attributeName + "'");
	}

	String value=(String)valuesMap.get(attributeName);
	if(value==null){
	    throw new Exception("Parameter '" + attributeName + "' not set.");

	}
	else{

		if (value.startsWith("' {")) value = value.substring (1);
		if (value.endsWith("}'")) value = value.substring(0,value.indexOf("}'")+1);
		value = value.trim();
		logger.debug("Parameter value found: " + value);
		String replacement = null;
		String newListOfValues = null;
		if (attributeExcpetedToBeMultiValue) {
		    if (value.startsWith("{")) {
			// the parameter is multi-value
			String[] values = findAttributeValues(value);
			logger.debug("N. " + values.length + " profile attribute values found: '" + values + "'");
			newListOfValues = values[0];
			for (int i = 1; i < values.length; i++) {
			    newListOfValues = newListOfValues + split + values[i];
			}
		    } else {
			logger
				.warn("The attribute value has not the sintax of a multi value attribute; considering it as a single value.");
			newListOfValues = value;
		    }
		} else {
		    if (value.startsWith("{")) {
			// the profile attribute is multi-value
			logger
				.warn("The attribute value seems to be a multi value attribute; trying considering it as a multi value using its own splitter and no prefix and suffix.");
			try {
			    // checks the sintax
			    String[] values = findAttributeValues(value);
			    newListOfValues = values[0];
			    for (int i = 1; i < values.length; i++) {
				newListOfValues = newListOfValues + value.charAt(1) + values[i];
			    }
			} catch (Exception e) {
			    logger
				    .error(
					    "The attribute value does not respect the sintax of a multi value attribute; considering it as a single value.",
					    e);
			    newListOfValues = value;
			}
	    } else {
		newListOfValues = value;
	    }
	}

	replacement = prefix + newListOfValues + suffix;
	if (!replacement.startsWith("'")) replacement = "'" + replacement;
	if (!replacement.endsWith("'")) replacement = replacement + "'";
	attribute = quote(attribute);
	statement = statement.replaceAll("\\$P\\{" + attribute + "\\}", replacement);

//	statement = statement.replaceAll("\\P\\{" + attribute + "\\}", replacement);
/*
	profileAttributeStartIndex = statement.indexOf("$P{", profileAttributeEndIndex-1);
	if (profileAttributeStartIndex != -1)
	    statement = substituteParametersInString(statement, valuesMap, profileAttributeStartIndex);
*/
	logger.debug("OUT");
	}
	
	return statement;
	
	}
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    /**
     * Substitutes the profile attributes with sintax "${attribute_name}" with
     * the correspondent value in the string passed at input.
     * 
     * @param statement The string to be modified (tipically a query)
     * @param profile The IEngUserProfile object
     * 
     * @return The statement with profile attributes replaced by their values.
     * 
     * @throws Exception the exception
     */
    public static String substituteProfileAttributesInString(String statement, IEngUserProfile profile)
	    throws Exception {
	logger.debug("IN");
	int profileAttributeStartIndex = statement.indexOf("${");
	if (profileAttributeStartIndex != -1) {
	    statement = substituteProfileAttributesInString(statement, profile, profileAttributeStartIndex);
	}
	logger.debug("OUT");
	return statement;
    }

    
    
    /**
     * Substitutes the profile attributes with sintax "${attribute_name}" with
     * the correspondent value in the string passed at input.
     * 
     * @param statement
     *                The string to be modified (tipically a query)
     * @param profile
     *                The IEngUserProfile object
     * @param profileAttributeStartIndex
     *                The start index for query parsing (useful for recursive
     *                calling)
     * @return The statement with profile attributes replaced by their values.
     * @throws Exception
     */
    private static String substituteProfileAttributesInString(String statement, IEngUserProfile profile,
	    int profileAttributeStartIndex) throws Exception {
	logger.debug("IN");
	int profileAttributeEndIndex = statement.indexOf("}",profileAttributeStartIndex);
	if (profileAttributeEndIndex == -1)
	    throw new Exception("Not closed profile attribute: '}' expected.");
	if (profileAttributeEndIndex < profileAttributeEndIndex)
	    throw new Exception("Not opened profile attribute: '${' expected.");
	String attribute = statement.substring(profileAttributeStartIndex + 2, profileAttributeEndIndex).trim();
	int startConfigIndex = attribute.indexOf("(");
	String attributeName = "";
	String prefix = "";
	String split = "";
	String suffix = "";
	boolean attributeExcpetedToBeMultiValue = false;
	if (startConfigIndex != -1) {
	    // the attribute profile is expected to be multivalue
	    attributeExcpetedToBeMultiValue = true;
	    int endConfigIndex = attribute.length() - 1;
	    if (attribute.charAt(endConfigIndex) != ')')
		throw new Exception(
			"Sintax error: \")\" missing. The expected sintax for "
				+ "attribute profile is ${attributeProfileName(prefix;split;suffix)} for multivalue profile attributes "
				+ "or ${attributeProfileName} for singlevalue profile attributes. 'attributeProfileName' must not contain '(' characters.");
	    String configuration = attribute.substring(startConfigIndex + 1, endConfigIndex);
	    String[] configSplitted = configuration.split(";");
	    if (configSplitted == null || configSplitted.length != 3)
		throw new Exception(
			"Sintax error. The expected sintax for "
				+ "attribute profile is ${attributeProfileName(prefix;split;suffix)} for multivalue profile attributes "
				+ "or ${attributeProfileName} for singlevalue profile attributes. 'attributeProfileName' must not contain '(' characters. "
				+ "The (prefix;split;suffix) is not properly configured");
	    prefix = configSplitted[0];
	    split = configSplitted[1];
	    suffix = configSplitted[2];
	    logger.debug("Multi-value attribute profile configuration found: prefix: '" + prefix + "'; split: '"
		    + split + "'; suffix: '" + suffix + "'.");
	    attributeName = attribute.substring(0, startConfigIndex);
	    logger.debug("Expected multi-value attribute profile name: '" + attributeName + "'");
	} else {
	    attributeName = attribute;
	    logger.debug("Expected single-value attribute profile name: '" + attributeName + "'");
	}

	Object attributeValueObj = profile.getUserAttribute(attributeName);
	if (attributeValueObj == null || attributeValueObj.toString().trim().equals(""))
	    throw new Exception("Profile attribute '" + attributeName + "' not existing.");

	String attributeValue = attributeValueObj.toString();
	logger.debug("Profile attribute value found: '" + attributeValue + "'");
	String replacement = null;
	String newListOfValues = null;
	if (attributeExcpetedToBeMultiValue) {
	    if (attributeValue.startsWith("{")) {
		// the profile attribute is multi-value
		String[] values = findAttributeValues(attributeValue);
		logger.debug("N. " + values.length + " profile attribute values found: '" + values + "'");
		newListOfValues = values[0];
		for (int i = 1; i < values.length; i++) {
		    newListOfValues = newListOfValues + split + values[i];
		}
	    } else {
		logger
			.warn("The attribute value has not the sintax of a multi value attribute; considering it as a single value.");
		newListOfValues = attributeValue;
	    }
	} else {
	    if (attributeValue.startsWith("{")) {
		// the profile attribute is multi-value
		logger
			.warn("The attribute value seems to be a multi value attribute; trying considering it as a multi value using its own splitter and no prefix and suffix.");
		try {
		    // checks the sintax
		    String[] values = findAttributeValues(attributeValue);
		    newListOfValues = values[0];
		    for (int i = 1; i < values.length; i++) {
			newListOfValues = newListOfValues + attributeValue.charAt(1) + values[i];
		    }
		} catch (Exception e) {
		    logger
			    .error(
				    "The attribute value does not respect the sintax of a multi value attribute; considering it as a single value.",
				    e);
		    newListOfValues = attributeValue;
		}
	    } else {
		newListOfValues = attributeValue;
	    }
	}

	replacement = prefix + newListOfValues + suffix;
	attribute = quote(attribute);
	statement = statement.replaceAll("\\$\\{" + attribute + "\\}", replacement);

	profileAttributeStartIndex = statement.indexOf("${", profileAttributeEndIndex);
	if (profileAttributeStartIndex != -1)
	    statement = substituteProfileAttributesInString(statement, profile, profileAttributeStartIndex);
	logger.debug("OUT");
	return statement;
    }

    /*
     * This method exists since jdk 1.5 (java.util.regexp.Patter.quote())
     */
    /**
     * Quote.
     * 
     * @param s the s
     * 
     * @return the string
     */
    public static String quote(String s) {
	logger.debug("IN");
	int slashEIndex = s.indexOf("\\E");
	if (slashEIndex == -1)
	    return "\\Q" + s + "\\E";

	StringBuffer sb = new StringBuffer(s.length() * 2);
	sb.append("\\Q");
	slashEIndex = 0;
	int current = 0;
	while ((slashEIndex = s.indexOf("\\E", current)) != -1) {
	    sb.append(s.substring(current, slashEIndex));
	    current = slashEIndex + 2;
	    sb.append("\\E\\\\E\\Q");
	}
	sb.append(s.substring(current, s.length()));
	sb.append("\\E");
	logger.debug("OUT");
	return sb.toString();
    }

    /**
     * Find the attribute values in case of multi value attribute. The sintax
     * is: {splitter character{list of values separated by the splitter}}.
     * Examples: {;{value1;value2;value3....}} {|{value1|value2|value3....}}
     * 
     * @param attributeValue
     *                The String representing the list of attribute values
     * @return The array of attribute values
     * @throws Exception
     *                 in case of sintax error
     */
    public static String[] findAttributeValues(String attributeValue) throws Exception {
	logger.debug("IN");
	String sintaxErrorMsg = "Multi value attribute sintax error.";
	if (attributeValue.length() < 6)
	    throw new Exception(sintaxErrorMsg);
	if (!attributeValue.endsWith("}}"))
	    throw new Exception(sintaxErrorMsg);
	if (attributeValue.charAt(2) != '{')
	    throw new Exception(sintaxErrorMsg);
	char splitter = attributeValue.charAt(1);
	String valuesList = attributeValue.substring(3, attributeValue.length() - 2);
	String[] values = valuesList.split(String.valueOf(splitter));
	logger.debug("OUT");
	return values;
    }

    /**
     * Gets the all profile attributes.
     * 
     * @param profile the profile
     * 
     * @return the all profile attributes
     * 
     * @throws EMFInternalError the EMF internal error
     */
    public static HashMap getAllProfileAttributes(IEngUserProfile profile) throws EMFInternalError {
	logger.debug("IN");
	if (profile == null)
	    throw new EMFInternalError(EMFErrorSeverity.ERROR,
		    "getAllProfileAttributes method invoked with null input profile object");
	HashMap profileattrs = new HashMap();
	Collection profileattrsNames = profile.getUserAttributeNames();
	if (profileattrsNames == null || profileattrsNames.size() == 0)
	    return profileattrs;
	Iterator it = profileattrsNames.iterator();
	while (it.hasNext()) {
	    Object profileattrName = it.next();
	    Object profileattrValue = profile.getUserAttribute(profileattrName.toString());
	    profileattrs.put(profileattrName, profileattrValue);
	}
	logger.debug("OUT");
	return profileattrs;
    }

    /**
     * Delete a folder and its contents.
     * 
     * @param dir The java file object of the directory
     * 
     * @return the result of the operation
     */
    public static boolean deleteDir(File dir) {
	logger.debug("IN");
	if (dir.isDirectory()) {
	    String[] children = dir.list();
	    for (int i = 0; i < children.length; i++) {
		boolean success = deleteDir(new File(dir, children[i]));
		if (!success) {
		    return false;
		}
	    }
	}
	logger.debug("OUT");
	return dir.delete();
    }

    /**
     * Delete contents of a directory.
     * 
     * @param dir The java file object of the directory
     * 
     * @return the result of the operation
     */
    public static boolean deleteContentDir(File dir) {
	logger.debug("IN");
	if (dir.isDirectory()) {
	    String[] children = dir.list();
	    for (int i = 0; i < children.length; i++) {
		boolean success = deleteDir(new File(dir, children[i]));
		if (!success) {
		    logger.debug("OUT");
		    return false;
		}
	    }
	}
	logger.debug("OUT");
	return true;
    }

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
     * Questo metodo permette di sostituire una parte di una stringa con
     * un'altra.
     * 
     * @param toParse stringa da manipolare.
     * @param replacing parte di stringa da sostituire.
     * @param replaced stringa nuova.
     * 
     * @return the string
     */
    public static String replace(String toParse, String replacing, String replaced) {
	logger.debug("IN");
	if (toParse == null) {
	    return toParse;
	} // if (toParse == null)
	if (replacing == null) {
	    return toParse;
	} // if (replacing == null)
	if (replaced != null) {
	    int parameterIndex = toParse.indexOf(replacing);
	    while (parameterIndex != -1) {
		String newToParse = toParse.substring(0, parameterIndex);
		newToParse += replaced;
		newToParse += toParse.substring(parameterIndex + replacing.length(), toParse.length());
		toParse = newToParse;
		parameterIndex = toParse.indexOf(replacing, parameterIndex + replaced.length());
	    } // while (parameterIndex != -1)
	} // if (replaced != null)
	logger.debug("OUT");
	return toParse;
    } // public static String replace(String toParse, String replacing, String
	// replaced)

    /**
	 * Questo metodo implementa la stessa logica della funzione javascript
	 * <em>escape</em>.
	 * 
	 * @param input stringa da manipolare.
	 * 
	 * @return the string
	 */
    public static String encode(String input) {
	/*
	 * input = replace(input, "%", "%25"); input = replace(input, " ",
	 * "%20"); input = replace(input, "\"", "%22"); input = replace(input,
	 * "'", "%27"); input = replace(input, "<", "%3C"); input =
	 * replace(input, "<", "%3E"); input = replace(input, "?", "%3F");
	 * input = replace(input, "&", "%26");
	 */
	// input = replace(input, " ", "&#160;");
	input = replace(input, " ", "_");
	return input;
    }

    /**
     * Questo metodo implementa la stessa logica della funzione javascript
     * <em>escape</em>.
     * 
     * @param input stringa da manipolare.
     * 
     * @return the string
     */
    public static String decode(String input) {
	/*
	 * input = replace(input, "%25", "%"); input = replace(input, "%20", "
	 * "); input = replace(input, "%22", "\""); input = replace(input,
	 * "%27", "'"); input = replace(input, "%3C", "<"); input =
	 * replace(input, "%3E", "<"); input = replace(input, "%3F", "?");
	 * input = replace(input, "%26", "&");
	 */
	// input = replace(input, "&#160;", " ");
	input = replace(input, "_", " ");
	return input;
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
		    value = GeneralUtilities.substituteProfileAttributesInString(value, profile,
			    profileAttributeStartIndex);
		    biparam.getParameter().getModalityValue().setLovProvider(value);
		}
	    }
	}
	logger.debug("OUT");
    }

    /**
     * Substitute quotes into string.
     * 
     * @param value the value
     * 
     * @return the string
     */
    public static String substituteQuotesIntoString(String value) {
	logger.debug("IN");
	if (value == null) value = "";
	String singleQuoteString = "'";
	String doubleQuoteString = new String();
	char doubleQuoteChar = '"';
	doubleQuoteString += doubleQuoteChar;
	String singleQuoteReplaceString = "&#39;";
	String doubleQuotesReplaceString = "&#34;";
	value = value.replaceAll(singleQuoteString, singleQuoteReplaceString);
	value = value.replaceAll(doubleQuoteString, doubleQuotesReplaceString);
	logger.debug("OUT:" + value);
	return value;

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
     * From list to string.
     * 
     * @param values the values
     * @param separator the separator
     * 
     * @return the string
     */
    public static String fromListToString(List values, String separator) {
	logger.debug("IN");
	String valStr = "";
	if (values == null) {
	    return valStr;
	}
	Iterator iterVal = values.iterator();
	while (iterVal.hasNext()) {
	    String val = (String) iterVal.next();
	    valStr += val + separator;
	}
	if (valStr.length() != 0) {
	    valStr = valStr.substring(0, valStr.length() - separator.length());
	}
	logger.debug("OUT:" + valStr);
	return valStr;
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
			profile = new UserProfile(user);
	    } catch (Exception e) {
			logger.error("Reading user information... ERROR", e);
			throw new SecurityException();
	    } finally {
	    	logger.debug("OUT");
	    }
	    return profile;
    }

    /**
     * Finds the user identifier from http request or from SSO system (by the http request in input).
     * If SSO is enabled, the identifier specified on http request must be equal to the user identifier detected by the SSO system.
     * In case the http request does not contain the user identifier and SSO in disabled, null is returned.
     * 
     * @param httpRequest The http request
     * 
     * @return the current user unique identified
     * 
     * @throws Exception in case the SSO is enabled and the user identifier specified on http request is different from the SSO detected one.
     */
    public static String findUserId(HttpServletRequest httpRequest) throws Exception {
    	logger.debug("IN");
    	String userId = null;
    	try {
    		// Get userid from request
    		String requestUserId = httpRequest.getParameter("userid");
    		userId = findUserId(requestUserId, httpRequest.getSession());
    	} finally {
    		logger.debug("OUT: userId = [" + userId + "]");
    	}
    	return userId;
    }
    
    /**
     * Finds the user identifier from service request or from SSO system (by the http request in input).
     * If SSO is enabled, the identifier specified on service request must be equal to the user identifier detected by the SSO system.
     * In case the service request does not contain the user identifier and SSO in disabled, null is returned.
     * 
     * @param httpRequest The http request
     * @param serviceRequest the service request
     * 
     * @return the current user unique identified
     * 
     * @throws Exception in case the SSO is enabled and the user identifier specified on service request is different from the SSO detected one.
     */
    public static String findUserId(SourceBean serviceRequest, HttpServletRequest httpRequest) throws Exception {
    	logger.debug("IN");
    	String userId = null;
    	try {
    		String requestUserId = null;
    		// Get userid from request
	    	Object requestUserIdObj = serviceRequest.getAttribute("userid");
	    	if (requestUserIdObj != null) requestUserId = requestUserIdObj.toString();
	    	userId = findUserId(requestUserId, httpRequest.getSession());
    	} finally {
    		logger.debug("OUT: userId = [" + userId + "]");
    	}
    	return userId;
    }

    private static String findUserId(String requestUserId, HttpSession httpSession) throws Exception {
    	logger.debug("IN");
    	String userId = null;
    	try {
	    	String sessionUserId = null;
	    	// Check if SSO is active
	    	ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	    	SourceBean validateSB = (SourceBean) serverConfig.getAttribute("SPAGOBI_SSO.ACTIVE");
	    	String active = (String) validateSB.getCharacters();
	    	// If SSO is active gets userid in session
	    	if (active != null && active.equals("true")) {
	    	    SsoServiceInterface ssoProxy = SsoServiceFactory.createProxyService();
	    	    sessionUserId = ssoProxy.readUserIdentifier(httpSession);
	    	    if (sessionUserId != null) {
	    	    	logger.debug("requestUserId: " + requestUserId );
	    	    	logger.debug("sessionUserId: " + sessionUserId );
	    			// if userid in session is different from userid in request throws an exception
	    	    	/*
	    			if (requestUserId != null && !requestUserId.equals(sessionUserId)) {
	    			    logger.error("The user identifier specified on service request is diferent from the one detected by SSO system: " +
	    			    		"requestUserId=" + requestUserId + "/sessionUserId=" + sessionUserId);
	    			    throw new SecurityException("Invalid userid");
	    			}
	    			*/
	    	    	return sessionUserId;
	    	    } else {
	    			logger.error("User id was not found in session");
	    			throw new SecurityException("User id was not found in session");
	    	    }
	    	}
	    	if (sessionUserId != null) userId = sessionUserId;
	    	// warning: the request user id can be null
	    	else userId = requestUserId;
    	} finally {
    		logger.debug("OUT");
    	}
    	return userId;
    }
    
	/**
	 * Checks if the String in input contains a reference to System property with the syntax
	 * ${property_name}, and, in case, substitutes the reference with the actual value.
	 * @return the string with reference to System property replaced with actual value.
	 */
	public static String checkForSystemProperty(String input) {
		logger.debug("IN");
		if (input == null) {
			logger.debug("Input string is null; returning null");
			return null;
		}
		String toReturn = input;
	    int beginIndex = input.indexOf("${");
	    if (beginIndex != - 1) {
	    	int endIndex = input.indexOf("}", beginIndex);
	    	if (endIndex != -1) {
	    		String propertyName = toReturn.substring(beginIndex + 2, endIndex);
	    		logger.debug("Found reference to property " + propertyName);
	    		String propertyValue = System.getProperty(propertyName);
	    		logger.debug("Property with name = [" + propertyName + "] has value = [" + propertyValue + "]");
	    		if (propertyValue != null && !propertyValue.trim().equals("")) {
	    			toReturn = toReturn.substring(0, beginIndex) + propertyValue + toReturn.substring(endIndex + 1);
	    		} else {
	    			logger.warn("Property with name = [" + propertyName + "] has no proper value.");
	    		}
	    	}
	    }
	    logger.debug("OUT: toReturn = [" + toReturn + "]");
	    return toReturn;
	}
    
	public static int getTemplateMaxSize() {
		logger.debug("IN");
		int toReturn = MAX_DEFAULT_TEMPLATE_SIZE;
		try {
			ConfigSingleton serverConfig = ConfigSingleton.getInstance();
	    	SourceBean maxSizeSB = (SourceBean) serverConfig.getAttribute("SPAGOBI.TEMPLATE_MAX_SIZE");
	    	if (maxSizeSB != null) {
	    		String maxSizeStr = (String) maxSizeSB.getCharacters();
	    		logger.debug("Configuration found for max template size: " + maxSizeStr);
	    		Integer maxSizeInt = new Integer(maxSizeStr);
	    		toReturn = maxSizeInt.intValue();
	    	} else {
	    		logger.debug("No configuration found for max template size");
	    	}
		} catch (Exception e) {
			logger.error("Error while retrieving max template size", e);
			logger.debug("Considering default value " + MAX_DEFAULT_TEMPLATE_SIZE);
			toReturn = MAX_DEFAULT_TEMPLATE_SIZE;
		}
		logger.debug("OUT: max size = " + toReturn);
		return toReturn;
	}
	
}
