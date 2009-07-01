/**
 * SpagoBI - The Business Intelligence Free Platform
 *
 * Copyright (C) 2004 - 2008 Engineering Ingegneria Informatica S.p.A.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 **/
package it.eng.qbe.utility;


// TODO: Auto-generated Javadoc
/**
 * The Class JSPUtilities.
 * 
 * @author Andrea Zoppello
 * @deprecated
 * A class with some JSP Utilities
 */
public class JSPUtilities {
	
	/** The Constant DOT_SEPARATOR. */
	private static final String DOT_SEPARATOR=".";
	
	/** The Constant SPAGO_REQUEST_COMPATIBLE_DOT_SEPARATOR. */
	private static final String SPAGO_REQUEST_COMPATIBLE_DOT_SEPARATOR=":";
	
	/**
	 * Gets the spago compatible request name.
	 * 
	 * @param originalRequestFieldName the original request field name
	 * 
	 * @return the spago compatible request name
	 */
	public static String getSpagoCompatibleRequestName(String originalRequestFieldName){
		return it.eng.spago.util.StringUtils.replace(originalRequestFieldName, DOT_SEPARATOR,SPAGO_REQUEST_COMPATIBLE_DOT_SEPARATOR);
	}

	/**
	 * Gets the original parameter request name.
	 * 
	 * @param spagoCompatibleRequestName the spago compatible request name
	 * 
	 * @return the original parameter request name
	 */
	public static String getOriginalParameterRequestName(String spagoCompatibleRequestName){
		return it.eng.spago.util.StringUtils.replace(spagoCompatibleRequestName, SPAGO_REQUEST_COMPATIBLE_DOT_SEPARATOR,DOT_SEPARATOR);
	}
}

