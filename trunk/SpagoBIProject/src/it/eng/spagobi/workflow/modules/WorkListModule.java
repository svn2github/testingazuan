/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.workflow.modules;

import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.dbaccess.sql.DataRow;
import it.eng.spago.dispatching.module.list.basic.AbstractBasicListModule;
import it.eng.spago.dispatching.service.RequestContextIFace;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.init.InitializerIFace;
import it.eng.spago.paginator.basic.ListIFace;
import it.eng.spago.paginator.basic.PaginatorIFace;
import it.eng.spago.paginator.basic.impl.GenericList;
import it.eng.spago.paginator.basic.impl.GenericPaginator;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.utilities.SpagoBITracer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jbpm.JbpmConfiguration;
import org.jbpm.JbpmContext;
import org.jbpm.context.exe.ContextInstance;
import org.jbpm.graph.def.ProcessDefinition;
import org.jbpm.graph.exe.ProcessInstance;
import org.jbpm.taskmgmt.exe.TaskInstance;

public class WorkListModule extends AbstractBasicListModule {

	protected JbpmContext jbpmContext = null;
	
	/**
	 * Get the list of the assignments for the user in current session and give the list of the assignment
	 * as a sourceBean that will be used by a Spago List Tag
	 * @return The list of assigment as a SourceBean 
	 * @throws Exception if some errors occurs
	 */
	private SourceBean getAssigments() throws Exception {
		
		// Empty task list definition
    	List taskList = new ArrayList();
    	try{
	    	//Getting Containers
	    	SessionContainer session = getRequestContainer().getSessionContainer();
	    	SessionContainer permSession = session.getPermanentContainer();
	    	//ApplicationContainer application = ApplicationContainer.getInstance();
	    	IEngUserProfile userProfile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
	    	//Getting Jbpm context 
	    	JbpmConfiguration jbpmConfiguration = JbpmConfiguration.getInstance();
	    	jbpmContext = jbpmConfiguration.createJbpmContext();
	    	//Getting user roles
	    	Collection roles = userProfile.getRoles();
	    	//For each user role, find the task list associated
	    	Iterator rolesIt = roles.iterator();
	    	while (rolesIt.hasNext()) {
	    		String role = (String) rolesIt.next();
	    		List tmpTaskList = jbpmContext.getTaskList(role);
	    		Iterator iterTaskInst = tmpTaskList.iterator();
	    		while(iterTaskInst.hasNext()) {
	    			TaskInstance ti = (TaskInstance)iterTaskInst.next();
	    			taskList.add(ti);
	    		}
	    	}
    	} catch (Exception e) {
    		SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
    							"getAssigments", "Cannot recover assignment", e);
    	}
    	SourceBean workListAsSourceBean = tasksToSourceBean(taskList);
    	jbpmContext.close();
    	return workListAsSourceBean;
    	
    	
    	
//    	GraphSession graphSession = jbpmContext.getGraphSession();
//    	List processDefinitions = graphSession.findAllProcessDefinitions();
//    	if (processDefinitions == null) return null;
//    	Iterator itProcessDefinitions = processDefinitions.iterator();
//    	while (itProcessDefinitions.hasNext()) {
//    		ProcessDefinition aProcessDefinition = (ProcessDefinition) itProcessDefinitions.next();
//    		long aProcessDefinitionId = aProcessDefinition.getId();
//    		List processInstances = graphSession.findProcessInstances(aProcessDefinitionId);
//    		if (processInstances == null) continue;
//    		Iterator itProcessInstances = processDefinitions.iterator();
//    		while (itProcessInstances.hasNext()) {
//    			ProcessInstance aProcessInstance = (ProcessInstance) itProcessInstances.next();
//    			.....
//    		}
//    	}
    	//Getting Workflow Engine 
    	//IWorkflowEngine wfEngine = (IWorkflowEngine)application.getAttribute("WfEngine");
    	//Getting Connection From Workflow
    	//IWorkflowConnection wfConnection = wfEngine.getWorkflowConnection();
    	// Open the connection
    	//wfConnection.open((String)userProfile.getUserUniqueIdentifier(), (String)userProfile.getUserAttribute("password"));
        //List worklist = wfConnection.getAssignments();
        // Transform List to sourceBean
        //SourceBean workListAsSourceBean = assignmentsToSourceBean(worklist);
        //return workListAsSourceBean;
        
	}
	
	/**
	 * Map a java.util.List of assignments to a source bean
	 * @param aWorkList - The java.util.List of the assignment
	 * @return The source bean structure of the Assignments List
	 * @throws SourceBeanException if some errors occurs in the source bean population
	 * @throws WorkflowEngineException if some errors occurs
	 */
	protected SourceBean tasksToSourceBean (List aWorkList) throws SourceBeanException {
		SourceBean rows = new SourceBean("ROWS");
        
		for (Iterator it = aWorkList.iterator(); it.hasNext();){
        	rows.setAttribute(adapt((TaskInstance)it.next()));
        }
        
        return rows; 
	}

	/** Map a single assignment to a source bean
	 * @param wfAssignment The assignment to map to the source bean
	 * @return the source bean form of the assignment
	 * @throws SourceBeanException if some errors occurs in the source bean population
	 * @throws WorkflowEngineException if some errors occurs
	 */
	protected SourceBean adapt(TaskInstance aTaskInstance) throws SourceBeanException {
		SourceBean row = new SourceBean(DataRow.ROW_TAG);
		row.setAttribute("ActivityDescription", aTaskInstance.getDescription() == null ? "" :  aTaskInstance.getDescription());
		row.setAttribute("ActivityKey", String.valueOf(aTaskInstance.getId()));
		row.setAttribute("ActivityName", aTaskInstance.getName() == null ? "" : aTaskInstance.getName());
		row.setAttribute("ActivityPriority", String.valueOf(aTaskInstance.getPriority()));
		row.setAttribute("ActivityState", aTaskInstance.isOpen() ? "Open" : "Closed"); // ??????????
		ContextInstance contextInstance = aTaskInstance.getContextInstance();
		ProcessInstance processInstance = contextInstance.getProcessInstance();
		ProcessDefinition processDefinition = processInstance.getProcessDefinition();
		row.setAttribute("ProcessDescription", ""); // ??????????
		row.setAttribute("ProcessKey", String.valueOf(processDefinition.getId()));
		row.setAttribute("ProcessName", processDefinition.getName());
		row.setAttribute("ProcessState", processInstance.isSuspended() ? "Suspended" : "In execution");
		row.setAttribute("Accepted", aTaskInstance.isOpen()  ? "Open" : "Closed"); // ??????????
		row.setAttribute("TargetForm", ""); // ??????????
//		row.setAttribute("ActivityDescription",(Object)wfAssignment.getActivityDescription());
//		row.setAttribute("ActivityKey",wfAssignment.getActivityKey());
//		row.setAttribute("ActivityName",wfAssignment.getActivityName());
//		row.setAttribute("ActivityPriority",new Integer(wfAssignment.getActivityPriority()));
//		row.setAttribute("ActivityState",wfAssignment.getActivityState());
//		row.setAttribute("ProcessDescription",wfAssignment.getProcessDescription());
//		row.setAttribute("ProcessKey",wfAssignment.getProcessKey());
//		row.setAttribute("ProcessName",wfAssignment.getProcessName());
//		row.setAttribute("ProcessState",wfAssignment.getProcessState());
//		row.setAttribute("Accepted",new Boolean(wfAssignment.isAccepted()));
//		row.setAttribute("TargetForm",wfAssignment.getMappedForm());
		return row;
		
	}
	
	/**
	 * Ovverided to code the logic to get the worklist from workflow engine 
	 */
	public ListIFace getList(SourceBean arg0, SourceBean arg1) throws Exception {
		PaginatorIFace paginator = new GenericPaginator();
		InitializerIFace serviceInitializer = (InitializerIFace) this;
		
		int pagedRows = 10;
		
		
		paginator.setPageSize(pagedRows);
		
		RequestContextIFace serviceRequestContext = (RequestContextIFace) this;
		
		// Chiamata al workflow
		SourceBean rowsSourceBean = getAssigments();
		
		
		List rowsVector = null;
		if (rowsSourceBean != null)
			rowsVector = rowsSourceBean.getAttributeAsList(DataRow.ROW_TAG);
		if ((rowsSourceBean == null)) {//|| (rowsVector.size() == 0)) {
			EMFErrorHandler engErrorHandler = serviceRequestContext.getErrorHandler();
			engErrorHandler.addError(new EMFUserError(EMFErrorSeverity.INFORMATION, 10001));
		} // if ((rowsSourceBean == null) || (rowsVector.size() == 0))
		else{
			for (int i = 0; i < rowsVector.size(); i++)
				paginator.addRow(rowsVector.get(i));
		}
		ListIFace list = new GenericList();
		list.setPaginator(paginator);
		// list.addStaticData(firstData);
		return list;
	} // public static ListIFace getList(ServiceIFace service, SourceBean)

}
