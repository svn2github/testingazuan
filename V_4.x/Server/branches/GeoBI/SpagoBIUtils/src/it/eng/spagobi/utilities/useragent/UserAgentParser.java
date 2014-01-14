/*
 * SpagoBI, the Open Source Business Intelligence suite
 * © 2005-2015 Engineering Group
 *
 * This file is part of SpagoBI. SpagoBI is free software: you can redistribute it and/or modify it under the terms of the GNU
 * Lesser General Public License as published by the Free Software Foundation, either version 2.1 of the License, or any later version. 
 * SpagoBI is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have received
 * a copy of the GNU Lesser General Public License along with SpagoBI. If not, see: http://www.gnu.org/licenses/.
 * The complete text of SpagoBI license is included in the COPYING.LESSER file. 
 */
package it.eng.spagobi.utilities.useragent;

/**
 * TODO use this http://uadetector.sourceforge.net/
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class UserAgentParser {
	public static String[] parse(String userAgent) {
		String info[] = null;
		String subsString = "";
		
		if (userAgent.contains("MSIE")) {
			subsString = userAgent.substring(userAgent.indexOf("MSIE"));
			info = (subsString.split(";")[0]).split(" ");
		} else if (userAgent.contains("like Gecko")) { // it's ie11 or greater
			info = new String[] { "MSIE", "11.0" };
		} else if (userAgent.contains("Firefox")) {
			subsString = userAgent.substring(userAgent.indexOf("Firefox"));
			info = (subsString.split(" ")[0]).split("/");
		} else if (userAgent.contains("Chrome")) {
			subsString = userAgent.substring(userAgent.indexOf("Chrome"));
			info = (subsString.split(" ")[0]).split("/");
		} else {
			info = new String[] { "unknown", "unknown" };
		}
		
		return info;
	}
}
