/**
 * 
 */
package it.eng.spagobi.tools.dataset.wsconnectors;

import java.util.HashMap;

import it.eng.spagobi.tools.dataset.bo.IDataSet;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IWsConnector {

    /**
     * 
     * @param adress String WS Address
     * @param params HasMap input parameters
     * @param operation String operation
     * @return IDataSet result
     */
    IDataSet readDataSet(String adress,HashMap params, String operation);  
    
}
