package it.eng.spagobi.engines.commonj.services;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import commonj.work.Work;

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
			Object o=session.getAttribute("SBI_PROCESS_"+"lavoro");

			CommonjWorkContainer container=(CommonjWorkContainer)o;
			WorkManager wm=container.getWm();

			Work work=container.getWork();
			CommonjWorkListener listener=container.getListener();

			FooRemoteWorkItem fooRemoteWorkItem=(FooRemoteWorkItem)wm.runWithReturn(work, listener);
			container.setFooRemoteWorkItem(fooRemoteWorkItem);

			session.setAttribute("SBI_PROCESS_"+"lavoro", container);

			try {
				writeBackToClient( new JSONAcknowledge() );
			} catch (IOException e) {
				String message = "Impossible to write back the responce to the client";
				throw new SpagoBIEngineServiceException(getActionName(), message, e);
			}

		}
		catch (Exception e) {
			logger.error("Error in starting the work");
			throw SpagoBIEngineServiceExceptionHandler.getInstance().getWrappedException(getActionName(), getEngineInstance(), e);
		}
		logger.debug("OUT");
	}




}
