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
public class DataStoreImpl implements IDataStore {


    public void applyTranformer(IDataTransformer transformer) {


    }


    public IRecord getAt(int i) {
    	
    	IRecord record = (IRecord)new Record();

	return record;
    }


    public IDataStoreMetaData getMetaData() {

	return null;
    }


    public IRecord getRecordByID(Object id) {
    	
    	IRecord record = (IRecord)new Record();

	return record;
    }


    public Iterator iterator() {

	return null;
    }


    public String toXml() {

	return null;
    }

}
