package com.tensegrity.palowebviewer.modules.util.client;

/**
 * Helper class for assertions.
 *
 */
public class Assertions {
	
	public static void assertNotNull(Object arg, String argName) {
		if(arg == null){
			String message = argName + " can not be null";
			throwIllegalArgument(message);
		}
	}

	private static void throwIllegalArgument(String message) {
		throw new IllegalArgumentException(message);
	}
	
	public static void assertMin(int x, int min, String name) {
        if(x < min)
            throwIllegalArgument(name+" can not be less then " + min + ".");
	}
	
}
