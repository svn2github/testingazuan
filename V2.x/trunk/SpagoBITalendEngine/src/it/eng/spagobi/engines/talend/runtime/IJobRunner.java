/**

 LICENSE: see COPYING file
  
**/
package it.eng.spagobi.engines.talend.runtime;

import it.eng.spagobi.engines.talend.exception.ContextNotFoundException;
import it.eng.spagobi.engines.talend.exception.JobExecutionException;
import it.eng.spagobi.engines.talend.exception.JobNotFoundException;
import it.eng.spagobi.utilities.callbacks.audit.AuditAccessUtils;

import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * @author Andrea Gioia
 *
 */
public interface IJobRunner {
	
	/**
	 * Sets the session.
	 * 
	 * @param session the new session
	 */
	public abstract void setSession(HttpSession session);
	
	 /**
 	 * Run.
 	 * 
 	 * @param job the job
 	 * @param parameters the parameters
 	 * @param auditAccessUtils the audit access utils
 	 * @param auditId the audit id
 	 * 
 	 * @throws JobNotFoundException the job not found exception
 	 * @throws ContextNotFoundException the context not found exception
 	 * @throws JobExecutionException the job execution exception
 	 */
 	public abstract void run(Job job, Map parameters, AuditAccessUtils auditAccessUtils, String auditId) 
	 	throws JobNotFoundException, ContextNotFoundException, JobExecutionException;

}
