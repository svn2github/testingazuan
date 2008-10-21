/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.dataset.bo.DataSet;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.JClassDataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.datastore.IRecord;
import it.eng.spagobi.tools.dataset.common.datastore.Record;
import it.eng.spagobi.tools.dataset.common.reader.IDataReader;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetImpl implements IDataSet{
    
    /*
     * oggetto che implementa il data set
     *  Una volta in possesso della definizione del data set crea l'oggetto
     *  reader corretto a seconda del tipo di DataSet
     */
    
    IDataStore dataStore=null;
    DataSet ds=null;
    IEngUserProfile profile=null;
    private HashMap parameters=null;
    
    private IDataReader dataReader=null;
    /*
     * Il data reader rappresenta le operazioni per il recupero
     * delle informazioni, per questo sarà necessario aggiungere nella
     * configurazione del dataset il tipo di datareader da utilizzare
     */

    private DataSetImpl(){
	
    }
    public DataSetImpl(DataSet ds,IEngUserProfile profile){
	this.profile=profile;
	this.ds=ds;
	
    }
    public IDataStore fetchNext() {
    	
    	IDataStore dataStore = (IDataStore)new DataStoreImpl();

	return dataStore;
    }

    public void loadData(HashMap parameters) {
	/*
	 * 
	 * operazioni da effettuar per il caricamento dei dati
	 * -  precondizione: avere già l'oggetto DataSet valorizzato
	 * - istanziare il data reader che nel costruttore deve ricevere 
	 *   l'oggetto DataSet per avere accesso ai dati di configurazione
	 *   della query/file/ws/groovy
	 */
    if (ds != null) {
    String type= "";
	    if(ds instanceof FileDataSet)type="it.eng.spagobi.tools.dataset.bo.FileDataSet";
		else 		
			if(ds instanceof QueryDataSet)type="it.eng.spagobi.tools.dataset.bo.QueryDataSet";
			else 		
				if(ds instanceof WSDataSet)type="it.eng.spagobi.tools.dataset.bo.WSDataSet";
				else 		
					if(ds instanceof ScriptDataSet)type="it.eng.spagobi.tools.dataset.bo.ScriptDataSet";
					else 		
						if(ds instanceof JClassDataSet)type="it.eng.spagobi.tools.dataset.bo.JClassDataSet";
	    try {
			dataReader = (IDataReader) Class.forName(type).newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    dataStore = dataReader.read(parameters);
    }
	
    }

    public IDataStore getDataStore() {
    	
		return this.dataStore;
    }
    

    public void setFetchSize(int l) {


    }

}
