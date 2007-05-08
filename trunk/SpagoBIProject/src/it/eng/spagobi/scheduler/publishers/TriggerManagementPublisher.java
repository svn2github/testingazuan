package it.eng.spagobi.scheduler.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.presentation.publishers.GenericPublisher;

public class TriggerManagementPublisher extends GenericPublisher {
	
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		// get the module response
		SourceBean moduleResponse = (SourceBean)responseContainer.getServiceResponse().getAttribute("TriggerManagementModule");
		// return publisher
		return getPublisherName(requestContainer, responseContainer, moduleResponse);
	}
}
