/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.reader.IDataReader;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetProxyImpl implements IDataSetProxy {

    
    IEngUserProfile profile=null;

    
    private DataSetProxyImpl(){
	
    }
    public DataSetProxyImpl(IEngUserProfile profile){
	this.profile=profile;
    }   
    
    /**
     * questo metodo a seconda se viene eseguito nei motori o nei core si comporta diversamente
     * lato motore:
     * 	- esegue il WS di recuper configurazione DS
     *  - costruisce l'oggetto IDataSet e lo ritorna
     *  
     */
    public IDataSet getDataSet(String dataSetLabel){
	return null;
    }
    
 
}
