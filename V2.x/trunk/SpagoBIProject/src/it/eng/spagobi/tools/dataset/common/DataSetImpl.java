/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.JClassDataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.reader.ClassReader;
import it.eng.spagobi.tools.dataset.common.reader.FileReader;
import it.eng.spagobi.tools.dataset.common.reader.GroovyReader;
import it.eng.spagobi.tools.dataset.common.reader.IDataReader;
import it.eng.spagobi.tools.dataset.common.reader.SQLResultSetReader;
import it.eng.spagobi.tools.dataset.common.reader.WebServiceReader;

import java.util.HashMap;

import org.apache.log4j.Logger;

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
	private static transient Logger logger = Logger.getLogger(DataSetImpl.class);
    IDataStore dataStore=null;
    DataSetConfig ds=null;
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
    
    public DataSetImpl(DataSetConfig ds,IEngUserProfile profile){
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
    if(ds instanceof FileDataSet){
		type="it.eng.spagobi.tools.dataset.common.reader.FileReader";
		FileDataSet ds2 =(FileDataSet)ds;
		dataReader = (IDataReader) new FileReader(ds2);	
	}
	else 		
		if(ds instanceof QueryDataSet){
			type="it.eng.spagobi.tools.dataset.common.reader.SQLResultSetReader";
			QueryDataSet ds2 =(QueryDataSet)ds;
			dataReader = (IDataReader) new SQLResultSetReader(profile,ds2);
		}
		else 		
			if(ds instanceof WSDataSet){
				type="it.eng.spagobi.tools.dataset.common.reader.WebServiceReader";
				WSDataSet ds2 = (WSDataSet)ds;
				dataReader = (IDataReader)new WebServiceReader(ds2);
			}
			else 		
				if(ds instanceof ScriptDataSet){
					type="it.eng.spagobi.tools.dataset.common.reader.GroovyReader";
					ScriptDataSet ds2 = (ScriptDataSet)ds;
					dataReader = (IDataReader)new GroovyReader(ds2);
				}
				else 		
					if(ds instanceof JClassDataSet){
						type="it.eng.spagobi.tools.dataset.common.reader.ClassReader";
						JClassDataSet ds2 = (JClassDataSet)ds;
						dataReader = (IDataReader)new ClassReader(profile, ds2);
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
