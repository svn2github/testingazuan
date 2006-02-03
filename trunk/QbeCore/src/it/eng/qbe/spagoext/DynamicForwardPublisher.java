/*
 * Created on 2-feb-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package it.eng.qbe.spagoext;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.presentation.PublisherDispatcherIFace;

/**
 * @author Andrea Zoppello
 * This class implements a SpagoBublisher that determine next Publisher assking the
 * service request for a parameter called PUBLISHER_NAME
 */
public class DynamicForwardPublisher implements PublisherDispatcherIFace {
	/**
	 *
	 */
	public DynamicForwardPublisher() {
		super();

	}

	public String getPublisherName(RequestContainer request,
			ResponseContainer response) {

		String navPar = (String) request.getServiceRequest().getAttribute(
				"PUBLISHER_NAME");
		if (navPar != null) {
			return navPar;
		} else {
			return "SERVICE_ERROR_PUBLISHER";
		}
	}
}

