/**
 * SchedulerServiceSoapBindingImpl.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package it.eng.spagobi.services.scheduler.stub;

import it.eng.spagobi.services.scheduler.service.SchedulerServiceImpl;

import java.rmi.RemoteException;

public class SchedulerServiceSoapBindingImpl implements it.eng.spagobi.services.scheduler.stub.SchedulerService{
    
	public String getJobList() throws RemoteException {
		SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String jobListXml = service.getJobList();
	    return jobListXml;
    }

    public String getJobSchedulationList(String jobName, String jobGroup) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String schedListXml = service.getJobSchedulationList(jobName, jobGroup);
	    return schedListXml;
    }

    public String deleteSchedulation(String triggerName, String triggerGroup) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.deleteSchedulation(triggerName, triggerGroup);
	    return res;
    }

    public String deleteJob(String jobName, String jobGroupName) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.deleteJob(jobName, jobGroupName);
	    return res;
    }

    public String defineJob(String xmlRequest) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.defineJob(xmlRequest);
	    return res;
    }

    public String getJobDefinition(String jobName, String jobGroup) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.getJobDefinition(jobName, jobGroup);
	    return res;
    }

    public String scheduleJob(String xmlRequest) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.scheduleJob(xmlRequest);
	    return res;
    }

    public String getJobSchedulationDefinition(String triggerName, String triggerGroup) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.getJobSchedulationDefinition(triggerName, triggerGroup);
	    return res;
    }

    public String existJobDefinition(String jobName, String jobGroup) throws RemoteException {
    	SchedulerServiceImpl service = new SchedulerServiceImpl();
	    String res = service.existJobDefinition(jobName, jobGroup);
	    return res;
    }

}
