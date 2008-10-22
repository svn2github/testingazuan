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
    if(ds instanceof FileDataSet){
		//type="it.eng.spagobi.tools.dataset.common.reader.FileReader";   
    	//Creation of the specific DataSet
		FileDataSet ds2 =(FileDataSet)ds;
		logger.debug("FileDataSet created");
		//Instanciation of the correct DataReader
		SourceBean reader = ((SourceBean)ConfigSingleton.getInstance().getAttribute("DATASET.FILE_READER"));		
		type = (String) reader.getAttribute("class");
		dataReader = (IDataReader) new FileReader(ds2);	
		logger.debug("FileReader instanciated");
	}
	else 		
		if(ds instanceof QueryDataSet){
			//type="it.eng.spagobi.tools.dataset.common.reader.SQLResultSetReader";
			//Creation of the specific DataSet
			QueryDataSet ds2 =(QueryDataSet)ds;
			logger.debug("QueryDataSet created");
			SourceBean reader = ((SourceBean)ConfigSingleton.getInstance().getAttribute("DATASET.SQL_READER"));		
			type = (String) reader.getAttribute("class");
			//Instanciation of the correct DataReader
			dataReader = (IDataReader) new SQLResultSetReader(profile,ds2);
			logger.debug("SQLResultSetReader instanciated");
		}
		else 		
			if(ds instanceof WSDataSet){
				//type="it.eng.spagobi.tools.dataset.common.reader.WebServiceReader";
				//Creation of the specific DataSet
				WSDataSet ds2 = (WSDataSet)ds;
				logger.debug("WSDataSet created");
				SourceBean reader = ((SourceBean)ConfigSingleton.getInstance().getAttribute("DATASET.WS_READER"));		
				type = (String) reader.getAttribute("class");
				//Instanciation of the correct DataReader
				dataReader = (IDataReader)new WebServiceReader(ds2);
				logger.debug("WebServiceReader instanciated");
			}
			else 		
				if(ds instanceof ScriptDataSet){
					//type="it.eng.spagobi.tools.dataset.common.reader.GroovyReader";
					//Creation of the specific DataSet
					ScriptDataSet ds2 = (ScriptDataSet)ds;
					logger.debug("ScriptDataSet created");
					SourceBean reader = ((SourceBean)ConfigSingleton.getInstance().getAttribute("DATASET.SCRIPT_READER"));		
					type = (String) reader.getAttribute("class");
					//Instanciation of the correct DataReader
					dataReader = (IDataReader)new GroovyReader(ds2);
					logger.debug("GroovyReader instanciated");
				}
				else 		
					if(ds instanceof JClassDataSet){
						//type="it.eng.spagobi.tools.dataset.common.reader.ClassReader";
						//Creation of the specific DataSet
						JClassDataSet ds2 = (JClassDataSet)ds;
						logger.debug("JClassDataSet created");
						SourceBean reader = ((SourceBean)ConfigSingleton.getInstance().getAttribute("DATASET.JCLASS_READER"));		
						type = (String) reader.getAttribute("class");
						//Instanciation of the correct DataReader
						dataReader = (IDataReader)new ClassReader(profile, ds2);
						logger.debug("ClassReader instanciated");
					}
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
