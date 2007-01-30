package it.eng.spagobi.engines.talend.messages;

import it.eng.spagobi.engines.talend.TalendServlet;

import java.util.HashMap;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

public class TalendMessageBundle {

	private static final String DEFAULT_BUNDLE = "messages";
	private static HashMap bundles = null;
	private static transient Logger logger = Logger.getLogger(TalendServlet.class);
	
    static {
        bundles = new HashMap();
    }
	
    /**
     * Returns an internazionalized message
     * 
     * @param code the code of the message.
     * @param bundle the message bundle.
     * @return the internazionalized message.
     */
    public static String getMessage(String code, String bundle, Locale userLocale) {
        
    	logger.debug("Input parameters: code = [" + code + "] ; bundle = [" + bundle + "] ; " +
    			"userlocale = [" + userLocale + "]");
    	if (bundle == null || bundle.trim().equals("")) {
        	logger.debug("Bundle not specified; considering \"" + DEFAULT_BUNDLE + "\" as default value");
    		bundle = DEFAULT_BUNDLE;
    	}
    	
        String bundleKey = bundle + "_" + userLocale.getLanguage() + userLocale.getCountry();
        ResourceBundle messages = null;
        if (bundles.containsKey(bundleKey)) {
            messages = (ResourceBundle) bundles.get(bundleKey);
        } else {
            // First access to this bundle
            try {
                messages = ResourceBundle.getBundle(bundle, userLocale);
            } catch (java.util.MissingResourceException ex) {
                logger.error("ResourceBundle with bundle = [" + bundle + "] and locale = " +
                		"[" + userLocale + "] missing.");
            }
            
            // Put bundle in cache
            bundles.put(bundleKey, messages);
        }
        
        if (messages == null) {
            // Bundle non existent
            return code;
        } // if (messages == null)

        String message = null;
        try {
            message = messages.getString(code);
        } // try
        catch (Exception ex) {
            // No trace: may be this is not an error
        } // catch (Exception ex)
        if (message == null) return code;
        else return message;
    }
	
    public static String getMessage(String code, Locale userLocale) {
    	return getMessage(code, DEFAULT_BUNDLE, userLocale);
    }
    
    public static String getMessage(String code, String bundle, Locale userLocale, String[] arguments) {
    	String message = getMessage(code, DEFAULT_BUNDLE, userLocale);
        for (int i = 0; i < arguments.length; i++){
        	message = replace(message, i, arguments[i].toString());
        }
    	return message;
    }
    
    public static String getMessage(String code, Locale userLocale, String[] arguments) {
    	return getMessage(code, DEFAULT_BUNDLE, userLocale, arguments);
    }
    
    /**
     * Substitutes the message value to the placeholders.
     * 
     * @param messageFormat The String representing the message format
     * @param iParameter	The numeric value defining the replacing string
     * @param value	Input object containing parsing information
     * @return	The parsed string
     */
    protected static String replace(String messageFormat, int iParameter, Object value) {
		if (value != null) {
			String toParse = messageFormat;
			String replacing = "%" + iParameter;
			String replaced = value.toString();
			StringBuffer parsed = new StringBuffer();
			int parameterIndex = toParse.indexOf(replacing);
			while (parameterIndex != -1) {
				parsed.append(toParse.substring(0, parameterIndex));
				parsed.append(replaced);
				toParse = toParse.substring(
						parameterIndex + replacing.length(), toParse.length());
				parameterIndex = toParse.indexOf(replacing);
			} // while (parameterIndex != -1)
			parsed.append(toParse);
			return parsed.toString();
		} else {
			return messageFormat;
		}
	}
}
