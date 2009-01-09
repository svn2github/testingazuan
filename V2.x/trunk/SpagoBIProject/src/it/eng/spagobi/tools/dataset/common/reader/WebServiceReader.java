/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;

import java.util.HashMap;

/**
 * @author Angelo Bernabei angelo.bernabei@eng.it
 */
public class WebServiceReader implements IDataReader {

    WSDataSet ds = null;
    IEngUserProfile profile = null;

    public WebServiceReader() {
	super();
	// TODO Auto-generated constructor stub
    }

    public IDataStore read(HashMap parameters) {
	
	/*
			Note:
		Con il data set recupero l'indirizzo, l'operazione
		
	 */

	IDataStore ids = (IDataStore) new DataStoreImpl();

	return ids;
    }

    public WSDataSet getDs() {
	return ds;
    }

    public void setDs(WSDataSet ds) {
	this.ds = ds;
    }

    public void setDataSetConfig(DataSetConfig ds) {
	this.ds = (WSDataSet) ds;
    }

    public void setProfile(IEngUserProfile profile) {
	this.profile = profile;

    }

    public IEngUserProfile getProfile() {
	return profile;
    }

}
