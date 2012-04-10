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

import java.beans.Introspector;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
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
	 * Converts a name to a Java variable name (<em>first letter
	 * not capitalized</em>)
	 * 
	 * @param name the name to convert
	 * @return the generated java variable name
	 * 
	 * TODO 1. handle reserved world; 
	 * TODO 2. handle offending chars.
	 */
	public static String nameToJavaClassName(String name) {
		String className;
		className = nameToJavaVariableName(name);
		if(className.startsWith("_")) className = "class" + className;
		name = StringUtils.initUpper(className);
		return className;
	}
	
	/**
	 * Converts a name to a Java variable name (<em>first letter
	 * not capitalized</em>)
	 * 
	 * @param name the name to convert
	 * @return the generated java variable name
	 * 
	 * TODO 1. handle reserved world; 
	 * TODO 2. handle offending chars.
	 */
	public static String nameToJavaVariableName(String name) {
		String varName;
		varName = name.replaceAll(" ", "_");
		return varName;
	}
	
	/**
	 * Converts a database table name to a Java variable name (<em>first letter
	 * not capitalized</em>).
	 * 
	 * @deprecated use nameToJavaClassName instead
	 * @see SPAGOBI-831
	 */
	public static String tableNameToVarName(String tableName) {
		return dbNameToVarName(tableName);
	}
	
	/**
	 * Converts a database column name to a Java variable name (<em>first letter
	 * not capitalized</em>).
	 * 
	 * @deprecated use nameToJavaVariableName instead
	 * @see SPAGOBI-831
	 */
	public static String columnNameToVarName(String columnName) {
		return dbNameToVarName(columnName);
	}
	
	/**
	 * Converts a database name (table or column) to a java name (<em>first letter
	 * not capitalized</em>). employee_name or employee-name -> employeeName
	 */
	private static String dbNameToVarName(String s) {
		if (s==null)return "";
	
		if ("".equals(s)) {
			return s;
		}
		StringBuffer result = new StringBuffer();

		boolean capitalize = true;
		boolean lastCapital = false;
		boolean lastDecapitalized = false;
		String p = null;
		for (int i = 0; i < s.length(); i++) {
			String c = s.substring(i, i + 1);
			if ("_".equals(c) || " ".equals(c)) {
				capitalize = true;
				continue;
			}

			if (c.toUpperCase().equals(c)) {
				if (lastDecapitalized && !lastCapital) {
					capitalize = true;
				}
				lastCapital = true;
			} else {
				lastCapital = false;
			}

			if (capitalize) {
				if (p == null || !p.equals("_")) {
					result.append(c.toUpperCase());
					capitalize = false;
					p = c;
				} else {
					result.append(c.toLowerCase());
					capitalize = false;
					p = c;
				}
			} else {
				result.append(c.toLowerCase());
				lastDecapitalized = true;
				p = c;
			}

		}
		/*this was using StringUtil.initLower. Changed to Introspector.decapitalize so that 
		 * it returns the correct bean property name when called from columnNameToVarName.
		 * This is necessary because otherwise URL would be uRL which would cause 
		 * an "The property uRL is undefined for the type xx" error because 
		 * Introspector.getBeanInfo (used by JavaTypeIntrospector) returns 
		 * the property name as URL.*/
		String resultStr = Introspector.decapitalize(result.toString());
		if (resultStr.equals("class")) {
			// "class" is illegal becauseOf Object.getClass() clash
			resultStr = "clazz";
		}
	
		return resultStr;
	}
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
	
	public final static void main(String args[]) {
		System.out.println( dbNameToVarName("C_CAUSALE") );
	}
}

