package it.eng.spagobi.engines.commonj.services;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkItem;

import de.myfoo.commonj.work.FooRemoteWorkItem;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkContainer;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkListener;
import it.eng.spagobi.engines.commonj.runtime.WorksRepository;
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.threadmanager.WorkManager;


public class StartWorkAction extends AbstractEngineAction {

	private static transient Logger logger = Logger.getLogger(StartWorkAction.class);



	@Override
	public void service(SourceBean request, SourceBean response) {
		logger.debug("IN");
		super.service(request, response);
		HttpSession session=getHttpSession();

		//recover from session
		try{

			String document_id=null;
			Object document_idO=null;
			document_idO=request.getAttribute("DOCUMENT_ID");
			if(document_id!=null){
				document_id=document_idO.toString();
			}
			else{
				document_id="308";
			}

			Object o=session.getAttribute("SBI_PROCESS_"+document_id);

			CommonjWorkContainer container=(CommonjWorkContainer)o;
			WorkManager wm=container.getWm();

			Work work=container.getWork();
			CommonjWorkListener listener=container.getListener();

			FooRemoteWorkItem fooRemoteWorkItem=wm.buildFooRemoteWorkItem(work, listener);
			container.setFooRemoteWorkItem(fooRemoteWorkItem);
			if(fooRemoteWorkItem.getStatus()==WorkEvent.WORK_ACCEPTED){
				WorkItem workItem=(WorkItem)wm.runWithReturnWI(work, listener);
				container.setWorkItem(workItem);
//				// TODO: Put timeou
//				while(workItem.getStatus()!=WorkEvent.WORK_STARTED){
//				status=workItem.getStatus();
//				Thread.sleep(2000);
//				}
				session.setAttribute("SBI_PROCESS_"+document_id, container);

				try {
					writeBackToClient( new JSONAcknowledge() );
				} catch (IOException e) {
					String message = "Impossible to write back the responce to the client";
					throw new SpagoBIEngineServiceException(getActionName(), message, e);
				}

			}
		}
		catch (Exception e) {
			logger.error("Error in starting the work");
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), e);
		}
		logger.debug("OUT");
	}




}
