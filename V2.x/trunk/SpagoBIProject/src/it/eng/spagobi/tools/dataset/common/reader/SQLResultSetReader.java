/**
 * 
 */
package it.eng.spagobi.tools.dataset.common.reader;

import java.util.HashMap;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.datasource.bo.DataSource;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class SQLResultSetReader implements IDataReader {

    IEngUserProfile profile=null;
    QueryDataSet ds=null;

    
    public SQLResultSetReader(QueryDataSet ds,IEngUserProfile profile){
	/*
	 * 
	 *  
	 */
    }

    public IDataStore read(HashMap parameters) {
	// TODO Auto-generated method stub
	return null;
    }





}
