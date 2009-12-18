package com.tensegrity.palowebviewer.modules.util.client;


/**
 * Helper class to work with arrays. There is need of it because GWT doesn't emulates all standart array functions.
 *
 */
public class Arrays {
	
	/**
	 * Returns index of the object in the array. 
	 * @param array where the object is located. Can't be null. 
	 * @param object the target objects. Can be null
	 * @return index of the object or -1 if the object isn't found in the array. 
	 */
	public static int indexOf(Object[] array, Object object){
		final int size = array.length;
		int result = -1;
        for( int i = 0 ; i < size ; i++ ) {
        	if( equals(object, array[i])){
        		result = i;
        		break;
        	}
        } 
        return result;
	}
	
	/**
	 * Checks the equality of two arrays.
	 * @param array1 first array. Can be null.
	 * @param array2 second array. Can be null.
	 * @return true, if they are equal.
	 */
    public static boolean equals(Object[] array1, Object[] array2){
        boolean result = true;
        if(array1 == null)
            result = array2 == null;
        else if(array2 == null)
            result = false;
        else {
            result = array1.length == array2.length;
            for( int i = 0 ; (i < array1.length) && result ; i++ ) { 
                Object obj1 = array1[i];
                Object obj2 = array2[i];
                result = equals(obj1, obj2);
            }
        }
        return result;
    }

    /**
     * Stringizes the array. Moslty needed for debug purposes.
     * @param array array to stringize. Can be null.
     * @return stringized representation of the array.
     */
	public static String toString(Object[] array) {
		String result = "null";
		if(array != null) {
			result = "[";
			if(array.length > 0)
				result += array[0];
			for (int i = 1; i < array.length; i++) {
				result += ", "+array[i];
			}
			result += "]";
		}
		return result;
	}
	
	/**
	 * Checks the equality of two obects, which can be null.
	 * @param o1 the first object
	 * @param o2 the second object
	 * @return true if objects are equal. False otherwise.
	 */
	public static boolean equals(Object o1, Object o2){
		boolean result = false;
		if(o1 == null)
			result = o2 == null;
		else
			result = o1.equals(o2);
		return result;
	}

}
