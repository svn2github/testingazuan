/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import java.util.HashMap;

import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataSet {
    /**
     * This method retrives the data
     * @throws EMFUserError 
     * @throws EMFInternalError 
     */
    void loadData(HashMap parameters) throws EMFUserError, EMFInternalError;
    
    /**
     * this method returns the data retrived with "loadData"
     * @return
     */
    IDataStore getDataStore();
    
    IDataStore fetchNext();
    
    void setFetchSize(int l);
}
