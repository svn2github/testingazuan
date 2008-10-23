/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
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

import java.io.FileInputStream;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

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

    public void loadData(HashMap parameters) {
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
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ClassNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
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
