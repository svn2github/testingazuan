package it.eng.spagobi.scheduler.publishers;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spagobi.presentation.publishers.GenericPublisher;

public class LovLookupAjaxPublisher extends GenericPublisher {
	
	public String getPublisherName(RequestContainer requestContainer, ResponseContainer responseContainer) {
		// get the module response
		SourceBean moduleResponse = (SourceBean)responseContainer.getServiceResponse().getAttribute("LovLookupAjaxModule");
		// return publisher
		return getPublisherName(requestContainer, responseContainer, moduleResponse);
	}
}
