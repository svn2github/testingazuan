/**
 * 
 */
package it.eng.spagobi.tools.dataset.common;

import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.behaviouralmodel.lov.bo.ModalitiesValue;
import it.eng.spagobi.behaviouralmodel.lov.dao.LovDAOHibImpl;
import it.eng.spagobi.behaviouralmodel.lov.metadata.SbiLov;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.tools.dataset.bo.DataSetConfig;
import it.eng.spagobi.tools.dataset.bo.FileDataSet;
import it.eng.spagobi.tools.dataset.bo.IJavaClassDataSet;
import it.eng.spagobi.tools.dataset.bo.JClassDataSet;
import it.eng.spagobi.tools.dataset.bo.QueryDataSet;
import it.eng.spagobi.tools.dataset.bo.ScriptDataSet;
import it.eng.spagobi.tools.dataset.bo.WSDataSet;
import it.eng.spagobi.tools.dataset.common.datastore.IDataStore;
import it.eng.spagobi.tools.dataset.common.reader.IDataReader;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * @author Angelo Bernabei
 *         angelo.bernabei@eng.it
 */
public class DataSetProxyImpl implements IDataSetProxy {

	private static transient Logger logger = Logger.getLogger(DataSetProxyImpl.class);
    IEngUserProfile profile=null;

    
    private DataSetProxyImpl(){
	
    }
    
    public DataSetProxyImpl(IEngUserProfile profile){
	this.profile=profile;
    }   
    
    /**
     * questo metodo a seconda se viene eseguito nei motori o nei core si comporta diversamente
     * lato motore:
     * 	- esegue il WS di recuper configurazione DS
     *  - costruisce l'oggetto IDataSet e lo ritorna
     *  
     */ 
    public IDataSet getDataSet(String dataSetLabel){
    	  	
    	
    	IDataSet ids= null;
    	try {
    		DataSetConfig ds= DAOFactory.getDataSetDAO().loadDataSetByLabel(dataSetLabel);
			ids = (IDataSet)new DataSetImpl(ds,profile);
		} catch (EMFUserError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ids;
    			
    }
    
 
}
