package it.eng.spagobi.services.scheduler;

public interface SchedulerService {

	String getJobList(String token,String user);
	
	String getJobSchedulationList(String token,String user,String jobName, String jobGroup);
	
	String deleteSchedulation(String token,String user,String triggerName, String triggerGroup);
	
	String deleteJob(String token,String user,String jobName, String jobGroupName);
	
	String defineJob(String token,String user,String xmlRequest);
	
	String getJobDefinition(String token,String user,String jobName, String jobGroup);
	
	String scheduleJob(String token,String user,String xmlRequest);
	
	String getJobSchedulationDefinition(String token,String user,String triggerName, String triggerGroup);	
	
	String existJobDefinition(String token,String user,String jobName, String jobGroup);
	
}
