/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.map;


/**
 * <p>
 * </p>
 * 
 * @stereotype factory
 */
public class MapProviderFactory {

    /**
     * @param 
     */
    public static MapProviderIFace getMapProvider(String className) throws Exception {

        return (MapProviderIFace) Class.forName(className).newInstance();
    }
}