/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.map;

/**
 * Factory for the creation of instances of the classes  which implements the MapProviderIFace
 */
public class MapProviderFactory {

	/**
	 * Creates a new instance of the class identified by the name passed as input. 
	 * The class must implement the MapProviderIFace
	 * @param className The class to create
	 * @return The instace of the class 
	 * @throws Exception raised if the class don't exist or it isn't an implementation of the MapProviderIFace
	 */ 
    public static MapProviderIFace getMapProvider(String className) throws Exception {
        return (MapProviderIFace) Class.forName(className).newInstance();
    }
}