package it.eng.spagobi.workflow.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.presentation.PublisherDispatcherIFace;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class AcceptActivityPublisher implements PublisherDispatcherIFace {

	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		SourceBean request = requestContainer.getServiceRequest();
    	JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
    	JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
    	String activityKeyIdStr = (String) request.getAttribute("ActivityKey");
		long activityKeyId = Long.valueOf(activityKeyIdStr).longValue();
		TaskInstance taskInstance = jbpmContext.getTaskInstance(activityKeyId);
		taskInstance.start();
		String publisherName = taskInstance.getVariable("spago_handler").toString(); 
		return publisherName;
	}

}
