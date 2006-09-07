package it.eng.spagobi.workflow.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spagobi.utilities.SpagoBITracer;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class CompleteOrRejectActivityModule extends AbstractModule {

	public void service(SourceBean request, SourceBean response) throws Exception {
    	// This action handle both activity completion and activity reject 
		
		JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
    	JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
    	String activityKeyIdStr = (String) request.getAttribute("ActivityKey");
		long activityKeyId = Long.valueOf(activityKeyIdStr).longValue();
		TaskInstance taskInstance = jbpmContext.getTaskInstance(activityKeyId);
    	
    	if (request.getAttribute("CompletedActivity") != null){
    		// Submit buttin named CompleteActivity is pressed
    		SpagoBITracer.info("Workflow", "CompleteOrRejectActivityModule", "service", "Completing Activity ["+ activityKeyId + "]");
    		taskInstance.end();
    	} else {
    		//  Submit buttin named RejectActivity is pressed
    		SpagoBITracer.info("Workflow", "CompleteOrRejectActivityModule", "service", "Completing Activity ["+ activityKeyId + "]");
    		taskInstance.cancel();
    	}
	}

}
