package it.eng.spagobi.engines.commonj.services;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.json.JSONObject;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkItem;

import de.myfoo.commonj.work.FooRemoteWorkItem;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkContainer;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkListener;
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.service.JSONSuccess;
import it.eng.spagobi.utilities.threadmanager.WorkManager;


public class StatusWorkAction extends AbstractEngineAction {

	private static transient Logger logger = Logger.getLogger(StatusWorkAction.class);


	@Override
	public void init(SourceBean config) {
		// TODO Auto-generated method stub
		super.init(config);
	}

	@Override
	public void service(SourceBean request, SourceBean response) {
		logger.debug("IN");
		super.service(request, response);
		JSONObject info=null;
		String document_id=null;
		Object document_idO=null;
		document_idO=request.getAttribute("DOCUMENT_ID");
		if(document_id!=null){
			document_id=document_idO.toString();
		}
		else{
			document_id="308";
		}

		HttpSession session=getHttpSession();

		//recover from session
		Object o=session.getAttribute("SBI_PROCESS_"+document_id);
		try{
			info = new JSONObject();
			int statusRemote;
			int statusWI;

			if(o!=null){

				CommonjWorkContainer container=(CommonjWorkContainer)o;

				FooRemoteWorkItem fooRwi=container.getFooRemoteWorkItem();
				WorkItem wi=container.getWorkItem();

				statusRemote=fooRwi.getStatus();
				statusWI=wi.getStatus();
				
				// if finds that work is finished delete the attribute from session
				if(statusWI==WorkEvent.WORK_COMPLETED){
					logger.debug("Work is finished - remove from session");
					session.removeAttribute("SBI_PROCESS_"+document_id);
				}
			}
			else{
				// No more present in session, so it has been deleted
				statusWI=WorkEvent.WORK_COMPLETED;
			}

			info.put("status", Integer.valueOf(statusWI));
			info.put("time", (new Date()).toString());


			try {
				writeBackToClient( new JSONSuccess(info));
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}

		}
		catch (Exception e) {
			logger.error("Error in Stopping the work");
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), e);
		}	
		logger.debug("OUT");

	}

}
