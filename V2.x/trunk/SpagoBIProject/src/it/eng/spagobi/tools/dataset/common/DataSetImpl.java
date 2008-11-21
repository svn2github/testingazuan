/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFInternalError;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.common.datastore.DataStoreImpl;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.reader.IDataReader;

import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetImpl implements IDataSet{
    

	private static transient Logger logger = Logger.getLogger(DataSetImpl.class);
    IDataStore dataStore=null;
    DataSetConfig ds=null;
    IEngUserProfile profile=null;
    private HashMap parameters=null;
    
    private IDataReader dataReader=null;


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

    public void loadData(HashMap parameters) throws EMFUserError, EMFInternalError{
	/*
	 * 
	 * operazioni da effettuar per il caricamento dei dati
	 * -  precondizione: avere già l'oggetto DataSet valorizzato
	 * - istanziare il data reader che nel costruttore deve ricevere 
	 *   l'oggetto DataSet per avere accesso ai dati di configurazione
	 *   della query/file/ws/groovy
	 */
  
    logger.debug("IN");
    if (ds != null) {
    String type= "";
    
    String dsType=ds.getClass().getName();
    SourceBean reader = ((SourceBean)ConfigSingleton.getInstance().getFilteredSourceBeanAttribute("DATASET.READER", "type", dsType));
    try {
		dataReader= (IDataReader)Class.forName((String) reader.getAttribute("class")).newInstance();
	} catch (InstantiationException e) {
		e.printStackTrace();
		logger.error("Instantiation Exception");
	} catch (IllegalAccessException e) {
		e.printStackTrace();
		logger.error("IllegalAccessException Exception");
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
		logger.error("ClassNotFoundException Exception");
	}
    dataReader.setDataSetConfig(ds);
    dataReader.setProfile(profile);
	dataStore = dataReader.read(parameters);
    }
    logger.debug("OUT");
    }

    public IDataStore getDataStore() {
    	
		return this.dataStore;
    }
    

    public void setFetchSize(int l) {


    }

}
