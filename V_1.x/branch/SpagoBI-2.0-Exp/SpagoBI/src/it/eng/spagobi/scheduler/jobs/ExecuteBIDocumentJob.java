package it.eng.spagobi.scheduler.jobs;

import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.Domain;
import it.eng.spagobi.bo.Engine;
import it.eng.spagobi.bo.ExecutionController;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IDomainDAO;
import it.eng.spagobi.bo.dao.IEngineDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.scheduler.SchedulerUtilities;
import it.eng.spagobi.scheduler.profile.AnonymousSchedulerProfile;
import it.eng.spagobi.utilities.ExecutionProxy;
import it.eng.spagobi.utilities.SpagoBITracer;
import it.eng.spagobi.utilities.UploadedFile;

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
				IEngUserProfile profile = new AnonymousSchedulerProfile();
				byte[] response = proxy.exec(profile);
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
					} //if(storeAsSnapshot!=null)
					
					
					// IF THE STORE DOCUMENT OPTION IS SELECTED THEN STORE ANEW OFFICE DOCUMENT
					if(storeAsDocument!=null) {
						// recover office document sbidomains
						IDomainDAO domainDAO = DAOFactory.getDomainDAO();
						Domain officeDocDom = domainDAO.loadDomainByCodeAndValue("BIOBJ_TYPE", "OFFICE_DOC");
						// recover development sbidomains
						Domain devDom = domainDAO.loadDomainByCodeAndValue("STATE", "DEV");
						// recover engine
						IEngineDAO engineDAO = DAOFactory.getEngineDAO();
						List engines = engineDAO.loadAllEnginesForBIObjectType(officeDocDom.getValueCd());
						Engine engine = (Engine)engines.get(0);
						// load the template
						UploadedFile uploadedFile = new UploadedFile();
						uploadedFile.setFieldNameInForm("template");
						uploadedFile.setFileName(storeName);
						uploadedFile.setSizeInBytes(response.length);
						uploadedFile.setFileContent(response);
						// load all functionality
						//List storeInFunctionalities = new ArrayList();
						//List functIds = request.getAttributeAsList("FUNCT_ID");
						//Iterator iterFunctIds = functIds.iterator();
						//while(iterFunctIds.hasNext()) {
						//	String functIdStr = (String)iterFunctIds.next();
						//	Integer functId = new Integer(functIdStr);
						//	storeInFunctionalities.add(functId);
						//}
						// create biobject
						BIObject newbiobj = new BIObject();
						newbiobj.setDescription(storeDesc);
						newbiobj.setLabel(storeName);
						newbiobj.setName(storeName);
						newbiobj.setEncrypt(new Integer(0));
						newbiobj.setEngine(engine);
						newbiobj.setRelName("");
						newbiobj.setBiObjectTypeCode(officeDocDom.getValueCd());
						newbiobj.setBiObjectTypeID(officeDocDom.getValueId());
						newbiobj.setStateCode(devDom.getValueCd());
						newbiobj.setStateID(devDom.getValueId());
						newbiobj.setVisible(new Integer(0));
						newbiobj.setTemplate(uploadedFile);
						//newbiobj.setFunctionalities(storeInFunctionalities);
						IBIObjectDAO objectDAO = DAOFactory.getBIObjectDAO();
						objectDAO.insertBIObject(biobj);
					} // if(storeAsDocument!=null)
					
					
					
				}
			}				
	    } catch (Exception e) {
	    	SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
	    			           "execute", "Error while executiong job ", e );
	    }
	}

}
