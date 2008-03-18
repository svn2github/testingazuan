/**
 * 
 */
package it.eng.spagobi.tools.dataset.wsconnectors;

import it.eng.spagobi.tools.dataset.bo.IDataSet;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IWsConnector {

    IDataSet readDataSet(String adress,String[] params);  
    
}
