package it.eng.spagobi.scheduler;

import it.eng.spago.base.SourceBean;
import it.eng.spago.soap.axis.client.AdapterAxisProxy;



public class SchedulerModuleTest {

	public static void main(String[] args) {
		
		AdapterAxisProxy proxy = null;
		StringBuffer message = new StringBuffer();
		message.append("<SERVICE_REQUEST PAGE=\"SchedulerPage\" task=\"schedulejob\" joblabel=\"label\" ");
		message.append(" jobname=\"name\" jobdescription=\"description\" />");
		try{
			proxy = new AdapterAxisProxy();
			proxy.setEndpoint("http://localhost:8080/spagobi/services/AdapterAxis");
			String response = proxy.service(message.toString());
			System.out.println(response);
			//SourceBean responseSB = SourceBean.fromXMLString(response, false);
			//System.out.println(responseSB.toXML(false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
