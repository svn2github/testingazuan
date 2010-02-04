package it.eng.spagobi.engines.commonj.services;

import java.io.IOException;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import commonj.work.Work;

import de.myfoo.commonj.work.FooRemoteWorkItem;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkContainer;
import it.eng.spagobi.engines.commonj.runtime.CommonjWorkListener;
import it.eng.spagobi.utilities.engines.AbstractEngineAction;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceException;
import it.eng.spagobi.utilities.engines.SpagoBIEngineServiceExceptionHandler;
import it.eng.spagobi.utilities.service.JSONAcknowledge;
import it.eng.spagobi.utilities.threadmanager.WorkManager;


public class StopWorkAction extends AbstractEngineAction {

	private static transient Logger logger = Logger.getLogger(StopWorkAction.class);


	@Override
	public void init(SourceBean config) {
		// TODO Auto-generated method stub
		super.init(config);
	}

	@Override
	public void service(SourceBean request, SourceBean response) {
		logger.debug("IN");
		super.service(request, response);
		String document_id=null;
		Object document_idO=null;
		document_idO=request.getAttribute("DOCUMENT_ID");
		if(document_id!=null){
			document_id=document_idO.toString();
		}
		else{
			document_id="";
		}
		
		HttpSession session=getHttpSession();

		//recover from session
		Object o=session.getAttribute("SBI_PROCESS_"+document_id);
		try{
			if(o!=null){

				CommonjWorkContainer container=(CommonjWorkContainer)o;

				FooRemoteWorkItem fooRwi=container.getFooRemoteWorkItem();
				// release the resource
				fooRwi.release();
//				remove the attribute
				session.removeAttribute("SBI_PROCESS_"+document_id);
			}
			try {
				writeBackToClient( new JSONAcknowledge() );
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
