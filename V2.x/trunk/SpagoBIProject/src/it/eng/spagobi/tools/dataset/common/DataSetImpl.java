/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.reader.IDataReader;

import java.util.HashMap;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetImpl {
    
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

	return null;
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
	
    }

    public IDataStore getDataStore() {

	return null;
    }
    
    /**
     * Questo metodo :
     * 	1. accede al DB per recuperare la definizione del Data Set ( utilizza il WS ?? )
     *  2. 
     */
    public void loadData(IEngUserProfile profile, String dataSetLabel, HashMap parameters) {


    }

    public void setFetchSize(int l) {


    }

}
