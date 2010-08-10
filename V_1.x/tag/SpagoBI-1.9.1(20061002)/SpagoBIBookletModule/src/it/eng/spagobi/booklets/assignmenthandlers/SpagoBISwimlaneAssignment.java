package it.eng.spagobi.booklets.assignmenthandlers;

import org.jbpm.graph.exe.ExecutionContext;
import org.jbpm.taskmgmt.def.AssignmentHandler;
import org.jbpm.taskmgmt.exe.Assignable;
import org.jbpm.taskmgmt.exe.SwimlaneInstance;

public class SpagoBISwimlaneAssignment implements AssignmentHandler {


	public void assign(Assignable ass, ExecutionContext arg1) throws Exception {
		SwimlaneInstance swimlaneInstance = (SwimlaneInstance)ass;
		String name = swimlaneInstance.getName();
		ass.setActorId(name);
	}

}
