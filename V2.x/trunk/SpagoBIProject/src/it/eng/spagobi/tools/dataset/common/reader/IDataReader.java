/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import java.util.HashMap;

import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataReader {
    
    /*
     * Il data reader è l'oggetto in grado di eseguire l'operazione
     * di accesso ai dati a seconda dal tipo di DataSource
     * 
     * L'operazione può essere eseguita diverse volte con parametri diversi
     * 
     */

    IDataStore read(HashMap parameters)throws EMFUserError, EMFInternalError;
    
    void setDataSetConfig(DataSetConfig ds);
    
    void setProfile(IEngUserProfile profile) ;

}
