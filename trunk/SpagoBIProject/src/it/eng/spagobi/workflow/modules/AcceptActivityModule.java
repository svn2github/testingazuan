package it.eng.spagobi.workflow.modules;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.taskmgmt.exe.TaskInstance;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.module.AbstractModule;

public class AcceptActivityModule extends AbstractModule {

	public void service(SourceBean request, SourceBean response) throws Exception {
    	JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
    	JbpmContext jbpmContext = jbpmConfiguration.createJbpmContext();
    	String activityKeyIdStr = (String) request.getAttribute("ActivityKey");
		long activityKeyId = Long.valueOf(activityKeyIdStr).longValue();
		TaskInstance taskInstance = jbpmContext.getTaskInstance(activityKeyId);
		if (taskInstance.isOpen()) {
			//TODO che si fa se il task è già aperto???
		}
	}

}
