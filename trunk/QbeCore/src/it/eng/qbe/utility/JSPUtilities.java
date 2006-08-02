/*
 * Created on 3-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.utility;



/**
 * @author Andrea Zoppello
 * @deprecated 
 * A class with some JSP Utilities
 */
public class JSPUtilities {
	
	private static final String DOT_SEPARATOR=".";
	private static final String SPAGO_REQUEST_COMPATIBLE_DOT_SEPARATOR=":";
	
	public static String getSpagoCompatibleRequestName(String originalRequestFieldName){
		return it.eng.spago.util.StringUtils.replace(originalRequestFieldName, DOT_SEPARATOR,SPAGO_REQUEST_COMPATIBLE_DOT_SEPARATOR);
	}

	public static String getOriginalParameterRequestName(String spagoCompatibleRequestName){
		return it.eng.spago.util.StringUtils.replace(spagoCompatibleRequestName, SPAGO_REQUEST_COMPATIBLE_DOT_SEPARATOR,DOT_SEPARATOR);
	}
}

