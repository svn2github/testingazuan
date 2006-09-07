package it.eng.spagobi.booklets.automatictasks;

import it.eng.spagobi.booklets.constants.BookletsConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.List;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.db.GraphSession;
import org.jbpm.graph.def.ActionHandler;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.graph.exe.ProcessInstance;

public class ExceptionHandlerAction implements ActionHandler {

	public void execute(ExecutionContext context) throws Exception {
		
		ProcessInstance procInst = context.getProcessInstance();
		procInst.end();
		ProcessDefinition processDefinition = context.getProcessDefinition();
		JbpmContext jbpmContext = JbpmConfiguration.getInstance().createJbpmContext();
		GraphSession graphSession = jbpmContext.getGraphSession();
		//graphSession.deleteProcessInstance(procInst.getId());
		List procInsts = graphSession.findProcessInstances(processDefinition.getId());
		if(procInsts.isEmpty()) {
			graphSession.deleteProcessDefinition(processDefinition.getId());
		}
		Exception e = (Exception)context.getException();
        SpagoBITracer.major(BookletsConstants.NAME_MODULE, this.getClass().getName(), 
        		            "execute", "Error during process flow", e); 
        throw e;
	}

}