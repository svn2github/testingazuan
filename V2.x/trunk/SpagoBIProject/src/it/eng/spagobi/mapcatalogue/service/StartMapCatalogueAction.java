package it.eng.spagobi.mapcatalogue.service;

import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import org.apache.log4j.Logger;


public class StartMapCatalogueAction extends AbstractHttpAction {
	private static transient Logger logger = Logger.getLogger(StartMapCatalogueAction.class);
	
	public void service(SourceBean request, SourceBean response) throws Exception {
		logger.info("service called");
	}

}
