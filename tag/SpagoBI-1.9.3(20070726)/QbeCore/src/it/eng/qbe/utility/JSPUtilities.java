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

