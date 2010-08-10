/*
 * Created on 4-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.utility;

import it.eng.spago.tracing.TracerSingleton;

/**
 * @author Andrea Zoppello
 *
 * A little Logging utility to keep separate from underlyng
 * Logging library
 */
public class Logger {
	
	/**
	 * @param clazz
	 * @param o
	 */
	public static void debug(Class clazz, Object o){
		TracerSingleton.log("SPAGO", TracerSingleton.DEBUG, o.toString());
	}
	
	/**
	 * @param clazz
	 * @param o
	 */
	public static void info(Class clazz, Object o){
		TracerSingleton.log("SPAGO", TracerSingleton.INFORMATION, o.toString());
	}
	/**
	 * @param clazz
	 * @param o
	 */
	public static void error(Class clazz, Object o){
		TracerSingleton.log("SPAGO", TracerSingleton.CRITICAL, o.toString());
	}
	/**
	 * @param clazz
	 * @param o
	 */
	public static void warn(Class clazz, Object o){
		TracerSingleton.log("SPAGO", TracerSingleton.WARNING, o.toString());
	}
	/**
	 * @param clazz
	 * @param o
	 */
	public static void critical(Class clazz, Object o){
		TracerSingleton.log("SPAGO", TracerSingleton.CRITICAL, o.toString());
	}
	
}
