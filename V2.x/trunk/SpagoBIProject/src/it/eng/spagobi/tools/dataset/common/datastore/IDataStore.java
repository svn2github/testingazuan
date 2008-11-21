/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

import it.eng.spagobi.tools.dataset.common.transformer.IDataTransformer;

import java.util.Iterator;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public interface IDataStore {

    /*
     * oggetto che contiene le informazioni lette con il DataReader
     * 
     */
    IRecord getRecordByID(Object id);
    IRecord getAt(int i);
    Iterator iterator();
    
    public boolean isEmpty();
    
    public void appendRow(IRecord r);
	public void insertRow(int position,IRecord r);
    
    void applyTranformer(IDataTransformer transformer);
    IDataStoreMetaData getMetaData();
    
    String toXml();
}
