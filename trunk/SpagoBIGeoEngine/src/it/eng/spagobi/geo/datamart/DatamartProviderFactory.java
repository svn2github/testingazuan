/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.geo.datamart;

/**
 * Builds and returns class instances of the  DatamartProviderIFace interface 
 */
public class DatamartProviderFactory {

	/**
	 * Builds and returns class instances of the  DatamartProviderIFace interface 
	 * @param className The class name 
	 * @return an instance of the class which must implement the DatamartProviderIFace interface
	 * @throws Exception if the class doesn't exist or it doens't implement the interface
	 */
    public static DatamartProviderIFace getDatamartProvider(String className) throws Exception {
        return (DatamartProviderIFace) Class.forName(className).newInstance();
    }
    
}