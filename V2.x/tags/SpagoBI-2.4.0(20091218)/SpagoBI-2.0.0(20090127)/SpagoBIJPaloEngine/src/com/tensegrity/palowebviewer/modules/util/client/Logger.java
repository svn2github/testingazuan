package com.tensegrity.palowebviewer.modules.util.client;

import com.google.gwt.core.client.GWT;

/**
 * Logger interface for client part of an applications.
 *
 */
public class Logger {
	
	private static boolean usefirebug = false;
	private static boolean on = false;
	
	
	public static void setOn(boolean value) {
		on = value;
	}
	
	public static boolean isOn() {
		return on;
	}

    public static boolean isUseFirebug() {
        return usefirebug;
    }

    public static void setUseFirebug(boolean value) {
        usefirebug = value && GWT.isScript();
    }
	
    public static void info(String message) {
    	if(!isOn())
    		return;    	
        if(isUseFirebug())
            FireBug.consoleInfo(message);
        else
            java.lang.System.out.println(message);
    }

    public static void warn(String message) {
    	if(!isOn())
    		return;
        if(isUseFirebug())
            FireBug.consoleWarn(message);
        else
            java.lang.System.err.println(message);
    }
    
    public static void error(String message) {
    	if(!isOn())
    		return;
        if(isUseFirebug())
            FireBug.consoleError(message);
        else
            java.lang.System.err.println("[Error]"+message);
    }
    
    public static void debug(String message) {
    	if(!isOn())
    		return;
        if(isUseFirebug())
            FireBug.consoleInfo(message);
        else
            java.lang.System.out.println("[Debug]"+message);
    }



}
