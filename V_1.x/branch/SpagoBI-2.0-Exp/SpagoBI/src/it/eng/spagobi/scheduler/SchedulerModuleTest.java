package it.eng.spagobi.scheduler;

import it.eng.spago.soap.axis.client.AdapterAxisProxy;



public class SchedulerModuleTest {

	public static void main(String[] args) {
		/*
		StringBuffer message = new StringBuffer();
		message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"defineJob\" ");
		message.append(" jobName=\"myjob\" ");
		message.append(" jobDescription=\"my job desc\" ");
		message.append(" jobClass=\"it.eng.spagobi.scheduler.jobs.ExecuteBIDocumentJob\" ");
		message.append(">");
		message.append("   <PARAMETERS>");
		message.append("   	   <PARAMETER name=\"documentid\" value=\"191\" />");
		message.append("   	   <PARAMETER name=\"storeoutput\" value=\"true\" />");
		message.append("   	   <PARAMETER name=\"storeassnapshot\" value=\"true\" />");
		message.append("   	   <PARAMETER name=\"storename\" value=\"nome\" />");
		message.append("   	   <PARAMETER name=\"storedescription\" value=\"descrizione\" />");
		message.append("   	   <PARAMETER name=\"storedocumenttype\" value=\"HTML\" />");
		message.append("   	   <PARAMETER name=\"parameters\" value=\"param_output_format=HTML%26ParRole=Middle Management\" />");
		message.append("   </PARAMETERS>");
		message.append("</SERVICE_REQUEST>");
	    */
        
		StringBuffer message = new StringBuffer();
		message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"scheduleJob\" ");
		message.append(" jobName=\"myjob\" ");
		message.append(" triggerName=\"mytrigger14\" ");
		message.append(" startDate=\"11/12/2006\" ");
		message.append(" startTime=\"17:48\" ");
		message.append(" endDate=\"11/12/2006\" ");
		message.append(" endTime=\"17:53\" ");
		message.append(" repeatInterval=\"10000\" ");
		message.append(" repeatCount=\"5\" ");
		message.append(">");
		message.append("   <PARAMETERS>");
		message.append("   	   <PARAMETER name=\"parameters\" value=\"param_output_format=HTML%26ParRole=Middle Management\" />");
		message.append("   </PARAMETERS>");
		message.append("</SERVICE_REQUEST>");
        
		
        AdapterAxisProxy proxy = null;
		try{
			proxy = new AdapterAxisProxy();
			proxy.setEndpoint("http://192.168.20.102:8080/spagobi/services/AdapterAxis");
			String response = proxy.service(message.toString());
			//SourceBean responseSB = SourceBean.fromXMLString(response, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
