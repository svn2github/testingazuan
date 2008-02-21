package com.tensegrity.palowebviewer.modules.util.client;


/**
 * Helper calss to escape/unescape strings.
 *
 */
public class Escaper {
	//either number of previous backslashes is even or no odd 
	private static final String SPLIT_BASE = "(?<=(?<!\\\\)(\\\\{2}){0,2000})"; //"(?<!\\\\)";

	private static String[] getEscapePair(String what){
		String rWhat, rTo;
		if(what.equals("/")){
			rWhat = "\\" + what;
			rTo = "\\\\" + what; 
		}else{
			rWhat = what;
			rTo = "\\\\" + what;
		}
		return new String[]{rWhat, rTo};
	}
	
	private static String getUnecape(String what){
		return "\\\\" + what;
	}
	
	/**
	 * \ -> \\
	 * / -> \[what]
	 */
	public static String escape(String text, String what){
		text = text.replaceAll("\\\\", "\\\\\\\\");
		String[] escapes = getEscapePair(what);
		String myWhat = escapes[0];
		String myTo = escapes[1];
		String r = text.replaceAll(myWhat, myTo);
		return r;
	}
	
	/**
	 * \[what] -> [what]
	 * \\ -> \
	 */
	public static String unescape(String text, String what){
		String myWhat = getUnecape(what);
		text = text.replaceAll(myWhat, what);
		text = text.replaceAll("\\\\\\\\", "\\\\");
		return text;
	}
	
	/**
	 * 
	 * splits text using unescaped [what] for count-parts
	 * 
	 */
	public static String[] spiltUnescaped(String text, String what, int count){
		String myRegexp = SPLIT_BASE + what;
		String[] r = text.split(myRegexp, count);
		return r;
	}

	/**
	 * 
	 * splits text using unescaped [what]
	 * (?<=(?<!\\\\)(\\\\{2}){0,2000})/
	 */
	public static String[] spiltUnescaped(String text, String what){
		String myRegexp = SPLIT_BASE + what;
		String[] r = text.split(myRegexp, Integer.MAX_VALUE);
		return r;
	}
	
	public static String[] splitAndUnescape(String text, String separator) {
		String[] result = spiltUnescaped(text, separator);
		for (int i = 0; i < result.length; i++) {
			result[i] = unescape(result[i], separator);
		}
		return result;
	}
	
	public static String escapeAndConcat(String[] array, String separator) {
		String result = null;
		result = array.length >0 ? escape(array[0], separator) : "";
		for (int i = 1; i < array.length; i++) {
			result += separator + escape(array[i], separator);
		} 
		return result;
	}
	
}
