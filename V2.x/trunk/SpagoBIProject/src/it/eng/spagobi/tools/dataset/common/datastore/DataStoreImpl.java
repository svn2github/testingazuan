/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.datastore;

import it.eng.spago.base.SourceBeanAttribute;
import it.eng.spagobi.tools.dataset.common.DataSetImpl;
import it.eng.spagobi.tools.dataset.common.transformer.IDataTransformer;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataStoreImpl implements IDataStore {
	
	private static transient Logger logger = Logger.getLogger(DataStoreImpl.class);
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
    	
    	String xml = "";
    	xml += "<ROWS>";
		Iterator i = records.iterator();
		while (i.hasNext()){
			xml += "<ROW ";
			IRecord r =(IRecord) i.next();				
			List fields = r.getFields();
			Iterator it = fields.iterator();
			while(it.hasNext()){					
				IField f = (IField)it.next();
				IFieldMeta fMeta = (IFieldMeta)f.getMetadata();
				String fieldName = fMeta.getName();
				SourceBeanAttribute fieldObject =(SourceBeanAttribute) f.getValue();
				String fieldValue = fieldObject.getValue().toString();
				xml += fieldName +"=\'"+fieldValue+"\' ";
			}
			xml += " />";
			
		}
		xml += "</ROWS>";

	return xml;
    }

}
