package it.eng.spagobi.workflow.modules;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class CompleteOrRejectActivityModule extends AbstractModule {

	public void service(SourceBean request, SourceBean response) throws Exception {
    	// This action handle both activity completion and activity reject 
		try{
			JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
	    	JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
	    	String activityKeyIdStr = (String) request.getAttribute("ActivityKey");
			long activityKeyId = Long.valueOf(activityKeyIdStr).longValue();
			TaskInstance taskInstance = jbpmContext.getTaskInstance(activityKeyId);
			ContextInstance contextInstance = taskInstance.getContextInstance();
	    	ProcessInstance processInstance = contextInstance.getProcessInstance();
			
	    	if (request.getAttribute("CompletedActivity") != null){
	    		// Submit buttin named CompleteActivity is pressed
	    		SpagoBITracer.info("Workflow", "CompleteOrRejectActivityModule", "service", "Completing Activity ["+ activityKeyId + "]");
	    		taskInstance.end();
	    	} else {
	    		//  Submit buttin named RejectActivity is pressed
	    		SpagoBITracer.info("Workflow", "CompleteOrRejectActivityModule", "service", "Completing Activity ["+ activityKeyId + "]");
	    		taskInstance.cancel();
	    	}
	    	jbpmContext.save(processInstance);
	    	jbpmContext.close();
		} catch (Exception e) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
					            "service", "Error during the complete or reject workflow activity", e);
			throw new EMFUserError(EMFErrorSeverity.ERROR, 100);
		}
	}

}
