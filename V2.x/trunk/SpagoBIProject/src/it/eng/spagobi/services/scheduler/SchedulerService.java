package it.eng.spagobi.services.scheduler;

public interface SchedulerService {

	String getJobList(String token);
	
	String getJobSchedulationList(String token,String jobName, String jobGroup);
	
	String deleteSchedulation(String token,String triggerName, String triggerGroup);
	
	String deleteJob(String token,String jobName, String jobGroupName);
	
	String defineJob(String token,String xmlRequest);
	
	String getJobDefinition(String token,String jobName, String jobGroup);
	
	String scheduleJob(String token,String xmlRequest);
	
	String getJobSchedulationDefinition(String token,String triggerName, String triggerGroup);	
	
	String existJobDefinition(String token,String jobName, String jobGroup);
	
}
