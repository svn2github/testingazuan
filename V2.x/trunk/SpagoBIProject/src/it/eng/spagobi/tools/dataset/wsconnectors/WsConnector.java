/**
 * 
 */
package it.eng.spagobi.tools.dataset.wsconnectors;

import it.eng.spagobi.services.proxy.DataSetWsServiceProxy;
import it.eng.spagobi.tools.dataset.bo.IDataSet;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class WsConnector implements IWsConnector {


    public String readDataSet(String adress, HashMap params, String operation) {
	DataSetWsServiceProxy proxy=new DataSetWsServiceProxy(adress);
	
	
	return proxy.readData(params, operation);
    }

}
