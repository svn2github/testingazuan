package it.eng.spagobi.scheduler.jobs;

import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.scheduler.SchedulerUtilities;
import it.eng.spagobi.utilities.ExecutionProxy;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.List;

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
	    String historylengthStr = jdm.getString("lengthhistory");
	    
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
				// if the user request the store
				if(storeOutput!=null) {
					if(storeAsSnapshot!=null) {
						// store document as snapshot
						IBIObjectCMSDAO objectCMSDAO = DAOFactory.getBIObjectCMSDAO();
						// get the list of snapshots
						List allsnapshots = objectCMSDAO.getSnapshots(biobj.getPath());
						// get the list of the snapshot with the store name
						List snapshots = SchedulerUtilities.getSnapshotsByName(allsnapshots, storeName);
						// get the number of previous snapshot saved
						int numSnap = snapshots.size();
						// if the number of snapshot is greater or equal to the history length then
						// delete the unecessary snapshots
						if((historylengthStr!=null) && !historylengthStr.trim().equals("")){
							try{
								Integer histLenInt = new Integer(historylengthStr);
								int histLen = histLenInt.intValue();
								if(numSnap>=histLen){
									int delta = numSnap - histLen;
									for(int i=0; i<=delta; i++) {
										BIObject.BIObjectSnapshot snap = SchedulerUtilities.getNamedHistorySnapshot(snapshots, storeName, histLen-1);
										String pathSnap = snap.getPath();
										objectCMSDAO.deleteSnapshot(pathSnap);
									}
								}
							} catch(Exception e) {
								SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
				    			           			"execute", "Error while deleting object snapshots", e );
							}
						}
						objectCMSDAO.saveSnapshot(response, biobj.getPath(), storeName, storeDesc);
					}
				}
			}				
	    } catch (Exception e) {
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	    			           "execute", "Error while executiong job ", e );
	    }
	}

}
