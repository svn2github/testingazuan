package it.eng.spagobi.scheduler.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFErrorHandler;
import it.eng.spago.error.EMFErrorSeverity;
import it.eng.spago.error.EMFUserError;
import it.eng.spago.presentation.PublisherDispatcherIFace;
import it.eng.spagobi.constants.SpagoBIConstants;
import it.eng.spagobi.importexport.ImportExportConstants;
import it.eng.spagobi.utilities.GeneralUtilities;
import it.eng.spagobi.utilities.SpagoBITracer;

public class SchedulerGUIPublisher implements PublisherDispatcherIFace {
	
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		
		EMFErrorHandler errorHandler = responseContainer.getErrorHandler();
		// get the module response
		SourceBean moduleResponse = (SourceBean)responseContainer.getServiceResponse().getAttribute("SchedulerGUIModule");
		// if the module response is null throws an error and return the name of the errors publisher
		if(moduleResponse==null) {
			SpagoBITracer.major(SpagoBIConstants.NAME_MODULE, this.getClass().getName(), 
		            			"getPublisherName", "Module response null");
			EMFUserError error = new EMFUserError(EMFErrorSeverity.ERROR, 100 );
			errorHandler.addError(error);
			return "error";
		}
		
		// if there are some errors into the errorHandler return the name for the errors publisher
		if(!errorHandler.isOKBySeverity(EMFErrorSeverity.ERROR)) {
			return new String("error");
		}
	
		String pubName = (String)moduleResponse.getAttribute(SpagoBIConstants.PUBLISHER_NAME);
		if((pubName!=null) && !(pubName.trim().equals(""))) {
			return pubName;
		} else {
			return new String("SchedulerGUIPub");
		}
	
	}
}
