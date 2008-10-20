/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.security.IEngUserProfile;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataSetProxyFactory {

    /**
     * Restituisce un oggetto IDataSet con le informazioni necessarie per 
     * il recupero delle informazioni
     * Sarà questa classe che accede alle informazioni di configurazione
     * utilizzando un accesso diretto al DB oppure invocando un WS
     * @param profile
     * @param dataSetLabel
     * @param parameters
     * @return
     */
    IDataSetProxy crateDataSetProxy(IEngUserProfile profile,String dataSetLabel,HashMap parameters);
}
