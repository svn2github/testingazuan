/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

import it.eng.spagobi.tools.dataset.common.transformer.IDataTransformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataStoreImpl implements IDataStore {
	
	List records = null;


    public DataStoreImpl() {
		super();
		this.records = new ArrayList();
	}


	public void applyTranformer(IDataTransformer transformer) {


    }

	public void appendRow(IRecord r){
		
		records.add(r);		
	}
	
	public void insertRow(int position,IRecord r){
		
		records.add(position, r);
		
	}

	
    public IRecord getAt(int i) {
    	
    	IRecord record =(IRecord)records.get(i);

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
    	
    	Iterator it = records.iterator();

	return it;
    }


    public String toXml() {

	return null;
    }

}
