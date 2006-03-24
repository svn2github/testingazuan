package it.eng.spago.cms.util;

import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spago.cms.CmsProperty;
import it.eng.spago.cms.util.constants.PropertiesConstants;
import it.eng.spago.error.EMFInternalError;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;

/**
 * 
 * Utility class for extract information from the operation sourceBean descriptor.
 * An operation is represented with a SourceBean 
 * that contains necessary information and parameters for the execution  
 * The methods allow to extract the parameters in format String, Boolean and Object.
 * 
 */


public class OperationDescriptorUtils {

	
	
	/**
	 * 
	 * The method searches the attibute, specified in input, inside the SourceBean 
	 * of operation description and it gives back the value as a String.  
	 * The attribute can be specified also with the punctual notation.  
	 * If the attribute does't exist and isn't obligatory the method returns null, 
	 * instead, if it's obligatory but it does not exist or its value 
	 * is an empty string the method throws an exception.
	 * 
	 * @param params SourceBean of operation description
	 * @param attr name of the attribute to search inside the SourceBean
	 * @param mandatory flag to indicate if the attribute is mandatory
	 * @return String the string value of the attribute find or null if the attribute 
	 * doesn't exist and isn't mandatory
	 * @throws EMFInternalError the exception will be throw if:
	 * <ul>
	 * <li>the attribute is mandatory but doesn't exist</li>
	 * <li>the value of the attribute is an empty string and is mandatory</li>
	 * </ul>
	 * 
	 */
	public static String getStringAttribute(SourceBean params, String attr) {
		
		String valAttr = null;
		try {
			valAttr = (String)params.getAttribute(attr);
			valAttr = valAttr.trim();
		} catch (Exception e) {
				return null;
		}
		return valAttr;
	}
	
	
    /**
     * 
     * The method searches the attibute, specified in input, inside the SourceBean 
	 * of operation description and it gives back the value as a Boolean.  
	 * The attribute can be specified also with the punctual notation.  
	 * If the attribute does't exist and isn't obligatory the method returns null, 
	 * instead, if it's obligatory but it does't exist or its value 
	 * is empty the method throws an exception.
	 * 
	 * @param params SourceBean of operation description
	 * @param attr name of the attribute to search inside the SourceBean
	 * @param mandatory flag to indicate if the attribute is mandatory
	 * @return Boolean, the Boolean value of the attribute find or null if the attribute 
	 * doesn't exist and isn't mandatory
	 * @throws EMFInternalError the exception will be throw if:
	 * <ul>
	 * <li>the attribute is mandatory but doesn't exist</li>
	 * <li>the value of the attribute is empty and is mandatory</li>
	 * </ul>
     * 
     */
	public static Boolean getBooleanAttribute(SourceBean params, String attr) {
		Boolean valAttr = null;
		String valAttrStr = null;
		try {
			valAttrStr = (String)params.getAttribute(attr);
			valAttrStr = valAttrStr.trim();
		} catch (Exception e) {
			return null;
		}
		if(valAttrStr.equalsIgnoreCase("true")) {
			valAttr = new Boolean(true);
		} else {
			valAttr = new Boolean(false);
		}	
		return valAttr;
	}
	
	
	/**
     * 
     * The method searches the attibute, specified in input, inside the SourceBean 
	 * of operation description and it returns the value as an Object.  
	 * The attribute can be specified also with the punctual notation.  
	 * If the attribute does't exist and isn't obligatory the method returns null, 
	 * instead, if it's obligatory but it does't exist the method throws an exception.
	 * 
	 * @param params SourceBean of operation description
	 * @param attr name of the attribute to search inside the SourceBean
	 * @param mandatory flag to indicate if the attribute is mandatory
	 * @return Object the Object value of the attribute find or null if the attribute 
	 * doesn't exist and isn't mandatory
	 * @throws EMFInternalError the exception will be throw if:
	 * <ul>
	 * <li>the attribute is mandatory but doesn't exist</li>
	 * </ul>
	 * 
     */
	public static Object getObjectAttribute(SourceBean params, String attr) {
		
		Object valAttr = null;
		valAttr = params.getAttribute(attr);	
		return valAttr;
	}
	
	
	
	/**
	 * The method searches the properties specified inside an operation SourceBean description
	 * and it gives back an Hashtable contains all the properties pair value (name, value).
	 * Inside an operation the property must be specified with the follow sintax
	 * <pre>
	 * 		<PROPERTIES>
  		 		<PROPERTY name="nameProperty" type="tyepProperty"/>
  		 			<PROPERTYVALUE value="propertyvalue" />
  		 		...
  		 	</PROPERTIES>
	 * </pre>
	 */
	public static Hashtable getProperties(SourceBean params) {
		Hashtable properties = new Hashtable();
		SourceBean propertiesSB = (SourceBean)params.getAttribute(PropertiesConstants.PROPERTIES);
	    if(propertiesSB != null) {
	    	List propertiesList = propertiesSB.getAttributeAsList(PropertiesConstants.PROPERTY);
	    	for(int ip=0; ip<propertiesList.size(); ip++) {
	    		CmsProperty prop = null;
	    		SourceBean propSB = (SourceBean)propertiesList.get(ip);
	    		//SourceBean propSB = (SourceBean)propSBA.getValue();
	    		String nameprop = (String)propSB.getAttribute(PropertiesConstants.NAME_PROPERTY);
	    		String typeprop = (String)propSB.getAttribute(PropertiesConstants.TYPE_PROPERTY);
	    		List valuesList = propSB.getAttributeAsList(PropertiesConstants.VALUE_PROPERTY_SB);
	    		int numValues = valuesList.size();
	    		if(typeprop.equalsIgnoreCase(CmsProperty.TYPE_STRING)) {
	    			String[] values = new String[numValues];
	    			for(int j=0; j<valuesList.size(); j++) {
		    			SourceBean valueSB = (SourceBean)valuesList.get(j);
		    			values[j] = (String)valueSB.getAttribute(PropertiesConstants.VALUE_PROPERTY);
	    			}
	    			prop = new CmsProperty(nameprop, values);
	    		}
	    		if(typeprop.equalsIgnoreCase(CmsProperty.TYPE_LONG)) {
	    			Long[] values = new Long[numValues];
	    			for(int j=0; j<valuesList.size(); j++) {
		    			SourceBean valueSB = (SourceBean)valuesList.get(j);
		    			values[j] = (Long)valueSB.getAttribute(PropertiesConstants.VALUE_PROPERTY);
	    			}
	    			prop = new CmsProperty(nameprop, values);
	    		}
	    		if(typeprop.equalsIgnoreCase(CmsProperty.TYPE_DOUBLE)) {
	    			Double[] values = new Double[numValues];
	    			for(int j=0; j<valuesList.size(); j++) {
		    			SourceBean valueSB = (SourceBean)valuesList.get(j);
		    			values[j] = (Double)valueSB.getAttribute(PropertiesConstants.VALUE_PROPERTY);
	    			}
	    			prop = new CmsProperty(nameprop, values);
	    		}
	    		if(typeprop.equalsIgnoreCase(CmsProperty.TYPE_DATE)) {
	    			Calendar[] values = new Calendar[numValues];
	    			for(int j=0; j<valuesList.size(); j++) {
		    			SourceBean valueSB = (SourceBean)valuesList.get(j);
		    			values[j] = (Calendar)valueSB.getAttribute(PropertiesConstants.VALUE_PROPERTY);
	    			}
	    			prop = new CmsProperty(nameprop, values);
	    		}
	    		if(typeprop.equalsIgnoreCase(CmsProperty.TYPE_BOOLEAN)) {
	    			Boolean[] values = new Boolean[numValues];
	    			for(int j=0; j<valuesList.size(); j++) {
		    			SourceBean valueSB = (SourceBean)valuesList.get(j);
		    			values[j] = (Boolean)valueSB.getAttribute(PropertiesConstants.VALUE_PROPERTY);
	    			}
	    			prop = new CmsProperty(nameprop, values);
	    		}
	    		if(typeprop.equalsIgnoreCase(CmsProperty.TYPE_BINARY)) {
	    			InputStream[] values = new InputStream[numValues];
	    			for(int j=0; j<valuesList.size(); j++) {
		    			SourceBean valueSB = (SourceBean)valuesList.get(j);
		    			values[j] = (InputStream)valueSB.getAttribute(PropertiesConstants.VALUE_PROPERTY);
	    			}
	    			prop = new CmsProperty(nameprop, values);
	    		}
	    		properties.put(nameprop, prop);
	    	}
	    }
	   return properties;
	}
	

}
