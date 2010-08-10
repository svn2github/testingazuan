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
package it.eng.spagobi.commons.utilities;

import java.util.Map;

import org.apache.log4j.Logger;

import it.eng.spago.security.IEngUserProfile;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class StringUtilities {
	 
	private static transient Logger logger = Logger.getLogger(StringUtilities.class);
	    

	
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
     * Substitutes parameters with sintax "$P{parameter_name}" whose value is set in the map.
     * 
     * @param statement The string to be modified (tipically a query)
     * @param valuesMap Map name-value
     * @param surroundWithQuotes flag: if true, the replacement will be surrounded by quotes if they are missing
     * 
     * @return The statement with profile attributes replaced by their values.
     * 
     * @throws Exception the exception
     */
    public static String substituteParametersInString(String statement, Map valuesMap, boolean surroundWithQuotes)
	    throws Exception {
	logger.debug("IN");
	
	boolean changePars = true;
	while ( changePars ){
		//int profileAttributeStartIndex = statement.indexOf("$P{");
		int profileAttributeStartIndex = statement.indexOf("$P{");
		if (profileAttributeStartIndex != -1) 
		    statement = substituteParametersInString(statement, valuesMap, profileAttributeStartIndex, surroundWithQuotes);
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
     * @param surroundWithQuotes 
     * 				  Flag: if true, the replacement will be surrounded by quotes if they are missing
     * 
     * @return The statement with profile attributes replaced by their values.
     * @throws Exception
     */
    private static String substituteParametersInString(String statement, Map valuesMap,
	    int profileAttributeStartIndex, boolean surroundWithQuotes) throws Exception {
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
	if (surroundWithQuotes) {
		if (!replacement.startsWith("'")) replacement = "'" + replacement;
		if (!replacement.endsWith("'")) replacement = replacement + "'";
	}
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
    
	public static boolean isNull(String str) {
		return str == null;
	}
	
	public static boolean isEmpty(String str) {
		return isNull( str ) || "".equals( str.trim() );
	}
		
	public static boolean containsOnlySpaces(String str) {
		return !isNull( str ) && isEmpty( str );
	}
}
