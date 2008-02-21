package com.tensegrity.palowebviewer.modules.util.client;


/**
 * Helper class for FireBug logging.
 *
 */
public class FireBug
{

    public static native void consoleInfo(String str)/*-{
    	if(window.console)
        	console.info(str);
    }-*/;

    public static native void consoleError(String str)/*-{
    	if(window.console)
        	console.error(str);
    }-*/;

    public static native void consoleWarn(String str)/*-{
    	if(window.console)
        	console.warn(str);
    }-*/;

}
