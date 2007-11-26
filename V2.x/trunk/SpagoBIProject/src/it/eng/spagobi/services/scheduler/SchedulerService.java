package it.eng.spagobi.services.scheduler;

public interface SchedulerService {

	String getJobList();
	
	String getJobSchedulationList(String jobName, String jobGroup);
	
	String deleteSchedulation(String triggerName, String triggerGroup);
	
	String deleteJob(String jobName, String jobGroupName);
	
	String defineJob(String xmlRequest);
	
	String getJobDefinition(String jobName, String jobGroup);
	
	String scheduleJob(String xmlRequest);
	
	String getJobSchedulationDefinition(String triggerName, String triggerGroup);	
	
	String existJobDefinition(String jobName, String jobGroup);
	
}
