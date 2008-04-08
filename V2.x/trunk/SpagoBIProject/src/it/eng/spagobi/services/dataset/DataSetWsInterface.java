/**
 * 
 */
package it.eng.spagobi.services.dataset;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface DataSetWsInterface {

    /**
     * 
     * @param params HashMap input parameters
     * @param operation String operation
     * @return XML data rapresentation
     */
    String readData(HashMap params, String operation);
}
