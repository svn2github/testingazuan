/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class WebServiceReader implements IDataReader {

	WSDataSet ds = null;

    public WebServiceReader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WebServiceReader(WSDataSet ds) {
		super();
		this.ds = ds;
	}

	public IDataStore read(HashMap parameters) {
    	
    	IDataStore ids = (IDataStore)new DataStoreImpl();

	return ids;
    }

	public WSDataSet getDs() {
		return ds;
	}

	public void setDs(WSDataSet ds) {
		this.ds = ds;
	}

}
