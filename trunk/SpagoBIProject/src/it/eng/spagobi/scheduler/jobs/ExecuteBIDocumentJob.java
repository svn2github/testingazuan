package it.eng.spagobi.scheduler.jobs;

import java.awt.image.IndexColorModel;

import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.ExecutionProxy;
import it.eng.spagobi.utilities.SpagoBITracer;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ExecuteBIDocumentJob implements Job {

	public void execute(JobExecutionContext jex) throws JobExecutionException {
		
		JobDataMap jdm = jex.getMergedJobDataMap();
	    String docIdStr = jdm.getString("documentid");
	    Integer docId = new Integer(docIdStr);
	    String storeOutput = jdm.getString("storeoutput");
	    String storeAsSnapshot =  jdm.getString("storeassnapshot");
	    String storeAsDocument =  jdm.getString("storeasdocument");
	    String storeName = jdm.getString("storename");
	    String storeDesc = jdm.getString("storedescription");
	    String storeDocumentType = jdm.getString("storedocumenttype");
	    String docParQueryString = jdm.getString("parameters");
	    
	    try{
		    // load bidocument
		    IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
			BIObject biobj = biobjdao.loadBIObjectForDetail(docId);
			// create the execution controller 
			ExecutionController execCtrl = new ExecutionController();
			execCtrl.setBiObject(biobj);
			// fill parameters 
			execCtrl.refreshParameters(biobj, docParQueryString);
			// exec the document only if all its parameter are filled
			if(execCtrl.directExecution()) {
				ExecutionProxy proxy = new ExecutionProxy();
				proxy.setBiObject(biobj);
				byte[] response = proxy.exec();
			
				// store document into
				IBIObjectCMSDAO objectCMSDAO = DAOFactory.getBIObjectCMSDAO();
				objectCMSDAO.saveSnapshot(response, biobj.getPath(), storeName, storeDesc);
			}
					
	    } catch (Exception e) {
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	    			           "execute", "Error while executiong job ", e );
	    }
	}

}
