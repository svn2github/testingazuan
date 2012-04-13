/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.spagobi.meta.generator.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StringUtils
{
	
	private static Logger logger = LoggerFactory.getLogger(StringUtils.class);
		
	/**
	 * Inserts a given character at the beginning and at the end of the specified string.
	 * For example if the string is <tt>extreme</tt> and the char is <tt>'</tt> then 
	 * the returned string is <tt>'exterme'</tt>.
	 */
	public static String quote(String str, char c) {
		
		assert(str != null);
		StringBuffer buffer = new StringBuffer(str.length()+2);
		buffer.append(c);
		buffer.append(str);
		buffer.append(c);
		return buffer.toString();
	}
	public static String doubleQuote(String str) {
		return quote(str, '"');
	}
	
	/**
	 * Returns the argument string with the first char upper-case.
	 */
	public static String initUpper(String str) {
		if (str == null || str.length() == 0) {
			return str;
		}
		return Character.toUpperCase(str.charAt(0)) + str.substring(1);
	}
	
	public static String strReplaceAll(String str, String pattern, String replaceStr)
	{
		if(str == null) {
			return null;
		}
		if (replaceStr == null)
			replaceStr = "";
		if(pattern == null || pattern.equals("")) {
			return str;
		}
		int index = str.indexOf(pattern);
		while (index >= 0) {
			str = str.substring(0, index) + replaceStr + str.substring(index + pattern.length());
			index = str.indexOf(pattern, index+replaceStr.length());
		}
	
		return str;
	}
	
	/**
	 * Utility methods used to convert DB object names to  
	 * appropriate Java type and field name 
	 */
	public static String pluralise(String name) {
		if (name ==null || "".equals(name)) return "";
		String result = name;
		if (name.length() == 1) {
			result += 's';
		} else if (!seemsPluralised(name)) {
			String lower = name.toLowerCase();
			if (!lower.endsWith("data")) { //orderData --> orderDatas is dumb
				char secondLast = lower.charAt(name.length() - 2);
				if (!isVowel(secondLast) && lower.endsWith("y")) {
					// city, body etc --> cities, bodies
					result = name.substring(0, name.length() - 1) + "ies";
				} else if (lower.endsWith("ch") || lower.endsWith("s")) {
					// switch --> switches  or bus --> buses
					result = name + "es";
				} else {
					result = name + "s";
				}
			}
		}
		return result;
	}
	private static boolean seemsPluralised(String name) {
		name = name.toLowerCase();
		boolean pluralised = false;
		pluralised |= name.endsWith("es");
		pluralised |= name.endsWith("s");
		pluralised &= !(name.endsWith("ss") || name.endsWith("us"));
		return pluralised;
	}
	private final static boolean isVowel(char c) {
		boolean vowel = false;
		vowel |= c == 'a';
		vowel |= c == 'e';
		vowel |= c == 'i';
		vowel |= c == 'o';
		vowel |= c == 'u';
		vowel |= c == 'y';
		return vowel;
	}	
	
	public static String getStringFromFile(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line = null;
		StringBuffer buffer = new StringBuffer();
		while((line = reader.readLine()) != null) {
			buffer.append(line + "\n");
		}
		
		return buffer.toString();
	}
	
	public static String getStringFromStream(InputStream in) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		StringBuffer buffer = new StringBuffer();
		while((line = reader.readLine()) != null) {
			buffer.append(line + "\n");
		}
		
		return buffer.toString();
	}
}

