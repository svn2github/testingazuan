/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.datamart;


/**
 * <p>
 * </p>
 * 
 * @stereotype factory
 */
public class DatamartProviderFactory {

    /**
     * <p>
     * Does ...
     * </p>
     * 
     * 
     * 
     * @param serviceId
     */
    public static DatamartProviderIFace getDatamartProvider(String className) throws Exception {

        return (DatamartProviderIFace) Class.forName(className).newInstance();
    }
}