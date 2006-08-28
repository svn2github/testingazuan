/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package it.eng.spagobi.util;

/**
 * @author Gioia
 */
public class ParameterSetter {
	public static String setParameters(String query, String pname, String pvalue) {
		String newQuery = query;
		// substitute the mondrian parameter sintax		
		int index = -1;
		int ptr = 0;
		while( (index = newQuery.indexOf("Parameter", ptr)) != -1 ) {
			ptr = newQuery.indexOf("(", index);
			String firstArg = newQuery.substring(newQuery.indexOf("(", ptr) + 1, newQuery.indexOf(",", ptr));	
			if(!firstArg.trim().equalsIgnoreCase("\""+pname+"\"")) 
				continue;
			ptr = newQuery.indexOf(",", ptr) + 1; // 2 arg
			ptr = newQuery.indexOf(",", ptr) + 1; // 3 arg
			String thirdArg = newQuery.substring(ptr, newQuery.indexOf(",", ptr));	
			newQuery = newQuery.substring(0, ptr) + pvalue + newQuery.substring(newQuery.indexOf(",", ptr+1), newQuery.length());		
		}
		// substitute the spagobi parameter sintax 
		index = -1;
		ptr = 0;
		while((index=newQuery.indexOf("${", ptr)) != -1 ) {
			int indexEnd = newQuery.indexOf("}", index);
			ptr = indexEnd;
			String namePar = newQuery.substring(index+2, indexEnd);
			if(!namePar.trim().equalsIgnoreCase(pname)) 
				continue;
			newQuery = newQuery.substring(0, index) + 
					   pvalue + 
					   newQuery.substring(indexEnd+1, newQuery.length());	
		}
		// return query
		return newQuery;
	}
}
