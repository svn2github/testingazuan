package it.eng.spagobi.scheduler.utils;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.bo.BIObject;
import it.eng.spagobi.bo.BIObjectParameter;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectDAO;
import it.eng.spagobi.bo.dao.IBIObjectParameterDAO;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.scheduler.to.JobInfo;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SchedulerUtilities {

	public static BIObject.BIObjectSnapshot getNamedHistorySnapshot(List allsnapshots, String namesnap, int hist) 
	       throws Exception {
		Map snapshots = new HashMap();
		List snapDates = new ArrayList();
		Iterator iterAllSnap = allsnapshots.iterator();
		while(iterAllSnap.hasNext()) {
			BIObject.BIObjectSnapshot snap =  (BIObject.BIObjectSnapshot)iterAllSnap.next();
			if(snap.getName().equals(namesnap)){
				Date creationDate = snap.getDateCreation();
				Long creationLong = new Long(creationDate.getTime());
				snapDates.add(creationLong);
				snapshots.put(creationLong, snap);
			}
		}
		// check if history is out of range
		if( (hist<0) || (snapDates.size()-1 > hist) ) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, SchedulerUtilities.class.getName(), 
					            "getNamedHistorySnapshot", "History step out of range");
			throw new Exception("History step out of range");
		}
		// get the right snapshot
		Collections.sort(snapDates);
		Collections.reverse(snapDates);
		Object key = snapDates.get(hist);
		BIObject.BIObjectSnapshot snap = (BIObject.BIObjectSnapshot)snapshots.get(key);
		return snap;
	}
	

	
	public static List getSnapshotsByName(List allsnapshots, String namesnap) throws Exception {
		List snaps = new ArrayList();
		Iterator iterAllSnap = allsnapshots.iterator();
		while(iterAllSnap.hasNext()) {
			BIObject.BIObjectSnapshot snap =  (BIObject.BIObjectSnapshot)iterAllSnap.next();
			if(snap.getName().equals(namesnap)){
				snaps.add(snap);
			}
		}
		return snaps;
	}	

	
	public static SourceBean getSBFromWebServiceResponse(String response) throws Exception {
		SourceBean respSB = SourceBean.fromXMLString(response);
		SourceBean servRespSB = (SourceBean)respSB.getAttribute("SERVICE_RESPONSE");
		SourceBean schedModRespSB = (SourceBean)servRespSB.getAttribute("SCHEDULERMODULE");
		return schedModRespSB;
	}
	
	
	
	public static JobInfo getJobInfoFromJobSourceBean(SourceBean jobDetSB) {
		JobInfo jobInfo = new JobInfo();
		try{
			List biobjects = new ArrayList();
			String jobNameRecovered = (String)jobDetSB.getAttribute("jobName");
			String jobDescriptionRecovered = (String)jobDetSB.getAttribute("jobDescription");
			String jobGroupNameRecovered = (String)jobDetSB.getAttribute("jobGroupName");
			jobInfo.setJobName(jobNameRecovered);
			jobInfo.setJobDescription(jobDescriptionRecovered);
			jobInfo.setJobGroupName(jobGroupNameRecovered);
			// set all documents and parameters
			SourceBean jobParSB = (SourceBean)jobDetSB.getAttribute("JOB_PARAMETERS");
			if(jobParSB!=null) {
				IBIObjectDAO biobjdao = DAOFactory.getBIObjectDAO();
				IBIObjectParameterDAO biobjpardao = DAOFactory.getBIObjectParameterDAO();
				SourceBean docLblSB = (SourceBean)jobParSB.getFilteredSourceBeanAttribute("JOB_PARAMETER", "name", "documentLabels");
				String docLblStr = (String)docLblSB.getAttribute("value");
				String[] docLbls = docLblStr.split(",");
				for(int i=0; i<docLbls.length; i++) {
					BIObject biobj = biobjdao.loadBIObjectByLabel(docLbls[i]);
					List biobjpars = biobjpardao.loadBIObjectParametersById(biobj.getId());
					biobj.setBiObjectParameters(biobjpars);
					String biobjlbl = biobj.getLabel();
					SourceBean queryStringSB = (SourceBean)jobParSB.getFilteredSourceBeanAttribute("JOB_PARAMETER", "name", biobjlbl);
					String queryString = (String)queryStringSB.getAttribute("value");
					String[] parCouples = queryString.split("%26");
					for(int j=0; j<parCouples.length; j++) {
						String parCouple = parCouples[j];
						String[] parDef = parCouple.split("=");
						Iterator iterbiobjpar = biobjpars.iterator();
						while(iterbiobjpar.hasNext()) {
							BIObjectParameter biobjpar = (BIObjectParameter)iterbiobjpar.next();
							if(biobjpar.getParameterUrlName().equals(parDef[0])){
								String[] valuesArr = parDef[1].split(";");
								List values = Arrays.asList(valuesArr);
								biobjpar.setParameterValues(values);
							}
						}
					}
					// calculate parameter
					biobjects.add(biobj);
				}
				jobInfo.setBiobjects(biobjects);
			}
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, SchedulerUtilities.class.getName(), 
					            "getJobInfoFromJobSourceBean", "Error while extracting job info from xml", e);
		}
		return jobInfo;
	}
	
}
